/**
 * 
 */
package org.buhe.hare.master.balance;

import org.buhe.hare.common.exception.handler.NotAnyHandlerException;
import org.buhe.hare.common.metadata.HanlderMetadata;
import org.buhe.hare.common.metadata.Metadata;
import org.buhe.hare.common.metadata.NeIdentifier;


/**
 * @author buhe
 *
 */
public interface Balancer {

	HanlderMetadata select(Metadata md,NeIdentifier neIdentifier) throws NotAnyHandlerException;
}
