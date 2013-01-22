/**
 * 
 */
package org.buhe.hare.common.job;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author buhe
 *
 */
public interface JobFuture {

	void waitForComplete();
	
	Object get();
}
