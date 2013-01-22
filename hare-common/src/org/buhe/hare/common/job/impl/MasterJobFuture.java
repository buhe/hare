/**
 * 
 */
package org.buhe.hare.common.job.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.MasterInterface;
import org.buhe.hare.common.job.JobFuture;
import org.buhe.hare.common.job.support.JobStatus;

/**
 * @author buhe
 *
 */
public class MasterJobFuture implements JobFuture {
	private final static Log LOG = LogFactory.getLog(MasterJobFuture.class);
	private transient MasterInterface clientToMaster;
	private transient String id;

	public MasterJobFuture(MasterInterface clientToMaster,String id){
		this.clientToMaster = clientToMaster;
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.npc.oss.hare.common.job.JobFuture#waitForComplete()
	 */
	@Override
	public void waitForComplete() {
		while (true) { // 客户端超时?
			// 等待Handler执行完毕
			JobStatus status = this.clientToMaster.getJobStatus(id);
			LOG.info("Current jobStatus : " + status);
			if (status == JobStatus.FAIL || status == JobStatus.FINISH) {
				return;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ignore) {
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.common.job.JobFuture#get()
	 */
	@Override
	public Object get() {
		return clientToMaster.getJobResult(this.id);
	}
}
