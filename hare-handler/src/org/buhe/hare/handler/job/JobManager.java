/**
 * 
 */
package org.buhe.hare.handler.job;
import org.buhe.hare.common.job.Job;
/**
 * @author buhe
 *
 */
public interface JobManager {

	public void execute(Job job);
	
	public void stop(Job job);
	
	public Object getJobResult(String id);
}
