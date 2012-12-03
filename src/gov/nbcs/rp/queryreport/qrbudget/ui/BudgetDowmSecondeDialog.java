package gov.nbcs.rp.queryreport.qrbudget.ui;
 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common; 
import gov.nbcs.rp.common.ui.table.TablePanel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.Global;

/**
打印下达表，选择打印报表对话框
 * @author Administrator
 *
 */
public class BudgetDowmSecondeDialog extends FDialog {
	private static final long serialVersionUID = 1L;

	FTable tablePanel;
 

	// 批次
	int iBatchNo;
 

	FButton fbtnExport;

	FButton fbtnClose;

	public BudgetDowmSecondeDialog() {
		super(Global.mainFrame);

		this.setSize(400, 300);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		int w = this.getWidth();
		int h = this.getHeight();
		this.setLocation( (width - w) / 2, (height - h) / 2);
		
		this.setResizable(false);
		this.setModal(true);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void init() throws Exception {
		FTitledPanel ftitlePanel = new FTitledPanel();
		ftitlePanel.setTopInset(10);
		ftitlePanel.setLeftInset(10);
		ftitlePanel.setRightInset(10);
		ftitlePanel.setBottomInset(10);
		RowPreferedLayout rLay = new RowPreferedLayout(2);
		rLay.setColumnWidth(80);
		rLay.setColumnGap(2);
		ftitlePanel.setLayout(rLay);
 
		FLabel fBatchNo = Common.getBatchNoLabel("请选择要打印的下达表" );

		FLabel flblPath = new FLabel();
		flblPath.setText(" ");  
		 

		ftitlePanel.addControl(fBatchNo,
				new TableConstraints(1, 1, false, true));
		ftitlePanel.addControl(flblPath,
				new TableConstraints(1, 1, false, true));
		 
		
		
		tablePanel = new TablePanel(
				new String[][] { { "showfilename", "下达报表名称", "270" }}, true);
		for (int i = 0; i < tablePanel.getColumnModel().getColumnCount(); i++) {
			tablePanel.getColumnModel().getColumn(i).setMinWidth(0);
			tablePanel.getColumnModel().getColumn(i).setMaxWidth(800);
		}
		
		List list = new ArrayList();
		//showfilename
		Map map = new HashMap();
		map.put("index","1");
		map.put("showfilename","收入预算数下达书");
		list.add(map);
		Map map2 = new HashMap();
		map2.put("index","2");
		map2.put("showfilename","支出预算数下达书");
		list.add(map2);
		Map map3 = new HashMap();
		map3.put("index","3");
		map3.put("showfilename","政府采购预算通知书");
		list.add(map3);
		tablePanel.setData(list); 
		
		//设置全选
		tablePanel.setCheckBoxSelectedAtRow(0,true);
		tablePanel.setCheckBoxSelectedAtRow(1,true);
		tablePanel.setCheckBoxSelectedAtRow(2,true);

		ftitlePanel.addControl(tablePanel, new TableConstraints(1, 2, true,
				true));
		FTabbedPane ftabPnl = new FTabbedPane();
		ftabPnl.addControl(ftitlePanel, "下达表打印选择");
		
		// 下面按钮
		FFlowLayoutPanel ffpnlPanel = new FFlowLayoutPanel();
		ffpnlPanel.setAlignment(FlowLayout.RIGHT);
		fbtnExport = new FButton("fbtnExport", "确定打印");
		fbtnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				if(this.doOperate())
					BudgetDowmSecondeDialog.this.dispose();
			}

			private boolean doOperate() { 
				try {
					if (!checkCondition())
						return false;
				} catch (HeadlessException e) { 
					e.printStackTrace();
				} catch (Exception e) { 
					e.printStackTrace();
				} 
				setButtonState(false); 
				
				return true;
			}
		});
		fbtnClose = new FButton("fbtnExport", "  关闭  ");
		fbtnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//清空数据
				clear();
				BudgetDowmSecondeDialog.this.dispose();
			}
		});
		ffpnlPanel.addControl(fbtnExport);
		ffpnlPanel.addControl(fbtnClose);

		FPanel fPanel = new FPanel();
		fPanel.setLayout(new RowPreferedLayout(1));
		fPanel.addControl(ftabPnl, new TableConstraints(1, 1, true, true));
		fPanel.addControl(ffpnlPanel, new TableConstraints(2, 1, false, true));
		this.getContentPane().add(fPanel);
	} 

	/**
	 * 检查条件是否符合
	 * 
	 */
	private boolean checkCondition() throws HeadlessException, Exception {
		List lstDivInfo = tablePanel.getSelectedData();
//		if(JOptionPane.showMessageDialog(this, "确定要打印？！", "提示",
//				JOptionPane.OK_CANCEL_OPTION)==JOptionPane.CANCEL_OPTION){
//			//tablePanel.setData(null);//清空不打印
//			return false;
//		}
		
		if (lstDivInfo.size() == 0) {
			JOptionPane.showMessageDialog(this, "至少选择一张下达表！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}
	
	/**
	 * 清空数据
	 * @param bIsEnabled
	 */
	public void clear(){
		tablePanel.setData(new ArrayList());
	}
 

	private void setButtonState(boolean bIsEnabled) {
		fbtnExport.setEnabled(bIsEnabled);
		fbtnClose.setEnabled(bIsEnabled); 
	}
	
	public FTable getTablePanel (){
		return tablePanel;
	}
   

}
