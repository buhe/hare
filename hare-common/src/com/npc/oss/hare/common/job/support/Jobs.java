/**
 * 
 */
package com.npc.oss.hare.common.job.support;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.cluster.zk.ZKUtil;
import com.npc.oss.hare.common.job.Job;
/**
 * @author buhe
 *
 */
public class Jobs {
	private static final String BLANK = "";
	private final static Log LOG = LogFactory.getLog(Jobs.class);
	protected final static String JOB_PREFIX = "hare-job-";
	
	public static String createJobZnode(String jobTypeNode,Job job){
		return createJobZnode(jobTypeNode,job.id());
	}
	//TODO - id 是否信息完备
	public static String createJobZnode(String jobTypeNode,String id){
		return ZKUtil.joinZNode(jobTypeNode, JOB_PREFIX + id);
	}
	
	public static Job parseJobFromZnode(Map<String,Job> jobs,String znode){
		return jobs.get(parseJobId(znode));
	}
	public static String parseJobId(String znode){
		int idIndex =  znode.indexOf(JOB_PREFIX);
		return znode.substring(idIndex + JOB_PREFIX.length());
	}
}
