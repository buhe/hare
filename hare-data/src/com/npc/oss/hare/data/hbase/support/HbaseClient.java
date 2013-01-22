/**
 * 
 */
package com.npc.oss.hare.data.hbase.support;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;

/**
 * Hbase client
 * TODO - 检查下,要求线程安全
 * TODO - 下面好多Helper方法的API设计不合理,粒度太细,不适合领域模型的操作
 * @author buhe
 * 
 */
public class HbaseClient {
	private final static Log LOG = LogFactory.getLog(HbaseClient.class);
	private Configuration hbaseConfiguration;
	private HBaseAdmin hbaseAdmin;

	public HbaseClient(ConfigurationImpl configuration) {
		String zkIp = configuration.getZooKeeperIp();
		int zkPort = configuration.getZooKeeperPort();
		hbaseConfiguration = HBaseConfiguration.create();
		hbaseConfiguration.set("hbase.zookeeper.quorum", zkIp );
		hbaseConfiguration.set("hbase.zookeeper.property.clientPort", String.valueOf(zkPort));
		try {
			hbaseAdmin = new HBaseAdmin(hbaseConfiguration);
		} catch (MasterNotRunningException e) {
			LOG.error(e);
		} catch (ZooKeeperConnectionException e) {
			LOG.error(e);
		}
	}

	public void addHTable(String tableName, String ... familyNames)
			throws IOException {
		HTableDescriptor desc = new HTableDescriptor(tableName);
		for(String familyName : familyNames){
			desc.addFamily(new HColumnDescriptor(familyName));
		}
		hbaseAdmin.createTable(desc);
	}
	
	public boolean existHTable(String tableName) throws IOException{
		HTableDescriptor td;
		try {
			td = hbaseAdmin.getTableDescriptor(toBytes(tableName));
			return true;
		} catch (TableNotFoundException e) {
			return false;
		}
	}
	
	public void deleteHTable(String tableName) throws IOException{
		hbaseAdmin.deleteTable(tableName);
	}
	
	public HTable findHTable(String tableName) throws IOException{
		HTable table = new HTable(hbaseConfiguration, tableName);
		return table;
	}

	public void put(HTable htable, String rowName, String family,
			String qualifier, String value) throws IOException {
		Put put = new Put(toBytes(rowName));
		put.add(toBytes(family), toBytes(qualifier), toBytes(value));
		htable.put(put);
		htable.flushCommits();
	}
	
	public void put(String tableName, String rowName, String family,
			String qualifier, String value) throws IOException{
		HTable table = findHTable(tableName);
		this.put(table, rowName, family, qualifier, value);
	}

	public String get(HTable htable, String rowName, String family,
			String qualifier) throws IOException {
		Get get = new Get(toBytes(rowName));
		get.addColumn(toBytes(family), toBytes(qualifier));
		Result r = htable.get(get);
		byte[] bytes = r.getValue(toBytes(family), toBytes(qualifier));
		if(bytes == null){
			return null;
		}else{
			return Bytes.toString(bytes);
		}
	}
	
	public String get(String tableName, String rowName, String family,
			String qualifier) throws IOException {
		HTable table = findHTable(tableName);
		return this.get(table, rowName, family, qualifier);
	}

	public void delete(HTable htable, String rowName, String family,
			String qualifier) throws IOException {
		Delete delete = new Delete(toBytes(rowName));
		delete.deleteColumn(toBytes(family), toBytes(qualifier));
		htable.delete(delete);
		htable.flushCommits();
	}
	
	public void delete(String tableName, String rowName, String family,
			String qualifier) throws IOException {
		HTable table = findHTable(tableName);
		this.delete(table, rowName, family, qualifier);
	}

	public ResultScanner scan(HTable htable, String family, String qualifier)
			throws IOException {
		Scan scan = new Scan();
		scan.addColumn(toBytes(family), toBytes(qualifier));
		return htable.getScanner(scan);
	}
	
	public ResultScanner scan(String tableName, String family, String qualifier)
			throws IOException {
		HTable table = findHTable(tableName);
		return this.scan(table, family, qualifier);
	}
	
	public void close(){
		try {
			hbaseAdmin.close();
		} catch (IOException e) {
			LOG.error(e);
		}
	}

}
