/**
 * 
 */
package com.npc.oss.hare.common.rpc.support;

import java.io.Serializable;

import com.npc.oss.hare.common.exception.rpc.RpcException;

/**
 * @author buhe
 *
 */
public class RpcResponse implements Serializable{

	private static final long serialVersionUID = -25030533162296246L;
	private long id;
	private Object result;
	private Throwable exception;
	private RpcException rpcException;
	public long getId() {
		return id;
	}
	public Throwable getException() {
		return exception;
	}
	

	public RpcException getRpcException() {
		return rpcException;
	}
	public Object getResult() {
		return result;
	}
	public RpcResponse(long id, Object result) {
		super();
		this.id = id;
		this.result = result;
	}
	public RpcResponse(long id, Throwable exception) {
		super();
		this.id = id;
		this.exception = exception;
	}
	public RpcResponse(RpcException exception){
		this.exception = exception;
	}
	
}
