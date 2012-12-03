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
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.janino.JavaEvaluator;
import gov.nbcs.rp.common.js.JSEvaluator;

import java.util.Iterator;
import java.util.Map;


/**
 * The Class DataSetUtil.
 */
public class DataSetUtil {

	/**
	 * Gets the data set reader.
	 * 
	 * @return the data set reader
	 */
	public static DataSetReader getDataSetReader(DataSet dataset) {
		return new DataSetReaderImpl(dataset);
	}

	/**
	 * Gets the data set full reader.
	 * 
	 * @return the data set full reader
	 */
	public static DataSetReader getDataSetFullReader(DataSet dataset) {
		return new DataSetFullReaderImpl(dataset);
	}

	/**
	 * ��ȡָ����ʼ�ͳ��ȵ��Ӽ�.
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ
	 * <p>
	 * ע�⣺ɾ��״̬�ļ�¼Ҳռ��count
	 * 
	 * @param begin
	 *            the begin
	 * @param count
	 *            the count
	 * @return the data set
	 */
	public static DataSet subDataSet(DataSet dataset, int begin, int count) {
		return subDataSet(dataset, begin, count, false);
	}

	/**
	 * ��ȡָ����ʼ�ͳ��ȵ��Ӽ�.
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ
	 * <p>
	 * ע�⣺ɾ��״̬�ļ�¼Ҳռ��count
	 * 
	 * @param begin
	 *            the begin
	 * @param count
	 *            the count
	 * @param resetState
	 *            �Ƿ����ü�¼״̬
	 * @return the data set
	 */
	public static DataSet subDataSet(DataSet dataset, int begin, int count,
			boolean resetState) {
		DataSet subSet = new DataSet(dataset.isClient());
		subSet.setDataCache(subSet.createDataCache());
		int rc = 0;
		// this.maskDataChange(true);
		int end = begin + count;
		int idx = begin;
		for (Iterator iter = dataset.getDataCache().listIterator(begin); iter
				.hasNext()
				&& (idx < end); idx++) {
			Record record = (Record) iter.next();

			Field[] fields = pickUpFields(record);
			Record newRecord = new Record(fields.length / 3 * 4);
			newRecord.dataset = subSet;
			for (int i = 0; i < fields.length; i++) {
				Field newField = (Field) fields[i].clone();
				newField.dataSet = subSet;
				newField.record = newRecord;
				newRecord.put(fields[i].getName(), newField);
			}
			subSet.getDataCache().add(newRecord);
			if (resetState) {
				// subSet.setState(newRecord, DataSet.NORMAL);
				newRecord.setState(DataSet.NORMAL);
				rc++;
			} else {
				int newState = record.getState();
				// subSet.setState(newRecord, newState);
				newRecord.setState(newState);
				// ֻ�����ɾ��״̬�ļ�¼
				if (newState != DataSet.FOR_DELETE) {
					rc++;
				}

			}

		}
		// if (subSet.dataCache != null) {
		// subSet.recordCount = new Integer(subSet.dataCache.size());
		// }
		// subSet.recordCount = new Integer(rc);
		subSet.recordCount = rc;
		// this.maskDataChange(false);
		return subSet;
	}

	/**
	 * ���ݼ�¼״ֵ̬��ȡ��״̬���и�״̬�µļ�¼��������֯Ϊһ��DataSet
	 * 
	 * @param recordState
	 * @return
	 */
	public static DataSet subDataSet(DataSet dataset, int recordState)
			throws Exception {
		return subDataSet(dataset, recordState, false);
	}

	/**
	 * ���ݼ�¼״ֵ̬��ȡ��״̬���и�״̬�µļ�¼��������֯Ϊһ��DataSet
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ
	 * 
	 * @param recordState
	 *            ��¼״̬
	 * @param changeState
	 *            �Ƿ����ü�¼״̬
	 * @return
	 */
	public static DataSet subDataSet(DataSet dataset, int recordState,
			boolean changeState) throws Exception {
		DataSet subSet = new DataSet(dataset.isClient());
		// qinj subSet.dataCache = new LinkedList();
		subSet.setDataCache(subSet.createDataCache());
		int rc = 0;
		// this.maskDataChange(true);
		for (Iterator iter = dataset.getDataCache().iterator(); iter.hasNext();) {
			Record record = (Record) iter.next();
			if (isCompatibleState(record, recordState)) {
				Field[] fields = pickUpFields(record);
				Record newRecord = new Record(fields.length / 3 * 4);
				newRecord.dataset = subSet;
				for (int i = 0; i < fields.length; i++) {
					Field newField = (Field) fields[i].clone();
					newField.dataSet = subSet;
					newField.record = newRecord;
					newRecord.put(fields[i].getName(), newField);
					if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
						fields[i].applyUpdate();
					}
				}
				subSet.getDataCache().add(newRecord);
				// ֻ�����ɾ��״̬�ļ�¼
				if (changeState) {
					// subSet.setState(newRecord, DataSet.NORMAL);
					newRecord.setState(DataSet.NORMAL);
					rc++;
				} else {
					int newState = record.getState();
					// subSet.setState(newRecord, newState);
					newRecord.setState(newState);
					if (newState != DataSet.FOR_DELETE) {
						rc++;
					}
				}
			}
		}

		// subSet.recordCount = new Integer(rc);
		subSet.recordCount = rc;
		// if (subSet.dataCache != null) {
		// subSet.recordCount = new Integer(subSet.dataCache.size());
		// }
		// qinj this.gotoBookmark(bookmark);
		// this.maskDataChange(false);
		return subSet;
	}

	/**
	 * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ.
	 * 
	 * @param filterStr
	 *            �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet filterBy(DataSet dataset, String filterStr)
			throws Exception {
		return filterBy(dataset, filterStr, false);
	}

	/**
	 * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ
	 * 
	 * @param filterStr
	 *            �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	 * @param changeState
	 *            �Ƿ�ı�״̬
	 * @return
	 */
	public static DataSet filterBy(DataSet dataset, String filterStr,
			boolean changeState) throws Exception {
		DataSet subSet = new DataSet(dataset.isClient());
		// qinj subSet.dataCache = new LinkedList();
		subSet.dataCache = subSet.createDataCache();
		int rc = 0;
		// this.maskDataChange(true);
		Map data = null;
		for (Iterator iter = dataset.dataCache.iterator(); iter.hasNext();) {
			Record record = (Record) iter.next();
			Field[] fields = pickUpFields(record);
			data = createOriginData(fields);
			// Map data = createOriginData(fields);
			// ��ָ����¼����
			if (isComfortable(data, filterStr)) {
				Record newRecord = new Record(fields.length / 3 * 4);
				newRecord.dataset = subSet;
				for (int i = 0; i < fields.length; i++) {
					Field newField = (Field) fields[i].clone();
					newField.dataSet = subSet;
					newField.record = newRecord;
					newRecord.put(fields[i].getName(), newField);
					if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
						fields[i].applyUpdate();
					}
				}
				subSet.dataCache.add(newRecord);

				// ֻ�����ɾ��״̬�ļ�¼
				if (changeState) {
					// subSet.setState(newRecord, DataSet.NORMAL);
					newRecord.setState(DataSet.NORMAL);
					rc++;
				} else {
					int newState = record.getState();
					// subSet.setState(newRecord, newState);
					newRecord.setState(newState);
					if (newState != DataSet.FOR_DELETE) {
						rc++;
					}
				}

			}
		}
		// subSet.recordCount = new Integer(rc);
		subSet.recordCount = rc;
		// if (subSet.dataCache != null) {
		// subSet.recordCount = new Integer(subSet.dataCache.size());
		// }
		// qinj this.gotoBookmark(bookmark);
		// this.maskDataChange(false);
		return subSet;
	}

	/**
	 * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ.
	 * 
	 * @param filterStr
	 *            �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet filterBy2(DataSet dataset, String filterStr)
			throws Exception {
		return filterBy2(dataset, filterStr, false);
	}

	/**
	 * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	 * <p>
	 * �����������ݿ⽻��ʱ�������Ϣ.
	 * 
	 * @param filterStr
	 *            �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	 * @param changeState
	 *            the change state �Ƿ�����״̬
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet filterBy2(DataSet dataset, String filterStr,
			boolean changeState) throws Exception {
		DataSet subSet = new DataSet(dataset.isClient());
		// qinj subSet.dataCache = new LinkedList();
		subSet.dataCache = subSet.createDataCache();
		int rc = 0;
		// this.maskDataChange(true);
		for (Iterator iter = dataset.dataCache.iterator(); iter.hasNext();) {
			Record record = (Record) iter.next();
			Field[] fields = pickUpFields(record);
			// ��ָ����¼����
			if (isComfortableByJava(record, filterStr)) {
				Record newRecord = new Record(fields.length / 3 * 4);
				newRecord.dataset = subSet;
				for (int i = 0; i < fields.length; i++) {
					Field newField = (Field) fields[i].clone();
					newField.dataSet = subSet;
					newField.record = newRecord;
					newRecord.put(fields[i].getName(), newField);
					if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
						fields[i].applyUpdate();
					}
				}
				subSet.dataCache.add(newRecord);

				// ֻ�����ɾ��״̬�ļ�¼
				if (changeState) {
					// subSet.setState(newRecord, DataSet.NORMAL);
					newRecord.setState(DataSet.NORMAL);
					rc++;
				} else {
					int newState = record.getState();
					// subSet.setState(newRecord, newState);
					newRecord.setState(newState);
					if (newState != DataSet.FOR_DELETE) {
						rc++;
					}
				}

			}
		}
		// subSet.recordCount = new Integer(rc);
		subSet.recordCount = rc;
		// if (subSet.dataCache != null) {
		// subSet.recordCount = new Integer(subSet.dataCache.size());
		// }
		// qinj this.gotoBookmark(bookmark);
		// this.maskDataChange(false);
		return subSet;
	}

	/**
	 * Puts the origin data. ���ֶ����ݷ���ָ��������Map��
	 * 
	 * @param data
	 *            the data
	 * @param fields
	 *            the fields
	 */
	private static Map createOriginData(Field[] fields) {
		Map data = new MyMap();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				data.put(fields[i].getName(), fields[i].getValue());
			}
		}
		return data;
	}

	/**
	 * Pick the up fields. ָ��record��������field
	 * 
	 * @param record
	 *            the record
	 * @return the field[]
	 */
	public static Field[] pickUpFields(Map record) {
		if ((record == null) || record.isEmpty()) {
			return null;
		}
		Field[] result = new Field[record.size() - 1];// ��¼�а����˸���״̬��Ϣ��Ҫȥ��
		// Field[] result = new Field[record.size()];// ��¼�а����˸���״̬��Ϣ��Ҫȥ��
		int i = 0;
		for (Iterator it = record.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			if (!DataSet.RECORD_STATE_ID.equals(key)) {
				result[i++] = (Field) record.get(key);
			}
		}
		return result;
	}

	/**
	 * Checks if is comfortable. �ж�ָ�������Ƿ����ָ��������
	 * 
	 * @param data
	 *            the data = field��Name-Value��
	 * @param condition
	 *            the condition ����js�﷨���߼����
	 * @return true, if is comfortable
	 */
	private static boolean isComfortable(Map data, String condition) {
		try {
			return Common.estimate(JSEvaluator.evaluate(condition, data));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * ����Jonina�����Ĺ�ʽ����
	 * 
	 * @param record
	 * @param condition������Java�﷨���������
	 * @return
	 */
	private static boolean isComfortableByJava(Record record, String condition) {
		try {
			return Common.estimate(JavaEvaluator.evaluate(condition, record,
					Boolean.TYPE));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * �ж�ָ����¼�Ƿ����ĳһ״̬����<code>getRecordState()</code>���ⲿ
	 * ���������ڲ����ܶ�λ����Delete�ļ�¼�����Դ˺���Ҳ�޷��жϼ�¼��ɾ��״̬
	 * 
	 * @param record
	 *            the record
	 * @param recordState
	 *            the record state
	 * @return true, if is compatible state
	 */
	private static boolean isCompatibleState(Record record, int recordState) {
		int state = record.getState();
		return (state & recordState) == state;
	}

	/**
	 * Sort.
	 * 
	 * @param ds
	 *            the ds
	 * @param orderByFieldNames
	 *            the order by field names
	 * @param isAsc
	 *            the is asc
	 * @return the data set
	 */
	public static DataSet sort(DataSet ds, String[] orderByFieldNames,
			boolean[] isAsc) {
		DataSet dsResult = DataSet.create();
		try {
			dsResult.edit();
			
			while (!ds.isEmpty()) {
				String bk = findBookmarkOfMinValue(ds, orderByFieldNames, isAsc);
				if (bk == null) {
					break;
				}
				ds.gotoBookmark(bk);
				Map data = ds.getOriginData();
				dsResult.append();
				dsResult.setOriginData(data);
				ds.delete();
			}

			dsResult.applyUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsResult;
	}

	private static String findBookmarkOfMinValue(DataSet ds,
			String[] orderByFieldNames, boolean[] isAsc) throws Exception {
		String bk = null;
		String[] preValues = null;
		int ln = orderByFieldNames.length;
		String[] thisValues = new String[ln];			
		ds.beforeFirst();
		while (ds.next()) {
			for (int i = 0; i < ln; i++) {
				String fn = orderByFieldNames[i];
				thisValues[i] = ds.fieldByName(fn).getString();				
			}
			if (preValues == null) {
				preValues = new String[ln];	
				bk = ds.toogleBookmark();
				System.arraycopy(thisValues, 0, preValues, 0, ln);
				continue;			
			}

			for (int i = 0; i < ln; i++) {
				int cmpResult = thisValues[i].compareTo(preValues[i]);
				if (cmpResult == 0) {
					continue;
				} else {
					if (cmpResult * (isAsc[i] ? 1 : -1) < 0) {
						bk = ds.toogleBookmark();
						System.arraycopy(thisValues, 0, preValues, 0, ln);
					}
					break;
				}
			}
		}
		return bk;
	}
}
