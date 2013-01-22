/**
 * 
 */
package org.buhe.hare.common.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author buhe
 *
 */
public class HanlderMetadata implements Serializable{

	private static final long serialVersionUID = 6475833279482367718L;
	private final static Log LOG = LogFactory.getLog(HanlderMetadata.class);
	public final static String JOB_SCHEDULE_PROTOCOL = "job-schedule";
	public final static String SNMP_PROTOCOL = "snmp";
	public final static String NETCONF_PROTOCOL = "netconf";
	
	private String ip;
	/**
	 * 协议和端口映射
	 */
	private Map<String,Integer> ports = new HashMap<String,Integer>();
	/**
	 * 端口偏移 ?
	 */
	private int offset = 0;
	private String handlerDescCache;
	public HanlderMetadata(String ip) {
		super();
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Map<String, Integer> getPorts() {
		return ports;
	}
	public void setPorts(Map<String, Integer> ports) {
		this.ports = ports;
	}
	
	public void addPort(String protocol,int port){
		ports.put(protocol, port);
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + offset;
		result = prime * result + ((ports == null) ? 0 : ports.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HanlderMetadata other = (HanlderMetadata) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (offset != other.offset)
			return false;
		if (ports == null) {
			if (other.ports != null)
				return false;
		} else if (!ports.equals(other.ports))
			return false;
		return true;
	}
	
	public String getHandlerDesc(){
		if(handlerDescCache == null){
			handlerDescCache = "H"+ip+":"+(int)(ports.get(JOB_SCHEDULE_PROTOCOL)+this.offset);
			LOG.debug("Handler Desc : " + handlerDescCache);
		}
		return handlerDescCache;
	}
	@Override
	public String toString() {
		return "HanlderMetadata [ip=" + ip + ", ports=" + ports + ", offset="
				+ offset + "]";
	}
	
	
	

}
