/**
 * 
 */
package org.buhe.hare.data.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.data.spi.DataLayerFactory;
import org.buhe.hare.data.spi.EnterpriseDataLayer;
import org.jboss.netty.util.internal.ConcurrentHashMap;


/**
 * 数据层注册表
 * 
 * @author buhe
 * 
 */
public class DataLayerFactoryRegistry {
	private final static Log LOG = LogFactory.getLog(DataLayerFactoryRegistry.class);

	public final static DataLayerFactoryRegistry REGISTRY = new DataLayerFactoryRegistry();
	private final ConcurrentHashMap<Class<? extends EnterpriseDataLayer>, DataLayerFactory> registry = new ConcurrentHashMap<Class<? extends EnterpriseDataLayer>, DataLayerFactory>();

	public void register(Class<? extends EnterpriseDataLayer> key,DataLayerFactory value) {
		registry.putIfAbsent(key, value);
	}
	
	public DataLayerFactory get(Class<? extends EnterpriseDataLayer> key){
		return registry.get(key);
	}

	public void unregister(Class<? extends EnterpriseDataLayer> key) {
		registry.remove(key);
	}
}
