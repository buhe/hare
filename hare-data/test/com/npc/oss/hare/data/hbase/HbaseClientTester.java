/**
 * 
 */
package com.npc.oss.hare.data.hbase;
import static org.apache.hadoop.hbase.util.Bytes.toBytes;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.data.hbase.support.HbaseClient;
/**
 * @author buhe
 *
 */
public class HbaseClientTester {
	private final static Log LOG = LogFactory.getLog(HbaseClientTester.class);
	private HbaseClient hc;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.setZooKeeperIp("192.168.74.44");
		configuration.setZooKeeperPort(228);
		hc = new HbaseClient(configuration);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		hc.close();
	}
	

	/**
	 * Test method for {@link com.npc.oss.hare.data.hbase.support.HbaseClient#addHTable(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testAddHTable() throws IOException {
		hc.addHTable("bugu-test", "fullhorse");
	}
	

	/**
	 * Test method for {@link com.npc.oss.hare.data.hbase.support.HbaseClient#put(org.apache.hadoop.hbase.client.HTable, java.lang.String, java.lang.String, java.lang.String, byte[])}.
	 * @throws IOException 
	 */
	@Test
	public void testPut() throws IOException {
		hc.put("bugu-test", "buhe", "fullhorse", "age", "26");
	}

	/**
	 * Test method for {@link com.npc.oss.hare.data.hbase.support.HbaseClient#get(org.apache.hadoop.hbase.client.HTable, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGet() throws IOException {
		String actuals = hc.get("bugu-test", "buhe", "fullhorse", "age");
		Assert.assertEquals("26", actuals);
	}

	/**
	 * Test method for {@link com.npc.oss.hare.data.hbase.support.HbaseClient#delete(org.apache.hadoop.hbase.client.HTable, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testDelete() throws IOException {
		hc.delete("bugu-test", "buhe", "fullhorse", "age");
		
		String actuals = hc.get("bugu-test", "buhe", "fullhorse", "age");
		Assert.assertNull(actuals);
		
	}

	/**
	 * Test method for {@link com.npc.oss.hare.data.hbase.support.HbaseClient#scan(org.apache.hadoop.hbase.client.HTable, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testScan() throws IOException {
		int c = 0;
		hc.put("bugu-test", "buhe", "fullhorse", "age", "26");
		hc.put("bugu-test", "guyanhua", "fullhorse", "age", "26");
		
		ResultScanner rs = hc.scan("bugu-test", "fullhorse", "age");
		for(Result r : rs){
			byte[] age = r.getValue(toBytes("fullhorse"), toBytes("age"));
			c++;
			Assert.assertEquals("26", age);
		}
		System.out.println(c);
		Assert.assertEquals(c, 2);
	}
	
	@Test
	public void deleteTable() throws IOException{
		hc.deleteHTable("MO");
	}
}
