/**
 * 
 */
package org.buhe.hare.common;

import org.buhe.hare.common.metadata.HanlderMetadata;


/**
 * 节点间通信接口
 * @author buhe
 *
 */
public interface MasterService {

	/**
	 * 处理器服务器启动
	 * @param address
	 * @param port
	 * @param serverCurrentTime
	 * @return
	 */
	//TODO - 先用IP,以后考虑用InetAddress
	boolean handlerServerStartup(HanlderMetadata neMetadata,long serverCurrentTime);

	void finishJob(String id);
	
	void stop();
}
