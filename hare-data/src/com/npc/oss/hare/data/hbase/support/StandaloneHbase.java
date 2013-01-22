/**
 * 
 */
package com.npc.oss.hare.data.hbase.support;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.master.HMasterCommandLine;
/**
 * 启动单机版本的Hbase
 * TODO - 这里应该不使用Hbase的zk,需要改一下
 * @author buhe
 *
 */
public class StandaloneHbase {
	private final static Log LOG = LogFactory.getLog(StandaloneHbase.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HMasterCommandLine cli = new HMasterCommandLine(HMaster.class);
		Configuration hbaseConfiguration = new Configuration();
		hbaseConfiguration.set(HConstants.ZOOKEEPER_DATA_DIR, "./hbase-zk-data");
		hbaseConfiguration.set(HConstants.ZOOKEEPER_CLIENT_PORT, "228");
		hbaseConfiguration.set(HConstants.HBASE_DIR, "./hbase-root-dir");
		cli.setConf(hbaseConfiguration);
		try {
			cli.run(new String[]{"start"});
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
