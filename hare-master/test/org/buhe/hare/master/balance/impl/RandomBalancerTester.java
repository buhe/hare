/**
 * 
 */
package org.buhe.hare.master.balance.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.metadata.HanlderMetadata;
import org.buhe.hare.common.metadata.Metadata;
import org.buhe.hare.master.balance.impl.RandomBalancer;
import org.junit.Test;

/**
 * @author buhe
 *
 */
public class RandomBalancerTester {
	private final static Log LOG = LogFactory
			.getLog(RandomBalancerTester.class);

	@Test
	public void test() {
		RandomBalancer rb = new RandomBalancer();
		HanlderMetadata _1 = new HanlderMetadata("22");
		HanlderMetadata _2 = new HanlderMetadata("22");
		Metadata md = new Metadata();
		md.addHandler(_1);
		md.addHandler(_2);
		try {
			HanlderMetadata i = rb.select(md , null);
		} catch (NotAnyHandlerException e) {
			LOG.error(e);
		}
	}
	
	@Test(expected=NotAnyHandlerException.class)
	public void test2() throws NotAnyHandlerException {
		RandomBalancer rb = new RandomBalancer();

		Metadata md = new Metadata();

			HanlderMetadata i = rb.select(md , null);
	}
}
