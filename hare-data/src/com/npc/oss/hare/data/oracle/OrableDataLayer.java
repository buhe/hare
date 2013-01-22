/**
 * 
 */
package com.npc.oss.hare.data.oracle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.npc.oss.domainmodel.cm.MO;
import com.npc.oss.hare.data.spi.EnterpriseDataLayer;
/**
 * 直接查询数据库
 * @author buhe
 *
 */
public class OrableDataLayer implements EnterpriseDataLayer {
	private final static Log LOG = LogFactory.getLog(OrableDataLayer.class);

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.EnterpriseDataLayer#findMOByDn(java.lang.String)
	 */
	@Override
	public MO findMOByDn(String dn) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.EnterpriseDataLayer#findMOsByDns(java.lang.String[])
	 */
	@Override
	public MO[] findMOsByDns(String[] dns) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.EnterpriseDataLayer#addMO(com.npc.oss.domainmodel.cm.MO)
	 */
	@Override
	public boolean addMO(MO mo) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.EnterpriseDataLayer#removeMOByDn(java.lang.String)
	 */
	@Override
	public boolean removeMOByDn(String dn) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.npc.oss.hare.data.EnterpriseDataLayer#modifyMO(com.npc.oss.domainmodel.cm.MO, com.npc.oss.domainmodel.cm.MO)
	 */
	@Override
	public boolean modifyMO(MO oldMO, MO newMO) {
		// TODO Auto-generated method stub
		return false;
	}
}
