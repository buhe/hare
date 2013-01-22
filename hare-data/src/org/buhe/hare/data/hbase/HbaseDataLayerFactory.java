/**
 * 
 */
package org.buhe.hare.data.hbase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.buhe.hare.common.configuration.ConfigurationImpl;
import org.buhe.hare.data.spi.DataLayerFactory;
import org.buhe.hare.data.spi.EnterpriseDataLayer;

/**
 * @author buhe
 *
 */
public class HbaseDataLayerFactory implements DataLayerFactory {
	private final static Log LOG = LogFactory
			.getLog(HbaseDataLayerFactory.class);

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.DataLayerFactory#create(com.npc.oss.hare.common.configuration.ConfigurationImpl)
	 */
	@Override
	public EnterpriseDataLayer create(ConfigurationImpl configuration) {
		HbaseDataLayer hdl = new HbaseDataLayer(configuration);
		return hdl;
	}
}
