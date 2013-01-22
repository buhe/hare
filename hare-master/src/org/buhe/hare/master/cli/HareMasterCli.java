/**
 * 
 */
package org.buhe.hare.master.cli;

import java.io.IOException;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.master.HareMaster;


/**
 * TODO - 未实现
 * @author buhe
 *
 */
public class HareMasterCli {

	public static void main(String[] args) throws IOException {//TODO - 异常处理
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.setZooKeeperIp("192.168.74.44");
		configuration.setZooKeeperPort(1314);
		configuration.setMasteNodeIp("192.168.74.44");
		configuration.setMasterNodePort(1986);
		HareMaster master = new HareMaster(configuration);
		Thread t = new Thread(master);
		t.start();
	}

}
