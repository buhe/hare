/**
 * 
 */
package com.npc.oss.hare.common.rpc.netty;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.npc.oss.hare.common.rpc.support.Invocation;
import com.npc.oss.hare.common.rpc.support.RpcResponse;

/**
 * @author buhe
 *
 */
public class SimpleRpcServerHandler extends SimpleChannelUpstreamHandler {

	private final static Log LOG = LogFactory.getLog(SimpleRpcServerHandler.class);
	private final ServerInvoker invoker;
	
	public SimpleRpcServerHandler(ServerInvoker invoker){
		this.invoker = invoker;
	}
	
	@Override
    public void messageReceived(
        ChannelHandlerContext ctx, MessageEvent e) {
		
		Invocation invocation = (Invocation) e.getMessage();
		Object r;
		try {
			r = invoker.invoke(invocation);
			RpcResponse response = new RpcResponse(invocation.getId(), r);
			e.getChannel().write(response);
		} catch (Exception e1) {
			LOG.error(e1);
			RpcResponse response = new RpcResponse(invocation.getId(),(Throwable)e1);
			e.getChannel().write(response);
		}
		
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
    	LOG.error(
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}
