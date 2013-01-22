/**
 * 
 */
package org.buhe.hare.common.rpc;

/**
 * @author buhe
 *
 */
public interface RpcClient {

	<T> T getProxy(Class<T> clazz);

	void close();
}
