package gov.nbcs.rp.audit.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.input.ui.AcctSelectDialog;
import gov.nbcs.rp.input.ui.TbPrjAuditAffix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDecimalField;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




public class ModifyAuditPanel extends FDialog
{
	private static final long serialVersionUID = 1L;

	protected FTextField tfPrjCode;
	protected FTextField tfPrjName;
	protected FTreeAssistInput treePrjProp;
	protected FComboBox cbPrjSort;
	protected FComboBox cbPrjCir;
	protected FComboBox cbYearBegin;
	protected FComboBox cbYearEnd;
	protected FComboBox cbPrjState;
	protected FTextField tfAcct;
	protected FButton btnAcct;

	protected FButton btnClose;
	protected FButton btnAdd;
	protected FButton btnDel;
	protected FButton btnSave;

	protected PrjInputDTO dto = new PrjInputDTO();
	protected String xmxh;
	protected String enId;
	TbPrjAuditAffix tpaa = null;

	private FDecimalField total_sums = null;

	private AcctSelectDialog ad = new AcctSelectDialog();



	public ModifyAuditPanel()
	{
		super(Global.mainFrame);
		// 初始化操作
		dto.setCurState(1);
		dto.setDsAcctSel();
		// 指定窗体父亲
		this.initControls();
		this.initEvents();
		this.initUI();
	}


	/**
	 * 显示修改面板
	 * 
	 * @param xmxh
	 * @param enId
	 */
	public void showModifyAuditPanel(String xmxh, String enId)
	{
		this.xmxh = xmxh;
		this.enId = enId;
		try
		{
			this.initValues();
			this.loadValues(xmxh);
			dto.getReportUI().repaint();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, e.getMessage());
		}
	}


	protected int getIndexByName(FComboBox cb, String name)
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


	/**
	 * 加载数据
	 * 
	 * @param xmxh
	 * @throws Exception
	 */
	protected void loadValues(String xmxh) throws Exception
	{
		DataSet ds = PrjInputStub.getMethod().getPrjCreateInfo(" and set_year = " + Global.loginYear + " and rg_code= " + Global.getCurrRegion() + " and xmxh = '" + xmxh + "'");
		ds.beforeFirst();
		ds.next();
		if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
		{
			tfPrjCode.setValue(ds.fieldByName("xmbm").getString());
			tfPrjName.setValue(ds.fieldByName("xmmc").getString());
			treePrjProp.setText(ds.fieldByName("c5").getString());
			cbPrjSort.setSelectedIndex(getIndexByName(cbPrjSort, ds.fieldByName("c4").getString()));
			cbPrjCir.setSelectedIndex(getIndexByName(cbPrjCir, ds.fieldByName("c3").getString()));
			cbYearBegin.setSelectedIndex(getIndexByName(cbYearBegin, ds.fieldByName("c1").getString()));
			cbYearEnd.setSelectedIndex(getIndexByName(cbYearEnd, ds.fieldByName("c2").getString()));
			cbPrjState.setSelectedIndex(getIndexByName(cbPrjState, ds.fieldByName("c6").getString()));
			tfAcct.setValue(ds.fieldByName("accts").getString());
			String total_sum = total_sums.getValue() == null ? "" : total_sums.getValue().toString();
			total_sums.setValue(total_sum);
		}
		dto.setDsBody(PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, xmxh, Global.getCurrRegion()));
		dto.refreshReportUI();
		((Report) dto.getReportUI().getReport()).setBodyData(dto.getDsBody());
		((Report) dto.getReportUI().getReport()).refreshBody();
		dto.getReportUI().repaint();
	}


	/**
	 * 初始化控件
	 */
	protected void initControls()
	{
		tfPrjCode = new FTextField("项目编码");
		tfPrjName = new FTextField("项目名称");
		treePrjProp = new FTreeAssistInput("项目属性");
		cbPrjSort = new FComboBox("项目分类");
		cbPrjCir = new FComboBox("执行周期");
		cbYearBegin = new FComboBox("起始年度");
		cbYearEnd = new FComboBox("结束年度");
		cbPrjState = new FComboBox("项目状态");
		tfAcct = new FTextField("科目选择");
		tfAcct.setEnabled(false);
		tfAcct.setEditable(false);
		tfAcct.setProportion(0.12f);
		btnAcct = new FButton("btnAcct", "选择科目");
		total_sums = new FDecimalField("总投入：");

		btnClose = new FButton("", "关闭");
		btnClose.setIcon("images/rp/close.gif");
		btnClose.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnAdd = new FButton("", "添加明细");
		btnAdd.setIcon("images/rp/add.gif");
		btnAdd.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnDel = new FButton("", "删除明细");
		btnDel.setIcon("images/rp/del.gif");
		btnDel.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnSave = new FButton("", "保存");
		btnSave.setIcon("images/rp/save.gif");
		btnSave.setVerticalTextPosition(SwingConstants.BOTTOM);
	}


	/**
	 * 初始化值
	 * 
	 * @throws Exception
	 */
	protected void initValues() throws Exception
	{
		// 起始年度初始化
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

		// 结束年度初始化
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

		// 项目属性初始化
		List xmsxList = PrjInputStub.getMethod().getDmXmsx(Global.loginYear, Global.getCurrRegion());
		XMLData treeData = new XMLData();
		treeData.put("row", xmsxList);
		treePrjProp.setData(treeData);
		treePrjProp.setIsCheck(true);
		treePrjProp.setOnlyLeafCanBeSelected(true);

		// 项目分类初始化
		List xmflList = PrjInputStub.getMethod().getDmXmfl(Global.loginYear, Global.getCurrRegion());
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
		cbPrjSort.setRefModel(xmflTmp);
		cbPrjSort.setValue("");

		// 执行周期初始化
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
		cbPrjCir.setRefModel(zxzqTmp);
		cbPrjCir.setValue("");

		// 项目状态初始化
		List xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear, Global.getCurrRegion());
		String xmztTmp = "";
		int k = 0;
		if (!GlobalEx.isFisVis())
		{
			k = 1;
		}
		for (int i = k; i < xmztList.size(); i++)
		{
			Map m = new HashMap();
			m = (Map) xmztList.get(i);
			xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
		}
		if (xmztTmp.length() > 0)
		{
			xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
		}
		cbPrjState.setRefModel(xmztTmp);
		cbPrjState.setValue("");
	}


	/**
	 * 初始化事件
	 */
	protected void initEvents()
	{
		btnAcct.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ad.showAcctSelect(Common.nonNullStr(tfAcct.getValue()));
				Tools.centerWindow(ad);
				ad.setVisible(true);
				List value = ad.getTreeSelect();
				if (value != null && ad.isOK)
				{
					tfAcct.setValue(Common.nonNullStr(value.get(2)));
					createDefaultData(value);
					// dto.setDsAcctSel((String[]) value.get(0));
				}
			}
		});

		// 初始化关闭事件
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		// 初始化添加明细事件
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					DataSet ds = ((Report) dto.getReportUI().getReport()).getBodyData();
					ds.maskDataChange(true);
					ds.afterLast();
					ds.prior();
					String sbType = ds.fieldByName("SB_TYPE").getString();
					String sbCode = ds.fieldByName("SB_CODE").getString();
					ds.append();
					ds.fieldByName("SB_TYPE").setValue(String.valueOf((Integer.parseInt(sbType) + 1)));
					ds.fieldByName("SB_CODE").setValue(String.valueOf((Integer.parseInt(sbCode) + 1)));
					ds.fieldByName("ACCT_NAME").setValue("");
					ds.fieldByName("ACCT_NAME_JJ").setValue("");
					dto.getReportUI().repaint();
					ds.maskDataChange(false);
				}
				catch (Exception e1)
				{
					ErrorInfo.showErrorDialog(e1.getMessage());
				}
			}
		});

		// 初始化删除明细事件
		btnDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Cell cell = (Cell) ((Report) dto.getReportUI().getReport()).getSelectedCell();
					if (((Report) dto.getReportUI().getReport()).getBodyData().gotoBookmark(cell.getBookmark()))
					{
						String supType = ((Report) dto.getReportUI().getReport()).getBodyData().fieldByName("SB_CODE").getString();
						if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
							return;
						((Report) dto.getReportUI().getReport()).getBodyData().delete();
						dto.getReportUI().repaint();
					}
				}
				catch (Exception e1)
				{
					ErrorInfo.showErrorDialog(e1.getMessage());
				}
			}
		});

		// 初始化保存事件
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doSave();
			}
		});
	}


	/**
	 * 初始化界面
	 */
	protected void initUI()
	{
		// 设置窗体大小
		this.setSize(835, 600);
		// 设置窗体大小是否可变
		this.setResizable(false);

		// 窗体组件自动充满。
		this.getContentPane().add(getMainPanel());

		this.dispose();
		// 设置窗体标题
		this.setTitle("审核轨迹记录查看界面");
		// 设置窗体模态显示
		this.setModal(true);
	}


	/**
	 * 窗口主面板
	 * 
	 * @return
	 */
	private FPanel getMainPanel()
	{
		FPanel pnl = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(12);
		pnl.setLayout(lay);
		FToolBar toolbar = new FToolBar();
		toolbar.addControl(btnClose);
		toolbar.addControl(btnAdd);
		toolbar.addControl(btnDel);
		toolbar.addControl(btnSave);
		pnl.addControl(toolbar, new TableConstraints(1, 12, true, true));
		pnl.addControl(getBaseInfoPanel(), new TableConstraints(2, 12, true, true));
		pnl.addControl(getPrjDownPanel(), new TableConstraints(10, 12, true, true));
		return pnl;
	}


	/**
	 * 项目基本信息
	 * 
	 * @return
	 */
	protected FPanel getBaseInfoPanel()
	{
		FPanel baseInfo = new FPanel();
		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		baseInfo.setLayout(xmLay);

		baseInfo.setLeftInset(10);
		baseInfo.setRightInset(10);
		baseInfo.setBottomInset(10);
		baseInfo.setTopInset(10);
		baseInfo.addControl(tfPrjCode, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(tfPrjName, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(treePrjProp, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		baseInfo.addControl(tfAcct, new TableConstraints(1, 5, false, true));
		baseInfo.addControl(btnAcct, new TableConstraints(1, 1, false, true));
		baseInfo.addControl(total_sums, new TableConstraints(1, 2, false, true));

		return baseInfo;
	}


	/**
	 * 项目明细选项卡面板
	 * 
	 * @return
	 */
	protected FTabbedPane getPrjDownPanel()
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("项目填报", getPrjDetailPanel());
		tabpane.addControl("附 件", getPrjAffixPanel());
		// tabpane.addControl("申报理由", getPrjRemarkPanel());
		return tabpane;
	}


	private FPanel getPrjAffixPanel()
	{
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		tpaa.setAuditXmbm(this.xmxh);
		return tpaa.setBottomPanel();
	}


	/**
	 * 项目填报明细表格
	 * 
	 * @return
	 */
	protected FPanel getPrjDetailPanel()
	{
		FPanel pnlReport = new FPanel();
		RowPreferedLayout layReport = new RowPreferedLayout(1);
		pnlReport.setLayout(layReport);
		pnlReport.add(dto.getReportUI(), new TableConstraints(1, 1, true, true));
		return pnlReport;
	}


	protected void createDefaultData(List value)
	{
		int c = 0;
		try
		{
			DataSet dsBody = ((Report) dto.getReportUI().getReport()).getBodyData();
			if (dsBody != null && !dsBody.isEmpty())
			{
				String[] sAcctCodes = (String[]) value.get(0);
				String[] sAcctNames = (String[]) value.get(1);
				String[] sAcctBsIDs = (String[]) value.get(3);

				for (int j = 0; j < sAcctCodes.length; j++)
				{

					if (dsBody.locate("BS_ID", sAcctBsIDs[j]))
					{
						c++;
						continue;

					}

					dsBody.append();
					dsBody.fieldByName("xmxh").setValue(xmxh);
					dsBody.fieldByName("rg_code").setValue(Global.getCurrRegion());
					dsBody.fieldByName("ACCT_NAME").setValue("[" + sAcctCodes[j] + "]" + sAcctNames[j]);
					dsBody.fieldByName("BS_ID").setValue(sAcctBsIDs[j]);
					dsBody.fieldByName("ACCT_CODE").setValue(sAcctCodes[j]);
					dsBody.fieldByName("setYear").setValue(Global.loginYear);
					dsBody.fieldByName("YSJC_MC").setValue("");
					// dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
					// dsBody.fieldByName("SB_CODE").setValue(
					// String.valueOf(400000 + i));
				}

				List list = new ArrayList();
				Arrays.sort(sAcctBsIDs);
				dsBody.beforeFirst();
				while (dsBody.next())
				{
					String supType = dsBody.fieldByName("SB_CODE").getString();
					if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
						continue;
					if (Arrays.binarySearch(sAcctBsIDs, dsBody.fieldByName("BS_ID").getString()) < 0)
					{
						list.add(dsBody.fieldByName("BS_ID").getString());
					}
				}
				for (int k = 0; k < list.size(); k++)
				{
					if (dsBody.locate("BS_ID", Common.nonNullStr(list.get(k))))
						dsBody.delete();
				}
				dsBody.beforeFirst();
				dsBody.edit();
				int i = 1;
				while (dsBody.next())
				{
					String supType = dsBody.fieldByName("SB_CODE").getString();
					if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType) || "333".equalsIgnoreCase(supType))
						continue;
					dsBody.fieldByName("SB_TYPE").setValue(String.valueOf(i++));
					dsBody.fieldByName("SB_CODE").setValue(String.valueOf(400000 + i));
				}
				// dsBody.applyUpdate();
				dto.refreshReportUI();
			}
			if (c == 0)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "功能科目修改，项目明细金额数据被清除");
			}
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "生成项目明细信息失败，错误信息为:" + ee.getMessage());
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			ReportUI reportUI = dto.getReportUI();
			String st = "";
			String sl = "";
			String sn = "";

			for (int i = 6; i < 13; i++)
			{
				if (9 == i)
					continue;
				// 合计行计算
				st = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue());
				sl = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue());
				sn = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue());
				if (Double.parseDouble(st) < (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() != 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("资金来源：“合计”的“本年预算数”加上“往年安排数”超过总预算");
						// break;
						case 6:
							info.setsMessage("资金来源：“一般预算”的“本年预算数”加上“往年安排数”超过总预算");
							break;
						case 7:
							info.setsMessage("资金来源：“基金预算”的“本年预算数”加上“往年安排数”超过总预算");
							break;
						case 8:
							info.setsMessage("资金来源：“其他”的“本年预算数”加上“往年安排数”超过总预算");
							break;

						case 10:
							info.setsMessage("资金来源：上级 “一般预算”的“本年预算数”加上“往年安排数”超过总预算");
							break;
						case 11:
							info.setsMessage("资金来源：上级 “基金预算”的“本年预算数”加上“往年安排数”超过总预算");
							break;
						case 12:
							info.setsMessage("资金来源：上级 “其他”的“本年预算数”加上“往年安排数”超过总预算");
							break;

					}
					return info;
				}
				if (Double.parseDouble(st) != (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() == 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("资金来源：“合计”的“本年预算数”加上“往年安排数”超过总预算");
						// break;
						case 6:
							info.setsMessage("结束年度为本年 资金来源：“一般预算”的“本年预算数”加上“往年安排数”不等于总预算");
							break;
						case 7:
							info.setsMessage("结束年度为本年 资金来源：“基金预算”的“本年预算数”加上“往年安排数”不等于总预算");
							break;
						case 8:
							info.setsMessage("结束年度为本年 资金来源：“其他”的“本年预算数”加上“往年安排数”不等于总预算");
							break;

						case 10:
							info.setsMessage("结束年度为本年 上级 资金来源：“一般预算”的“本年预算数”加上“往年安排数”不等于总预算");
							break;
						case 11:
							info.setsMessage("结束年度为本年 上级 资金来源：“基金预算”的“本年预算数”加上“往年安排数”不等于总预算");
							break;
						case 12:
							info.setsMessage("结束年度为本年 上级 资金来源：“其他”的“本年预算数”加上“往年安排数”不等于总预算");
							break;

					}
					return info;
				}
			}
			for (int i = 1; i < 5; i++)
			{
				// 从第二列开始
				for (int j = 5; j < reportUI.getReport().getRowCount(); j++)
				{
					if (Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, j).getValue())))
					{
						info.setSuccess(false);
						String value = "第" + (j + 1) + "行，第" + i + "列";
						if (i == 1)
							value += "  预算级次不能为空";
						else if (i == 2)
							value += "  功能科目不能为空";
						else if (i == 3)
							value = "  经济科目不能为空";
						else if (i == 4)
							value = "  资金为0";
						else
							value = "预算级次、功能科目、经济科目 不允许为空。";
						info.setsMessage(value);
						return info;
					}
				}
			}
			DataSet ds = (((Report) dto.getReportUI().getReport()).getBodyData());
			int k = 1;
			String type = "";
			ds.beforeFirst();
			while (ds.next())
			{
				type = ds.fieldByName("SB_CODE").getString();
				if ("111".equals(type) || "222".equals(type) || "333".equals(type))
					continue;
				ds.fieldByName("SB_TYPE").setValue(String.valueOf(k++));
				ds.fieldByName("SB_CODE").setValue("40000" + k);
			}
		}
		catch (Exception e1)
		{
			info.setSuccess(false);
			info.setsMessage("保存失败，错误信息为" + e1.getMessage());
			return info;
		}
		return info;
	}


	public void doSave()
	{
		dto.calData();
		if (cbYearBegin.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "起始年度不能为空");
			return;
		}
		if (cbYearEnd.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "结束年度不能为空");
			return;
		}
		if ((cbPrjCir.getValue() == null) || cbPrjCir.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "执行周期不能为空");
			return;
		}
		if ((cbPrjSort.getValue() == null) || cbPrjSort.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "项目分类不能为空");
			return;
		}
		// if (fxmsx.getValue() == null) {
		// JOptionPane.showMessageDialog(Global.mainFrame, "项目属性不能为空");
		// return;
		// }
		if ((cbPrjState.getValue() == null) || cbPrjState.getValue().equals(""))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "项目状态不能为空");
			return;
		}
		if (Common.isNullStr(treePrjProp.getText()))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "项目属性不能为空");
			return;
		}
		if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue())))
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "功能科目不能为空");
			return;
		}
		if (Integer.valueOf((String) cbYearBegin.getValue()).intValue() == Integer.valueOf((String) cbYearEnd.getValue()).intValue() && cbPrjCir.getSelectedIndex() == 2)
		{
			new MessageBox("周期性项目起始年度不能等于结束年度，请重新输入!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
	

		InfoPackage info = checkData();
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "保存失败，错误信息为：\n" + info.getsMessage());
			return;
		}
		try
		{
			DataSet xmData = PrjInputStub.getMethod().getXmDataByXmxh(xmxh);
			// if (!xmData.locate("xmxh", dto.getXmxh())) { return; }
			// if (Common.isNullStr(treePrjProp.getText()))
			// {
			// new MessageBox("归口单位不能为空", MessageBox.MESSAGE,
			// MessageBox.OK).setVisible(true);
			// return;
			// }
			xmData.edit();
			xmData.locate("xmxh", xmxh);
			xmData.fieldByName("c1").setValue(cbYearBegin.getValue());
			xmData.fieldByName("c2").setValue(cbYearEnd.getValue());
			xmData.fieldByName("c3").setValue(cbPrjCir.getValue());
			xmData.fieldByName("c4").setValue(cbPrjSort.getValue());
			// String xmsx = "";
			String xmsx = treePrjProp.getText();
			if (Common.isNullStr(xmsx))
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "项目属性为空，请选择");
				return;
			}
			xmData.fieldByName("c5").setValue(xmsx);

			// if (!Common.isNullStr(fxmsx.getText()))
			// xmData.fieldByName("c5").setValue(xmsx);
			xmData.fieldByName("c6").setValue(cbPrjState.getValue());
			xmData.fieldByName("accts").setValue(tfAcct.getValue());
			xmData.fieldByName("xgr_dm").setValue(GlobalEx.getUserId());
			xmData.fieldByName("xgrq").setValue(new Date());

			// 科目
			List listArr = new ArrayList();
			String[] kmxzStr = Common.nonNullStr(tfAcct.getValue()).split(";");
			listArr.add("delete from rp_xmjl_km where xmxh='" + dto.getXmxh() + "' and set_year = " + Global.loginYear);
			for (int i = 0; i < kmxzStr.length; i++)
			{
				if (Common.isNullStr(kmxzStr[i]))
					continue;
				String kmdm = kmxzStr[i].substring(1, kmxzStr[i].indexOf("]"));
				String bs_id = PrjInputStub.getMethod().getBsIDByCode(kmdm);
				listArr.add("insert into rp_xmjl_km (set_year, xhid, xmxh, kmdm) values(" + Global.loginYear + ",newid,'" + dto.getXmxh() + "','" + bs_id + "')");
			}
			// RpXmjl rpXmjl = new RpXmjl();
			// rpXmjl.setXmxh(xmxh);
			// 项目建立信息
			info = PrjInputStub.getMethod().editXmjl(xmData);
			if (!info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "保存失败，错误信息为：" + info.getsMessage());
				return;
			}
			QueryStub.getClientQueryTool().executeBatch(listArr);
			if (StringUtils.isNotEmpty(enId))
			{
				PrjInputStub.getMethod().savePrjDetailInfo(enId, xmxh, ((Report) dto.getReportUI().getReport()).getBodyData(), Global.loginYear, Global.getCurrRegion(), Global.getUserId(),
						Tools.getCurrDate());
			}
			// 保存申报理由
			List sqlSbly = new ArrayList();
			sqlSbly.add("delete from rp_xmsb_ly where xmxh = '" + xmxh + "' and set_year = " + Global.loginYear + " and rg_code = " + Global.getCurrRegion());
			sqlSbly.add("insert into rp_xmsb_ly (set_year,rg_code, ly_id, xmxh, ly) values(" + Global.loginYear + "," + Global.getCurrRegion() + ",newid,'" + dto.getXmxh() + "','')");
			QueryStub.getClientQueryTool().executeBatch(sqlSbly);
			PrjInputStub.getMethod().savePrjInfoHistory(new String[] { xmxh }, dto.getBatchNoByPrjCode(xmxh), dto.getAuditStepByPrjCode(xmxh));
			if (info.getSuccess())
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "保存成功");
			}
			else
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "保存失败");
				return;
			}
			// dto.modData(dto.getReportUI(), (Report) dto.getReportUI()
			// .getReport(), ((Report) dto.getReportUI().getReport())
			// .getBodyData(), (Cell) ((Report) dto.getReportUI()
			// .getReport()).getSelectedCell());
			// ((Report) dto.getReportUI().getReport()).setBodyData(PrjInputStub
			// .getMethod().getPrjDetailInfo(Global.loginYear,
			// dto.getXmxh(), Global.getCurrRegion()));
			// ((Report) dto.getReportUI().getReport()).refreshBody();
			// dto.getReportUI().repaint();

		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "保存失败");
			e.printStackTrace();
		}

	}

}
