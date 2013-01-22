/**
 * 
 */
package com.npc.oss.hare.common.exception.rpc;

/**
 * @author buhe
 *
 */
public class RpcException extends RuntimeException {

	private static final long serialVersionUID = -2268266548685211654L;
	public RpcException(){
		super();
	}
	public RpcException(String message){
		super(message);
	}
}
