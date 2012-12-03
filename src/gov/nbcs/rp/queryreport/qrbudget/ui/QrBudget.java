
package gov.nbcs.rp.queryreport.qrbudget.ui;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.queryreport.qrbudget.action.ReadWriteFile;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.fr.report.PaperSize;

/**
 * ԭʼ��ʽ��ѯ������ӡ����
 * 
 * @author qzc
 * 
 */
public class QrBudget extends FModulePanel {

	public static final int TYPE_NORMAL = 0;

	public static final int TYPE_SZZB = 1;

	public static final int TYPE_ROWSET = 2;

	public static final int TYPE_COVER = 3;

	public static final int TYPE_GROUP = 4;

	public static final int USER_CZ = 1;// �����û�

	public static final int USER_DW = 0;// ��λ�û�

	private static final long serialVersionUID = 1L;

	// ���ݿ�����
	private IQrBudget qrBudgetServ;

	private CustomTree ftreReportName;// ��ѯ��

	private CustomTree ftreDivName;// ��λ��Ϣ

	private ReportUI reportUI; // ����

	private String sFieldSelect; // ȡ�õ��е�EName��Ϣ

	private DataSet dsReportHeader;// ��ͷDataSet

	private Node nodeHeader; // �����ͷNode

	private List lstColInfo; // ��������Ϣ�������ͣ�����ʾ��ʽ)

	private int iUserType;// �û����ͣ�1��ҵ���ң���λ

	// ��������
	public String reporttype;

	// �ұ�Tabҳ��
	private ReportTabbedPane tabPaneReport;

	// ��������ѯ���
	private List lstResult;

	// ���Ŀ¼������Ϣ
	private DataSet dsSzzb;

	// ������
	private ToolBarPanel fpnlToolBar = null;

	// ÿ���һ�β�ѯ��ť�������ɵ�UUID
	private String UUID;

	// ��ʾ����������ʾ��Ϣ
	private FLabel flblInfo;

	// ��ʾ������ܱ�����ⱨ�����ݱ�
	private FTable ftabReport;

	// ����ԭ����ID
	private String sOldReportID = null;

	// ��������
	public String prjtype;

	public void initize() {
		try {
			// ���ݿ�����
			qrBudgetServ = QrBudgetI.getMethod();

			// 1:ҵ����,������λ
			iUserType = UntPub.FIS_VIS.equals(GlobalEx.getBelongType()) ? 1 : 0;

			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(300);
			this.add(fSplitPane);

			// �����Ϣ
			FSplitPane fSplitPaneLeft = new FSplitPane();
			fSplitPaneLeft.setOrientation(FSplitPane.VERTICAL_SPLIT);
			fSplitPaneLeft.setDividerLocation(240);
			fSplitPane.addControl(fSplitPaneLeft);

			// ����Ԥ���ѯ��
			FPanel fpnlReportName = new ReportNamePanel();

			// ��λ�б�
			FPanel fpnlDivName = new DivNamePanel();
			// �������
			this.addAncestorListener(new AncestorListener() {

				public void ancestorAdded(AncestorEvent event) {
				}

				public void ancestorMoved(AncestorEvent event) {
				}

				public void ancestorRemoved(AncestorEvent event) {
					try {
						tabPaneReport.treatSearchObj();
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(QrBudget.this,
								"�˳��������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}// �����ѯ��
				}

			});

			fSplitPaneLeft.addControl(fpnlReportName);
			fSplitPaneLeft.addControl(fpnlDivName);

			fpnlReportName.setBorder(null);
			fpnlDivName.setBorder(null);

			// ���屨��
			Report curReport = new Report();
			reportUI = new ReportUI(curReport);
			reportUI.getReport().getReportSettings().setPaperSize(
					new PaperSize(2000, 3000));
			reportUI.setColumnEndless(false);
			reportUI.setRowEndless(false);
			reportUI.clearColumnHeaderContent();
			reportUI.clearRowHeaderContent();

			tabPaneReport = new ReportTabbedPane(this);
			flblInfo = new FLabel();
			flblInfo.setTitle("��Ϣ:");
			FPanel fpnlRight = new FPanel();
			fpnlToolBar = new ToolBarPanel();
			fpnlRight.setLayout(new RowPreferedLayout(1));
//			fpnlRight.addControl(fpnlToolBar, new TableConstraints(1, 1, false,
//					true));
			fpnlRight.addControl(tabPaneReport, new TableConstraints(1, 1,
					true, true));
			fpnlRight.addControl(flblInfo, new TableConstraints(1, 1, false,
					true));
			fSplitPane.addControl(fpnlRight);
			// ������
			this.createToolBar();
			modulePanelActivedLoad();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ѯ���ʼ�����淢�����󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
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
			if (fpnlToolBar.jspDivLayer.getValue() != null) {
				iLevel = Integer.parseInt(fpnlToolBar.jspDivLayer.getValue()
						.toString());
			}

			DataSet dsDivName = QrBudgetI.getMethod().getDivName(
					Global.loginYear, iLevel, iUserType);
			ftreDivName.setDataSet(dsDivName);
			ftreDivName.reset();
			if (iUserType == USER_CZ) {
				// expandTo(ftreDivName);
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
	 * * ��ȫչ����ر�һ����,���ڵݹ�ִ�� *
	 * 
	 * @param tree
	 *            JTree *
	 * @param parent
	 *            ���ڵ� *
	 * @param expand
	 *            Ϊtrue���ʾչ����,����Ϊ�ر�������
	 */
	public static void expandTo(JTree tree) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		int count = root.getChildCount();
		if (count == 0)
			return;
		TreeNode node;
		for (int i = 0; i < count; i++) {
			node = root.getChildAt(i);
			tree.expandPath(new TreePath(((DefaultTreeModel) tree.getModel())
					.getPathToRoot(node)));

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
			this.setLayout(new RowPreferedLayout(1));
			FLabel flblReportName = new FLabel();
			flblReportName.setTitle("��ѯ��");
			flblReportName.setHorizontalAlignment(FLabel.CENTER);

			ftreReportName = new CustomTree("��ѯ��", null,
					IDefineReport.SHOW_LVL, IQrBudget.REPORT_CNAME,
					IDefineReport.PAR_ID, null, IQrBudget.LVL_ID, true);
			((DefaultTreeModel) ftreReportName.getModel())
					.setAsksAllowsChildren(true);

			// ����¼�
			ftreReportName.addMouseListener(new TreReportNameMouseListener());

			ftreReportName.setHasRelation(false);
			ftreReportName.setIsCheckBoxEnabled(true);
			FScrollPane fspnlReportName = new FScrollPane();
			fspnlReportName.addControl(ftreReportName);

			this.addControl(flblReportName, new TableConstraints(1, 1, false,
					true));
			this.addControl(fspnlReportName, new TableConstraints(1, 1, true,
					true));
		}

		/**
		 * ����������¼�
		 * 
		 */
		private class TreReportNameMouseListener extends
				java.awt.event.MouseAdapter {

			public void mouseClicked(java.awt.event.MouseEvent mouseevent) {
				if (mouseevent.getButton() != 1
						|| !(mouseevent.getSource() instanceof CustomTree))
					return;
				CustomTree tree = (CustomTree) mouseevent.getSource();

				int row = tree.getRowForLocation(mouseevent.getX(), mouseevent
						.getY());
				if (row < 0) {
					return;
				}
				TreePath path = tree.getPathForRow(row);
				if (path == null) {
					return;
				}
				MyTreeNode node = (MyTreeNode) path.getLastPathComponent();

				if (node != null) {
					PfTreeNode pNode = ((PfTreeNode) node.getUserObject());
					if (!pNode.getIsLeaf() || node == tree.getRoot()) {
						pNode.setIsSelect(!pNode.getIsSelect());
						return;
					}
					// ��λ�ڵ�
					try {
						DataSet dsReportName = ftreReportName.getDataSet();
						dsReportName.gotoBookmark(node.getBookmark());
						if (pNode.getIsSelect()) {
							if (!tabPaneReport.addTitlePane(dsReportName
									.getOriginData())) {
								pNode.setIsSelect(false);
							}
						} else {
							tabPaneReport.removeTitlePane(dsReportName
									.getOriginData());
						}

						// ɾ��ԭ���ļ�
						String sReportId = dsReportName.fieldByName(
								IQrBudget.REPORT_ID).toString();
						ReadWriteFile.deleteFile(sReportId);

					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(QrBudget.this,
								"��λ�ڵ㷢�����󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
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
			this.setLayout(new RowPreferedLayout(1));
			FLabel flblDivName = new FLabel();
			flblDivName.setTitle("��λ�б�");
			flblDivName.setHorizontalAlignment(FLabel.CENTER);

			ftreDivName = new CustomTree("����", null, IQrBudget.EN_ID,
					IQrBudget.CODE_NAME, IQrBudget.PARENT_ID, null,
					IQrBudget.DIV_CODE, true);
			// �û����ͣ�1:ҵ����,������λ
			ftreDivName.setIsCheckBoxEnabled(true);
			FScrollPane fspnlDivName = new FScrollPane();
			fspnlDivName.addControl(ftreDivName);

			this.addControl(flblDivName,
					new TableConstraints(1, 1, false, true));
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

		IntegerSpinner jspFrozenColumn;// �̶���

		IntegerSpinner jspDivLayer;// ��λ����

//		FLabel flblDataType;

//		JComboBox cbxDataType;// ��������

		FLabel flblDataVer; // ���ݰ汾

		JComboBox cbxDataVer;

		FLabel flblRown;

		IntegerSpinner jspRow; // ÿ����ʾ������

		FLabel flblRown1;

		List lstID;// ��������ID

		List lstVer = null;// ��ѯ�汾

		DataSet dsDataTypeList; // ��������DataSet

		public ToolBarPanel() throws Exception {
			super();
			// �̶�ǰ������
			FLabel flblFrozenColumn = new FLabel();
			flblFrozenColumn.setText("�̶�ǰ");
			SpinnerModel smFrozenColumn = new SpinnerNumberModel(0, 0, 100, 1);
			jspFrozenColumn = new IntegerSpinner(smFrozenColumn);
			jspFrozenColumn.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (reportUI == null || reportUI.getReport() == null)
						return;
					Report curReport = (Report) reportUI.getReport();

					int iCol = Integer.parseInt(jspFrozenColumn.getValue()
							.toString());
					int iColFrozen = 0;
					int iColNum = 0;
					for (int i = 0; i < curReport.getReportHeader()
							.getColumns(); i++) {
						if (iColFrozen >= iCol)
							break;
						iColNum++;
						if (curReport.getColumnWidth(i) > 0)
							iColFrozen++;
					}
					reportUI.freezeColumn(iColNum);
				}
			});
			FLabel flblFrozenColumn1 = new FLabel();
			flblFrozenColumn1.setText("��");

			// �������
//			flblDataType = new FLabel();
//			flblDataType.setTitle("��������");
//			cbxDataType = new JComboBox();
//			cbxDataType.addItemListener(new ClearItemListener());

			// ��λ����
			FLabel flblDivLayer = new FLabel();
			flblDivLayer.setText("��λ����");
			SpinnerModel smDivLayer = new SpinnerNumberModel(1, 1, 100, 1);
			jspDivLayer = new IntegerSpinner(smDivLayer);
			jspDivLayer.setValue(new Integer(4));
			jspDivLayer.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					// ���õ�λ��Ϣ����Ϣ
					int iLevel = Integer.parseInt(jspDivLayer.getValue()
							.toString());
					try {
						DataSet dsDivName = QrBudgetI.getMethod().getDivName(
								Global.loginYear, iLevel, iUserType);
						ftreDivName.setDataSet(dsDivName);
						ftreDivName.reset();

					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(QrBudget.this,
								"���õ�λ��ʾ�ڴη������󣬴�����Ϣ:" + e.getMessage(), "��ʾ",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// ÿ����ʾ������
			flblRown = new FLabel();
			flblRown.setText("ÿ����ʾ");
			SpinnerModel smRow = new SpinnerNumberModel(0, 0, 100, 1);
			jspRow = new IntegerSpinner(smRow);
			smRow.setValue(new Integer(30));
			flblRown1 = new FLabel();
			flblRown1.setText("��");

			// ��ѯ�汾
			flblDataVer = new FLabel();
			flblDataVer.setTitle("��ѯʱ�㣺");
			cbxDataVer = new JComboBox();
			cbxDataVer.addItemListener(new ClearItemListener());

			FLabel flblEmpty = new FLabel();
			flblEmpty.setTitle("   ");
			flblEmpty1 = new FLabel();
			flblEmpty1.setTitle("   ");
			flblEmpty2 = new FLabel();
			flblEmpty2.setTitle("   ");
			flblEmpty3 = new FLabel();
			flblEmpty3.setTitle("   ");

			FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
			this.setLayout(fLayout);
			this.addControl(flblFrozenColumn);
			this.add(jspFrozenColumn);
			this.addControl(flblFrozenColumn1);
			this.addControl(flblEmpty);
			this.addControl(flblDivLayer);
			this.add(jspDivLayer);
			this.addControl(flblEmpty1);
//			this.addControl(flblDataType);
//			this.add(cbxDataType);
			this.addControl(flblEmpty2);
			this.addControl(flblDataVer);
			this.add(cbxDataVer);
			initData();
		}

		private class ClearItemListener implements ItemListener {

			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					try {
						// ��ձ�����Ϣ
						clearReportInfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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
//				cbxDataType.addItem(dsDataTypeList.fieldByName(IQrBudget.NAME)
//						.getString());
			}
			if (lstID.size() > 0) {
				IPubInterface iPubInterface = PubInterfaceStub.getMethod();
				if (GlobalEx.getBelongType().equals(UntPub.FIS_VIS)) {// �����û�
					int iCurBatchNo = 1;
					String sID = getTypeID(dsDataTypeList, iCurBatchNo, 1);
//					if (sID == null) {
//						cbxDataType.setSelectedIndex(-1);
//					} else {
//						cbxDataType.setSelectedIndex(lstID.indexOf(new Integer(
//								sID)));
//					}
				} else {// ��λ�û�
					// �ж��ǲ����˴�����
					int iCurBatchNo = 1;
					String sID;
//					if (iPubInterface.getCurStateKind() == 2) {
						sID = getTypeID(dsDataTypeList, iCurBatchNo, 1);
//					} else {
						sID = getTypeID(dsDataTypeList, iCurBatchNo, 0);
//					}
//					if (sID == null) {
//						cbxDataType.setSelectedIndex(-1);
//					} else {
//						cbxDataType.setSelectedIndex(lstID.indexOf(new Integer(
//								sID)));
//					}
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
//		public Map getDataTypeInfo() throws Exception {
//			int iSelectIndex = cbxDataType.getSelectedIndex();
//			Object sID = lstID.get(iSelectIndex);
//			if (dsDataTypeList.locate(IQrBudget.ID, sID)) {
//				return dsDataTypeList.getOriginData();
//			}
//			return null;
//		}

		public String getVerNo() {
			int iSelectIndex = cbxDataVer.getSelectedIndex();
			if (iSelectIndex == 0 || iSelectIndex < 0)
				return "0";
			else
				return lstVer.get(iSelectIndex).toString();
		}

		public ReportUI getReportUI() {
			return reportUI;
		}

		public IntegerSpinner getJspFrozenColumn() {
			return jspFrozenColumn;
		}

//		public JComboBox getCbxDataType() {
//			return cbxDataType;
//		}

		public int getColRows() {
			return ((Integer) jspRow.getValue()).intValue();
		}

		public List getLstID() {
			return lstID;
		}

		public List getLstVer() {
			return lstVer;
		}
	}

	/**
	 * ��ձ�����Ϣ
	 * 
	 * @throws Exception
	 * 
	 */
	private void clearReportInfo() throws Exception {
		if (reportUI == null || reportUI.getReport() == null)
			return;
		int index = tabPaneReport.getSelectedIndex();
		if (index == -1) {
			return;
		}
		ReportInfo reportInfo = (ReportInfo) tabPaneReport.getLstReport().get(
				index);
		if (reportInfo.getBussType() == QrBudget.TYPE_SZZB) {// ��֧�ܱ��ʽ
			ReportHeaderShow reportHeaderShow = new ReportHeaderShow(this);
			reportHeaderShow.showHeader(reportInfo);
		} else {

			Report curReport = (Report) reportUI.getReport();
			if (curReport == null)
				return;
			TableHeader tableHeader = curReport.getReportHeader();

			int cols = tableHeader.getColumns();
			double[] colArray = new double[cols];
			for (int i = 0; i < cols; i++) {
				colArray[i] = curReport.getColumnWidth(i);
			}
			curReport.removeAllCellElements();
			curReport.setReportHeader(tableHeader);
			// �����п�
			for (int i = 0; i < cols; i++) {
				curReport.setColumnWidth(i, colArray[i]);
			}

			curReport.setReportHeader(tableHeader);
		}
		reportUI.repaint();
		// �������ѡ�����Ϣ
		sOldReportID = null;
	}

	/**
	 * �ж��Ƿ�ѡ����һ���ڵ�
	 * 
	 * @return false û��ѡ��ڵ�,true:ѡ���˽ڵ�
	 */
	public boolean checkSelectedNode(CustomTree customTree) {
		if (customTree.getSelectedNodeCount(true) == 0
				|| customTree.getSelectedNode() == customTree.getRoot()) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ�Ų�ѯ��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * �õ���������
	 * 
	 * @throws Exception
	 */
	private DataSet getOptDataTypeList() throws Exception {
		IPubInterface iPubInterface = PubInterfaceStub.getMethod();
		int iBatchNo = 1;
		if (iBatchNo > 10) {
			JOptionPane.showMessageDialog(this, "���γ�������Χ��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		return iPubInterface.getOptDataTypeList();
	}

	public CustomTree getFtreReportName() {
		return ftreReportName;
	}

	public IQrBudget getQrBudgetServ() {
		return qrBudgetServ;
	}

	public DataSet getDsReportHeader() {
		return dsReportHeader;
	}

	public void setDsReportHeader(DataSet dsReportHeader) {
		this.dsReportHeader = dsReportHeader;
	}

	public String getSFieldSelect() {
		return sFieldSelect;
	}

	public void setSFieldSelect(String fieldSelect) {
		sFieldSelect = fieldSelect;
	}

	public CustomTree getFtreDivName() {
		return ftreDivName;
	}

	public List getLstColInfo() {
		return lstColInfo;
	}

	public int getIUserType() {
		return iUserType;
	}

	public Node getNodeHeader() {
		return nodeHeader;
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

	public void setCurReport(Report aReport) {
		this.reportUI.setReport(aReport);
		this.reportUI.repaint();
	}

	public void setLstColInfo(List lstColInfo) {
		this.lstColInfo = lstColInfo;
	}

	/**
	 * �����ѯ��
	 * 
	 */

	/**
	 * ÿ���һ�β�ѯ��ť�������ɵ�UUID
	 * 
	 * @return
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * ÿ���һ�β�ѯ��ť�������ɵ�UUID
	 * 
	 * @param uuid
	 */
	public void setUUID(String uuid) {
		UUID = uuid;
	}

	/**
	 * ��������ѯ���
	 * 
	 * @return
	 */
	public List getLstResult() {
		return lstResult;
	}

	/**
	 * ��������ѯ���
	 * 
	 * @param lstResult
	 */
	public void setLstResult(List lstResult) {
		this.lstResult = lstResult;
	}

	public ReportUI getReportUI() {
		return reportUI;
	}

	public ToolBarPanel getFpnlToolBar() {
		return fpnlToolBar;
	}

	public int getColRows() {
		return ((Integer) fpnlToolBar.jspRow.getValue()).intValue();
	}

	public ReportTabbedPane getTabPaneReport() {
		return tabPaneReport;
	}

	public DataSet getDsSzzb() {
		return dsSzzb;
	}

	public void setDsSzzb(DataSet dsSzzb) {
		this.dsSzzb = dsSzzb;
	}

	public FLabel getFlblInfo() {
		return flblInfo;
	}

	public void setNodeHeader(Node nodeHeader) {
		this.nodeHeader = nodeHeader;
	}

	public FTable getFtabReport() {
		return ftabReport;
	}

	public void setReportUI(ReportUI reportUI) {
		this.reportUI = reportUI;
	}

	public void setFtabReport(FTable ftabReport) {
		this.ftabReport = ftabReport;
	}

	public String getSOldReportID() {
		return sOldReportID;
	}

	public void setSOldReportID(String oldReportID) {
		sOldReportID = oldReportID;
	}

	public String getprjtype() {
		return prjtype;
	}

	public void setprjtype(String prjtype) {
		this.prjtype = prjtype;
	}

}

/**
 * ԭʼ��ʽ��ѯ������ӡ����
 * 
 * @author qzc
 * 
 */

