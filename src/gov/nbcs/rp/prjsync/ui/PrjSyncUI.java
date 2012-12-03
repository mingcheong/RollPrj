/**
 * @Copyright 浙江易桥版权所有
 * 
 * @ProjectName 宁海财政扶持项目系统
 * 
 * @aouthor 陈宪标
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.ui;

import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.prjsync.stub.PrjSyncStub;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDecimalField;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;




/**
 * @author 毛建亮
 * 
 * @version 创建时间：2012-6-7 上午10:47:12
 * 
 * @Description 项目同步界面
 */
public class PrjSyncUI extends RpModulePanel
{

	private static final long serialVersionUID = 1L;
	private FSplitPane pnlBase;
	private CustomTree treeEn = null;
	private CustomTreeFinder cf = null;

	private String[] divCodes;

	private DataSet dsDiv;
	private FTabbedPane tabpane = null;

	FComboBox cbYear = null; // 可选年度

	FListPanel bottomPanel = null;

	private String selectYear = GlobalEx.loginYear;

	FTextArea fSbly = null;

	DataSet treeds = null;

	String[] kmxzListString = null;
	FSplitPane pnlInfoXm;
	private CustomTable table;

	private FDecimalField totalMoney = null;

	private boolean flag = true;



	/*
	 * 界面初始化
	 */
	public void initize()
	{
		try
		{
			this.createToolBar();
			add(getBasePanel());
			setButtonStateAn(true);
			// refreshPrjData();
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "生成预算界面初始化失败");
		}

	}


	public FSplitPane getBasePanel() throws Exception
	{
		this.pnlBase = new FSplitPane();
		pnlBase.setOrientation(FSplitPane.HORIZONTAL_SPLIT);// 左右分割
		this.pnlBase.setDividerLocation(300); // 设置树形面板宽度位置为300px

		this.pnlBase.addControl(getLeftTopPanel()); // 加载左边面板
		this.pnlBase.addControl(getRPanel()); // 加载右边面板
		// refreshPrjData();// 刷新列表
		return this.pnlBase;
	}


	/**
	 * 生成左边的panel 放置树
	 */
	private FPanel getLeftTopPanel()
	{
		FPanel pnlLTop = new FPanel();
		FLabel label = new FLabel();
		label.setText("预算单位");
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlLTop.setLayout(lay);
		getDivTree();
		pnlLTop.addControl(getTreePanel(this.treeEn), new TableConstraints(10, 1, true, true));

		pnlLTop.setTopInset(-20);
		pnlLTop.setLeftInset(10);
		pnlLTop.setRightInset(10);
		pnlLTop.setBottomInset(10);
		return pnlLTop;
	}


	private FPanel getTreePanel(CustomTree tree)
	{
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


	private CustomTree getDivTree()
	{
		try
		{
			this.treeEn = new CustomTree("单位信息表", treeds, "En_ID", "CODE_NAME", "PARENT_ID", null, "DIV_CODE");
			cf = new CustomTreeFinder(treeEn);
			refreshTreeData();
			treeEn.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 1)
					{}
				}
			});
			treeEn.setIsCheckBoxEnabled(true);
			treeEn.setIsCheck(true);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "创建单位树失败");
		}
		return this.treeEn;
	}


	// 刷新树
	private void refreshTreeData()
	{
		try
		{
			treeds = PrjSyncStub.getMethod().getDivDataPop(selectYear);
			treeEn.setDataSet(treeds);
			treeEn.reset();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "刷新预算单位出错！");
			return;
		}
	}


	// 刷新表格数据
	private String refreshPrjData()
	{
		try
		{
			StringBuffer cond = new StringBuffer();
			cond.append(" and set_year = " + selectYear);
			cond.append(" and rg_code = " + Global.getCurrRegion());
			cond.append(" and ");
			String queryType = "0";
			MyTreeNode nodeSel = treeEn.getSelectedNode();
			if (nodeSel != null)
			{
				DataSet ds = treeEn.getDataSet();// 获取预算单位
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						queryType = "1";
						this.divCodes = getDivRecords();
					}
				}
			}

			DataSet data = null;
			// switch (this.tabpane.getSelectedIndex()) {
			// case 0:
			// // 未同步
			data = PrjSyncStub.getMethod().getOldPrjSync(divCodes, selectYear, Global.getCurrRegion(), queryType);

			table.setDataSet(data);// 查询项目信息
			table.reset();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}


	/**
	 * 加载右边面板
	 */
	private FPanel getRPanel()
	{
		FPanel pnlInfo = new FPanel();

		RowPreferedLayout layCF = new RowPreferedLayout(2);
		pnlInfo.setLayout(layCF);
		pnlInfo.setTopInset(10);
		pnlInfo.setLeftInset(10);
		pnlInfo.setRightInset(10);
		pnlInfo.setBottomInset(10);

		totalMoney = new FDecimalField("未同步项目总金额(万元)：");
		totalMoney.setEditable(false);
		totalMoney.setProportion(0.3f);

		// sumMoney = new FDecimalField("已同步项目总金额(元)：");
		// sumMoney.setEditable(false);
		// sumMoney.setProportion(0.3f);

		pnlInfo.addControl(totalMoney, new TableConstraints(1, 1, false, true));
		// pnlInfo.addControl(sumMoney, new TableConstraints(1, 1, false,
		// true));

		tabpane = new FTabbedPane();
		// RowPreferedLayout topdivlay = new RowPreferedLayout(1);
		// tabpane.setLayout(topdivlay);

		tabpane.addControl("未同步项目", getUsynPanle());
		// tabpane.addControl("已同步项目", getsynPanle());

		tabpane.addChangeListener(new ChangeListener()
		{

			public void stateChanged(ChangeEvent e)
			{
				if (flag)
				{
					flag = false;
					setButtonStateAn(flag);

				}
				else
				{
					flag = true;
					setButtonStateAn(flag);

				}

			}
		});

		pnlInfo.addControl(tabpane, new TableConstraints(1, 2, true, true));
		return pnlInfo;
	}


	private FPanel getUsynPanle()
	{
		FPanel synPanel = new FPanel();
		try
		{

			RowPreferedLayout synLay = new RowPreferedLayout(1);
			synPanel.setLayout(synLay);
			String[] columnText = { "单位编码", "单位名称", "项目编码", "项目名称", "文号", "指标来源", "指标" };
			String[] columnField = { "en_code", "en_name", "bis_code", "bis_name", "file_name", "bl_name", "budget_money" };
			this.table = new CustomTable(columnText, columnField, null, true, null);

			setTableProp();
			// synPanel.addControl(getQueryType(), new TableConstraints(1, 1,
			// false, true));
			synPanel.addControl(this.table, new TableConstraints(1, 1, true, true));

		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "生成项目列表失败");
		}
		return synPanel;
	}


	private void setTableProp() throws Exception
	{
		// this.table.getTable().setAutoResizeMode(0);
		this.table.reset();
		this.table.getTable().getColumnModel().getColumn(0).setPreferredWidth(30);
		this.table.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		this.table.getTable().getColumnModel().getColumn(2).setPreferredWidth(50);
		this.table.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		this.table.getTable().getColumnModel().getColumn(4).setPreferredWidth(100);
		this.table.getTable().getColumnModel().getColumn(5).setPreferredWidth(80);

		// this.table.getTable().setAutoResizeMode(0);
		this.table.getTable().setRowHeight(20);
		// this.table.setSize(2000, 2000);
		this.table.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
	}


	// 按钮状态
	private void setButtonStateAn(boolean isEdit)
	{
		if (this.getToolbarPanel() == null) { return; }
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("录入指标同步".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEdit);
			}
			if ("撤销指标同步".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEdit);
			}
			if ("设置关联单位".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEdit);
			}
		}
	}


	/**
	 * 同步操作
	 */
	public void doSync()
	{
		int[] rows = this.table.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		String result = "";
		ArrayList enid = new ArrayList();
		ArrayList bisname = new ArrayList();

		List syncList = new ArrayList(rows.length);
		String[] prjDs = null;
		Map prjMap = null;
		try
		{
			String[] bmks = getBMKs(rows, table);
			DataSet ds = table.getDataSet();
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "您确定同步" + rows.length + "条项目吗?", "提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;

			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					String en_id = ds.fieldByName("en_id").getValue().toString(); // 单位id
					String bis_name = this.table.getDataSet().fieldByName("bis_name").getString();// 项目名称
					String money = ds.fieldByName("budget_money").getValue().toString(); // 单位id
					result = PrjSyncStub.getMethod().isXmjl(en_id, bis_name, money);

					if ("ok".equals(result))
					{
						enid.add(en_id);
						bisname.add(bis_name);
					}

					prjMap = new HashMap();
					prjMap.put("en_id", ds.fieldByName("en_id").getString());
					prjMap.put("en_code", ds.fieldByName("en_code").getString());
					prjMap.put("en_name", ds.fieldByName("en_name").getString());
					prjMap.put("bs_id", ds.fieldByName("bs_id").getString());
					prjMap.put("bs_code", ds.fieldByName("bs_code").getString());
					prjMap.put("bs_name", ds.fieldByName("bs_name").getString());
					prjMap.put("bl_name", ds.fieldByName("bl_name").getString());
					prjMap.put("bis_name", ds.fieldByName("bis_name").getString());
					prjMap.put("budget_money", ds.fieldByName("budget_money").getString());
					syncList.add(prjMap);
				}
			}
			// result = PrjSyncStub.getMethod().prjSync(enid, bisname);
			result = PrjSyncStub.getMethod().makeSynchronous(syncList);
			JOptionPane.showMessageDialog(Global.mainFrame, result);
			refreshPrjData();// 重新刷新数据
			calData();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, e.getMessage());
		}
	}


	private String[] getBMKs(int[] rows, CustomTable table)
	{
		String[] bmks = new String[rows.length];
		for (int i = 0; i < rows.length; i++)
		{
			bmks[i] = table.rowToBookmark(rows[i]);
		}
		return bmks;
	}


	public void doAdd()
	{
		refreshPrjData();
		calData();
	}


	private void calData()
	{
		try
		{
			BigDecimal sum = new BigDecimal("0");
			// BigDecimal cur = new BigDecimal("0");
			BigDecimal sum1 = new BigDecimal("0");
			// BigDecimal cur1 = new BigDecimal("0");
			DataSet dataSet = table.getDataSet();
			dataSet.beforeFirst();
			while (dataSet.next())
			{

				sum1 = new BigDecimal(Double.parseDouble(dataSet.fieldByName("budget_money").getValue().toString()));
				sum = sum.add(sum1);
			}
			sum = sum.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			totalMoney.setValue(String.valueOf(sum));
			// dataSet = table1.getDataSet();
			// dataSet.beforeFirst();
			// while (dataSet.next())
			// {
			// cur1 = new
			// BigDecimal(Double.parseDouble(dataSet.fieldByName("budget_money").getValue().toString()));
			// cur = cur.add(cur1);
			// }
			// cur = cur.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			// sumMoney.setValue(String.valueOf(cur));

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}


	private String[] getDivRecords()
	{
		MyTreeNode root = (MyTreeNode) this.treeEn.getRoot();
		Enumeration enumeration = root.breadthFirstEnumeration();
		int i = 0;
		this.divCodes = new String[this.treeEn.getSelectedNodeCount(true)];
		while (enumeration.hasMoreElements())
		{
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (node.getChildCount() > 0)
			{
				continue;
			}
			PfTreeNode pNode = (PfTreeNode) node.getUserObject();
			if (pNode == null)
			{
				continue;
			}
			if (pNode.getIsSelect())
			{
				this.divCodes[i] = pNode.getValue();
				i++;
			}
		}
		try
		{
			this.dsDiv = treeEn.getDataSet();
			String bmk = this.dsDiv.toogleBookmark();
			this.dsDiv.maskDataChange(true);
			for (int k = 0; k < this.divCodes.length; k++)
			{
				if (this.dsDiv.locate("EID", this.divCodes[k]))
				{
					this.divCodes[k] = this.dsDiv.fieldByName("DIV_CODE").getString();
				}
			}
			this.dsDiv.maskDataChange(false);
			this.dsDiv.gotoBookmark(bmk);
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "获取所选单位出错");
		}
		return this.divCodes;
	}


	public void doModify()
	{
		try
		{
			int[] rows = this.table.getTable().getSelectedRows();
			if (rows.length == 0)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "请选择将合并的项目！");
				return;
			}

			if (rows.length < 2)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "合并的项目必需选择2条以上！");
				return;
			}

			List prjNameList = new ArrayList();
			for (int i = 0; i < rows.length; i++)
			{
				Object check = this.table.getTable().getValueAt(rows[i], 0);
				if ((!((Boolean) check).booleanValue()) || (!this.table.getDataSet().gotoBookmark(this.table.rowToBookmark(rows[i]))))
					continue;
				prjNameList.add(table.getDataSet().fieldByName("bis_name").getString());
			}
			PrjSyncDialog psd = new PrjSyncDialog(prjNameList);
			Tools.centerWindow(psd);
			psd.setVisible(true);

			double budgetMoney = 0d;
			if (StringUtils.isNotEmpty(psd.getReturnVal()))
			{
				for (int i = rows.length - 1; i >= 0; i--)
				{
					Object check = this.table.getTable().getValueAt(rows[i], 0);
					if ((!((Boolean) check).booleanValue()) || (!this.table.getDataSet().gotoBookmark(this.table.rowToBookmark(rows[i]))))
						continue;
					budgetMoney += table.getDataSet().fieldByName("budget_money").getDouble();

					if (i > 0)
						table.getDataSet().delete();

					if (i == 0)
					{
						table.getDataSet().fieldByName("budget_money").setValue(new Double(budgetMoney));
						table.getDataSet().fieldByName("bis_name").setValue(psd.getReturnVal());
					}
				}
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e.getMessage());
		}
	}


	public void doImpProject()
	{
		// TODO Auto-generated method stub

	}

}
