/**
 * 
 */
package com.npc.oss.hare.master.balance.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;
import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.metadata.Metadata;
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
