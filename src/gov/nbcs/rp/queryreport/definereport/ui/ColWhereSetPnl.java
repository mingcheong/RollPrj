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

 * 定义数据源及枚举数据源过滤条件设置面板
 */
public class ColWhereSetPnl extends FPanel {

	private static final long serialVersionUID = 1L;

	// 数据源组合框
	FComboBox dataSourceCbx = null;

	private FComboBox cbxCompare;// 比较类型

	// 条件值
	// private FTextField txtWhereValue = null;
	private FComboBox cbxWhereValue = null;

	// 定义条件类型
	private FRadioGroup frdoType = null;

	// 字段名称组合框
	private FComboBox cbxFieldName = null;

	// 条件树
	private MyTree treWhere = null;

	// Sql条件
	private FTextArea txtSql;

	private List isPriserve = null;

	private CalcColumnDialog calcColumnDialog = null;

	private List lstdsDictionary = null;

	public ColWhereSetPnl(final CalcColumnDialog calcColumnDialog) {
		this.calcColumnDialog = calcColumnDialog;

		// 数据源组合框
		dataSourceCbx = new MyFComboBox("数据源:", calcColumnDialog
				.getQuerySource());
		((JComboBox) dataSourceCbx.getComponent(1))
				.addItemListener(new SelectItemListener());

		// 字段名
		FLabel fieldNameLbl = new FLabel();
		fieldNameLbl.setTitle("字段名:");
		// 升降序选择
		FLabel operateLbl = new FLabel();
		operateLbl.setText("操作:");
		// 条件值
		FLabel whereValueLbl = new FLabel();
		whereValueLbl.setText("值:");

		// 字段名称组合框
		cbxFieldName = new FComboBox();
		cbxFieldName.setTitleVisible(false);
		((JComboBox) cbxFieldName.getComponent(1))
				.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent arg0) {
						if (cbxFieldName.getValue() == null) {
							return;
						}

						String sFieldTyp = getFieldType();
						// 判断字段类型，如果是数值型包含和不包含不显示
						if (DefinePub.checkCharVal(sFieldTyp)) {
							cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
						} else {
							cbxCompare
									.setRefModel(CompareType.COMPARE_TYPE_NUM);
						}
						cbxCompare.setSelectedIndex(-1);
						// 得到基础信息选择列的值，根据报表id和ename值
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
								new MessageBox(calcColumnDialog, "错误信息："
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

		// 比较类型
		cbxCompare = new FComboBox();
		cbxCompare.setTitleVisible(false);
		cbxCompare.setRefModel(CompareType.COMPARE_TYPE);
		cbxCompare.setSelectedIndex(-1);

		// 条件值
		// txtWhereValue = new FTextField(false);
		cbxWhereValue = new FComboBox();
		((JComboBox) cbxWhereValue.getEditor()).setEditable(true);
		cbxWhereValue.setTitleVisible(false);

		// 定义条件面板
		FPanel orderPnl = new FPanel();
		orderPnl.setLayout(new RowPreferedLayout(5));

		FLabel flblEmpty = new FLabel();
		// 数据来源
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
		// 编码框
		orderPnl.addControl(cbxFieldName, new TableConstraints(1, 2, false,
				true));
		orderPnl
				.addControl(cbxCompare, new TableConstraints(1, 1, false, true));
		orderPnl.add(cbxWhereValue, new TableConstraints(1, 2, false, true));

		// 定义条件类型
		frdoType = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoType.setRefModel(JoinBefore.JOIN_BEFORE);
		frdoType.setTitleVisible(false);
		frdoType.setValue("and");

		// 增加、修改按钮
		FButton addBtn = new FButton("addBtn", "增加");
		addBtn.addActionListener(new AddActionListener());

		FButton modifyBtn = new FButton("addBtn", "修改");
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

		// 定义条件树
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("过滤条件");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treWhere = new MyTree(treeModel);
		// 变换树节点，刷新显示框信息事件
		treWhere.addTreeSelectionListener(new WhereTreeSelectionListener());
		JScrollPane fpnltalRela = new JScrollPane(treWhere);

		// 过滤条件按钮面板
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

		pnlWhere.add("列条件信息", pnlCol);
		pnlWhere.add("分栏设置条件", spnlSql);
		this.setLayout(new BorderLayout());
		this.add(pnlWhere, BorderLayout.CENTER);
	}

	/**
	 * 过滤条件按钮面板
	 */
	private class BtnPnl extends FPanel {

		private static final long serialVersionUID = 1L;

		public BtnPnl() {
			// 删除排序条件按钮
			FButton delBtn = new FButton("delBtn", "删除");
			delBtn.addActionListener(new DelActionListener());
			// 上移按钮
			FButton upBtn = new FButton("upBtn", "上移");
			upBtn.addActionListener(new UpActionListener());
			// 下移按钮
			FButton downBtn = new FButton("downBtn", "下移");
			downBtn.addActionListener(new DownActionListener());

			// 左括号按钮
			FButton lParenthesisBtn = new FButton("lParenthesisBtn", "左括号");
			lParenthesisBtn.addActionListener(new LParenthesisActionListener());
			// 右括号按钮
			FButton rParenthesisBtn = new FButton("rParenthesisBtn", "右括号");
			rParenthesisBtn.addActionListener(new RParenthesisActionListener());
			// 删括号按钮
			FButton delParenthesisBtn = new FButton("delParenthesisBtn", "删括号");
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
	 * 变换树结节，刷新显示框内容事件
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

			// 数据源组合框
			dataSourceCbx.setValue(cal.getACal().getSourceID());

			// 根据字段列名得到字段类型
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

			// 字段名称组合框
			// cbxFieldName.setValue(cal.getACal().getSourceColID());
			// 比较类型
			cbxCompare.setValue(cal.getACal().getCompareType());

			// 根据sFieldEname返回字段类型
			String sFieldTyp = getFieldType();
			// 根据字段类型判断参数加不加引号(')
			// 条件值
			if (DefinePub.checkCharVal(sFieldTyp)) {
				String paraValue = cal.getACal().getValue();
				// 判断是不是in或not in比较符
				if (CompareType.IN_TYPE.equalsIgnoreCase(cal.getACal()
						.getCompareType())
						|| CompareType.NOTIN_TYPE.equalsIgnoreCase(cal
								.getACal().getCompareType())) {
					// 去掉左右括号
					paraValue = paraValue.substring(1, paraValue.length() - 1);
					// 去掉逗号旁的引号
					paraValue = paraValue.replaceAll("','", ",");
				}
				// 去掉最外层引号
				cbxWhereValue.setValue(paraValue.substring(1, paraValue
						.length() - 1));

			} else {
				cbxWhereValue.setValue(cal.getACal().getValue());
			}

			// 定义条件类型
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
	 * 根据列表和字段名，组织语句
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
	 * 根据sFieldEname返回字段类型
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
		// 初始化内容
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
		// 节点展开
		treWhere.expandTree();

	}

	/**
	 * 数据源值改变事件
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
	 * 增加按钮点击事件
	 */
	private class AddActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// 检查信息是否填写完整
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
				// 判断是不是in或not in比较符
				if (CompareType.IN_TYPE.equalsIgnoreCase(objCompare)
						|| CompareType.NOTIN_TYPE.equalsIgnoreCase(objCompare)) {
					objValue = objValue.replaceAll(",", "','");
					objValue = "(" + objValue + ")";
				}
			} else {
				// 检查一下是不是数字
				try {
					Double.parseDouble(objValue);
				} catch (Exception e) {
					new MessageBox(calcColumnDialog, "值不是合法的数字!",
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

			// 刷新节点中文名称
			refreshNodeChName(node);
			// 定位到新增加的节点
			treWhere.expendTo(node);
		}
	}

	/**
	 * 修改按钮点击事件
	 */
	private class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null || !curTreeNode.isLeaf())
				return;

			// 检查信息是否填写完整
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
				// 检查一下是不是数字
				try {
					Double.parseDouble(objValue);
				} catch (Exception e) {
					new MessageBox(calcColumnDialog, "值不是合法的数字!",
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
			// 刷新节点中文名称
			refreshNodeChName(defaultMutableTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel())
					.nodeChanged(defaultMutableTreeNode);

		}
	}

	/**
	 * 检查填写的信息
	 * 
	 * @return
	 */
	private boolean checkInputInfo() {
		String objDs = (String) dataSourceCbx.getValue();
		if (Common.isNullStr(objDs)) {
			new MessageBox(calcColumnDialog, "请选择数据源!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}
		String objField = (String) cbxFieldName.getValue();
		if (Common.isNullStr(objField)) {
			new MessageBox(calcColumnDialog, "请选择一字段!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}

		String objCompare = (String) cbxCompare.getValue();
		if (Common.isNullStr(objCompare)) {
			new MessageBox(calcColumnDialog, "请指定比较类型!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}
		String objValue = (String) cbxWhereValue.getValue();
		if (Common.isNullStr(objValue)) {
			new MessageBox(calcColumnDialog, "请指定比较值!", MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return false;
		}

		return true;
	}

	/**
	 * 删除按钮点击事件
	 */
	private class DelActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (!curTreeNode.isLeaf())
				return;

			// 得到父对象节点
			DefaultMutableTreeNode parNode = (DefaultMutableTreeNode) curTreeNode
					.getParent();
			// 得到父对象的子节点数
			int iChildCount = parNode.getChildCount();

			DefaultTreeModel model = (DefaultTreeModel) treWhere.getModel();
			model.removeNodeFromParent(curTreeNode);
			// 点有一个子节点，子节点删除时，删除父节点
			if (iChildCount == 1) {
				model.removeNodeFromParent(parNode);
			}
		}
	}

	/**
	 * 上移按钮点击事件
	 */
	private class UpActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();
			if (curTreeNode == null) {
				new MessageBox(calcColumnDialog, "请选择树节点!", MessageBox.MESSAGE,
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
			// 刷新节点中文名称
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
	 * 下移按钮点击事件
	 */
	private class DownActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultMutableTreeNode curTreeNode = (DefaultMutableTreeNode) treWhere
					.getLastSelectedPathComponent();

			if (curTreeNode == null) {
				new MessageBox(calcColumnDialog, "请选择树节点!", MessageBox.MESSAGE,
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
			// 刷新节点中文名称
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
	 * 左括号按钮点击事件
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
			// 刷新节点中文名称
			refreshNodeChName(curTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * 右括号按钮点击事件
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
			// 刷新节点中文名称
			refreshNodeChName(curTreeNode);
			// 刷新节点
			((DefaultTreeModel) treWhere.getModel()).nodeChanged(curTreeNode);
		}
	}

	/**
	 * 删括号按钮点击事件
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
				// 刷新节点中文名称
				refreshNodeChName(curTreeNode);
				// 刷新节点
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
	 * 参数条件树上增加节点
	 * 
	 * @param conditionNodeObj
	 */
	private TreeNode addParaTreNode(IStatisticCaliber aCal) {
		// 得到父节点
		DefaultTreeModel treeModel = (DefaultTreeModel) treWhere.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		// 判断增加的父节点是否已存在,默认父节点不存在
		boolean sFlag = false;
		IStatisticCaliber curCal = null;
		DefaultMutableTreeNode curNode = null;
		int iChildCount = root.getChildCount();
		// 得到根节点下的子节点
		for (int i = 0; i < iChildCount; i++) {
			curNode = (DefaultMutableTreeNode) root.getChildAt(i);
			curCal = ((Caliber) curNode.getUserObject()).getACal();
			if (aCal.getSourceID().equals(curCal.getSourceID())) {
				sFlag = true;
				break;
			}
		}

		DefaultMutableTreeNode ANode = null;
		if (!sFlag) {// 父节点不存在
			// 增加父节点
			ICustomStatisticCaliber parCal = new MySummaryStatisticCaliberImpl();
			parCal.setSourceID(aCal.getSourceID());
			// 数据源名称
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
	 * 刷新节点中文名称
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
