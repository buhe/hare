/**
 * 
 */
package org.buhe.hare.handler.job;

import org.buhe.hare.common.job.Job;

/**
 * Job注册器,用于注册Job
 * @author buhe
 *
 */
public interface JobRegistry {

	boolean regisiterJob(Job job);
	
	boolean unregisiterJob(Job job);
}
