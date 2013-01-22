/**
 * 
 */
package com.npc.oss.hare.handler.job;
import com.npc.oss.hare.common.job.Job;
/**
 * @author buhe
 *
 */
public interface JobManager {

	public void execute(Job job);
	
	public void stop(Job job);
	
	public Object getJobResult(String id);
}
