/**
 * 
 */
package org.buhe.hare.common.util;

/**
 * @author buhe
 *
 */
public class Assert {
	
	public static void assertNotNull(String message,Object i){
		if(i == null){
			throw new IllegalArgumentException(message);
		}
	}
}
