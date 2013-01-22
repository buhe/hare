/**
 * 
 */
package org.buhe.hare.master.balance.impl;

import java.util.List;
import java.util.Random;

import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.metadata.HanlderMetadata;
import org.buhe.hare.common.metadata.Metadata;
import org.buhe.hare.common.metadata.NeIdentifier;
import org.buhe.hare.master.balance.Balancer;


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
