/**
 * 
 */
package com.npc.oss.hare.common;

import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;
import com.npc.oss.hare.common.job.Job;
import com.npc.oss.hare.common.job.support.JobStatus;
import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.metadata.Metadata;
import com.npc.oss.hare.common.metadata.NeIdentifier;



/**
 * 客户端通信接口
 * @author buhe
 *
 */
public interface MasterInterface {
	/**
	 * 根据网元唯一标识查找处理节点
	 * @param neIdentifier
	 * @return
	 */
	HanlderMetadata resolveHandlerNode(NeIdentifier neIdentifier) throws NotAnyHandlerException ;
	
	Metadata findMetaData();
	
	boolean execute(Job job);

	JobStatus getJobStatus(String jobId);
	
	Object getJobResult(String jobId);

}
