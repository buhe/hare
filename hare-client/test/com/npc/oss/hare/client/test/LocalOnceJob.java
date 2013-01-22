/**
 * 
 */
package com.npc.oss.hare.client.test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.job.impl.OnceJob;
import com.npc.oss.hare.common.job.support.JobContext;
import com.npc.oss.hare.common.metadata.NeIdentifier;
/**
 * @author buhe
 *
 */
public class LocalOnceJob extends OnceJob {
	public LocalOnceJob(JobContext ctx) {
		super(ctx);
		NeIdentifier neIdentifier = new NeIdentifier("eNodeB=3","1.0.0","TDD_ENB");
		this.neIdentifier(neIdentifier);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5389444865747353945L;
	private final static Log LOG = LogFactory.getLog(LocalOnceJob.class);

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.common.job.OnceJob#doCall()
	 */
	@Override
	public Object doCall() throws Exception {
		Thread.sleep(20 * 1000);
		LOG.info("call LocalMockOnceJob");
		return "LocalMockOnceJob";
	}
}
