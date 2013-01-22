/**
 * 
 */
package org.buhe.hare.front;

/**
 * Hare的协议适配器和前端转发器,只依赖Client模块的Metadata.cache
 * 尽量小巧稳定,不想本地做业务级合并或抑制
 * 可以有适当的丢弃策略
 * 尽量支持QoS降级?
 * 
 * 不封装成job了,直接协议转发,job-schedule只作为一种协议由client使用
 * 
 * 
 * 一直发送转发,不要求ACK,直到对应handler失效(怎么判断的?心跳?因为可能非阻塞的,e.g. UDP)
 * handler注册成功后同步网元,Front得到通知开始继续转发,期间消息根据retry策略(感觉应该都丢弃..)
 * @author buhe
 *
 */
public class HareFront {

	public HareFront(){
		
	}
}
