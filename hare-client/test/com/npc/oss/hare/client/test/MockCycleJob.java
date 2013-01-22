/**
 * 
 */
package com.npc.oss.hare.client.test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.job.impl.CycleJob;
import com.npc.oss.hare.common.job.support.JobContext;
import com.npc.oss.hare.common.metadata.NeIdentifier;
/**
 * @author buhe
 *
 */
public class MockCycleJob extends CycleJob {
	int i = 0;
	public MockCycleJob(JobContext ctx) {
		super(ctx);
		NeIdentifier neIdentifier = new NeIdentifier("eNodeB=2","1.0.0","TDD_ENB");
		this.neIdentifier(neIdentifier);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4317615032523347390L;
	private final static Log LOG = LogFactory.getLog(MockCycleJob.class);


	@Override
	public void doLoop() {
		LOG.info("loop " + i++);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		
	}


	

}
