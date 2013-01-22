/**
 * 
 */
package org.buhe.hare.common.metadata;

import java.io.Serializable;

/**
 * @author buhe
 *
 */
public class NeIdentifier implements Serializable{

	private static final long serialVersionUID = -5219368578195489119L;
	private String dn;
	private String version;
	private String type;
	public NeIdentifier(String dn, String version, String type) {
		super();
		this.dn = dn;
		this.version = version;
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dn == null) ? 0 : dn.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NeIdentifier other = (NeIdentifier) obj;
		if (dn == null) {
			if (other.dn != null)
				return false;
		} else if (!dn.equals(other.dn))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NeIdentifier [dn=" + dn + ", version=" + version + ", type="
				+ type + "]";
	}
	
	
	
	
}
