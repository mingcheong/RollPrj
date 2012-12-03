/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: ��ʹ��String������Ϊ��ֵ��ʱ�򣬱�Map���Դ�Сд��<br>
 * 	               ����Map������ͨ������Keyֵ���еĲ���
 * </p>
 */
public class MyMap extends HashMap {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3292335444727085863L;

	public MyMap(int capacity) {
		this(capacity, 0.75f);
	}

	public MyMap(Map map) {
        this(map.size()/3*4);
		this.putAll(map);
	}

	public MyMap(int capacity, float factory) {
		super(capacity, factory);
	}

	public MyMap() {
		super();
	}

	public void putAll(Map map) {
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
            Object value = map.get(key);
			if (key == null) {
				super.put(key, value);
			} else {
				this.put(key, value);
			}
		}
	}	
	
	public Object get(Object key) {
		return super.get(getCustomKey(key));
	}

	public Object put(Object key, Object value) {
		return super.put(getCustomKey(key), value);
	}

	protected Object getCustomKey(Object key) {
		if ((key instanceof String) || (key instanceof Character)) {
			key = (key.toString()).toUpperCase(Locale.getDefault());
		}
		return key;
	}
	
	public boolean containsKey(Object key) {
		return super.containsKey(getCustomKey(key));
	}
	
	public Object remove(Object key) {
		return super.remove(getCustomKey(key));
	}
}