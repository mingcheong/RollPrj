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

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * The Class DataSetFullReaderImpl.
 * һ��DataSetReader��ʵ�֣����ڶ�ȡ����״̬�ļ�¼��������FOR_DELETE�ļ�¼��
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
	 * Field by name. ��ȡָ��record��field����ReadonlyField����
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
	 * Field by name. ��ȡָ��index��record��field����ReadonlyField����
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
