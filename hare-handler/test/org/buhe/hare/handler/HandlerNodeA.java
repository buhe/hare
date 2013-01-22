/**
 * 
 */
package org.buhe.hare.handler;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.common.configuration.HandlerConfiguration;
import org.buhe.hare.handler.HareHandler;

/**
 * @author buhe
 *
 */
public class HandlerNodeA {
	private final static Log LOG = LogFactory.getLog(HandlerNodeA.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
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
