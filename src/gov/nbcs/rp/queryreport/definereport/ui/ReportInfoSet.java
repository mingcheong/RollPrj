/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomSummaryReportBasicAttr;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.definereport.ibs.RepSetObject;
import gov.nbcs.rp.queryreport.szzbset.ui.SzzbSetI;

import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.reportcy.common.constants.ReportTypeConstants;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportBasicAttr;

/**
 * <p>
 * Title:分组报表定义第一步（报表信息设置)客服端页面
 * </p>
 * <p>
 * Description:分组报表定义第一步（报表信息设置)客服端页面
 * </p>
 * <p>

 */
public class ReportInfoSet extends FTitledPanel {

	private static final long serialVersionUID = 1L;

	// 报表名称
	private FTextField reportNameTxt = null;

	// 报表分类
	private ReportTypeList reportTypeList;

	// 报表用户类型
	private FRadioGroup reportUserTypeGrp = null;

	// private FCheckBox fchkIsMoneyOp = null;

	// 是否进行四舍五入后再汇总
	private FRadioGroup frdoIsMoneyOp = null;

	// // 是否跨年度
	// private FCheckBox fchkIsMulYear = null;
	//
	// // 是否是否跨区域
	// private FCheckBox fchkIsMulRgion = null;

	// 是否启用
	private FCheckBox fchkIsActice = null;

	// 基础信息类
	private SummaryReportBasicAttr summaryReportBasicAttr = null;

	// 报表主表信息
	private RepSetObject repSetObject = null;

	// 报表ID,增加报表传null或"",修改报表传报表ID
	private String sReportId = null;

	// 设计界面类
	private ReportGuideUI reportGuideUI;

	// 货币类型
	private FComboBox fcbxCurrencyUnit;

	// 支持更换支出资金来源数据源
	private FCheckBox fchkChangeSource;

	// 支持更换支出资金来源数据源
	private FCheckBox fchkCompare;

	// 是否显示合计行
	private FCheckBox fchkIsShowTotalRow;

	/**
	 * 构造函数
	 * 
	 * @param summaryReportBasicAttr
	 *            基础信息类型
	 * @param RepSetObject
	 *            报表主表信息
	 * @param sReportId
	 *            报表ID,增加报表传null或"",修改报表传报表ID
	 * 
	 * 
	 */
	public ReportInfoSet(ReportGuideUI reportGuideUI) {
		this.reportGuideUI = reportGuideUI;
		this.sReportId = reportGuideUI.sReportId;
		this.repSetObject = reportGuideUI.repSetObject;
		if (reportGuideUI.querySource != null) {
			if (reportGuideUI.querySource.getReportBasicAttr() instanceof SummaryReportBasicAttr) {
				this.summaryReportBasicAttr = (SummaryReportBasicAttr) reportGuideUI.querySource
						.getReportBasicAttr();
			}
		}
		if (summaryReportBasicAttr == null) {
			summaryReportBasicAttr = new SummaryReportBasicAttr();
			summaryReportBasicAttr
					.setReportType(ReportTypeConstants.REPORT_TYPE_8_RUNTIME_STATIC_REPORT);
		}
		// 界面初始化方法
		jbInit();

		// 判断是增加报表还是修改报表
		if (!Common.isNullStr(sReportId)) {// 修改
			// 显示报表基础信息界面内容
			try {
				showSummaryReportBasicAttr();
			} catch (Exception e) {
				new MessageBox(reportGuideUI, "显示报表基础信息界面内容发生错误，错误信息:"
						+ e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			}
		} else {
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
			((JCheckBox) fchkIsShowTotalRow.getEditor()).setSelected(true);
		}

	}

	/**
	 * 界面初始化方法
	 * 
	 */
	private void jbInit() {

		// 定义报表名称文本框
		reportNameTxt = new FTextField("报表名称：");

		// 报表分类信息
		FPanel fpnlReportType = new FPanel();
		fpnlReportType.setTitle("报表类型：");
		reportTypeList = new ReportTypeList();
		fpnlReportType.setLayout(new RowPreferedLayout(1));
		fpnlReportType.add(reportTypeList, new TableConstraints(1, 1, true,
				true));

		// 定义报表用户类型
		reportUserTypeGrp = new FRadioGroup("", FRadioGroup.HORIZON);
		reportUserTypeGrp.setRefModel("0#仅单位使用 +1#仅财政使用+2#财政单位共同使用");
		reportUserTypeGrp.setValue("2");
		reportUserTypeGrp.setTitleVisible(false);

		// 定义报表用户类型面板
		FPanel reportUserTypePnl = new FPanel();
		reportUserTypePnl.setTitle("报表用户类型");
		reportUserTypePnl.setLayout(new RowPreferedLayout(1));

		// 报表用户类型单选框加入报表用户类型面板
		reportUserTypePnl.addControl(reportUserTypeGrp, new TableConstraints(1,
				1, true, true));

		// 是否启用
		fchkIsActice = new FCheckBox("是否启用");
		fchkIsActice.setTitlePosition("RIGHT");

		fcbxCurrencyUnit = new FComboBox("货币类型：");
		((JComboBox) fcbxCurrencyUnit.getEditor()).setEditable(true);
		fcbxCurrencyUnit.setRefModel("#+元#元+万元#万元");
		fcbxCurrencyUnit.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				if ("万元".equals(arg0.getNewValue())) {
					frdoIsMoneyOp.setEditable(true);
					frdoIsMoneyOp.setValue("0");
				} else {
					frdoIsMoneyOp.setEditable(false);
					frdoIsMoneyOp.setValue("0");
				}

			}
		});

		// 表内容
		FPanel fpnlReportContent = new FPanel();
		fpnlReportContent.setLayout(new RowPreferedLayout(2));
		fpnlReportContent.addControl(fcbxCurrencyUnit, new TableConstraints(1,
				1, false, true));
		fpnlReportContent.addControl(fchkIsActice, new TableConstraints(1, 1,
				false, true));

		frdoIsMoneyOp = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoIsMoneyOp.setRefModel("0#不转换+1#四舍五入后再汇总+2#汇总后再四舍五入");
		frdoIsMoneyOp.setTitleVisible(false);
		frdoIsMoneyOp.setValue("0");
		frdoIsMoneyOp.setEditable(false);

		FPanel isMoneyOpPnl = new FPanel();
		isMoneyOpPnl.setTitle("元转换成万元选择");
		isMoneyOpPnl.setLayout(new RowPreferedLayout(1));
		isMoneyOpPnl.addControl(frdoIsMoneyOp, new TableConstraints(1, 1, true,
				true));

		// // 是否跨年度
		// fchkIsMulYear = new FCheckBox("是否跨年度");
		// fchkIsMulYear.setTitlePosition("RIGHT");
		//
		// // 是否跨区域
		// fchkIsMulRgion = new FCheckBox("是否跨区域");
		// fchkIsMulRgion.setTitlePosition("RIGHT");

		fchkChangeSource = new FCheckBox("支持变换支出资金来源");
		fchkChangeSource.setTitlePosition("RIGHT");
		fchkChangeSource.setProportion(0.14f);

		fchkCompare = new FCheckBox("对比分析表");
		fchkCompare.setTitlePosition("RIGHT");
		fchkCompare.setProportion(0.25f);

		fchkIsShowTotalRow = new FCheckBox("显示总计行");
		fchkIsShowTotalRow.setTitlePosition("RIGHT");
		fchkIsShowTotalRow.setProportion(0.25f);

		FFlowLayoutPanel fpnlChoice = new FFlowLayoutPanel();
		fpnlChoice.setAlignment(FlowLayout.LEFT);
		fpnlChoice
				.add(fchkChangeSource, new TableConstraints(1, 1, true, true));
		fpnlChoice.add(fchkCompare, new TableConstraints(1, 1, true, true));
		fpnlChoice.add(fchkIsShowTotalRow, new TableConstraints(1, 1, true,
				true));

		this.setTopInset(5);
		this.setLeftInset(5);
		// 设置布局
		RowPreferedLayout leftRlay = new RowPreferedLayout(2);
		this.setLayout(leftRlay);

		// 报表名称文本框加入左边显示面板
		this.addControl(reportNameTxt, new TableConstraints(1, 1, false, true));

		// // 是否跨年度选择框加入左边显示面板
		// this.addControl(fchkIsMulYear, new TableConstraints(1, 1, false,
		// true));
		// // 是否是否跨区域选择框加入左边显示面板
		// this
		// .addControl(fchkIsMulRgion, new TableConstraints(1, 1, false,
		// true));

		// 定义报表类型面板加入左边显示面板
		this
				.addControl(fpnlReportType, new TableConstraints(5, 1, false,
						true));
		// 定义报表用户类型面板加入左边显示面板
		this.addControl(reportUserTypePnl, new TableConstraints(2, 1, false,
				true));

		// 是否启用，货币类型
		this.addControl(fpnlReportContent, new TableConstraints(1, 1, false,
				true));

		// 是否进行四舍五入后再汇总选择框加入左边显示面板
		this.addControl(isMoneyOpPnl, new TableConstraints(2, 1, false, true));

		this.addControl(fpnlChoice, new TableConstraints(1, 1, false, true));

	}

	/**
	 * 保存报表基础信息
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	RepSetObject getReportBasicInfo() throws NumberFormatException, Exception {
		// 判断是增加还是修改报表,增加生成Report_Id,修改使用原reportid
		if (!Common.isNullStr(sReportId)) {// 修改
			summaryReportBasicAttr.setReportID(sReportId);
			repSetObject.setREPORT_ID(sReportId);
		} else {// 增加
			String sNewReportId = SzzbSetI.getMethod().getMaxCode("REPORT_ID");

			summaryReportBasicAttr.setReportID(sNewReportId);
			repSetObject.setREPORT_ID(sNewReportId);

		}
		// 根据界面填写信息,保存基础信息类信息
		saveSummaryReportBasicAttr();
		// 保存查询表主表信息
		saveReportRepsetInfo();
		sReportId = repSetObject.getREPORT_ID();
		return this.repSetObject;
	}

	/**
	 * 填写检查
	 * 
	 * @return
	 */
	String check() {
		if (Common.isNullStr(reportNameTxt.getValue().toString())) {
			reportNameTxt.setFocus();
			return "请填写报表名称!";
		}

		List lstType = reportTypeList.getSelectData();
		if (lstType.size() == 0) {
			return "请选择报表类型!";
		}
		if (Common.isNullStr(fcbxCurrencyUnit.getText().toString().trim())) {
			return "请选择或填写货币类型!";
		}

		return "";
	}

	/**
	 * 根据界面填写信息,保存基础信息类信息
	 * 
	 */
	private void saveSummaryReportBasicAttr() {
		if (!(summaryReportBasicAttr instanceof ICustomSummaryReportBasicAttr)) {
			summaryReportBasicAttr = new MySummaryReportBasicAttr();
		}
		// 报表名称
		String sReportName = reportNameTxt.getValue().toString();
		summaryReportBasicAttr.setReportName(sReportName);
		summaryReportBasicAttr.setMode(1);
		// 当前为第几面
		summaryReportBasicAttr.setIntPageIndex(1);
		// 每面显示行数
		summaryReportBasicAttr.setPageCount(10000);
		// 适用子系统
		// summaryReportBasicAttr.setSysIDArray(new String[] { "101" });
		// 是否进行四舍五入后再汇总
		summaryReportBasicAttr.setMoneyOp(Integer.parseInt(frdoIsMoneyOp
				.getValue().toString()));

		// // 是否跨年度
		// int isMulYear = ("false".equals(fchkIsMulYear.getValue().toString())
		// ? 0
		// : 1);
		summaryReportBasicAttr.setIsMulYear(0);

		// 是否跨区域
		// int isMulRgion =
		// ("false".equals(fchkIsMulRgion.getValue().toString()) ? 0
		// : 1);
		summaryReportBasicAttr.setIsMulRgion(0);

		// 是否显示合计行
		((ICustomSummaryReportBasicAttr) summaryReportBasicAttr)
				.setIsShowTotalRow(Common.estimate(this.fchkIsShowTotalRow
						.getValue()) ? "1" : "0");

		reportGuideUI.querySource.setReportBasicAttr(summaryReportBasicAttr);
	}

	/**
	 * 保存查询表主表信息
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	private void saveReportRepsetInfo() throws NumberFormatException, Exception {
		// 报表名称
		repSetObject.setREPORT_CNAME(reportNameTxt.getValue().toString());

		if (Common.isNullStr(sReportId)) {// 增加
			String sLvl = "";
		}

		// 报表用户类型
		repSetObject.setDATA_USER(Integer.parseInt(reportUserTypeGrp.getValue()
				.toString()));

		// 报表类型(封面、分组报表等）
		repSetObject.setTYPE_FLAG(IDefineReport.REPORTTYPE_GROUP);

		// // 是否进行四舍五入后再汇总
		// repSetObject.setIS_MONEYOP(Integer.parseInt(frdoIsMoneyOp.getValue()
		// .toString()));

		// 是否跨年度
		// repSetObject.setIS_MULYEAR(("false".equals(fchkIsMulYear.getValue()
		// .toString()) ? 0 : 1));
		//
		// // 是否是否跨区域
		// repSetObject.setIS_MULRGION(("false".equals(fchkIsMulRgion.getValue()
		// .toString()) ? 0 : 1));

		// 是否启用
		repSetObject.setIS_ACTIVE(("false".equals(fchkIsActice.getValue()
				.toString()) ? "否" : "是"));
		// 货币类型
		repSetObject.setCURRENCYUNIT(fcbxCurrencyUnit.getText().toString());

		// 支出更换支出资金来源
		repSetObject.setFUNDSOURCE_FLAG(("false".equals(fchkChangeSource
				.getValue().toString()) ? "0" : "1"));

		// 对比分析表
		repSetObject.setCOMPARE_FLAG(("false".equals(this.fchkCompare
				.getValue().toString()) ? "0" : "1"));
	}

	/**
	 * 根据基础信息类信息，显示报表基础信息界面内容
	 * 
	 * @throws Exception
	 * 
	 */
	private void showSummaryReportBasicAttr() throws Exception {

		// 报表名称
		String sReportName = repSetObject.getREPORT_CNAME();
		reportNameTxt.setValue(sReportName);

		// 货币类型
		fcbxCurrencyUnit.setText(repSetObject.getCURRENCYUNIT());

		// 是否进行四舍五入后再汇总
		frdoIsMoneyOp.setValue(String.valueOf(summaryReportBasicAttr
				.getMoneyOp()));

		// // 是否跨年度
		// int isMulYear = repSetObject.getIS_MULYEAR();
		// ((JCheckBox) fchkIsMulYear.getEditor()).setSelected(Common
		// .estimate(new Integer(isMulYear)));
		// // 是否跨区域
		// int isMulRgion = repSetObject.getIS_MULRGION();
		// ((JCheckBox) fchkIsMulRgion.getEditor()).setSelected(Common
		// .estimate(new Integer(isMulRgion)));

		// 是否否启
		String sIsActive = repSetObject.getIS_ACTIVE();
		if ("是".equals(sIsActive))
			((JCheckBox) fchkIsActice.getEditor()).setSelected(true);
		else
			((JCheckBox) fchkIsActice.getEditor()).setSelected(false);

		// 定义报表分类
		reportTypeList.setSelected(sReportId);
		// 定义报表用户类型
		String sUserType = String.valueOf(repSetObject.getDATA_USER());
		reportUserTypeGrp.setValue(sUserType);

		// 支出更换支出资金来源
		((JCheckBox) fchkChangeSource.getEditor()).setSelected(Common
				.estimate(repSetObject.getFUNDSOURCE_FLAG()));

		// 对比分析表
		((JCheckBox) fchkCompare.getEditor()).setSelected(Common
				.estimate(repSetObject.getCOMPARE_FLAG()));

		// 是否显示合计行
		if (summaryReportBasicAttr instanceof ICustomSummaryReportBasicAttr) {
			((JCheckBox) fchkIsShowTotalRow.getEditor())
					.setSelected(Common
							.estimate(((ICustomSummaryReportBasicAttr) summaryReportBasicAttr)
									.getIsShowTotalRow()));
		} else {
			((JCheckBox) fchkIsShowTotalRow.getEditor()).setSelected(true);
		}

	}

	List getType() {
		return reportTypeList.getSelectData();
	}
}
