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
	 * 获取指定起始和长度的子集.
	 * <p>
	 * 丢弃了与数据库交互时的相关信息
	 * <p>
	 * 注意：删除状态的记录也占用count
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
	 * 获取指定起始和长度的子集.
	 * <p>
	 * 丢弃了与数据库交互时的相关信息
	 * <p>
	 * 注意：删除状态的记录也占用count
	 * 
	 * @param begin
	 *            the begin
	 * @param count
	 *            the count
	 * @param resetState
	 *            是否重置记录状态
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
				// 只计算非删除状态的记录
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
	 * 根据记录状态值获取该状态所有该状态下的记录，并且组织为一个DataSet
	 * 
	 * @param recordState
	 * @return
	 */
	public static DataSet subDataSet(DataSet dataset, int recordState)
			throws Exception {
		return subDataSet(dataset, recordState, false);
	}

	/**
	 * 根据记录状态值获取该状态所有该状态下的记录，并且组织为一个DataSet
	 * <p>
	 * 丢弃了与数据库交互时的相关信息
	 * 
	 * @param recordState
	 *            记录状态
	 * @param changeState
	 *            是否重置记录状态
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
					if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
						fields[i].applyUpdate();
					}
				}
				subSet.getDataCache().add(newRecord);
				// 只计算非删除状态的记录
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
	 * 根据过滤条件产生一个新的DataSet子记录集
	 * <p>
	 * 丢弃了与数据库交互时的相关信息.
	 * 
	 * @param filterStr
	 *            过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet filterBy(DataSet dataset, String filterStr)
			throws Exception {
		return filterBy(dataset, filterStr, false);
	}

	/**
	 * 根据过滤条件产生一个新的DataSet子记录集
	 * <p>
	 * 丢弃了与数据库交互时的相关信息
	 * 
	 * @param filterStr
	 *            过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	 * @param changeState
	 *            是否改变状态
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
			// 对指定记录操作
			if (isComfortable(data, filterStr)) {
				Record newRecord = new Record(fields.length / 3 * 4);
				newRecord.dataset = subSet;
				for (int i = 0; i < fields.length; i++) {
					Field newField = (Field) fields[i].clone();
					newField.dataSet = subSet;
					newField.record = newRecord;
					newRecord.put(fields[i].getName(), newField);
					if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
						fields[i].applyUpdate();
					}
				}
				subSet.dataCache.add(newRecord);

				// 只计算非删除状态的记录
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
	 * 根据过滤条件产生一个新的DataSet子记录集
	 * <p>
	 * 丢弃了与数据库交互时的相关信息.
	 * 
	 * @param filterStr
	 *            过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	 * @return the data set
	 * @throws Exception
	 *             the exception
	 */
	public static DataSet filterBy2(DataSet dataset, String filterStr)
			throws Exception {
		return filterBy2(dataset, filterStr, false);
	}

	/**
	 * 根据过滤条件产生一个新的DataSet子记录集
	 * <p>
	 * 丢弃了与数据库交互时的相关信息.
	 * 
	 * @param filterStr
	 *            过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	 * @param changeState
	 *            the change state 是否重置状态
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
			// 对指定记录操作
			if (isComfortableByJava(record, filterStr)) {
				Record newRecord = new Record(fields.length / 3 * 4);
				newRecord.dataset = subSet;
				for (int i = 0; i < fields.length; i++) {
					Field newField = (Field) fields[i].clone();
					newField.dataSet = subSet;
					newField.record = newRecord;
					newRecord.put(fields[i].getName(), newField);
					if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
						fields[i].applyUpdate();
					}
				}
				subSet.dataCache.add(newRecord);

				// 只计算非删除状态的记录
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
	 * Puts the origin data. 将字段数据放入指定的数据Map内
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
	 * Pick the up fields. 指定record所包含的field
	 * 
	 * @param record
	 *            the record
	 * @return the field[]
	 */
	public static Field[] pickUpFields(Map record) {
		if ((record == null) || record.isEmpty()) {
			return null;
		}
		Field[] result = new Field[record.size() - 1];// 记录中包含了附加状态信息，要去除
		// Field[] result = new Field[record.size()];// 记录中包含了附加状态信息，要去除
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
	 * Checks if is comfortable. 判断指定数据是否符合指定的条件
	 * 
	 * @param data
	 *            the data = field的Name-Value对
	 * @param condition
	 *            the condition 符合js语法的逻辑语句
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
	 * 采用Jonina技术的公式计算
	 * 
	 * @param record
	 * @param condition，符合Java语法的条件语句
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
	 * 判断指定记录是否符合某一状态，和<code>getRecordState()</code>，外部
	 * 调用者由于不可能定位到待Delete的记录，所以此函数也无法判断记录的删除状态
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
