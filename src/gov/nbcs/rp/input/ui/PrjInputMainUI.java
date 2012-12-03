package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import mappingfiles.sysdb.RpXmjl;
import mappingfiles.sysdb.RpXmjlKm;
import mappingfiles.sysdb.RpXmsb;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




public class PrjInputMainUI extends RpModulePanel implements PrjActionUI
{
	protected AcctSelectDialog acctSelect = new AcctSelectDialog();
	private static final long serialVersionUID = 1L;

	/** -1 浏览状态 0:增加 1：修改 */
	private int state = -1;

	CustomTree treeEn = null;

	CustomTable tbPrj = null;

	DataSet dsPrj = null;

	/** 项目树 */
	CustomTree treePrj = null;

	/** 项目编码 */
	FTextField tfPrjCode = null; // 项目编码

	/** 项目名称 */
	FTextField tfPrjName = null; // 项目名称

	/** 起始年度 */
	FComboBox cbYearBegin = null; // 起始年度

	/** 结束年度 */
	FComboBox cbYearEnd = null; // 结束年度

	/** 执行周期 */
	FComboBox cbPrjCir = null; // 执行周期

	/** 项目分类 */
	FComboBox cbPrjSort = null; // 项目分类

	/** 项目属性 */
	FTreeAssistInput fxmsx = null;// 项目属性

	/** 项目状态 */
	FComboBox cbPrjState = null; // 项目状态

	/** 科目选择 */
	FTextField tfAcct = null; // 科目选择

	/** 功能科目选择按钮 */
	FButton btnAcct;

	/** 选择返回的功能科目编码 */
	private String[] sAcctCodes;

	/** 选择返回的功能科目名称 */
	private String[] sAcctNames;

	/** 选择返回的功能流水码 */
	private String[] sAcctBsIDs;

	/** 项目状态 */
	private List xmztList;

	/** 项目分类 */
	private List xmflList;

	private PrjInputDTO dto = new PrjInputDTO();

	FComboBox cbYear = null; // 可选年度
	String selectYear = GlobalEx.loginYear;



	public void initize()
	{
		try
		{
			this.add(getBasePanel());
			this.createToolBar();
			setPanelEnable(false);
			setButtonState(true);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "界面初始化失败");
		}
	}


	/**
	 * 设置工具栏的按钮状态
	 * 
	 * @param aState
	 */
	public void setButtonState1(boolean state)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("增加".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("修改".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("删除".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("保存".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("取消".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
			if ("批量修改".equals(btnGet.getText()))
			{
				btnGet.setEnabled(state);
			}
		}
	}


	private FComboBox getYear()
	{
		cbYear = new FComboBox("年度：");
		String[] years = new String[] { "2010", "2011", "2012", "2013" };
		String YearValue = "";
		for (int i = 0; i < years.length; i++)
		{
			YearValue += years[i] + "#" + years[i] + "+";
		}
		if (YearValue.length() > 0)
		{
			YearValue = YearValue.substring(0, YearValue.length() - 1);
		}
		cbYear.setRefModel(YearValue);
		cbYear.setValue(Global.loginYear);
		cbYear.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				selectYear = cbYear.getValue().toString();
				if (!selectYear.equals(GlobalEx.loginYear))
				{
					// setCompEnable(false);
					setButtonState1(false);
				}
				else
				{
					setButtonState1(true);
				}
				// ?else setButtonStateAn(false, false, true, false, false);
				// refreshTreeData();// 刷新列表

				refreshPrjData(selectYear);
			}

		});
		return cbYear;
	}


	/**
	 * 获取主面板
	 * 
	 * @return
	 * @throws Exception
	 */
	private FPanel getBasePanel() throws Exception
	{

		FPanel pnlff = new FPanel();
		RowPreferedLayout layff = new RowPreferedLayout(1);
		pnlff.setLayout(layff);

		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);

		tfPrjCode = new FTextField("项目编码");
		tfPrjCode.setEnabled(false);
		tfPrjCode.setEditable(false);
		tfPrjName = new FTextField("项目名称");
		cbYearBegin = new FComboBox("起始年度");
		String qsndTmp = "";
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		for (int i = 0; i <= 5; i++)
		{

			qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";

		}
		if (qsndTmp.length() > 0)
		{
			qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
		}
		cbYearBegin.setRefModel(qsndTmp);
		cbYearBegin.setValue("");

		cbYearEnd = new FComboBox("结束年度");
		String jsndTmp = "";
		int num = Integer.parseInt(GlobalEx.loginYear);
		for (int i = 0; i < 10; i++)
		{
			jsndTmp += (num + i) + "#" + (num + i) + "+";
		}
		if (jsndTmp.length() > 0)
		{
			jsndTmp = jsndTmp.substring(0, jsndTmp.length() - 1);
		}
		cbYearEnd.setRefModel(jsndTmp);
		cbYearEnd.setValue("");
		try
		{
			List zxzqList = PrjInputStub.getMethod().getDmZxzq(Global.loginYear, Global.getCurrRegion());
			String zxzqTmp = "";
			for (int i = 0; i < zxzqList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) zxzqList.get(i);
				zxzqTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (zxzqTmp.length() > 0)
			{
				zxzqTmp = zxzqTmp.substring(0, zxzqTmp.length() - 1);
			}
			cbPrjCir = new FComboBox("执行周期");
			cbPrjCir.setRefModel(zxzqTmp);
			cbPrjCir.setValue("");
			cbPrjCir.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent e)
				{

					if (cbPrjCir.getSelectedIndex() != 2)
					{
						// 不是周期性，则年度起始与截止都是当前年度，并不可编辑
						cbYearBegin.setSelectedIndex(5);
						cbYearEnd.setSelectedIndex(0);
						cbYearBegin.setEnabled(false);
						cbYearEnd.setEnabled(false);
						dto.setOneYearPrj(true);
					}
					else
					{
						// 如果是周期性，则可以编辑
						cbYearBegin.setEnabled(true);
						cbYearEnd.setEnabled(true);
					}

				}
			});

			xmflList = PrjInputStub.getMethod().getDmXmfl(Global.loginYear, Global.getCurrRegion());
			String xmflTmp = "";
			for (int i = 0; i < xmflList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) xmflList.get(i);
				xmflTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmflTmp.length() > 0)
			{
				xmflTmp = xmflTmp.substring(0, xmflTmp.length() - 1);
			}
			cbPrjSort = new FComboBox("项目类别");
			cbPrjSort.setRefModel(xmflTmp);
			cbPrjSort.setValue("");

			List xmsxList = PrjInputStub.getMethod().getDmXmsx(Global.loginYear, Global.getCurrRegion());
			fxmsx = new FTreeAssistInput("项目属性");
			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			fxmsx.setData(treeData);
			fxmsx.setIsCheck(true);
			fxmsx.setOnlyLeafCanBeSelected(true);
			fxmsx.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent e)
				{
					String[] fxmsxStr = fxmsx.getValue() == null ? null : (String[]) fxmsx.getValue();
					if (fxmsxStr == null)
						return;
					String[] fxmsxf1 = new String[fxmsxStr.length];
					String xmsxstr = "";
					try
					{

						for (int i = 0; i < fxmsxStr.length; i++)
						{

							DataSet ds1 = DBSqlExec.client().getDataSet("select * from dm_xmsx  where chr_id ='" + fxmsxStr[i] + "'");
							ds1.fieldByName("chr_code").getValue();
							xmsxstr += ds1.fieldByName("chr_code").getValue().toString() + " " + ds1.fieldByName("chr_name").getValue().toString() + ";";
							fxmsxf1[i] = ds1.fieldByName("chr_code1").getValue().toString();
							if (i > 0)
							{
								if (!fxmsxf1[i].equals(fxmsxf1[i - 1]))
								{
									JOptionPane.showMessageDialog(Global.mainFrame, "项目属性错误请重新修改,如民生和非民生重复选择");
									fxmsx.setValue("");

								}

							}
							if (xmsxstr.length() > 0)
							{
								xmsxstr = xmsxstr.substring(0, xmsxstr.length() - 1);
							}

						}
					}
					catch (Exception ee)
					{
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}

				}
			});

			xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear, Global.getCurrRegion());
			String xmztTmp = "";
			for (int i = 0; i < xmztList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) xmztList.get(i);
				xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmztTmp.length() > 0)
			{
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}
			cbPrjState = new FComboBox("项目状态");
			cbPrjState.setRefModel(xmztTmp);
			cbPrjState.setValue("");

			tfAcct = new FTextField("科目选择");
			tfAcct.setProportion(0.083f);
			tfAcct.setEnabled(false);
			tfAcct.setEditable(false);
			btnAcct = new FButton("btnAcct", "选择科目");
			btnAcct.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					acctSelect.showAcctSelect(Common.nonNullStr(tfAcct.getValue()));
					Tools.centerWindow(acctSelect);
					acctSelect.setVisible(true);
					if (acctSelect.isOK)
					{
						List value = acctSelect.getTreeSelect();
						if (value != null && !value.isEmpty())
						{
							sAcctCodes = (String[]) value.get(0);
							sAcctNames = (String[]) value.get(1);
							String rvalue = Common.nonNullStr(value.get(2));
							sAcctBsIDs = (String[]) value.get(3);
							tfAcct.setValue(rvalue);
						}
					}
				}
			});
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.addControl(getYear(), new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfPrjCode, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfPrjName, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));

		xmxx.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfAcct, new TableConstraints(1, 7, false, true));
		xmxx.addControl(btnAcct, new TableConstraints(1, 1, false, true));

		pnlff.addControl(xmxx, new TableConstraints(2, 1000, true, true));

		tbPrj = new CustomTable(new String[] { "项目编码", "项目名称", "起始年度", "结束年度", "执行周期", "项目分类", "项目属性", "项目状态", "科目" }, new String[] { "xmbm", "xmmc", "c1", "c2", "c3", "c4", "c5", "c6", "accts" },
				null, true, null);
		tbPrj.reset();
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);
		tbPrj.setSize(2000, 2000);
		tbPrj.getTable().getTableHeader().setBackground(new Color(250, 228, 184));

		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
		treeEn = new CustomTree("预算单位", ds, "en_id", "code_name", "parent_id", null, "div_code");
		treeEn.reset();
		treeEn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1 && state == -1)
				{
					refreshPrjData();
					setPanelValue(true);
				}
			}
		});
		CustomTreeFinder cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);
		FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlCF.setLayout(layCF);
		pnlCF.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlCF.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		pnlCF.setTopInset(10);
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);

		FPanel pnlXM = new FPanel();
		RowPreferedLayout layXM = new RowPreferedLayout(4);
		pnlXM.setLayout(layXM);

		pnlff.addControl(tbPrj, new TableConstraints(10, 1000, true, true));

		pnlXM.addControl(pnlff, new TableConstraints(10, 1000, true, true));

		pnlXM.setTopInset(10);
		pnlXM.setLeftInset(10);
		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(pnlXM);

		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));
		return pnlBase;
	}


	public void refreshPrjData()
	{
		refreshPrjData(Global.loginYear);
	}


	private void refreshPrjData(String year)
	{
		if (treeEn.getSelectedNode() != null)
		{
			MyPfNode node = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if (node == null)
				return;
			if (state != -1)
				return;
			StringBuffer filter = new StringBuffer();
			filter.append(" and a.set_year = " + year);
			filter.append(" and a.rg_code = " + Global.getCurrRegion());
			DataSet ds = treeEn.getDataSet();
			try
			{
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						String divCode = ds.fieldByName("div_code").getString();
						filter.append(" and a.div_code like '" + divCode + "%'");
					}
				}
				// filter.append(" order by xmbm");
				tbPrj.setDataSet(PrjInputStub.getMethod().getPrjCreateInfo(filter.toString()));
				tbPrj.reset();
				setTableProp();
				// setPanelValue(true);
			}
			catch (Exception ee)
			{
				ErrorInfo.showErrorDialog(ee, "刷新单位信息出错");
				return;
			}

		}

	}


	public void doAdd()
	{
		// TODO 增加操作
		try
		{
			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			if (nodeSel != null && nodeSel.isLeaf())
			{
				state = 0;
				setPanelEnable(true);
				setPanelValue(true);
				setButtonState(false);
				String aPrjCode = dto.getNewPrjCode(treeEn.getDataSet().fieldByName("div_code").getString());
				// PrjInputStub.getMethod()
				// .getXmbm(
				// treeEn.getDataSet().fieldByName("div_code")
				// .getString(),GlobalEx.loginYear);
				tfPrjCode.setValue(aPrjCode);
				cbPrjState.setSelectedIndex(0);
				tfPrjName.setFocus();
				tbPrj.getDataSet().append();
				tbPrj.getDataSet().fieldByName("set_year").setValue(Global.loginYear);
				tbPrj.getDataSet().fieldByName("rg_code").setValue(Global.getCurrRegion());
				tbPrj.getDataSet().fieldByName("xmxh").setValue(UUIDRandom.generate());
				tbPrj.getDataSet().fieldByName("xmbm").setValue(aPrjCode);
				tbPrj.getDataSet().fieldByName("en_id").setValue(treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("div_code").setValue(treeEn.getDataSet().fieldByName("div_code").getString());
				tbPrj.getDataSet().fieldByName("div_name").setValue(treeEn.getDataSet().fieldByName("div_name").getString());
				tbPrj.getDataSet().fieldByName("lrr_dm").setValue(GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("lrrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("xgr_dm").setValue(GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("xgrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("gkdw_id").setValue(treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("gkdw_code").setValue(treeEn.getDataSet().fieldByName("div_code").getString());
				tbPrj.getDataSet().fieldByName("gkdw_name").setValue(treeEn.getDataSet().fieldByName("div_name").getString());

			}
			else
			{
				JOptionPane.showMessageDialog(null, "请选择预算单位");
				return;
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "增加失败");
		}
	}


	public void doDelete()
	{
		// TODO 删除操作
		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "请选择项目");
			return;
		}
		try
		{
			String xmxhs = "";
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}
			if (!xmxhs.equals(""))
			{
				List isExists = PrjInputStub.getMethod().isExistsXm(xmxhs);
				String xmmcs = "";
				if (isExists.size() > 0)
				{
					for (int i = 0; i < isExists.size(); i++)
					{
						Map map = new HashMap();
						map = (Map) isExists.get(i);
						xmmcs += map.get("xmmc").toString() + ",";
					}
				}
				if (xmmcs.length() > 0)
				{
					xmmcs = xmmcs.substring(0, xmmcs.length() - 1);
					JOptionPane.showMessageDialog(null, "以下项目已经被使用，不能删除\n" + xmmcs);
					return;
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "确认删除所选项目？", "提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) { return; }

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						List listSql = new ArrayList();
						listSql.add("delete from rp_xmjl_km where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmjl where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						PrjInputStub.getMethod().postData(listSql);
					}
				}
			}
			// MyPfNode enID = (MyPfNode)
			// treeEn.getSelectedNode().getUserObject();
			// dsPrj = PrjInputStub.getMethod().getXmByEnId(GlobalEx.loginYear,
			// GlobalEx.getCurrRegion(), enID.getValue());
			// tbPrj.setDataSet(dsPrj);
			// tbPrj.reset();
			// setTableProp();
			refreshPrjData();
			JOptionPane.showMessageDialog(null, "删除成功");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "删除失败");
			e.printStackTrace();
		}
	}


	public void doModify()
	{
		// TODO 修改操作
		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num > 1)
		{
			JOptionPane.showMessageDialog(null, "请选择一个项目进行修改");
			return;
		}
		int row = tbPrj.getTable().getSelectedRow();
		try
		{
			if (row >= 0)
			{
				String bmk = tbPrj.rowToBookmark(row);
				if (!tbPrj.getDataSet().gotoBookmark(bmk))
				{
					JOptionPane.showMessageDialog(null, "请选择项目");
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "请选择项目");
				return;
			}

		}
		catch (HeadlessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// PrjInfoDlg.setDoStates(PrjInfoDlg.IS_EDIT);
		// PrjInfoDlg userInfo = new PrjInfoDlg(this, treeEn, tbPrj, dsPrj);
		// setSizeAndLocation(userInfo);
		// userInfo.setVisible(true);
		state = 1;
		setPanelEnable(true);

		setButtonState(false);

		try
		{
			tbPrj.getDataSet().edit();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void doSave()
	{
		// TODO 保存操作
		String bmk = tbPrj.getDataSet().toogleBookmark();
		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
			return;
		}
		else
		{
			info = saveData();
			if (!info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
				return;
			}
		}
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		refreshPrjData();
		try
		{
			if (tbPrj.getDataSet().gotoBookmark(bmk))
			{
				DataSet ds = tbPrj.getDataSet();
				setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), ds.fieldByName("c1").getString(), ds.fieldByName("c2").getString(), ds.fieldByName("c3")
						.getString(), ds.fieldByName("c4").getString(), ds.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName("accts").getString());
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		String yb = cbYearBegin.getValue() == null ? "" : cbYearBegin.getValue().toString();
		String ye = cbYearEnd.getValue() == null ? "" : cbYearEnd.getValue().toString();
		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("项目名称不允许为空");
			tfPrjName.setFocus();
		}
		MyPfNode enID = (MyPfNode) treeEn.getSelectedNode().getUserObject();
		List list = null;
		try
		{
			list = PrjInputStub.getMethod().isExistsXmmc(enID.getValue(), Common.nonNullStr(tfPrjName.getValue()), Global.loginYear, Global.getCurrRegion());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (state == 0)
		{
			// 如果是增加，则新增加的项目名称在该单位下不允许重复
			if (list != null && list.size() > 0)
			{
				info.setSuccess(false);
				info.setsMessage("项目名称已经存在，请重新输入");
				tfPrjName.setFocus();
				return info;
			}
		}
		else if (state == 1)
		{
			// 如果是修改，则修改的项目名称不允许存在：编码不同，名称相同
			int i = 0;
			try
			{
				i = DBSqlExec.client().getRecordCount(
						"rp_xmjl",
						"xmmc='" + Common.nonNullStr(tfPrjName.getValue()) + "' and xmxh!='" + tbPrj.getDataSet().fieldByName("xmxh").getString() + "'and div_code='"
								+ tbPrj.getDataSet().fieldByName("gkdw_code").getString() + "' and set_year =" + Global.loginYear);
			}
			catch (Exception ee)
			{
				i = 0;
			}
			if (i > 0)
			{
				info.setSuccess(false);
				info.setsMessage("项目名称已经存在，请重新输入");
				tfPrjName.setFocus();
				return info;
			}
		}
		if (!Common.isNullStr(yb) && !Common.isNullStr(ye))
		{
			if (Integer.valueOf(yb).intValue() > Integer.valueOf(ye).intValue())
			{
				info.setSuccess(false);
				info.setsMessage("起始年度不允许大于结束年度，请重新输入");
				cbYearBegin.setFocus();
				return info;
			}
		}
		else
		{
			info.setSuccess(false);
			info.setsMessage("起始年度/结束年度 不允许为空，请重新输入");
			return info;
		}
		return info;
	}


	private InfoPackage saveData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			String xmmcStr = tfPrjName.getValue().toString();
			String qsndStr = cbYearBegin.getValue() == null ? "" : cbYearBegin.getValue().toString();
			String jsndStr = cbYearEnd.getValue() == null ? "" : cbYearEnd.getValue().toString();
			String zxzqStr = cbPrjCir.getValue() == null ? "" : cbPrjCir.getValue().toString();
			String xmflStr = getPrjSortCode();
			String xmsxStr = fxmsx.getText().toString();
			String xmztStr = getPrjStateCode();
			String accts = Common.nonNullStr(tfAcct.getValue());
			tbPrj.getDataSet().fieldByName("xmmc").setValue(xmmcStr);
			tbPrj.getDataSet().fieldByName("c1").setValue(qsndStr);
			tbPrj.getDataSet().fieldByName("c2").setValue(jsndStr);
			tbPrj.getDataSet().fieldByName("c3").setValue(zxzqStr);
			tbPrj.getDataSet().fieldByName("c4").setValue(xmflStr);
			tbPrj.getDataSet().fieldByName("c5").setValue(xmsxStr);
			tbPrj.getDataSet().fieldByName("c6").setValue(xmztStr);
			tbPrj.getDataSet().fieldByName("accts").setValue(accts);
			PrjInputStub.getMethod().savePrjCreateInfo(Global.loginYear, tbPrj.getDataSet().fieldByName("xmxh").getString(), tbPrj.getDataSet(), sAcctBsIDs, Global.getCurrRegion(), "-1", "0");
		}
		catch (Exception ee)
		{
			info.setSuccess(false);
			info.setsMessage("保存失败，错误信息为:" + ee.getMessage());
		}
		return info;
	}


	public void doCancel()
	{
		// TODO 取消操作
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		try
		{
			tbPrj.getDataSet().cancel();
			tbPrj.getDataSet().applyUpdate();
			refreshPrjData();
		}
		catch (Exception ee)
		{

		}
	}


	public void doImpProject()
	{
		try
		{
			List dbList = PrjInputStub.getMethod().getDBLink();
			if (dbList.size() == 0)
			{
				JOptionPane.showMessageDialog(null, "请配置DBLink链接");
				return;
			}

			String dbLinkName = ((Map) dbList.get(0)).get("dblink_name").toString();
			// 先同步项目
			// 将财政核心库中的FB_P_BASE表数据迁移到RP_XMJL表中去
			// 一次性全部导入，开发中会内存溢出，所以分单位导入
			List xmList = null;
			List kmList = null;
			List tbList = null;

			List disDivCode = PrjInputStub.getMethod().getDisDivCode(dbLinkName);

			for (int i = 0; i < disDivCode.size(); i++)
			{

				Map m = (Map) disDivCode.get(i);
				String divCode = m.get("div_code").toString();
				List baseData = PrjInputStub.getMethod().getDBDataXm(dbLinkName, "FB_P_BASE", divCode);
				for (int k = 0; k < baseData.size(); k++)
				{
					xmList = new ArrayList();
					tbList = new ArrayList();
					kmList = new ArrayList();

					RpXmjl rpXm = new RpXmjl();
					Map map = (Map) baseData.get(k);
					String xmbm = PrjInputStub.getMethod().getXmbm(divCode, Global.loginYear);
					String xmxh = UUIDRandom.generate();
					rpXm.setSetYear(Long.valueOf(map.get("set_year").toString()));
					rpXm.setXmxh(xmxh);
					rpXm.setXmbm(xmbm);
					rpXm.setXmmc(map.get("prj_name").toString());
					rpXm.setC4(map.get("prjsort_code").toString());
					rpXm.setEnId(map.get("en_id").toString());
					rpXm.setLrrDm(GlobalEx.getUserId());
					rpXm.setLrrq(new Date());
					rpXm.setXgrDm(GlobalEx.getUserId());
					rpXm.setXgrq(new Date());
					rpXm.setDivCode(divCode);
					rpXm.setC6("001");
					rpXm.setDivName(map.get("div_name").toString());
					rpXm.setRgCode(map.get("rg_code").toString());
					// rpXm.setGkdwCode("div_code");
					// rpXm.setGkdwId(map.get("en_id").toString())
					xmList.add(rpXm);

					// 科目
					List dbDataKm = PrjInputStub.getMethod().getDisDivKm(dbLinkName, divCode, map.get("prj_code").toString());
					for (int p = 0; p < dbDataKm.size(); p++)
					{

						Map mm = (Map) dbDataKm.get(p);
						RpXmjlKm rpKm = new RpXmjlKm();
						rpKm.setSetYear(Long.valueOf(GlobalEx.loginYear));
						rpKm.setXhid(UUIDRandom.generate());
						rpKm.setRpXmjl(rpXm);
						rpKm.setKmdm(mm.get("bs_id").toString());
						kmList.add(rpKm);
					}

					// 填报
					List tbData = PrjInputStub.getMethod().getDataTbxm(dbLinkName, divCode, map.get("prj_code").toString());
					int num = 1;
					int sbCode = 1;
					Map mTb = null;
					for (int tb = 0; tb < tbData.size(); tb++)
					{

						mTb = (Map) tbData.get(tb);
						RpXmsb rpSb = new RpXmsb();
						String detailType = mTb.get("detail_type").toString();

						rpSb.setSetYear(Long.valueOf(mTb.get("set_year").toString()));
						rpSb.setXmsbid(UUIDRandom.generate());
						rpSb.setRpXmjl(rpXm);
						Double f2 = Double.valueOf(mTb.get("f2").toString());
						Double f3 = Double.valueOf(mTb.get("f3").toString());
						Double f6 = Double.valueOf(mTb.get("f6").toString());
						Double f7 = Double.valueOf(mTb.get("f7").toString());
						double total = Double.parseDouble(mTb.get("f2").toString()) + Double.parseDouble(mTb.get("f3").toString()) + Double.parseDouble(mTb.get("f6").toString())
								+ Double.parseDouble(mTb.get("f7").toString());
						rpSb.setF2(f2);
						rpSb.setF3(f3);
						rpSb.setF6(f6);
						rpSb.setF7(f7);
						rpSb.setEnId(mTb.get("en_id").toString());
						rpSb.setRgCode(mTb.get("rg_code").toString());
						rpSb.setWsztDm("0");
						rpSb.setLrrDm(GlobalEx.getUserId());
						rpSb.setLrrq(new Date());
						rpSb.setXgrDm(GlobalEx.getUserId());
						rpSb.setXgrq(new Date());
						rpSb.setTotalSum(Double.valueOf(total + ""));
						if ("1".equals(detailType))
						{
							rpSb.setSbType("总预算");
							rpSb.setSbCode("111");
						}
						else if ("2".equals(detailType))
						{
							rpSb.setSbType("已安排数");
							rpSb.setSbCode("222");
						}
						else if ("11".equals(detailType))
						{
							rpSb.setSbType(num++ + "");
							rpSb.setBsId(mTb.get("bs_id").toString());
							rpSb.setBsiId(mTb.get("bsi_id") == null ? "" : mTb.get("bsi_id").toString());
							int sbCodeLength = (sbCode + "").length();
							if (sbCodeLength == 1)
							{
								rpSb.setSbCode("40000" + sbCode++);
							}
							else if (sbCodeLength == 2)
							{
								rpSb.setSbCode("4000" + sbCode++);
							}
							else if (sbCodeLength == 3)
							{
								rpSb.setSbCode("400" + sbCode++);
							}
							else if (sbCodeLength == 4)
							{
								rpSb.setSbCode("40" + sbCode++);
							}
							else if (sbCodeLength == 5)
							{
								rpSb.setSbCode("4" + sbCode++);
							}
						}
						tbList.add(rpSb);

					}
					RpXmsb rpSbTS = new RpXmsb();
					rpSbTS.setSetYear(Long.valueOf(mTb.get("set_year").toString()));
					rpSbTS.setXmsbid(UUIDRandom.generate());
					rpSbTS.setRpXmjl(rpXm);
					rpSbTS.setEnId(mTb.get("en_id").toString());
					rpSbTS.setRgCode(mTb.get("rg_code").toString());
					rpSbTS.setWsztDm("0");
					rpSbTS.setLrrDm(GlobalEx.getUserId());
					rpSbTS.setLrrq(new Date());
					rpSbTS.setXgrDm(GlobalEx.getUserId());
					rpSbTS.setXgrq(new Date());
					rpSbTS.setSbType("本年预算");
					rpSbTS.setSbCode("333");
					rpSbTS.setF2(Double.valueOf("0"));
					rpSbTS.setF3(Double.valueOf("0"));
					rpSbTS.setF6(Double.valueOf("0"));
					rpSbTS.setF7(Double.valueOf("0"));
					rpSbTS.setTotalSum(Double.valueOf("0"));
					tbList.add(rpSbTS);

					if (xmList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(xmList);// 保存项目
					}
					if (kmList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(kmList);// 保存科目
					}
					if (tbList.size() > 0)
					{
						PrjInputStub.getMethod().saveXmjl(tbList);// 保存上报
					}
				}

			}

			JOptionPane.showMessageDialog(Global.mainFrame, "导入成功");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "导入失败");
			e.printStackTrace();
		}
	}


	public void doUpdatedlg()
	{
		int count = tbPrj.getTable().getRowCount();
		int num = 0;

		for (int i = 0; i < count; i++)
		{
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "请选择项目");
			return;
		}
		try
		{
			String xmxhs = "";
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}

			TbPrjUpdateDlgzt tpud = new TbPrjUpdateDlgzt(xmxhs);
			Tools.centerWindow(tpud);
			setSizeAndLocation(tpud);
			tpud.setVisible(true);

			dsPrj = PrjInputStub.getMethod().getEnxmxhs(xmxhs);
			dsPrj.edit();
			dsPrj.beforeFirst();

			while (dsPrj.next())
			{
				String xmxh = dsPrj.fieldByName("xmxh").getString();
				List xmkmList = PrjInputStub.getMethod().getKmmcByXmxh(xmxh);
				String kmmc = "";
				for (int i = 0; i < xmkmList.size(); i++)
				{
					Map m = (Map) xmkmList.get(i);
					kmmc += m.get("chr_name") + ";";
				}
				if (kmmc.length() > 0)
				{
					kmmc = kmmc.substring(0, kmmc.length() - 1);
				}
				dsPrj.fieldByName("km").setValue(kmmc);
			}
			refreshPrjData();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void setPanelEnable(boolean isEnable)
	{
		treeEn.setEnabled(!isEnable);
		btnAcct.setEnabled(isEnable);
		tbPrj.setEnabled(!isEnable);
		tbPrj.getTable().setEnabled(!isEnable);
		tfPrjName.setEnabled(isEnable);
		tfPrjName.setEditable(isEnable);
		if (1 == state)
		{
			if (cbPrjCir.getValue() != null && "003".equals(cbPrjCir.getValue().toString()))
			{
				cbYearBegin.setEnabled(isEnable);
				cbYearEnd.setEnabled(isEnable);
			}
		}
		else
		{
			cbYearBegin.setEnabled(false);
			cbYearEnd.setEnabled(false);

		}
		cbPrjCir.setEnabled(isEnable);
		cbPrjSort.setEnabled(isEnable);
		fxmsx.setEnabled(isEnable);
		cbPrjState.setEnabled(isEnable);
		cbYear.setEnabled(!isEnable);
	}


	/**
	 * 是否清空表
	 * 
	 * @param isClear
	 */
	private void setPanelValue(boolean isClear)
	{
		if (isClear)
		{
			tfPrjName.setValue("");
			cbYearBegin.setSelectedIndex(-1);
			cbYearEnd.setSelectedIndex(-1);
			cbPrjCir.setSelectedIndex(-1);
			cbPrjSort.setSelectedIndex(-1);
			fxmsx.setValue("");
			cbPrjState.setSelectedIndex(-1);
		}
	}


	/**
	 * 设置面板值
	 * 
	 * @param bm
	 *            项目编码
	 * @param mc
	 *            名称
	 * @param qn
	 *            起始年度
	 * @param jn
	 *            结束年度
	 * @param zq
	 *            执行周期
	 * @param fl
	 *            项目分类
	 * @param sx
	 *            项目属性
	 * @param zt
	 *            项目状态
	 * @param km
	 *            科目信息
	 */
	private void setPanelValue(String bm, String mc, String qn, String jn, String zq, String fl, String sx, String zt, String km)
	{
		tfPrjCode.setValue(bm);
		tfPrjName.setValue(mc);
		fxmsx.setText(sx);
		cbPrjState.setSelectedIndex(getIndexByName(cbPrjState, zt));
		cbPrjSort.setSelectedIndex(getIndexByName(cbPrjSort, fl));
		cbPrjCir.setSelectedIndex(getIndexByName(cbPrjCir, zq));
		cbYearBegin.setSelectedIndex(getIndexByName(cbYearBegin, qn));
		cbYearEnd.setSelectedIndex(getIndexByName(cbYearEnd, jn));
		tfAcct.setValue(km);
	}


	private void setSizeAndLocation(TbPrjUpdateDlgzt userInfo)
	{
		Dimension userInfoSize = new Dimension(200, 100);
		userInfo.setSize(userInfoSize);
		// userInfo.setLocation(400, 300);

	}


	/**
	 * 设置工具栏的按钮状态
	 * 
	 * @param aState
	 */
	public void setButtonState(boolean isEditState)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("增加".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("修改".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("删除".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("保存".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			if ("取消".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
		}
	}


	private void setTableProp() throws Exception
	{
		tbPrj.getTable().setRowHeight(25);
		if (tbPrj != null && tbPrj.getDataSet() != null)
		{
			tbPrj.getTable().addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (tbPrj.getTable() != null)
					{

						int row = tbPrj.getTable().getSelectedRow();
						if (row < 0)
							return;
						String bmk = tbPrj.rowToBookmark(row);
						try
						{
							if (tbPrj.getDataSet() != null && !tbPrj.getDataSet().isEmpty() && tbPrj.getDataSet().gotoBookmark(bmk))
							{
								DataSet ds = tbPrj.getDataSet();
								if (!ds.isEmpty() && !ds.bof() && !ds.eof())
								{
									setPanelValue(ds.fieldByName("xmbm").getString(), ds.fieldByName("xmmc").getString(), ds.fieldByName("c1").getString(), ds.fieldByName("c2").getString(), ds
											.fieldByName("c3").getString(), ds.fieldByName("c4").getString(), ds.fieldByName("c5").getString(), ds.fieldByName("c6").getString(), ds.fieldByName(
											"accts").getString());
								}
							}
							else
							{
								setPanelValue(true);
							}
							cbYearBegin.setEnabled(false);
							cbYearEnd.setEnabled(false);
						}
						catch (Exception ee)
						{
							ErrorInfo.showErrorDialog(ee, "刷新项目信息出错，错误信息为:" + ee.getMessage());
							return;
						}
					}
				}
			});
		}
	}


	private String getPrjStateCode()
	{
		return Common.nonNullStr(cbPrjState.getValue());
	}


	private String getPrjSortCode()
	{
		return Common.nonNullStr(this.cbPrjSort.getValue());
	}


	private int getIndexByName(FComboBox cb, String name)
	{
		int index = -1;
		if (Common.isNullStr(name))
			return index;
		for (int i = 0; i < cb.getItemsCount(); i++)
		{
			if (cb.getItemAt(i) != null)
			{
				if (name.trim().equalsIgnoreCase(Common.nonNullStr(cb.getItemAt(i)))) { return i; }
			}
		}
		return index;
	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doBackToAudit()
	{
		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "请选择项目");
			return;
		}
		try
		{
			String xmmcs = "";

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmbm = tbPrj.getDataSet().fieldByName("xmbm").getString();
						String xmmc = tbPrj.getDataSet().fieldByName("xmmc").getString();
						xmmcs += "[" + xmbm + "]" + xmmc + ",";
					}
				}
			}
			if (xmmcs.length() > 0)
			{
				xmmcs = xmmcs.substring(0, xmmcs.length() - 1);
			}

			if (JOptionPane.showConfirmDialog(Global.mainFrame, "确认删除" + xmmcs + "--？删除无控制会彻底去除项目所有信息", "提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) { return; }

			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
						String xmbm = tbPrj.getDataSet().fieldByName("xmbm").getString();

						List listSql = new ArrayList();
						listSql.add("delete from rp_xmjl_km where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmsb where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " ");
						listSql.add("delete from rp_xmjl where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						listSql.add("delete from rp_audit_cur where prj_code = '" + xmbm + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
						PrjInputStub.getMethod().postData(listSql);
					}
				}
			}
			// MyPfNode enID = (MyPfNode)
			// treeEn.getSelectedNode().getUserObject();
			// dsPrj = PrjInputStub.getMethod().getXmByEnId(GlobalEx.loginYear,
			// GlobalEx.getCurrRegion(), enID.getValue());
			// tbPrj.setDataSet(dsPrj);
			// tbPrj.reset();
			// setTableProp();
			refreshPrjData();
			JOptionPane.showMessageDialog(null, "清除成功");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "清除失败");
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void doSendToAudit()
	{
		// TODO Auto-generated method stub

	}

}
