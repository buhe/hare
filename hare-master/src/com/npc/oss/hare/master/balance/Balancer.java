/**
 * 
 */
package com.npc.oss.hare.master.balance;

import com.npc.oss.hare.common.metadata.HanlderMetadata;
import com.npc.oss.hare.common.metadata.Metadata;
import com.npc.oss.hare.common.metadata.NeIdentifier;
import com.npc.oss.hare.common.exception.handler.NotAnyHandlerException;

/**
 * @author buhe
 *
 */
public interface Balancer {

	HanlderMetadata select(Metadata md,NeIdentifier neIdentifier) throws NotAnyHandlerException;
}
