/**
 * 
 */
package com.npc.oss.hare.common.rpc;

/**
 * @author buhe
 *
 */
public interface RpcServer {

	<T> void bind(T service);
}
