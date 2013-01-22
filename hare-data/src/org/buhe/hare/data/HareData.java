/**
 * 
 */
package org.buhe.hare.data;

import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.data.mixd.MixdDataLayer;
import org.buhe.hare.data.spi.DataLayerFactory;
import org.buhe.hare.data.spi.EnterpriseDataLayer;
import org.buhe.hare.data.support.DataLayerFactoryRegistry;


/**
 * Hare访问数据的门面,用于访问各种外部存储,包括传统DB,NoSQL,Cache等
 * 对Hare提供业务上的接口,会依赖类似MO这样的领域模型
 * @author buhe
 *
 */
public class HareData {
	private ConfigurationImpl configuration;
	public HareData(ConfigurationImpl configuration){
		this.configuration = configuration;
	}
	
	/**
	 * 获取系统中注册的数据层
	 * @param clazz
	 * @return
	 */
	public EnterpriseDataLayer obtainDataLayer(Class<? extends EnterpriseDataLayer> clazz){
		DataLayerFactory factory = DataLayerFactoryRegistry.REGISTRY.get(clazz);
		return factory.create(configuration);
	}
	
	/**
	 * 混合多个数据层
	 * @param dataLayers
	 * @return
	 */
	public EnterpriseDataLayer mixDataLayer(EnterpriseDataLayer ... dataLayers){
		return new MixdDataLayer(dataLayers);
	}
}
