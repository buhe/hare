/**
 * 
 */
package org.buhe.hare.common.util;

/**
 * @author buhe
 *
 */
public class RefectHelper {

	public static  Class loadClass(String className){
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			//TODO
			e.printStackTrace();
		}
		return clazz;
	}
	
	public static <T> T NEW(Class<T> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
