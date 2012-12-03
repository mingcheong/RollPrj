package gov.nbcs.rp.generateBudget.ui;

import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.generateBudget.action.GeBudgetAction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDecimalField;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

public class GeBudgetUI extends RpModulePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FSplitPane pnlBase;
	private CustomTree treeDiv = null;

	private CustomTreeFinder cf = null;
	private DataSet dsDiv;
	// private PrjAuditDTO pd = new PrjAuditDTO();
	private FDecimalField totalBudget = null;
	private FDecimalField sumBudge = null;

	CustomTree xmTree = null;

	FTextField fxmbm = null;

	FTextField fxmmc = null;

	FComboBox fqsnd = null;

	FComboBox fjsnd = null;

	FComboBox fzxzq = null;

	FComboBox fxmfl = null;

	FTreeAssistInput fxmsx = null;

	FComboBox fxmzt = null;

	FTreeAssistInput fkmxz = null;

	FListPanel bottomPanel = null;

	FTextArea fSbly = null;

	String[] kmxzListString = null;
	FSplitPane pnlInfoXm;
	private CustomTable table;
	private CustomTable table1;
	private String[] divCodes;
	private DataSet dsAll;
	private boolean flag = true;

	// private CustomTable tableAll;
	// private DataSet dsStep;

	public void initize() {
		try {
			initData();
			add(getBasePanel());
			createToolBar();
			setButtonState(true);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "初始化面板失败");
		}
	}

	private void initData() throws Exception {
	}

	public FSplitPane getBasePanel() throws Exception {
		this.pnlBase = new FSplitPane();
		pnlBase.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlBase.setDividerLocation(300);
		this.pnlBase.addControl(getLeftTopPanel());
		FPanel rPnlBase = new FPanel();
		RowPreferedLayout rp = new RowPreferedLayout(1);
		rPnlBase.setLayout(rp);
		rPnlBase.setLeftInset(10);

		FPanel labelPanel = new FPanel();
		RowPreferedLayout lab = new RowPreferedLayout(4);
		labelPanel.setLayout(lab);
		// lab.setRowGap(10);
		labelPanel.setTopInset(10);
		labelPanel.setLeftInset(10);
		labelPanel.setRightInset(10);
		labelPanel.setBottomInset(10);

		totalBudget = new FDecimalField("项目总金额：");
		totalBudget.setEditable(false);
		totalBudget.setProportion(0.25f);

		sumBudge = new FDecimalField("已生成预算金额：");
		sumBudge.setEditable(false);
		sumBudge.setProportion(0.3f);

		labelPanel.addControl(totalBudget, new TableConstraints(1, 2, false,
				false));

		labelPanel.addControl(sumBudge,
				new TableConstraints(1, 2, false, false));

		rPnlBase
				.addControl(labelPanel, new TableConstraints(3, 1, false, true));

		rPnlBase
				.addControl(getRPanel(), new TableConstraints(1, 1, true, true));
		this.pnlBase.addControl(rPnlBase);
		return this.pnlBase;
	}

	private FPanel getLeftTopPanel() {
		FPanel pnlLTop = new FPanel();
		FLabel label = new FLabel();
		label.setText("预算单位");
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlLTop.setLayout(lay);
		getDivTree();

		pnlLTop.addControl(getTreePanel(this.treeDiv), new TableConstraints(1,
				10, true, true));

		pnlLTop.setTopInset(-20);
		pnlLTop.setLeftInset(10);
		pnlLTop.setRightInset(10);
		pnlLTop.setBottomInset(10);
		return pnlLTop;
	}

	private FPanel getTreePanel(CustomTree tree) {
		FPanel pnlLeft = new FPanel();
		RowPreferedLayout leftlay = new RowPreferedLayout(1);
		pnlLeft.setLayout(leftlay);
		pnlLeft.addControl(cf, new TableConstraints(1, 1, false, true));
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		pnlLeft.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlLeft.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		return pnlLeft;
	}

	private CustomTree getDivTree() {
		try {
			this.dsDiv = GeBudgetAction.getMethod().getDivTreePop(
					Global.loginYear, 1);

			this.treeDiv = new CustomTree("单位信息表", this.dsDiv, "En_ID",
					"CODE_NAME", "PARENT_ID", null, "DIV_CODE", true);
			cf = new CustomTreeFinder(treeDiv);
			treeDiv.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {

					}
				}
			});
			this.treeDiv.setIsCheckBoxEnabled(true);
			this.treeDiv.reset();
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "创建单位树失败");
		}
		return this.treeDiv;
	}

	private FTabbedPane getRPanel() throws Exception {
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("未生成预算项目", getUnGePanel());
		tabpane.addControl("已生成预算项目", getGePanel());
		tabpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (flag) {
					flag = false;
					setButtonState(flag);
				} else {
					flag = true;
					setButtonState(flag);
				}
			}
		});
		return tabpane;
	}

	private FPanel getUnGePanel() {
		FPanel tabpane = new FPanel();
		RowPreferedLayout lay1 = new RowPreferedLayout(1);
		tabpane.setLayout(lay1);
		tabpane.setTopInset(10);
		tabpane.setLeftInset(10);
		tabpane.setRightInset(10);
		tabpane.setBottomInset(10);
		try {
			String[] columnText = { "单位编码", "单位名称", "项目编码", "项目名称", "总预算",
					"本年预算", "归口单位编码", "归口单位名称" };

			String[] columnField = { "div_code", "div_name", "prj_code",
					"prj_name", "total_money", "cur_money", "gkdw_code",
					"gkdw_name" };
			this.table = new CustomTable(columnText, columnField, null, true,
					null);
			setTableProp();
			tabpane.addControl(this.table, new TableConstraints(15, 2, true,
					true));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tabpane;
	}

	private FPanel getGePanel() {
		FPanel tabpane = new FPanel();
		try {
			RowPreferedLayout lay1 = new RowPreferedLayout(1);
			tabpane.setLayout(lay1);
			tabpane.setLayout(lay1);
			tabpane.setTopInset(10);
			tabpane.setLeftInset(10);
			tabpane.setRightInset(10);
			tabpane.setBottomInset(10);
			String[] columnText = { "单位编码", "单位名称", "项目编码", "项目名称", "总预算",
					"本年预算" };
			String[] columnField = { "div_code", "div_name", "prj_code",
					"prj_name", "total_money", "cur_money" };
			this.table1 = new CustomTable(columnText, columnField, null, true,
					null);
			setTableProp1();
			tabpane.addControl(this.table1, new TableConstraints(15, 2, true,
					true));
		} catch (Exception e) {
		}
		return tabpane;
	}

	private void setTableProp1() {
		try {

			table1.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.table1.reset();

			this.table1.getTable().getColumnModel().getColumn(1)
					.setPreferredWidth(100);
			this.table1.getTable().getColumnModel().getColumn(2)
					.setPreferredWidth(200);
			this.table1.getTable().getColumnModel().getColumn(3)
					.setPreferredWidth(100);
			this.table1.getTable().getColumnModel().getColumn(4)
					.setPreferredWidth(230);
			this.table1.getTable().getColumnModel().getColumn(5)
					.setPreferredWidth(100);
			this.table1.getTable().getColumnModel().getColumn(6)
					.setPreferredWidth(100);

			this.table1.getTable().setAutoResizeMode(0);
			this.table1.getTable().setRowHeight(20);
			this.table1.getTable().getTableHeader().setBackground(
					new Color(250, 228, 184));

			this.table1.getTable().getTableHeader().setPreferredSize(
					new Dimension(4, 30));

			this.table1.getTable().setAutoResizeMode(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setTableProp() throws Exception {
		this.table.getTable().setAutoResizeMode(0);
		this.table.reset();
		this.table.getTable().getColumnModel().getColumn(1).setPreferredWidth(
				75);
		this.table.getTable().getColumnModel().getColumn(2).setPreferredWidth(
				150);
		this.table.getTable().getColumnModel().getColumn(3).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(4).setPreferredWidth(
				200);
		this.table.getTable().getColumnModel().getColumn(5).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(6).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(7).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(8).setPreferredWidth(
				150);

		this.table.getTable().setAutoResizeMode(0);
		this.table.getTable().setRowHeight(20);
		this.table.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));

		this.table.getTable().getTableHeader().setPreferredSize(
				new Dimension(4, 30));

	}

	public void doAdd() {
		refreData();
		calDate();
	}

	private String[] getDivRecords() {
		MyTreeNode root = (MyTreeNode) this.treeDiv.getRoot();
		Enumeration enumeration = root.breadthFirstEnumeration();
		int i = 0;
		this.divCodes = new String[this.treeDiv.getSelectedNodeCount(true)];
		while (enumeration.hasMoreElements()) {
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (node.getChildCount() > 0) {
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject();
			if (pNode == null) {
				continue;
			}
			if (pNode.getIsSelect()) {
				this.divCodes[i] = pNode.getValue();
				i++;
			}
		}
		try {
			String bmk = this.dsDiv.toogleBookmark();
			this.dsDiv.maskDataChange(true);
			for (int k = 0; k < this.divCodes.length; k++) {
				if (this.dsDiv.locate("EID", this.divCodes[k])) {
					this.divCodes[k] = this.dsDiv.fieldByName("DIV_CODE")
							.getString();
				}
			}
			this.dsDiv.maskDataChange(false);
			this.dsDiv.gotoBookmark(bmk);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "获取所选单位出错");
		}
		return this.divCodes;
	}

	public void doSave() {// 生成预算项目
		int[] rows = this.table.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		try {
			// Map mDivCode = new HashMap();
			// Map mPrjCode = new HashMap();
			// Map mPrjName = new HashMap();

			List lBudgetData = new ArrayList();
			float sums = 0;
			for (int i = 0; i < rows.length; i++) {
				Object check = this.table.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table.getDataSet().gotoBookmark(
								this.table.rowToBookmark(rows[i]))))
					continue;
				String sBudgetData = "";

				if (table.getDataSet().fieldByName("div_code").getValue() == null)
					continue;
				sums += this.table.getDataSet().fieldByName("cur_money")
						.getFloat();

				sBudgetData = this.table.getDataSet().fieldByName("div_code")
						.getString()
						+ ","
						+ this.table.getDataSet().fieldByName("prj_code")
								.getString()
						+ ","
						+ this.table.getDataSet().fieldByName("prj_name")
								.getString()
						+ ","
						+ this.table.getDataSet().fieldByName("gkdw_code")
								.getString();
				lBudgetData.add(sBudgetData);
			}

			try {
				if (JOptionPane.showConfirmDialog(Global.mainFrame,
						"您确定生成预算金额 " + sums + "万元 ?", "提示",
						JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
					return;
				String sum = GeBudgetAction.getMethod().insertTempBudget(
						Global.loginYear, "2", "1", lBudgetData);

				JOptionPane.showMessageDialog(Global.mainFrame, "生成预算" + sum
						+ "成功！");

			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame, "生成预算失败！");
			}
			// GeQrChoose chs = new GeQrChoose(lBudgetData);
			// Tools.centerWindow(chs);
			// chs.setVisible(true);

			this.divCodes = getDivRecords();

			refreData();
			calDate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.mainFrame, "生成预算失败");
			e.printStackTrace();
		}
	}

	public void doImpProject() {
		int[] rows = this.table1.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		try {
			String whereSql = " and ";
			for (int i = rows.length - 1; i >= 0; i--) {
				Object check = this.table1.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table1.getDataSet().gotoBookmark(
								this.table1.rowToBookmark(rows[i]))))
					continue;
				String batch_no = "";
				String dataType = "";
				if (this.table1.getDataSet().fieldByName("batch_no").getValue()
						.toString().equals("一上")) {
					batch_no = "1";
				}
				if (this.table1.getDataSet().fieldByName("batch_no").getValue()
						.toString().equals("二上")) {
					batch_no = "2";
				}
				if (this.table1.getDataSet().fieldByName("data_type")
						.getValue().toString().equals("财政审核数")) {
					dataType = "1";
				}
				if (this.table1.getDataSet().fieldByName("data_type")
						.getValue().toString().equals("原始数")) {
					dataType = "0";
				}
				whereSql = whereSql
						+ "  prj_code ='"
						+ this.table1.getDataSet().fieldByName("PRJ_CODE")
								.getValue().toString() + "'"
						+ " and batch_no='" + batch_no + "'"
						+ " and data_type='" + dataType + "'";
				whereSql = whereSql + "or ";
			}
			whereSql = whereSql.substring(0, whereSql.length() - 3);
			GeBudgetAction.getMethod().cancleBudget(Global.loginYear, whereSql);

			refreData();
			calDate();
			JOptionPane.showMessageDialog(Global.mainFrame, "撤销预算成功");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "撤销预算失败");

		}
	}

	private void setButtonState(boolean isEdit) {
		if (this.getToolbarPanel() == null) {
			return;
		}
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btnGet = (FButton) controls.get(i);
			if ("生成预算".equals(btnGet.getText())) {
				btnGet.setEnabled(isEdit);
			}
			if ("撤销预算".equals(btnGet.getText())) {
				btnGet.setEnabled(!isEdit);
			}
		}
	}

	private void calDate() {
		try {
			BigDecimal sum = new BigDecimal("0");
			BigDecimal cur = new BigDecimal("0");
			BigDecimal sum1 = new BigDecimal("0");
			BigDecimal cur1 = new BigDecimal("0");
			DataSet dataSet = table.getDataSet();
			dataSet.beforeFirst();
			while (dataSet.next()) {

				sum1 = new BigDecimal(Double.parseDouble(dataSet.fieldByName(
						"total_money").getValue().toString()));
				sum = sum.add(sum1);
			}
			sum = sum.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			totalBudget.setValue(String.valueOf(sum));

			dataSet = table1.getDataSet();
			dataSet.beforeFirst();
			while (dataSet.next()) {
				cur1 = new BigDecimal(Double.parseDouble(dataSet.fieldByName(
						"cur_money").getValue().toString()));
				cur = cur.add(cur1);
			}
			cur = cur.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			sumBudge.setValue(String.valueOf(cur));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void refreData() {
		try {
			String queryType = "0";
			this.divCodes = getDivRecords();
			MyTreeNode nodeSel = treeDiv.getSelectedNode();
			if (nodeSel != null) {
				DataSet ds = treeDiv.getDataSet();// 获取预算单位
				if (treeDiv.getSelectedNode() != treeDiv.getRoot()) {
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof()) {
						queryType = "1";
					}
				}
			}
			this.dsAll = GeBudgetAction.getMethod().findProjectByPara(
					this.divCodes, Global.loginYear, queryType);

			this.table.setDataSet(this.dsAll);
			this.table.reset();
			setTableProp();

			String filter = " ";
			// if (batch.getValue() != null) {
			// filter = filter + " and a.batch_no ='"
			// + batch.getValue().toString() + "'";
			// }
			// if (dataTypeBox.getValue() != null) {
			// filter = filter + "and a.data_type = '"
			// + dataTypeBox.getValue().toString() + "'";
			// }
			this.dsAll = GeBudgetAction.getMethod().findDoProjectByPara(
					this.divCodes, Global.loginYear, filter, queryType);

			this.table1.setDataSet(this.dsAll);
			this.table1.reset();
			setTableProp1();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "查询出错");
		}
	}

	public void doModify() {// 撤销已生成项目
		int[] rows = this.table1.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		if (JOptionPane.showConfirmDialog(Global.mainFrame, "您确定撤销已生成预算项目！",
				"提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
			return;
		try {
			String whereSql = " and ";
			for (int i = rows.length - 1; i >= 0; i--) {
				Object check = this.table1.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table1.getDataSet().gotoBookmark(
								this.table1.rowToBookmark(rows[i]))))
					continue;
				// String batch_no = "";
				// String dataType = "";
				// if
				// (this.table1.getDataSet().fieldByName("batch_no").getValue()
				// .toString().equals("一上")) {
				// batch_no = "1";
				// }
				// if
				// (this.table1.getDataSet().fieldByName("batch_no").getValue()
				// .toString().equals("二上")) {
				// batch_no = "2";
				// }
				// if (this.table1.getDataSet().fieldByName("data_type")
				// .getValue().toString().equals("财政审核数")) {
				// dataType = "1";
				// }
				// if (this.table1.getDataSet().fieldByName("data_type")
				// .getValue().toString().equals("原始数")) {
				// dataType = "0";
				// }
				whereSql = whereSql
						+ "  prj_code ='"
						+ this.table1.getDataSet().fieldByName("PRJ_CODE")
								.getValue().toString() + "'";
				// + " and batch_no='" + batch_no + "'"
				// + " and data_type='" + dataType + "'";
				whereSql = whereSql + "or ";
			}
			whereSql = whereSql.substring(0, whereSql.length() - 3);
			GeBudgetAction.getMethod().cancleBudget(Global.loginYear, whereSql);

			refreData();
			calDate();
			JOptionPane.showMessageDialog(Global.mainFrame, "撤销预算成功");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "撤销预算失败");

		}
	}

	public void doCancel() {
		try {
			int[] rows = this.table.getTable().getSelectedRows();
			if (rows.length == 0) {
				JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
				return;
			}
			List lBudgetData = new ArrayList();

			for (int i = 0; i < rows.length; i++) {
				Object check = this.table.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table.getDataSet().gotoBookmark(
								this.table.rowToBookmark(rows[i]))))
					continue;
				XMLData xmlData = new XMLData();
				xmlData.put("div_code", this.table.getDataSet().fieldByName(
						"div_code").getString());
				xmlData.put("prj_code", this.table.getDataSet().fieldByName(
						"prj_code").getString());
				xmlData.put("prj_name", this.table.getDataSet().fieldByName(
						"prj_name").getString());
				lBudgetData.add(xmlData);
			}
			GeSetDivToDiv gdt = new GeSetDivToDiv(lBudgetData);
			Tools.centerWindow(gdt);
			gdt.setVisible(true);
			refreData();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}