/**
 * 
 */
package com.npc.oss.hare.common.job.support;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 循环任务会返回这个存根
 * @author buhe
 *
 */
public class CycleJobResult {
	private final static Log LOG = LogFactory.getLog(CycleJobResult.class);
	public static final CycleJobResult INSTANCE = new CycleJobResult();
}
