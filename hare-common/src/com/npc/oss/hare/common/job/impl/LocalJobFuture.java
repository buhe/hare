/**
 * 
 */
package com.npc.oss.hare.common.job.impl;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.job.JobFuture;
/**
 * @author buhe
 *
 */
public class LocalJobFuture implements JobFuture {
	private final static Log LOG = LogFactory.getLog(LocalJobFuture.class);
	private Future jucFuture;
	
	public LocalJobFuture(Future jucFuture){
		this.jucFuture = jucFuture;
	}
	/* (non-Javadoc)
	 * @see com.npc.oss.hare.common.job.JobFuture#waitForComplete()
	 */
	@Override
	public void waitForComplete() {
		try {
			jucFuture.get();
		} catch (InterruptedException e) {
			LOG.error(e);
		} catch (ExecutionException e) {
			LOG.error(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.common.job.JobFuture#get()
	 */
	@Override
	public Object get() {
		try {
			return jucFuture.get();
		} catch (InterruptedException e) {
			LOG.error(e);
			//FIXME - 异常要统一处理下
			return null;
		} catch (ExecutionException e) {
			LOG.error(e);
			return null;
		}
	}
}
