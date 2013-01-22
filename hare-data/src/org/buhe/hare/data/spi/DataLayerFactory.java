/**
 * 
 */
package org.buhe.hare.data.spi;
import org.buhe.hare.common.configuration.ConfigurationImpl;
/**
 * @author buhe
 *
 */
public interface DataLayerFactory {

	/**
	 * EnterpriseDataLayer 实例内部处理是否缓存,线程安全自行处理
	 * @param configuration
	 * @return
	 */
	EnterpriseDataLayer create(ConfigurationImpl configuration);
}
