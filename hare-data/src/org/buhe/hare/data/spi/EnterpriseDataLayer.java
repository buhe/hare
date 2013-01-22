/**
 * 
 */
package org.buhe.hare.data.spi;

import com.npc.oss.domainmodel.cm.MO;

/**
 * 业务数据层,由底层存储实现,层次比较简单,只用了两层,没有使用数据汇聚功能
 * 比如把不同的数据源汇聚到一起
 * @author buhe
 *
 */
public interface EnterpriseDataLayer {

	MO findMOByDn(String dn);
	
	MO[] findMOsByDns(String[] dns);
	
	boolean addMO(MO mo);
	
	boolean removeMOByDn(String dn);
	
	boolean modifyMO(MO oldMO,MO newMO);
}
