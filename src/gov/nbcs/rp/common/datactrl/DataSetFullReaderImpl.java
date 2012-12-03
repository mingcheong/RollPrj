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

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * The Class DataSetFullReaderImpl.
 * 一个DataSetReader的实现，用于读取所有状态的记录集（包含FOR_DELETE的记录）
 */
public class DataSetFullReaderImpl extends DataSetReader {

	/**
	 * Instantiates a new data set reader.
	 * 
	 * @param dataSet
	 *            the data set
	 */
	public DataSetFullReaderImpl(DataSet dataSet) {
		super(dataSet);
	}

	/**
	 * Gets the data cache.
	 * 
	 * @return the data cache
	 * 
	 * @see gov.nbcs.rp.common.datactrl.DataSet#getDataCache()
	 */
	private List getDataCache() {
		return dataSet.getDataCache();
	}

	/**
	 * Record iterator.
	 * 
	 * @return the iterator
	 */
	public Iterator iterator() {
		return getDataCache().iterator();
	}

	/**
	 * Record list iterator.
	 * 
	 * @return the list iterator
	 */
	public ListIterator listIterator() {
		return getDataCache().listIterator();
	}

	/**
	 * Record list iterator.
	 * 
	 * @param index
	 *            the index
	 * 
	 * @return the list iterator
	 */
	public ListIterator listIterator(int index) {
		return getDataCache().listIterator(index);
	}

	/**
	 * Field by name. 获取指定record的field，以ReadonlyField返回
	 * 
	 * @param record
	 *            the record
	 * @param fieldName
	 *            the field name
	 * 
	 * @return the readonly field
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public ReadonlyField fieldByName(Map record, String fieldName) {
		ReadonlyField rf = null;
		if ((record != null) && (fieldName != null)) {
			Field field = (Field) record.get(fieldName);
			if (field != null) {
				rf = new ReadonlyField(field);
			}
		}
		return rf;
	}

	/**
	 * Field by name. 获取指定index的record的field，以ReadonlyField返回
	 * 
	 * @param recordIndex
	 *            the record index
	 * @param fieldName
	 *            the field name
	 * 
	 * @return the readonly field
	 */
	public ReadonlyField fieldByName(int recordIndex, String fieldName) {
		Map record = (Map) getDataCache().get(recordIndex);
		return fieldByName(record, fieldName);
	}
}
