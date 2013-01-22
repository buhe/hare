/**
 * 
 */
package org.buhe.hare.client.test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.client.HareClient;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.job.Job;
import org.buhe.hare.common.job.support.JobContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author buhe
 *
 */
public class HareClientTester {
	private final static Log LOG = LogFactory.getLog(HareClientTester.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCycleJob() {
		Job job = null;
		ConfigurationImpl c = new ConfigurationImpl();
		c.setMasteNodeIp("192.168.74.44");
		c.setMasterNodePort(1986);
		HareClient client = new HareClient(c);
		JobContext ctx = new JobContext();
		ctx.setCrucial(true);
		ctx.setLocal(false);
		try {
			client.execute(job = new MockCycleJob(ctx));
		} catch (NotAnyHandlerException e) {
			LOG.error(e);
		}finally{
			client.close();
		}
		
		
	}
	
	@Test
	public void testOnceJob() {
		Job job = null;
		ConfigurationImpl c = new ConfigurationImpl();
		c.setMasteNodeIp("192.168.74.44");
		c.setMasterNodePort(1986);
		HareClient client = new HareClient(c);
		JobContext ctx = new JobContext();
		ctx.setCrucial(true);
		ctx.setLocal(false);
		try {
			client.execute(job =new MockOnceJob(ctx));
			job.waitComplete();
			Object o = job.getResult();
			LOG.info(o.toString());
		} catch (NotAnyHandlerException e) {
			LOG.error(e);
		}finally{
			client.close();
		}
	}
	
	@Test
	public void testLocalOnceJob() {
		Job job = null;
		ConfigurationImpl c = new ConfigurationImpl();
		c.setMasteNodeIp("192.168.74.44");
		c.setMasterNodePort(1986);
		HareClient client = new HareClient(c);
		JobContext ctx = new JobContext();
		ctx.setCrucial(false);
		ctx.setLocal(true);
		try {
			client.execute(job =new LocalOnceJob(ctx));
			job.waitComplete();
			Object o = job.getResult();
			LOG.info(o.toString());
		} catch (NotAnyHandlerException e) {
			LOG.error(e);
		}finally{
			client.close();
		}
	}
	
	
}
