/**
 * 
 */
package org.buhe.hare.common.rpc.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author buhe
 * 
 */
public class Invocation implements Serializable{

	private static final long serialVersionUID = -3278850173204906759L;
	protected String methodName;
	@SuppressWarnings("rawtypes")
	protected Class[] parameterClasses;
	protected Object[] parameters;
	private static transient AtomicLong idGen = new AtomicLong(0);
	private long id;
	public Invocation() {}
	
	public Invocation(Method method, Object[] parameters) {
		this.methodName = method.getName();
	    this.parameterClasses = method.getParameterTypes();
	    this.parameters = parameters;
	    this.id = idGen.getAndIncrement();
	}

	public Object[] getParameters() {
		return parameters;
	}


	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParameterClasses() {
		return parameterClasses;
	}

	public long getId() {
		return id;
	}
	
	
}
