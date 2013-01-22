package org.buhe.hare.common.rpc.netty;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.exception.rpc.TimeoutException;
import org.buhe.hare.common.rpc.support.Invocation;
import org.buhe.hare.common.rpc.support.RpcResponse;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;


public class ClientInvoker implements InvocationHandler{

	private final static Log LOG = LogFactory.getLog(ClientInvoker.class);
	
	private Channel nettyChannel;
	private final ReentrantLock writeLocker = new ReentrantLock();
	private final Condition c = writeLocker.newCondition();
	private volatile RpcResponse r;
	private volatile Invocation invocation;
	
	public ClientInvoker(Channel nettyChannel) {
		this.nettyChannel = nettyChannel;
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//阻塞执行
		writeLocker.lock();
		long now = System.currentTimeMillis();
		long timeout = 60 * 1000; //60s
		try{
			invocation = new Invocation(method,args);
			ChannelFuture future = nettyChannel.write(invocation);
			future.awaitUninterruptibly();
			if(future.isSuccess()){
				while(System.currentTimeMillis() - now < timeout){
					if(r != null){
						RpcResponse response = r;
						//结果置空
						resetResponse();
						
						if(response.getRpcException() != null){
							throw response.getRpcException();
						}
						if(response.getException() != null){
							//enterprise exception
							if(response.getException() instanceof InvocationTargetException){
								throw ((InvocationTargetException)response.getException()).getTargetException();
							}else{
								//TODO something else
							}
						}else{
							Object actualResult =  response.getResult();
							LOG.info("Actual result : " + actualResult);
							return actualResult;
						}
					}
					c.await(1l,TimeUnit.SECONDS);
				}
				throw new TimeoutException("Hare RPC Timeout.");
			}
			throw new TimeoutException("Hare RPC wait ack Timeout.");
		}finally{
			writeLocker.unlock();
		}
		
	}

	private void resetResponse() {
		r = null;
	}
	
	public void signal(RpcResponse r){
		writeLocker.lock();
		try{
			if(r.getRpcException() != null){
				this.r = r;
				c.signal();
				return;
			}
			if(invocation == null) // 方法调用尚未开始
				return;
			if(invocation.getId() == r.getId()){
				this.r = r;
				c.signal();
			}
		}finally{
			writeLocker.unlock();
		}
	}

}
