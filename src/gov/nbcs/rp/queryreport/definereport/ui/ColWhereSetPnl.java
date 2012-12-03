/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomStatisticCaliber;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FComboBoxItem;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.object.cellvalue.SummaryStatisticCaliberImpl;
import com.foundercy.pf.util.XMLData;
/*

 * ��������Դ��ö������Դ���������������
 */
public class ColWhereSetPnl extends FPanel {

	private static final long serialVersionUID = 1L;

	// ����Դ��Ͽ�
	FComboBox dataSourceCbx = null;

	private FComboBox cbxCompare;// �Ƚ�����

	// ����ֵ
	// private FTextField txtWhereValue = null;
	private FComboBox cbxWhereValue = null;

	// ������������
	private FRadioGroup frdoType = null;

	// �ֶ�������Ͽ�
	private FComboBox cbxFieldName = null;

	// ������
	private MyTree treWhere = null;

	// Sql����
	private FTextArea txtSql;

	private List isPriserve = null;

	private CalcColumnDialog calcColumnDialog = null;

	private List lstdsDictionary = null;

	public ColWhereSetPnl(final CalcColumnDialog calcColumnDialog) {
		this.calcColumnDialog = calcColumnDialog;

		// ����Դ��Ͽ�
		dataSourceCbx = new MyFComboBox("����Դ:", calcColumnDialog
				.getQuerySource());
		((JComboBox) dataSourceCbx.getComponent(1))
				.addItemListener(new SelectItemListener());

		// �ֶ���
		FLabel fieldNameLbl = new FLabel();
		fieldNameLbl.setTitle("�ֶ���:");
		// ������ѡ��
		FLabel operateLbl = new FLabel();
		operateLbl.setText("����:");
		// ����ֵ
		FLabel whereValueLbl = new FLabel();
		whereValueLbl.setText("ֵ:");

		// �ֶ�������Ͽ�
		cbxFieldName = new FComboBox();
		cbxFieldName.setTitleVisible(false);
		((JComboBox) cbxFieldName.getComponent(1))
				.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent arg0) {
						if (cbxFieldName.getValue() == null) {
							return;
						}

						String sFieldTyp = getFieldType();
						// �ж��ֶ����ͣ��������ֵ�Ͱ����Ͳ���������ʾ
						if (DefinePub.checkCharVal(sFieldTyp)) {
							cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
						} else {
							cbxCompare
									.setRefModel(CompareType.COMPARE_TYPE_NUM);
						}
						cbxCompare.setSelectedIndex(-1);
						// �õ�������Ϣѡ���е�ֵ�����ݱ���id��enameֵ
						if (!Common.isNullStr(dataSourceCbx.getValue()
								.toString())
								&& !Common.isNullStr(cbxFieldName.getValue()
										.toString())) {
							String sFeildEname = cbxFieldName.getValue()
									.toString();
							sFeildEname = sFeildEname.substring(0, sFeildEname
									.indexOf(":"));

							List lstInputValue = null;
							try {
								lstInputValue = DefineReportI.getMethod()
										.getBasSelectInputValue(
												dataSourceCbx.getValue()
														.toString(),
												sFeildEname);
							} catch (Exception e) {
								e.printStackTrace();
								new MessageBox(calcColumnDialog, "������Ϣ��"
										+ e.getMessage(), MessageBox.ERROR,
										MessageBox.BUTTON_OK).show();
							}
							cbxWhereValue.setEditable(true);
							addRefValue(cbxWhereValue, lstInputValue,
									IDefineReport.CODE, IDefineReport.NAME);
							cbxWhereValue.setSelectedIndex(-1);
						} else {
							cbxWhereValue.removeAllItems();
						}

					}
				});

		// �Ƚ�����
		cbxCompare = new FComboBox();
		cbxCompare.setTitleVisible(false);
		cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
		cbxCompare.setSelectedIndex(-1);

		// ����ֵ
		// txtWhereValue = new FTextField(false);
		cbxWhereValue = new FComboBox();
		((JComboBox) cbxWhereValue.getEditor()).setEditable(true);
		cbxWhereValue.setTitleVisible(false);

		// �����������
		FPanel orderPnl = new FPanel();
		orderPnl.setLayout(new RowPreferedLayout(5));

		FLabel flblEmpty = new FLabel();
		// ������Դ
		orderPnl.addControl(dataSourceCbx, new TableConstraints(1, 2, false,
				true));
		orderPnl.addControl(flblEmpty, new TableConstraints(1, 3, false, true));
		// Label
		orderPnl.addControl(fieldNameLbl, new TableConstraints(1, 2, false,
				true));
		orderPnl
				.addControl(operateLbl, new TableConstraints(1, 1, false, true));
		orderPnl.addControl(whereValueLbl, new TableConstraints(1, 2, false,
				true));
		// �����
		orderPnl.addControl(cbxFieldName, new TableConstraints(1, 2, false,
				true));
		orderPnl
				.addControl(cbxCompare, new TableConstraints(1, 1, false, true));
		orderPnl.add(cbxWhereValue, new TableConstraints(1, 2, false, true));

		// ������������
		frdoType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoType.setRefModel(JoinBefore.JOIN_BEFORE);
		frdoType.setTitleVisible(false);
		frdoType.setValue("and");

		// ���ӡ��޸İ�ť
		FButton addBtn = new FButton("addBtn", "����");
		addBtn.addActionListener(new AddActionListener());

		FButton modifyBtn = new FButton("addBtn", "�޸�");
		modifyBtn.addActionListener(new ModifyActionListener());
		// FFlowLayoutPanel editBtnPnl = new FFlowLayoutPanel();
		FPanel editBtnPnl = new FPanel();
		RowPreferedLayout rEditBtnLay = new RowPreferedLayout(2);
		rEditBtnLay.setColumnGap(1);
		rEditBtnLay.setColumnWidth(70);
		editBtnPnl.setLayout(rEditBtnLay);
		// editBtnPnl.setAlignment(FlowLayout.RIGHT);
		editBtnPnl.addControl(addBtn, new TableConstraints(1, 1, false, false));
		editBtnPnl.addControl(modifyBtn, new TableConstraints(1, 1, false,
				false));

		FLabel flbEmpty = new FLabel();
		FPanel fpnlEdit = new FPanel();
		RowPreferedLayout rEditLay = new RowPreferedLayout(4);
		rEditLay.setColumnWidth(140);
		fpnlEdit.setLayout(rEditLay);

		fpnlEdit.addControl(flbEmpty, new TableConstraints(1, 1, true, true));
		fpnlEdit.addControl(frdoType, new TableConstraints(1, 2, true, true));
		fpnlEdit
				.addControl(editBtnPnl, new TableConstraints(1, 1, true, false));

		// ����������
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("��������");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treWhere = new MyTree(treeModel);
		// �任���ڵ㣬ˢ����ʾ����Ϣ�¼�
		treWhere.addTreeSelectionListener(new WhereTreeSelectionListener());
		JScrollPane fpnltalRela = new JScrollPane(treWhere);

		// ����������ť���
		FPanel btnPnl = new BtnPnl();

		JTabbedPane pnlWhere = new JTabbedPane();
		RowPreferedLayout rLay = new RowPreferedLayout(2);
		FPanel pnlCol = new FPanel();
		pnlCol.setLayout(rLay);
		rLay.setColumnWidth(70);
		rLay.setColumnGap(1);

		pnlCol.addControl(orderPnl, new TableConstraints(3, 2, false, true));
		pnlCol.addControl(fpnlEdit, new TableConstraints(1, 2, false, true));
		pnlCol.add(fpnltalRela, new TableConstraints(1, 1, true, true));
		pnlCol.addControl(btnPnl, new TableConstraints(1, 1, true, false));

		txtSql = new FTextArea();
		txtSql.setTitleVisible(false);
		txtSql.setEditable(false);

		FScrollPane spnlSql = new FScrollPane(txtSql);

		pnlWhere.add("��������Ϣ", pnlCol);
		pnlWhere.add("������������", spnlSql);
		this.setLayout(new BorderLayout());
		this.add(pnlWhere, BorderLayout.CENTER);
	}

	/**
	 * ����������ť���
	 */
	private class BtnPnl extends FPanel {

		private static final long serialVersionUID = 1L;

		public BtnPnl() {
			// ɾ������������ť
			FButton delBtn = new FButton("delBtn", "ɾ��");
			delBtn.addActionListener(new DelActionListener());
			// ���ư�ť
			FButton upBtn = new FButton("upBtn", "����");
			upBtn.addActionListener(new UpActionListener());
			// ���ư�ť
			FButton downBtn = new FButton("downBtn", "����");
			downBtn.addActionListener(new DownActionListener());

			// �����Ű�ť
			FButton lParenthesisBtn = new FButton("lParenthesisBtn", "������");
			lParenthesisBtn.addActionListener(new LParenthesisActionListener());
			// �����Ű�ť
			FButton rParenthesisBtn = new FButton("rParenthesisBtn", "������");
			rParenthesisBtn.addActionListener(new RParenthesisActionListener());
			// ɾ���Ű�ť
			FButton delParenthesisBtn = new FButton("delParenthesisBtn", "ɾ����");
			delParenthesisBtn
					.addActionListener(new DelParenthesisActionListener());

			this.setLayout(new RowPreferedLayout(1));
			this.addControl(delBtn, new TableConstraints(1, 1, false, true));
			this.addControl(upBtn, new TableConstraints(1, 1, false, true));
			this.addControl(downBtn, new TableConstraints(1, 1, false, true));
			this.addControl(lParenthesisBtn, new TableConstraints(1, 1, false,
					true));
			this.addControl(rParenthesisBtn, new TableConstraints(1, 1, false,
					true));
			this.addControl(delParenthesisBtn, new TableConstraints(1, 1,
					false, true));
		}
	}

	/**
	 * �任����ڣ�ˢ����ʾ�������¼�
	 * 
	 */
	private class WhereTreeSelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent arg0) {

			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) ((MyTree) arg0
					.getSource()).getLastSelectedPathComponent();
			if (curTreeNode == null)
				return;

			if (!(curTreeNode.getUserObject() instanceof Caliber)) {
				return;
			}
			Caliber cal = (Caliber) curTreeNode.getUserObject();

			// ����Դ��Ͽ�
			dataSourceCbx.setValue(cal.getACal().getSourceID());

			// �����ֶ������õ��ֶ�����
			// cbxFieldName.setSelectedIndex(-1);
			JComboBox cbxFieldNameTmp = (JComboBox) cbxFieldName.getEditor();
			int count = cbxFieldNameTmp.getItemCount();
			String value;
			for (int i = 0; i < count; i++) {
				value = ((FComboBoxItem) cbxFieldNameTmp.getItemAt(i))
						.getValue().toString();
				if (value.substring(0, value.indexOf(":")).equals(
						cal.getACal().getSourceColID())) {
					cbxFieldName.setSelectedIndex(i);
					break;
				}
			}

			// �ֶ�������Ͽ�
			// cbxFieldName.setValue(cal.getACal().getSourceColID());
			// �Ƚ�����
			cbxCompare.setValue(cal.getACal().getCompareType());

			// ����sFieldEname�����ֶ�����
			String sFieldTyp = getFieldType();
			// �����ֶ������жϲ����Ӳ�������(')
			// ����ֵ
			if (DefinePub.checkCharVal(sFieldTyp)) {
				String paraValue = cal.getACal().getValue();
				// �ж��ǲ���in��not in�ȽϷ�
				if (CompareType.IN_TYPE.equalsIgnoreCase(cal.getACal()
						.getCompareType())
						|| CompareType.NOTIN_TYPE.equalsIgnoreCase(cal
								.getACal().getCompareType())) {
					// ȥ����������
					paraValue = paraValue.substring(1, paraValue.length() - 1);
					// ȥ�������Ե�����
					paraValue = paraValue.replaceAll("','", ",");
				}
				// ȥ�����������
				cbxWhereValue.setValue(paraValue.substring(1, paraValue
						.length() - 1));

			} else {
				cbxWhereValue.setValue(cal.getACal().getValue());
			}

			// ������������
			if (!Common.isNullStr(cal.getACal().getJoinBefore()))
				frdoType.setValue(cal.getACal().getJoinBefore());
		}
	}

	public IStatisticCaliber[] getWhere() {

		List lstNode = treWhere.getIsLeafNode();

		int iCount = 0;
		if (lstNode != null && lstNode.size() != 0) {
			iCount = lstNode.size();
		}
		if (isPriserve != null) {
			iCount = iCount + isPriserve.size();
		}
		if (iCount == 0)
			return null;

		IStatisticCaliber[] result = new IStatisticCaliber[iCount];

		if (lstNode != null && lstNode.size() != 0) {
			iCount = lstNode.size();
			for (int i = 0; i < iCount; i++) {
				result[i] = ((Caliber) lstNode.get(i)).aCal;
			}
		}
		if (isPriserve != null) {
			iCount = isPriserve.size();
			for (int i = 0; i < iCount; i++) {
				result[result.length - iCount + i] = (IStatisticCaliber) isPriserve
						.get(i);
			}
		}
		return result;
	}

	/**
	 * �����б���ֶ�������֯���
	 * 
	 * @param lstValue
	 * @param sFieldName
	 * @return
	 */
	public static void addRefValue(FComboBox combox, List lstValue,
			String codeField, String nameField) {
		combox.removeAllItems();

		if (lstValue != null) {
			String value = "";
			for (Iterator it = lstValue.iterator(); it.hasNext();) {
				XMLData data = (XMLData) it.next();
				if (!Common.isNullStr(value)) {
					value += "+";
				}
				value = value + data.get(codeField).toString() + "#"
						+ data.get(nameField).toString();
			}
			combox.setRefModel(value);
		}
	}

	/**
	 * ����sFieldEname�����ֶ�����
	 * 
	 * @return
	 */
	private String getFieldType() {
		if (cbxFieldName.getSelectedIndex() == -1)
			return null;
		String objField = (String) cbxFieldName.getValue();

		String[] arrField = objField.split(":");
		String fieldType = arrField[1];
		return DefinePub.getFieldTypeWithCh(fieldType);

	}

	public void refreshData() {
		// ��ʼ������
		MyCalculateValueImpl curCell = calcColumnDialog.getCurCell();
		if (curCell == null)
			return;
		IStatisticCaliber[] stacist = curCell.getStatisticCaliberArray();
		if (stacist != null) {
			for (int i = 0; i < stacist.length; i++) {
				if (DefinePub.isPACaliber(stacist[i])) {
					if (isPriserve == null)
						isPriserve = new ArrayList();
					isPriserve.add(stacist[i]);
					txtSql.setValue(txtSql.getValue() + stacist[i].toString());
					continue;
				}
				addParaTreNode(stacist[i]);
			}
		}
		// �ڵ�չ��
		treWhere.expandTree();

	}

	/**
	 * ����Դֵ�ı��¼�
	 */
	private class SelectItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {
			if (arg0.getStateChange() == ItemEvent.DESELECTED) {
				return;
			}
			String sSourceID = ((FComboBoxItem) arg0.getItem()).getValue()
					.toString();
			lstdsDictionary = DefineReportI.getMethod().getFieldWithEname(
					sSourceID);

			Map dsDictionaryMap = null;
			String sDataSourceRef = "";
			if (lstdsDictionary != null) {
				int iCount = lstdsDictionary.size();
				for (int i = 0; i < iCount; i++) {
					dsDictionaryMap = (Map) lstdsDictionary.get(i);
					if (!"".equals(sDataSourceRef)) {
						sDataSourceRef = sDataSourceRef + "+";
					}
					sDataSourceRef = sDataSourceRef
							+ dsDictionaryMap.get(IDefineReport.FIELD_ENAME)
							+ ":"
							+ dsDictionaryMap.get(IDefineReport.FIELD_TYPE)
							+ "#"
							+ dsDictionaryMap.get(IDefineReport.FIELD_FNAME);
				}
			}
			cbxFieldName.setRefModel(sDataSourceRef);
			cbxFieldName.setSelectedIndex(-1);

		}
	}

	/**
	 * ���Ӱ�ť����¼�
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// �����Ϣ�Ƿ���д����
			if (!checkInputInfo())
				return;

			String objDs = (String) dataSourceCbx.getValue();
			String objField = (String) cbxFieldName.getValue();
			String objValue = (String) cbxWhereValue.getValue();
			String objCompare = (String) cbxCompare.getValue();

			String[] arrField = objField.split(":");
			String fieldType = arrField[1];
			fieldType = DefinePub.getFieldTypeWithCh(fieldType);
			if (DefinePub.checkCharVal(fieldType)) {
				objValue = "'" + objValue + "'";
				// �ж��ǲ���in��not in�ȽϷ�
				if (CompareType.IN_TYPE.equalsIgnoreCase(objCompare)
						|| CompareType.NOTIN_TYPE.equalsIgnoreCase(objCompare)) {
					objValue = objValue.replaceAll(",", "','");
					objValue = "(" + objValue + ")";
				}
			} else {
				// ���һ���ǲ�������
				try {
					Double.parseDouble(objValue);
				} catch (Exception e) {
					new MessageBox(calcColumnDialog, "ֵ���ǺϷ�������!",
							MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
					return;
				}
			}

			IStatisticCaliber asCal = new SummaryStatisticCaliberImpl();
			asCal.setSourceID(objDs.toString());
			asCal.setSourceColID(arrField[0]);
			asCal.setCompareType(objCompare.toString());
			asCal.setValue(objValue.toString());
			asCal.setJoinBefore(frdoType.getValue().toString());
			asCal.setCaliberID(DefinePub.getRandomUUID());
			TreeNode node = addParaTreNode(asCal);

			// ˢ�½ڵ���������
			refreshNodeChName(node);
			// ��λ�������ӵĽڵ�
			treWhere.expendTo(node);
		}
	}

	/**
	 * �޸İ�ť����¼�
	 */
	private class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;

			// �����Ϣ�Ƿ���д����
			if (!checkInputInfo())
				return;

			String objDs = (String) dataSourceCbx.getValue();
			String objField = (String) cbxFieldName.getValue();
			String objValue = (String) cbxWhereValue.getValue();
			String objCompare = (String) cbxCompare.getValue();

			String[] arrField = objField.split(":");
			String fieldType = arrField[1];
			fieldType = DefinePub.getFieldTypeWithCh(fieldType);
			if (DefinePub.checkCharVal(fieldType)) {
				objValue = "'" + objValue + "'";
			} else {
				// ���һ���ǲ�������
				try {
					Double.parseDouble(objValue);
				} catch (Exception e) {
					new MessageBox(calcColumnDialog, "ֵ���ǺϷ�������!",
							MessageBox.MESSAGE, MessageBox.BUTTON_OK).show();
					return;
				}
			}

			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (defaultMutableTreeNode == null)
				return;
			IStatisticCaliber asCal = ((Caliber) defaultMutableTreeNode
					.getUserObject()).getACal();

			asCal.setSourceID(objDs.toString());
			asCal.setSourceColID(arrField[0]);
			asCal.setCompareType(objCompare.toString());
			asCal.setValue(objValue.toString());
			asCal.setJoinBefore(frdoType.getValue().toString());

			defaultMutableTreeNode.setUserObject(new Caliber(asCal));
			// ˢ�½ڵ���������
			refreshNodeChName(defaultMutableTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel())
					.nodeChanged(defaultMutableTreeNode);

		}
	}

	/**
	 * �����д����Ϣ
	 * 
	 * @return
	 */
	private boolean checkInputInfo() {
		String objDs = (String) dataSourceCbx.getValue();
		if (Common.isNullStr(objDs)) {
			new MessageBox(calcColumnDialog, "��ѡ������Դ!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}
		String objField = (String) cbxFieldName.getValue();
		if (Common.isNullStr(objField)) {
			new MessageBox(calcColumnDialog, "��ѡ��һ�ֶ�!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}

		String objCompare = (String) cbxCompare.getValue();
		if (Common.isNullStr(objCompare)) {
			new MessageBox(calcColumnDialog, "��ָ���Ƚ�����!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}
		String objValue = (String) cbxWhereValue.getValue();
		if (Common.isNullStr(objValue)) {
			new MessageBox(calcColumnDialog, "��ָ���Ƚ�ֵ!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}

		return true;
	}

	/**
	 * ɾ����ť����¼�
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (!curTreeNode.isLeaf())
				return;

			// �õ�������ڵ�
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			// �õ���������ӽڵ���
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			model.removeNodeFromParent(curTreeNode);
			// ����һ���ӽڵ㣬�ӽڵ�ɾ��ʱ��ɾ�����ڵ�
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * ���ư�ť����¼�
	 */
	private class UpActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null) {
				new MessageBox(calcColumnDialog, "��ѡ�����ڵ�!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			if (!curTreeNode.isLeaf())
				return;

			int row = treWhere.getSelectionRows()[0];
			DefaultMutableTreeNode preTreeNode = curTreeNode
					.getPreviousNode();
			if (!preTreeNode.isLeaf())
				return;

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			DefaultMutableTreeNode parTreeNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			int index = parTreeNode.getIndex(curTreeNode);

			model.removeNodeFromParent(curTreeNode);
			model.insertNodeInto(curTreeNode, parTreeNode, index - 1);
			// ˢ�½ڵ���������
			if (curTreeNode != null)
				refreshNodeChName(curTreeNode);
			if (curTreeNode.getNextSibling() != null)
				refreshNodeChName(curTreeNode.getNextSibling());
			model.nodeChanged(curTreeNode);
			model.nodeChanged(curTreeNode.getNextSibling());

			treWhere.repaint();
			treWhere.setSelectionRow(row - 1);

		}
	}

	/**
	 * ���ư�ť����¼�
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();

			if (curTreeNode == null) {
				new MessageBox(calcColumnDialog, "��ѡ�����ڵ�!", MessageBox.MESSAGE,
						MessageBox.BUTTON_OK).show();
				return;
			}
			if (!curTreeNode.isLeaf())
				return;

			int row = treWhere.getSelectionRows()[0];
			DefaultMutableTreeNode nextTreeNode = curTreeNode
					.getNextNode();
			if (!nextTreeNode.isLeaf())
				return;

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			DefaultMutableTreeNode parTreeNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			int index = parTreeNode.getIndex(curTreeNode);

			model.removeNodeFromParent(curTreeNode);
			model.insertNodeInto(curTreeNode, parTreeNode, index + 1);
			// ˢ�½ڵ���������
			if (curTreeNode != null)
				refreshNodeChName(curTreeNode);
			if (curTreeNode.getPreviousSibling() != null)
				refreshNodeChName(curTreeNode.getPreviousSibling());
			model.nodeChanged(curTreeNode);
			model.nodeChanged(curTreeNode.getPreviousSibling());

			treWhere.repaint();
			treWhere.setSelectionRow(row + 1);
		}
	}

	/**
	 * �����Ű�ť����¼�
	 */
	private class LParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;

			Caliber cal = (Caliber) curTreeNode.getUserObject();
			IStatisticCaliber myCaliber = cal.getACal();
			if (!(myCaliber instanceof ICustomStatisticCaliber)) {
				ICustomStatisticCaliber caliberTmp = new MySummaryStatisticCaliberImpl();
				caliberTmp.setAddSQL(myCaliber.getAddSQL());
				caliberTmp.setCompareType(myCaliber.getCompareType());
				caliberTmp.setJoinBefore(myCaliber.getJoinBefore());
				caliberTmp.setSourceColID(myCaliber.getSourceColID());
				caliberTmp.setSourceID(myCaliber.getSourceID());
				caliberTmp.setValue(myCaliber.getValue());
				caliberTmp.setCaliberID(DefinePub.getRandomUUID());
				cal.setACal(caliberTmp);
			}

			myCaliber = cal.getACal();
			if (((ICustomStatisticCaliber) myCaliber).getLParenthesis() != null) {
				((ICustomStatisticCaliber) myCaliber)
						.setLParenthesis(((ICustomStatisticCaliber) cal
								.getACal()).getLParenthesis()
								+ "(");
			} else {
				((ICustomStatisticCaliber) myCaliber).setLParenthesis("(");
			}
			curTreeNode.setUserObject(cal);
			// ˢ�½ڵ���������
			refreshNodeChName(curTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * �����Ű�ť����¼�
	 */
	private class RParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;

			Caliber cal = (Caliber) curTreeNode.getUserObject();
			IStatisticCaliber myCaliber = cal.getACal();
			if (!(myCaliber instanceof ICustomStatisticCaliber)) {
				ICustomStatisticCaliber caliberTmp = new MySummaryStatisticCaliberImpl();
				caliberTmp.setAddSQL(myCaliber.getAddSQL());
				caliberTmp.setCompareType(myCaliber.getCompareType());
				caliberTmp.setJoinBefore(myCaliber.getJoinBefore());
				caliberTmp.setSourceColID(myCaliber.getSourceColID());
				caliberTmp.setSourceID(myCaliber.getSourceID());
				caliberTmp.setValue(myCaliber.getValue());
				caliberTmp.setCaliberID(DefinePub.getRandomUUID());
				cal.setACal(caliberTmp);
			}

			myCaliber = cal.getACal();
			if (((ICustomStatisticCaliber) myCaliber).getRParenthesis() != null) {
				((ICustomStatisticCaliber) myCaliber)
						.setRParenthesis(((ICustomStatisticCaliber) cal
								.getACal()).getRParenthesis()
								+ ")");
			} else {
				((ICustomStatisticCaliber) myCaliber).setRParenthesis(")");
			}
			curTreeNode.setUserObject(cal);
			// ˢ�½ڵ���������
			refreshNodeChName(curTreeNode);
			// ˢ�½ڵ�
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * ɾ���Ű�ť����¼�
	 */
	private class DelParenthesisActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;
			Caliber cal = (Caliber) curTreeNode.getUserObject();
			if (cal.getACal() instanceof ICustomStatisticCaliber) {
				ICustomStatisticCaliber myCal = (ICustomStatisticCaliber) cal
						.getACal();
				myCal.setLParenthesis("");
				myCal.setRParenthesis("");
				curTreeNode.setUserObject(cal);
				// ˢ�½ڵ���������
				refreshNodeChName(curTreeNode);
				// ˢ�½ڵ�
				((DefaultTreeModel) treWhere.getModel())
						.nodeChanged(curTreeNode);
			}
		}
	}

	private class Caliber {
		/**
		 * 
		 */
		private IStatisticCaliber aCal;

		private boolean isFirstNode = false;

		public Caliber(IStatisticCaliber aCal) {
			this.aCal = aCal;

		}

		private static final long serialVersionUID = -7103191606827180022L;

		public String toString() {

			XMLData aData = (XMLData) calcColumnDialog.getXmlDsFieldToName()
					.get(aCal.getSourceID());
			if (aData == null)
				return "";
			String fieldName = (String) aData.get(aCal.getSourceColID());
			String oper = CompareType.getComparTypeName(aCal.getCompareType());
			String join = "";
			if (!isFirstNode) {
				if (aCal.getJoinBefore() == null) {
					join = "";
				} else {
					join = frdoType.getRefModel().getNameByValue(
							aCal.getJoinBefore()).substring(0, 2);
				}
			}

			// String sSourceName = definePub.getDataSourceNameWithID(
			// calcColumnDialog.getQuerySource(), aCal.getSourceID());
			String result = "";
			if (!Common.isNullStr(join)) {
				result = result + join + " ";
			}

			if (aCal instanceof ICustomStatisticCaliber) {
				if (((ICustomStatisticCaliber) aCal).getLParenthesis() != null) {
					result = result
							+ ((ICustomStatisticCaliber) aCal)
									.getLParenthesis();
				}
			}

			if (!Common.isNullStr(fieldName)) {
				result = result + fieldName + "   ";
			}
			if (!Common.isNullStr(oper)) {
				result = result + oper + "   ";
			}
			result = result + aCal.getValue();

			if (aCal instanceof ICustomStatisticCaliber) {
				if (((ICustomStatisticCaliber) aCal).getRParenthesis() != null) {
					result = result
							+ ((ICustomStatisticCaliber) aCal)
									.getRParenthesis();
				}
			}

			return result;
			// join + " "
			// + sSourceName + "."
			// + fieldName + " " + oper + " " + aCal.getValue();

		}

		public IStatisticCaliber getACal() {
			return aCal;
		}

		public void setACal(IStatisticCaliber cal) {
			aCal = cal;
		}

		public boolean isFirstNode() {
			return isFirstNode;
		}

		public void setFirstNode(boolean isFirstNode) {
			this.isFirstNode = isFirstNode;
		}

	}

	/**
	 * ���������������ӽڵ�
	 * 
	 * @param conditionNodeObj
	 */
	private TreeNode addParaTreNode(IStatisticCaliber aCal) {
		// �õ����ڵ�
		DefaultTreeModel treeModel = (DefaultTreeModel) treWhere.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// �ж����ӵĸ��ڵ��Ƿ��Ѵ���,Ĭ�ϸ��ڵ㲻����
		boolean sFlag = false;
		IStatisticCaliber curCal = null;
		DefaultMutableTreeNode curNode = null;
		int iChildCount = root.getChildCount();
		// �õ����ڵ��µ��ӽڵ�
		for (int i = 0; i < iChildCount; i++) {
			curNode = (DefaultMutableTreeNode) root.getChildAt(i);
			curCal = ((Caliber) curNode.getUserObject()).getACal();
			if (aCal.getSourceID().equals(curCal.getSourceID())) {
				sFlag = true;
				break;
			}
		}

		DefaultMutableTreeNode ANode = null;
		if (!sFlag) {// ���ڵ㲻����
			// ���Ӹ��ڵ�
			ICustomStatisticCaliber parCal = new MySummaryStatisticCaliberImpl();
			parCal.setSourceID(aCal.getSourceID());
			// ����Դ����
			String sDataSourceName = dataSourceCbx.getRefModel()
					.getNameByValue(aCal.getSourceID());
			parCal.setValue(sDataSourceName);
			ANode = new DefaultMutableTreeNode(new Caliber(parCal));
			treeModel.insertNodeInto(ANode, root, root.getChildCount());
			curNode = (DefaultMutableTreeNode) root.getLastChild();
		}
		ANode = new DefaultMutableTreeNode(new Caliber(aCal));
		treeModel.insertNodeInto(ANode, curNode, curNode.getChildCount());
		return ANode;
	}

	/**
	 * ˢ�½ڵ���������
	 * 
	 * @param node
	 */
	private void refreshNodeChName(TreeNode node) {
		TreeNode parNode = node.getParent();
		int index = parNode.getIndex(node);
		Caliber cal = (Caliber) ((DefaultMutableTreeNode) node).getUserObject();
		if (index == 0) {
			cal.setFirstNode(true);
		} else {
			cal.setFirstNode(false);
		}
	}
}
