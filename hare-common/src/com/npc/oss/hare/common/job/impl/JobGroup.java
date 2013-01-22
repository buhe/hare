/**
 * 
 */
package com.npc.oss.hare.common.job.impl;

import com.npc.oss.hare.common.HandlerService;
import com.npc.oss.hare.common.MasterInterface;
import com.npc.oss.hare.common.MasterService;
import com.npc.oss.hare.common.job.Job;
import com.npc.oss.hare.common.job.JobFuture;
import com.npc.oss.hare.common.job.support.JobContext;
import com.npc.oss.hare.common.job.support.JobType;
import com.npc.oss.hare.common.metadata.NeIdentifier;

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
