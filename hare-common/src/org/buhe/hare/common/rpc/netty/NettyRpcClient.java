/**
 * 
 */
package org.buhe.hare.common.rpc.netty;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.RpcConfiguration;
import org.buhe.hare.common.rpc.RpcClient;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;


/**
 * 这里Netty用里长连接,以后考虑改成短连接,对网络压力小一点
 * @author buhe
 * 
 */
public class NettyRpcClient implements RpcClient {

	private Channel nettyChannel;
	private ConfigurationImpl configuration;
	private final SimpleRpcClientHandler handler;

	public NettyRpcClient(ConfigurationImpl configuration, RpcConfiguration rpc) {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		handler = new SimpleRpcClientHandler();
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline( 
						new ObjectEncoder(),
						new ObjectDecoder(),
						handler);
			}
		});

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(rpc
				.getIp(), rpc.getPort()));

		nettyChannel = future.getChannel();
		
		
	}

	@Override
	public <T> T getProxy(Class<T> clazz) {
		ClientInvoker invoker = new ClientInvoker(nettyChannel);
		handler.addInvoker(invoker);
		return (T) Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { clazz }, invoker);
	}
	
	@Override
	public void close(){
		nettyChannel.close();
	}

}
