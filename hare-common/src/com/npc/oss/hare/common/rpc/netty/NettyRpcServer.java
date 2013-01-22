/**
 * 
 */
package com.npc.oss.hare.common.rpc.netty;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.rpc.RpcServer;
import com.npc.oss.hare.common.rpc.support.Invocation;

/**
 * @author buhe
 * 
 */
public class NettyRpcServer implements RpcServer, ServerInvoker {
	Class<?> clazz;
	Object service;
	ConfigurationImpl configuration;
	RpcConfiguration rpc;

	public NettyRpcServer(ConfigurationImpl configuration,RpcConfiguration rpc) {
		this.configuration = configuration;
		this.rpc = rpc;
	}

	@Override
	public <T> void bind(T service) {
		clazz = service.getClass();
		this.service = service;
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(),
						new SimpleRpcServerHandler(NettyRpcServer.this));
			}
		});

		// Bind and start to accept incoming connections.
		bootstrap
				.bind(new InetSocketAddress(rpc.getPort()));
	}

	@Override
	public Object invoke(Invocation invocation) throws Exception {
		Method target = clazz.getMethod(invocation.getMethodName(),
				invocation.getParameterClasses());
		return target.invoke(service, invocation.getParameters());

	}

}
