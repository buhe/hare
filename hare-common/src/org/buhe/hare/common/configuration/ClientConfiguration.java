/**
 * 
 */
package org.buhe.hare.common.configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 客户端配置
 * @author buhe
 *
 */
public class ClientConfiguration {
	private final static Log LOG = LogFactory.getLog(ClientConfiguration.class);
	
	private final static int DEFAULT_LOCAL_EXECUTOR_COUNT = Runtime.getRuntime().availableProcessors() * 2;
	
	private int localExecutorCount = DEFAULT_LOCAL_EXECUTOR_COUNT;

	public int getLocalExecutorCount() {
		return localExecutorCount;
	}

	public void setLocalExecutorCount(int localExecutorCount) {
		this.localExecutorCount = localExecutorCount;
	}
	
	
}
