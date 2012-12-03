/**
 * @title - 预算编审子系统 - Copyright 浙江易桥 版权所有
 * @author qj
 * @version 6.2.80
 */
package gov.nbcs.rp.common.datactrl;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.UUID;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;
import gov.nbcs.rp.common.janino.JavaEvaluator;
import gov.nbcs.rp.common.js.JSEvaluator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataSet implements StatefulData, Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4893828636706025252L;

	public static final int FOR_DELETE = 0x0001;

	public static final int FOR_INSERT = 0x0002;

	public static final int FOR_APPEND = 0x0004;

	public static final int FOR_UPDATE = 0x0008;

	public static final int NORMAL = 0x0010;

	/** The Constant RECORD_STATE_ID. Record中state值的key */
	public static final String RECORD_STATE_ID = "DATA_SET_CUSTOM_RECORD_STATE_ID";

	/**
	 * EDIT事件类型，当DataSet进入可编辑状态时触发这一事件，分为before、after（下列意义相同）
	 */
	public static final String EDIT_EVENT = "EDIT_EVENT";

	public static final String INSERT_EVENT = "INSERT_EVENT";

	public static final String DELETE_EVENT = "DELETE_EVENT";

	public static final String CANCEL_EVENT = "CANCEL_EVENT";

	public static final String POST_EVENT = "POST_EVENT";

	public static final String OPEN_EVENT = "OPEN_EVENT";

	private static Pattern queryPatten = Pattern.compile(
			"select.+from(\\s+\\S+\\s+).+", Pattern.CASE_INSENSITIVE);

	/**
	 * The value is true if it is client.
	 */
	protected boolean isClient;
	 
	/** The value is true if it is using column cache. */
	private boolean isCachingColumnInfo = true;

	/**
	 * DataSet的主要用途是否主要用于插入和删除
	 */
	private boolean mostlyForInsertOrRemove = false;

	/**
	 * 创建一个在服务端执行SQL服务的DataSet对象 默认其主要用于读取或更新已有数据
	 * 
	 * @return
	 */
	public static DataSet create() {
		return create(false);
	}

	/**
	 * 创建一个在服务端执行SQL服务的DataSet对象.
	 * 
	 * @param mostlyForInsertOrRemove
	 *            是否主要用于插入或删除数据，反之主要用途是读取或更新已有数据。 只在大数据量的情况下需要考虑这个参数。默认为false。
	 * @return the data set
	 */
	public static DataSet create(boolean mostlyForInsertOrRemove) {
		DataSet ds = new DataSet(false);
		ds.mostlyForInsertOrRemove = mostlyForInsertOrRemove;
		return ds;
	}

	/**
	 * 创建一个在客户端执行SQL服务的DataSet对象 默认其主要用于读取或更新已有数据.
	 * 
	 * @return the data set
	 */
	public static DataSet createClient() {
		return createClient(false);
	}

	/**
	 * 创建一个在客户端执行SQL服务的DataSet对象.
	 * 
	 * @param mostlyForInsertOrRemove
	 *            the mostly for insert or remove
	 * @return the data set
	 */
	public static DataSet createClient(boolean mostlyForInsertOrRemove) {
		DataSet ds = new DataSet(true);
		ds.mostlyForInsertOrRemove = mostlyForInsertOrRemove;
		return ds;
	}

	/** The primarykey. */
	private String[] primarykey = null;

	/**
	 * 字段名集合
	 */
	private Set colNames;

	/**
	 * 字段数据类型映射
	 */
	private Map colTypes;

	/**
	 * 记录数（不含FOR_DELETE的记录）
	 */
	// protected Integer recordCount = new Integer(0);
	protected int recordCount = 0;

	/**
	 * 缓存事件侦听处理类序列
	 */
	private transient Map listenerCache;

	/**
	 * DataSet对应的表名
	 */
	private String name;

	/**
	 * DataSet所处的状态，状态常量定义在@see StatefulData 接口中
	 */
	private int state = StatefulData.DS_BROWSE;

	/**
	 * 数据集游标
	 */
	private int cursor = -1;

	/**
	 * 数据集
	 */
	List dataCache;

	/**
	 * 书签
	 */
	private Map bookmarkCache = new MyMap();

	/**
	 * 书签索引
	 */
	private SortedMap bookmarkIdx = new TreeMap();

	/**
	 * SQL查询语句
	 */
	private String sqlClause;

	/**
	 * SQL查询参数
	 */
	private Object[] queryParams;

	/** The data change listeners. */
	private transient List dataChangeListeners;

	/** The state change listeners. */
	private transient List stateChangeListeners;

	/**
	 * 屏蔽DataChange响应
	 */
	private boolean maskDataChange = false;

	/** The mask state change. */
	private boolean maskStateChange = false;

	/** The mask operation. */
	private boolean maskOperation = false;

	/**
	 * Instantiates a new data set.
	 * 
	 * @param isClient
	 *            the value is true if it is client
	 */
	protected DataSet(boolean isClient) {
		this.isClient = isClient;
	}

	/**
	 * 添加cancel操作的侦听器
	 */
	public void addCancelListener(DataSetProcListener listener) {
		bindListener(CANCEL_EVENT, listener);
	}

	/**
	 * 添加数据变更监听
	 * 
	 * @param listener
	 */
	public void addDataChangeListener(DataChangeListener listener) {
		if (dataChangeListeners == null) {
			dataChangeListeners = new ArrayList();
		}
		if (!dataChangeListeners.contains(listener)) {
			dataChangeListeners.add(listener);
		}
	}

	/**
	 * 添加delete操作的侦听器
	 */
	public void addDeleteListener(DataSetProcListener listener) {
		bindListener(DELETE_EVENT, listener);
	}

	/**
	 * 添加edit操作的侦听器
	 */
	public void addEditListener(DataSetProcListener listener) {
		bindListener(EDIT_EVENT, listener);
	}

	/**
	 * 添加insert操作的侦听器
	 */
	public void addInsertListener(DataSetProcListener listener) {
		bindListener(INSERT_EVENT, listener);
	}

	/**
	 * 添加add操作的侦听器
	 */
	public void addOpenListener(DataSetProcListener listener) {
		bindListener(OPEN_EVENT, listener);
	}

	/**
	 * 添加post操作的侦听器
	 */
	public void addPostListener(DataSetProcListener listener) {
		bindListener(POST_EVENT, listener);
	}

	/**
	 * append和insert的实际操作类，具体的进行添加记录的工作
	 * 
	 * @param index
	 *            添加记录到下标index
	 */
	protected void addRecord(int index) throws Exception {
		fireBeforeEvent(INSERT_EVENT);
		Record newRecord = new Record(NORMAL);
		newRecord.dataset = this;
		if (index >= 0) {
			// this.setState(newRecord, FOR_INSERT);
			newRecord.setState(FOR_INSERT);
			this.dataCache.add(index, newRecord);
			this.adjustedBookmarkPointer(index, 1);
			pointTo(index);
		} else {
			// this.setState(newRecord, FOR_APPEND);
			newRecord.setState(FOR_APPEND);
			pointTo(this.dataCache.size());
			this.dataCache.add(newRecord);
		}
		// this.recordCount = new Integer(this.recordCount.intValue() + 1);
		this.recordCount++;
		int preStat = this.toState(DS_INSERT | DS_EDIT);
		if (preStat >= 0) {
			this.fireStateChange(preStat, DataChangeEvent.CURSOR_MOVED);
		}
		fireAfterEvent(INSERT_EVENT);
		this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
	}

	/**
	 * 添加状态变更的事件侦听器
	 * 
	 * @param listener
	 */
	public void addStateChangeListener(StateChangeListener listener) {
		if (stateChangeListeners == null) {
			stateChangeListeners = new ArrayList();
		}
		if (!stateChangeListeners.contains(listener)) {
			stateChangeListeners.add(listener);
		}
	}

	/**
	 * 当发生插入/删除数据操作时，所有操作点之后的书签索引坐标都要调整.
	 * 
	 * @param insertPos
	 *            the insert pos
	 * @param delta
	 *            the delta 调整值 >0：后移值；<0：前移值
	 */
	protected void adjustedBookmarkPointer(int insertPos, int delta) {
		Integer insertPointer = delta > 0 ? new Integer(insertPos)
				: new Integer(insertPos + 1);// 分删除和插入两种情况
		Map afterPointers = new TreeMap(this.bookmarkIdx.tailMap(insertPointer));
		if ((afterPointers != null) && !afterPointers.isEmpty()) {
			Object pointerArray[] = afterPointers.keySet().toArray();
			int loopInit = 0, loopDelta = 1;
			if (delta > 0) {
				loopInit = pointerArray.length - 1;
				loopDelta = -1;
			}
			for (int i = loopInit; ((delta > 0) && (i >= 0))
					|| ((delta <= 0) && (i < pointerArray.length)); i += loopDelta) {
				Integer pointer = (Integer) pointerArray[i];
				String bookmark = (String) this.bookmarkIdx.get(pointer);
				bookmarkIdx.remove(pointer);
				pointer = new Integer(pointer.intValue() + delta);
				this.bookmarkCache.put(bookmark, pointer);
				this.bookmarkIdx.put(pointer, bookmark);
			}
		}
	}

	/**
	 * 将游标重置到末尾状态
	 */
	public void afterLast() throws Exception {
		int index = this.isEmpty() ? 0 : this.dataCache.size();
		if (pointTo(index)) {
			this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
		}
	}

	/**
	 * 添加一条记录，append操作后，DataSet会在数据集尾添加一条空值记录 并且将当前游标指向这条记录
	 */
	public void append() throws Exception {
		if (this.dataCache == null) {
			this.dataCache = createDataCache();
		}
		addRecord(-1);
	}

	/**
	 * 在客户端确认所有修改,如果数据需要提交给数据库服务器 这个调用应该发生在服务端调用POST之后，起到和服务端 DataSet同步的效果
	 */
	public void applyUpdate() throws Exception {
		if ((getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
			maskDataChange(true);
			String bookmark = toogleBookmark();
			this.beforeFirst();
			for (++cursor; !eof(); cursor++) {
				Record record = this.pickUpRecord();
				Field[] fields = DataSetUtil.pickUpFields(record);
				if (fields != null) {
					// switch (this.getState(record)) {
					switch (record.getState()) {
					case FOR_DELETE:
						this.dataCache.remove(cursor);
						this.adjustedBookmarkPointer(cursor, -1);
						cursor--;
						break;
					default:
						for (int i = 0; i < fields.length; i++) {
							fields[i].applyUpdate();
						}
						break;
					}
				}
				// setState(record, NORMAL);
				record.setState(NORMAL);
			}
			// this.recordCount = new Integer(dataCache.size());
			this.recordCount = dataCache.size();
			if (!this.gotoBookmark(bookmark)) {
				this.beforeFirst();// 恢复操作前游标值
			}
			int preStat = this.toState(DS_BROWSE);
			if (preStat >= 0) {
				this.fireStateChange(preStat, DataChangeEvent.CHANGE_APPLIED);
			}
			maskDataChange(false);
		}
	}

	/**
	 * 将游标重置到起始状态
	 */
	public void beforeFirst() throws Exception {
		if (pointTo(-1)) {
			this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
		}
	}

	/**
	 * 增加一个关联事件侦听器
	 * 
	 * @param eventName
	 *            事件名字
	 * @param listener
	 *            侦听器
	 */
	public void bindListener(String eventName, DataSetProcListener listener) {
		if (listenerCache == null) {
			listenerCache = new HashMap();
		}
		List listeners = (List) listenerCache.get(eventName);
		if (listeners == null) {
			listeners = new ArrayList();
			listenerCache.put(eventName, listeners);
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * 判断是否在记录集开头，指游标为-1
	 */
	public boolean bof() {
		return this.cursor < 0;
	}

	/**
	 * 撤销之前尚未保存的所有操作，包括删除、插入、修改的记录
	 */
	public void cancel() throws Exception {
		if ((getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
			fireBeforeEvent(CANCEL_EVENT);
			maskDataChange(true);
			String bookmark = this.toogleBookmark();
			this.beforeFirst();
			for (++cursor; !eof(); cursor++) {// 遍历所有字段，并且将每个字段undo
				Record record = this.pickUpRecord();
				// switch (this.getState(record)) {
				switch (record.getState()) {
				case FOR_APPEND:
				case FOR_INSERT:
					this.dataCache.remove(cursor);
					this.removeBookmark();
					this.adjustedBookmarkPointer(cursor, -1);
					cursor--;
					break;
				case FOR_UPDATE:
					Field[] fields = this.fields();
					if (fields != null) {
						for (int i = 0; i < fields.length; i++) {
							if (fields[i].isModified()) {
								fields[i].undo();
							}
						}
					}
				default:
					break;
				}
				// this.setState(record, NORMAL);
				record.setState(NORMAL);
			}
			// this.recordCount = new Integer(dataCache.size());
			this.recordCount = dataCache.size();
			if (!this.gotoBookmark(bookmark)) {
				this.beforeFirst();
			}
			int preStat = this.toState(DS_BROWSE);
			if (preStat >= 0) {
				this.fireStateChange(preStat, DataChangeEvent.CHANGE_CANCELED);
			}
			maskDataChange(false);
			fireAfterEvent(CANCEL_EVENT);
			this.fireDataChange(this, this, DataChangeEvent.CHANGE_CANCELED);
		}
	}

	/**
	 * 删除所有DataSet的数据，相应的删除表中对应记录
	 * 
	 * @throws Exception
	 */
	public void clearAll() throws Exception {
		if (!this.isEmpty()) {
			for (int i = 0; i < this.dataCache.size(); i++) {
				this.pointTo(i);
				// if (getState(this.pickUpRecord()) != FOR_DELETE) {
				if (this.pickUpRecord().getState() != FOR_DELETE) {
					this.delete();
				}
			}
		}
	}

	/**
	 * 丢弃了与数据库交互时的相关信息
	 */
	public Object clone() {
		return DataSetUtil.subDataSet(this, 0, this.dataCache.size());
	}

	/**
	 * 判断当前记录是否包含有指定名字的字段
	 * 
	 * @param name
	 *            字段名字
	 * @return
	 */
	public boolean containsField(String name) {
		return this.pickUpRecord().containsKey(name);
	}

	/**
	 * 判断指定的记录是否包含有指定名字的字段
	 * 
	 * @param name
	 *            the name
	 * @param record
	 *            the record
	 * @return true, if successful
	 */
	public boolean containsField(String name, Map record) {
		return (record != null) && record.containsKey(name);
	}

	/**
	 * Checks if it contains the specified listener.
	 * 
	 * @param eventType
	 *            the event type
	 * @return true, if it contains the specified listener
	 */
	public boolean containsListener(String eventType,
			DataSetProcListener listener) {
		if (listenerCache != null) {
			List listeners = (List) listenerCache.get(eventType);
			return (listeners != null) && listeners.contains(listener);

		}
		return false;
	}

	/**
	 * 从查询那结果List<Map<String,Object>>创建数据集List<Map<String,Field>>
	 * 
	 * @param list
	 *            查询结果
	 * @return 创建结果
	 */
	void createCacheFrom(List list) {
		if ((list == null) || (list.size() == 0)) {
			this.dataCache = createDataCache();
			// this.recordCount = new Integer(0);
			this.recordCount = 0;
			return;
		}
		// qinj List result = new LinkedList();
		this.dataCache = createDataCache(list.size());
		try {
			for (Iterator it = list.iterator(); it.hasNext();) {
				Map data = (Map) it.next();
				// qj Map record = new MyMap();
				// this.setState(record, NORMAL);
				Record record = new Record(NORMAL);
				record.dataset = this;
				for (Iterator inner_it = data.keySet().iterator(); inner_it
						.hasNext();) {
					String fieldName = Common.nonNullStr(inner_it.next());
					Object value = data.get(fieldName);

					Field f = new Field(fieldName, value, getFieldType(
							fieldName, value));
					f.dataSet = this;
					f.record = record;
					record.put(fieldName, f);
				}
				this.dataCache.add(record);
			}
			// 计算记录数 by qinj at Jan 23, 2009
			// this.recordCount = new Integer(dataCache.size());
			this.recordCount = dataCache.size();
		} catch (OutOfMemoryError e) {
			System.out
					.println("OutOfMemoryError thrown by gov.nbcs.rp.common.datactrl.DataSet#createCacheFrom(List list); Current size of the result is "
							+ this.dataCache.size());
		}
	}

	/**
	 * Creates the data cache. 根据上下文关系判断使用何种类型的List。
	 * <p>
	 * 默认为ArrayList，一般情况下，其get(i)取值效率高于LinkedList；
	 * LinkedList的数据插入和删除效率高于ArrayList。
	 * 
	 * @return the list 用于DataSet中存储数据的List对象
	 */
	protected List createDataCache() {
		// 根据上下文关系判断采用何种类型的List
		if (mostlyForInsertOrRemove) {
			return new LinkedList();
		} else {
			return new ArrayList();
		}
	}

	protected List createDataCache(int initialCapacity) {
		// 根据上下文关系判断采用何种类型的List
		if (mostlyForInsertOrRemove) {
			return new LinkedList();
		} else {
			return new ArrayList(initialCapacity);
		}
	}

	// /**
	// * Creates the origin data.
	// *
	// * @param fields
	// * the fields
	// * @return the map
	// */
	// protected Map createOriginData(Field[] fields) {
	// Map data = new MyMap();
	// if (fields != null) {
	// for (int i = 0; i < fields.length; i++) {
	// data.put(fields[i].getName(), fields[i].getValue());
	// }
	// }
	// return data;
	// }

	/**
	 * 删除当前记录
	 */
	public void delete() throws Exception {
		if (!bof() && !eof()) {
			fireBeforeEvent(DELETE_EVENT);
			Record record = pickUpRecord();
			// if (this.isModified()) {
			// Field[] fields = this.fields();
			// for (int i = 0; i < fields.length; i++) {
			// fields[i].undo();
			// }
			// }

			// this.setState(record, FOR_DELETE);
			record.setState(FOR_DELETE);
			this.removeBookmark();
			int newPosition = -1;
			if (cursor + 1 >= dataCache.size()) {
				newPosition = dataCache.size() - 1;// 删除后将游标回移
			} else {
				newPosition = cursor + 1;
			}
			boolean posChanged = this.pointTo(newPosition);
			// this.recordCount = new Integer(this.recordCount.intValue() - 1);
			this.recordCount--;
			if (posChanged) {
				this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
			}
			int stat = this.toState(DS_EDIT);
			if (stat >= 0) {
				this.fireStateChange(stat, DataChangeEvent.CURSOR_MOVED);
			}
			fireAfterEvent(DELETE_EVENT);
		}
	}

	/**
	 * 设置当前记录可编辑
	 */
	public void edit() throws Exception {
		fireBeforeEvent(EDIT_EVENT);
		int preStat = this.toState(DS_EDIT);
		if (preStat >= 0) {
			this.fireStateChange(preStat, DataChangeEvent.No_CHANGE);
		}
		fireAfterEvent(EDIT_EVENT);
	}

	/**
	 * 判断是否到记录集尾，这里记录集尾是指游标已经指向最后一条记录的下一个空挡位
	 */
	public boolean eof() {
		return this.cursor >= dataCache.size();
	}
	
	/**
	 * 判断是否在记录集中部，游标指向存在的某一条记录
	 */
	public boolean mof() {
		return (this.cursor >= 0) && (this.cursor < dataCache.size());
	}

	/**
	 * 由字段名获取字段对象
	 */
	public Field fieldByName(String fieldName) throws Exception {
		Record record = pickUpRecord();
		Field field = (Field) record.get(fieldName);
		if (field == null) {
			field = new Field(fieldName, getFieldType(fieldName, null));
			field.dataSet = this;
			field.record = record;
			record.put(fieldName, field);
		}
		return field;
	}

	/**
	 * 返回一条当前记录的字段对象集，这个集合是一个Collection<Field>对象 Field对象定义在@see Field
	 */
	public Field[] fields() {
		return DataSetUtil.pickUpFields(pickUpRecord());
	}

	/**
	 * 触发监听器
	 * 
	 * @param eventName
	 *            事件名字
	 */
	public void fireAfterEvent(String eventName) throws Exception {
		if ((listenerCache != null) && !maskOperation) {
			DataSetEvent e = new DataSetEvent(this);
			List listeners = (List) listenerCache.get(eventName);
			if (listeners != null) {
				for (int i = listeners.size() - 1; i >= 0; i--) {
					DataSetProcListener listener = (DataSetProcListener) listeners
							.get(i);
					listener.afterProc(e);
				}
			}
		}
	}

	/**
	 * 触发监听器
	 * 
	 * @param eventName
	 *            事件名字
	 */
	public void fireBeforeEvent(String eventName) throws Exception {
		if ((listenerCache != null) && !maskOperation) {
			DataSetEvent e = new DataSetEvent(this);
			List listeners = (List) listenerCache.get(eventName);
			if (listeners != null) {
				for (int i = listeners.size() - 1; i >= 0; i--) {
					DataSetProcListener listener = (DataSetProcListener) listeners
							.get(i);
					listener.beforeProc(e);
				}
			}
		}
	}

	/**
	 * 触发数据更新事件
	 */
	public void fireDataChange(Object sender, Object source, int eventType)
			throws Exception {
		DataChangeEvent event = new DataChangeEvent(this, source, eventType);
		event.setSender(sender);
		if (!maskDataChange && (dataChangeListeners != null)) {
			for (int i = this.dataChangeListeners.size() - 1; i >= 0; i--) {
				((DataChangeListener) dataChangeListeners.get(i))
						.onDataChange(event);
			}
		}
	}

	/**
	 * 触发DataSet状态改变
	 * 
	 * @param previousStat
	 *            改变之前的状态
	 * @param eventType
	 *            造成状态改变的事件类型
	 */
	public void fireStateChange(int previousStat, int eventType)
			throws Exception {
		DataChangeEvent event = new DataChangeEvent(this, this, eventType);
		if ((stateChangeListeners != null) && !maskStateChange) {
			for (int i = this.stateChangeListeners.size() - 1; i >= 0; i--) {
				((StateChangeListener) stateChangeListeners.get(i))
						.onStateChange(event);
			}
		}
	}

	/**
	 * Generate col info.
	 * 
	 * @param query
	 *            the query
	 * @throws Exception
	 *             the exception
	 */
	protected void generateColInfo(Query query) throws Exception {
		try {
			if (isCachingColumnInfo) {
				this.colNames = query.getColumnInfo(name);
				this.colTypes = query.getColumnTypeMap(name);
			} else {
				this.colNames = query.getColumnInfo2(name);
				this.colTypes = query.getColumnTypeMap2(name);
			}
		} catch (Exception ex) {
			System.out
					.println("Failed to gain the table's column infomation:TABEL["
							+ name + "]\nSQL:[" + this.sqlClause + "]\n");
		}
	}

	/**
	 * Gets the bookmark. 获取指定位置的书签
	 * 
	 * @param index
	 *            the index
	 * @return the bookmark
	 */
	public String getBookmark(int index) {
		Integer pointer = new Integer(index);
		if (this.bookmarkIdx.containsKey(pointer)) {
			return (String) bookmarkIdx.get(pointer);
		}
		return null;
	}

	/**
	 * 返回所有书签的数量
	 */
	public int getBookmarkSize() {
		return bookmarkCache.size();
	}

	/**
	 * @return the colNames
	 */
	public Set getColNames() {
		return colNames;
	}

	/**
	 * @return the colTypes
	 */
	public Map getColTypes() {
		return colTypes;
	}

	/**
	 * Gets the data cache.
	 * 
	 * @return the data cache
	 */
	public List getDataCache() {
		return dataCache;
	}

	/**
	 * Gets the field type.
	 * 指定fieldName所对应的Class类型，当colTypes为null时，返回value的Class类型
	 * 
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @return the field type
	 */
	protected Class getFieldType(String fieldName, Object value) {
		if (this.colTypes == null) {
			if (value != null) {
				return value.getClass();
			} else {
				return null;
			}
		}
		return (Class) colTypes.get(fieldName);
	}

	/**
	 * 获取DataSet对应表名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 获取当前记录的原始<字段名,字段值>对应的Map结构
	 */
	public Map getOriginData() {
		Map data = new MyMap();
		if (!this.isEmpty()) {
			Field[] fields = this.fields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					data.put(fields[i].getName(), fields[i].getValue());
				}
			}
		}
		return data;
	}

	/**
	 * Gets the primarykey.
	 * 
	 * @return the primarykey
	 */
	public String[] getPrimarykey() {
		return primarykey;
	}

	/**
	 * 获得当前记录号
	 */
	public int getRecNo() {
		return cursor;
	}

	/**
	 * 获得总的记录数（不含FOR_DELETE的记录）
	 */
	public int getRecordCount() {
		// return recordCount.intValue();
		return recordCount;
	}

	/**
	 * 获取一条记录的状态，外部 调用者由于不可能定位到待删除的记录，所以不可能获取记录的删除状态
	 */
	public int getRecordState() {
		Record record = this.pickUpRecord();
		return record.getState();
	}

	/**
	 * 获得当前DataSet的状态
	 * 
	 * @return DataSet的状态值，这些值定义在@see StatefulData
	 */
	public int getState() {
		return state;
	}

	// /**
	// * 获取一条记录的状态
	// */
	// protected int getState(Record record) {
	// // return ((Integer) record.get(RECORD_STATE_ID)).intValue();
	// return record.getState();
	// }

	/**
	 * 快速将游标定位到书签处.
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @param isSilent
	 *            the value is true if it is silent 是否激活DataChangeEvent.CURSOR_MOVED事件
	 * @return true, if goto bookmark
	 * @throws Exception
	 *             the exception
	 */
	public boolean gotoBookmark(String bookmark, boolean isSilent) throws Exception {
		synchronized (this) {
			if (bookmark == null) {
				return false;
			}
			Integer pointer = (Integer) this.bookmarkCache.get(bookmark);
			if (pointer == null) {
				return false;
			}
			if (pointTo(pointer.intValue())) {
				if (!isSilent) {
					this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
				}
			}
			return true;
		}
	}
	
	/**
	 * Goto bookmark.
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean gotoBookmark(String bookmark) throws Exception {
		return gotoBookmark(bookmark, false);
	}
	
	/**
	 * Goto bookmark silently. 不激活DataChangeEvent.CURSOR_MOVED事件
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean gotoBookmarkSilently(String bookmark) throws Exception {
		return gotoBookmark(bookmark, true);
	}
	
	/**
	 * 和append的操作类似，但是插入记录的位置是当前记录的前一个
	 */
	public void insert() throws Exception {
		if (this.dataCache == null) {
			this.dataCache = createDataCache();
		}
		if (!eof() && !bof()) {
			addRecord(this.cursor);
		}
	}
	
	/**
	 * 和append的操作类似，但是插入记录的位置是当前记录的后一个
	 */
	public void insertBehind() throws Exception {
		if (this.dataCache == null) {
			this.dataCache = createDataCache();
		}
		if (!eof() && !bof()) {
			addRecord(this.cursor + 1);
		}
	}

	/**
	 * Checks if is client.
	 * 
	 * @return true, if is client
	 */
	public boolean isClient() {
		return this.isClient;
	}

	/**
	 * 判断当前记录是否符合指定的条件
	 * 
	 * @param condition
	 *            指定的条件，Javascript语法的逻辑判断语句
	 * @return
	 */
	protected boolean isComfortable(String condition) {
		try {
			return Common.estimate(JSEvaluator.evaluate(condition,
					getOriginData()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断当前记录是否符合指定的条件
	 * 
	 * @param condition
	 *            指定的条件，Java语法的逻辑判断语句
	 * @return
	 */
	protected boolean isComfortableByJava(String condition) {
		try {
			return Common.estimate(JavaEvaluator.evaluate(condition,
					pickUpRecord(), Boolean.TYPE));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断当前记录是否符合某一状态，和<code>getRecordState()</code>，外部
	 * 调用者由于不可能定位到待Delete的记录，所以此函数也无法判断记录的删除状态
	 * 
	 * @param recordState
	 * @return
	 */
	protected boolean isCompatibleState(int recordState) {
		int state = pickUpRecord().getState();
		return (state & recordState) == state;
	}

	// /**
	// * 判断指定记录是否符合某一状态，和<code>getRecordState()</code>，外部
	// * 调用者由于不可能定位到待Delete的记录，所以此函数也无法判断记录的删除状态
	// *
	// * @param record
	// * the record
	// * @param recordState
	// * the record state
	// * @return true, if is compatible state
	// */
	// protected boolean isCompatibleState(Record record, int recordState) {
	// int state = record.getState();
	// return (state & recordState) == state;
	// }

	/**
	 * 判断数据集是否为空
	 */
	public boolean isEmpty() {
		// return this.dataCache == null || this.dataCache.size() <= 0
		// || recordCount.intValue() <= 0;
		return (this.dataCache == null) || (this.dataCache.size() <= 0)
				|| (recordCount <= 0);
	}

	/**
	 * 判断当前记录的是否已经被修改
	 */
	public boolean isModified() {
		// Field[] fields = this.fields();
		// if (fields != null) {
		// for (int i = 0; i < fields.length; i++) {
		// if (fields[i].isModified()) {
		// return true;
		// }
		// }
		// }
		return getRecordState() != NORMAL;
	}
	
	/**
	 * 根据字段条件定位游标
	 * 
	 * @param fieldName
	 *            字段名字
	 * @param value
	 *            字段值
	 */
	public boolean locate(String fieldName, Object value) throws Exception {
		return locate(fieldName, value, false);
	}
	
	/**
	 * Locate.
	 * 
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @param isSilent
	 *            the value is true if it is silent
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean locate(String fieldName, Object value, boolean isSilent) throws Exception {
		// qinj for (int i = 0; i < dataCache.size(); i++) {
		// Map record = (Map) dataCache.get(i);
		int i = 0;
		for (Iterator iter = dataCache.iterator(); iter.hasNext(); i++) {
			Record record = (Record) iter.next();
			if (record.getState() == FOR_DELETE) {
				continue;
			}
			Field field = (Field) record.get(fieldName);
			// if (record.get(fieldName) != null
			// && ((Field) record.get(fieldName)).getValue() != null
			// && value.toString().equals(
			// ((Field) record.get(fieldName)).getValue()
			// .toString())) {
			// chcx modify 2011-4-25
			// qinj 增加 value的null判断 2011-6-11
			// qzc 取消比较时的大小写限制
			if ((field != null) && (value != null)
					&& value.toString().equalsIgnoreCase(field.getString())) {
				if (this.pointTo(i)) {
					if (!isSilent) {
						this.fireDataChange(this, this,
								DataChangeEvent.CURSOR_MOVED);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据字段条件定位游标
	 * 
	 * @param fieldName
	 *            字段名字
	 * @param value
	 *            字段值 add by qzc
	 */
	public boolean locate(String fieldName1, Object value1, String fieldName2,
			Object value2) throws Exception {
		return locate(fieldName1, value1, fieldName2, value2, false);
	}
	
	/**
	 * Locate.
	 * 
	 * @param fieldName1
	 *            the field name1
	 * @param value1
	 *            the value1
	 * @param fieldName2
	 *            the field name2
	 * @param value2
	 *            the value2
	 * @param isSilent
	 *            the value is true if it is silent
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean locate(String fieldName1, Object value1, String fieldName2,
			Object value2, boolean isSilent) throws Exception {
		// qinj for (int i = 0; i < dataCache.size(); i++) {
		// Map record = (Map) dataCache.get(i);
		int i = 0;
		for (Iterator iter = dataCache.iterator(); iter.hasNext(); i++) {
			Record record = (Record) iter.next();
			if (record.getState() == FOR_DELETE) {
				continue;
			}
			// if (record.get(fieldName1) != null
			// && record.get(fieldName2) != null
			// && ((Field) record.get(fieldName1)).getValue() != null
			// && ((Field) record.get(fieldName2)).getValue() != null
			// && value1.toString().equals(
			// ((Field) record.get(fieldName1)).getValue()
			// .toString())
			// && value2.toString().equals(
			// ((Field) record.get(fieldName2)).getValue()
			// .toString())
			//
			// ) {
			// chcx modify 2011-5-26
			// qinj 增加 value1、value2的null判断 2011-6-11
			Field field1 = (Field) record.get(fieldName1);
			Field field2 = (Field) record.get(fieldName2);
			if ((field1 != null) && (field2 != null) && (value1 != null)
					&& (value2 != null)
					&& value1.toString().equals(field1.getString())
					&& value2.toString().equals(field2.getString())) {
				if (this.pointTo(i)) {
					if (!isSilent) {
						this.fireDataChange(this, this,
								DataChangeEvent.CURSOR_MOVED);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据一系列字段过滤条件定位游标
	 */
	public boolean locateByFilter(String filter) throws Exception {
		return locateByFilter(filter, false);
	}
	
	/**
	 * Locate by filter.
	 * 
	 * @param filter
	 *            the filter
	 * @param isSilent
	 *            the value is true if it is silent
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean locateByFilter(String filter, boolean isSilent) throws Exception {
		// qinj for (int i = 0; i < dataCache.size(); i++) {
		// Map record = (Map) dataCache.get(i);
		int i = 0;
		for (Iterator iter = dataCache.iterator(); iter.hasNext(); i++) {
			Record record = (Record) iter.next();
			if (record.getState() == FOR_DELETE) {
				continue;
			}
			this.pointTo(i);
			if (isComfortable(filter)) {
				if(!isSilent) {
					this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 关闭/启动DataChange事件响应
	 * 
	 * @param maskDataChange
	 */
	public void maskDataChange(boolean maskDataChange) {
		this.maskDataChange = maskDataChange;
	}

	/**
	 * 关闭/启动各类操作的Before/After事件响应
	 * 
	 * @param maskDataChange
	 */
	public void maskOperationChange(boolean maskOperation) {
		this.maskOperation = maskOperation;
	}

	/**
	 * 关闭/启动StateChange事件响应
	 * 
	 * @param maskDataChange
	 */
	public void maskStateChange(boolean maskStateChange) {
		this.maskStateChange = maskStateChange;
	}

	/**
	 * 从Query语句中提取表名，但是如果是一个复合查询此方法结果无效 不过复合查询结果的DataSet的Post效果也是基本无效的
	 * 
	 * @param sqlClause
	 *            Query语句
	 */
	protected void nameFromClause(String sqlClause) {
		Matcher mat = queryPatten.matcher(sqlClause + "  ");
		if (mat.find()) {			
			String tn = mat.group(1).trim();
			if (!tn.startsWith("(")) {
				this.name = tn;
			}
		}
	}

	/**
	 * 遍历数据集，同时返回当前是否已经eof()
	 */
	public boolean next() throws Exception {
		if (this.isEmpty() || this.eof()) {
			return false;
		}
		do {
			if (this.pointTo(cursor + 1)) {
				this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
			}
		} while (!this.eof() && (this.pickUpRecord().getState() == FOR_DELETE));
		return !this.eof();
	}

	/**
	 * 打开、激活一个DataSet，并且使DataSet数据集有内容 如果制定了SQL和SQL parameters，将会优先使用SQL语句
	 * 查询结果来填充DataSet数据集，否则将会打开制定的name 全表作为数据集
	 */
	public void open() throws Exception {
		this.open(-1, -1);
	}

	// /**
	// * 根据过滤条件产生一个新的DataSet子记录集
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息
	// *
	// * @param filterStr
	// * 过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	// * @param changeState
	// * 是否改变状态
	// * @return
	// */
	// public DataSet filterBy(String filterStr, boolean changeState)
	// throws Exception {
	// DataSet subSet = new DataSet(this.isClient());
	// // qinj subSet.dataCache = new LinkedList();
	// subSet.dataCache = createDataCache();
	// int rc = 0;
	// this.maskDataChange(true);
	// Map data = null;
	// for (Iterator iter = dataCache.iterator(); iter.hasNext();) {
	// Map record = (Map) iter.next();
	// Field[] fields = pickUpFields(record);
	// data = putOriginData(data, fields);
	// // Map data = createOriginData(fields);
	// // 对指定记录操作
	// if (isComfortable(data, filterStr)) {
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	//
	// // 只计算非删除状态的记录
	// if (changeState) {
	// subSet.setState(newRecord, NORMAL);
	// rc++;
	// } else {
	// int newState = this.getState(record);
	// subSet.setState(newRecord, newState);
	// if (newState != FOR_DELETE)
	// rc++;
	// }
	//
	// }
	// }
	// subSet.recordCount = new Integer(rc);
	// // if (subSet.dataCache != null) {
	// // subSet.recordCount = new Integer(subSet.dataCache.size());
	// // }
	// // qinj this.gotoBookmark(bookmark);
	// this.maskDataChange(false);
	// return subSet;
	// }

	// /**
	// * 根据过滤条件产生一个新的DataSet子记录集
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息.
	// *
	// * @param filterStr
	// * 过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	// *
	// * @return the data set
	// *
	// * @throws Exception
	// * the exception
	// */
	// public DataSet filterBy(String filterStr) throws Exception {
	// return filterBy(filterStr, false);
	// }

	// /**
	// * 根据过滤条件产生一个新的DataSet子记录集
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息.
	// *
	// * @param filterStr
	// * 过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	// * @param changeState
	// * the change state 是否重置状态
	// *
	// * @return the data set
	// *
	// * @throws Exception
	// * the exception
	// */
	// public DataSet filterBy2(String filterStr, boolean changeState)
	// throws Exception {
	// DataSet subSet = new DataSet(this.isClient());
	// // qinj subSet.dataCache = new LinkedList();
	// subSet.dataCache = createDataCache();
	// int rc = 0;
	// this.maskDataChange(true);
	// for (Iterator iter = dataCache.iterator(); iter.hasNext();) {
	// Record record = (Record) iter.next();
	// Field[] fields = pickUpFields(record);
	// // 对指定记录操作
	// if (isComfortable2(record, filterStr)) {
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	//
	// // 只计算非删除状态的记录
	// if (changeState) {
	// subSet.setState(newRecord, NORMAL);
	// rc++;
	// } else {
	// int newState = this.getState(record);
	// subSet.setState(newRecord, newState);
	// if (newState != FOR_DELETE)
	// rc++;
	// }
	//
	// }
	// }
	// subSet.recordCount = new Integer(rc);
	// // if (subSet.dataCache != null) {
	// // subSet.recordCount = new Integer(subSet.dataCache.size());
	// // }
	// // qinj this.gotoBookmark(bookmark);
	// this.maskDataChange(false);
	// return subSet;
	// }

	// /**
	// * 根据过滤条件产生一个新的DataSet子记录集
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息.
	// *
	// * @param filterStr
	// * 过滤条件（语法必须符合javascript脚本的语法，并且注意字段名约定大写）
	// *
	// * @return the data set
	// *
	// * @throws Exception
	// * the exception
	// */
	// public DataSet filterBy2(String filterStr) throws Exception {
	// return filterBy2(filterStr, false);
	// }

	// /**
	// * 根据记录状态值获取该状态所有该状态下的记录，并且组织为一个DataSet
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息
	// *
	// * @param recordState
	// * 记录状态
	// * @param changeState
	// * 是否重置记录状态
	// * @return
	// */
	// public DataSet subDataSet(int recordState, boolean changeState)
	// throws Exception {
	// DataSet subSet = new DataSet(this.isClient());
	// // qinj subSet.dataCache = new LinkedList();
	// subSet.dataCache = createDataCache();
	// int rc = 0;
	// this.maskDataChange(true);
	// for (Iterator iter = dataCache.iterator(); iter.hasNext();) {
	// Map record = (Map) iter.next();
	// if (isCompatibleState(record, recordState)) {
	// Field[] fields = pickUpFields(record);
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// if (changeState) {// 如果重置状态，把每个字段的更新状态也去除
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	// // 只计算非删除状态的记录
	// if (changeState) {
	// subSet.setState(newRecord, NORMAL);
	// rc++;
	// } else {
	// int newState = this.getState(record);
	// subSet.setState(newRecord, newState);
	// if (newState != FOR_DELETE)
	// rc++;
	// }
	// }
	// }
	//
	// subSet.recordCount = new Integer(rc);
	// // if (subSet.dataCache != null) {
	// // subSet.recordCount = new Integer(subSet.dataCache.size());
	// // }
	// // qinj this.gotoBookmark(bookmark);
	// this.maskDataChange(false);
	// return subSet;
	// }

	// /**
	// * 根据记录状态值获取该状态所有该状态下的记录，并且组织为一个DataSet
	// *
	// * @param recordState
	// * @return
	// */
	// public DataSet subDataSet(int recordState) throws Exception {
	// return subDataSet(recordState, false);
	// }

	/**
	 * 支持分页查询的open操作
	 * 
	 * @param pageStart
	 * @param pageSize
	 * @throws Exception
	 */
	protected void open(int pageStart, int pageSize) throws Exception {
		if (Common.isNullStr(this.sqlClause)) {
			this.sqlClause = SQLTool.querySQL(this.getName());
		}
		this.open(pageStart, pageSize, this.sqlClause, this.queryParams);
	}

	/**
	 * open的实际操作类
	 * 
	 * @param sql
	 *            查询SQL
	 * @param params
	 *            查询参数
	 * @throws Exception
	 */
	protected void open(int pageStart, int pageSize, String sql, Object[] params)
			throws Exception {
		if ((pageStart >= 0) && (pageSize > 1)) {
			sql = Common.getPageQrySQL(sql, pageStart, pageSize);
		}
		fireBeforeEvent(OPEN_EVENT);
		Query query = this.isClient ? QueryStub.getClientQueryTool()
				: QueryStub.getQueryTool();
		List datas = params == null ? query.executeQuery(sql) : query
				.executeQuery2(sql, params);
		this.generateColInfo(query);
		this.createCacheFrom(datas);
		this.beforeFirst();
		int preStat = this.toState(DS_BROWSE);
		if (preStat >= 0) {
			this.fireStateChange(preStat, DataChangeEvent.DATA_FILLED);
		}
		fireAfterEvent(OPEN_EVENT);
		this.fireDataChange(this, this, DataChangeEvent.DATA_FILLED);
	}

	/**
	 * Pick the up record. 当前cursor所指record
	 * 
	 * @return the record
	 */
	public Record pickUpRecord() {
		return (Record) dataCache.get(cursor);
	}

	/**
	 * 将游标定位到一个新位置，并且返回位置是否变化的信息(TRUE:变化)
	 * 
	 * @param newIndex
	 * @return
	 */
	protected boolean pointTo(int newIndex) {
		boolean b_ret = this.cursor != newIndex;
		this.cursor = newIndex;
		return b_ret;
	}

	/**
	 * 提交保存所有尚未保存的操作结果
	 */
	public void post() throws Exception {
		if ((getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
			fireBeforeEvent(POST_EVENT);
			List sqls = new LinkedList();
			maskDataChange(true);
			String bookmark = toogleBookmark();
			Query query = this.isClient ? QueryStub.getClientQueryTool()
					: QueryStub.getQueryToolNT();
			this.beforeFirst();
			if (this.colNames == null) {
				this.setName(this.getName());// 触发搜集列信息
			}
			for (++cursor; !eof(); cursor++) {
				Record record = this.pickUpRecord();
				Field[] fields = DataSetUtil.pickUpFields(record);
				if (fields != null) {
					switch (record.getState()) {
					case FOR_DELETE:// 根据待删除对象生成删除的SQL语句
						if (this.primarykey == null) {
							sqls.add(SQLTool.deleteSQL(getName(), fields,
									this.colNames));
							this.dataCache.remove(cursor);
							this.adjustedBookmarkPointer(cursor, -1);
							cursor--;
						} else {
							boolean falg = true;
							List primarykeyValue = new ArrayList();
							for (int i = 0; i < primarykey.length; i++) {
								if (!this.containsField(primarykey[i])) {
									falg = false;
									break;
								} else {
									Object value = this.fieldByName(
											primarykey[i]).getValue();
									if (value == null) {
										falg = false;
										break;
									} else {
										primarykeyValue.add(value);
									}
								}
							}
							if (falg) {
								sqls.add(SQLTool.deleteSQL(getName(),
										primarykeyValue, primarykey));
								this.dataCache.remove(cursor);
								this.adjustedBookmarkPointer(cursor, -1);
								cursor--;
							}
						}
						break;
					case FOR_APPEND:
					case FOR_INSERT: // 根据待插入对象生成插入数据的SQL语句
						sqls.add(SQLTool.insertSQL(getName(), fields,
								this.colNames));
						for (int i = 0; i < fields.length; i++) {
							fields[i].applyUpdate();
						}
						break;
					default:
						if (this.isModified()) {// 根据修改的记录生成Update语句
							if (this.primarykey == null) {
								sqls.add(SQLTool.updateSQL(getName(), fields,
										this.colNames));
								for (int i = 0; i < fields.length; i++) {
									fields[i].applyUpdate();
								}

							} else {
								boolean falg = true;
								List primarykeyValue = new ArrayList();
								for (int i = 0; i < primarykey.length; i++) {
									if (!this.containsField(primarykey[i])) {
										falg = false;
										break;
									} else {
										Object value = this.fieldByName(
												primarykey[i]).getValue();
										if (value == null) {
											falg = false;
											break;
										} else {
											primarykeyValue.add(value);
										}
									}
								}
								if (falg) {
									sqls.add(SQLTool.updateSQL(getName(),
											fields, this.colNames,
											primarykeyValue, primarykey));
								}
								for (int i = 0; i < fields.length; i++) {
									fields[i].applyUpdate();
								}
							}
						}
						break;
					}
				}
				// setState(record, NORMAL);
				record.setState(NORMAL);
			}
			// this.recordCount = new Integer(dataCache.size());
			this.recordCount = dataCache.size();
			if (!this.gotoBookmark(bookmark)) {
				this.beforeFirst();// 恢复操作前游标值
			}
			if (!sqls.isEmpty()) {
				query.executeBatch(sqls);
			}
			int preStat = this.toState(DS_BROWSE);
			if (preStat >= 0) {
				this.fireStateChange(preStat, DataChangeEvent.CHANGE_APPLIED);
			}
			maskDataChange(false);
			fireAfterEvent(POST_EVENT);
		}
	}

	/**
	 * 将游标回退一位，，同时返回当前是否已经bof()
	 */
	public boolean prior() throws Exception {
		if (this.isEmpty() || this.bof()) {
			return false;
		}
		do {
			if (this.pointTo(cursor - 1)) {
				this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
			}
		} while (!this.bof() && (this.pickUpRecord().getState() == FOR_DELETE));
		return !this.bof();
	}

	/**
	 * 去除书签
	 */
	public void removeBookmark() {
		Object mark = this.bookmarkIdx.remove(new Integer(cursor));
		if (mark != null) {
			this.bookmarkCache.remove(mark);
		}
	}

	/**
	 * 去除指定的书签
	 */
	public void removeBookmark(int index) {
		Object mark = this.bookmarkIdx.remove(new Integer(index));
		if (mark != null) {
			this.bookmarkCache.remove(mark);
		}
	}

	/**
	 * Sets the client.
	 * 
	 * @param isClient
	 *            the isClient to set
	 */
	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}

	/**
	 * Sets the data cache.
	 * 
	 * @param dataCache
	 *            the new data cache
	 */
	public void setDataCache(List dataCache) {
		this.dataCache = dataCache;
	}

	/**
	 * 设置DataSet对应表名
	 * 
	 * @param name
	 *            表名
	 */
	public void setName(String name) throws Exception {
		this.name = name;
		this.generateColInfo(this.isClient ? QueryStub.getClientQueryTool()
				: QueryStub.getQueryTool());
	}

	/**
	 * 字段名->字段值对应的Map结构映射到DataSet的记录字段集中
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void setOriginData(Map data) throws Exception {
		for (Iterator it = data.keySet().iterator(); it.hasNext();) {
			String key = Common.nonNullStr(it.next());
			this.fieldByName(key).setValue(data.get(key));
		}
	}
	
	/**
	 * 将指定record放入当前位置
	 * @param record
	 * @throws Exception
	 */
	public void grabRecord(Record record) throws Exception {
		Record currRecord = pickUpRecord();
		int st = currRecord.getState();
		for (Iterator itr = record.keySet().iterator(); itr.hasNext();) {
			Object key = itr.next();
			currRecord.put(key, record.get(key));
		}
		currRecord.setState(st);
	}

	/**
	 * Sets the primarykey.
	 * 
	 * @param primarykey
	 *            the new primarykey
	 */
	public void setPrimarykey(String[] primarykey) {
		this.primarykey = primarykey;
	}

	/**
	 * 设置SQL查询参数
	 */
	public void setQueryParams(Object[] queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * 设置一条记录的状态，外部 调用者由于不可能定位到待删除的记录，所以不可能获取记录的删除状态 可以将无变化的记录设置为修改、删除、插入等等
	 * 
	 * @param state
	 */
	public void setRecordState(int state) {
		Record record = this.pickUpRecord();
		// this.setState(record, state);
		record.setState(state);
	}

	/**
	 * 设置SQL查询语句
	 */
	public void setSqlClause(String sqlClause) {
		this.sqlClause = sqlClause;
		this.nameFromClause(sqlClause);
	}

	// /**
	// * 设置记录的状态
	// *
	// * @param record
	// * 记录
	// * @param state
	// * 状态值
	// */
	// protected void setState(Record record, int state) {
	// // record.put(RECORD_STATE_ID, new Integer(state));
	// record.setState(state);
	// }

	/**
	 * 产生一个指定记录的书签
	 * 
	 * @param index
	 * @return
	 */
	public String toggleBookmark(int index) {
		Integer pointer = new Integer(index);
		if (this.bookmarkIdx.containsKey(pointer)) {
			return (String) bookmarkIdx.get(pointer);
		}
		String bookmark = UUID.randomUUID().md5Code();
		this.bookmarkCache.put(bookmark, pointer);
		this.bookmarkIdx.put(pointer, bookmark);
		return bookmark;
	}

	/**
	 * 产生一个当前记录的书签便于之后进行快速定位
	 */
	public String toogleBookmark() {
		Integer pointer = new Integer(cursor);
		if (this.bookmarkIdx.containsKey(pointer)) {
			return (String) bookmarkIdx.get(pointer);
		}
		String bookmark = UUID.randomUUID().md5Code();
		this.bookmarkCache.put(bookmark, pointer);
		this.bookmarkIdx.put(pointer, bookmark);
		return bookmark;
	}

	/**
	 * 将DataSet转变到某一状态，如果前后状态不同会返回变更与否 （返回值-1表示无变化，否则就是变化之前的状态值）
	 * 
	 * @param state
	 *            转变后的状态值
	 * @return 状态是否有变化。如果没有变化，返回-1；否则返回转变前的状态值
	 */
	public int toState(int state) {
		int result = this.state == state ? -1 : this.state;
		this.state = state;
		return result;
	}

	// /**
	// * 获取指定起始和长度的子集.
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息
	// * <p>
	// * 注意：删除状态的记录也占用count
	// *
	// * @param begin
	// * the begin
	// * @param count
	// * the count
	// *
	// * @return the data set
	// */
	// public DataSet subDataSet(int begin, int count) {
	// return subDataSet(begin, count, false);
	// }

	// /**
	// * 获取指定起始和长度的子集.
	// *
	// * <p>
	// * 丢弃了与数据库交互时的相关信息
	// * <p>
	// * 注意：删除状态的记录也占用count
	// *
	// * @param begin
	// * the begin
	// * @param count
	// * the count
	// * @param resetState
	// * 是否重置记录状态
	// * @return the data set
	// */
	// public DataSet subDataSet(int begin, int count, boolean resetState) {
	// DataSet subSet = new DataSet(this.isClient());
	// subSet.dataCache = createDataCache();
	// int rc = 0;
	// this.maskDataChange(true);
	// int end = begin + count;
	// int idx = begin;
	// for (Iterator iter = dataCache.listIterator(begin); iter.hasNext()
	// && idx < end; idx++) {
	// Map record = (Map) iter.next();
	//
	// Field[] fields = pickUpFields(record);
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// }
	// subSet.dataCache.add(newRecord);
	// if (resetState) {
	// subSet.setState(newRecord, NORMAL);
	// rc++;
	// } else {
	// int newState = this.getState(record);
	// subSet.setState(newRecord, newState);
	// // 只计算非删除状态的记录
	// if (newState != FOR_DELETE)
	// rc++;
	//
	// }
	//
	// }
	// // if (subSet.dataCache != null) {
	// // subSet.recordCount = new Integer(subSet.dataCache.size());
	// // }
	// subSet.recordCount = new Integer(rc);
	// this.maskDataChange(false);
	// return subSet;
	// }

	/**
	 * 转移DataCache的存储结构.
	 * 
	 * @param destStorage
	 *            the to storage: <br>
	 *            0 for ArrayList，主要用于读取、尾部附加等操作; <br>
	 *            1 for LinkedList，主要用于插入、删除等操作
	 *            <p>
	 *            只有在大数据量的情况下，当目标存储结构有利于提高性能时才考虑使用本方法。
	 *            </p>
	 */
	public void transferDataCahceStorage(int destStorage) {
		if (dataCache != null) {
			if ((destStorage == 0) && !(dataCache instanceof ArrayList)) {
				dataCache = new ArrayList(dataCache);
			} else if ((destStorage == 1) && !(dataCache instanceof LinkedList)) {
				dataCache = new LinkedList(dataCache);
			}
		}
	}

	/**
	 * @return the isCachingColumnInfo
	 */
	public boolean isCachingColumnInfo() {
		return isCachingColumnInfo;
	}

	/**
	 * @param isCachingColumnInfo
	 *            the isCachingColumnInfo to set
	 */
	public void setCachingColumnInfo(boolean isCachingColumnInfo) {
		this.isCachingColumnInfo = isCachingColumnInfo;
	}

	/**
	 * Gets the record by bookmark.
	 * 
	 * @param bookmark the bookmark
	 * @return the record
	 */
	public Record recordByBookmark(String bookmark) {
		Integer pointer = (Integer) this.bookmarkCache.get(bookmark);
		if (pointer == null) {
			return null;
		} else {
			return (Record) dataCache.get(pointer.intValue());
		}
	}
	
	public void cleanAllListeners() {
		this.stateChangeListeners.clear();
		this.dataChangeListeners.clear();
		this.listenerCache.clear();
	}
}
