/**
 * 
 */
package org.buhe.hare.common;

import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.JobStatus;
import org.buhe.hare.common.metadata.HanlderMetadata;
import org.buhe.hare.common.metadata.Metadata;
import org.buhe.hare.common.metadata.NeIdentifier;




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
