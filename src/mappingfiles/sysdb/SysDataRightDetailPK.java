package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysDataRightDetailPK implements Serializable {

	/** identifier field */
	private String rightDetailId;

	/** identifier field */
	private String rightId;

	/** full constructor */
	public SysDataRightDetailPK(String rightDetailId, String rightId) {
		this.rightDetailId = rightDetailId;
		this.rightId = rightId;
	}

	/** default constructor */
	public SysDataRightDetailPK() {
	}

	public String getRightDetailId() {
		return this.rightDetailId;
	}

	public void setRightDetailId(String rightDetailId) {
		this.rightDetailId = rightDetailId;
	}

	public String getRightId() {
		return this.rightId;
	}

	public void setRightId(String rightId) {
		this.rightId = rightId;
	}

	public String toString() {
		return new ToStringBuilder(this).append("rightDetailId",
				getRightDetailId()).append("rightId", getRightId()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SysDataRightDetailPK))
			return false;
		SysDataRightDetailPK castOther = (SysDataRightDetailPK) other;
		return new EqualsBuilder().append(this.getRightDetailId(),
				castOther.getRightDetailId()).append(this.getRightId(),
				castOther.getRightId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getRightDetailId()).append(
				getRightId()).toHashCode();
	}

}
