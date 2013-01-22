/**
 * 
 */
package com.npc.oss.hare.common.rpc.netty;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.common.rpc.support.MockService;

/**
 * @author buhe
 * 
 */
public class NettyRpcServerTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HareRPC hRpc = new HareRPC(new ConfigurationImpl());

		RpcConfiguration c = new RpcConfiguration("192.168.74.44", 1314, "");
		
		hRpc.getRpcServer(c).bind(new MockService());

	}

}
