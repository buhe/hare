/**
 * 
 */
package com.npc.oss.hare.common.configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author buhe
 *
 */
public class HandlerConfiguration {
	private final static Log LOG = LogFactory.getLog(HandlerConfiguration.class);
	private final static int DEFAULT_LONG_TIME_JOB_COUNT = Runtime.getRuntime().availableProcessors();
	private final static int DEFAULT_SHORT_TIME_JOB_COUNT = Runtime.getRuntime().availableProcessors();
	/**
	 * 在Handler执行的
	 */
	private int shortTimeJobCount = DEFAULT_SHORT_TIME_JOB_COUNT;
	private int longTimeJobCount = DEFAULT_LONG_TIME_JOB_COUNT;
	public int getShortTimeJobCount() {
		return shortTimeJobCount;
	}
	public void setShortTimeJobCount(int shortTimeJobCount) {
		this.shortTimeJobCount = shortTimeJobCount;
	}
	public int getLongTimeJobCount() {
		return longTimeJobCount;
	}
	public void setLongTimeJobCount(int longTimeJobCount) {
		this.longTimeJobCount = longTimeJobCount;
	}
	
	
	
}
