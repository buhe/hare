/**
 * 
 */
package org.buhe.hare.handler.job.impl;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.MasterService;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.HandlerConfiguration;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.JobType;
import org.buhe.hare.handler.HareHandler;
import org.buhe.hare.handler.job.JobManager;
import org.buhe.hare.handler.job.JobRegistry;
import org.jboss.netty.util.internal.ConcurrentHashMap;

/**
 * 管理提交到Handler的Job
 * 这里会根据job type放入两种队列中
 * 这里不管job是否是持久任务,由外部跟踪JobTracker 或者 调用者本身跟踪
 * @author buhe
 *
 */
public class JobManagerImpl implements JobManager{
	private final static Log LOG = LogFactory.getLog(JobManagerImpl.class);
	private ConfigurationImpl configuration;
	private ExecutorService longTimeJobPool;
	private ExecutorService shortTimeJobPool;
	private Set<Job> shortJobs = new CopyOnWriteArraySet<Job>();
	private Set<Job> longjobs = new CopyOnWriteArraySet<Job>();
	private JobRegistry jobRegistry;
	private MasterService master;
	private HareHandler handler;
	private HandlerConfiguration handlerConfiguration;
	private Map<String,Object> jobResults = new ConcurrentHashMap<String,Object>(shortJobs.size());
	
	public JobManagerImpl(ConfigurationImpl configuration,MasterService master,HareHandler handler){
		this.configuration = configuration;
		this.handlerConfiguration = configuration.getHandlerConfiguration();
		this.master = master;
		this.handler = handler;
		jobRegistry = new ZkJobRegistry(configuration,handler.getZooKeeperWatcher());
		longTimeJobPool = Executors.newFixedThreadPool(handlerConfiguration.getLongTimeJobCount());
		shortTimeJobPool = Executors.newFixedThreadPool(handlerConfiguration.getShortTimeJobCount());
	}
	@Override
	public void execute(Job job){
		job.localHandler(handler);
		job.handlerToMasterProxy(master);
		if(job.jobType() == JobType.ONCE){
			addShortJob(job);
		}else if(job.jobType() == JobType.CYCLE){
			addLongJob(job);
		}
		
		
	}
	
	@Override
	public void stop(Job job) {
		//去注册job,结束整个流程
		//cycle job 不会进来,因为不可能结束
		jobRegistry.unregisiterJob(job);
		//TODO - 超时检查停止?
		Object result = job.result();
		if(result != null){
			jobResults.put(job.id(), result);
		}
	}
	
	private void addLongJob(Job job) {
		synchronized (longjobs) {
			if(longjobs.size() >= handlerConfiguration.getLongTimeJobCount()){
				LOG.error("Long time job count more than " + handlerConfiguration.getLongTimeJobCount());
				return ;
			}
			boolean success = longjobs.add(job);
			if(!success){
				LOG.error("Submit duplicate job " + job);
				return ;
			}
		}
		longTimeJobPool.submit(job);
		//根据不同的级别(P or E) 注册到不同节点下
		jobRegistry.regisiterJob(job);
		
	}
	//--- short job ----暂时不实现
	private void addShortJob(Job job) {
		boolean success = shortJobs.add(job);
		shortTimeJobPool.submit(job);
		//根据不同的级别(P or E) 注册到不同节点下
		jobRegistry.regisiterJob(job);
		//TODO - 考虑直接移植LTE中的
	}
	@Override
	public Object getJobResult(String id) {
		return jobResults.remove(id);
	}
	
	
}
