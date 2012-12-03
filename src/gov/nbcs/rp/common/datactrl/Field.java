/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.Populator;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Field implements Serializable, Cloneable {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5264155137038857498L;

	/**
     * ���Ҹ�ʽ����ʾ�Ĺ�����
     */
    private static NumberFormat currencyFormatter = NumberFormat
            .getCurrencyInstance(Locale.getDefault());

    /**
     * �ֶ�ֵ
     */
    private Object value;

    /**
     * �ֶ���
     */
    private String name;

    /**
     * �ֶ�ԭʼֵ
     */
    Object oldValue;

    /**
     * �ֶ���ֵ
     */
    Object newValue;

    /**
     * ���������ݼ�����
     */
    DataSet dataSet;
    
    /**
     * ��������ݼ�¼����
     */
    Record record;

    /**
     * ���β���֮ǰ���ֶ�ֵ
     */
    Object previousValue;

    /**
     *
     */
    Class valueType;

    private boolean locked = false;

    /**
     * java.lang�����value�ֶζ���
     */
    private static java.lang.reflect.Field valueField;
    static {
        try {
            valueField = Field.class.getDeclaredField("value");
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    /**
     * ���ֶ�������Field
     *
     * @param name
     *            �ֶ���
     * @param value
     *            �ֶ�ֵ
     */
    public Field(String name, Class valueType) {
        this(name, null, valueType);
    }

    /**
     * ���ֶ������ֶ�ֵ����Field
     *
     * @param name
     *            �ֶ���
     * @param value
     *            �ֶ�ֵ
     */
    public Field(String name, Object value, Class valueType) {
        previousValue = this.newValue = this.oldValue = this.value = value;
        this.name = name;
        this.valueType = valueType;
    }

    /**
     * �ָ�ԭʼֵ��ȡ���޸�
     */
    void undo() throws Exception {
        setValue(this.oldValue);
    }

    /**
     * �ָ���ֵ�������޸�
     */
    void redo() throws Exception {
        setValue(this.newValue);
    }

    /**
     * ��ȡ�ֶ���
     *
     * @return �ֶ���
     */
    public String getName() {
        return name;
    }

    /**
     * �����ֶγ�ʼֵ
     *
     * @return
     */
    public Object getOldValue() {
        return this.oldValue;
    }

    /**
     * �õ����β���֮ǰ��value
     *
     * @return
     */
    public Object getPreviousValue() {
        return this.previousValue;
    }

    /**
     * �����ֶ���
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ��ȡ�ֶ�ֵ
     *
     * @return �����ֶ�ֵ
     */
    public Object getValue() {
        return value;
    }

    /**
     * �����ֶ�ֵ
     *
     * @param val
     *            �ֶ�ֵ
     * @throws Exception
     */
    public void setValue(Object val) throws Exception {
        this.setValue(val, null, false);
    }
    
    /**
	 * Sets the value silently. ��ֵ�����������¼�
	 * 
	 * @param val
	 *            the new value silently
	 * @throws Exception
	 *             the exception
	 */
    public void setValueSilently(Object val) throws Exception {
        this.setValue(val, null, true);
    }

    /**
     * �����ֶ�ֵ
     *
     * @param val
     *            �ֶ�ֵ
     *
     * @param format
     *            ����ת��ʱ��Ҫ�ĸ�ʽ����Ϣ
     * @throws Exception
     */
    public void setValue(Object val, String format, boolean isSilent) throws Exception {
        if (((dataSet.getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT)
                && !locked) {
            this.previousValue = this.value;
            if ((valueType != null) && (val != null)) {
                this.value = Populator.getPopulator(this.valueType,
                        val.getClass()).populate(this, valueField, val, format);
            } else {
            	if(val != null) {
					this.valueType = val.getClass();
				}
                this.value = val;
            }
            this.newValue = this.value;
            if (!Common.isEqual(previousValue, value)) {
                if (dataSet.getRecordState() == DataSet.NORMAL) {
                    dataSet.setRecordState(DataSet.FOR_UPDATE);
                }
				if (!isSilent) {
					dataSet.fireDataChange(this, this,
							DataChangeEvent.FIELD_MODIRED);
				}
            }
        }
    }

    /**
     * ȷ�����ݵ��޸ģ�ʹ oldValue��value��newValue���
     */
    public void applyUpdate() {
        if (!locked) {
            this.newValue = this.oldValue = this.previousValue = this.value;
        }
    }

    /**
     * ����ֶ�ֵ����Ϊ�ַ������ͣ������Ҫ���������ת��
     *
     * @return �ֶε��ַ���ֵ
     * @throws Exception
     */
    public String getString() throws Exception {
        return this.getString(null);
    }

    public String getString(String format) throws Exception {
        if (value == null) {
            return "";
        }
        return (String) Populator.getPopulator(String.class, value.getClass())
                .populate(this, valueField, value, format);
    }
    
    /**
     * ��ȡ
     * 
     * @return
     * @throws Exception
     */
    public BigDecimal getBigDecimal() throws Exception {
        Number num = getNumber();
        if (num == null) {
			return null;
		}
        if (num instanceof BigDecimal) {
        	return (BigDecimal) num;
        } else {
        	return new BigDecimal(num.doubleValue());
        }
    }

    /**
     * �����ֵ�ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε���������ֵ
     * @throws Exception
     */
    public Number getNumber() throws Exception {
        if (value == null) {
			return null;
		}
        return (Number) Populator.getPopulator(Number.class, value.getClass())
                .populate(this, valueField, value);
    }

    /**
     * ����ַ��ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε��ַ�����ֵ
     * @throws Exception
     */
    public byte getByte() throws Exception {
        return ((Byte) Populator.getPopulator(Byte.class, value.getClass())
                .populate(this, valueField, value)).byteValue();
    }

    /**
     * ���long�����ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε�long����ֵ
     * @throws Exception
     */
    public long getLong() throws Exception {
        return getNumber().longValue();
    }

    /**
     * ���int�ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε���������ֵ
     * @throws Exception
     */
    public int getInteger() throws Exception {
        return getNumber().intValue();
    }

    /**
     * ���float�ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε�float����ֵ
     * @throws Exception
     */
    public float getFloat() throws Exception {
        return getNumber().floatValue();
    }

    /**
     * ���double�ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε�double����ֵ
     * @throws Exception
     */
    public double getDouble() throws Exception {
        return getNumber().doubleValue();
    }

    /**
     * ���boolean�ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶε�boolean����ֵ
     * @throws Exception
     */
    public boolean getBoolean() throws Exception {
        return Common.estimate(value);
    }

    /**
     * ��û����ַ����ֶ�ֵ�������Ҫ���������ת��
     *
     * @return �ֶεĻ��ҷ�ʽ��ʾ�ַ���
     * @throws Exception
     */
    public String getCurrency() throws Exception {
        if (value == null) {
			return null;
		}
        return currencyFormatter.format(getNumber());
    }

    /**
     * ��������ֶ�ֵ(java.util.Date����)�������Ҫ���������ת��
     *
     * @return �ֶε���������ֵ
     * @throws Exception
     */
    public Date getDate() throws Exception {
        if (value == null) {
			return null;
		}
        return (Date) Populator.getPopulator(Date.class, value.getClass())
                .populate(this, valueField, value);
    }

    /**
     * ��ø�ʽ���������ַ���ֵ�������Ҫ���������ת��
     *
     * @param format
     *            ��ʽPattern
     * @return ��ʽ���˵������ַ���ֵ
     * @throws Exception
     */
    public String getDateTimeString(String format) throws Exception {
        if (value == null) {
			return null;
		}
        String fmt = format == null ? Common.COMMON_CONFIG
                .getString("java.util.Date.format") : format;
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        return df.format(getDate());
    }

    /**
     * ��ø�ʽ���������ַ���ֵ�������Ҫ���������ת��
     *
     * @return ��ʽ���˵������ַ���ֵ
     * @throws Exception
     */
    public String getDateTimeString() throws Exception {
        return getDateTimeString(null);
    }

    /**
     * �ж�һ���ֶε�ֵ�Ƿ����䶯
     *
     * @return �жϽ��
     */
    public boolean isModified() {
        return !Common.isEqual(oldValue, value);
    }

    /**
     * �Ƿ���ϴβ�����ͬ
     *
     * @return
     */
    public boolean isLatestModified() {
        return !Common.isEqual(previousValue, value);
    }

    public String toString() {
        return "[" + name + ":" + value + "]";
    }

    public Class getValueType() {
        return valueType;
    }

    public Object clone() {
        Field newField = new Field(name, valueType);
        newField.oldValue = this.oldValue;
        newField.previousValue = this.previousValue;
        newField.value = this.value;
        newField.newValue = this.newValue;
        newField.locked = this.locked;
        return newField;
    }
}
