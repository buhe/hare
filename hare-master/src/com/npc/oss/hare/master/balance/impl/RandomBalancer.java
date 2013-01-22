/**
 * 
 */
package com.npc.oss.hare.master.balance.impl;

import java.util.List;
import java.util.Random;

import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.metadata.Metadata;
import com.npc.oss.hare.common.metadata.NeIdentifier;
import com.npc.oss.hare.master.balance.Balancer;
import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;

/**
 * 随机均衡器
 * @author buhe
 *
 */
public class RandomBalancer implements Balancer {

	Random r = new Random(20041212);
	/* (non-Javadoc)
	 * @see com.npc.oss.hare.master.balance.Balancer#select(com.npc.oss.hare.common.metadata.Metadata, com.npc.oss.hare.common.metadata.NeIdentifier)
	 */
	@Override
	public HanlderMetadata select(Metadata md, NeIdentifier neIdentifier)throws NotAnyHandlerException {
		if(md.getHandlers().isEmpty()){
			throw new NotAnyHandlerException();
		}
		List<HanlderMetadata> mds = md.getHandlers();
		return mds.get(r.nextInt(mds.size()));
	}

}
