/**
 * 
 */
package org.buhe.hare.common.rpc.netty;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.RpcConfiguration;
import org.buhe.hare.common.rpc.HareRPC;
import org.buhe.hare.common.rpc.support.MockService;


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
