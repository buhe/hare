/**
 * 
 */
package com.npc.oss.hare.common.util;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 命名线程工厂
 * @author buhe
 *
 */
public class NamedThreadFactory implements ThreadFactory {
	private final static Log LOG = LogFactory.getLog(NamedThreadFactory.class);
	private final AtomicInteger ID_GEN = new AtomicInteger(0);
	private String name;
	public NamedThreadFactory(String name){
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + ID_GEN.getAndIncrement());
		return t;
	}
}
