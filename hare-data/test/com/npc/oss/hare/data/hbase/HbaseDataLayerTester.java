/**
 * 
 */
package com.npc.oss.hare.data.hbase;
import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.npc.oss.domainmodel.cm.Attribute;
import com.npc.oss.domainmodel.cm.MO;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
/**
 * @author buhe
 *
 */
public class HbaseDataLayerTester {
	private final static Log LOG = LogFactory
			.getLog(HbaseDataLayerTester.class);
	HbaseDataLayer hdl;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.setZooKeeperIp("192.168.74.44");
		configuration.setZooKeeperPort(228);
		hdl = new HbaseDataLayer(configuration);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddMO() {
		MO mo = new MO();
		mo.setDn("bugu=1");
		mo.setCid(1314);
		mo.append(new Attribute(1, "xixi"));
		mo.append(new Attribute(10, "_10"));
		hdl.addMO(mo);
	}
	@Test
	public void testFindMOByDn(){
		String dn = "bugu=1";
		MO mo = hdl.findMOByDn(dn);
		LOG.info(mo);
	}
	
	
	@Test
	public void testRemoveMOByDn() {
		String dn = "bugu=1";
		hdl.removeMOByDn(dn);
		
		MO mo = hdl.findMOByDn(dn);
		
		Assert.assertNull(mo);
	}
	@Test
	public void testModifyMO(){
		testRemoveMOByDn();
		//add
		testAddMO();
		
		String dn = "bugu=1";
		MO oldMO = new MO();
		oldMO.setDn(dn);
		MO newMO = new MO();
		newMO.setDn(dn);
		newMO.append(new Attribute(1, "balabala"));
		newMO.append(new Attribute(2, "hehe"));
		
		hdl.modifyMO(oldMO, newMO);
		
		MO mo = hdl.findMOByDn(dn);
		Assert.assertNotNull(mo);
		LOG.info(mo);
		
	}
	
	@Test
	public void testFindMOsByDns(){
		MO amo = new MO();
		amo.setDn("bugu=2");
		amo.setCid(1314);
		amo.append(new Attribute(1, "xixi"));
		amo.append(new Attribute(10, "_10"));
		hdl.addMO(amo);
		
		MO[] mos = hdl.findMOsByDns(new String[]{"bugu=1","bugu=2"});
		Assert.assertNotNull(mos);
		Assert.assertNotSame(mos.length, 0);
		for(MO mo : mos){
			LOG.info(mo);
		}
	}
}
