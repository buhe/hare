/**
 * 
 */
package org.buhe.hare.handler.job.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.buhe.hare.common.cluster.zk.ZooKeeperWatcher;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.Jobs;
import org.buhe.hare.handler.job.JobRegistry;

/**
 * @author buhe
 *
 */
public class ZkJobRegistry implements JobRegistry {
	private final static Log LOG = LogFactory.getLog(ZkJobRegistry.class);
	private ZooKeeperWatcher watcher;
	private ConfigurationImpl configuration;

	public ZkJobRegistry(ConfigurationImpl configuration,ZooKeeperWatcher watcher){
		this.configuration = configuration;
		this.watcher = watcher;
	}
	
	@Override
	public boolean regisiterJob(Job job) {
		String jobTypeNode = getJobLevel(job);
		String node = Jobs.createJobZnode(jobTypeNode,job);
		try {
			watcher.getZooKeeperClient().createEphemeralNode(node, null);
		} catch (KeeperException e) {
			LOG.error(e);
			return false;
		} catch (InterruptedException e) {
			LOG.error(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean unregisiterJob(Job job) {
		String jobTypeNode = getJobLevel(job);
		String node = Jobs.createJobZnode(jobTypeNode,job);
		try {
			watcher.getZooKeeperClient().delete(node);
		} catch (InterruptedException e) {
			LOG.error(e);
			return false;
		} catch (KeeperException e) {
			LOG.error(e);
			return false;
		}
		return true;
	}

	private String getJobLevel(Job job) {
		//其实在这里注册的都应该是Crucital,否则不应该和Master通信
		String jobTypeNode;
		if(job.jobContext().isCrucial()){
			jobTypeNode = configuration.getPersistJobZnode();
		}else {
			jobTypeNode = configuration.getEphemeralJobZnode();
		}
		return jobTypeNode;
	}
}
