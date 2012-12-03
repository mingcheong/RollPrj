/**
 * @# TableSourceAddUI.java    <�ļ���>
 */
package gov.nbcs.rp.dicinfo.datadict.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.basinfo.common.BaseUtil;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.exception.AppException;

/**
 * ����˵��:�µ�����Դ�༭����ֻ�������ֵ������޸Ĳ���
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class DataDictionaryUI extends FModulePanel implements ActionedUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6550186405941084233L;

	private FTree treSource;

	private MainSourceInfoPanel mainPanel;// ����Ϣ�������

	private DetailListPanel detailPanel;// ��ϸ�б����

	private IDataDictBO dataDicBO;// ����Դ��

	/**
	 * ��ʼ������
	 */
	public void initize() {
		dataDicBO = (IDataDictBO) BOCache.getBO("rp.dataDictService");

		treSource = new FTree();
		treSource.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				// ���ڵ�����ͽڵ���ʾΪ��
				XMLData aData = treSource.getSelectedData();
				if (aData == null
						|| "1".equals(aData.get("isclass"))
						|| treSource.getSelectionPath().getLastPathComponent() == treSource
								.getRoot()) {
					detailPanel.setTableID("");
					mainPanel.setDispData(null);
				} else {
					detailPanel.setTableID((String) aData.get("dicid"));
					mainPanel.setDispData(aData);
				}

			}

		});
		DefaultTreeSelectionModel mod = new DefaultTreeSelectionModel();
		mod.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treSource.setSelectionModel(mod);
		treSource.setRoot("����Դ����");
		FScrollPane spnlTree = new FScrollPane(treSource);

		mainPanel = new MainSourceInfoPanel();
		detailPanel = new DetailListPanel();

		FSplitPane spnlBack = new FSplitPane();
		spnlBack.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spnlBack.setDividerLocation(250);
		spnlBack.add(spnlTree, JSplitPane.LEFT);

		FPanel pnlRight = new FPanel();// �ұ��������
		pnlRight.setLayout(new BorderLayout());
		pnlRight.add(mainPanel, BorderLayout.NORTH);
		pnlRight.add(detailPanel, BorderLayout.CENTER);
		initTree();

		spnlBack.add(pnlRight, JSplitPane.RIGHT);

		this.add(spnlBack);
		this.detailPanel.setEditable(false);
		this.mainPanel.setEditable(false);
		try {
			this.createToolBar();
			setButtonState(false);
		} catch (ControlException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (AppException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}

	public void doAdd() {

		if (!isDataSaved) {
			new MessageBox("������û�б��棬�����Խ������Ӳ���!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
		new AddTableDlg();
		if (!AddTableDlg.isPressOK())// ������˳���ر�,�򷵻�
			return;
		try {
			dataDicBO.makeListTable(AddTableDlg.getArrTableSelect(),
					AddTableDlg.getPage() == AddTableDlg.PAGE_DEFTABLE,
					AddTableDlg.getTabProp(), Global.loginYear);
		} catch (Exception e) {
			new MessageBox("��ӱ�(��ͼ)ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
			AddTableDlg.clearSelect();
			return;
		}
		new MessageBox("��ӳɹ�!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
				.show();
		AddTableDlg.clearSelect();
		// ˢ����

		initTree();
		treSource.setSelectionRow(0);

	}

	public void doCancel() {
		if (this.isDataSaved)
			return;
		this.detailPanel.cancelEdit();
		this.mainPanel.refreshData();
		this.isDataSaved = true;
		setButtonState(false);
		treSource.setEnabled(true);

	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();

	}

	public void doDelete() {
		XMLData selectData = treSource.getSelectedData();
		String isClass = BaseUtil.getAStringField(selectData,
				IDataDictBO.ISCLASS);
		if (selectData == null || isClass == null || isClass.equals("1")) {
			MessageBox.msgBox(Global.mainFrame, "��ѡ��Ҫɾ�������ݣ�",
					MessageBox.MESSAGE);
			return;
		}
		// �ж��Ƿ���������Դ
//		if (Integer.parseInt(selectData.get(IDataDictBO.ISDEFAULT).toString()) == 1) {
//			MessageBox.msgBox(Global.mainFrame, "ϵͳ��������Դ������ɾ����",
//					MessageBox.MESSAGE);
//			return;
//		}

		String tableEName = BaseUtil.getAStringField(selectData,
				IDataDictBO.TABLEENAME);

		String tableCname = BaseUtil.getAStringField(selectData,
				IDataDictBO.TABLECNAME);

		MessageBox confirm = new MessageBox("ȷʵҪɾ���ֵ���[" + tableEName + "|"
				+ tableCname + "]��?", MessageBox.MESSAGE, MessageBox.OK
				| MessageBox.CANCEL);
		confirm.show();
		if (!(confirm.result == 1)) {
			confirm.dispose();
			return;
		}
		confirm.dispose();
		String dicID = BaseUtil.getAStringField(selectData, IDataDictBO.DICID);
		int retCode = dataDicBO.deleteTableInfo(dicID);

		if (retCode > 0) {
			MessageBox.msgBox(Global.mainFrame, "ɾ���ɹ���", MessageBox.MESSAGE);

		} else {
			MessageBox.msgBox(Global.mainFrame, "ɾ��ʧ�ܣ�", MessageBox.MESSAGE);

		}
		// ɾ�����Ͻڵ�
		treSource.deleteSelectedData();

	}

	public void doInsert() {
		// TODO �Զ����ɷ������

	}

	public void doModify() {
		XMLData aData = treSource.getSelectedData();
		if (!this.isDataSaved) {
			new MessageBox("���ڱ༭״̬!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
					.show();
			return;
		}
		if (aData == null
				|| "1".equals(aData.get("isclass"))
				|| treSource.getSelectionPath().getLastPathComponent() == treSource
						.getRoot()) {
			new MessageBox("��ѡ��һ������Դ�����޸�!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
		this.isDataSaved = false;
		this.detailPanel.setEditable(true);
		this.mainPanel.setEdit(true);
		treSource.setEnabled(false);
		setButtonState(true);
	}

	public void doSave() {
		if (this.isDataSaved) {
			new MessageBox("û�б䶯��Ҫ����!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}
//		DictionaryState.getInstance().setChanged();
		try {
			XMLData xmlMain = this.mainPanel.getData();
			if (dataDicBO.isExistTableCname(xmlMain.get("tablecname")
					.toString(), xmlMain.get("dicid").toString())) {
				new MessageBox("�����������ظ���", MessageBox.INFOMATION,
						MessageBox.BUTTON_OK).show();
				return;
			}

			List lstFields = this.detailPanel.getFieldByOrdered();
			String sErr = judgetSortRepeat(lstFields);
			if (!Common.isNullStr(sErr)) {
				new MessageBox(sErr, MessageBox.INFOMATION,
						MessageBox.BUTTON_OK).show();
				return;
			}

			sErr = dataDicBO.saveDs(xmlMain, lstFields);
			// �����µ����ݸ������ϵ�����
			treSource.getSelectedData().putAll(xmlMain);
			XMLData aData = treSource.getSelectedData();
			// ������ʾ

			treSource.getSelectedPfTreeNode().setShowContent(
					aData.get("chr_code") + " " + aData.get("tablecname"));

			if (!sErr.equals("")) {
				new MessageBox("����ʧ��!", sErr, MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				return;
			}
		} catch (Exception e) {
			new MessageBox("����ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return;
		}
		new MessageBox("����ɹ�!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
				.show();
		treSource.updateUI();
		treSource.setEnabled(true);
		isDataSaved = true;
		this.detailPanel.setEditable(false);
		this.mainPanel.setEdit(false);
		setButtonState(false);

	}

	private void initTree() {
		List lstData = dataDicBO.getTableList(Global.loginmode);
		if (lstData == null)
			return;
		// treSource.setDataBySpecCode(lstData, "chr_code", "chr_name", "����Դ");
		treSource.setData(lstData);
		treSource.updateUI();
		treSource.expendedNode(treSource.getRoot());

	}

	public void setButtonState(boolean isEdit) {
		FToolBarPanel panel = this.getToolbarPanel();
		setButtonState(panel, "�޸�", !isEdit);
		setButtonState(panel, "����", isEdit);
		setButtonState(panel, "ȡ��", isEdit);
		setButtonState(panel, "ɾ��", !isEdit);
		setButtonState(panel, "����", !isEdit);
		setButtonState(panel, "����", !isEdit);
		setButtonState(panel, "����", !isEdit);
	}

	public static boolean setButtonState(FToolBarPanel panel, String title,
			boolean isEnable) {
		if (panel == null || title == null)
			return false;
		List lstControls = panel.getSubControls();
		if (lstControls == null)
			return false;
		int iCount = lstControls.size();
		Object obj;
		for (int i = 0; i < iCount; i++) {
			obj = lstControls.get(i);
			if (!(obj instanceof FButton))
				continue;
			if (title.equals(((FButton) obj).getText())) {
				((FButton) obj).setEnabled(isEnable);
				return true;
			}
		}
		return false;
	}

	/**
	 * ������
	 * 
	 */
	public void moveDown() {
		Global.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			if (!isDataSaved)
				return;
			XMLData aData = treSource.getSelectedData();
			if (aData == null || "1".equals(aData.get("isclass")))
				return;
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) treSource
					.getSelectionPath().getLastPathComponent();
			DefaultMutableTreeNode nextNode = aNode.getNextNode();
			if (nextNode == null)
				return;
			XMLData xmlNext = (XMLData) ((PfTreeNode) nextNode.getUserObject())
					.getCustomerObject();
			if (nextNode == null || "1".equals(xmlNext.get("isclass")))
				return;
			dataDicBO.changeOrder(aData, xmlNext, Global.loginYear);
			initTree();
			BaseUtil.signFirstBySpecMutiFields(treSource,
					new String[] { "dicid" }, new String[] { (String) aData
							.get("dicid") }, null);
		} finally {
			Global.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * ������
	 * 
	 */
	public void moveUp() {
		Global.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			if (!isDataSaved)
				return;
			XMLData aData = treSource.getSelectedData();
			if (aData == null || "1".equals(aData.get("isclass")))
				return;
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) treSource
					.getSelectionPath().getLastPathComponent();
			DefaultMutableTreeNode preNode = aNode.getPreviousNode();
			XMLData xmlPre = (XMLData) ((PfTreeNode) preNode.getUserObject())
					.getCustomerObject();

			if (preNode == null || preNode == treSource.getRoot()
					|| "1".equals(xmlPre.get("isclass")))
				return;
			dataDicBO.changeOrder(aData, xmlPre, Global.loginYear);
			initTree();

			BaseUtil.signFirstBySpecMutiFields(treSource,
					new String[] {"dicid"}, new String[] { (String) aData
							.get("dicid") }, null);
		} finally {
			Global.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * �ж�������Ƿ�Ϊ�ջ��ظ�
	 * 
	 * @param lstFields
	 * @return
	 */
	private String judgetSortRepeat(List lstFields) {
		if (lstFields == null && lstFields.isEmpty()) {
			return "";
		}

		String sSortFrom, sSortTo;
		int size = lstFields.size();
		for (int i = 0; i < size; i++) {
			XMLData data = (XMLData) lstFields.get(i);
			if (data.get(IDataDictBO.AFIELD_SORT) == null
					|| Common.isNullStr(data.get(IDataDictBO.AFIELD_SORT)
							.toString())) {
				return "��" + (i + 1) + "�������Ϊ�գ�����д��";
			}

			sSortFrom = data.get(IDataDictBO.AFIELD_SORT).toString();
			for (int j = i + 1; j < size; j++) {
				XMLData dataTmp = (XMLData) lstFields.get(j);
				if (dataTmp.get(IDataDictBO.AFIELD_SORT) == null
						|| Common.isNullStr(dataTmp
								.get(IDataDictBO.AFIELD_SORT).toString())) {
					continue;
				}
				sSortTo = dataTmp.get(IDataDictBO.AFIELD_SORT).toString();
				if (sSortFrom.equals(sSortTo)) {
					return "��" + (i + 1) + "," + (j + 1) + "��������ظ�,���޸ģ�";
				}
			}
		}
		return "";
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
