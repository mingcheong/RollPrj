/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.list;

import gov.nbcs.rp.common.Common;

public class MyListElement implements Comparable {
    String id;

    String text;

    String bookmark;

    public String getBookmark() {
        return bookmark;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
    
    public String toString() {
        return text;
    }

    public int compareTo(Object o) {
        return Common.nonNullStr(this).compareTo(Common.nonNullStr(o));
    }
}
