/**
 * 
 */
package com.npc.oss.hare.common.job;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.npc.oss.hare.common.HandlerService;
import com.npc.oss.hare.common.MasterInterface;
import com.npc.oss.hare.common.MasterService;
import com.npc.oss.hare.common.job.support.JobContext;
import com.npc.oss.hare.common.job.support.JobType;
import com.npc.oss.hare.common.metadata.NeIdentifier;

/**
 * @author buhe
 *
 */
public interface Job extends Callable<Object>,Serializable{


	NeIdentifier neIdentifier();
	
	void neIdentifier(NeIdentifier neIdentifier);
	
	JobContext jobContext();
	
	void waitComplete();
	
	JobType jobType();
	
	String id();
	
	void clientToMasterProxy(MasterInterface master);
	
	void handlerToMasterProxy(MasterService master);
	
	void localHandler(HandlerService handler);
	
	Object getResult();
	
	Object result();
	
	void future(JobFuture future);
	
}
