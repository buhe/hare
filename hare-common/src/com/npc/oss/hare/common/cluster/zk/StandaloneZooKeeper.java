/**
 * 
 */
package com.npc.oss.hare.common.cluster.zk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;

/**
 * @author buhe
 * 
 */
public class StandaloneZooKeeper {

	private final static Log LOG = LogFactory.getLog(StandaloneZooKeeper.class);
	private ConfigurationImpl configuration;

	public StandaloneZooKeeper(ConfigurationImpl configuration) {
		this.configuration = configuration;
	}

	public void startup() throws IOException {
		String baseDir = "d:/";
		File dir = new File(baseDir, "zookeeper_hare").getAbsoluteFile();
		recreateDir(dir);
		ZooKeeperServer server = new ZooKeeperServer(dir, dir, 2000);
		NIOServerCnxnFactory standaloneServerFactory;
		while (true) {
			try {
				standaloneServerFactory = new NIOServerCnxnFactory();
				standaloneServerFactory.configure(new InetSocketAddress(
						configuration.getZooKeeperPort()), 1000);
			} catch (BindException e) {
				LOG.error(e);
				continue;
			}
			break;
		}

		// Start up this ZK server
		try {
			standaloneServerFactory.startup(server);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		waitForServerUp(configuration.getZooKeeperPort(),50000);
	}

	private void recreateDir(File dir) throws IOException {
		if (dir.exists()) {
			dir.delete();
		}
		try {
			dir.mkdirs();
		} catch (SecurityException e) {
			throw new IOException("creating dir: " + dir, e);
		}
	}
	
	private static boolean waitForServerUp(int port, long timeout) {
	    long start = System.currentTimeMillis();
	    while (true) {
	      try {
	        Socket sock = new Socket("localhost", port);
	        BufferedReader reader = null;
	        try {
	          OutputStream outstream = sock.getOutputStream();
	          outstream.write("stat".getBytes());
	          outstream.flush();

	          Reader isr = new InputStreamReader(sock.getInputStream());
	          reader = new BufferedReader(isr);
	          String line = reader.readLine();
	          if (line != null && line.startsWith("Zookeeper version:")) {
	            return true;
	          }
	        } finally {
	          sock.close();
	          if (reader != null) {
	            reader.close();
	          }
	        }
	      } catch (IOException e) {
	        // ignore as this is expected
	        LOG.info("server localhost:" + port + " not up " + e);
	      }

	      if (System.currentTimeMillis() > start + timeout) {
	        break;
	      }
	      try {
	        Thread.sleep(250);
	      } catch (InterruptedException e) {
	        // ignore
	      }
	    }
	    return false;
	  }
	
	public static void main(String[] args) {
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.setZooKeeperPort(1314);
		StandaloneZooKeeper szk = new StandaloneZooKeeper(configuration);
		try {
			szk.startup();
		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
