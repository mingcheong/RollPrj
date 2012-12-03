/**
 * @title - Ԥ�������ϵͳ - Copyright �㽭���� ��Ȩ����
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

	/** The Constant RECORD_STATE_ID. Record��stateֵ��key */
	public static final String RECORD_STATE_ID = "DATA_SET_CUSTOM_RECORD_STATE_ID";

	/**
	 * EDIT�¼����ͣ���DataSet����ɱ༭״̬ʱ������һ�¼�����Ϊbefore��after������������ͬ��
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
	 * DataSet����Ҫ��;�Ƿ���Ҫ���ڲ����ɾ��
	 */
	private boolean mostlyForInsertOrRemove = false;

	/**
	 * ����һ���ڷ����ִ��SQL�����DataSet���� Ĭ������Ҫ���ڶ�ȡ�������������
	 * 
	 * @return
	 */
	public static DataSet create() {
		return create(false);
	}

	/**
	 * ����һ���ڷ����ִ��SQL�����DataSet����.
	 * 
	 * @param mostlyForInsertOrRemove
	 *            �Ƿ���Ҫ���ڲ����ɾ�����ݣ���֮��Ҫ��;�Ƕ�ȡ������������ݡ� ֻ�ڴ����������������Ҫ�������������Ĭ��Ϊfalse��
	 * @return the data set
	 */
	public static DataSet create(boolean mostlyForInsertOrRemove) {
		DataSet ds = new DataSet(false);
		ds.mostlyForInsertOrRemove = mostlyForInsertOrRemove;
		return ds;
	}

	/**
	 * ����һ���ڿͻ���ִ��SQL�����DataSet���� Ĭ������Ҫ���ڶ�ȡ�������������.
	 * 
	 * @return the data set
	 */
	public static DataSet createClient() {
		return createClient(false);
	}

	/**
	 * ����һ���ڿͻ���ִ��SQL�����DataSet����.
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
	 * �ֶ�������
	 */
	private Set colNames;

	/**
	 * �ֶ���������ӳ��
	 */
	private Map colTypes;

	/**
	 * ��¼��������FOR_DELETE�ļ�¼��
	 */
	// protected Integer recordCount = new Integer(0);
	protected int recordCount = 0;

	/**
	 * �����¼���������������
	 */
	private transient Map listenerCache;

	/**
	 * DataSet��Ӧ�ı���
	 */
	private String name;

	/**
	 * DataSet������״̬��״̬����������@see StatefulData �ӿ���
	 */
	private int state = StatefulData.DS_BROWSE;

	/**
	 * ���ݼ��α�
	 */
	private int cursor = -1;

	/**
	 * ���ݼ�
	 */
	List dataCache;

	/**
	 * ��ǩ
	 */
	private Map bookmarkCache = new MyMap();

	/**
	 * ��ǩ����
	 */
	private SortedMap bookmarkIdx = new TreeMap();

	/**
	 * SQL��ѯ���
	 */
	private String sqlClause;

	/**
	 * SQL��ѯ����
	 */
	private Object[] queryParams;

	/** The data change listeners. */
	private transient List dataChangeListeners;

	/** The state change listeners. */
	private transient List stateChangeListeners;

	/**
	 * ����DataChange��Ӧ
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
	 * ���cancel������������
	 */
	public void addCancelListener(DataSetProcListener listener) {
		bindListener(CANCEL_EVENT, listener);
	}

	/**
	 * ������ݱ������
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
	 * ���delete������������
	 */
	public void addDeleteListener(DataSetProcListener listener) {
		bindListener(DELETE_EVENT, listener);
	}

	/**
	 * ���edit������������
	 */
	public void addEditListener(DataSetProcListener listener) {
		bindListener(EDIT_EVENT, listener);
	}

	/**
	 * ���insert������������
	 */
	public void addInsertListener(DataSetProcListener listener) {
		bindListener(INSERT_EVENT, listener);
	}

	/**
	 * ���add������������
	 */
	public void addOpenListener(DataSetProcListener listener) {
		bindListener(OPEN_EVENT, listener);
	}

	/**
	 * ���post������������
	 */
	public void addPostListener(DataSetProcListener listener) {
		bindListener(POST_EVENT, listener);
	}

	/**
	 * append��insert��ʵ�ʲ����࣬����Ľ�����Ӽ�¼�Ĺ���
	 * 
	 * @param index
	 *            ��Ӽ�¼���±�index
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
	 * ���״̬������¼�������
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
	 * ����������/ɾ�����ݲ���ʱ�����в�����֮�����ǩ�������궼Ҫ����.
	 * 
	 * @param insertPos
	 *            the insert pos
	 * @param delta
	 *            the delta ����ֵ >0������ֵ��<0��ǰ��ֵ
	 */
	protected void adjustedBookmarkPointer(int insertPos, int delta) {
		Integer insertPointer = delta > 0 ? new Integer(insertPos)
				: new Integer(insertPos + 1);// ��ɾ���Ͳ����������
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
	 * ���α����õ�ĩβ״̬
	 */
	public void afterLast() throws Exception {
		int index = this.isEmpty() ? 0 : this.dataCache.size();
		if (pointTo(index)) {
			this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
		}
	}

	/**
	 * ���һ����¼��append������DataSet�������ݼ�β���һ����ֵ��¼ ���ҽ���ǰ�α�ָ��������¼
	 */
	public void append() throws Exception {
		if (this.dataCache == null) {
			this.dataCache = createDataCache();
		}
		addRecord(-1);
	}

	/**
	 * �ڿͻ���ȷ�������޸�,���������Ҫ�ύ�����ݿ������ �������Ӧ�÷����ڷ���˵���POST֮���𵽺ͷ���� DataSetͬ����Ч��
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
				this.beforeFirst();// �ָ�����ǰ�α�ֵ
			}
			int preStat = this.toState(DS_BROWSE);
			if (preStat >= 0) {
				this.fireStateChange(preStat, DataChangeEvent.CHANGE_APPLIED);
			}
			maskDataChange(false);
		}
	}

	/**
	 * ���α����õ���ʼ״̬
	 */
	public void beforeFirst() throws Exception {
		if (pointTo(-1)) {
			this.fireDataChange(this, this, DataChangeEvent.CURSOR_MOVED);
		}
	}

	/**
	 * ����һ�������¼�������
	 * 
	 * @param eventName
	 *            �¼�����
	 * @param listener
	 *            ������
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
	 * �ж��Ƿ��ڼ�¼����ͷ��ָ�α�Ϊ-1
	 */
	public boolean bof() {
		return this.cursor < 0;
	}

	/**
	 * ����֮ǰ��δ��������в���������ɾ�������롢�޸ĵļ�¼
	 */
	public void cancel() throws Exception {
		if ((getState() & StatefulData.DS_EDIT) == StatefulData.DS_EDIT) {
			fireBeforeEvent(CANCEL_EVENT);
			maskDataChange(true);
			String bookmark = this.toogleBookmark();
			this.beforeFirst();
			for (++cursor; !eof(); cursor++) {// ���������ֶΣ����ҽ�ÿ���ֶ�undo
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
	 * ɾ������DataSet�����ݣ���Ӧ��ɾ�����ж�Ӧ��¼
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
	 * �����������ݿ⽻��ʱ�������Ϣ
	 */
	public Object clone() {
		return DataSetUtil.subDataSet(this, 0, this.dataCache.size());
	}

	/**
	 * �жϵ�ǰ��¼�Ƿ������ָ�����ֵ��ֶ�
	 * 
	 * @param name
	 *            �ֶ�����
	 * @return
	 */
	public boolean containsField(String name) {
		return this.pickUpRecord().containsKey(name);
	}

	/**
	 * �ж�ָ���ļ�¼�Ƿ������ָ�����ֵ��ֶ�
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
	 * �Ӳ�ѯ�ǽ��List<Map<String,Object>>�������ݼ�List<Map<String,Field>>
	 * 
	 * @param list
	 *            ��ѯ���
	 * @return �������
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
			// �����¼�� by qinj at Jan 23, 2009
			// this.recordCount = new Integer(dataCache.size());
			this.recordCount = dataCache.size();
		} catch (OutOfMemoryError e) {
			System.out
					.println("OutOfMemoryError thrown by gov.nbcs.rp.common.datactrl.DataSet#createCacheFrom(List list); Current size of the result is "
							+ this.dataCache.size());
		}
	}

	/**
	 * Creates the data cache. ���������Ĺ�ϵ�ж�ʹ�ú������͵�List��
	 * <p>
	 * Ĭ��ΪArrayList��һ������£���get(i)ȡֵЧ�ʸ���LinkedList��
	 * LinkedList�����ݲ����ɾ��Ч�ʸ���ArrayList��
	 * 
	 * @return the list ����DataSet�д洢���ݵ�List����
	 */
	protected List createDataCache() {
		// ���������Ĺ�ϵ�жϲ��ú������͵�List
		if (mostlyForInsertOrRemove) {
			return new LinkedList();
		} else {
			return new ArrayList();
		}
	}

	protected List createDataCache(int initialCapacity) {
		// ���������Ĺ�ϵ�жϲ��ú������͵�List
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
	 * ɾ����ǰ��¼
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
				newPosition = dataCache.size() - 1;// ɾ�����α����
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
	 * ���õ�ǰ��¼�ɱ༭
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
	 * �ж��Ƿ񵽼�¼��β�������¼��β��ָ�α��Ѿ�ָ�����һ����¼����һ���յ�λ
	 */
	public boolean eof() {
		return this.cursor >= dataCache.size();
	}
	
	/**
	 * �ж��Ƿ��ڼ�¼���в����α�ָ����ڵ�ĳһ����¼
	 */
	public boolean mof() {
		return (this.cursor >= 0) && (this.cursor < dataCache.size());
	}

	/**
	 * ���ֶ�����ȡ�ֶζ���
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
	 * ����һ����ǰ��¼���ֶζ��󼯣����������һ��Collection<Field>���� Field��������@see Field
	 */
	public Field[] fields() {
		return DataSetUtil.pickUpFields(pickUpRecord());
	}

	/**
	 * ����������
	 * 
	 * @param eventName
	 *            �¼�����
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
	 * ����������
	 * 
	 * @param eventName
	 *            �¼�����
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
	 * �������ݸ����¼�
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
	 * ����DataSet״̬�ı�
	 * 
	 * @param previousStat
	 *            �ı�֮ǰ��״̬
	 * @param eventType
	 *            ���״̬�ı���¼�����
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
	 * Gets the bookmark. ��ȡָ��λ�õ���ǩ
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
	 * ����������ǩ������
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
	 * ָ��fieldName����Ӧ��Class���ͣ���colTypesΪnullʱ������value��Class����
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
	 * ��ȡDataSet��Ӧ����
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ��ȡ��ǰ��¼��ԭʼ<�ֶ���,�ֶ�ֵ>��Ӧ��Map�ṹ
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
	 * ��õ�ǰ��¼��
	 */
	public int getRecNo() {
		return cursor;
	}

	/**
	 * ����ܵļ�¼��������FOR_DELETE�ļ�¼��
	 */
	public int getRecordCount() {
		// return recordCount.intValue();
		return recordCount;
	}

	/**
	 * ��ȡһ����¼��״̬���ⲿ ���������ڲ����ܶ�λ����ɾ���ļ�¼�����Բ����ܻ�ȡ��¼��ɾ��״̬
	 */
	public int getRecordState() {
		Record record = this.pickUpRecord();
		return record.getState();
	}

	/**
	 * ��õ�ǰDataSet��״̬
	 * 
	 * @return DataSet��״ֵ̬����Щֵ������@see StatefulData
	 */
	public int getState() {
		return state;
	}

	// /**
	// * ��ȡһ����¼��״̬
	// */
	// protected int getState(Record record) {
	// // return ((Integer) record.get(RECORD_STATE_ID)).intValue();
	// return record.getState();
	// }

	/**
	 * ���ٽ��α궨λ����ǩ��.
	 * 
	 * @param bookmark
	 *            the bookmark
	 * @param isSilent
	 *            the value is true if it is silent �Ƿ񼤻�DataChangeEvent.CURSOR_MOVED�¼�
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
	 * Goto bookmark silently. ������DataChangeEvent.CURSOR_MOVED�¼�
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
	 * ��append�Ĳ������ƣ����ǲ����¼��λ���ǵ�ǰ��¼��ǰһ��
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
	 * ��append�Ĳ������ƣ����ǲ����¼��λ���ǵ�ǰ��¼�ĺ�һ��
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
	 * �жϵ�ǰ��¼�Ƿ����ָ��������
	 * 
	 * @param condition
	 *            ָ����������Javascript�﷨���߼��ж����
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
	 * �жϵ�ǰ��¼�Ƿ����ָ��������
	 * 
	 * @param condition
	 *            ָ����������Java�﷨���߼��ж����
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
	 * �жϵ�ǰ��¼�Ƿ����ĳһ״̬����<code>getRecordState()</code>���ⲿ
	 * ���������ڲ����ܶ�λ����Delete�ļ�¼�����Դ˺���Ҳ�޷��жϼ�¼��ɾ��״̬
	 * 
	 * @param recordState
	 * @return
	 */
	protected boolean isCompatibleState(int recordState) {
		int state = pickUpRecord().getState();
		return (state & recordState) == state;
	}

	// /**
	// * �ж�ָ����¼�Ƿ����ĳһ״̬����<code>getRecordState()</code>���ⲿ
	// * ���������ڲ����ܶ�λ����Delete�ļ�¼�����Դ˺���Ҳ�޷��жϼ�¼��ɾ��״̬
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
	 * �ж����ݼ��Ƿ�Ϊ��
	 */
	public boolean isEmpty() {
		// return this.dataCache == null || this.dataCache.size() <= 0
		// || recordCount.intValue() <= 0;
		return (this.dataCache == null) || (this.dataCache.size() <= 0)
				|| (recordCount <= 0);
	}

	/**
	 * �жϵ�ǰ��¼���Ƿ��Ѿ����޸�
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
	 * �����ֶ�������λ�α�
	 * 
	 * @param fieldName
	 *            �ֶ�����
	 * @param value
	 *            �ֶ�ֵ
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
			// qinj ���� value��null�ж� 2011-6-11
			// qzc ȡ���Ƚ�ʱ�Ĵ�Сд����
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
	 * �����ֶ�������λ�α�
	 * 
	 * @param fieldName
	 *            �ֶ�����
	 * @param value
	 *            �ֶ�ֵ add by qzc
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
			// qinj ���� value1��value2��null�ж� 2011-6-11
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
	 * ����һϵ���ֶι���������λ�α�
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
	 * �ر�/����DataChange�¼���Ӧ
	 * 
	 * @param maskDataChange
	 */
	public void maskDataChange(boolean maskDataChange) {
		this.maskDataChange = maskDataChange;
	}

	/**
	 * �ر�/�������������Before/After�¼���Ӧ
	 * 
	 * @param maskDataChange
	 */
	public void maskOperationChange(boolean maskOperation) {
		this.maskOperation = maskOperation;
	}

	/**
	 * �ر�/����StateChange�¼���Ӧ
	 * 
	 * @param maskDataChange
	 */
	public void maskStateChange(boolean maskStateChange) {
		this.maskStateChange = maskStateChange;
	}

	/**
	 * ��Query�������ȡ���������������һ�����ϲ�ѯ�˷��������Ч �������ϲ�ѯ�����DataSet��PostЧ��Ҳ�ǻ�����Ч��
	 * 
	 * @param sqlClause
	 *            Query���
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
	 * �������ݼ���ͬʱ���ص�ǰ�Ƿ��Ѿ�eof()
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
	 * �򿪡�����һ��DataSet������ʹDataSet���ݼ������� ����ƶ���SQL��SQL parameters����������ʹ��SQL���
	 * ��ѯ��������DataSet���ݼ������򽫻���ƶ���name ȫ����Ϊ���ݼ�
	 */
	public void open() throws Exception {
		this.open(-1, -1);
	}

	// /**
	// * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ
	// *
	// * @param filterStr
	// * �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	// * @param changeState
	// * �Ƿ�ı�״̬
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
	// // ��ָ����¼����
	// if (isComfortable(data, filterStr)) {
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	//
	// // ֻ�����ɾ��״̬�ļ�¼
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
	// * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ.
	// *
	// * @param filterStr
	// * �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
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
	// * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ.
	// *
	// * @param filterStr
	// * �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
	// * @param changeState
	// * the change state �Ƿ�����״̬
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
	// // ��ָ����¼����
	// if (isComfortable2(record, filterStr)) {
	// Record newRecord = new Record(fields.length / 3 * 4);
	// for (int i = 0; i < fields.length; i++) {
	// Field newField = (Field) fields[i].clone();
	// newField.dataSet = subSet;
	// newField.record = newRecord;
	// newRecord.put(fields[i].getName(), newField);
	// if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	//
	// // ֻ�����ɾ��״̬�ļ�¼
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
	// * ���ݹ�����������һ���µ�DataSet�Ӽ�¼��
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ.
	// *
	// * @param filterStr
	// * �����������﷨�������javascript�ű����﷨������ע���ֶ���Լ����д��
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
	// * ���ݼ�¼״ֵ̬��ȡ��״̬���и�״̬�µļ�¼��������֯Ϊһ��DataSet
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ
	// *
	// * @param recordState
	// * ��¼״̬
	// * @param changeState
	// * �Ƿ����ü�¼״̬
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
	// if (changeState) {// �������״̬����ÿ���ֶεĸ���״̬Ҳȥ��
	// fields[i].applyUpdate();
	// }
	// }
	// subSet.dataCache.add(newRecord);
	// // ֻ�����ɾ��״̬�ļ�¼
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
	// * ���ݼ�¼״ֵ̬��ȡ��״̬���и�״̬�µļ�¼��������֯Ϊһ��DataSet
	// *
	// * @param recordState
	// * @return
	// */
	// public DataSet subDataSet(int recordState) throws Exception {
	// return subDataSet(recordState, false);
	// }

	/**
	 * ֧�ַ�ҳ��ѯ��open����
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
	 * open��ʵ�ʲ�����
	 * 
	 * @param sql
	 *            ��ѯSQL
	 * @param params
	 *            ��ѯ����
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
	 * Pick the up record. ��ǰcursor��ָrecord
	 * 
	 * @return the record
	 */
	public Record pickUpRecord() {
		return (Record) dataCache.get(cursor);
	}

	/**
	 * ���α궨λ��һ����λ�ã����ҷ���λ���Ƿ�仯����Ϣ(TRUE:�仯)
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
	 * �ύ����������δ����Ĳ������
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
				this.setName(this.getName());// �����Ѽ�����Ϣ
			}
			for (++cursor; !eof(); cursor++) {
				Record record = this.pickUpRecord();
				Field[] fields = DataSetUtil.pickUpFields(record);
				if (fields != null) {
					switch (record.getState()) {
					case FOR_DELETE:// ���ݴ�ɾ����������ɾ����SQL���
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
					case FOR_INSERT: // ���ݴ�����������ɲ������ݵ�SQL���
						sqls.add(SQLTool.insertSQL(getName(), fields,
								this.colNames));
						for (int i = 0; i < fields.length; i++) {
							fields[i].applyUpdate();
						}
						break;
					default:
						if (this.isModified()) {// �����޸ĵļ�¼����Update���
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
				this.beforeFirst();// �ָ�����ǰ�α�ֵ
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
	 * ���α����һλ����ͬʱ���ص�ǰ�Ƿ��Ѿ�bof()
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
	 * ȥ����ǩ
	 */
	public void removeBookmark() {
		Object mark = this.bookmarkIdx.remove(new Integer(cursor));
		if (mark != null) {
			this.bookmarkCache.remove(mark);
		}
	}

	/**
	 * ȥ��ָ������ǩ
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
	 * ����DataSet��Ӧ����
	 * 
	 * @param name
	 *            ����
	 */
	public void setName(String name) throws Exception {
		this.name = name;
		this.generateColInfo(this.isClient ? QueryStub.getClientQueryTool()
				: QueryStub.getQueryTool());
	}

	/**
	 * �ֶ���->�ֶ�ֵ��Ӧ��Map�ṹӳ�䵽DataSet�ļ�¼�ֶμ���
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
	 * ��ָ��record���뵱ǰλ��
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
	 * ����SQL��ѯ����
	 */
	public void setQueryParams(Object[] queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * ����һ����¼��״̬���ⲿ ���������ڲ����ܶ�λ����ɾ���ļ�¼�����Բ����ܻ�ȡ��¼��ɾ��״̬ ���Խ��ޱ仯�ļ�¼����Ϊ�޸ġ�ɾ��������ȵ�
	 * 
	 * @param state
	 */
	public void setRecordState(int state) {
		Record record = this.pickUpRecord();
		// this.setState(record, state);
		record.setState(state);
	}

	/**
	 * ����SQL��ѯ���
	 */
	public void setSqlClause(String sqlClause) {
		this.sqlClause = sqlClause;
		this.nameFromClause(sqlClause);
	}

	// /**
	// * ���ü�¼��״̬
	// *
	// * @param record
	// * ��¼
	// * @param state
	// * ״ֵ̬
	// */
	// protected void setState(Record record, int state) {
	// // record.put(RECORD_STATE_ID, new Integer(state));
	// record.setState(state);
	// }

	/**
	 * ����һ��ָ����¼����ǩ
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
	 * ����һ����ǰ��¼����ǩ����֮����п��ٶ�λ
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
	 * ��DataSetת�䵽ĳһ״̬�����ǰ��״̬��ͬ�᷵�ر����� ������ֵ-1��ʾ�ޱ仯��������Ǳ仯֮ǰ��״ֵ̬��
	 * 
	 * @param state
	 *            ת����״ֵ̬
	 * @return ״̬�Ƿ��б仯�����û�б仯������-1�����򷵻�ת��ǰ��״ֵ̬
	 */
	public int toState(int state) {
		int result = this.state == state ? -1 : this.state;
		this.state = state;
		return result;
	}

	// /**
	// * ��ȡָ����ʼ�ͳ��ȵ��Ӽ�.
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ
	// * <p>
	// * ע�⣺ɾ��״̬�ļ�¼Ҳռ��count
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
	// * ��ȡָ����ʼ�ͳ��ȵ��Ӽ�.
	// *
	// * <p>
	// * �����������ݿ⽻��ʱ�������Ϣ
	// * <p>
	// * ע�⣺ɾ��״̬�ļ�¼Ҳռ��count
	// *
	// * @param begin
	// * the begin
	// * @param count
	// * the count
	// * @param resetState
	// * �Ƿ����ü�¼״̬
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
	// // ֻ�����ɾ��״̬�ļ�¼
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
	 * ת��DataCache�Ĵ洢�ṹ.
	 * 
	 * @param destStorage
	 *            the to storage: <br>
	 *            0 for ArrayList����Ҫ���ڶ�ȡ��β�����ӵȲ���; <br>
	 *            1 for LinkedList����Ҫ���ڲ��롢ɾ���Ȳ���
	 *            <p>
	 *            ֻ���ڴ�������������£���Ŀ��洢�ṹ�������������ʱ�ſ���ʹ�ñ�������
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
