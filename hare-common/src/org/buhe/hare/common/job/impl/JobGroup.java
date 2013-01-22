/**
 * 
 */
package org.buhe.hare.common.job.impl;


import org.buhe.hare.common.HandlerService;
import org.buhe.hare.common.MasterInterface;
import org.buhe.hare.common.MasterService;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.JobFuture;
import org.buhe.hare.common.job.support.JobContext;
import org.buhe.hare.common.job.support.JobType;
import org.buhe.hare.common.metadata.NeIdentifier;


/**
 * 任务组,执行多个任务
 * 是否有必要?
 * @author buhe
 *
 */
public class JobGroup implements Job {

	private static final long serialVersionUID = -80894931078208328L;

	@Override
	public NeIdentifier neIdentifier() {
		// TODO 因为是组的概念,可能涉及多个网元..,balance 也是个难点
		return null;
	}

	@Override
	public void waitComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final JobType jobType() {
		return JobType.GROUP;
	}

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobContext jobContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void neIdentifier(NeIdentifier neIdentifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void localHandler(HandlerService handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientToMasterProxy(MasterInterface master) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlerToMasterProxy(MasterService master) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object result() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void future(JobFuture future) {
		// TODO Auto-generated method stub
		
	}

}
