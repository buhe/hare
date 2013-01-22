/**
 * 
 */
package org.buhe.hare.common.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.buhe.hare.common.rpc.netty.NettyRpcProxyFactory;


/**
 * @author buhe
 *
 */
public class ConfigurationImpl {

	private final static String DEFAULT_MASTER_ZNODE = "/master";
	private final static String DEFAULT_HANDLER_ZNODE = "/handler";
	private final static String DEFAULT_PERSIST_JOB_ZNODE = "/persist_job";
	private final static String DEFAULT_EPHEMERAL_JOB_ZNODE = "/ephemeral_job";
	private static String DEFAULT_HANDLER_IP;
	static{
		try {
			DEFAULT_HANDLER_IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ignore) {
		}
	}
	//TODO - 改成String?
	private final static Class<?> DEFAULT_RPC_PROXY_FACTORY = NettyRpcProxyFactory.class;
	private final static String DEFAULT_BALANCER = "com.npc.oss.hare.master.balance.impl.RandomBalancer";
	
	private String zooKeeperIp;
	private int zooKeeperPort;
	private String masterZnode = DEFAULT_MASTER_ZNODE;
	private String handlerZnode = DEFAULT_HANDLER_ZNODE;
	private String persistJobZnode = DEFAULT_PERSIST_JOB_ZNODE;
	private String ephemeralJobZnode = DEFAULT_EPHEMERAL_JOB_ZNODE;
	private Class<?> rpcProxyFactoryClass = DEFAULT_RPC_PROXY_FACTORY;
	private String balancer = DEFAULT_BALANCER;
	private HandlerConfiguration handlerConfiguration;
	private ClientConfiguration clientConfiguration;

	//TODO - 考虑放到ZK的data中
	private String masteNodeIp;
	private int masterNodePort;
	
	private String handlerNodeIp = DEFAULT_HANDLER_IP;
	private int handlerNodePort;
	
	public ConfigurationImpl(){		
	}
	
	public String getMasteNodeIp() {
		return masteNodeIp;
	}

	public void setMasteNodeIp(String masteNodeIp) {
		this.masteNodeIp = masteNodeIp;
	}

	public int getMasterNodePort() {
		return masterNodePort;
	}

	public void setMasterNodePort(int masterNodePort) {
		this.masterNodePort = masterNodePort;
	}

	public Class<?> getRpcProxyFactoryClass() {
		return rpcProxyFactoryClass;
	}

	public void setRpcProxyFactoryClass(Class<?> rpcProxyFactoryClass) {
		this.rpcProxyFactoryClass = rpcProxyFactoryClass;
	}

	public String getMasterZnode() {
		return masterZnode;
	}

	public void setMasterZnode(String masterZnode) {
		this.masterZnode = masterZnode;
	}

	public String getHandlerZnode() {
		return handlerZnode;
	}

	public void setHandlerZnode(String handlerZnode) {
		this.handlerZnode = handlerZnode;
	}

	public String getZooKeeperIp() {
		return zooKeeperIp;
	}

	public void setZooKeeperIp(String zooKeeperIp) {
		this.zooKeeperIp = zooKeeperIp;
	}

	public int getZooKeeperPort() {
		return zooKeeperPort;
	}

	public void setZooKeeperPort(int zooKeeperPort) {
		this.zooKeeperPort = zooKeeperPort;
	}

	public String getHandlerNodeIp() {
		return handlerNodeIp;
	}

	public void setHandlerNodeIp(String handlerNodeIp) {
		this.handlerNodeIp = handlerNodeIp;
	}

	public int getHandlerNodePort() {
		return handlerNodePort;
	}

	public void setHandlerNodePort(int handlerNodePort) {
		this.handlerNodePort = handlerNodePort;
	}
	
	public String getBalancer() {
		return balancer;
	}

	public void setBalancer(String balancer) {
		this.balancer = balancer;
	}

	public String getPersistJobZnode() {
		return persistJobZnode;
	}

	public void setPersistJobZnode(String persistJobZnode) {
		this.persistJobZnode = persistJobZnode;
	}

	public String getEphemeralJobZnode() {
		return ephemeralJobZnode;
	}

	public void setEphemeralJobZnode(String ephemeralJobZnode) {
		this.ephemeralJobZnode = ephemeralJobZnode;
	}

	public HandlerConfiguration getHandlerConfiguration() {
		return handlerConfiguration;
	}

	public void setHandlerConfiguration(HandlerConfiguration handlerConfiguration) {
		this.handlerConfiguration = handlerConfiguration;
	}

	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}

	public void setClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}
	
	
}
