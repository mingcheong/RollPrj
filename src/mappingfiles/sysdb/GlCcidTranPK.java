package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlCcidTranPK implements Serializable {

	/** identifier field */
	private String sccid;

	/** identifier field */
	private String tccid;

	/** full constructor */
	public GlCcidTranPK(String sccid, String tccid) {
		this.sccid = sccid;
		this.tccid = tccid;
	}

	/** default constructor */
	public GlCcidTranPK() {
	}

	public String getSccid() {
		return this.sccid;
	}

	public void setSccid(String sccid) {
		this.sccid = sccid;
	}

	public String getTccid() {
		return this.tccid;
	}

	public void setTccid(String tccid) {
		this.tccid = tccid;
	}

	public String toString() {
		return new ToStringBuilder(this).append("sccid", getSccid()).append(
				"tccid", getTccid()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GlCcidTranPK))
			return false;
		GlCcidTranPK castOther = (GlCcidTranPK) other;
		return new EqualsBuilder()
				.append(this.getSccid(), castOther.getSccid()).append(
						this.getTccid(), castOther.getTccid()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSccid()).append(getTccid())
				.toHashCode();
	}

}
