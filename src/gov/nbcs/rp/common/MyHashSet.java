/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class MyHashSet extends HashSet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public MyHashSet() {
        super();
    }

    public MyHashSet(Collection c) {
        super(c);
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MyHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public boolean add(Object o) {
        return super.add(getCustomKey(o));
    }

    public boolean contains(Object o) {
        return super.contains(getCustomKey(o));
    }

    public boolean remove(Object o) {
        return super.remove(getCustomKey(o));
    }
    
    protected Object getCustomKey(Object key) {
        if ((key instanceof String) || (key instanceof Character)) {
            key = (key.toString()).toUpperCase(Locale.getDefault());
        }
        return key;
    }
}
