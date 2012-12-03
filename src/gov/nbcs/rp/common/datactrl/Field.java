/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
     * 货币格式化显示的工具类
     */
    private static NumberFormat currencyFormatter = NumberFormat
            .getCurrencyInstance(Locale.getDefault());

    /**
     * 字段值
     */
    private Object value;

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段原始值
     */
    Object oldValue;

    /**
     * 字段新值
     */
    Object newValue;

    /**
     * 关联的数据集对象
     */
    DataSet dataSet;
    
    /**
     * 管理的数据记录对象
     */
    Record record;

    /**
     * 本次操作之前的字段值
     */
    Object previousValue;

    /**
     *
     */
    Class valueType;

    private boolean locked = false;

    /**
     * java.lang反射的value字段对象
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
     * 用字段名构造Field
     *
     * @param name
     *            字段名
     * @param value
     *            字段值
     */
    public Field(String name, Class valueType) {
        this(name, null, valueType);
    }

    /**
     * 用字段名和字段值构造Field
     *
     * @param name
     *            字段名
     * @param value
     *            字段值
     */
    public Field(String name, Object value, Class valueType) {
        previousValue = this.newValue = this.oldValue = this.value = value;
        this.name = name;
        this.valueType = valueType;
    }

    /**
     * 恢复原始值，取消修改
     */
    void undo() throws Exception {
        setValue(this.oldValue);
    }

    /**
     * 恢复新值，继续修改
     */
    void redo() throws Exception {
        setValue(this.newValue);
    }

    /**
     * 获取字段名
     *
     * @return 字段名
     */
    public String getName() {
        return name;
    }

    /**
     * 返回字段初始值
     *
     * @return
     */
    public Object getOldValue() {
        return this.oldValue;
    }

    /**
     * 得到本次操作之前的value
     *
     * @return
     */
    public Object getPreviousValue() {
        return this.previousValue;
    }

    /**
     * 设置字段名
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取字段值
     *
     * @return 返回字段值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置字段值
     *
     * @param val
     *            字段值
     * @throws Exception
     */
    public void setValue(Object val) throws Exception {
        this.setValue(val, null, false);
    }
    
    /**
	 * Sets the value silently. 赋值，但不激发事件
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
     * 设置字段值
     *
     * @param val
     *            字段值
     *
     * @param format
     *            类型转换时需要的格式化信息
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
     * 确认数据的修改，使 oldValue、value、newValue相等
     */
    public void applyUpdate() {
        if (!locked) {
            this.newValue = this.oldValue = this.previousValue = this.value;
        }
    }

    /**
     * 获得字段值，作为字符串类型，如果需要会进行类型转换
     *
     * @return 字段的字符串值
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
     * 获取
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
     * 获得数值字段值，如果需要会进行类型转换
     *
     * @return 字段的数字类型值
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
     * 获得字符字段值，如果需要会进行类型转换
     *
     * @return 字段的字符类型值
     * @throws Exception
     */
    public byte getByte() throws Exception {
        return ((Byte) Populator.getPopulator(Byte.class, value.getClass())
                .populate(this, valueField, value)).byteValue();
    }

    /**
     * 获得long类型字段值，如果需要会进行类型转换
     *
     * @return 字段的long类型值
     * @throws Exception
     */
    public long getLong() throws Exception {
        return getNumber().longValue();
    }

    /**
     * 获得int字段值，如果需要会进行类型转换
     *
     * @return 字段的数字类型值
     * @throws Exception
     */
    public int getInteger() throws Exception {
        return getNumber().intValue();
    }

    /**
     * 获得float字段值，如果需要会进行类型转换
     *
     * @return 字段的float类型值
     * @throws Exception
     */
    public float getFloat() throws Exception {
        return getNumber().floatValue();
    }

    /**
     * 获得double字段值，如果需要会进行类型转换
     *
     * @return 字段的double类型值
     * @throws Exception
     */
    public double getDouble() throws Exception {
        return getNumber().doubleValue();
    }

    /**
     * 获得boolean字段值，如果需要会进行类型转换
     *
     * @return 字段的boolean类型值
     * @throws Exception
     */
    public boolean getBoolean() throws Exception {
        return Common.estimate(value);
    }

    /**
     * 获得货币字符串字段值，如果需要会进行类型转换
     *
     * @return 字段的货币方式显示字符串
     * @throws Exception
     */
    public String getCurrency() throws Exception {
        if (value == null) {
			return null;
		}
        return currencyFormatter.format(getNumber());
    }

    /**
     * 获得日期字段值(java.util.Date对象)，如果需要会进行类型转换
     *
     * @return 字段的日期类型值
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
     * 获得格式化的日期字符串值，如果需要会进行类型转换
     *
     * @param format
     *            格式Pattern
     * @return 格式化了的日期字符串值
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
     * 获得格式化的日期字符串值，如果需要会进行类型转换
     *
     * @return 格式化了的日期字符串值
     * @throws Exception
     */
    public String getDateTimeString() throws Exception {
        return getDateTimeString(null);
    }

    /**
     * 判断一个字段的值是否发生变动
     *
     * @return 判断结果
     */
    public boolean isModified() {
        return !Common.isEqual(oldValue, value);
    }

    /**
     * 是否和上次操作不同
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
