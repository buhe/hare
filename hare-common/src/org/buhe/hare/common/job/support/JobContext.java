/**
 * 
 */
package org.buhe.hare.common.job.support;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author buhe
 *
 */
public class JobContext implements Serializable{
	private static final long serialVersionUID = -2845301876495092296L;
	private final static Log LOG = LogFactory.getLog(JobContext.class);
	/**
	 * 是否需要跟踪,确保一定会做完,默认是True
	 */
	private boolean crucial = true;
	/**
	 * 默认不是本地
	 */
	private boolean local = false;
	
	private long timeout = 5 * 60 * 1000;  //5 mins

	public boolean isCrucial() {
		return crucial;
	}

	public void setCrucial(boolean crucial) {
		this.crucial = crucial;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	
	
}
