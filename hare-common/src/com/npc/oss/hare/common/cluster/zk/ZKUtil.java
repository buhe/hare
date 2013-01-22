/**
 * 
 */
package com.npc.oss.hare.common.cluster.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author buhe
 * 
 */
public class ZKUtil {

	private static final Log LOG = LogFactory.getLog(ZKUtil.class);

	private static final char ZNODE_PATH_SEPARATOR = '/';

	public static String joinZNode(String prefix, String suffix) {
		return prefix + ZNODE_PATH_SEPARATOR + suffix;
	}
}
