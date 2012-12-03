package gov.nbcs.rp.sys.sysrefcol.ui;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataSetEvent;
import gov.nbcs.rp.common.datactrl.event.StateChangeListener;
import gov.nbcs.rp.common.ui.tree.CustomTree;
//import gov.nbcs.rp.sys.sysiaestru.ui.SetActionStatus;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;

/**
 * 
 * 
 * �����й���
 * 

 */
public class SysRefCol extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// ������
	private DataSet dsRefCol;

	private CustomTree ftreRefCol;

	// �����б��
	private FTextField ftxtfRefColID;

	// ����������
	private FTextField ftxtfRefColName;

	// ������ʹ����
	private FTextField ftxtfDataOwner;

	// ����
	private FRadioGroup frdoRefColKind;

	// ѡȡ��ʽ
	private FRadioGroup frdoSelectKind;

	// �����ֶ�
	private FTextField ftxtfPrimaryField;

	// ��������
	private FTextField ftxtfPrimaryType;

	// �����ֶ�
	private FTextField ftxtfCodeField;

	// �����ֶ�
	private FTextField ftxtfNameField;

	// ����ֶ�
	private FTextField ftxtfLvlField;

	// ��η��
	private FTextField ftxtfLvlStyle;

	// ����������
	private FTextArea ftxtaSqlDet;

	private final String ARRAY_KEY_KIND[] = { "��ֵ", "�ַ�" };

	// �ؼ�toolbar��ť״̬class
//	private SetActionStatus setActionStatus;

	private SysRefColPub sysRefColPub = new SysRefColPub();

	private SysRefColSet sysRefColSet;

	private ISysRefCol sysRefColServ = SysRefColI.getMethod();

	public SysRefCol() {
	}

	public void initize() {
		try {

			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// �������Ϣ
			SysCodeRule codeRule = SysCodeRule.createClient(new int[] { 4 });
			ftreRefCol = new CustomTree("�������б�", null, "REFCOL_ID", "name",
					codeRule);
			ftreRefCol
					.addTreeSelectionListener(new RefColTreeSelectionListener());
			FScrollPane fspnlRefCol = new FScrollPane();
			fspnlRefCol.addControl(ftreRefCol);
			fSplitPane.addControl(fspnlRefCol);

			// ���ұ���ϸ��Ϣ
			FPanel fpnlRightPanel = new FPanel();
			fSplitPane.addControl(fpnlRightPanel);
			{
				fpnlRightPanel.setLeftInset(10);
				fpnlRightPanel.setTopInset(10);
				fpnlRightPanel.setRightInset(10);
				fpnlRightPanel.setBottomInset(10);
				fpnlRightPanel.setLayout(new RowPreferedLayout(6));
				ftxtfRefColID = new FTextField("�����б�ţ�");
				ftxtfRefColName = new FTextField("���������ƣ�");
				ftxtfDataOwner = new FTextField("������ʹ���ߣ�");
				ftxtfRefColID.setProportion(0.35f);
				ftxtfRefColName.setProportion(0.35f);
				ftxtfDataOwner.setProportion(0.35f);
				FLabel flblEmpty = new FLabel();

				// ����
				FPanel fpnlRefColKind = new FPanel();
				fpnlRefColKind.setLayout(new RowPreferedLayout(1));
				fpnlRefColKind.setTitle(" ���� ");
				fpnlRefColKind.setFontSize(this.getFont().getSize());
				fpnlRefColKind.setFontName(this.getFont().getName());
				fpnlRefColKind.setTitledBorder();
				frdoRefColKind = new FRadioGroup("", FRadioGroup.VERTICAL);
				frdoRefColKind.setTitleVisible(false);
				frdoRefColKind.setRefModel("0#�û��Զ���+1#ϵͳ����");
				fpnlRefColKind.addControl(frdoRefColKind, new TableConstraints(
						1, 1, true));

				// ѡȡ��ʽ
				FPanel fpnlSelectKind = new FPanel();
				fpnlSelectKind.setLayout(new RowPreferedLayout(1));
				fpnlSelectKind.setTitle(" ѡȡ��ʽ ");
				fpnlSelectKind.setFontSize(this.getFont().getSize());
				fpnlSelectKind.setFontName(this.getFont().getName());
				fpnlSelectKind.setTitledBorder();
				frdoSelectKind = new FRadioGroup("", FRadioGroup.VERTICAL);
				frdoSelectKind.setTitleVisible(false);
				frdoSelectKind.setRefModel("0#�б�+1#���ṹ");
				fpnlSelectKind.addControl(frdoSelectKind, new TableConstraints(
						1, 1, true));

				// ����Ϣ
				FPanel fpnlColInfo = new FPanel();
				fpnlColInfo.setTitle(" ����Ϣ ");
				fpnlColInfo.setFontSize(this.getFont().getSize());
				fpnlColInfo.setFontName(this.getFont().getName());
				fpnlColInfo.setTitledBorder();
				RowPreferedLayout rLay = new RowPreferedLayout(4);
				fpnlColInfo.setLayout(rLay);
				ftxtfPrimaryField = new FTextField("�����ֶΣ�");
				ftxtfPrimaryType = new FTextField("�������ͣ�");
				ftxtfCodeField = new FTextField("�����ֶΣ�");
				ftxtfNameField = new FTextField("�����ֶΣ�");
				ftxtfLvlField = new FTextField("����ֶΣ�");
				ftxtfLvlStyle = new FTextField("��η��");
				ftxtfPrimaryField.setProportion(0.33f);
				ftxtfPrimaryType.setProportion(0.33f);
				ftxtfCodeField.setProportion(0.33f);
				ftxtfNameField.setProportion(0.33f);
				ftxtfLvlField.setProportion(0.33f);
				ftxtfLvlStyle.setProportion(0.33f);
				// �����ֶ�
				fpnlColInfo.addControl(ftxtfPrimaryField, new TableConstraints(
						1, 2, false));
				// ��������
				fpnlColInfo.addControl(ftxtfPrimaryType, new TableConstraints(
						1, 2, false));
				// �����ֶ�
				fpnlColInfo.addControl(ftxtfCodeField, new TableConstraints(1,
						2, false));
				// �����ֶ�
				fpnlColInfo.addControl(ftxtfNameField, new TableConstraints(1,
						2, false));
				// ����ֶ�
				fpnlColInfo.addControl(ftxtfLvlField, new TableConstraints(1,
						2, false));
				// ��η��
				fpnlColInfo.addControl(ftxtfLvlStyle, new TableConstraints(1,
						2, false));

				// ����������
				FPanel fpnlSqlDet = new FPanel();
				fpnlSqlDet.setLayout(new RowPreferedLayout(1));
				fpnlSqlDet.setTitle(" ���������� ");
				fpnlSqlDet.setFontSize(this.getFont().getSize());
				fpnlSqlDet.setFontName(this.getFont().getName());
				fpnlSqlDet.setTitledBorder();
				ftxtaSqlDet = new FTextArea();
				ftxtaSqlDet.setTitleVisible(false);
				fpnlSqlDet.addControl(ftxtaSqlDet, new TableConstraints(1, 1,
						true));

				// �����б��
				fpnlRightPanel.addControl(ftxtfRefColID, new TableConstraints(
						1, 2, false));
				// ����
				fpnlRightPanel.addControl(fpnlRefColKind, new TableConstraints(
						3, 1, false));
				// ѡȡ��ʽ
				fpnlRightPanel.addControl(fpnlSelectKind, new TableConstraints(
						3, 1, false));
				// ��FLabel,Ϊ����
				fpnlRightPanel.addControl(flblEmpty, new TableConstraints(3, 2,
						false));
				// ����������
				fpnlRightPanel.addControl(ftxtfRefColName,
						new TableConstraints(1, 2, false));
				// ������ʹ����
				fpnlRightPanel.addControl(ftxtfDataOwner, new TableConstraints(
						1, 2, false));
				// ����Ϣ
				fpnlRightPanel.addControl(fpnlColInfo, new TableConstraints(5,
						4, false));
				// ����������
				fpnlRightPanel.addControl(fpnlSqlDet, new TableConstraints(1,
						6, true));
			}
			this.createToolBar();
			Common.changeChildControlsEditMode(fpnlRightPanel, false);

			// ��������
			modulePanelActivedLoad();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ�����н��淢������,������Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ��������
	 * 
	 */
	private void modulePanelActivedLoad() {
		try {
			dsRefCol = sysRefColServ.getRefColRecord(Global.loginYear);
			dsRefCol.addStateChangeListener(new RefColStateChangeListener());
			ftreRefCol.setDataSet(dsRefCol);
			ftreRefCol.reset();
			sysRefColSet = new SysRefColSet(dsRefCol);
			// ���ð�ť״̬
//			setActionStatus = new SetActionStatus(this);
			setToolBarState();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�����н���������ݷ������󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doAdd() {
		try {
			sysRefColSet.initInfo(SysRefColOperation.rcoAdd);
			Tools.centerWindow(sysRefColSet);
			sysRefColSet.setVisible(true);
			ftreRefCol.expandTo("REFCOL_ID", dsRefCol.fieldByName("refcol_id")
					.getString());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "������������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doDelete() {
		try {
			if (ftreRefCol.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ�������м�¼��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String sRefColId = dsRefCol.fieldByName("Refcol_id").getString();
			if (!checkRefColUsed(sRefColId))
				return;
			// ��ʾ�Ƿ�ȷ��ɾ��
			if (JOptionPane.showConfirmDialog(this, "���Ƿ�ȷ��Ҫɾ��������¼?", "��ʾ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
			dsRefCol.delete();
			sysRefColServ.DeleteRefCol(sRefColId, Global.loginYear);
			dsRefCol.applyUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ɾ����������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doCancel() {
	}

	public void doInsert() {
	}

	public void doModify() {
		if (ftreRefCol.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "��ѡ��һ�������м�¼��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try {
			String sRefColId = dsRefCol.fieldByName("Refcol_id").getString();
			if (!checkRefColUsed(sRefColId))
				return;
			sysRefColSet.initInfo(SysRefColOperation.rcoModify);
			Tools.centerWindow(sysRefColSet);
			sysRefColSet.setVisible(true);
			showOneRecordInfo();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�޸���������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/**
	 * �鿴��ϸ
	 * 
	 */
	public void doSeeList() {
		try {
			sysRefColSet.initInfo(SysRefColOperation.rcoView);
			Tools.centerWindow(sysRefColSet);
			sysRefColSet.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�鿴��������ϸ�����쳣��������Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doPreview() {
		try {
			String sSqlDet = dsRefCol.fieldByName("Sql_det").getString();
			sSqlDet = SysRefColPub.ReplaceRefColFixFlag(sSqlDet.toUpperCase());

			String sRefColName = dsRefCol.fieldByName("REFCOL_NAME")
					.getString();

			DataSet ds = null;
			if ("0".equals(dsRefCol.fieldByName("select_kind").getString())) {
				ds = sysRefColServ.exeSqlDs(sSqlDet);
				String sRefColId = dsRefCol.fieldByName("refcol_id")
						.getString();
				DataSet dsDetail = sysRefColServ.getRefColDetailWithRefColId(
						sRefColId, Global.loginYear);
				int iCount = dsDetail.getRecordCount();
				String[] sFieldText = new String[iCount];
				String[] sFieldName = new String[iCount];
				int[] sColWidth = new int[iCount];
				dsDetail.beforeFirst();
				int i = 0;
				while (dsDetail.next()) {
					sFieldText[i] = dsDetail.fieldByName("FIELD_CNAME")
							.getString();
					sFieldName[i] = dsDetail.fieldByName("FIELD_ENAME")
							.getString();
					sColWidth[i] = dsDetail.fieldByName("FIELD_Width")
							.getInteger();
					i++;
				}
				SysRefColTable sysRefColTable = new SysRefColTable(sFieldText,
						sFieldName, sColWidth, ds);
				sysRefColTable.setTitle(sRefColName);
				Tools.centerWindow(sysRefColTable);
				sysRefColTable.setVisible(true);
			} else {
				String sFieldId = dsRefCol.fieldByName("Lvl_Field").getString();
				String sFieldName = dsRefCol.fieldByName("NAME_Field")
						.getString();
				String sName = "'['||" + sFieldId + "||']'||" + sFieldName
						+ " as Name ";
				sSqlDet = " select " + sFieldId + "," + sFieldName + ","
						+ sName + " from (" + sSqlDet + ")";
				ds = sysRefColServ.exeSqlDs(sSqlDet);
				String sLvlStyle = dsRefCol.fieldByName("Lvl_Style")
						.getString();
				SysCodeRule lvlRule = SysCodeRule.createClient(sLvlStyle); // ��������
				SysRefColTree sysRefColTree = new SysRefColTree(sRefColName,
						ds, sFieldId, "Name", lvlRule);
				sysRefColTree.setTitle(sRefColName);
				Tools.centerWindow(sysRefColTree);
				sysRefColTree.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "������Ԥ������������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean checkRefColUsed(String sRefColId) throws Exception {
		// �ж��Ƿ��ѱ�ʹ��
		InfoPackage infoPackage = sysRefColServ.checkRefColUsed(sRefColId,
				Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	private class RefColTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			if (dsRefCol.isEmpty())
				return;
			try {
				showOneRecordInfo();
				setToolBarState();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(SysRefCol.this,
						"��������ʾ��ϸ��Ϣ�������󣬴�����Ϣ:" + e1.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void showOneRecordInfo() throws Exception {
		// ������
		ftxtfRefColID.setValue(dsRefCol.fieldByName("REFCOL_ID").getString());
		// �����б��
		ftxtfRefColName.setValue(dsRefCol.fieldByName("REFCOL_NAME")
				.getString());
		// ������ʹ����
		ftxtfDataOwner.setValue(sysRefColPub.ARRAY_REFCOL_DATA_OWNER[dsRefCol
				.fieldByName("DATA_OWNER").getInteger()]);
		// ����
		frdoRefColKind
				.setValue(dsRefCol.fieldByName("RefCol_Kind").getString());
		// ѡȡ��ʽ
		frdoSelectKind
				.setValue(dsRefCol.fieldByName("Select_Kind").getString());
		// �����ֶ�
		ftxtfPrimaryField.setValue(dsRefCol.fieldByName("Primary_Field")
				.getString());
		// ��������
		ftxtfPrimaryType.setValue(ARRAY_KEY_KIND[dsRefCol.fieldByName(
				"Primary_Type").getInteger()]);
		// �����ֶ�
		ftxtfCodeField.setValue(dsRefCol.fieldByName("Code_Field").getString());
		// �����ֶ�
		ftxtfNameField.setValue(dsRefCol.fieldByName("Name_Field").getString());
		// ����ֶ�
		ftxtfLvlField.setValue(dsRefCol.fieldByName("Lvl_Field").getString());
		// ��η��
		ftxtfLvlStyle.setValue(dsRefCol.fieldByName("Lvl_Style").getString());
		// ����������
		ftxtaSqlDet.setValue(dsRefCol.fieldByName("Sql_Det").getString());
	}

	private class RefColStateChangeListener implements StateChangeListener {

		private static final long serialVersionUID = 1L;

		public void onStateChange(DataSetEvent event) throws Exception {
			setToolBarState();
		}
	}

	private void setToolBarState() throws Exception {
//		if (dsRefCol.isEmpty() || ftreRefCol.getSelectedNode() == null
//				|| ftreRefCol.getSelectedNode() == ftreRefCol.getRoot()) {
//			setActionStatus.setOneBtnState("�޸�", false);
//			setActionStatus.setOneBtnState("ɾ��", false);
//			setActionStatus.setOneBtnState("�鿴��ϸ", false);
//			setActionStatus.setOneBtnState("Ԥ�����", false);
//		} else {
//			if (!dsRefCol.bof() && !dsRefCol.eof()
//					&& dsRefCol.containsField("Refcol_Kind")
//					&& dsRefCol.fieldByName("Refcol_Kind").getInteger() == 1) {
//				setActionStatus.setOneBtnState("ɾ��", false);
//			} else {
//				setActionStatus.setOneBtnState("ɾ��", true);
//			}
//			setActionStatus.setOneBtnState("�޸�", true);
//			setActionStatus.setOneBtnState("�鿴��ϸ", true);
//			setActionStatus.setOneBtnState("Ԥ�����", true);
//		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
