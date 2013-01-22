/**
 * 
 */
package org.buhe.hare.common.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.metadata.storage.MetaDataStorage;


/**
 * @author buhe
 * 
 */
public class Metadata implements Serializable {

	private static final long serialVersionUID = 4459351126615191136L;
	private final static Log LOG = LogFactory.getLog(Metadata.class);
	private final ConcurrentMap<NeIdentifier, HanlderMetadata> dnTohandler = new ConcurrentHashMap<NeIdentifier, HanlderMetadata>();
	private final CopyOnWriteArrayList<HanlderMetadata> handlers = new CopyOnWriteArrayList<HanlderMetadata>();
	/**
	 * 持久化在Master failover时使用
	 */
	private MetaDataStorage storage;

	public Metadata() {

	}

	public void syncStorage() {
		storage = MetaDataStorage.load();
	}

	public HanlderMetadata putIfAbsent(NeIdentifier key, HanlderMetadata value) {

		HanlderMetadata old = dnTohandler.putIfAbsent(key, value);
		if (old == null) {
			// storage add
		}
		return old;
	}

	public HanlderMetadata remove(NeIdentifier key) {
		HanlderMetadata removed = dnTohandler.remove(key);
		if (removed != null) {
			// storage remove
		}
		return removed;
	}

	public HanlderMetadata get(NeIdentifier key) {
		HanlderMetadata handlerMetadata = dnTohandler.get(key);
		synchronized (handlers) {
			if (handlers.contains(handlerMetadata)) {
				return handlerMetadata;
			} else {
				dnTohandler.remove(key);
				return null;
			}
		}
	}

	public boolean addHandler(HanlderMetadata md) {
		synchronized (handlers) {
			return handlers.add(md);
		}
	}

	public boolean removeHandler(HanlderMetadata md) {
		synchronized (handlers) {
			return handlers.remove(md);
		}
	}

	public List<HanlderMetadata> getHandlers() {
		return Collections.unmodifiableList(handlers);
	}

	public void updateHandlers(List<String> current) {
		CopyOnWriteArrayList<HanlderMetadata> result = new CopyOnWriteArrayList<HanlderMetadata>();
		synchronized (handlers) {
			for (HanlderMetadata h : handlers) {
				if (current.contains(h.getHandlerDesc())) {
					result.add(h);
				}
			}
			handlers.clear();
			handlers.addAll(result);
			LOG.debug("Update handler node alive list : " + handlers);
		}

	}

}
