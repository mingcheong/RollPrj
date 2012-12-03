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

import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.DataSetProcListener;

import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;


public class CustomComboBox extends FComboBox implements DataChangeListener,
        ValueChangeListener {
    private DataSet dataSet;

    private String idName;

    private String textName;

    private Map elementCache = new MyMap();

    private Vector elementDatas = new Vector();

    private LinkedList itemListeners;

    private boolean maskValueChange;

    public DataSet getDataSet() {
        return dataSet;
    }

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

    /**
     * 构造一个ComboBox
     * 
     * @param ds
     * @param idName
     * @param textName
     */
    public CustomComboBox(DataSet ds, String idName, String textName)
            throws Exception {
        this.setDataSet(ds);
        this.setIdName(idName);
        this.setTextName(textName);
        this.reset();
        super.addValueChangeListener(this);
    }
    
    /**
     * 构造一个ComboBox
     * 
     * @param ds
     * @param idName
     * @param textName
     */
    public CustomComboBox()
            throws Exception {
        this(null,null,null);
    }

    /**
     * 返回swing原始控件
     * 
     * @return
     */
    public JComboBox getComboBox() {
        JComboBox box = (JComboBox) this.getEditor();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < elementDatas.size(); i++) {
            model.addElement(elementDatas.get(i));
        }
        box.setModel(model);
        return box;
    }

    public void addValueChangeListener(ValueChangeListener listener) {
        if (itemListeners == null) {
			itemListeners = new LinkedList();
		}
        itemListeners.addFirst(listener);
    }
    
    /**
     * 根据ID设置选中的项
     * @param elementId
     */
    public void setSelectedValue(Object elementId) {
        this.setValue(this.elementCache.get(elementId));
    }

    /**
     * 重建列表数据
     * 
     * @throws Exception
     */
    public void reset() throws Exception {
        elementCache.clear();
        elementDatas.clear();
        if (dataSet != null) {
            dataSet.maskDataChange(true);
            dataSet.beforeFirst();
            while (dataSet.next()) {
                String id = dataSet.fieldByName(this.getIdName()).getString();
                String text = dataSet.fieldByName(this.getTextName())
                        .getString();
                String bookmark = dataSet.toogleBookmark();
                MyListElement element = CustomList.createElement(id, text,
                        bookmark);
                elementDatas.addElement(element);
                elementCache.put(id, element);
            }
            dataSet.maskDataChange(false);
            this.setRefModel(elementDatas);
        }
    }

    /**
     * 数据变动的时候改变列表内容
     */
    public void onDataChange(DataChangeEvent event) throws Exception {
        if (event.type() != DataChangeEvent.FIELD_MODIRED) {
			return;
		}
        if ((!dataSet.bof() && !dataSet.eof()) || dataSet.isModified()) {
            Field field = (Field) event.getSource();
            String id = dataSet.fieldByName(getIdName()).getString();
            MyListElement element = CustomList.getResponseElement(field, id,
                    elementCache);
            if ((element != null) && field.isLatestModified()) {
                if (getIdName().equalsIgnoreCase(field.getName())) {
                    element.id = field.getString();
                } else if (getTextName().equalsIgnoreCase(field.getName())) {
                    element.text = dataSet.fieldByName(getTextName())
                            .getString();
                }
            } else {
                MyListElement newElement = CustomList.createElement(id, dataSet
                        .fieldByName(getTextName()).getString(), dataSet
                        .toogleBookmark());
                this.insertItemAt(newElement, 0);
                this.elementCache.put(id, newElement);
                elementDatas.add(0, newElement);
            }
        }
    }

    /**
     * 当DataSet取消时的响应处理
     */
    class CancelActionListener implements DataSetProcListener {
        public void beforeProc(DataSetEvent event) throws Exception {
        }

        public void afterProc(DataSetEvent event) throws Exception {
            maskValueChange(true);
            reset();
            maskValueChange(false);
        }
    }

    /**
     * 当DataSet删除一条记录时的响应处理
     */
    class DeleteActionListener implements DataSetProcListener {
        public void beforeProc(DataSetEvent event) throws Exception {
            String id = dataSet.fieldByName(getIdName()).getString();
            MyListElement element = (MyListElement) elementCache.get(id);
            if (element != null) {
                elementCache.remove(id);
                removeItem(element);
                elementDatas.remove(element);
            }
        }

        public void afterProc(DataSetEvent event) throws Exception {
        }
    }

    public void maskValueChange(boolean maskValueChange) {
        this.maskValueChange = maskValueChange;
    }
    
    public void valueChanged(ValueChangeEvent e) {
        if (!maskValueChange) {
            try {
                MyListElement element = (MyListElement) e.getNewValue();
                dataSet.gotoBookmark(element.getBookmark());
                if (this.itemListeners != null) {
                    for (int i = 0; i < this.itemListeners.size(); i++) {
                        ((ValueChangeListener) itemListeners.get(i))
                                .valueChanged(e);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
