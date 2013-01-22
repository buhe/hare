package com.npc.oss.hare.handler;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;

import com.npc.oss.hare.common.HandlerInterface;
import com.npc.oss.hare.common.HandlerService;
import com.npc.oss.hare.common.MasterService;
import com.npc.oss.hare.common.cluster.zk.ZKUtil;
import com.npc.oss.hare.common.cluster.zk.ZooKeeperListener;
import com.npc.oss.hare.common.cluster.zk.ZooKeeperWatcher;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.job.Job;
import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.handler.job.JobManager;
import com.npc.oss.hare.handler.job.impl.JobManagerImpl;

/**
 * 处理器节点
 * 
 * @author buhe
 * 
 */
public class HareHandler implements HandlerInterface, HandlerService, Runnable {

	private static final Log LOG = LogFactory.getLog(HareHandler.class);
	private static final Object MASTER_LATCH = new Object();
	private ConfigurationImpl configuration;
	private ZooKeeperWatcher watcher;
	private String currentHandlerNodeName;
	private MasterService master;
	private JobManager jobManager;

	public HareHandler(ConfigurationImpl configuration) throws IOException {
		this.configuration = configuration;
		watcher = new ZooKeeperWatcher(configuration);
		currentHandlerNodeName = ZKUtil.joinZNode(
				configuration.getHandlerZnode()
				// TODO - H Constats ?
				, "H" + configuration.getHandlerNodeIp() + ":" + configuration.getHandlerNodePort());
		//TODO protocol
		RpcConfiguration rpc = new RpcConfiguration(configuration.getMasteNodeIp(),configuration.getMasterNodePort(),"MASTER");
		master = new HareRPC(configuration).getRpcClient(rpc)
				.getProxy(MasterService.class);
		jobManager = new JobManagerImpl(configuration, master, this);
	}

	@Override
	public void run() {
		try {

			waitMasterNode(watcher);
			LOG.info("Found Master Node.");
			registerHandlerNode(watcher);
			registerJvmHook();
			startHandlerRpcServer();
			reportMasterNode();
			mainLoop();
		} catch (IOException e) {
			LOG.error(e);
		} catch (KeeperException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		}

	}
	private void mainLoop() {
		while(true){
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void startHandlerRpcServer() {
		HareRPC hRpc = new HareRPC(configuration);
		RpcConfiguration rpc = new RpcConfiguration(
				configuration.getHandlerNodeIp(),
				configuration.getHandlerNodePort(), "HANDLER");
		hRpc.getRpcServer(rpc).bind(this);
		
	}

	private void registerHandlerNode(ZooKeeperWatcher watcher)
			throws KeeperException, InterruptedException, UnknownHostException {

		if (watcher.getZooKeeperClient().existsAndWatch(currentHandlerNodeName) != null) {
			LOG.error(currentHandlerNodeName + "has EXISTS");
			return;
		}
		watcher.getZooKeeperClient().createEphemeralNode(
				currentHandlerNodeName, new byte[0]);
	}

	private void reportMasterNode() throws UnknownHostException {
		HanlderMetadata handleMetadata = new HanlderMetadata(configuration.getHandlerNodeIp());
		handleMetadata.addPort(HanlderMetadata.JOB_SCHEDULE_PROTOCOL, configuration.getHandlerNodePort());
		
		boolean result = master.handlerServerStartup(handleMetadata,
				System.currentTimeMillis());
		LOG.info("Report MN " + (result ? "sucessfull" : "fail"));
	}

	private void waitMasterNode(ZooKeeperWatcher watcher) throws IOException,
			KeeperException, InterruptedException {

		watcher.registerListener(new MasterNodeListener(watcher));
		while (true) {
			if (watcher.getZooKeeperClient().existsAndWatch(
					configuration.getMasterZnode()) != null) {
				return;
			}
			synchronized (MASTER_LATCH) {
				MASTER_LATCH.wait(1000);
			}
		}
	}

	private void registerJvmHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					watcher.getZooKeeperClient().deleteNode(
							currentHandlerNodeName);
				} catch (InterruptedException e) {
					LOG.error(e);
				} catch (KeeperException e) {
					LOG.error(e);
				}
			}
		});
	}

	private class MasterNodeListener extends ZooKeeperListener {

		public MasterNodeListener(ZooKeeperWatcher watcher) {
			super(watcher);
		}

		public void nodeCreated(String node) {
			if (node.equals(configuration.getMasterZnode())) {
				LOG.info("Master Node started");
				try {
					watcher.getZooKeeperClient().existsAndWatch(node);
				} catch (KeeperException e) {
					LOG.error(e);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
				synchronized (MASTER_LATCH) {
					MASTER_LATCH.notifyAll();
				}
			}
		}

		public void nodeDeleted(String node) {
			if (node.equals(configuration.getMasterZnode())) {
				LOG.info("Master Node stoped");
				try {
					watcher.getZooKeeperClient().existsAndWatch(node);
				} catch (KeeperException e) {
					LOG.error(e);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	@Override
	public boolean execute(Job job) {
		//TODO - 这里直觉应该有某种机制
		jobManager.execute(job);
		return true;
	}
	
	public ZooKeeperWatcher getZooKeeperWatcher(){
		return watcher;
	}

	@Override
	public void stop(Job job) {
		jobManager.stop(job);
	}

	@Override
	public Object getJobResult(String jobId) {
		return jobManager.getJobResult(jobId);
	}

	@Override
	public void stop() {
		// TODO
		
	}

}
