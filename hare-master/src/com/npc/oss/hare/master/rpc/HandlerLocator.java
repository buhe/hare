/**
 * 
 */
package com.npc.oss.hare.master.rpc;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.common.HandlerService;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.common.rpc.RpcClient;
/**
 * @author buhe
 *
 */
public class HandlerLocator {
	private final static Log LOG = LogFactory.getLog(HandlerLocator.class);
	private final ConcurrentHashMap<HanlderMetadata, HandlerService> proxyCache = new ConcurrentHashMap<HanlderMetadata, HandlerService>();
	private ConfigurationImpl configuration;
	public HandlerLocator(ConfigurationImpl configuration){
		this.configuration = configuration;
	}
	public HandlerService lookup(HanlderMetadata meta){
		if(proxyCache.containsKey(meta)){
			LOG.debug("Found handler proxy in cache for " + meta);
			return proxyCache.get(meta);
		}
		String ip = meta.getIp();
		int rpcPort = meta.getPorts().get(HanlderMetadata.JOB_SCHEDULE_PROTOCOL) + meta.getOffset();
		RpcConfiguration rpc = new RpcConfiguration(ip,rpcPort,"HANDLER");
		RpcClient rpcClient = new HareRPC(configuration).getRpcClient(rpc);
		HandlerService handler = rpcClient.getProxy(HandlerService.class);
		LOG.debug("Connected " + meta);
		HandlerService old = proxyCache.putIfAbsent(meta, handler);
		if(old != null){
			safeClose(rpcClient);
			return old;
		}else{
			return handler;
		}
		
	}
	private void safeClose(RpcClient client) {
		try{
			client.close();
		}catch(Exception ignore){
			
		}
		
	}
}
