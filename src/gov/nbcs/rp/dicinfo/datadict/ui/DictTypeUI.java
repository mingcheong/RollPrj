/**
 * Classname DictTypeUI
 *
 * Version 6.2.40
 *
 * Copyright �㽭���� ��Ȩ����
 */

package gov.nbcs.rp.dicinfo.datadict.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSplitPane;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import org.springframework.beans.factory.BeanFactory;

import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.gl.viewer.FInputPanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * <p>
 * Title:�����ֵ����͹��������
 * </p>
 * <p>

 */

public class DictTypeUI extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	private static final String DICT_TYPE_TYPE_CODE = "type_code";

	private static final String DICT_TYPE_TYPE_NAME = "type_name";

	private static final String DICT_TYPE_MEMO = "memo";

//	private TablePanel tableTable;
	
	//�������
	private FTree tableTypeTree = new FTree();

	IDataDictBO dataDictBO;

	private FInputPanel taleInfoPanel;
	
	private final static int State_Append = 1;

	private final static int State_Update = 3;
	
	private final static int State_Browse = 0;
	
	private int iState = State_Browse; // �༭״̬ 1: ��� 2�� �޸� 3: ɾ�� 0�� ���


	/**
	 * ���±����Ϣ
	 * 
	 */
	private void refreshTableTree() {
		tableTypeTree.setData(dataDictBO.queryTableTypeList());
	}
	/**
	 * 
	 * ���ɱ�ҳ��
	 * 
	 * @return
	 */
	private FPanel getTableInfoPane() {
		FTextField tableName = new FTextField("   ����Դ��������   ");
		tableName.setId("type_name");
		tableName.setTitleAdapting(true);

		FTextField txtSchemeKey = new FTextField("   ����Դ���ͱ��   ");
		txtSchemeKey.setId("type_code");
		txtSchemeKey.setTitleAdapting(true);

		FTextArea txtInterfaceKey = new FTextArea("   ����Դ����˵��   ");
		txtInterfaceKey.setId("memo");
		txtInterfaceKey.setTitleAdapting(true);

		taleInfoPanel = new FInputPanel();
		taleInfoPanel.setLayout(new RowPreferedLayout(3));

		taleInfoPanel.addControl(new FLabel(), new TableConstraints(1, 3,
				false, false));
		taleInfoPanel.addControl(txtSchemeKey, new TableConstraints(1, 3, false,
				false));

		taleInfoPanel.addControl(tableName, new TableConstraints(1, 3,
				false, false));
		taleInfoPanel.addControl(txtInterfaceKey, new TableConstraints(3, 3,
				false, false));

		taleInfoPanel.setEditable(false);
		return taleInfoPanel;
	}

	
	/**
	 * �޸�
	 */
	public void modify() throws Exception {}

	/**
	 * ����
	 */
	public void save() throws Exception {}

	private void setFirstSelect() {
		if (tableTypeTree != null && tableTypeTree.getData().size() > 0)
			tableTypeTree.setSelectionInterval(0,0);

	}

	/**
	 * ȡ��
	 */
//	public void cancel() throws Exception {
//		tableTypeTree.setEnabled(true);
//		taleInfoPanel.setData(null);
//	}

	/**
	 * �ָ�״̬
	 * 
	 */
	private void toInitState() {
		tableTypeTree.setEditable(true);
		taleInfoPanel.setEditable(false);
		taleInfoPanel.setData(null);
	}

	public void initize() {

		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/dicinfo/datadict/conf/dataDictConf.xml");
		dataDictBO = (IDataDictBO) beanfac.getBean("rp.dataDictService");

		FSplitPane mainSplitPane = new FSplitPane();

		mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setDividerLocation(280);

		FPanel tablePanel = new FPanel();
		tablePanel.setLayout(new RowPreferedLayout(1));

		
		tableTypeTree.setRoot("����Դ���");
		refreshTableTree();
		DefaultTreeSelectionModel mod = new DefaultTreeSelectionModel();
		mod.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tableTypeTree.setSelectionModel(mod);

		tableTypeTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
				
					// ��ǰѡ�еĽڵ�
					XMLData crrentData = tableTypeTree.getSelectedData();
					if (crrentData != null) {
						((FTextField) taleInfoPanel
								.getSubControl(DICT_TYPE_TYPE_NAME))
								.setValue(crrentData.get(DICT_TYPE_TYPE_NAME) == null ? ""
										: crrentData.get(DICT_TYPE_TYPE_NAME)
												.toString());
						((FTextField) taleInfoPanel
								.getSubControl(DICT_TYPE_TYPE_CODE))
								.setValue(crrentData.get(DICT_TYPE_TYPE_CODE) == null ? ""
										: crrentData.get(DICT_TYPE_TYPE_CODE)
												.toString());
						((FTextArea) taleInfoPanel.getSubControl(DICT_TYPE_MEMO))
								.setValue(crrentData.get(DICT_TYPE_MEMO) == null ? ""
										: crrentData.get(DICT_TYPE_MEMO).toString());
					}
				}
			}
		});
		
//		tableTable = new TablePanel(new String[][] {
//				{ DICT_TYPE_TYPE_CODE, "���ͱ���", "80" },
//				{ DICT_TYPE_TYPE_NAME, "��������", "130" } }, true);
//		tableTable.setCheckBoxAffectedByClickRow(false);
//		tableTable.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				XMLData crrentData = tableTable.getCurrentRow();
//				if (crrentData != null) {
//					((FTextField) taleInfoPanel
//							.getSubControl(DICT_TYPE_TYPE_NAME))
//							.setValue(crrentData.get(DICT_TYPE_TYPE_NAME) == null ? ""
//									: crrentData.get(DICT_TYPE_TYPE_NAME)
//											.toString());
//					((FTextField) taleInfoPanel
//							.getSubControl(DICT_TYPE_TYPE_CODE))
//							.setValue(crrentData.get(DICT_TYPE_TYPE_CODE) == null ? ""
//									: crrentData.get(DICT_TYPE_TYPE_CODE)
//											.toString());
//					((FTextArea) taleInfoPanel.getSubControl(DICT_TYPE_MEMO))
//							.setValue(crrentData.get(DICT_TYPE_MEMO) == null ? ""
//									: crrentData.get(DICT_TYPE_MEMO).toString());
//				}
//			}
//		});

//		tableTable.setData(dataDictBO.queryTableTypeList());
//		tableTable.setSelectionMode(TablePanel.SINGLE_SELECTION);
		tablePanel.addControl(tableTypeTree, new TableConstraints(25, 1, true));

		FPanel tableInfoPanel = new FPanel();

		tableInfoPanel.setLayout(new RowPreferedLayout(1));
		tableInfoPanel.add(getTableInfoPane());

		mainSplitPane.addControl(tablePanel);
		mainSplitPane.addControl(tableInfoPanel);
		try {
			this.createToolBar();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		this.add(mainSplitPane);
	}

	public void doAdd() {
		 iState = State_Append;
		tableTypeTree.setEnabled(false);
		taleInfoPanel.setEditable(true);
		((FTextArea) taleInfoPanel.getSubControl(DICT_TYPE_MEMO)).getEditor()
				.setBackground(Color.WHITE);

		taleInfoPanel.setData(null);
	}

	public void doDelete() {
		XMLData deleteData = tableTypeTree.getSelectedData();
		
		if(deleteData==null)
		{MessageBox
			.msgBox(Global.mainFrame, "��ѡ��Ҫɾ�������ݣ�", MessageBox.MESSAGE);
		return;
		}
		MessageBox confirm = new MessageBox("ȷʵҪɾ����ǰѡ�е�������?", MessageBox.MESSAGE,
				MessageBox.OK | MessageBox.CANCEL);
		confirm.show();
		if (!(confirm.result == 1)) {
			return;
		}	
		
		// ɾ��
		int retCode = 1;
		
		
			String typeCode = deleteData.get(DICT_TYPE_TYPE_CODE) == null ? ""
					: deleteData.get(DICT_TYPE_TYPE_CODE).toString();
			if (typeCode != null && !typeCode.equals("") && retCode >= 0)
				retCode = dataDictBO.deleteDictType(typeCode);
			
			taleInfoPanel.setEditable(false);
			toInitState();
			refreshTableTree();
			setFirstSelect();

//		if (retCode >= 0) {
//			MessageBox
//					.msgBox(Global.mainFrame, "ɾ���ɹ���", MessageBox.MESSAGE);
//			taleInfoPanel.setEditable(false);
//			toInitState();
//			refreshTableTree();
//			setFirstSelect();
//			return;
//		} else {
//			MessageBox
//					.msgBox(Global.mainFrame, "ɾ��ʧ�ܣ�", MessageBox.MESSAGE);
//			taleInfoPanel.setEditable(false);
//			toInitState();
//			refreshTableTree();
//			setFirstSelect();
//			return;
//		}
	
		}

	public void doCancel() {
		
		if(iState == State_Update||iState == State_Append)
		{
		String showMessage = "ȷʵҪ�����Ե�ǰ���ݵ��޸���?";
		if(	iState == State_Append)
			showMessage="ȷʵҪ�����Ե�ǰ���ӵ�������?";
		MessageBox confirm = new MessageBox(showMessage, MessageBox.MESSAGE,
				MessageBox.OK | MessageBox.CANCEL);
		confirm.show();
		if (!(confirm.result == 1)) {
			return;
		}	
		 iState = State_Browse;
		tableTypeTree.setEnabled(true);
		taleInfoPanel.setEditable(false);
		taleInfoPanel.setData(null);
		refreshTableTree();
		}
		
	}

	public void doInsert() {
		// TODO �Զ����ɷ������
		
	}

	public void doModify() {
		XMLData selectData = tableTypeTree.getSelectedData();
		if(selectData==null)
		{MessageBox
			.msgBox(Global.mainFrame, "��ѡ��Ҫ�޸ĵ����ݣ�", MessageBox.MESSAGE);
		return;
		}
		
		iState = State_Update;
		tableTypeTree.setEditable(false);
		tableTypeTree.setEnabled(false);
		taleInfoPanel.setEditable(true);
	
		((FTextArea) taleInfoPanel.getSubControl(DICT_TYPE_MEMO)).getEditor()
				.setBackground(Color.WHITE);
		((FTextField) taleInfoPanel.getSubControl(DICT_TYPE_TYPE_CODE))
				.setEditable(false);
	}

	public void doSave() {

		if(iState == State_Update||iState == State_Append)
		{
//		MessageBox confirm = new MessageBox("ȷʵҪ���浱ǰ������?", MessageBox.MESSAGE,
//				MessageBox.OK | MessageBox.CANCEL);
//		confirm.show();
//		if (!(confirm.result == 1)) {
//			return;
//		}	
		
		if (this.iState == State_Append) {
			// ���
			XMLData tableInfoData = taleInfoPanel.getData();

			// ���
			if (tableInfoData == null) {
				MessageBox.msgBox(Global.mainFrame, "����Դ�������ݲ���Ϊ�գ�",
						MessageBox.MESSAGE);
//				toInitState();
				return;
			}
			Object typeCodeObje = tableInfoData.get(DICT_TYPE_TYPE_CODE);
			if (typeCodeObje == null|| typeCodeObje.toString().trim()
							.equals("")) {
				MessageBox.msgBox(Global.mainFrame, "����Դ���ͱ�Ų���Ϊ�գ�",
						MessageBox.MESSAGE);
//				toInitState();
				return;
			}
			
			if (typeCodeObje.toString().trim().length() != 3) {
				MessageBox.msgBox(Global.mainFrame, "����Դ���ͱ�ű���Ϊ3λ��",
						MessageBox.MESSAGE);
//				toInitState();
				return;
			}
			
			boolean isExist =dataDictBO.isCodeExist(typeCodeObje.toString());
			
			if(isExist){
				MessageBox.msgBox(Global.mainFrame, "����Դ���ͱ���Ѿ����ڣ�",
						MessageBox.MESSAGE);
//				toInitState();
				return;
			}
			if (tableInfoData.get(DICT_TYPE_TYPE_NAME) == null
					|| tableInfoData.get(DICT_TYPE_TYPE_NAME).toString().trim()
							.equals("")) {
				MessageBox.msgBox(Global.mainFrame, "����Դ�������Ʋ���Ϊ�գ�",
						MessageBox.MESSAGE);
//				toInitState();
				return;
			}
			String typeCode = tableInfoData.get(DICT_TYPE_TYPE_CODE).toString()
					.trim();
			String typeName = tableInfoData.get(DICT_TYPE_TYPE_NAME).toString()
					.trim();
			String memo = tableInfoData.get(DICT_TYPE_MEMO) == null ? ""
					: tableInfoData.get(DICT_TYPE_MEMO).toString().trim();

			// ��������
			int ret = dataDictBO.addDictType(typeCode, typeName, memo);

			// ����ظ�
			if (ret == 2) {
				MessageBox.msgBox(Global.mainFrame, "�Ѵ��ڸñ�ŵ�����Դ���ͣ�",
						MessageBox.MESSAGE);
				toInitState();
				return;
			}
			// ����ɹ�
			if (ret > 0 && ret != 2) {
				iState = State_Browse;
				MessageBox
						.msgBox(Global.mainFrame, "����ɹ���", MessageBox.MESSAGE);
				taleInfoPanel.setEditable(false);
				taleInfoPanel.setData(null);
				refreshTableTree();
				tableTypeTree.setEnabled(true);
				return;

			} else if (ret < 0) {
				// ����ʧ��
				MessageBox
						.msgBox(Global.mainFrame, "����ʧ�ܣ�", MessageBox.MESSAGE);
				taleInfoPanel.setEditable(false);
				toInitState();
				setFirstSelect();
				return;
			}
		} else if (this.iState == State_Update) {
			// ����
			String typeCode = ((FTextField) taleInfoPanel
					.getSubControl(DICT_TYPE_TYPE_CODE)).getValue().toString();
			String typeName = ((FTextField) taleInfoPanel
					.getSubControl(DICT_TYPE_TYPE_CODE)).getValue() == null ? ""
					: ((FTextField) taleInfoPanel
							.getSubControl(DICT_TYPE_TYPE_NAME)).getValue()
							.toString();
			String memo = ((FTextArea) taleInfoPanel
					.getSubControl(DICT_TYPE_MEMO)).getValue() == null ? ""
					: ((FTextArea) taleInfoPanel.getSubControl(DICT_TYPE_MEMO))
							.getValue().toString();
			int ret = dataDictBO.updateDictType(typeCode, typeName, memo);
			if (ret > 0) {
				iState = State_Browse;
				MessageBox
						.msgBox(Global.mainFrame, "����ɹ���", MessageBox.MESSAGE);
				taleInfoPanel.setEditable(false);
				taleInfoPanel.setData(null);
				refreshTableTree();
				tableTypeTree.setEnabled(true);
				return;
			} else {
				MessageBox
						.msgBox(Global.mainFrame, "����ʧ�ܣ�", MessageBox.MESSAGE);
				taleInfoPanel.setEditable(false);
				refreshTableTree();
				tableTypeTree.setEnabled(true);
				return;
			}

		} 
		}
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
		
	}
	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
