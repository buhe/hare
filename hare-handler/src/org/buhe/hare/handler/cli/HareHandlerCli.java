/**
 * 
 */
package org.buhe.hare.handler.cli;

import java.io.IOException;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.HandlerConfiguration;
import org.buhe.hare.handler.HareHandler;


/**
 * TODO - 未实现
 * @author buhe
 *
 */
public class HareHandlerCli {

	public static void main(String[] args) throws IOException {
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.setZooKeeperIp("192.168.74.44");
		configuration.setZooKeeperPort(1314);
		configuration.setMasteNodeIp("192.168.74.44");
		configuration.setMasterNodePort(1986);
		configuration.setHandlerNodeIp("192.168.74.44");
		configuration.setHandlerNodePort(2004);
		//default handler configuration
		HandlerConfiguration handlerConfig = new HandlerConfiguration();
		configuration.setHandlerConfiguration(handlerConfig);
		HareHandler handler = new HareHandler(configuration);
		Thread t = new Thread(handler);
		t.start();
	}

	
}
