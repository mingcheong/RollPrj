package gov.nbcs.rp.query.ui;
/**
 * @author 谢昀荪
 *
 * @version 创建时间：Jul 16, 20129:32:48 AM
 *
 * @Description
 */

import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.query.action.QrBudgetAction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

public class PrjContinue extends RpModulePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FSplitPane pnlBase;
	private CustomTree treeDiv = null;
	private DataSet dsDiv;
	private PrjAuditDTO pd = new PrjAuditDTO();

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
	private String[] divCodes;
	private DataSet dsAll;
	private CustomTable tableAll;
	private DataSet dsStep;
	private FButton btnE;

	public void initize() {
		try {
			initData();
			add(getBasePanel());
			createToolBar();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "初始化面板失败");
		}
	}

	private void initData() throws Exception {
	}

	public FSplitPane getBasePanel() throws Exception {
		this.pnlBase = new FSplitPane();
		this.pnlBase.setDividerLocation(200);
		this.pnlBase.addControl(getLeftTopPanel());
		this.pnlBase.addControl(getRPanel());
		return this.pnlBase;
	}

	private FPanel getLeftTopPanel() {
		FPanel pnlLTop = new FPanel();
		FLabel label = new FLabel();
		label.setText("预算单位");
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlLTop.setLayout(lay);
		getDivTree();

		pnlLTop.addControl(getTreePanel(this.treeDiv), new TableConstraints(10,
				1, true, true));

		pnlLTop.setTopInset(10);
		pnlLTop.setLeftInset(10);
		pnlLTop.setRightInset(10);
		pnlLTop.setBottomInset(10);
		return pnlLTop;
	}

	private FScrollPane getTreePanel(CustomTree tree) {
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		return pnlTree;
	}

	private CustomTree getDivTree() {
		try {
			this.dsDiv = QrBudgetAction.getMethod().getDivDataPop(
					Global.loginYear, this.pd.getBathcNo());

			this.treeDiv = new CustomTree("单位信息表", this.dsDiv, "En_ID",
					"CODE_NAME", "PARENT_ID", null, "DIV_CODE", true);

			this.treeDiv.setIsCheckBoxEnabled(true);
			this.treeDiv.reset();
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "创建单位树失败");
		}
		return this.treeDiv;
	}

	private FPanel getRPanel() {
		FPanel pnlInfo = new FPanel();

		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlInfo.setLayout(layCF);

		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);
		this.fxmbm = new FTextField("项目编码");
		this.fxmmc = new FTextField("项目名称");
		this.fqsnd = new FComboBox("起始年度");
		String qsndTmp = "";
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		for (int i = 0; i <= 5; i++) {
			qsndTmp = qsndTmp + (numQs + i) + "#" + (numQs + i) + "+";
		}
		if (qsndTmp.length() > 0) {
			qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
		}
		this.fqsnd.setRefModel(qsndTmp);
		this.fqsnd.setValue("");

		this.fjsnd = new FComboBox("结束年度");
		String jsndTmp = "";
		int num = Integer.parseInt(GlobalEx.loginYear);
		for (int i = 0; i < 10; i++) {
			jsndTmp = jsndTmp + (num + i) + "#" + (num + i) + "+";
		}

		if (jsndTmp.length() > 0) {
			jsndTmp = jsndTmp.substring(0, jsndTmp.length() - 1);
		}
		this.fjsnd.setRefModel(jsndTmp);
		this.fjsnd.setValue("");
		try {
			List zxzqList = PrjInputStub.getMethod().getDmZxzq(
					Global.loginYear, Global.getCurrRegion());
			String zxzqTmp = "";
			for (int i = 0; i < zxzqList.size(); i++) {
				Map m = new HashMap();
				m = (Map) zxzqList.get(i);
				zxzqTmp = zxzqTmp + m.get("chr_code") + "#" + m.get("chr_name")
						+ "+";
			}
			if (zxzqTmp.length() > 0) {
				zxzqTmp = zxzqTmp.substring(0, zxzqTmp.length() - 1);
			}
			this.fzxzq = new FComboBox("执行周期");
			this.fzxzq.setRefModel(zxzqTmp);
			this.fzxzq.setValue("001");
			this.fzxzq.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent e) {
					PrjContinue.this.doAdd();
				}
			});
			List xmflList = PrjInputStub.getMethod().getDmXmfl(
					Global.loginYear, Global.getCurrRegion());
			String xmflTmp = "";
			for (int i = 0; i < xmflList.size(); i++) {
				Map m = new HashMap();
				m = (Map) xmflList.get(i);
				xmflTmp = xmflTmp + m.get("chr_code") + "#" + m.get("chr_name")
						+ "+";
			}
			if (xmflTmp.length() > 0) {
				xmflTmp = xmflTmp.substring(0, xmflTmp.length() - 1);
			}
			this.fxmfl = new FComboBox("项目类别");
			this.fxmfl.setRefModel(xmflTmp);
			this.fxmfl.setValue("");

			List xmsxList = PrjInputStub.getMethod().getDmXmsx(
					Global.loginYear, Global.getCurrRegion());
			this.fxmsx = new FTreeAssistInput("项目属性");
			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			this.fxmsx.setData(treeData);

			List xmztList = PrjInputStub.getMethod().getDmXmzt(
					Global.loginYear, Global.getCurrRegion());
			String xmztTmp = "";
			for (int i = 0; i < xmztList.size(); i++) {
				Map m = new HashMap();
				m = (Map) xmztList.get(i);
				xmztTmp = xmztTmp + m.get("chr_code") + "#" + m.get("chr_name")
						+ "+";
			}
			if (xmztTmp.length() > 0) {
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}
			this.fxmzt = new FComboBox("项目状态");
			this.fxmzt.setRefModel(xmztTmp);
			this.fxmzt.setValue("");

			List kmxzList = PrjInputStub.getMethod().getDmKmxz(
					Global.loginYear, Global.getCurrRegion());
			this.fkmxz = new FTreeAssistInput("科目选择");
			this.fkmxz.setProportion(0.082F);
			XMLData treeDataKm = new XMLData();
			treeDataKm.put("row", kmxzList);
			this.fkmxz.setData(treeDataKm);
			this.fkmxz.setIsCheck(true);
			this.btnE = new FButton("ele", "重置");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.btnE = new FButton("ele", "重  置");
		this.btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PrjContinue.this.setTableProp();
					PrjContinue.this.fxmbm.setValue("");
					PrjContinue.this.fxmmc.setValue("");
					PrjContinue.this.fzxzq.setValue("001");

					PrjContinue.this.fqsnd.setValue("");
					PrjContinue.this.fjsnd.setValue("");
					PrjContinue.this.fxmfl.setValue("");
					PrjContinue.this.fxmsx.setValue("");
					PrjContinue.this.fxmzt.setValue("");
					PrjContinue.this.fkmxz.setValue("");
				} catch (Exception ee) {
					ErrorInfo.showErrorDialog(ee, "取消失败");
				}
			}
		});
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.setTopInset(10);

		xmxx.addControl(this.fxmbm, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fxmmc, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fqsnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fjsnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fzxzq, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fxmfl, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fxmzt, new TableConstraints(1, 2, false, true));
		xmxx.addControl(this.fkmxz, new TableConstraints(1, 7, false, true));
		xmxx.addControl(this.btnE, new TableConstraints(1, 1, false, true));

		this.pnlInfoXm = new FSplitPane();
		this.pnlInfoXm.setOrientation(0);
		this.pnlInfoXm.setDividerLocation(100);
		this.pnlInfoXm.addControl(xmxx);

		pnlInfo.addControl(this.pnlInfoXm);

		FPanel tabpane = new FPanel();
		try {
			RowPreferedLayout lay1 = new RowPreferedLayout(1);
			tabpane.setLayout(lay1);
			String[] columnText = { "单位编码", "单位名称", "项目编码", "项目名称", "总预算",
					"本年预算", "往年支出" };

			String[] columnField = { "div_code", "div_name", "xmbm", "xmmc",
					"total_money", "cur_money", "before_money" };

			this.table = new CustomTable(columnText, columnField, null, true,
					null);
			setTableProp();
			tabpane.addControl(this.table, new TableConstraints(15, 2, true,
					true));
			this.table.getTable().addMouseListener(new MouseAdapter() {
				private static final long serialVersionUID = 1L;

				public void mouseClicked(MouseEvent e) {
					if ((e.getButton() != 2) && (e.getButton() != 3)) {
						try {
							if (PrjContinue.this.dsAll
									.gotoBookmark(PrjContinue.this.table
											.rowToBookmark(PrjContinue.this.table
													.getTable()
													.getSelectedRow()))) {
								PrjContinue.this.fxmbm
										.setValue(PrjContinue.this.dsAll
												.fieldByName("xmbm")
												.getString());

								PrjContinue.this.fxmmc
										.setValue(PrjContinue.this.dsAll
												.fieldByName("xmmc")
												.getString());

								PrjContinue.this.fqsnd
										.setValue(PrjContinue.this.dsAll
												.fieldByName("qsnd")
												.getString());

								PrjContinue.this.fjsnd
										.setValue(PrjContinue.this.dsAll
												.fieldByName("jsnd")
												.getString());

								PrjContinue.this.fzxzq
										.setValue(PrjContinue.this.dsAll
												.fieldByName("zxzq")
												.getString());

								PrjContinue.this.fxmfl
										.setValue(PrjContinue.this.dsAll
												.fieldByName("xmfl")
												.getString());

								PrjContinue.this.fxmsx
										.setValue(PrjContinue.this.dsAll
												.fieldByName("xmsx")
												.getString());

								PrjContinue.this.fxmzt
										.setValue(PrjContinue.this.dsAll
												.fieldByName("xmzt")
												.getString());

								String xmxh = PrjContinue.this.dsAll
										.fieldByName("xmxh").getString();

								PrjContinue.this.fkmxz.setText(QrBudgetAction
										.getMethod().findkmdm(xmxh));
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

				}

			});
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "生成项目列表失败");
		}

		this.pnlInfoXm.addControl(tabpane);

		return pnlInfo;
	}

	private void setTableProp() throws Exception {
		this.table.getTable().setAutoResizeMode(0);
		this.table.reset();

		this.table.getTable().getColumnModel().getColumn(0).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(1).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(2).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(3).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(4).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(5).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(6).setPreferredWidth(
				100);
		this.table.getTable().getColumnModel().getColumn(7).setPreferredWidth(
				100);

		this.table.getTable().setAutoResizeMode(0);
		this.table.getTable().setRowHeight(30);
		this.table.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));

		this.table.getTable().getTableHeader().setPreferredSize(
				new Dimension(4, 30));
	}

	public void doAdd() {
		try {
			this.divCodes = getDivRecords();
			if ((this.divCodes == null) || (this.divCodes.length == 0)) {
				JOptionPane.showMessageDialog(Global.mainFrame, "请选择要查询单位");
				return;
			}

			String str1 = String.valueOf(this.fxmbm.getValue());
			String str2 = String.valueOf(this.fxmmc.getValue());
			String str3 = String.valueOf(this.fqsnd.getValue());
			String str4 = String.valueOf(this.fjsnd.getValue());
			String str5 = String.valueOf(this.fzxzq.getValue());
			String str6 = String.valueOf(this.fxmfl.getValue());
			String str7 = String.valueOf(this.fxmsx.getValue());

			String str8 = String.valueOf(this.fxmzt.getValue());
			String[] kmxz = (String[]) this.fkmxz.getValue();
			Map parMap = new HashMap();
			parMap.put("fxmbm", str1);
			parMap.put("fqsnd", str3);
			parMap.put("fjsnd", str4);
			parMap.put("fzxzq", str5);
			parMap.put("fxmfl", str6);
			parMap.put("fxmsx", str7);
			parMap.put("fxmzt", str8);

			this.dsAll = QrBudgetAction.getMethod().findProjectByPara(
					this.divCodes, parMap, kmxz, Global.loginYear);

			this.table.setDataSet(this.dsAll);
			setTableProp();
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "查询出错");
		}
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

	public void doSave() {
		int[] rows = this.table.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		try {
			String xmbms = "";
			String xmxhs = "";
			List prjCode = new ArrayList();
			for (int i = 0; i < rows.length; i++) {
				Object check = this.table.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table.getDataSet().gotoBookmark(
								this.table.rowToBookmark(rows[i]))))
					continue;
				String xmbm = this.table.getDataSet().fieldByName("xmbm")
						.getString();

				String xmxh = this.table.getDataSet().fieldByName("xmxh")
						.getString();

				xmbms = xmbms + "'" + xmbm + "',";
				xmxhs = xmxhs + "'" + xmxh + "',";
				prjCode.add(xmbm);
			}

			if (xmbms.length() > 0) {
				xmbms = xmbms.substring(0, xmbms.length() - 1);
			}
			if (xmxhs.length() > 0) {
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}

			QrScronyChoose chs = new QrScronyChoose(xmbms, xmxhs, prjCode);
			Tools.centerWindow(chs);

			chs.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.mainFrame, "同步失败");
			e.printStackTrace();
		}
	}

	public void doImpProject() {
		int[] rows = this.table.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		try {
			String xmbms = "";
			String[] xmxhs = new String[rows.length];

			for (int i = 0; i < rows.length; i++) {
				Object check = this.table.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue())
						|| (!this.table.getDataSet().gotoBookmark(
								this.table.rowToBookmark(rows[i]))))
					continue;
				xmxhs[i] = this.table.getDataSet().fieldByName("xmxh")
						.getString();

				String xmbm = this.table.getDataSet().fieldByName("xmbm")
						.getString();

				xmbms = xmbms + "'" + xmbm + "',";
			}

			if (xmbms.length() > 0) {
				xmbms = xmbms.substring(0, xmbms.length() - 1);
			}
			if (("".equals(this.fzxzq.getValue().toString()))
					|| (this.fzxzq.getValue().toString() == null)) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"请选择执行周期--查询后对应进行滚动");
				return;
			}

			if ("001".equals(this.fzxzq.getValue().toString())) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"一次性项目不能进行滚动进行滚动");
				return;
			}
			if ("002".equals(this.fzxzq.getValue().toString())) {
				QrBudgetAction.getMethod().gunDongNextyearLXXM(xmbms, xmxhs,
						Global.user_code, Global.loginYear,
						GlobalEx.getCurrRegion());
			} else {
				QrBudgetAction.getMethod().gunDongNextyear(xmbms, xmxhs,
						Global.user_code, Global.loginYear,
						GlobalEx.getCurrRegion());
			}
			JOptionPane.showMessageDialog(Global.mainFrame, "滚动成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Global.mainFrame, "滚动失败");
			e.printStackTrace();
		}
	}
}