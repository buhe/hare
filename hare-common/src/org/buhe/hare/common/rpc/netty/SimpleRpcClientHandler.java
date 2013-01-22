/**
 * 
 */
package org.buhe.hare.common.rpc.netty;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.rpc.support.RpcResponse;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;


/**
 * @author buhe
 *
 */
public class SimpleRpcClientHandler extends SimpleChannelUpstreamHandler {

	private final static Log LOG = LogFactory.getLog(SimpleRpcClientHandler.class);
	private List<ClientInvoker> invokers = new CopyOnWriteArrayList<ClientInvoker>();
	
	public SimpleRpcClientHandler(){
	}
	
	
	public void addInvoker(ClientInvoker invoker) {
		this.invokers.add(invoker);
	}


	@Override
    public void messageReceived(
        ChannelHandlerContext ctx, MessageEvent e) throws Exception{
		if(e.getMessage() instanceof RpcResponse){
			RpcResponse r = (RpcResponse) e.getMessage();
			for(ClientInvoker invoker : invokers){
				invoker.signal(r);
			}
		}
		super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
    	if(e.getCause() instanceof NotYetConnectedException || e.getCause() instanceof ClosedChannelException){
    		RpcResponse er = new RpcResponse(new org.buhe.hare.common.exception.rpc.NotYetConnectedException());
    		for(ClientInvoker invoker : invokers){
				invoker.signal(er);
			}
    	}
    	LOG.error(
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}
