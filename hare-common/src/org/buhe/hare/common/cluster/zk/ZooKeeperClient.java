/**
 * 
 */
package org.buhe.hare.common.cluster.zk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * Zookeeper的Watcher是一次性的Watcher,当被回调后如需继续Watcher需要再次设置
 * 
 * @author buhe
 * 
 */
public class ZooKeeperClient {

	private ZooKeeper zk;
	private ZooKeeperWatcher watcher;

	public ZooKeeperClient(String connectString, int sessionTimeout,
			ZooKeeperWatcher watcher) throws IOException {
		this.zk = new ZooKeeper(connectString, sessionTimeout, watcher);
		this.watcher = watcher;
	}

	/**
	 * 创建临时znode,在创建的客户端session结束时,ZK会删除
	 * 
	 * @param node
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean createEphemeralNode(String node, byte[] data)
			throws KeeperException, InterruptedException {
		return zk.create(node, data, createACL(watcher, node),
				CreateMode.EPHEMERAL) != null;
	}

	/**
	 * 创建持久化znode
	 * 
	 * @param node
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean createPersistentNode(String node, byte[] data)
			throws KeeperException, InterruptedException {
		return zk.create(node, data, createACL(watcher, node),
				CreateMode.PERSISTENT) != null;
	}

	public void deleteNode(String node) throws InterruptedException,
			KeeperException {
		zk.delete(node, -1);
	}

	/**
	 * 查看node节点是否存在,并观察(Watch)节点增删的下一次事件
	 * 
	 * @param node
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public Stat existsAndWatch(String node) throws KeeperException,
			InterruptedException {
		return zk.exists(node, watcher);
	}

	public Stat exists(String node) throws KeeperException,
			InterruptedException {
		return zk.exists(node, false);
	}

	/**
	 * 查看node节点的数据,并观察(Watch)节点数据变化的下一次事件
	 * 
	 * @param node
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] getDataAndWatch(String node) throws KeeperException,
			InterruptedException {
		return zk.getData(node, watcher, null);
	}

	/**
	 * 查看node节点的子节点,并观察(Watch)节点的子节点变化的下一次事件
	 * 
	 * @param node
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChildrenAndWatch(String node)
			throws KeeperException, InterruptedException {
		return zk.getChildren(node, watcher);
	}

	public List<String> getChildren(String node) throws KeeperException,
			InterruptedException {
		return zk.getChildren(node, false);
	}

	public void delete(String node) throws InterruptedException,
			KeeperException {
		zk.delete(node, -1);
	}

	public List<String> listChildrenAndWatchThem(String znode)
			throws KeeperException {
		List<String> children = listChildrenAndWatchForNewChildren(znode);
		if (children == null) {
			return null;
		}
		for (String child : children) {
			watchAndCheckExists(watcher, ZKUtil.joinZNode(znode, child));
		}
		return children;
	}

	public List<String> listChildrenAndWatchForNewChildren(String znode)
			throws KeeperException {
		try {
			List<String> children = zk.getChildren(znode, watcher);
			return children;
		} catch (KeeperException.NoNodeException ke) {
			return null;
		} catch (KeeperException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	public boolean watchAndCheckExists(ZooKeeperWatcher zkw, String znode)
			throws KeeperException {
		try {
			Stat s = zk.exists(znode, zkw);
			boolean exists = s != null ? true : false;
			return exists;
		} catch (KeeperException e) {
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}

	private ArrayList<ACL> createACL(ZooKeeperWatcher zkw, String node) {
		return Ids.OPEN_ACL_UNSAFE;
	}
}
