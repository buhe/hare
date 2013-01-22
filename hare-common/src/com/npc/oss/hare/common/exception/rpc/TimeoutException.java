/**
 * 
 */
package com.npc.oss.hare.common.exception.rpc;

/**
 * @author buhe
 *
 */
public class TimeoutException extends RpcException {

	private static final long serialVersionUID = -59155952523498051L;

	public TimeoutException(String message){
		super(message);
	}
}
