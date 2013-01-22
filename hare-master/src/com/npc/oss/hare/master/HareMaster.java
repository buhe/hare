package com.npc.oss.hare.master;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;

import com.npc.oss.hare.common.MasterInterface;
import com.npc.oss.hare.common.MasterService;
import com.npc.oss.hare.common.cluster.zk.ZooKeeperListener;
import com.npc.oss.hare.common.cluster.zk.ZooKeeperWatcher;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;
import com.npc.oss.hare.common.job.Job;
import com.npc.oss.hare.common.job.support.JobStatus;
import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.metadata.Metadata;
import com.npc.oss.hare.common.metadata.NeIdentifier;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.common.util.Assert;
import com.npc.oss.hare.common.util.RefectHelper;
import com.npc.oss.hare.master.balance.Balancer;
import com.npc.oss.hare.master.jobtracker.JobTracker;
import com.npc.oss.hare.master.jobtracker.impl.ZkJobTracker;

/**
 * 
 * @author buhe
 * 
 */
public class HareMaster implements MasterService, MasterInterface, Runnable {

	private final static Log LOG = LogFactory.getLog(HareMaster.class);
	private ConfigurationImpl configuration;
	private List<String> handlers;
	private ZooKeeperWatcher watcher;
	private Metadata metaData = new Metadata();
	private Balancer balancer;
	private JobTracker jobTracker;

	public HareMaster(ConfigurationImpl configuration) throws IOException {
		this.configuration = configuration;
		watcher = new ZooKeeperWatcher(configuration);
		balancer = RefectHelper.NEW((Class<Balancer>) RefectHelper
				.loadClass(configuration.getBalancer()));
		jobTracker = new ZkJobTracker(configuration, watcher, this);
	}

	@Override
	public void run() {
		registerZookeeper();
		registerJvmHook();
		startMasterRpcServer();
		mainLoop();
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

	private void startMasterRpcServer() {
		HareRPC hRpc = new HareRPC(configuration);
		RpcConfiguration rpc = new RpcConfiguration(
				configuration.getMasteNodeIp(),
				configuration.getMasterNodePort(), "MASTER");
		hRpc.getRpcServer(rpc).bind(this);
	}

	private void registerJvmHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					watcher.getZooKeeperClient().deleteNode(
							configuration.getMasterZnode());
				} catch (InterruptedException e) {
					LOG.error(e);
				} catch (KeeperException e) {
					LOG.error(e);
				}
			}
		});
	}

	/**
	 * 注册自己到Zookeeper
	 */
	private void registerZookeeper() {
		try {

			watcher.registerListener(new MasterNodeListener(watcher));
			if (watcher.getZooKeeperClient().existsAndWatch(
					configuration.getMasterZnode()) != null) {
				// TODO - 未实现主备切换
				LOG.info("Master already started");
				// 先直接退出了
				System.exit(-1);
				return;
			}
			watcher.getZooKeeperClient().createEphemeralNode(
					configuration.getMasterZnode(), new byte[0]);
			if (watcher.getZooKeeperClient().existsAndWatch(
					configuration.getHandlerZnode()) == null) {
				watcher.getZooKeeperClient().createPersistentNode(
						configuration.getHandlerZnode(), new byte[0]);
			}
			this.handlers = watcher.getZooKeeperClient().getChildrenAndWatch(
					configuration.getHandlerZnode());
		} catch (KeeperException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		}

	}

	private class MasterNodeListener extends ZooKeeperListener {

		public MasterNodeListener(ZooKeeperWatcher watcher) {
			super(watcher);
		}

		public void nodeCreated(String node) {
			if (node.equals(configuration.getMasterZnode())) {
				LOG.info("Register Master Node");
				try {
					watcher.getZooKeeperClient().existsAndWatch(node);
				} catch (KeeperException e) {
					LOG.error(e);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}

		public void nodeChildrenChanged(String node) {
			if (node.equals(configuration.getHandlerZnode())) {
				try {
					List<String> handlers = watcher.getZooKeeperClient()
							.getChildrenAndWatch(node);
					HareMaster.this.handlers = handlers;
					LOG.info("Current handlers" + handlers);
					metaData.updateHandlers(handlers);
				} catch (KeeperException e) {
					LOG.error(e);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	@Override
	public boolean handlerServerStartup(HanlderMetadata neMetadata,
			long serverCurrentTime) {
		// TODO - to be continued
		metaData.addHandler(neMetadata);
		return true;
	}

	@Override
	public HanlderMetadata resolveHandlerNode(NeIdentifier neIdentifier) throws NotAnyHandlerException {
		Assert.assertNotNull("NeIdentifier should be NOT null", neIdentifier);
		HanlderMetadata md;
		if ((md = metaData.get(neIdentifier)) != null) {
			LOG.debug("Found exist " + neIdentifier + " in " + md);
			return md;
		} else {
			md = selectHandler(neIdentifier);
			LOG.debug("Select new " + md + " for " + neIdentifier);
			HanlderMetadata old = metaData.putIfAbsent(neIdentifier, md);
			if (old != null) {
				return old;
			} else {
				return md;
			}
		}
	}

	private HanlderMetadata selectHandler(NeIdentifier neIdentifier) throws NotAnyHandlerException {
		return balancer.select(metaData, neIdentifier);
	}

	@Override
	public Metadata findMetaData() { // TODO - find???..mmm 名字不好
		return metaData;
	}

	@Override
	public boolean execute(Job job) {
		return jobTracker.prepare(job);
	}

	@Override
	public JobStatus getJobStatus(String jobId) {
		return jobTracker.getJobStatus(jobId);
	}

	@Override
	public void finishJob(String id) {
		jobTracker.finish(id);
	}

	@Override
	public Object getJobResult(String jobId) {
		return jobTracker.getJobResult(jobId);
	}

	@Override
	public void stop() {
		//TODO
	}

}
