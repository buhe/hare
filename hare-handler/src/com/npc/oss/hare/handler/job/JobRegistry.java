/**
 * 
 */
package com.npc.oss.hare.handler.job;

import com.npc.oss.hare.common.job.Job;

/**
 * Job注册器,用于注册Job
 * @author buhe
 *
 */
public interface JobRegistry {

	boolean regisiterJob(Job job);
	
	boolean unregisiterJob(Job job);
}
