package com.npc.oss.hare.common.rpc.netty;

import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.Test;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.common.rpc.support.MockInterface;


public class NettyRpcClientTester {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	@Test
	public void testNormal(){
		 // Configure the client.
		HareRPC hRpc = new HareRPC(new ConfigurationImpl());
		
		RpcConfiguration c = new RpcConfiguration("192.168.74.44", 1314, "");
		
		MockInterface master = hRpc.getRpcClient(c).getProxy(MockInterface.class);
		
		String hello = master.hello();
		System.out.println("client" + hello);
		Assert.assertEquals("hello", hello);
	}
	
	
	@Test
	public void testEnterpriseException(){
		 // Configure the client.
		HareRPC hRpc = new HareRPC(new ConfigurationImpl());
		
		RpcConfiguration c = new RpcConfiguration("192.168.74.44", 1314, "");
		
		MockInterface master = hRpc.getRpcClient(c).getProxy(MockInterface.class);
		try{
			String hello = master.ex();
		}catch(Exception e){
			Assert.assertEquals("EXXX", e.getMessage());
		}
	}
	
	@Test
	public void testRpcException(){
		 // Configure the client.
		HareRPC hRpc = new HareRPC(new ConfigurationImpl());
		
		RpcConfiguration c = new RpcConfiguration("192.168.74.44", 1314, "");
		
		MockInterface master = hRpc.getRpcClient(c).getProxy(MockInterface.class);
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try{
			String hello = master.ex();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
