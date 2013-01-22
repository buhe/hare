/**
 * 
 */
package org.buhe.hare.master.jobtracker;

import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.JobStatus;
import org.buhe.hare.common.metadata.HanlderMetadata;



/**
 * 提交任务,跟踪任务
 * @author buhe
 *
 */
public interface JobTracker {
	
	boolean prepare(Job job);
	
	void commit(String id);
	
	void cancel(String id);
	
	void rebalance(HanlderMetadata newHandler);
	
	JobStatus getJobStatus(String id);
	
	void finish(String id);

	Object getJobResult(String jobId);
}
