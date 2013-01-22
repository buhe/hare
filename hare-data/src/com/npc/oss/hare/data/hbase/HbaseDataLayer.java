/**
 * 
 */
package com.npc.oss.hare.data.hbase;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.npc.oss.domainmodel.cm.Attribute;
import com.npc.oss.domainmodel.cm.MO;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.util.Assert;
import com.npc.oss.hare.data.hbase.support.HbaseClient;
import com.npc.oss.hare.data.spi.EnterpriseDataLayer;

/**
 * 查询Hbase集群
 * 
 * @author buhe
 * 
 */
public class HbaseDataLayer implements EnterpriseDataLayer {
	private final static Log LOG = LogFactory.getLog(HbaseDataLayer.class);
	public final static String MO_HTABLE_NAME = "MO";
	public final static String MO_FAMILY_NAME = "mo";
	public final static String ATTRIBUTE_FAMILY_NAME = "attribute";
	public final static String DN_DEFAULT_QUALIFIER = "dn";
	public final static String CID_DEFAULT_QUALIFIER = "cid";
	private ConfigurationImpl configuration;
	private HbaseClient client;

	public HbaseDataLayer(ConfigurationImpl configuration) {
		this.configuration = configuration;
		client = new HbaseClient(configuration);
		initalizeHTable();
	}

	/**
	 * 如果表不存在则创建表
	 */
	private void initalizeHTable() {
		try {
			if (!client.existHTable(MO_HTABLE_NAME)) {
				// 表不存在
				client.addHTable(MO_HTABLE_NAME, MO_FAMILY_NAME,
						ATTRIBUTE_FAMILY_NAME);
			}
		} catch (IOException e) {
			LOG.error(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.npc.oss.hare.data.EnterpriseDataLayer#findMOByDn(java.lang.String)
	 */
	@Override
	public MO findMOByDn(String dn) {
		MO[] mos = findMOsByDns(new String[]{dn});
		if(mos == null || mos.length == 0){
			return null;
		}else{
			return mos[0];
		}
//		Get get = new Get(toBytes(dn));
//		get.addFamily(getMOFamily());
//		get.addFamily(getAttributeFamily());
//		try {
//			HTable table = client.findHTable(MO_HTABLE_NAME);
//			Result result = table.get(get);
//			NavigableMap<byte[], byte[]> moDataMap = result
//					.getFamilyMap(getMOFamily());
//			if (moDataMap == null || moDataMap.isEmpty()) {
//				return null;
//			}
//			String dnInHbase = Bytes.toString(moDataMap
//					.remove(toBytes(DN_DEFAULT_QUALIFIER)));
//			int cidInHbase = Bytes.toInt(moDataMap
//					.remove(toBytes(CID_DEFAULT_QUALIFIER)));
//			if (dnInHbase.equals(dn)) {
//				NavigableMap<byte[], byte[]> attributeDataMap = result
//						.getFamilyMap(this.getAttributeFamily());
//				MO mo = new MO();
//				mo.setDn(dnInHbase);
//				mo.setCid(cidInHbase);
//				for (Map.Entry<byte[], byte[]> attributeEntry : attributeDataMap
//						.entrySet()) {
//					int aid = Bytes.toInt(attributeEntry.getKey());
//					String value = Bytes.toString(attributeEntry.getValue());
//					mo.append(new Attribute(aid, value));
//				}
//				return mo;
//			} else {
//				throw new IllegalStateException(dn + " != " + dnInHbase);
//			}
//		} catch (IOException e) {
//			LOG.error(e);
//			throw new RuntimeException(e);
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.npc.oss.hare.data.EnterpriseDataLayer#findMOsByDns(java.lang.String
	 * [])
	 */
	@Override
	public MO[] findMOsByDns(String[] dns) {
		Assert.assertNotNull("Dn should not be null", dns);
		List<Get> gets = new ArrayList<Get>();
		List<MO> moResults = new ArrayList<MO>();
		for (String dn : dns) {
			Get get = new Get(toBytes(dn));
			get.addFamily(getMOFamily());
			get.addFamily(getAttributeFamily());
			gets.add(get);
		}
		try {
			HTable table = client.findHTable(MO_HTABLE_NAME);
			Result[] results = table.get(gets);
			for (Result result : results) {
				NavigableMap<byte[], byte[]> moDataMap = result
						.getFamilyMap(getMOFamily());
				if (moDataMap == null || moDataMap.isEmpty()) {
					continue;
				}
				String dnInHbase = Bytes.toString(moDataMap
						.remove(toBytes(DN_DEFAULT_QUALIFIER)));
				int cidInHbase = Bytes.toInt(moDataMap
						.remove(toBytes(CID_DEFAULT_QUALIFIER)));
				NavigableMap<byte[], byte[]> attributeDataMap = result
						.getFamilyMap(this.getAttributeFamily());
				MO mo = new MO();
				mo.setDn(dnInHbase);
				mo.setCid(cidInHbase);
				for (Map.Entry<byte[], byte[]> attributeEntry : attributeDataMap
						.entrySet()) {
					int aid = Bytes.toInt(attributeEntry.getKey());
					String value = Bytes.toString(attributeEntry.getValue());
					mo.append(new Attribute(aid, value));
				}
				moResults.add(mo);
			}
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
		return moResults.toArray(new MO[moResults.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.npc.oss.hare.data.EnterpriseDataLayer#addMO(com.npc.oss.domainmodel
	 * .cm.MO)
	 */
	@Override
	public boolean addMO(MO mo) {
		String dn = mo.getDn();
		int cid = mo.getCid();
		Put put = new Put(toBytes(dn));
		put.add(getMOFamily(), toBytes(DN_DEFAULT_QUALIFIER), toBytes(dn));
		put.add(getMOFamily(), toBytes(CID_DEFAULT_QUALIFIER), toBytes(cid));
		if (mo.getAttributes() != null && mo.getAttributes().length > 0) {

			for (Attribute attribute : mo.getAttributes()) {
				int aid = attribute.getAid();
				String aValue = attribute.getValue();
				put.add(getAttributeFamily(), toBytes(aid), toBytes(aValue));
			}
		}
		try {
			HTable table = client.findHTable(MO_HTABLE_NAME);
			table.put(put);
			table.flushCommits();
			return true;
		} catch (IOException e) {
			LOG.error(e);
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.npc.oss.hare.data.EnterpriseDataLayer#removeMOByDn(java.lang.String)
	 */
	@Override
	public boolean removeMOByDn(String dn) {
		Delete delete = new Delete(toBytes(dn));
		try {
			HTable table = client.findHTable(MO_HTABLE_NAME);
			table.delete(delete);
			table.flushCommits();
			return true;
		} catch (IOException e) {
			LOG.error(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.npc.oss.hare.data.EnterpriseDataLayer#modifyMO(com.npc.oss.domainmodel
	 * .cm.MO, com.npc.oss.domainmodel.cm.MO)
	 */
	@Override
	public boolean modifyMO(MO oldMO, MO newMO) {
		Assert.assertNotNull("old mo not null", oldMO);
		Assert.assertNotNull("new mo not null", newMO);
		if (!oldMO.getDn().equals(newMO.getDn())) {
			throw new IllegalArgumentException("old mo dn not equals new mo");
		}
		String dn = newMO.getDn();
		Put put = new Put(toBytes(dn));
		// 只修改属性
		if (newMO.getAttributes() != null && newMO.getAttributes().length > 0) {

			for (Attribute attribute : newMO.getAttributes()) {
				int aid = attribute.getAid();
				String aValue = attribute.getValue();
				put.add(getAttributeFamily(), toBytes(aid), toBytes(aValue));
			}
		}

		// Delete delete = new Delete(toBytes(dn));
		// delete attributes
		// delete.deleteFamily(this.getAttributeFamily());
		try {
			HTable table = client.findHTable(MO_HTABLE_NAME);
			// table.delete(delete);
			table.put(put);
			table.flushCommits();
			return true;
		} catch (IOException e) {
			LOG.error(e);
			return false;
		}
	}

	private byte[] getMOFamily() {
		return toBytes(MO_FAMILY_NAME);
	}

	private byte[] getAttributeFamily() {
		return toBytes(ATTRIBUTE_FAMILY_NAME);
	}
}
