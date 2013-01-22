/**
 * 
 */
package org.buhe.hare.common.job;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.buhe.hare.common.HandlerService;
import org.buhe.hare.common.MasterInterface;
import org.buhe.hare.common.MasterService;
import org.buhe.hare.common.job.support.JobContext;
import org.buhe.hare.common.job.support.JobType;
import org.buhe.hare.common.metadata.NeIdentifier;



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
