/**
 * 
 */
package com.npc.oss.hare.common.cluster.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.npc.oss.hare.common.configuration.ConfigurationImpl;

/**
 * TODO - 集群实现可能会抽出接口,类似RPC,支持类似JGS的实现
 * @author buhe
 * 
 */
public class ZooKeeperWatcher implements Watcher {

	// listeners to be notified
	private final List<ZooKeeperListener> listeners = new CopyOnWriteArrayList<ZooKeeperListener>();

	private ZooKeeperClient client;
	public ZooKeeperWatcher(ConfigurationImpl configuration) throws IOException {
		client = new ZooKeeperClient(configuration.getZooKeeperIp()+":"+configuration.getZooKeeperPort(),30000,this);
	}
	
	public ZooKeeperClient getZooKeeperClient(){
		return client;
	}

	/**
	 * Register the specified listener to receive ZooKeeper events.
	 * 
	 * @param listener
	 */
	public void registerListener(ZooKeeperListener listener) {
		listeners.add(listener);
	}

	/**
	 * Register the specified listener to receive ZooKeeper events and add it as
	 * the first in the list of current listeners.
	 * 
	 * @param listener
	 */
	public void registerListenerFirst(ZooKeeperListener listener) {
		listeners.add(0, listener);
	}

	/**
	 * Method called from ZooKeeper for events and connection status.
	 * <p>
	 * Valid events are passed along to listeners. Connection status changes are
	 * dealt with locally.
	 */
	@Override
	public void process(WatchedEvent event) {

		switch (event.getType()) {

		// If event type is NONE, this is a connection status change
		case None: {
			connectionEvent(event);
			break;
		}

		// Otherwise pass along to the listeners

		case NodeCreated: {
			for (ZooKeeperListener listener : listeners) {
				listener.nodeCreated(event.getPath());
			}
			break;
		}

		case NodeDeleted: {
			for (ZooKeeperListener listener : listeners) {
				listener.nodeDeleted(event.getPath());
			}
			break;
		}

		case NodeDataChanged: {
			for (ZooKeeperListener listener : listeners) {
				listener.nodeDataChanged(event.getPath());
			}
			break;
		}

		case NodeChildrenChanged: {
			for (ZooKeeperListener listener : listeners) {
				listener.nodeChildrenChanged(event.getPath());
			}
			break;
		}
		}
	}

	private void connectionEvent(WatchedEvent event) {
		// TODO 
	}

}
