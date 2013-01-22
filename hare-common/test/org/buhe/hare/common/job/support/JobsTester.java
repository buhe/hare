/**
 * 
 */
package org.buhe.hare.common.job.support;
import static org.junit.Assert.*;
import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.job.support.Jobs;
import org.junit.Test;

/**
 * @author buhe
 *
 */
public class JobsTester {
	private final static Log LOG = LogFactory.getLog(JobsTester.class);

	@Test
	public void testCreate() {
		String node = Jobs.createJobZnode("/p","xxx-xxx-eee-yyyy");
		Assert.assertEquals("/p/"+Jobs.JOB_PREFIX + "xxx-xxx-eee-yyyy", node);
	}
	
	
	@Test
	public void testParse() {
		String id = Jobs.parseJobId("hare-job-13c7952c-2392-4565-8e17-6fdccaf42f70");
		Assert.assertEquals("13c7952c-2392-4565-8e17-6fdccaf42f70", id);
	}
	
	@Test
	public void testVal() {
		String node = Jobs.createJobZnode("/p","xxx-xxx-eee-yyyy");
		Assert.assertTrue(node.startsWith("/"));
	}
}
