/**
 * 
 */
package org.buhe.hare.common.job.impl;

import org.buhe.hare.common.job.support.CycleJobResult;
import org.buhe.hare.common.job.support.JobContext;
import org.buhe.hare.common.job.support.JobType;

/**
 * 带有执行周期的循环任务
 * 一般是贴近网元的任务,要在网元处理器执行
 * @author buhe
 *
 */
public abstract class CycleJob extends AbstractJob {

	private static final long serialVersionUID = 5532121106471173645L;
	
	public CycleJob(JobContext ctx) {
		super(ctx);
		this.jobType = JobType.CYCLE;
	}

	public Object call() throws Exception{
		while(true){
			try{
				doLoop();
			}catch(Throwable t){
				//LOG
				//策略,如果失败的次数过多后续处理(停掉,等一会?)
			}
			
		}
		//不会返回
	}
	
	@Override
	public void waitComplete(){//循环任务立即返回
	}
	
	/**
	 * 循环调用
	 */
	public abstract void doLoop();
	
	@Override
	public Object getResult(){
		return CycleJobResult.INSTANCE;
	}

}
