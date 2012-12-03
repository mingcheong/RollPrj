/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultTreeModel;

import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title: 查询报表导出模块
 * </p>
 * <p>
 * Description:查询报表导出模块
 * </p>

 */
public class ExportExcelUI extends FModulePanel {

	private static final long serialVersionUID = 1L;

	// 数据库联接
	private IQrBudget qrBudgetServ;

	private CustomTree ftreDivName;// 单位信息

	private CustomTree ftreReportName;// 查询表

	// 报表类型
	public String reporttype;

	private int iUserType;// 用户类型：1：业务处室，单位

	// 文件生成类型单选框
	private FRadioGroup frdoFileType = null;

	// 导出类型单选框
	private FRadioGroup frdoExportType = null;

	// 工具栏
	private ToolBarPanel fpnlToolBar;

	public void initize() {
		// 数据库联接
		qrBudgetServ = QrBudgetI.getMethod();

		// 定义文件生成类型面板
		FPanel fpnlFileType = new FPanel();
		fpnlFileType.setLayout(new RowPreferedLayout(1));
		fpnlFileType.setTitle("选择生成文件类型");
		fpnlFileType.setFontSize(this.getFont().getSize());
		fpnlFileType.setFontName(this.getFont().getName());
		fpnlFileType.setTitledBorder();

		// 定义数据来源单选框
		frdoFileType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoFileType.setRefModel("0#按单位名称生成文件 +1#按报表名称生成文件 ");
		fpnlFileType.addControl(frdoFileType, new TableConstraints(1, 1, true));
		frdoFileType.setValue("0");

		// 定义导出类型面板
		FPanel fpnlExportType = new FPanel();
		fpnlExportType.setLayout(new RowPreferedLayout(1));
		fpnlExportType.setTitle("选择导出类型");
		fpnlExportType.setFontSize(this.getFont().getSize());
		fpnlExportType.setFontName(this.getFont().getName());
		fpnlExportType.setTitledBorder();

		frdoExportType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoExportType.setRefModel("0#按业务处室导出+1#主管局导出    +2#按基层单位导出");
		fpnlExportType.addControl(frdoExportType, new TableConstraints(1, 1,
				true));
		frdoExportType.setValue("0");
		// 1:业务处室,其他单位
		iUserType = UntPub.FIS_VIS.equals(GlobalEx.getBelongType()) ? 1 : 0;

		// 设置分栏
		FSplitPane fSplitPane = new FSplitPane();
		fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		fSplitPane
				.setDividerLocation(this.getToolkit().getScreenSize().width / 2);

		try {

			ReportNamePanel reportNamePanel = new ReportNamePanel();
			DivNamePanel DivNamePanel = new DivNamePanel();
			fSplitPane.addControl(DivNamePanel);
			fSplitPane.addControl(reportNamePanel);
			modulePanelActivedLoad();

			// 工具栏
			this.createToolBar();
			FToolBarPanel fToolBarPanel = this.getToolbarPanel();
			fpnlToolBar = new ToolBarPanel();
			fToolBarPanel.addSeparator();
			fToolBarPanel.addControl(fpnlToolBar);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "查询表导出界面发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(2));
		fpnlMain.addControl(fpnlExportType, new TableConstraints(2, 1, false,
				true));
		fpnlMain.addControl(fpnlFileType, new TableConstraints(2, 1, false,
				true));
		fpnlMain.addControl(fSplitPane, new TableConstraints(1, 2, true, true));
		this.add(fpnlMain);

	}

	/**
	 * 加载数据
	 * 
	 */
	private void modulePanelActivedLoad() {
		try {
			// 设置部门预算查询表树信息
			DataSet dsReportName = qrBudgetServ.getReportName(iUserType,
					Global.loginYear, this.getreporttype());

			ftreReportName.setDataSet(dsReportName);
			ftreReportName.reset();

			// 设置单位信息树信息
			int iLevel = 4;
			DataSet dsDivName = QrBudgetI.getMethod().getDivName(
					Global.loginYear, iLevel, iUserType);
			ftreDivName.setDataSet(dsDivName);
			ftreDivName.reset();
			if (UntPub.FIS_VIS.equals(GlobalEx.getBelongType())) {
				//QrBudget.expandTo(ftreDivName);
				ftreDivName.updateUI();

			} else {

				ftreDivName.expandAll();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "部门预算查询表加载数据发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * 部门预算查询表
	 * 
	 * @author qzc
	 * 
	 */
	private class ReportNamePanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public ReportNamePanel() throws Exception {
			super();
			this.setTitle("部门预算查询表");
			this.setLayout(new RowPreferedLayout(1));

			ftreReportName = new CustomTree("查询表", null,
					IDefineReport.SHOW_LVL, IQrBudget.REPORT_CNAME,
					IDefineReport.PAR_ID, null, IQrBudget.LVL_ID, true);
			((DefaultTreeModel) ftreReportName.getModel())
					.setAsksAllowsChildren(true);

			ftreReportName.setIsCheckBoxEnabled(true);
			FScrollPane fspnlReportName = new FScrollPane();
			fspnlReportName.addControl(ftreReportName);
			this.addControl(fspnlReportName, new TableConstraints(1, 1, true,
					true));
		}
	}

	/**
	 * 单位信息Panel
	 * 
	 * @author qzc
	 * 
	 */
	private class DivNamePanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public DivNamePanel() throws Exception {
			super();
			this.setTitle("单位列表");
			this.setLayout(new RowPreferedLayout(1));

			ftreDivName = new CustomTree("所有", null, IQrBudget.EN_ID,
					IQrBudget.CODE_NAME, IQrBudget.PARENT_ID, null,
					IQrBudget.DIV_CODE, true);
			// 用户类型：1:业务处室,其他单位
			ftreDivName.setIsCheckBoxEnabled(true);
			FScrollPane fspnlDivName = new FScrollPane();
			fspnlDivName.addControl(ftreDivName);

			this.addControl(fspnlDivName,
					new TableConstraints(1, 1, true, true));
		}
	}

	/**
	 * 工具栏上加载的Panel
	 * 
	 * @author qzc
	 * 
	 */
	public class ToolBarPanel extends FPanel {

		private static final long serialVersionUID = 1L;

		FLabel flblEmpty1;

		FLabel flblEmpty2;

		FLabel flblEmpty3;

		FLabel flblDataType;

		JComboBox cbxDataType;// 数据类型

		FLabel flblDataVer; // 数据版本

		JComboBox cbxDataVer;

		List lstID;// 数据类型ID

		List lstVer = null;// 查询版本

		DataSet dsDataTypeList; // 数据类型DataSet

		// 报表内容为空时不导出
		JCheckBox fchkIsNotExport;

		public ToolBarPanel() throws Exception {
			super();
			// 类据类型
			flblDataType = new FLabel();
			flblDataType.setTitle("数据类型");
			cbxDataType = new JComboBox();

			// 查询版本
			flblDataVer = new FLabel();
			flblDataVer.setTitle("查询时点：");
			cbxDataVer = new JComboBox();

			FLabel flblEmpty = new FLabel();
			flblEmpty.setTitle("   ");
			flblEmpty1 = new FLabel();
			flblEmpty1.setTitle("   ");
			flblEmpty2 = new FLabel();
			flblEmpty2.setTitle("   ");
			flblEmpty3 = new FLabel();
			flblEmpty3.setTitle("   ");
			// 报表内容为空时不导出
			fchkIsNotExport = new JCheckBox("报表内容为空时不导出");
			// fchkIsNotExport.setTitlePosition("RIGHT");
			// 查询类型0：表示江苏和江苏相同的地区 ，1：表示宁波
			if (qrBudgetServ.getSearchType(Global.loginYear) == 0) {
				fchkIsNotExport.setSelected(false);
				fchkIsNotExport.setVisible(false);
			} else {
				fchkIsNotExport.setSelected(true);
				fchkIsNotExport.setVisible(true);
			}

			FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT, 0, 10);
			this.setLayout(fLayout);
			this.addControl(flblEmpty1);
			this.addControl(flblDataType);
			this.add(cbxDataType);
			this.addControl(flblEmpty2);
			this.addControl(flblDataVer);
			this.add(cbxDataVer);
			this.add(fchkIsNotExport);
			initData();
		}

		/**
		 * 初始化数据
		 * 
		 * @throws Exception
		 */
		private void initData() throws Exception {
			dsDataTypeList = getOptDataTypeList();
			lstID = new ArrayList();
			dsDataTypeList.beforeFirst();
			while (dsDataTypeList.next()) {
				lstID.add(new Integer(dsDataTypeList.fieldByName(IQrBudget.ID)
						.getInteger()));
				cbxDataType.addItem(dsDataTypeList.fieldByName(IQrBudget.NAME)
						.getString());
			}
			if (lstID.size() > 0) {
				IPubInterface iPubInterface = PubInterfaceStub.getMethod();
				if (GlobalEx.getBelongType().equals(UntPub.FIS_VIS)) {// 财政用户
					int iCurBatchNo = 0;
					//					int iCurBatchNo = iPubInterface.getCurBatchNO();
					String sID = getTypeID(dsDataTypeList, iCurBatchNo, 1);
					if (sID == null) {
						cbxDataType.setSelectedIndex(-1);
					} else {
						cbxDataType.setSelectedIndex(lstID.indexOf(new Integer(
								sID)));
					}
				} else {// 单位用户
					// 判断是不是人大批复
//					int iCurBatchNo = iPubInterface.getCurBatchNO();
					int iCurBatchNo = 0;
					String sID;
//					if (iPubInterface.getCurStateKind() == 2) {
//						sID = getTypeID(dsDataTypeList, iCurBatchNo, 1);
//					} else {
						sID = getTypeID(dsDataTypeList, iCurBatchNo, 0);
//					}
					if (sID == null) {
						cbxDataType.setSelectedIndex(-1);
					} else {
						cbxDataType.setSelectedIndex(lstID.indexOf(new Integer(
								sID)));
					}
				}
			}

			// chcx add 查询版本,财政用户显示版本信息
			if (iUserType == 1) {
				flblEmpty3.setVisible(true);
				flblDataVer.setVisible(true);
				cbxDataVer.setVisible(true);
				lstVer = new ArrayList();
				cbxDataVer.addItem("实时");
				lstVer.add(new Integer(0));
				cbxDataVer.setSelectedIndex(0);
			} else {
				flblEmpty3.setVisible(false);
				flblDataVer.setVisible(false);
				cbxDataVer.setVisible(false);
			}
		}

		private String getTypeID(DataSet ds, int iBatchNo, int iDataType)
				throws Exception {
			ds.beforeFirst();
			while (dsDataTypeList.next()) {
				if (dsDataTypeList.fieldByName("BatchNo").getInteger() == iBatchNo
						&& dsDataTypeList.fieldByName("DataType").getInteger() == iDataType) {
					return dsDataTypeList.fieldByName(IQrBudget.ID).getString();
				}
			}
			return null;
		}

		/**
		 * 得到数据类型值
		 * 
		 * @return
		 * @throws Exception
		 */
		public Map getDataTypeInfo() throws Exception {
			int iSelectIndex = cbxDataType.getSelectedIndex();
			Object sID = lstID.get(iSelectIndex);
			if (dsDataTypeList.locate(IQrBudget.ID, sID)) {
				return dsDataTypeList.getOriginData();
			}
			return null;
		}

		public String getVerNo() {
			int iSelectIndex = cbxDataVer.getSelectedIndex();
			if (iSelectIndex == 0 || iSelectIndex < 0)
				return "0";
			else
				return lstVer.get(iSelectIndex).toString();
		}

		public JComboBox getCbxDataType() {
			return cbxDataType;
		}

		public List getLstID() {
			return lstID;
		}

		public List getLstVer() {
			return lstVer;
		}

		/**
		 * 得到数据类型
		 * 
		 * @throws Exception
		 */
		private DataSet getOptDataTypeList() throws Exception {
			IPubInterface iPubInterface = PubInterfaceStub.getMethod();
//			int iBatchNo = iPubInterface.getCurBatchNO();
			int iBatchNo =  0;
			if (iBatchNo > 10) {
				JOptionPane.showMessageDialog(this, "批次超出合理范围。", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
//			return iPubInterface.getOptDataTypeList();
			return null;
		}

		/**
		 * 得到查询条件
		 * 
		 * @return
		 * @throws Exception
		 */
		public InfoPackage getFilter() throws Exception {
			InfoPackage infoPackage = new InfoPackage();
			if (this.getCbxDataType().getSelectedIndex() < 0) {
				infoPackage.setsMessage("请选择数据类型。");
				infoPackage.setSuccess(false);
				return infoPackage;
			}

			Map dataTypeInfo = this.getDataTypeInfo();
			if (dataTypeInfo.get("BatchNO") == null) {
				infoPackage.setsMessage("取数错误,取得批次值为空。");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
			int iBatchNO = Integer.parseInt(dataTypeInfo.get("BatchNO")
					.toString());
			String sStatusWhere = " AND Batch_No= " + String.valueOf(iBatchNO);

			if (dataTypeInfo.get("DataType") == null) {
				infoPackage.setsMessage("取数错误,取得数据类型值为空。");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
			int iDataType = Integer.parseInt(dataTypeInfo.get("DataType")
					.toString());
			sStatusWhere = sStatusWhere + " AND Data_Type="
					+ String.valueOf(iDataType);

			infoPackage.setsMessage(sStatusWhere);
			infoPackage.setSuccess(true);
			return infoPackage;
		}

		public boolean getIsNotExportWhenEmpty() {
			return fchkIsNotExport.isSelected();
		}
	}

	public String getreporttype() {
		if (reporttype == null) {
			reporttype = "50";
		}
		return reporttype;
	}

	public void setreporttype(String reporttype) {
		this.reporttype = reporttype;
	}

	public FRadioGroup getFrdoFileType() {
		return frdoFileType;
	}

	public void setFrdoFileType(FRadioGroup frdoFileType) {
		this.frdoFileType = frdoFileType;
	}

	public FRadioGroup getFrdoExportType() {
		return frdoExportType;
	}

	public CustomTree getFtreDivName() {
		return ftreDivName;
	}

	public CustomTree getFtreReportName() {
		return ftreReportName;
	}

	public IQrBudget getQrBudgetServ() {
		return qrBudgetServ;
	}

	public int getIUserType() {
		return iUserType;
	}

	public void setIUserType(int userType) {
		iUserType = userType;
	}

	public ToolBarPanel getFpnlToolBar() {
		return fpnlToolBar;
	}

}
