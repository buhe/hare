package com.npc.oss.hare.client.test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.job.impl.OnceJob;
import com.npc.oss.hare.common.job.support.JobContext;
import com.npc.oss.hare.common.metadata.NeIdentifier;
public class MockOnceJob extends OnceJob {
	public MockOnceJob(JobContext ctx) {
		super(ctx);
		NeIdentifier neIdentifier = new NeIdentifier("eNodeB=3","1.0.0","TDD_ENB");
		this.neIdentifier(neIdentifier);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5316013823159259953L;
	private final static Log LOG = LogFactory.getLog(MockOnceJob.class);

	@Override
	public Object doCall() throws Exception {
		Thread.sleep(20 * 1000);
		LOG.info("call MockOnceJob");
		return "MockOnceJob";
	}
}
