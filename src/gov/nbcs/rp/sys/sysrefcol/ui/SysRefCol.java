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
 * 引用列管理
 * 

 */
public class SysRefCol extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 引用列
	private DataSet dsRefCol;

	private CustomTree ftreRefCol;

	// 引用列编号
	private FTextField ftxtfRefColID;

	// 引用列名称
	private FTextField ftxtfRefColName;

	// 引用列使用者
	private FTextField ftxtfDataOwner;

	// 类型
	private FRadioGroup frdoRefColKind;

	// 选取方式
	private FRadioGroup frdoSelectKind;

	// 主键字段
	private FTextField ftxtfPrimaryField;

	// 主键类型
	private FTextField ftxtfPrimaryType;

	// 代码字段
	private FTextField ftxtfCodeField;

	// 名称字段
	private FTextField ftxtfNameField;

	// 层次字段
	private FTextField ftxtfLvlField;

	// 层次风格
	private FTextField ftxtfLvlStyle;

	// 引用列内容
	private FTextArea ftxtaSqlDet;

	private final String ARRAY_KEY_KIND[] = { "数值", "字符" };

	// 控件toolbar按钮状态class
//	private SetActionStatus setActionStatus;

	private SysRefColPub sysRefColPub = new SysRefColPub();

	private SysRefColSet sysRefColSet;

	private ISysRefCol sysRefColServ = SysRefColI.getMethod();

	public SysRefCol() {
	}

	public void initize() {
		try {

			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(200);
			this.add(fSplitPane);

			// 建左边信息
			SysCodeRule codeRule = SysCodeRule.createClient(new int[] { 4 });
			ftreRefCol = new CustomTree("引用列列表", null, "REFCOL_ID", "name",
					codeRule);
			ftreRefCol
					.addTreeSelectionListener(new RefColTreeSelectionListener());
			FScrollPane fspnlRefCol = new FScrollPane();
			fspnlRefCol.addControl(ftreRefCol);
			fSplitPane.addControl(fspnlRefCol);

			// 建右边详细信息
			FPanel fpnlRightPanel = new FPanel();
			fSplitPane.addControl(fpnlRightPanel);
			{
				fpnlRightPanel.setLeftInset(10);
				fpnlRightPanel.setTopInset(10);
				fpnlRightPanel.setRightInset(10);
				fpnlRightPanel.setBottomInset(10);
				fpnlRightPanel.setLayout(new RowPreferedLayout(6));
				ftxtfRefColID = new FTextField("引用列编号：");
				ftxtfRefColName = new FTextField("引用列名称：");
				ftxtfDataOwner = new FTextField("引用列使用者：");
				ftxtfRefColID.setProportion(0.35f);
				ftxtfRefColName.setProportion(0.35f);
				ftxtfDataOwner.setProportion(0.35f);
				FLabel flblEmpty = new FLabel();

				// 类型
				FPanel fpnlRefColKind = new FPanel();
				fpnlRefColKind.setLayout(new RowPreferedLayout(1));
				fpnlRefColKind.setTitle(" 类型 ");
				fpnlRefColKind.setFontSize(this.getFont().getSize());
				fpnlRefColKind.setFontName(this.getFont().getName());
				fpnlRefColKind.setTitledBorder();
				frdoRefColKind = new FRadioGroup("", FRadioGroup.VERTICAL);
				frdoRefColKind.setTitleVisible(false);
				frdoRefColKind.setRefModel("0#用户自定义+1#系统保留");
				fpnlRefColKind.addControl(frdoRefColKind, new TableConstraints(
						1, 1, true));

				// 选取方式
				FPanel fpnlSelectKind = new FPanel();
				fpnlSelectKind.setLayout(new RowPreferedLayout(1));
				fpnlSelectKind.setTitle(" 选取方式 ");
				fpnlSelectKind.setFontSize(this.getFont().getSize());
				fpnlSelectKind.setFontName(this.getFont().getName());
				fpnlSelectKind.setTitledBorder();
				frdoSelectKind = new FRadioGroup("", FRadioGroup.VERTICAL);
				frdoSelectKind.setTitleVisible(false);
				frdoSelectKind.setRefModel("0#列表+1#树结构");
				fpnlSelectKind.addControl(frdoSelectKind, new TableConstraints(
						1, 1, true));

				// 列信息
				FPanel fpnlColInfo = new FPanel();
				fpnlColInfo.setTitle(" 列信息 ");
				fpnlColInfo.setFontSize(this.getFont().getSize());
				fpnlColInfo.setFontName(this.getFont().getName());
				fpnlColInfo.setTitledBorder();
				RowPreferedLayout rLay = new RowPreferedLayout(4);
				fpnlColInfo.setLayout(rLay);
				ftxtfPrimaryField = new FTextField("主键字段：");
				ftxtfPrimaryType = new FTextField("主键类型：");
				ftxtfCodeField = new FTextField("代码字段：");
				ftxtfNameField = new FTextField("名称字段：");
				ftxtfLvlField = new FTextField("层次字段：");
				ftxtfLvlStyle = new FTextField("层次风格：");
				ftxtfPrimaryField.setProportion(0.33f);
				ftxtfPrimaryType.setProportion(0.33f);
				ftxtfCodeField.setProportion(0.33f);
				ftxtfNameField.setProportion(0.33f);
				ftxtfLvlField.setProportion(0.33f);
				ftxtfLvlStyle.setProportion(0.33f);
				// 主键字段
				fpnlColInfo.addControl(ftxtfPrimaryField, new TableConstraints(
						1, 2, false));
				// 主键类型
				fpnlColInfo.addControl(ftxtfPrimaryType, new TableConstraints(
						1, 2, false));
				// 代码字段
				fpnlColInfo.addControl(ftxtfCodeField, new TableConstraints(1,
						2, false));
				// 名称字段
				fpnlColInfo.addControl(ftxtfNameField, new TableConstraints(1,
						2, false));
				// 层次字段
				fpnlColInfo.addControl(ftxtfLvlField, new TableConstraints(1,
						2, false));
				// 层次风格
				fpnlColInfo.addControl(ftxtfLvlStyle, new TableConstraints(1,
						2, false));

				// 引用列内容
				FPanel fpnlSqlDet = new FPanel();
				fpnlSqlDet.setLayout(new RowPreferedLayout(1));
				fpnlSqlDet.setTitle(" 引用列内容 ");
				fpnlSqlDet.setFontSize(this.getFont().getSize());
				fpnlSqlDet.setFontName(this.getFont().getName());
				fpnlSqlDet.setTitledBorder();
				ftxtaSqlDet = new FTextArea();
				ftxtaSqlDet.setTitleVisible(false);
				fpnlSqlDet.addControl(ftxtaSqlDet, new TableConstraints(1, 1,
						true));

				// 引用列编号
				fpnlRightPanel.addControl(ftxtfRefColID, new TableConstraints(
						1, 2, false));
				// 类型
				fpnlRightPanel.addControl(fpnlRefColKind, new TableConstraints(
						3, 1, false));
				// 选取方式
				fpnlRightPanel.addControl(fpnlSelectKind, new TableConstraints(
						3, 1, false));
				// 空FLabel,为布局
				fpnlRightPanel.addControl(flblEmpty, new TableConstraints(3, 2,
						false));
				// 引用列名称
				fpnlRightPanel.addControl(ftxtfRefColName,
						new TableConstraints(1, 2, false));
				// 引用列使用者
				fpnlRightPanel.addControl(ftxtfDataOwner, new TableConstraints(
						1, 2, false));
				// 列信息
				fpnlRightPanel.addControl(fpnlColInfo, new TableConstraints(5,
						4, false));
				// 引用列内容
				fpnlRightPanel.addControl(fpnlSqlDet, new TableConstraints(1,
						6, true));
			}
			this.createToolBar();
			Common.changeChildControlsEditMode(fpnlRightPanel, false);

			// 加载数据
			modulePanelActivedLoad();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示引用列界面发生错误,错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 加载数据
	 * 
	 */
	private void modulePanelActivedLoad() {
		try {
			dsRefCol = sysRefColServ.getRefColRecord(Global.loginYear);
			dsRefCol.addStateChangeListener(new RefColStateChangeListener());
			ftreRefCol.setDataSet(dsRefCol);
			ftreRefCol.reset();
			sysRefColSet = new SysRefColSet(dsRefCol);
			// 设置按钮状态
//			setActionStatus = new SetActionStatus(this);
			setToolBarState();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "引用列界面加载数据发生错误，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "增加引用列信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doDelete() {
		try {
			if (ftreRefCol.getSelectedNode() == null) {
				JOptionPane.showMessageDialog(this, "请选择一个引用列记录！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String sRefColId = dsRefCol.fieldByName("Refcol_id").getString();
			if (!checkRefColUsed(sRefColId))
				return;
			// 提示是否确定删除
			if (JOptionPane.showConfirmDialog(this, "您是否确认要删除该条记录?", "提示",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
			dsRefCol.delete();
			sysRefColServ.DeleteRefCol(sRefColId, Global.loginYear);
			dsRefCol.applyUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "删除引用列信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doCancel() {
	}

	public void doInsert() {
	}

	public void doModify() {
		if (ftreRefCol.getSelectedNode() == null) {
			JOptionPane.showMessageDialog(this, "请选择一个引用列记录！", "提示",
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
			JOptionPane.showMessageDialog(this, "修改引用列信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	/**
	 * 查看明细
	 * 
	 */
	public void doSeeList() {
		try {
			sysRefColSet.initInfo(SysRefColOperation.rcoView);
			Tools.centerWindow(sysRefColSet);
			sysRefColSet.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "查看引用列明细发生异常，错误信息:"
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
				SysCodeRule lvlRule = SysCodeRule.createClient(sLvlStyle); // 层次码规则
				SysRefColTree sysRefColTree = new SysRefColTree(sRefColName,
						ds, sFieldId, "Name", lvlRule);
				sysRefColTree.setTitle(sRefColName);
				Tools.centerWindow(sysRefColTree);
				sysRefColTree.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "引用列预览结果发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean checkRefColUsed(String sRefColId) throws Exception {
		// 判断是否已被使用
		InfoPackage infoPackage = sysRefColServ.checkRefColUsed(sRefColId,
				Global.loginYear);
		if (!infoPackage.getSuccess()) {
			JOptionPane.showMessageDialog(this, infoPackage.getsMessage(),
					"提示", JOptionPane.INFORMATION_MESSAGE);
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
						"引用列显示明细信息发生错误，错误信息:" + e1.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void showOneRecordInfo() throws Exception {
		// 引用列
		ftxtfRefColID.setValue(dsRefCol.fieldByName("REFCOL_ID").getString());
		// 引用列编号
		ftxtfRefColName.setValue(dsRefCol.fieldByName("REFCOL_NAME")
				.getString());
		// 引用列使用者
		ftxtfDataOwner.setValue(sysRefColPub.ARRAY_REFCOL_DATA_OWNER[dsRefCol
				.fieldByName("DATA_OWNER").getInteger()]);
		// 类型
		frdoRefColKind
				.setValue(dsRefCol.fieldByName("RefCol_Kind").getString());
		// 选取方式
		frdoSelectKind
				.setValue(dsRefCol.fieldByName("Select_Kind").getString());
		// 主键字段
		ftxtfPrimaryField.setValue(dsRefCol.fieldByName("Primary_Field")
				.getString());
		// 主键类型
		ftxtfPrimaryType.setValue(ARRAY_KEY_KIND[dsRefCol.fieldByName(
				"Primary_Type").getInteger()]);
		// 代码字段
		ftxtfCodeField.setValue(dsRefCol.fieldByName("Code_Field").getString());
		// 名称字段
		ftxtfNameField.setValue(dsRefCol.fieldByName("Name_Field").getString());
		// 层次字段
		ftxtfLvlField.setValue(dsRefCol.fieldByName("Lvl_Field").getString());
		// 层次风格
		ftxtfLvlStyle.setValue(dsRefCol.fieldByName("Lvl_Style").getString());
		// 引用列内容
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
//			setActionStatus.setOneBtnState("修改", false);
//			setActionStatus.setOneBtnState("删除", false);
//			setActionStatus.setOneBtnState("查看明细", false);
//			setActionStatus.setOneBtnState("预览结果", false);
//		} else {
//			if (!dsRefCol.bof() && !dsRefCol.eof()
//					&& dsRefCol.containsField("Refcol_Kind")
//					&& dsRefCol.fieldByName("Refcol_Kind").getInteger() == 1) {
//				setActionStatus.setOneBtnState("删除", false);
//			} else {
//				setActionStatus.setOneBtnState("删除", true);
//			}
//			setActionStatus.setOneBtnState("修改", true);
//			setActionStatus.setOneBtnState("查看明细", true);
//			setActionStatus.setOneBtnState("预览结果", true);
//		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
