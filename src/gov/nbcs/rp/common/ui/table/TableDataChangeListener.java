/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.table;

import gov.nbcs.rp.common.Common;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


public class TableDataChangeListener implements TableModelListener {
    private boolean hasCheck;

    private CustomTable table;

    /**
     * 构造一个表格数据变化的侦听器
     * 
     * @param ds
     *            表格绑定的数据集
     * @param table
     *            表格
     * @param hasCheck
     *            是否具有CheckBox
     */
    public TableDataChangeListener(CustomTable table, boolean hasCheck) {
        this.hasCheck = hasCheck;
        this.table = table;
    }

    /**
     * 表格数据发生变化鸟
     */
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if (hasCheck && (e.getColumn() == 0)) {
                doLeaderCheck();
                boolean checked = ((Boolean) table.getTable().getModel()
                        .getValueAt(e.getLastRow(), 0)).booleanValue();
                if (checked) {
                    table.fireValueChanged(new ListSelectionEvent(
                            e.getSource(), e.getFirstRow(), e.getLastRow(),
                            false));
                }
            }
            this.fireDataSetUpdate(e);
        }
    }

    /**
     * table数据变更的同时更新DataSet相应数据
     */
    protected void fireDataSetUpdate(TableModelEvent e) {
        TableModel model = table.getTable().getModel();
        Object value = model.getValueAt(e.getLastRow(), e.getColumn());
        TableColumnModel colModel = table.getTable().getColumnModel();
        String bookmark = (String) model.getValueAt(e.getLastRow(), model
                .getColumnCount() - 1);
        try {
            table.getDataSet().maskDataChange(true);
            table.getDataSet().gotoBookmark(bookmark);
            for (int i = 0; i < colModel.getColumnCount(); i++) {
                TableColumn col = colModel.getColumn(i);
                if (col.getModelIndex() == e.getColumn()) {
                    String fieldName = Common.nonNullStr(col.getIdentifier());
                    if (table.getDataSet().containsField(fieldName)) {
                        table.getDataSet().fieldByName(fieldName).setValue(
                                value);
                    }
                    break;
                }
            }
            table.getDataSet().maskDataChange(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 通过遍历表内所有相关CheckBox来设置表头CheckBox的勾选状态
     */
    void doLeaderCheck() {
        TableModel model = table.getTable().getModel();
        boolean result = true;
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean checked = ((Boolean) model.getValueAt(i, 0)).booleanValue();
            result = result && checked;
        }
        MyHeaderRenderer renderer = (MyHeaderRenderer) table.getTable()
                .getTableHeader().getDefaultRenderer();
        if (renderer.isChecked() != result) {
            renderer.setChecked(result);
            table.getTable().getTableHeader().repaint();
        }
    }
}
