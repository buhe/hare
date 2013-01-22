/**
 * 
 */
package com.npc.oss.hare.handler;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.HandlerConfiguration;
/**
 * @author buhe
 *
 */
public class HandlerNodeB {
	private final static Log LOG = LogFactory.getLog(HandlerNodeB.class);

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
		configuration.setHandlerNodePort(1212);
		//default handler configuration
		HandlerConfiguration handlerConfig = new HandlerConfiguration();
		configuration.setHandlerConfiguration(handlerConfig);
		HareHandler handler = new HareHandler(configuration);
		Thread t = new Thread(handler);
		t.start();

	}
}
