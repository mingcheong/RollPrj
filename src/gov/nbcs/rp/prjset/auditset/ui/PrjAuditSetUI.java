package gov.nbcs.rp.prjset.auditset.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.SearchPanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.prjset.auditset.action.PrjAuditSetAction;
import gov.nbcs.rp.prjset.auditset.action.PrjAuditSetStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

public class PrjAuditSetUI extends RpModulePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FSplitPane pnlBase;

	private DataSet dsStep; // 审核流程步骤信息表

	private CustomTable table; // 外部流程表
    
	private String bmk;
	FTextField tfWID;
	FTextField tfWName;
	FTextArea taWC;
	FTextArea taWCShow;
	FCheckBox cbPD;

	FButton btnE;


	DataSet dsUnitInfo;
	CustomTree treeElement;
	CustomTree treeUserElement;
	CustomTree treeInUserElement;

	FButton btnOK = new FButton();

	private String widInfo;

	private FTextField lbCSCode = new FTextField("  处室编码  ");

	PrjAuditSetAction  pa = new PrjAuditSetAction();

	private int sOldStateevent = -1;

	private FCheckBox cbPD2;

	public void initize() {
		// TODO Auto-generated method stub
		try {
			this.add(getBasePanel());
			this.createToolBar();
			this.setButtonEditState(true);
			initData();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "初始化失败");
		}
	}

	private void initData() throws Exception {
		DataSet dsElement = PrjAuditSetStub.getMethod().getAuditElement(false, "");
		DataSet dsUserElement = PrjAuditSetStub.getMethod().getAuditElement(true,"");
	    treeElement = new CustomTree("审核元素", dsElement, "code", "name","par_id", null, "code", true);

		treeElement.reset();

		// treeElement.setIsCheck(true);
		// treeUserElement.setIsCheck(true);
		treeElement.setIsCheckBoxEnabled(true);

	}

	/**
	 * 获取主面板
	 * 
	 * @return
	 */
	private FSplitPane getBasePanel() {
		// pnlBase = new FSplitPane();
		// // 左右分
		// pnlBase.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		// // //设置面板的左面宽度
		// pnlBase.setDividerLocation(900);
		pnlBase = getRPanel();
		// pnlBase.addControl(getLeftTopPanel());
		return pnlBase;
	}

	/**
	 * 右面板
	 * 
	 * @return
	 */
	private FSplitPane getRPanel() {
		FSplitPane pnl = new FSplitPane();
		pnl.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnl.setDividerLocation(260);
		pnl.addControl(getRTopPanel());
		pnl.addControl(getRBotmPanel());
		return pnl;
	}

	/**
	 * 获取右上编辑面板
	 * 
	 * @return
	 */
	private FPanel getRTopPanel() {
		FPanel pnlTop = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(16);
		pnlTop.setLayout(lay);
		
		FTextArea taR = new FTextArea("配置说明");
		taR.setProportion(0.065f);
		StringBuffer text = new StringBuffer();
		text.append("1.步骤流程：即该步骤在整个流程中所处的流程位置\n");
		text.append("2.步骤名称：该名称会在审核界面上的流程信息中显示\n");
		text.append("3.审核元素：即该步骤是有谁来审核，可以是处室也可以是用户，可以多选\n");
		
		taR.setValue(text.toString());
		taR.setEnabled(false);

		tfWID = new FTextField("步骤流程");
		tfWID.setProportion(0.165f);
		tfWID.setEditable(false);
		tfWName = new FTextField("步骤名称");
		tfWName.setProportion(0.165f);
		
		taWCShow = new FTextArea("审核元素");
		taWC = new FTextArea("审核元素编码");
		taWCShow.setProportion(0.25f);
		
		cbPD = new FCheckBox("");
		cbPD.setTitle("是否插入历史表");
		cbPD2 = new FCheckBox("");
		cbPD2.setTitle("是否可修改数据");




		btnE = new FButton("ele", "选择");

		//taWC.setEditable(false);

		btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ElementTree et = new ElementTree(1, Common.nonNullStr(taWC
						.getValue()));
				Tools.centerWindow(et);
				et.setVisible(true);
				if (!et.isCancel && tfWName.isEditable()) {
					String[] str = pa.getSelElement(treeElement,true);
					taWC.setValue(getSplitString(str));
					String[] strShow = pa.getSelElementContent(treeElement);
					taWCShow.setValue(getSplitString(strShow));
					try {
						dsStep.maskDataChange(true);
						dsStep.gotoBookmark(bmk);
						dsStep.fieldByName("OPID")
								.setValue(getSplitString(str));
						dsStep.fieldByName("OPIDSHOW").setValue(
								getSplitString(strShow));
						dsStep.maskDataChange(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		

		taR.setProportion(0.05f);
		cbPD.setProportion(0.3f);
		cbPD2.setProportion(0.3f);
		taWCShow.setProportion(0.13f);
		tfWID.setProportion(0.13f);
		tfWName.setProportion(0.13f);
		
		


		pnlTop.addControl(taR, new TableConstraints(3, 15, true, true));
		pnlTop.addControl(new FLabel(), new TableConstraints(3, 1, true, true));
		pnlTop.addControl(new FLabel(), new TableConstraints(1, 15, true, true));
		pnlTop.addControl(tfWID, new TableConstraints(1, 6, true, true));
		pnlTop.addControl(tfWName, new TableConstraints(1, 6, true, true));
		
		pnlTop.addControl(new FLabel(), new TableConstraints(1, 15, true, true));
		pnlTop.addControl(taWCShow, new TableConstraints(3, 6, true, true));
		pnlTop.addControl(btnE, new TableConstraints(1, 1, true, true));
		pnlTop.addControl(new FLabel(), new TableConstraints(1, 7, true, true));
		pnlTop.addControl(cbPD, new TableConstraints(1, 8, true, true));
		pnlTop.addControl(cbPD2, new TableConstraints(1, 8, true, true));
		pnlTop.addControl(new FLabel(), new TableConstraints(1, 8, true, true));
		
		
		pnlTop.setTopInset(10);
		pnlTop.setLeftInset(10);
		pnlTop.setRightInset(10);
		pnlTop.setBottomInset(10);

		FPanel pnlRT = new FPanel();
		RowPreferedLayout layRT = new RowPreferedLayout(10);
		pnlRT.setLayout(layRT);

		FPanel pnlRC = new FPanel();
		RowPreferedLayout layRC = new RowPreferedLayout(5);
		pnlRC.setLayout(layRC);
 
		pnlRC.addControl(new FLabel(), new TableConstraints(1, 1, true, true));
		pnlRT.addControl(pnlTop, new TableConstraints(9, 10, false, true));
		pnlRT.addControl(pnlRC, new TableConstraints(1, 10, false, true));
		
		

		pnlRT.addControl(new FLabel(), new TableConstraints(1, 6, false, true));
		layRT.setRowGap(10);
		
	
		return pnlRT;
	}

	private String getSplitString(String[] str) {
		StringBuffer sc = new StringBuffer();
		if ((str != null) && (str.length != 0)) {
			for (int i = 0; i < str.length; i++) {
				if (i == str.length) {
					sc.append(str[i] + ";");
				}
				sc.append(str[i] + ";\n");
			}
		} else {
			return "";
		}
		return sc.toString();
	}

	private FPanel getRBotmPanel() {
		FPanel pnlTop = new FPanel();
		try {
			RowPreferedLayout lay = new RowPreferedLayout(2);
			pnlTop.setLayout(lay);

			String[] columnText = new String[] { "步骤", " 步骤名称 ", "  审核元素  " };
			String[] columnField = new String[] { "STEP_ID", "STEP_NAME", "AUDIT_OPER_NAME"};
			dsStep = PrjAuditSetStub.getMethod().getAuditStepInfo();
			table = new CustomTable(columnText, columnField, dsStep, false,
					null);
			setTableProp();
			pnlTop.addControl(table, new TableConstraints(15, 2, true, true));
//			table.setRowSelectionInterval(n, n);
//			dsStep.addDataChangeListener(null);
			dsStep.addDataChangeListener(
					new DataChangeListener() {
				private static final long serialVersionUID = 1L;

				public void onDataChange(DataChangeEvent event)
						throws Exception {
					if ((dsStep != null) && !dsStep.isEmpty() && !dsStep.bof()
							&& !dsStep.eof()) {
						String wid = dsStep.fieldByName("STEP_ID").getString();
						tfWID.setValue("第 " + wid + " 步");
						tfWName.setValue(dsStep.fieldByName("STEP_NAME")
								.getString());
						
						taWCShow.setValue(dsStep.fieldByName("AUDIT_OPER_NAME")
								.getString());
					    taWC.setValue(dsStep.fieldByName("AUDIT_OPER").getString());
					    String isinhis  =dsStep.fieldByName("IS_INSERTHIS").getString();
					    String iscanmod  =dsStep.fieldByName("is_canmod").getString();
					    
					    if("1".equals(isinhis) ) {
							cbPD.setValue(new Boolean(true));
						} else {
							cbPD.setValue(new Boolean(false));
						}
					    if("1".equals(iscanmod) ) {
							cbPD2.setValue(new Boolean(true));
						} else {
							cbPD2.setValue(new Boolean(false));
						}
						    
						    
					
						StringBuffer sql = new StringBuffer();
						sql.append("select * from rp_s_audit_step ");
						sql.append(" where 1 = 1");

						DataSet ds = DBSqlExec.client().getDataSet(
								sql.toString());
					//	String su = " ";
						ds.beforeFirst();
						

					}
				}
			});
		} catch (Exception ee) {
		ErrorInfo.showErrorDialog(ee, "生成步骤审核表失败");
	}
		return pnlTop;
	}

	private void setTableProp() throws Exception {
		table.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.reset();
	
		table.getTable().getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getTable().getColumnModel().getColumn(1).setPreferredWidth(195);
		table.getTable().getColumnModel().getColumn(2).setPreferredWidth(475);
//		
//		table.getTable().getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTable().setRowHeight(30);
		table.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));
		table.getTable().getTableHeader()
				.setPreferredSize(new Dimension(4, 30));
	}

	/**
	 * 设置工具栏的按钮状态
	 * 
	 * @param aState
	 */
	public void setButtonEditState(boolean isEdit) {
	//	boolean isD = GlobalEx.isFisVis();
		boolean isD = true;
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btnGet = (FButton) controls.get(i);
			if ("增加".equals(btnGet.getText())) {
				btnGet.setEnabled(isEdit & isD);
			}
			if ("修改".equals(btnGet.getText())) {
				btnGet.setEnabled(isEdit & isD);
			}
			if ("删除".equals(btnGet.getText())) {
				btnGet.setEnabled(isEdit & isD);
			}
			if ("保存".equals(btnGet.getText())) {
				btnGet.setEnabled(!isEdit & isD);
			}
			if ("取消".equals(btnGet.getText())) {
				btnGet.setEnabled(!isEdit & isD);
			}
		}
		this.cbPD.setEditable(!isEdit & isD);
		this.cbPD2.setEditable(!isEdit & isD);
     	this.tfWName.setEditable(!isEdit & isD);
     	this.taWC.setEditable(!isEdit & isD);
     	this.taWCShow.setEditable(!isEdit & isD);
//		this.cbUnitType.setEditable(!isEdit & isD);
		if (isD) {
			if (isEdit) {
				this.btnE.setText("选择");
//				this.btnU.setText("查看");
			} else {
				this.btnE.setText("选择");
//				this.btnU.setText("选择");
			}
		} else {
			this.btnE.setText("查看");
//			this.btnU.setText("查看");
		}
	}

	public void doExport() {
		// TODO Auto-generated method stub

	}

	public void doSendAudit() {
		// TODO Auto-generated method stub

	}

	public void doBackAudit() {
		// TODO Auto-generated method stub

	}

	public void doBackToDivAudit() {
		// TODO Auto-generated method stub

	}

	public void doAdd() {
		// 增加
		try {
			 //dsStep.maskDataChange(true);
			String nextWID = String
					.valueOf(Integer.parseInt(DBSqlExec.client().getMaxValueFromField("RP_S_AUDIT_STEP", "STEP_ID", "")) + 1);
			this.tfWID.setValue(nextWID);

			if ((dsStep != null) && !dsStep.isEmpty()) {
				dsStep.append();
				dsStep.fieldByName("STEP_ID").setValue(nextWID);
				
				bmk = dsStep.toogleBookmark();
			} else {
				dsStep.append();
				dsStep.fieldByName("STEP_ID").setValue(" ");
				dsStep.fieldByName("STEP_NAME").setValue(" ");
				dsStep.fieldByName("AUDIT_OPER").setValue(" ");
				dsStep.fieldByName("IS_USER").setValue(" ");
				dsStep.fieldByName("IS_INSERTHIS").setValue(" ");
				dsStep.fieldByName("RG_CODE").setValue("330212");
				dsStep.fieldByName("SET_YEAR").setValue(Global.loginYear);
				dsStep.append();
//				dsStep.fieldByName("STEP_ID").setValue(nextWID);
				bmk = dsStep.toogleBookmark();
			}
			this.setButtonEditState(false);
			setTableProp();
			// dsStep.maskDataChange(false);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "增加失败");
		}
	}

	public void doModify() {
		try {
			if (dsStep.isEmpty() || dsStep.bof() || dsStep.eof()) {
				JOptionPane.showMessageDialog(pnlBase, "请选择要修改的步骤");
				return;
			}
			if ("0".equalsIgnoreCase(dsStep.fieldByName("step_id").getString())) {
				JOptionPane.showMessageDialog(pnlBase, "流程起始节点不允许删除及修改");
				return;
			}
			this.setButtonEditState(false);
			dsStep.edit();
			bmk = dsStep.toogleBookmark();
			// setTableProp();

		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "修改失败");
		}
	}

	public void doCancel() {
		// 取消
		try {
			dsStep.cancel();
			dsStep.applyUpdate();
			this.setButtonEditState(true);
			setTableProp();
			this.tfWID.setValue(" ");
			this.tfWName.setValue(" ");

			this.taWC.setValue(" ");
//			this.cbUnitType.setEnabled(false);
			table.getTable().setEnabled(true);
			lbCSCode.setEditable(false);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "取消失败");
		}
	}

	public void doSave() {
		// 保存
		try {
			if (Common.isNullStr(Common.nonNullStr(tfWName.getValue()))) {
				JOptionPane.showMessageDialog(pnlBase, "名称不允许为空");
				return;
			}

			if (Common.isNullStr(Common.nonNullStr(taWC.getValue()))) {
				JOptionPane.showMessageDialog(pnlBase, "审核元素不允许为空");
				return;
			}
			
			String rgCode = Global.getCurrRegion();
//			boolean his  = ((Boolean) cbPD.getValue()).booleanValue();
//			
////			boolean repeatIndicator = Boolean.valueOf(his).booleanValue();
//////			boolean ifhis = his;
			String isinserthis;
		    if(((Boolean) cbPD.getValue()).booleanValue())
			{
		    	isinserthis= "1";	
			} else {
				isinserthis="0";
			}
		    String canMod;
		    if(((Boolean) cbPD2.getValue()).booleanValue())
			{
		    	canMod= "1";	
			} else {
				canMod="0";
			}
			
			dsStep.maskDataChange(true);
			dsStep.gotoBookmark(bmk);
			dsStep.fieldByName("step_name").setValue(this.tfWName.getValue());
   		    dsStep.fieldByName("is_inserthis").setValue(isinserthis);
   		    dsStep.fieldByName("is_canmod").setValue(canMod);
   		    dsStep.fieldByName("rg_code").setValue(rgCode);

			dsStep.fieldByName("audit_oper").setValue(this.taWC.getValue());
			dsStep.fieldByName("audit_oper_name").setValue(this.taWCShow.getValue());
			dsStep.fieldByName("set_year").setValue(Global.loginYear);
			PrjAuditSetStub.getMethod().dsPost(dsStep);
			dsStep.applyUpdate();
			this.setButtonEditState(true);
			setTableProp();
	//		UpdateUnitWidInfo();
//			this.cbUnitType.setEnabled(false);
			table.getTable().setEnabled(true);
			lbCSCode.setEditable(false);
			dsStep.maskDataChange(false);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "保存失败");
		}
	}



	public void doDelete() {
		// 删除
		try {
			if (dsStep.isEmpty() || dsStep.bof() || dsStep.eof()) {
				JOptionPane.showMessageDialog(pnlBase, "请选择要删除的步骤");
				return;
			}
			if ("0".equalsIgnoreCase(dsStep.fieldByName("STEP_ID").getString())) {
				JOptionPane.showMessageDialog(pnlBase, "流程起始节点不允许删除及修改");
				return;
			}
			dsStep.delete();
			this.setButtonEditState(false);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "删除失败");
		}
	}

	public void doExprtOtherInfo() {
		// TODO Auto-generated method stub
	}

	private class ElementTree extends FDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private CustomTree tree;

		private String str;

		private boolean isCancel = false;

		private FPanel pnlBased;
		
		private int type;

		public ElementTree(int type, String str) {
			super(Global.mainFrame); // 指定窗体父亲
			this.type = type;
			this.setResizable(false); // 设置窗体大小是否可变
			switch(type){
			case 0:
				tree = treeUserElement;
				this.setTitle("审核负责人选择界面"); // 设置窗体标题
				break;
			case 1:
				tree = treeElement;
				this.setTitle("审核元素选择界面"); // 设置窗体标题
				break;
			case 2:
				tree = treeInUserElement;
				this.setTitle("审核接收人选择界面"); // 设置窗体标题
				break;
		    default:
		    	break;
			}
//			if (isUser)
//				tree = treeUserElement;
//			else
//				tree = treeElement;
			this.str = str;
//			this.isUser = isUser;
			this.setSize(400, 500); // 设置窗体大小
			this.getContentPane().add(getBasePanel());
			this.dispose(); // 窗体组件自动充满。
//			if (type==1)
//				this.setTitle("审核负责人选择界面"); // 设置窗体标题
//			else
//				this.setTitle("审核人选择界面"); // 设置窗体标题
			this.setModal(true); // 设置窗体模态显示
			// tree.setIsCheckBoxEnabled(cbUnitType.isEnabled());
		}

		private FPanel getBasePanel() {
			pnlBased = new FPanel();
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlBased.setLayout(layBase);

			FPanel pnlUnder = new FPanel(); // 下面放按钮的面板
			RowPreferedLayout layUnder = new RowPreferedLayout(10);
			layUnder.setColumnWidth(60);
			layUnder.setRowGap(5);
			pnlUnder.setLayout(layUnder);
			btnOK.setText("确定");
			FButton btnCancel = new FButton();
			btnCancel.setText("取消");
			pnlUnder.addControl(new FLabel(), new TableConstraints(1, 3, false,
					true));
			pnlUnder.addControl(btnOK, new TableConstraints(1, 2, false, true));
			pnlUnder.addControl(btnCancel, new TableConstraints(1, 2, false,
					true));
			pnlUnder.addControl(new FLabel(), new TableConstraints(1, 2, false,
					true));

			FScrollPane pnlTree = new FScrollPane();
			pnlTree.addControl(tree);
			// 增加类档树
			pnlBased.addControl(new SearchPanel(tree, "code", "name"),
					new TableConstraints(1, 1, false, true));
			pnlBased.addControl(pnlTree, new TableConstraints(17, 1, false,
					true));
			pnlBased.addControl(pnlUnder, new TableConstraints(1, 1, false,
					true));
			// 取消按钮的操作时间
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						isCancel = true;
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			// 确定按钮的确定时间
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (!btnOK.isEnabled()) {
							return;
						}
						if ((type==1) && !checkUserAndDep(tree)) {
							JOptionPane.showMessageDialog(pnlBased,
									"业务处室和用户不能单独处于同一流程位置");
							return;
						}
						isCancel = false;
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			pnlBased.setTopInset(10);
			pnlBased.setLeftInset(10);
			pnlBased.setBottomInset(10);
			pnlBased.setRightInset(10);
			pa.setSelElement(tree, str);
			btnOK.setEnabled(tfWName.isEditable());
			return pnlBased;
		}

		public boolean isCancel() {
			return this.isCancel;
		}
	}

	private boolean checkUserAndDep(CustomTree tree) throws Exception {
		dsStep.maskDataChange(true);
		// 如果是选择审核元素时同事选择了业务处室和单位，则是不允许的
		List list = new ArrayList();
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		while (enumeration.hasMoreElements()) { // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (node.getLevel() != 1) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if ("用户".equalsIgnoreCase(pNode.getShowContent())
					|| "业务处室".equalsIgnoreCase(pNode.getShowContent())) {
				list.add(String.valueOf(pNode.getIsSelect()));
			}
		}
		for (int i = 1; i < list.size(); i++) {
			if (Common.nonNullStr(list.get(0)).equalsIgnoreCase(
					Common.nonNullStr(list.get(i)))) {
				return false;
			}
		}
		dsStep.gotoBookmark(bmk);
		if ("true".equalsIgnoreCase(Common.nonNullStr(list.get(0)))) {
			dsStep.fieldByName("IS_USER").setValue("1");
		} else {
			dsStep.fieldByName("IS_USER").setValue("0");
		}
		dsStep.maskDataChange(false);
		return true;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
