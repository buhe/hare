/**
 * 
 */
package org.buhe.hare.common.rpc;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.RpcConfiguration;


/**
 * @author buhe
 *
 */
public interface RpcProxyFactory {

	public RpcClient getRpcClient(ConfigurationImpl configuration,RpcConfiguration rpc);
	
	public RpcServer getRpcServer(ConfigurationImpl configuration,RpcConfiguration rpc);
}
