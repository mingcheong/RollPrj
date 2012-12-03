package gov.nbcs.rp.sync.ui;

/**
 * 数据同步主界面
 * 
 */

import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.query.action.QrSync;
import gov.nbcs.rp.sync.action.ScrAction;
import gov.nbcs.rp.sync.util.MainInfoOper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;

public class ScronyUI extends RpModulePanel {

	protected static final long serialVersionUID = 1L;
	// 业务处理类
	protected MainInfoOper oper = null;
	// 主面板
	public FPanel mainPanel = null;
	// topPanel
	public FPanel topPanel = null;



	// middlePanel
	public FPanel middlePanel = null;


	public FComboBox schemeComboBox;

	// bottomPanel
	public FPanel bottomPanel;

	public CustomTable tbDetail;

//	SyncInterface syncService;
	
	

	/**
	 * 构造方法
	 */
	public ScronyUI() {
	}

	/**
	 * 界面初始化
	 */
	public void initize() {
	
		try {
			initData();
			
		
			this.add(getPanel());
			this.createToolBar();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "初始化面板失败");
		}
	

	}
	private void initData() throws Exception {

	}

	/**
	 * 取得必须的属性面板
	 * 
	 * @return
	 * @throws ControlException
	 */
	public FPanel getPanel() throws Exception {
		if (mainPanel == null) {
			mainPanel = new FPanel();
			RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
			mainPanel.setLayout(rowPreferedLayout);
//			
			mainPanel.addControl(getTablePanel(), new TableConstraints(1, 1,
					true, true));
			mainPanel.setLeftInset(10);
			mainPanel.setRightInset(10);
			mainPanel.setBottomInset(10);
			mainPanel.setTopInset(10);
		}
		return mainPanel;
	}

//	

	/**
	 * 
	 * @return
	 * @throws Exception
	 */

	

	protected void setTableData(String string) {
		tbDetail.setDataSet(setListData(string));
		try {
			tbDetail.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected DataSet setListData(String string) {
		DataSet dataSet = null;
		try {
			if ((string==null) ||"".equals(string) || "all".equals(string.trim())) {
				dataSet = ScrAction.getMethod().getScronyTableData("");
			} else {
				dataSet = ScrAction.getMethod().getScronyTableData(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 
	 * @return
	 * @throws ControlException
	 */
	protected FPanel getTablePanel() throws ControlException {
		if (bottomPanel == null) {
			bottomPanel = new FPanel();
		}

		try {

			DataSet ds = this.setListData(null);
			tbDetail = new CustomTable(new String[]{"表名","中文名称","说明"},new String[]{"table_ename","table_cname","remark"},ds,true,null);
			
			setTableProp();
			
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.addControl(tbDetail, BorderLayout.CENTER);
			
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return bottomPanel;
	}


	
	private void setTableProp() throws Exception {
		tbDetail.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbDetail.reset();

		tbDetail.getTable().getColumnModel().getColumn(0).setPreferredWidth(100);
		tbDetail.getTable().getColumnModel().getColumn(1).setPreferredWidth(200);
		tbDetail.getTable().getColumnModel().getColumn(2).setPreferredWidth(200);
		tbDetail.getTable().getColumnModel().getColumn(3).setPreferredWidth(400);
		

		tbDetail.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbDetail.getTable().setRowHeight(30);
		tbDetail.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));
		tbDetail.getTable().getTableHeader()
				.setPreferredSize(new Dimension(4, 30));
	}

	public void  doInsert(){
		int[] rows = tbDetail.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择需要结转的表");
			return;
		}

		try {
			String [] tableSy = new String[rows.length] ;
			for (int i = 0; i < rows.length; i++) {
				Object check = tbDetail.getTable().getValueAt(rows[i], 0);
				if (((Boolean) check).booleanValue()) {
					if (tbDetail.getDataSet().gotoBookmark(tbDetail.rowToBookmark(rows[i]))) {
						String table_ename = tbDetail.getDataSet().fieldByName("table_ename")
								.getString();
						tableSy [i]=table_ename;
					}
				}
			}
		
	
			QrSync.getMethod().scronyNextyear(Global.loginYear, Global.getCurrRegion(), tableSy);

			JOptionPane.showMessageDialog(null, "结转基础数据成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "结转失败");
			e.printStackTrace();
		}
		
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
	
	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}
	
	
	public void doSave() {
		int[] rows = tbDetail.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择需要同步的表");
			return;
		}

		try {
			String [] tableSy = new String[rows.length] ;
			for (int i = 0; i < rows.length; i++) {
				Object check = tbDetail.getTable().getValueAt(rows[i], 0);
				if (((Boolean) check).booleanValue()) {
					if (tbDetail.getDataSet().gotoBookmark(tbDetail.rowToBookmark(rows[i]))) {
						String table_ename = tbDetail.getDataSet().fieldByName("table_ename")
								.getString();
						tableSy [i]=table_ename;
					}
				}
			}
		
	
			QrSync.getMethod().scronyNextyear(Global.loginYear, Global.getCurrRegion(), tableSy);

			JOptionPane.showMessageDialog(null, "结转基础数据成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "结转失败");
			e.printStackTrace();
		}
	}



}
