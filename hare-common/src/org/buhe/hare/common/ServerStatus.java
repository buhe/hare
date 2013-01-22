/**
 * 
 */
package org.buhe.hare.common;


/**
 * 服务器负载状态
 * @author buhe
 * 
 */
public enum ServerStatus {
	/**
	 * 服务器负载高,不适合分配任务
	 */
	HIGH, 
	/**
	 * 服务器负载正常,但成为不适合大量的负载均衡的目标
	 */
	NORMAL, 
	/**
	 * 服务器负载很低,适合高负载转移任务到该服务器
	 */
	LOW
}
