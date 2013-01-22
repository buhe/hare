package com.npc.oss.hare.common.rpc;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;

public class HareRPC {

	private RpcProxyFactory proxyFactory;
	private ConfigurationImpl configuration;
	
	public HareRPC(ConfigurationImpl configuration){
		try {
			//TODO - cache
			this.proxyFactory = (RpcProxyFactory) configuration.getRpcProxyFactoryClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public  RpcClient getRpcClient(RpcConfiguration rpc){
		return proxyFactory.getRpcClient(configuration,rpc);
	}
	
	public RpcServer getRpcServer(RpcConfiguration rpc){
		return proxyFactory.getRpcServer(configuration,rpc);
	}
}
