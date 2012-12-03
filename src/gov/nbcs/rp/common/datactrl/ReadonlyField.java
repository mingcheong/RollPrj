package gov.nbcs.rp.common.datactrl;

import java.util.Date;

public class ReadonlyField {
	private Field field;

	public ReadonlyField(Field field) {
		super();
		this.field = field;
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getBoolean()
	 */
	public boolean getBoolean() throws Exception {
		return field.getBoolean();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getByte()
	 */
	public byte getByte() throws Exception {
		return field.getByte();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getCurrency()
	 */
	public String getCurrency() throws Exception {
		return field.getCurrency();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getDate()
	 */
	public Date getDate() throws Exception {
		return field.getDate();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getDateTimeString()
	 */
	public String getDateTimeString() throws Exception {
		return field.getDateTimeString();
	}

	/**
	 * @param format
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getDateTimeString(java.lang.String)
	 */
	public String getDateTimeString(String format) throws Exception {
		return field.getDateTimeString(format);
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getDouble()
	 */
	public double getDouble() throws Exception {
		return field.getDouble();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getFloat()
	 */
	public float getFloat() throws Exception {
		return field.getFloat();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getInteger()
	 */
	public int getInteger() throws Exception {
		return field.getInteger();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getLong()
	 */
	public long getLong() throws Exception {
		return field.getLong();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#getName()
	 */
	public String getName() {
		return field.getName();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getNumber()
	 */
	public Number getNumber() throws Exception {
		return field.getNumber();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#getOldValue()
	 */
	public Object getOldValue() {
		return field.getOldValue();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#getPreviousValue()
	 */
	public Object getPreviousValue() {
		return field.getPreviousValue();
	}

	/**
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getString()
	 */
	public String getString() throws Exception {
		return field.getString();
	}

	/**
	 * @param format
	 * @return
	 * @throws Exception
	 * @see gov.nbcs.rp.common.datactrl.Field#getString(java.lang.String)
	 */
	public String getString(String format) throws Exception {
		return field.getString(format);
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#getValue()
	 */
	public Object getValue() {
		return field.getValue();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#getValueType()
	 */
	public Class getValueType() {
		return field.getValueType();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#isLatestModified()
	 */
	public boolean isLatestModified() {
		return field.isLatestModified();
	}

	/**
	 * @return
	 * @see gov.nbcs.rp.common.datactrl.Field#isModified()
	 */
	public boolean isModified() {
		return field.isModified();
	}
}
