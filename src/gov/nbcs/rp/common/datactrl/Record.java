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

import gov.nbcs.rp.common.MyMap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Record implements Map, Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5770332125866982862L;
	
	/** The state. */
//	private int state; // = DataSet.NORMAL
	
	/**
	 * The parent dataset.
	 */
	DataSet dataset;
	
	/** The values. */
	private MyMap values; 

	public Record() {
		this.values = new MyMap();
		this.setState(DataSet.NORMAL);
	}

	public Record(int state) {
		this.values = new MyMap();
		this.setState(state);
	}

//	public Record(int state, int capacity, float factory) {
////		super(capacity, factory);
//		this.values = new MyMap(capacity, factory);
//		this.setState(state);
//	}
//
//	public Record(int state, int capacity) {
////		super(capacity);
//		this.values = new MyMap(capacity);
//		this.setState(state);
//	}
//
//	public Record(int state, Map map) {
////		super(map);
//		this.values = new MyMap(map);
//		this.setState(state);
//	}

	/**
	 * @return the state
	 */
	public int getState() {
		return ((Integer) get(DataSet.RECORD_STATE_ID)).intValue();
//		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		put(DataSet.RECORD_STATE_ID, new Integer(state));
//		this.state = state;
	}

	/**
	 * Field by name.
	 *
	 * @param fieldName
	 *            the field name
	 *
	 * @return the field
	 */
	public Field fieldByName(String fieldName) {
		Field field = (Field) get(fieldName);
		if (field == null) {
			field = new Field(fieldName, dataset != null ? dataset
					.getFieldType(fieldName, null) : null);
			field.dataSet = this.dataset;
			field.record = this;
			put(fieldName, field);
		}
		return field;
	}

	/**
	 * 获取原始的字段名－>字段值对应的Map结构.
	 *
	 * @return the map
	 */
	public Map toOriginData() {
		Map result = new MyMap();
		for (Iterator iter = keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if(!DataSet.RECORD_STATE_ID.equals(key)) {
				result.put(key, ((Field) this.get(key)).getValue());
			}
		}
		return result;
	}

	/**
	 * 通过map传入原始数据
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void putOriginData(Map map) throws Exception {
		if (map != null) {
			for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				((Field) this.get(key)).setValue(map.get(key));
			}
		}
	}
	
	/**
	 * 设定为待删除记录
	 */
	public void delete() {
		setState(DataSet.FOR_DELETE);
	}
	
	/**
	 * 
	 * @see java.util.HashMap#clear()
	 */
	public void clear() {
		values.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see gov.nbcs.rp.common.MyMap#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return values.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.HashMap#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return values.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.HashMap#entrySet()
	 */
	public Set entrySet() {
		return values.entrySet();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return values.equals(o);
	}

	/**
	 * @param key
	 * @return
	 * @see gov.nbcs.rp.common.MyMap#get(java.lang.Object)
	 */
	public Object get(Object key) {
		return values.get(key);
	}

	/**
	 * @return
	 * @see java.util.AbstractMap#hashCode()
	 */
	public int hashCode() {
		return values.hashCode();
	}

	/**
	 * @return
	 * @see java.util.HashMap#isEmpty()
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.HashMap#keySet()
	 */
	public Set keySet() {
		return values.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see gov.nbcs.rp.common.MyMap#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		return values.put(key, value);
	}

	/**
	 * @param map
	 * @see gov.nbcs.rp.common.MyMap#putAll(java.util.Map)
	 */
	public void putAll(Map map) {
		values.putAll(map);
	}

	/**
	 * @param key
	 * @return
	 * @see gov.nbcs.rp.common.MyMap#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		return values.remove(key);
	}

	/**
	 * @return
	 * @see java.util.HashMap#size()
	 */
	public int size() {
		return values.size();
	}

	/**
	 * @return
	 * @see java.util.HashMap#values()
	 */
	public Collection values() {
		return values.values();
	}

	public Object clone() {
		Record r = new Record();
		r.dataset = this.dataset;
		r.setState(getState());
		r.values = (MyMap) values.clone();
		return r;
	}
}
