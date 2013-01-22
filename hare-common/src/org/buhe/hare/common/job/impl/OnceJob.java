/**
 * 
 */
package org.buhe.hare.common.job.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.job.support.JobContext;
import org.buhe.hare.common.job.support.JobType;


/**
 * 执行一次的任务,考虑设置一定超时时间,防止有人写成了循环任务
 * 
 * @author buhe
 * 
 */
public abstract class OnceJob extends AbstractJob {

	private final static Log LOG = LogFactory.getLog(OnceJob.class);
	private static final long serialVersionUID = 2025922778650358577L;
	private long startTime;

	public OnceJob(JobContext ctx) {
		super(ctx);
		this.jobType = JobType.ONCE;
	}

	public final boolean timeout() {
		return System.currentTimeMillis() - startTime > ctx.getTimeout();
	}

	public abstract Object doCall() throws Exception;

	public Object call() throws Exception {
		doBefore();
		Throwable ex = null;
		try {
			try {
				result = doCall();
				return result;
			} catch (Exception e) {
				ex = e; //FIXME - ex 跟 result 一样
				throw e;
			}
		} finally {
			doAfter(result,ex);
		}
	}

	private void doBefore() {
		startTime = System.currentTimeMillis();

	}

	private void doAfter(Object result,Throwable ex) {
		
		if(ctx.isLocal()){ //本地任务不通知
			return ;
		}
		// in vm 通知Handler,任务结束,回收资源
		handler.stop(this);
		
		if(ctx.isCrucial()){
			//TODO - 要能传回结果或者异常
			// rpc 通知Master,任务结束
			this.handlerToMaster.finishJob(id);
		}
		
	}

	@Override
	public void waitComplete() {
//		if(ctx.isLocal()){ //本地任务
//			//TODO
//			return ;
//		}
//		
//		while (true) { // 客户端超时?
//			// 等待Handler执行完毕
//			JobStatus status = this.clientToMaster.getJobStatus(id);
//			LOG.info("Current jobStatus : " + status);
//			if (status == JobStatus.FAIL || status == JobStatus.FINISH) {
//				return;
//			}
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException ignore) {
//			}
//		}
		future.waitForComplete();
	}
	
	@Override
	public Object getResult(){
//		if(ctx.isLocal()){ //本地任务
//			//TODO 
//			return null;
//		}
//		
//		//TODO 性能需要考量
//		if(this.jobContext().isCrucial()){
//			return clientToMaster.getJobResult(this.id);
//		}else{
//			//TODO -  未实现
//			return null;
//		}
		return future.get();
		
	}
	
	

}
