package gov.nbcs.rp.common.ui.table;

import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FBaseTableRowCellRenderer;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.XMLData;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;


public class TablePanel extends FTable
{ 
 
    public static int SINGLE_SELECTION = ListSelectionModel.SINGLE_SELECTION;
 
    public static int SINGLE_INTERVAL_SELECTION = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
 
    public static int MULTIPLE_INTERVAL_SELECTION = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    
	private static final long serialVersionUID = 1L;
	public TablePanel(String[][] columns,boolean isCheck)
	{
		init(columns,isCheck);
	}

	private void init(String[][] columns, boolean isCheck)
	{ 
		this.setIsCheck(isCheck); 
		for (int i = 0; i < columns.length; i++) {
			String[] col = columns[i];
			FBaseTableColumn column = new FBaseTableColumn();
			 
			column.setId(col[0]);
			column.setTitle(col[1]);
			if(col.length==3)
			{
				column.setPreferredWidth(Integer.parseInt(col[2]));
				column.setMaxWidth(Integer.parseInt(col[2]));
				column.setMinWidth(Integer.parseInt(col[2]));
			}
			this.addColumn(column);
		}
		
	}
	public void setSelectionMode(int mode)
	{
		this.getRightActiveTable().setSelectionMode(mode);
		this.getLeftLockedTable().setSelectionMode(mode);
	}
	public List getAllSelectedData()
	{
		int[] rows = this.getRightActiveTable().getSelectedRows();
		List list = new ArrayList();
		List data = this.getData();
		for (int i = 0; i < rows.length; i++) {
			list.add(data.get(rows[i]));
		}
		return list;
	}
	public void deleteAllSelectedData()
	{
		int[] rows = this.getRightActiveTable().getSelectedRows();
		
		for (int i = rows.length-1; i > -1; i--) {
			this.deleteRow(rows[i]);
		} 
	}
	/**
	 * 只更新table显示的列
	 * @param data
	 * @param rowNum
	 */
	public void refreshRowData(XMLData data ,int rowNum)
	{ 
		FBaseTableColumn[] columns = this.getAllColumns();
		for (int i = 0; i < columns.length; i++) {
			FBaseTableColumn column = columns[i];
			this.getModel().setValueAt(data.get(column.getId()), rowNum, i);
		} 
	}
	public void lockTable()
	{
		this.getLeftLockedTable().setEnabled(false);
		this.getRightActiveTable().setEnabled(false);
	}
	public void unLockTable()
	{
		this.getLeftLockedTable().setEnabled(true);
		this.getRightActiveTable().setEnabled(true);
	}
	public void setRowDeleteIcon(int rowIndex)
	{  
		this.getLeftLockedTable().getColumnModel().getColumn(0).setCellRenderer(new RowStateRenderer(rowIndex));
	}
	class RowStateRenderer  extends FBaseTableRowCellRenderer
	{
		private static final long serialVersionUID = 1L;
		int rowIndex;
		 public RowStateRenderer(int rowIndex)
		 {
			 this.rowIndex = rowIndex;
		 }
		public  Component getTableCellRendererComponent(JTable table1,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column)
		{ 
			Component com =  super.getTableCellRendererComponent(table1,value,isSelected,hasFocus,row,column);
			if(row == rowIndex){
//				Icon icon = new ImageIcon("images/fbudget/close.jpg");
//				com.setIcon(icon);
//				int h = icon.getIconHeight();
//				int w = icon.getIconWidth();
//				
//				com.setText("X");
				com.setForeground(Color.red);
			} 
			
			return com;
		}
 
	}
	public void cancelRowDeleteIcon() { 
		this.getLeftLockedTable().getColumnModel().getColumn(0).setCellRenderer(new RowStateRenderer(-1));
	}
}