package org.buhe.hare.client.metacache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.buhe.hare.common.HandlerInterface;
import org.buhe.hare.common.metadata.NeIdentifier;



/**
 * @author buhe
 *
 */
public class MetaDataCache {

	private final ConcurrentMap<NeIdentifier, HandlerInterface> dnTohandler = new ConcurrentHashMap<NeIdentifier, HandlerInterface>();
	public MetaDataCache(){
		
	}

	public HandlerInterface putIfAbsent(NeIdentifier key, HandlerInterface value) {
		
		HandlerInterface old = dnTohandler.putIfAbsent(key, value);
		if(old == null){
			//storage add
		}
		return old;
	}
	
	public boolean containsKey(NeIdentifier key){
		return dnTohandler.containsKey(key);
	}

	public HandlerInterface remove(NeIdentifier key) {
		HandlerInterface removed =  dnTohandler.remove(key);
		if(removed != null){
			//storage remove
		}
		return removed;
	}

	public HandlerInterface get(NeIdentifier key) {
		return dnTohandler.get(key);
	}
	
	
}