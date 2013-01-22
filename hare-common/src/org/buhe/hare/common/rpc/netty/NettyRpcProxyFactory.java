/**
 * 
 */
package org.buhe.hare.common.rpc.netty;

import java.util.concurrent.ConcurrentHashMap;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.RpcConfiguration;
import org.buhe.hare.common.rpc.RpcClient;
import org.buhe.hare.common.rpc.RpcProxyFactory;
import org.buhe.hare.common.rpc.RpcServer;


/**
 * 用Netty作为传输层实现了一个简单的RPC
 * @author buhe
 *
 */
public class NettyRpcProxyFactory implements RpcProxyFactory{

	private final static ConcurrentHashMap<RpcConfiguration, RpcClient> CLIENT_MAP = new ConcurrentHashMap<RpcConfiguration, RpcClient>();
	private final static ConcurrentHashMap<RpcConfiguration, RpcServer> SERVER_MAP = new ConcurrentHashMap<RpcConfiguration, RpcServer>();
	
	public  RpcClient getRpcClient(ConfigurationImpl configuration,RpcConfiguration key){
		if(!CLIENT_MAP.containsKey(key)){
			RpcClient c = new NettyRpcClient(configuration,key);
			CLIENT_MAP.putIfAbsent(key, c);
		}
		return CLIENT_MAP.get(key);
	}

	@Override
	public  RpcServer getRpcServer(ConfigurationImpl configuration,RpcConfiguration key) {
		if(!SERVER_MAP.containsKey(key)){
			RpcServer c = new NettyRpcServer(configuration,key);
			SERVER_MAP.putIfAbsent(key, c);
		}
		return SERVER_MAP.get(key);
	}
		
}
