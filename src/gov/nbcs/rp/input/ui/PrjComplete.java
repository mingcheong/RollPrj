package gov.nbcs.rp.input.ui;

/**
 * @author 谢昀荪
 * 
 * @version 创建时间：Jun 7, 20122:38:45 PM
 * 
 * @Description
 */

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.audit.ibs.IPrjAudit.PrjAuditTable.Affix;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.prjsync.action.PrjSyncActionedUI;
import gov.nbcs.rp.statc.ConstStr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.util.Global;




/**
 * @author xys
 * 
 * @version 创建时间：2012-3-12 下午05:02:14
 * 
 * @Description 填报项目
 */
public class PrjComplete extends RpModulePanel implements PrjActionUI,PrjSyncActionedUI
{

	private static final long serialVersionUID = 1L;

	private int state = -1;// -1 浏览状态 0:增加 1：修改

	private CustomTree treeEn;// 预算单位

	private CustomTreeFinder cf = null;

	// private CustomComboBox cbQueryType;// 项目状态

	public CustomTable tbPrj = null;// 列表

	FTextField tfPrjCode = null; // 项目编码

	FTextField tfPrjName = null; // 项目名称

	FComboBox cbYearBegin = null; // 起始年度

	FComboBox cbYearEnd = null; // 结束年度

	FComboBox cbPrjCir = null; // 执行周期

	FComboBox cbPrjSort = null; // 项目分类
	FTreeAssistInput fxmsx = null;// 项目属性
	FTreeAssistInput gkdw = null;// 归口单位
	FComboBox cbPrjState = null; // 项目状态
	FComboBox sendState = null; // 审核状态
	FTextField tfAcct = null; // 科目选择
	FButton btnAcct; // 选择科目按钮
	private FButton btnAdd = null;// 增加明细
	private FButton btnDel = null;// 删除明细
	// private FButton btnRead = null;// 读费用明细
	// private FButton btnMod = null;// 修改费用明细
	private FTextArea fSbly;// 申报理由
	private Affix tbaffix; // 填报附件类

	FComboBox cbYear = null; // 可选年度
	String selectYear = GlobalEx.loginYear;
	DataSet treeds = null;
	PrjInputDTO prj_tb = null;
	TbPrjAuditAffix tpaa;
	String module_id = GlobalEx.getModuleId();



	// TbModPanel tbmodpanel=null;
	// private TbModPanel tm=null;
	/**
	 * 初始化
	 */
	public void initize()
	{
		try
		{
			this.initData();
			this.initAffix();
			this.createToolBar();
			this.add(getBasePanel());
			this.controlButton();
		}
		catch (Exception ex)
		{
			ErrorInfo.showErrorDialog(ex, "项目申报界面初始化失败");
		}
	}


	/**
	 * 实例化附件列表
	 */
	private void initAffix() throws Exception
	{
		// tbaffix = new Affix();
	}


	/**
	 * 初始化项目明细列表
	 */
	private void initData() throws Exception
	{
		prj_tb = new PrjInputDTO();
	}


	/**
	 * 获取主面板
	 * 
	 * @return FPanel
	 * @throws Exception
	 */
	private FPanel getBasePanel() throws Exception
	{
		// 主面板
		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);

		// 分割左右面板
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(300);// 设置树形面板宽度位置为300px

		// 左边面板
		FPanel leftInfo = new FPanel();
		FScrollPane pnlTree = new FScrollPane();
		RowPreferedLayout leftlay = new RowPreferedLayout(1);
		leftInfo.setLayout(leftlay);

		treeEn = new CustomTree("预算单位", treeds, "en_id", "code_name", "parent_id", null, "div_code");
		refreshTreeData();
		this.addTreeListener();
		cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);
		leftInfo.addControl(cf, new TableConstraints(1, 1, false, true));
		leftInfo.addControl(pnlTree, new TableConstraints(1, 1, true, true));

		// 右边面板
		FPanel rightInfo = new FPanel();
		RowPreferedLayout rightlay = new RowPreferedLayout(1);
		rightInfo.setLayout(rightlay);
		rightInfo.setTopInset(10);
		rightInfo.setLeftInset(10);
		rightInfo.setRightInset(10);
		rightInfo.setBottomInset(10);

		// 分割上下面板
		FSplitPane pnlff = new FSplitPane();
		pnlff.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnlff.setDividerLocation(200);

		// 将上下面板放进分割上下面板
		pnlff.addControl(getTopFpanel());
		pnlff.addControl(getRight_BottomPanel());

		// 将分割上下面板放进右边面板
		rightInfo.add(pnlff);

		// 将左右面板放进分割面板
		pnlInfo.addControl(leftInfo);
		pnlInfo.addControl(rightInfo);
		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		refreshPrjData();// 刷新列表
		pnlBase.add(pnlInfo);
		return pnlBase;
	}


	public FPanel getPrjAffixPanel() throws Exception
	{
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		return tpaa.setBottomPanel();
	}


	/**
	 * 实例右边面板上部分面板
	 * 
	 * @return FPanel
	 */
	private FPanel getTopFpanel()
	{
		// 右边面板上部分面板
		FPanel topInfo = new FPanel();
		RowPreferedLayout toplay = new RowPreferedLayout(1);
		topInfo.setLayout(toplay);

		// 内容面板
		FPanel topdiv = new FPanel();
		RowPreferedLayout topdivlay = new RowPreferedLayout(4);
		topdivlay.setRowGap(10);
		topdiv.setLayout(topdivlay);
		topdiv.setTopInset(10);
		topdiv.setLeftInset(10);
		topdiv.setRightInset(10);
		topdiv.setBottomInset(10);
		try
		{
			tbPrj = new CustomTable(new String[] { "是否补填", "项目编码", "单位编码", "单位名称", "项目名称", "起始年度", "结束年度", "执行周期", "项目分类", "项目属性", "科目", "指标金额" }, new String[] { "step_id", "xmbm", "div_code",
					"div_name", "xmmc", "c1", "c2", "c3", "c4", "c5", "accts", "budget_money" }, null, true, null);
			// setTableProp();
			this.addTableListener();
			topdiv.addControl(getsendstate(), new TableConstraints(1, 1, false, true));
			topdiv.addControl(getYear(), new TableConstraints(1, 1, false, true));
			topdiv.addControl(tbPrj, new TableConstraints(1, 4, true, true));

			// 将内容面板放进上部门面板
			topInfo.add(topdiv);
		}
		catch (Exception ex)
		{
			ErrorInfo.showErrorDialog(ex, "项目列表初始化失败");
		}
		return topInfo;
	}


	/**
	 * 设置表格的列宽
	 * 
	 * @throws Exception
	 */
	protected void setTableProp() throws Exception
	{
		tbPrj.reset();
		tbPrj.getTable().setRowHeight(25);
		tbPrj.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(3).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(4).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(5).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(6).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(7).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(8).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(9).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(10).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(11).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(12).setPreferredWidth(80);
		tbPrj.setSize(2000, 2000);
		tbPrj.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
	}


	/**
	 * 实例右边面板上部分面板中的项目状态
	 * 
	 * @return CustomComboBox
	 */
	// private CustomComboBox getQueryType() {
	// try {
	// DataSet ds = DataSet.createClient();
	// if (!GlobalEx.isFisVis()) {
	// ds.append();
	// ds.fieldByName("id").setValue("1");
	// ds.fieldByName("name").setValue("未补填");
	// ds.append();
	// ds.fieldByName("id").setValue("2");
	// ds.fieldByName("name").setValue("已补填");
	//			
	// } else {
	// ds.append();
	// ds.fieldByName("id").setValue("1");
	// ds.fieldByName("name").setValue("未补填");
	// ds.append();
	// ds.fieldByName("id").setValue("2");
	// ds.fieldByName("name").setValue("已补填");
	//			
	// }
	// cbQueryType = new CustomComboBox(ds, "id", "name");
	// cbQueryType.setTitle("项目状态：");
	// cbQueryType.addValueChangeListener(new ValueChangeListener() {
	// public void valueChanged(ValueChangeEvent arg0) {
	// refreshPrjData();// 刷新列表
	// controlButton();
	// }
	// });
	//			
	//
	// } catch (Exception ex) {
	// ErrorInfo.showErrorDialog(ex, "项目状态初始化失败");
	// }
	// return cbQueryType;
	// }
	// private FComboBox getsendstate()
	// {
	// try
	// {
	// DataSet ds2 = DataSet.createClient();
	// ds2.append();
	// ds2.fieldByName("id").setValue("1");
	// ds2.fieldByName("name").setValue("未送审");
	// ds2.append();
	// ds2.fieldByName("id").setValue("2");
	// ds2.fieldByName("name").setValue("已送审");
	// sendState = new CustomComboBox(ds2, "id", "name");
	// sendState.setTitle("审核状态：");
	// sendState.addValueChangeListener(new ValueChangeListener()
	// {
	// public void valueChanged(ValueChangeEvent arg0)
	// {
	// refreshPrjData();// 刷新列表
	// controlButton();
	// }
	// });
	// }
	// catch (Exception ex)
	// {
	// ErrorInfo.showErrorDialog(ex, "审核状态初始化失败");
	// }
	//
	// return sendState;
	// }
	// private CustomComboBox getsendstate(){
	// try{
	// DataSet ds2 = DataSet.createClient();
	// ds2.append();
	// ds2.fieldByName("id").setValue("1");
	// ds2.fieldByName("name").setValue("未送审");
	// ds2.append();
	// ds2.fieldByName("id").setValue("2");
	// ds2.fieldByName("name").setValue("已送审");
	// sendState = new CustomComboBox(ds2, "id", "name");
	// sendState.setTitle("审核状态：");
	// sendState.addValueChangeListener(new ValueChangeListener() {
	// public void valueChanged(ValueChangeEvent arg0) {
	// refreshPrjData();// 刷新列表
	// controlButton();
	// }
	// });
	// } catch (Exception ex) {
	// ErrorInfo.showErrorDialog(ex, "审核状态初始化失败");
	// }
	//	
	// return sendState;
	// }
	private FComboBox getsendstate() throws Exception
	{
		sendState = new FComboBox("");
		sendState.setTitle("项目状态：");
		sendState.setProportion(0.4f);
		sendState.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				refreshPrjData();
			}
		});
		setStatusCbx(sendState);
		try
		{
			createToolBar();
		}
		catch (Exception e)
		{

		}
		return sendState;
	}


	private FComboBox getYear()
	{
		cbYear = new FComboBox("年度：");
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		String YearValue = "";
		for (int i = 0; i <= 5; i++)
		{
			YearValue += (numQs + i) + "#" + (numQs + i) + "+";
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
				if (selectYear != GlobalEx.loginYear)
				{
					setButtonStateAn(false, false, false, false, false);
				}
				else
					setButtonStateAn(false, false, true, false, false);
				refreshTreeData();// 刷新列表
				refreshPrjData();
			}

		});
		return cbYear;
	}


	/**
	 * 实例右边面板下部分分隔面板
	 */
	private FPanel getRight_BottomPanel() throws Exception
	{
		FPanel right_bottom_panel = new FPanel();
		RowPreferedLayout right_bottomlay = new RowPreferedLayout(2);
		right_bottom_panel.setLayout(right_bottomlay);
		right_bottom_panel.addControl(getPrjFilePanel(), new TableConstraints(15, 2, false, true));
		return right_bottom_panel;
	}


	/**
	 * 多标签
	 * 
	 * @return FTabbedPane
	 */
	public FTabbedPane getPrjFilePanel() throws Exception
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("项目填报", getPrjTbPanel());
		// tabpane.addControl("附 件", getPrjAffixPanel());
		tabpane.addControl("附    件", getPrjAffixPanel());
		tabpane.addControl("申报理由", getPrjRemarkPanel());
		return tabpane;
	}


	/**
	 * 项目填报
	 */
	private FPanel getPrjTbPanel()
	{
		FPanel table_div = new FPanel();
		RowPreferedLayout table_div_lay = new RowPreferedLayout(1);
		table_div.setLayout(table_div_lay);
		table_div.setTopInset(10);
		table_div.setLeftInset(10);
		table_div.setRightInset(10);
		table_div.setBottomInset(10);
		table_div.add(prj_tb.getReportUI(), new TableConstraints(1, 1, true, true));
		// prj_tb.getReportUI().getGrid().addMouseListener(new MouseAdapter()
		// {
		// // 单击列表行触发事件
		// public void mouseClicked(MouseEvent e)
		// {
		// try
		// {
		// // Cell cell = (Cell) ((Report) prj_tb.getReportUI()
		// // .getReport()).getSelectedCell();
		// // if (((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().gotoBookmark(cell.getBookmark()))
		// // {
		// // String supType = ((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("SB_CODE").getString();
		// // if ("111".equalsIgnoreCase(supType)||
		// // "222".equalsIgnoreCase(supType)||
		// // "333".equalsIgnoreCase(supType))
		// // {
		// // return;
		// // }
		// String xmxh = ((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("xmxh").getString();
		// // Double.valueOf(
		// // double sum = Double.valueOf(Common.nonNullStr(((Report)
		// //
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("TOTAL_SUM").getValue())).doubleValue();
		// // oper.getOperationState().setSum(sum);
		// prj_tb.getReportUI().repaint();
		// // }
		// }
		// catch (Exception ex)
		// {
		// // TODO Auto-generated catch block
		// ex.printStackTrace();
		// }
		// }
		// });
		return table_div;
	}


	/**
	 * 多标签中申报理由
	 * 
	 * @return FPanel
	 */
	private FPanel getPrjRemarkPanel()
	{
		FPanel ply = new FPanel();
		RowPreferedLayout plyRpl = new RowPreferedLayout(1);
		ply.setLayout(plyRpl);
		ply.setTopInset(10);
		ply.setLeftInset(10);
		ply.setRightInset(10);
		ply.setBottomInset(10);
		fSbly = new FTextArea();
		fSbly.setProportion(0.001f);
		fSbly.setEnabled(false);

		fSbly.getEditor().setBackground(Color.white);
		ply.addControl(fSbly, new TableConstraints(1, 1, true, true));
		return ply;
	}


	/**
	 * 多标签中附件
	 * 
	 * @return FPanel
	 */
	// private FPanel getPrjAffixPanel() {
	// String[] columnText = new String[] { "附件名称", "附件文件名称", "附件类型" };
	// String[] columnField = new String[] { "affix_title", "affix_file",
	// "affix_type" };
	// tbaffix.setColumnText(columnText);
	// tbaffix.setColumnField(columnField);
	// tbaffix.setBtn(true);
	// tbaffix.setSelectYear(selectYear);
	// tbaffix.setTableName("RP_PRJ_AXFFIX");// 表名
	// FPanel affix = tbaffix.setBasePanel();// 初始化附件主界面
	// //tbaffix.setButtonStat(false);// 禁止操作按钮
	// tbaffix.setViewAndSave(false);
	// return affix;
	// }
	// 修改操作
	public void doModify()
	{
		if (state == 0)
			return;
		state = 1;// 修改状态
		// tbaffix.setBtn(false);
		// tbaffix.setViewAndSave(true);
		if (tbPrj.getTable().getSelectedRow() < 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目进行修改");
			return;
		}
		else
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
			if (num > 1)
			{
				JOptionPane.showMessageDialog(null, "请选择一条记录进行修改");
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
						JOptionPane.showMessageDialog(null, "请选择记录");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "请选择记录");
					return;
				}
				if (prj_tb.getDsBody() != null && !prj_tb.getDsBody().isEmpty())
				{
					// 设置reportUI可编辑
					try
					{}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				// tbPrj.getDataSet().fieldByName("bj").setValue("this");//做标记
				// 赋值给附件做保存时候使用
				// tbaffix.setChrId(tbPrj.getDataSet().fieldByName("xmxh")
				// .getString());
				// tbaffix.setPrjCode(tbPrj.getDataSet().fieldByName("xmbm")
				// .getString());
				// tbaffix.setEnId(tbPrj.getDataSet().fieldByName("en_id")
				// .getString());
				// tbaffix.setEnCode(tbPrj.getDataSet().fieldByName("div_code")
				// .getString());
				// tbPrj.getDataSet().edit();// 设置可编辑列表
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			BtModPanel mod = new BtModPanel(this);
			Dimension d = new Dimension(900, 530);
			mod.setSize(d);
			mod.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);

			mod.setEnabled(true);
			mod.setVisible(true);
		}
	}


	// 取消操作
	public void doCancel()
	{
		state = -1;
		// tbaffix.setBtn(true);
		setbtn(false, false, false, false);
		this.setButtonStateAn(true, true, true, false, false);
		this.setPanelEnable(false);
		tbPrj.getTable().addMouseListener(a);

		// tbaffix.setBtn(false);// 禁止操作
		try
		{
			this.refreshPrjData();
			// this.emptyPrj();
			// getDefaultInputSetStatePanel(false);
			// 清空项目明细列表
			this.emptyPrjInfo();
			// tbaffix.setChrId(null);
			// tbaffix.tbCancel();// 清空附件列表数据
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 送审操作
	public void doSendToAudit()
	{

		String msg = "";
		try
		{
			int[] rows = tbPrj.getTable().getSelectedRows();
			if (rows.length <= 0)
			{
				JOptionPane.showMessageDialog(null, "请选择要送审的申报项目记录");
				return;
			}
			List xmxhs = new ArrayList();
			String[] prjId = new String[rows.length];
			String[] bmks = getBMKs(rows, tbPrj);
			DataSet ds = tbPrj.getDataSet();
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					prjId[i] = ds.fieldByName("XMXH").getValue().toString();
					Map values = new HashMap();
					values.put("xmxh", tbPrj.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", tbPrj.getDataSet().fieldByName("en_id").getString());
					xmxhs.add(values);
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "您确定送审" + rows.length + "条信息?", "提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					// msg =
					// RportProjectStub.getMethod().startPrjRportFlow("RP_PRJ_MAIN",
					// "XMXH",
					// ds.fieldByName("XMXH").getValue().toString(),GlobalEx.getModuleId());//
					// 进入工作流
					PubInterfaceStub.getMethod().sendAuditInfoByDiv(xmxhs, GlobalEx.user_code, GlobalEx.getCurrRegion(), module_id);
					// PubInterfaceStub.getMethod().startflowInfoByDiv(prjId,
					// GlobalEx.user_code, GlobalEx.getCurrRegion(),
					// Global.getModuleId());

				}
			}
			// msg = RportProjectStub.getMethod().goPrjRportFlow("RP_PRJ_MAIN",
			// "XMXH", valueList, "TONEXT",GlobalEx.getModuleId());
			//			
			if (!"".equals(msg))
			{
				JOptionPane.showMessageDialog(null, msg);
				return;
			}
			refreshPrjData();
			msg = "审核成功";
			JOptionPane.showMessageDialog(null, msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}


	// 撤销送审操作
	public void doBackToAudit()
	{
		if (GlobalEx.isFisVis())
		{
			// 只有单位用户才能操作
			JOptionPane.showMessageDialog(Global.mainFrame, "只有单位用户才用撤消送审");
			return;
		}
		int[] rows = tbPrj.getTable().getSelectedRows();
		if (rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
			return;
		}
		String[] prjcodes = new String[rows.length];

		// 只有没有审核记录或者审核结束吃能有送审资格

		try
		{

			for (int i = 0; i < rows.length; i++)
			{
				String bmk = tbPrj.rowToBookmark(rows[i]);

				if (tbPrj.getDataSet().gotoBookmark(bmk))
					prjcodes[i] = tbPrj.getDataSet().fieldByName("xmxh").getString();

			}
			// 单位可以审核
			PubInterfaceStub.getMethod().BackAuditInfoByDiv(1, prjcodes, GlobalEx.user_code, GlobalEx.getCurrRegion(), GlobalEx.getModuleId());
			// }
			JOptionPane.showMessageDialog(null, "撤消成功");
			this.refreshPrjData();
			if (!GlobalEx.isFisVis())
			{
				// this.zts =
				// PrjInputStub.getMethod().getxmztNum(GlobalEx.user_code,
				// true);
				// f2.setTitle(" 填报项目：" + zts[0] + " 送审项目:" + zts[1] + " 退回项目:"
				// + zts[2] + " 全部项目:" + zts[3]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "撤消失败");

		}
	}


	/**
	 * 树形菜单监听事件
	 */
	private void addTreeListener()
	{
		treeEn.addMouseListener(b);
	}



	MouseAdapter b = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 1)
			{
				if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
				{
					MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
					if ((enPer == null) || "".equals(enPer.getValue())) { return; }
					// if (prj_tb.getCurState() != -1)
					// return;
					try
					{
						if (enPer.getIsLeaf())
							enPer.getValue();
						if ("".equals(refreshPrjData()))
						{
							controlButton();
						}// 显示列表信息
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
	};



	/**
	 * 点击列表行信息触发事件
	 */
	private void addTableListener()
	{
		tbPrj.getTable().addMouseListener(a);
	}



	MouseAdapter a = new MouseAdapter()
	{
		// 单击列表行触发事件
		public void mouseClicked(MouseEvent e)
		{
			// TODO Auto-generated method stub
			if (tbPrj != null)
			{
				if (tbPrj.getTable().getSelectedRow() < 0)
					return;
				else
				{
					controlButton();
					try
					{
						if (!tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(tbPrj.getTable().getSelectedRow())))
							return;
						else
						{
							tbPrj.getDataSet().applyUpdate();
							DataSet ds_table = tbPrj.getDataSet();
							String xmxh = ds_table.fieldByName("XMXH").getString();
							// oper.getOperationState().setPrjCategory(ds_table.fieldByName("C11").getString());
							String step_id = ds_table.fieldByName("STEP_ID").getString();// 未补填
							// or
							// 已补填
							refreshPrjDetailData(step_id, ds_table.fieldByName("XMBM").getString(), ds_table.fieldByName("div_code").getString(), xmxh);// 显示项目明细

							// tbaffix.setSelectYear(selectYear);
							// tbaffix.setChrId(xmxh);
							// tbaffix.refreshPrjData();// 显示项目附件

							// 显示申报理由信息
							String ly = ConstStr.nonNullStr(ds_table.fieldByName("sb_ly").getValue());// 申报理由
							fSbly.setValue(ly);

							// setbtn(false, false, true, false);
						}
					}
					catch (Exception ex)
					{
						ErrorInfo.showErrorDialog(ex, "项目附件列表初始化失败");
					}
				}
			}
		}
	};



	/**
	 * 显示列表数据
	 */
	// public String refreshPrjData()
	// {
	// try
	// {
	//
	// StringBuffer cond = new StringBuffer();
	// cond.append(" AND SET_YEAR = " + selectYear);
	// cond.append(" AND RG_CODE = "+ Global.getCurrRegion());
	// MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
	// if (nodeSel != null) {
	// DataSet ds = treeEn.getDataSet();// 获取预算单位
	// if (treeEn.getSelectedNode() != treeEn.getRoot()) {
	// if (ds != null && !ds.isEmpty() && !ds.bof()
	// && !ds.eof()) {
	// String divCode = ds.fieldByName("div_code")
	// .getString();
	// cond
	// .append(" AND EN_CODE LIKE '" + divCode
	// + "%'");
	// }
	// }
	// }
	// DataSet data=null;
	// String condSql = cond.toString();
	// cond.delete(0, cond.length());
	// this.setButtonStateAn(true, true, true, false, false);
	// data = PrjInputStub.getMethod().getQueryProject(condSql);
	// tbPrj.setDataSet(data);// 查询项目信息
	// tbPrj.reset();
	// }
	// catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// return "";
	// }
	/**
	 * 显示列表数据
	 */
	public String refreshPrjData()
	{
		try
		{

			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			DataSet data = null;
			String divCode = "";
			if (nodeSel != null)
			{
				DataSet ds = treeEn.getDataSet();// 获取预算单位
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						divCode = ds.fieldByName("div_code").getString();
					}
				}
			}
			switch (this.sendState.getSelectedIndex())
			{
				case 0:
					// setButtonStateBn(true);
					// this.setButtonStateAn(true, true, true, false, false);
					data = PrjInputStub.getMethod().getQueryProject(divCode);
					break;
				case 1:
					// setButtonStateBn(false);
					// this.setButtonStateAn(false, false, false, false, false);
					data = PrjInputStub.getMethod().getQueryProject2(divCode);
					break;
			}
			// TODO 查询
			tbPrj.setDataSet(data);// 查询项目信息
			setTableProp();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}


	/**
	 * 显示列表数据
	 */
	// public String refreshPrjData2() {
	// try {
	// StringBuffer cond = new StringBuffer();
	// // cond.append(" AND RP_PRJ_MAIN.SET_YEAR = " + selectYear);
	// // cond.append(" AND RP_PRJ_MAIN.RG_CODE = "
	// // + Global.getCurrRegion());
	// MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
	// DataSet data=null;
	// String condSql="";
	// if (nodeSel != null) {
	// DataSet ds = treeEn.getDataSet();// 获取预算单位
	// if (treeEn.getSelectedNode() != treeEn.getRoot()) {
	// if (ds != null && !ds.isEmpty() && !ds.bof()
	// && !ds.eof()) {
	// String divCode = ds.fieldByName("div_code").getString();
	// // switch (this.sendState.getSelectedIndex()) {
	// // case 0:
	// // // 补填项目
	// // cond.append(" AND EN_CODE LIKE '" + divCode + "%'");
	// // condSql = cond.toString();
	// // cond.delete(0, cond.length());
	// // // data = PrjSyncStub.getMethod().getQueryProject(condSql);
	// // break;
	// // case 1:
	// // // 已补填项目
	// // cond.append(" AND DIV_CODE LIKE '" + divCode + "%'");
	// // condSql = cond.toString();
	// // cond.delete(0, cond.length());
	// // //data= PrjSyncStub.getMethod().getQueryProject2(condSql);
	// // break;
	// // }
	// }
	// }
	// }
	// switch (this.sendState.getSelectedIndex()) {
	// case 0:
	// setButtonStateBn(true);
	// data= PrjSyncStub.getMethod().getQueryProject(condSql);
	// break;
	// case 1:
	// // 已补填项目
	// setButtonStateBn(false);
	// data= PrjSyncStub.getMethod().getQueryProject3(condSql);
	// break;
	// }
	//						
	// // TODO 查询
	// tbPrj.setDataSet(data);// 查询项目信息
	// tbPrj.reset();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return "";
	// }
	/**
	 * 显示项目填报明细信息
	 */
	public void refreshPrjDetailData(String step_id, String xmbm, String div_code, String xmxh)
	{
		try
		{
			DataSet data_detail = null;
			if ("未补填".equals(step_id))
			{
				tpaa.setAuditXmxh(xmxh);
				tpaa.setTableData();
				data_detail = PrjInputStub.getMethod().getPrjTbDetailInfo(selectYear, GlobalEx.getCurrRegion(), xmbm, div_code, xmxh);// 查询项目填报明细
			}
			if ("已补填".equals(step_id))
			{

				tpaa.setAuditXmxh(xmxh);

				tpaa.setTableData();
				data_detail = PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, xmxh, Global.getCurrRegion());
			}
			prj_tb.setDsBody(data_detail);
			prj_tb.refreshReportUI();
			((Report) prj_tb.getReportUI().getReport()).setBodyData(prj_tb.getDsBody());
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
			prj_tb.getReportUI().repaint();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * 清空明细列表
	 */
	private void emptyPrjInfo()
	{
		try
		{
			prj_tb.refreshReportUI();
			prj_tb.setCurState(-1);
			// ((Report) prj_tb.getReportUI().getReport()).setBodyData(TbPrjStub
			// .getMethod().getPrjTbDetailInfo(selectYear,Global.getCurrRegion(),
			// prj_tb.getXmxh(), 0));
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * 设置表单状态
	 */
	private void setPanelEnable(boolean isEnable)
	{
		// cbQueryType.setEnabled(!isEnable);
		cbYear.setEnabled(!isEnable);
		treeEn.setEnabled(!isEnable);
		btnAcct.setEnabled(isEnable);
		tbPrj.setEnabled(!isEnable);
		tbPrj.getTable().setEnabled(!isEnable);
		tfPrjName.setEnabled(isEnable);
		tfPrjName.setEditable(isEnable);
		cbPrjState.setEnabled(isEnable);
		fxmsx.setEnabled(isEnable);
		gkdw.setEnabled(isEnable);
		cbPrjSort.setEnabled(isEnable);
		cbPrjCir.setEnabled(isEnable);
		cbYearBegin.setEnabled(isEnable);
		cbYearEnd.setEnabled(isEnable);
		fSbly.setEnabled(isEnable);
		cbYear.setEnabled(!isEnable);

	}


	/**
	 * 设置工具栏的按钮状态
	 */
	/**
	 * 设置工具栏的按钮状态
	 */

	private void setButtonStateAn(boolean add, boolean mod, boolean del, boolean save, boolean cancel)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("增加".equals(btnGet.getText()))
			{
				btnGet.setEnabled(add);
			}
			if ("修改".equals(btnGet.getText()))
			{
				btnGet.setEnabled(mod);
			}
			if ("删除".equals(btnGet.getText()))
			{
				btnGet.setEnabled(del);
			}
			if ("保存".equals(btnGet.getText()))
			{
				btnGet.setEnabled(save);
			}
			if ("取消".equals(btnGet.getText()))
			{
				btnGet.setEnabled(cancel);
			}
		}
	}


	public void setbtn(boolean add, boolean del, boolean read, boolean mod)
	{
		btnAdd.setEnabled(add);
		btnDel.setEnabled(del);
	}


	/**
	 * 设置工具栏的按钮状态
	 * 
	 * @param aState
	 */
	// public void setButtonStateBn(boolean send)
	// {
	// List controls = null;
	// if (this.getToolbarPanel() != null)
	// {
	// controls = this.getToolbarPanel().getSubControls();
	// }
	// else
	// {
	// return;
	// }
	// for (int i = 0; i < controls.size(); i++)
	// {
	// FButton btnGet = (FButton) controls.get(i);
	// if ("送审".equals(btnGet.getText()))
	// {
	// btnGet.setEnabled(send);
	// }
	// }
	// }
	private String[] getBMKs(int[] rows, CustomTable table)
	{
		String[] bmks = new String[rows.length];
		for (int i = 0; i < rows.length; i++)
		{
			bmks[i] = table.rowToBookmark(rows[i]);
		}
		return bmks;
	}


	public void doImpProject()
	{
		// TODO Auto-generated method stub

	}


	public void backToAuditback()
	{
		// TODO Auto-generated method stub

	}


	public void doBackAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doQueryToAudit()
	{
		// TODO Auto-generated method stub

	}


	// 刷新树
	private void refreshTreeData()
	{
		// TODO Auto-generated method stub
		try
		{
			treeds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
			treeEn.setDataSet(treeds);
			treeEn.reset();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "刷新预算单位出错！");
			return;
		}
	}


	// 控制审核按钮
	public void controlButton()
	{
		if (!selectYear.equals(GlobalEx.loginYear))
		{
			setButtonStateAn(false, false, false, false, false);

		} // else {
		// this.setButtonStateAn(true, true, true, false, false);
		// if (0 == cbQueryType.getSelectedIndex()) {
		// setButtonStateAn(true, true, true, false, false);
		//				
		// } else if (1 == cbQueryType.getSelectedIndex()) {
		// }
		// }

	}


	// /**
	// * 获取项目本年预算总计
	// */
	// public double getBnysTotalPrice() throws Exception {
	// double result = 0;
	// DataSet bodyDS = prj_tb.getDsBody();
	// Report report = prj_tb.getReport();
	// bodyDS.gotoBookmark(report.rowToBookmark(report.getReportHeader().getRows()
	// + 2));
	// String tmp = bodyDS.fieldByName("TOTAL_SUM").getString();
	// if (!Arith.isNumeric(tmp, Arith.NUMBER_PATTERN)) {
	// tmp = "0";
	// }
	// result = Double.parseDouble(tmp);
	// return result;
	// }

	public void readHistory()
	{
		// TODO Auto-generated method stub

	}


	private InfoPackage getMsg()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("项目名称不允许为空");
			tfPrjName.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjCir.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("执行周期不能为空");
			cbPrjCir.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjSort.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("项目类别不能为空");
			cbPrjSort.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjState.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("项目状态不能为空");
			cbPrjState.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(fxmsx.getText())))
		{
			info.setSuccess(false);
			info.setsMessage("项目属性为空,请选择");
			fxmsx.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(gkdw.getText())))
		{
			info.setSuccess(false);
			info.setsMessage("归口单位为空,请选择");
			gkdw.setFocus();
		}
		return info;
	}


	public void dosave()
	{
		// TODO Auto-generated method stub

	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void doUpdatedlg()
	{
		// TODO Auto-generated method stub

	}


	public void doMerger()
	{

		String msg = "";
		try
		{
			int[] rows = tbPrj.getTable().getSelectedRows();
			if (rows.length <= 0)
			{
				JOptionPane.showMessageDialog(null, "请选择合并的指标项目！");
				return;
			}
			else if (rows.length <2)
			{
				JOptionPane.showMessageDialog(null, "合并的指标项目必须多于1条以上！");
				return;
			}
			List xmxhs = new ArrayList();
			String[] prjId = new String[rows.length];
			String[] bmks = getBMKs(rows, tbPrj);
			DataSet ds = tbPrj.getDataSet();
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					prjId[i] = ds.fieldByName("XMXH").getValue().toString();
					Map values = new HashMap();
					values.put("xmxh", tbPrj.getDataSet().fieldByName("xmxh").getString());
					values.put("enid", tbPrj.getDataSet().fieldByName("en_id").getString());
					xmxhs.add(values);
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "您确定合并" + rows.length + "条指标吗?", "提示", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return;
			for (int i = 0; i < bmks.length; i++)
			{
				if (ds.gotoBookmark(bmks[i]))
				{
					// msg =
					// RportProjectStub.getMethod().startPrjRportFlow("RP_PRJ_MAIN",
					// "XMXH",
					// ds.fieldByName("XMXH").getValue().toString(),GlobalEx.getModuleId());//
					// 进入工作流
					PubInterfaceStub.getMethod().sendAuditInfoByDiv(xmxhs, GlobalEx.user_code, GlobalEx.getCurrRegion(), module_id);
					// PubInterfaceStub.getMethod().startflowInfoByDiv(prjId,
					// GlobalEx.user_code, GlobalEx.getCurrRegion(),
					// Global.getModuleId());

				}
			}
			// msg = RportProjectStub.getMethod().goPrjRportFlow("RP_PRJ_MAIN",
			// "XMXH", valueList, "TONEXT",GlobalEx.getModuleId());
			//			
			if (!"".equals(msg))
			{
				JOptionPane.showMessageDialog(null, msg);
				return;
			}
			refreshPrjData();
			msg = "审核成功";
			JOptionPane.showMessageDialog(null, msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
