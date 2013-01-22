/**
 * 
 */
package org.buhe.hare.common.job.impl;

import java.util.UUID;

import org.buhe.hare.common.HandlerService;
import org.buhe.hare.common.MasterInterface;
import org.buhe.hare.common.MasterService;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.JobFuture;
import org.buhe.hare.common.job.support.JobContext;
import org.buhe.hare.common.job.support.JobType;
import org.buhe.hare.common.metadata.NeIdentifier;



/**
 * @author buhe
 *
 */
public abstract class AbstractJob implements Job {

	private static final long serialVersionUID = 905382958227945415L;
	protected NeIdentifier neIdentifier;
	protected JobContext ctx;
	protected String id;
	protected JobType jobType;
	//Job访问MasterInterface的代理,只在client端使用
	protected transient MasterInterface clientToMaster;
	protected transient HandlerService handler;
	//Handler访问Master的代理
	protected transient MasterService handlerToMaster;
	protected transient Object result;
	protected transient JobFuture future;
	
	public String id() {
		return id;
	}

	public JobType jobType() {
		return jobType;
	}

	public AbstractJob(JobContext ctx){
		this.ctx = ctx;
		this.id = UUID.randomUUID().toString();
	}
	

	@Override
	public NeIdentifier neIdentifier() {
		return neIdentifier;
	}
	
	public void neIdentifier(NeIdentifier neIdentifier){
		this.neIdentifier = neIdentifier;
	}
	
	@Override
	public JobContext jobContext(){
		return ctx;
	}
	
	@Override
	public void waitComplete(){
		//push or pull
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AbstractJob other = (AbstractJob) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public void clientToMasterProxy(MasterInterface master){
		this.clientToMaster = master;
	}
	
	@Override
	public void handlerToMasterProxy(MasterService master){
		this.handlerToMaster = master;
	}
	
	@Override
	public void localHandler(HandlerService handler){
		this.handler = handler;
	}

	@Override
	public void future(JobFuture future) {
		this.future = future;
	}
	
	@Override
	public Object result(){
		return result;
	}
	
}
