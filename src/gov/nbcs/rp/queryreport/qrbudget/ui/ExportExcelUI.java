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
 * Title: ��ѯ������ģ��
 * </p>
 * <p>
 * Description:��ѯ������ģ��
 * </p>

 */
public class ExportExcelUI extends FModulePanel {

	private static final long serialVersionUID = 1L;

	// ���ݿ�����
	private IQrBudget qrBudgetServ;

	private CustomTree ftreDivName;// ��λ��Ϣ

	private CustomTree ftreReportName;// ��ѯ��

	// ��������
	public String reporttype;

	private int iUserType;// �û����ͣ�1��ҵ���ң���λ

	// �ļ��������͵�ѡ��
	private FRadioGroup frdoFileType = null;

	// �������͵�ѡ��
	private FRadioGroup frdoExportType = null;

	// ������
	private ToolBarPanel fpnlToolBar;

	public void initize() {
		// ���ݿ�����
		qrBudgetServ = QrBudgetI.getMethod();

		// �����ļ������������
		FPanel fpnlFileType = new FPanel();
		fpnlFileType.setLayout(new RowPreferedLayout(1));
		fpnlFileType.setTitle("ѡ�������ļ�����");
		fpnlFileType.setFontSize(this.getFont().getSize());
		fpnlFileType.setFontName(this.getFont().getName());
		fpnlFileType.setTitledBorder();

		// ����������Դ��ѡ��
		frdoFileType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoFileType.setRefModel("0#����λ���������ļ� +1#���������������ļ� ");
		fpnlFileType.addControl(frdoFileType, new TableConstraints(1, 1, true));
		frdoFileType.setValue("0");

		// ���嵼���������
		FPanel fpnlExportType = new FPanel();
		fpnlExportType.setLayout(new RowPreferedLayout(1));
		fpnlExportType.setTitle("ѡ�񵼳�����");
		fpnlExportType.setFontSize(this.getFont().getSize());
		fpnlExportType.setFontName(this.getFont().getName());
		fpnlExportType.setTitledBorder();

		frdoExportType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoExportType.setRefModel("0#��ҵ���ҵ���+1#���ֵܾ���    +2#�����㵥λ����");
		fpnlExportType.addControl(frdoExportType, new TableConstraints(1, 1,
				true));
		frdoExportType.setValue("0");
		// 1:ҵ����,������λ
		iUserType = UntPub.FIS_VIS.equals(GlobalEx.getBelongType()) ? 1 : 0;

		// ���÷���
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

			// ������
			this.createToolBar();
			FToolBarPanel fToolBarPanel = this.getToolbarPanel();
			fpnlToolBar = new ToolBarPanel();
			fToolBarPanel.addSeparator();
			fToolBarPanel.addControl(fpnlToolBar);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ѯ�������淢�����󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
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
	 * ��������
	 * 
	 */
	private void modulePanelActivedLoad() {
		try {
			// ���ò���Ԥ���ѯ������Ϣ
			DataSet dsReportName = qrBudgetServ.getReportName(iUserType,
					Global.loginYear, this.getreporttype());

			ftreReportName.setDataSet(dsReportName);
			ftreReportName.reset();

			// ���õ�λ��Ϣ����Ϣ
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
			JOptionPane.showMessageDialog(this, "����Ԥ���ѯ��������ݷ������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * ����Ԥ���ѯ��
	 * 
	 * @author qzc
	 * 
	 */
	private class ReportNamePanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public ReportNamePanel() throws Exception {
			super();
			this.setTitle("����Ԥ���ѯ��");
			this.setLayout(new RowPreferedLayout(1));

			ftreReportName = new CustomTree("��ѯ��", null,
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
	 * ��λ��ϢPanel
	 * 
	 * @author qzc
	 * 
	 */
	private class DivNamePanel extends FPanel {

		private static final long serialVersionUID = 1L;

		public DivNamePanel() throws Exception {
			super();
			this.setTitle("��λ�б�");
			this.setLayout(new RowPreferedLayout(1));

			ftreDivName = new CustomTree("����", null, IQrBudget.EN_ID,
					IQrBudget.CODE_NAME, IQrBudget.PARENT_ID, null,
					IQrBudget.DIV_CODE, true);
			// �û����ͣ�1:ҵ����,������λ
			ftreDivName.setIsCheckBoxEnabled(true);
			FScrollPane fspnlDivName = new FScrollPane();
			fspnlDivName.addControl(ftreDivName);

			this.addControl(fspnlDivName,
					new TableConstraints(1, 1, true, true));
		}
	}

	/**
	 * �������ϼ��ص�Panel
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

		JComboBox cbxDataType;// ��������

		FLabel flblDataVer; // ���ݰ汾

		JComboBox cbxDataVer;

		List lstID;// ��������ID

		List lstVer = null;// ��ѯ�汾

		DataSet dsDataTypeList; // ��������DataSet

		// ��������Ϊ��ʱ������
		JCheckBox fchkIsNotExport;

		public ToolBarPanel() throws Exception {
			super();
			// �������
			flblDataType = new FLabel();
			flblDataType.setTitle("��������");
			cbxDataType = new JComboBox();

			// ��ѯ�汾
			flblDataVer = new FLabel();
			flblDataVer.setTitle("��ѯʱ�㣺");
			cbxDataVer = new JComboBox();

			FLabel flblEmpty = new FLabel();
			flblEmpty.setTitle("   ");
			flblEmpty1 = new FLabel();
			flblEmpty1.setTitle("   ");
			flblEmpty2 = new FLabel();
			flblEmpty2.setTitle("   ");
			flblEmpty3 = new FLabel();
			flblEmpty3.setTitle("   ");
			// ��������Ϊ��ʱ������
			fchkIsNotExport = new JCheckBox("��������Ϊ��ʱ������");
			// fchkIsNotExport.setTitlePosition("RIGHT");
			// ��ѯ����0����ʾ���պͽ�����ͬ�ĵ��� ��1����ʾ����
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
		 * ��ʼ������
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
				if (GlobalEx.getBelongType().equals(UntPub.FIS_VIS)) {// �����û�
					int iCurBatchNo = 0;
					//					int iCurBatchNo = iPubInterface.getCurBatchNO();
					String sID = getTypeID(dsDataTypeList, iCurBatchNo, 1);
					if (sID == null) {
						cbxDataType.setSelectedIndex(-1);
					} else {
						cbxDataType.setSelectedIndex(lstID.indexOf(new Integer(
								sID)));
					}
				} else {// ��λ�û�
					// �ж��ǲ����˴�����
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

			// chcx add ��ѯ�汾,�����û���ʾ�汾��Ϣ
			if (iUserType == 1) {
				flblEmpty3.setVisible(true);
				flblDataVer.setVisible(true);
				cbxDataVer.setVisible(true);
				lstVer = new ArrayList();
				cbxDataVer.addItem("ʵʱ");
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
		 * �õ���������ֵ
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
		 * �õ���������
		 * 
		 * @throws Exception
		 */
		private DataSet getOptDataTypeList() throws Exception {
			IPubInterface iPubInterface = PubInterfaceStub.getMethod();
//			int iBatchNo = iPubInterface.getCurBatchNO();
			int iBatchNo =  0;
			if (iBatchNo > 10) {
				JOptionPane.showMessageDialog(this, "���γ�������Χ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
//			return iPubInterface.getOptDataTypeList();
			return null;
		}

		/**
		 * �õ���ѯ����
		 * 
		 * @return
		 * @throws Exception
		 */
		public InfoPackage getFilter() throws Exception {
			InfoPackage infoPackage = new InfoPackage();
			if (this.getCbxDataType().getSelectedIndex() < 0) {
				infoPackage.setsMessage("��ѡ���������͡�");
				infoPackage.setSuccess(false);
				return infoPackage;
			}

			Map dataTypeInfo = this.getDataTypeInfo();
			if (dataTypeInfo.get("BatchNO") == null) {
				infoPackage.setsMessage("ȡ������,ȡ������ֵΪ�ա�");
				infoPackage.setSuccess(false);
				return infoPackage;
			}
			int iBatchNO = Integer.parseInt(dataTypeInfo.get("BatchNO")
					.toString());
			String sStatusWhere = " AND Batch_No= " + String.valueOf(iBatchNO);

			if (dataTypeInfo.get("DataType") == null) {
				infoPackage.setsMessage("ȡ������,ȡ����������ֵΪ�ա�");
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
