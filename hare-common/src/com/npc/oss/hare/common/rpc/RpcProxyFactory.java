/**
 * 
 */
package com.npc.oss.hare.common.rpc;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;


/**
 * @author buhe
 *
 */
public interface RpcProxyFactory {

	public RpcClient getRpcClient(ConfigurationImpl configuration,RpcConfiguration rpc);
	
	public RpcServer getRpcServer(ConfigurationImpl configuration,RpcConfiguration rpc);
}
