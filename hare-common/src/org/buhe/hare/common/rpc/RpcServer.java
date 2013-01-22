/**
 * 
 */
package org.buhe.hare.common.rpc;

/**
 * @author buhe
 *
 */
public interface RpcServer {

	<T> void bind(T service);
}
