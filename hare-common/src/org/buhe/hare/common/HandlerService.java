/**
 * 
 */
package org.buhe.hare.common;

import org.buhe.hare.common.job.Job;

/**
 * @author buhe
 *
 */
public interface HandlerService {

	boolean execute(Job job);
	
	void stop(Job job);
	
	Object getJobResult(String jobId);
	
	void stop();
}
