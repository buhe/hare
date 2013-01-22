/**
 * 
 */
package com.npc.oss.hare.client;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.hare.client.metacache.MetaDataCache;
import com.npc.oss.hare.common.MasterInterface;
import com.npc.oss.hare.common.configuration.ClientConfiguration;
import com.npc.oss.hare.common.configuration.ConfigurationImpl;
import com.npc.oss.hare.common.configuration.RpcConfiguration;
import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;
import com.npc.oss.hare.common.job.Job;
import com.npc.oss.hare.common.job.impl.LocalJobFuture;
import com.npc.oss.hare.common.job.impl.MasterJobFuture;
import com.npc.oss.hare.common.job.support.JobType;
import com.npc.oss.hare.common.rpc.HareRPC;
import com.npc.oss.hare.common.rpc.RpcClient;
import com.npc.oss.hare.common.util.NamedThreadFactory;

/**
 * 客户端访问入口
 * 
 * @author buhe
 * 
 */
public class HareClient {
	private final static Log LOG = LogFactory.getLog(HareClient.class);

	private static MasterInterface master;
	private static MetaDataCache metaCache;
	private static RpcClient rpcClient;
	private static Set<HareClient> clients = new HashSet<HareClient>();
	private static ExecutorService localExecutor;

	private ConfigurationImpl configuration;
	private ClientConfiguration clientConfiguration;

	public HareClient(ConfigurationImpl configuration) {
		this.configuration = configuration;
		if (configuration.getClientConfiguration() == null) {
			clientConfiguration = new ClientConfiguration();
		} else {
			clientConfiguration = configuration.getClientConfiguration();
		}
		if (metaCache == null) {
			metaCache = new MetaDataCache();
		}
		initializeRPC();
		initializeLocalExecutor();
		clients.add(this);
	}

	private void initializeLocalExecutor() {
		localExecutor = Executors.newFixedThreadPool(
				clientConfiguration.getLocalExecutorCount(),
				new NamedThreadFactory("local-executor-"));
	}

	private void initializeRPC() {
		HareRPC hareRPC = new HareRPC(configuration);
		RpcConfiguration rpc = new RpcConfiguration(
				configuration.getMasteNodeIp(),
				configuration.getMasterNodePort(), "");
		if (rpcClient == null) {
			rpcClient = hareRPC.getRpcClient(rpc);
			master = rpcClient.getProxy(MasterInterface.class);
		}
	}

	public boolean execute(Job job) throws NotAnyHandlerException {
		if (job.jobContext().isLocal()) {
			return dispatchLocalThreadPool(job);
		} else {
			if (job.jobContext().isCrucial()) {
				return dispatchMasterNode(job);
			} else {
				return dispatchHandlerNode(job);
			}
		}
		// TODO - 放到master中

	}

	private boolean dispatchMasterNode(Job job) {
		// 设置Master Proxy给job
		job.clientToMasterProxy(master);
		job.future(new MasterJobFuture(master, job.id()));
		master.execute(job);
		return false;

	}

	private boolean dispatchLocalThreadPool(Job job) {
		if (job.jobType() == JobType.CYCLE) {
			// 循环任务不能在本地执行
			throw new IllegalStateException(
					"Job is cycle type,you should set local to false.");
		}
		Future<Object> f = localExecutor.submit(job);
		job.future(new LocalJobFuture(f));
		return true;

	}

	private boolean dispatchHandlerNode(Job job) {
		// HandlerInterface handler = null;
		// try {
		// if (metaCache.containsKey(job.neIdentifier())) {
		// handler = metaCache.get(job.neIdentifier());
		// } else {
		// handler = connectHandler(job.neIdentifier());
		// }
		// } catch (Exception e) {
		//
		// }

		// if (handler == null) {
		// // TODO - retry policy? retry once
		// handler = connectHandler(job.neIdentifier());
		//
		// }
		// if (handler == null) {
		// throw new NotAnyHandlerException();
		// }
		// try {
		// //TODO - 通过Master调度还是获取Handler提交，这是个问题
		// //但是master无论如何都要知道任务，并跟踪任务，并能重新调度任务
		// handler.execute(job);
		// } catch (RpcException e) {
		// //TODO - 分异常处理,业务和RPC
		// // TODO - retry policy? retry once
		// handler = connectHandler(job.neIdentifier());
		// handler.execute(job);
		// } catch(Exception e){
		// LOG.error(e);
		// }

		return false;

	}

	// private HandlerInterface connectHandler(NeIdentifier neIdentifier) {
	// HanlderMetadata handlerMeta = master.resolveHandlerNode(neIdentifier);
	// RpcConfiguration handlerRpc = new RpcConfiguration(handlerMeta.getIp(),
	// handlerMeta.getPorts().get(
	// HanlderMetadata.JOB_SCHEDULE_PROTOCOL), "HANDLER");
	// HandlerInterface handler = hareRPC.getRpcClient(handlerRpc).getProxy(
	// HandlerInterface.class);
	// metaCache.putIfAbsent(neIdentifier, handler);
	// return handler;
	// }

	public void close() {
		clients.remove(this);
		if (clients.isEmpty()) {
			// disconnect rpc channel
			rpcClient.close();
		}
	}
}
