/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.list;

import com.foundercy.pf.control.FList;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

import java.util.LinkedList;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class CustomList extends FList implements DataChangeListener,
        ListSelectionListener {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String idName;

    private String textName;

    private DataSet dataSet;

    private boolean maskValueChange;

    public DataSet getDataSet() {
        return dataSet;
    }

    Map elementCache = new MyMap();

    private LinkedList listSelectionListeners;

    /**
     * 设置数据集
     * 
     * @param dataSet
     */
    public void setDataSet(DataSet ds) {
        if (ds != this.dataSet) {
            this.dataSet = ds;
            this.dataSet.addDataChangeListener(this);
            this.dataSet.addCancelListener(new CancelActionListener());
            this.dataSet.addDeleteListener(new DeleteActionListener());
        }
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public CustomList() throws Exception {
        this(null, null, null);
    }

    /**
     * 创建List控件
     * 
     * @param idName
     *            数据ID字段名
     * @param textName
     *            数据显示文本字段名
     * @param ds
     *            数据集
     */
    public CustomList(DataSet ds, String idName, String textName)
            throws Exception {
        this.setIdName(idName);
        this.setTextName(textName);
        this.setDataSet(ds);
        this.reset();
        this.getSelectionModel().addListSelectionListener(this);
    }

    /**
     * 设置是否多选
     */
    public void setAllowMultiSelect(boolean canMultiSel) {
        if (canMultiSel) {
			this
                    .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        if (this.listSelectionListeners == null) {
			this.listSelectionListeners = new LinkedList();
		}
        this.listSelectionListeners.addFirst(listener);
    }

    public void setSelectedValue(Object value, boolean shouldScroll) {
        if (this.elementCache.containsKey(value)) {
            MyListElement element = (MyListElement) this.elementCache
                    .get(value);
            super.setSelectedValue(element, shouldScroll);
        }
    }

    /**
     * 根据DataSet重建List数据
     * 
     * @throws Exception
     */
    public void reset() throws Exception {
        this.elementCache.clear();
        DefaultListModel model = new DefaultListModel();
        if (dataSet != null) {
            dataSet.maskDataChange(true);
            dataSet.beforeFirst();
            while (dataSet.next()) {
                String id = dataSet.fieldByName(this.getIdName()).getString();
                String text = dataSet.fieldByName(this.getTextName())
                        .getString();
                String bookmark = dataSet.toogleBookmark();
                MyListElement element = createElement(id, text, bookmark);
                model.addElement(element);
                this.elementCache.put(id, element);
            }
            dataSet.maskDataChange(false);
        }
        this.setModel(model);
    }

    public MyListElement getSelectedElement() {
        return (MyListElement) this.getSelectedValue();
    }

    /**
     * 获取选中的列表项
     * 
     * @return
     */
    public MyListElement[] getSelectedElements() {
        Object selected[] = this.getSelectedValues();
        MyListElement[] result = new MyListElement[selected.length];
        System.arraycopy(selected, 0, result, 0, result.length);
        return result;
    }

    /**
     * 创建一个ListElement数据
     * 
     * @param id
     * @param text
     * @param bookmark
     * @return
     */
    static MyListElement createElement(String id, String text, String bookmark) {
        MyListElement element = new MyListElement();
        element.id = id;
        element.text = text;
        element.bookmark = bookmark;
        return element;
    }

    /**
     * 数据集发生变化的时候更新List显示
     */
    public void onDataChange(DataChangeEvent event) throws Exception {
        if (event.type() != DataChangeEvent.FIELD_MODIRED) {
			return;
		}
        if ((!dataSet.bof() && !dataSet.eof()) || dataSet.isModified()) {
            Field field = (Field) event.getSource();
            String id = dataSet.fieldByName(getIdName()).getString();
            MyListElement element = getResponseElement(field, id, elementCache);
            if ((element != null) && field.isLatestModified()) {
                if (getIdName().equalsIgnoreCase(field.getName())) {
                    element.id = field.getString();
                } else if (getTextName().equalsIgnoreCase(field.getName())) {
                    element.text = dataSet.fieldByName(getTextName())
                            .getString();
                }
            } else {
                DefaultListModel model = (DefaultListModel) this.getModel();
                int idx = this.getSelectedIndex();
                MyListElement newElement = createElement(id, dataSet
                        .fieldByName(getTextName()).getString(), dataSet
                        .toogleBookmark());
                if ((idx >= 0) && (idx < model.size()) && (dataSet.getRecordState()==DataSet.FOR_INSERT)) {
                    model.add(idx, newElement);
                } else {
                    model.addElement(newElement);
                }
                this.elementCache.put(id, newElement);
            }
        }
    }

    static MyListElement getResponseElement(Field idField, String id, Map cache)
            throws Exception {
        MyListElement element = (MyListElement) cache.get(id);
        if (element == null) {
            Object key = idField.getPreviousValue();
            if (key != null) {
                element = (MyListElement) cache.get(key);
                if (element != null) {
                    cache.put(id, cache.remove(key));
                    element.id = id;
                }
            }
        }
        return element;
    }

    public void setMaskValueChange(boolean maskValueChange) {
        this.maskValueChange = maskValueChange;
    }

    /**
     * 选择变化的时候定位DataSet
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!maskValueChange) {
            try {
                if (!e.getValueIsAdjusting()) {
                    MyListElement element = getSelectedElement();
                    if(element!=null) {
                    	dataSet.gotoBookmark(element.getBookmark());
                    }
                }
                if (listSelectionListeners != null) {
                    for (int i = 0; i < listSelectionListeners.size(); i++) {
                        ((ListSelectionListener) listSelectionListeners.get(i))
                                .valueChanged(e);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class CancelActionListener implements DataSetProcListener {
        public void beforeProc(DataSetEvent event) throws Exception {
        }

        public void afterProc(DataSetEvent event) throws Exception {
            setMaskValueChange(true);
            reset();
            setMaskValueChange(false);
        }
    }

    class DeleteActionListener implements DataSetProcListener {
        public void beforeProc(DataSetEvent event) throws Exception {
            String id = dataSet.fieldByName(getIdName()).getString();
            MyListElement element = (MyListElement) elementCache.get(id);
            if (element != null) {
                elementCache.remove(id);
                ((DefaultListModel) getModel()).removeElement(element);
            }
        }

        public void afterProc(DataSetEvent event) throws Exception {
        }
    }
}
