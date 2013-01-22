/**
 * 
 */
package org.buhe.hare.master.jobtracker.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.buhe.hare.common.HandlerService;
import org.buhe.hare.common.cluster.zk.ZooKeeperListener;
import org.buhe.hare.common.cluster.zk.ZooKeeperWatcher;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.JobStatus;
import org.buhe.hare.common.job.support.Jobs;
import org.buhe.hare.common.metadata.HanlderMetadata;
import org.buhe.hare.master.HandlerClusterChangeListener;
import org.buhe.hare.master.HareMaster;
import org.buhe.hare.master.jobtracker.JobTracker;
import org.buhe.hare.master.rpc.HandlerLocator;


/**
 * 用ZooKeeper实现任务的跟踪和持久化
 * 
 * 需要跟踪的任务注册到zk
 * 
 * @author buhe
 * 
 */
public class ZkJobTracker implements JobTracker, HandlerClusterChangeListener {
	private static final int DEFAULT_RETRY = 5;
	private static final int DEFAULT_RETRY_INTERVAL = 2 * 1000;
	private final static Log LOG = LogFactory.getLog(ZkJobTracker.class);
	private ZooKeeperWatcher zk;
	private HareMaster master;
	// 同步RPC的预提交列表
	private Map<String, Job> prepareJobs = new HashMap<String, Job>();
	// //ZooKeeper通知的预提交列表
	// private Set<String> zkPrepareJobs = new HashSet<String>();
	// 上述两个预提交列表都存在则,任务被提交
	private Map<String, Job> commitedJobs = new HashMap<String, Job>();
	private Map<String, Job> canceledJobs = new HashMap<String, Job>();
	private Map<String, Job> finishJobs = new HashMap<String, Job>();
	private HandlerLocator handlerLocator;
	private Executor zkEventExecutor = Executors.newFixedThreadPool(5); // TODO
																		// - 1-
																		// 3?
																		// Factory
	private ConfigurationImpl configuration;

	public ZkJobTracker(ConfigurationImpl configuration, ZooKeeperWatcher zk,
			HareMaster master) {
		this.zk = zk;
		this.master = master;
		this.configuration = configuration;
		initRootNode();
		registerZookeeperListener();
		handlerLocator = new HandlerLocator(configuration);
	}

	private void registerZookeeperListener() {
		ZooJobListener l = new ZooJobListener(zk);
		zk.registerListener(l);
		try {
			zk.getZooKeeperClient().getChildrenAndWatch(
					configuration.getPersistJobZnode());
			zk.getZooKeeperClient().getChildrenAndWatch(
					configuration.getEphemeralJobZnode());
		} catch (KeeperException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	private void initRootNode() {
		try {
			if (zk.getZooKeeperClient().exists(
					configuration.getPersistJobZnode()) == null) {
				zk.getZooKeeperClient().createPersistentNode(
						configuration.getPersistJobZnode(), null);
			}
			if (zk.getZooKeeperClient().exists(
					configuration.getEphemeralJobZnode()) == null) {
				zk.getZooKeeperClient().createPersistentNode(
						configuration.getEphemeralJobZnode(), null);
			}
		} catch (KeeperException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		}

	}

	/**
	 * 预备Job
	 */
	@Override
	public boolean prepare(Job job) {
		// TODO
		// 1. 分配给Handler
		// 2. 成功后Handler注册到ZK
		// 3. 收到成功消息后(zk和返回值都正确)持久化 prepare / commit

		// 预提交
		synchronized (prepareJobs) {
			prepareJobs.put(job.id(), job);
			LOG.info("PREPARE Job ( " + job.id() + " )");
		}
		int retry = 0;
		HandlerService handler = null;
		while (true) {
			try {
				handler = lookupHandler(job);
			} catch (NotAnyHandlerException e) {
				LOG.error("Try find handler fails.beasuse not alive handler.");
			} catch (Throwable ignore) {
			}
			if (handler != null || retry > DEFAULT_RETRY) {
				break;
			}
			try {
				Thread.sleep(DEFAULT_RETRY_INTERVAL);
				LOG.debug("Wait " + DEFAULT_RETRY_INTERVAL + " and retry.");
			} catch (InterruptedException e) {
				LOG.error(e);
			}
			retry++;
		}
		if (handler == null) {
			synchronized (prepareJobs) {
				// 不提交
				prepareJobs.remove(job.id());
			}
			synchronized (canceledJobs) {
				canceledJobs.put(job.id(), job);
			}

			LOG.error("Not alive handler server.");
			return false;
		}

		zkEventExecutor.execute(new PrepareAction(handler, job));
		return true;
	}

	/**
	 * 提交Job zk 回调
	 */
	@Override
	public void commit(String id) {
		tryCommit(id);
	}

	/**
	 * 任务非法结束,重新分配 {@link #prepare(Job)} 由zk发起
	 */
	@Override
	public void cancel(String id) {
		// TODO Auto-generated method stub
		// 1 任务提交后被取消,重新分配给其他Handler
		// 2 任务没被提交,但是prepareJobs 中有,这是zk消息乱了? 记录日志
		synchronized (commitedJobs) {
			if (commitedJobs.containsKey(id)) {
				Job job = commitedJobs.remove(id);
				zkEventExecutor.execute(new CancelAction(job));
			} else {
				LOG.error("Zk event error?");
			}
		}

	}

	/**
	 * 新Handler join,是否rebalance,由zk发起@see HandlerClusterChangeListener
	 */
	@Override
	public void rebalance(HanlderMetadata newHandler) {
		// TODO Auto-generated method stub

	}
	
	private HandlerService lookupHandler(Job job) throws NotAnyHandlerException{
		HanlderMetadata meta = master.resolveHandlerNode(job
				.neIdentifier());
		return handlerLocator.lookup(meta);
	}

	/**
	 * 尝试提交任务到任务列表
	 * 
	 * @param id
	 */
	// @ThreadSafe
	private void tryCommit(String id) {
		synchronized (prepareJobs) {
			if (prepareJobs.containsKey(id)) {
				// 可以提交
				synchronized (commitedJobs) {
					commitedJobs.put(id, prepareJobs.get(id));
					LOG.info("COMMIT Job ( " + id + " )");
				}
				prepareJobs.remove(id);
			}
		}
	}

	private class PrepareAction implements Runnable {
		private HandlerService handler;
		private Job job;

		public PrepareAction(HandlerService handler, Job job) {
			this.handler = handler;
			this.job = job;
		}

		@Override
		public void run() {
			boolean success = handler.execute(job);
			if (!success) {
				synchronized (prepareJobs) {
					// 不提交
					prepareJobs.remove(job.id());
				}
				synchronized (canceledJobs) {
					canceledJobs.put(job.id(), job);
				}
			}

		}

	}

	private class CancelAction implements Runnable {

		Job job;

		public CancelAction(Job job) {
			this.job = job;
		}

		@Override
		public void run() {
			// 重新提交
			if (job.jobContext().isCrucial()) {
				// 只有Crucial 才需要retry
				LOG.info("Cancel job " + job.id());
				LOG.info("Restart job " + job.id());
				prepare(job);
			}
		}
	}

	/**
	 * 根据负载重新分配任务
	 * 
	 * @author buhe
	 * 
	 */
	private class RebalanceAction implements Runnable {

		@Override
		public void run() {

		}
	}

	private class ZooJobListener extends ZooKeeperListener {

		public ZooJobListener(ZooKeeperWatcher watcher) {
			super(watcher);
		}

		public void nodeChildrenChanged(String node) {
			if (node.startsWith(configuration.getPersistJobZnode())) {
				// 持久化Job节点下的节点发生变化
				try {
					// 列出Job,并监视他们
					List<String> nodes = this.watcher.getZooKeeperClient()
							.listChildrenAndWatchThem(node);
					LOG.info("Current job : " + nodes);

					for (String job : nodes) {
						tryCommit(Jobs.parseJobId(job));
					}
				} catch (KeeperException e) {
					LOG.error(e);
				}
			}
		}

		@Override
		public void nodeDeleted(String node) {
			if (node.startsWith(configuration.getPersistJobZnode())) {
				LOG.info("Lose job " + node);
				cancel(Jobs.parseJobId(node));

			}

			if (node.startsWith(configuration.getEphemeralJobZnode())) {
				// 貌似不要处理
			}
		}

	}

	@Override
	public JobStatus getJobStatus(String id) {
		if (prepareJobs.containsKey(id) || commitedJobs.containsKey(id)) {
			return JobStatus.RUNNING;
		} else if (canceledJobs.containsKey(id)) {
			return JobStatus.FAIL;
		} else {
			return JobStatus.FINISH;
		}
	}

	@Override
	public void finish(String id) {
		synchronized (prepareJobs) {
			synchronized (commitedJobs) {
				Job removed = commitedJobs.remove(id);
				if (removed == null) {
					// 任务执行的太快了,没提交就结束了
					LOG.warn("Your job " + id
							+ "finish too soon.you can invoke it in local node");
					removed = prepareJobs.remove(id);
				}
				synchronized (finishJobs) {
					finishJobs.put(id, removed);
				}
			}
		}

	}

	@Override
	public Object getJobResult(String jobId) { //FIXME throws some
		synchronized (finishJobs) {
			Job finished = finishJobs.remove(jobId);
			try {
				//FIXME - 不能连续使用netty - rpc 调用
				HandlerService handler = this.lookupHandler(finished);
				return handler.getJobResult(jobId);
			} catch (NotAnyHandlerException e) {
				LOG.error(e);
				return null;
			}
		}
	}
}
