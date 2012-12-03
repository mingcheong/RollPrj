package gov.nbcs.rp.sys.sysrefcol.ui;

import java.util.Vector;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * 引用列管理,引用列主信息设置（第一个页面）

 * 
 */
public class SysRefColOnePanel extends FPanel {

	private static final long serialVersionUID = 1L;

	private DataSet dsRefCol;

	private SysRefColPub sysRefColPub = new SysRefColPub();

	private FTextField ftxtfRefColID;

	private FTextField ftxtfRefColName;

	private FTextField ftxtfRefColKind;

	private FComboBox fcbxDataOwner;

	private FRadioGroup frdoSelectKind;

	private FTextArea ftxtaSqlDet;

	private int iOperationType;

	// 存入DetSql查询出DataSet
	private DataSet dsDetSql;

	private final ISysRefCol sysRefColServ = SysRefColI
			.getMethod();

	public DataSet getDsDetSql() {
		return dsDetSql;
	}

	public SysRefColOnePanel() {
		this.setTopInset(5);
		this.setBottomInset(5);
		this.setLeftInset(5);
		this.setRightInset(5);
		RowPreferedLayout rLay = new RowPreferedLayout(4);
		rLay.setRowGap(5);
		this.setLayout(rLay);
		ftxtfRefColID = new FTextField("引用列编号：");
		ftxtfRefColID.setProportion(0.4f);
		ftxtfRefColID.setEditable(false);

		ftxtfRefColName = new FTextField("引用列名称：");
		ftxtfRefColName.setProportion(0.24f);

		ftxtfRefColKind = new FTextField("引用列类型：");
		ftxtfRefColKind.setProportion(0.4f);
		ftxtfRefColKind.setEditable(false);

		fcbxDataOwner = new FComboBox("引用列使用者：");
		fcbxDataOwner.setProportion(0.24f);
		Vector vector = new Vector();
		for (int i = 0; i < sysRefColPub.ARRAY_REFCOL_DATA_OWNER.length; i++)
			vector.add(i, sysRefColPub.ARRAY_REFCOL_DATA_OWNER[i]);
		fcbxDataOwner.setRefModel(vector);
		fcbxDataOwner.setSelectedIndex(-1);

		// 选取方式Panel
		FPanel fpnlSelectKind = new FPanel();
		fpnlSelectKind.setLayout(new RowPreferedLayout(1));
		fpnlSelectKind.setTitle(" 选取方式 ");
		frdoSelectKind = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoSelectKind.setProportion(0.15f);
		frdoSelectKind.setRefModel("0#列表+1#树结构");
		frdoSelectKind.setValue("0");
		fpnlSelectKind.addControl(frdoSelectKind, new TableConstraints(1, 1,
				true, true));

		// 引用列内容
		FTabbedPane ftabPnlCtrlSQL = new FTabbedPane();
		FTitledPanel ftitPnlCtrlSQL1 = new FTitledPanel();
		ftitPnlCtrlSQL1.setLayout(new RowPreferedLayout(1));
		ftabPnlCtrlSQL.addControl("引用列内容", ftitPnlCtrlSQL1);
		ftxtaSqlDet = new FTextArea();
		ftxtaSqlDet.setTitleVisible(false);
		ftitPnlCtrlSQL1.addControl(ftxtaSqlDet, new TableConstraints(1, 1,
				true, true));

		// 引用列编号
		this.addControl(ftxtfRefColID, new TableConstraints(1, 1, false, true));
		// 引用列名称
		this.addControl(ftxtfRefColName,
				new TableConstraints(1, 2, false, true));
		// 选取方式Panel
		this
				.addControl(fpnlSelectKind, new TableConstraints(2, 1, false,
						true));
		// 引用列类型
		this.addControl(ftxtfRefColKind,
				new TableConstraints(1, 1, false, true));
		// 引用列使用者
		this.addControl(fcbxDataOwner, new TableConstraints(1, 2, false, true));
		// 引用列内容
		this.addControl(ftabPnlCtrlSQL, new TableConstraints(1, 4, true, true));
	}

	/**
	 * 显示记录信息,根据Dataset内容
	 * 
	 * @param dsRefCol
	 * @param sOperationType,操作类型,add,mod,show
	 */
	public void showInfo(DataSet dsRefCol, int iOperationType) throws Exception {
		this.dsRefCol = dsRefCol;
		this.iOperationType = iOperationType;
		ftxtfRefColID.setValue(dsRefCol.fieldByName("RefCol_id").getString());
		if (dsRefCol.fieldByName("RefCol_Kind").getInteger() == 0)
			ftxtfRefColKind.setValue("用户自定义");
		else if (dsRefCol.fieldByName("RefCol_Kind").getInteger() == 1)
			ftxtfRefColKind.setValue("系统保留");
		ftxtfRefColName.setValue(dsRefCol.fieldByName("RefCol_Name")
				.getString());
		if ("".equals(dsRefCol.fieldByName("SELECT_KIND").getString()))
			frdoSelectKind.setValue("0");
		else
			frdoSelectKind.setValue(dsRefCol.fieldByName("SELECT_KIND")
					.getString());
		if (dsRefCol.fieldByName("Data_Owner").getValue() != null)
			fcbxDataOwner.setSelectedIndex(dsRefCol.fieldByName("Data_Owner")
					.getInteger());
		else
			fcbxDataOwner.setSelectedIndex(-1);
		ftxtaSqlDet.setValue(dsRefCol.fieldByName("Sql_Det").getString());
		// 查看明细，设所有控件不可编辑
		if (iOperationType == SysRefColOperation.rcoView) {
			setAllControlEnabledFalse();
		} else {
			setAllControlEnabledTrue();
		}
	}

	/**
	 * 判断填写的信息是否完整和正确
	 * 
	 * @throws Exception
	 * 
	 */
	private InfoPackage CheckFillInfo() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		if ("".equals(ftxtfRefColName.getValue().toString().trim())) {
			ftxtfRefColName.setFocus();
			infoPackage.setsMessage("请输入引用列名称！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (fcbxDataOwner.getValue() == null) {
			fcbxDataOwner.setFocus();
			infoPackage.setsMessage("请选择引用列使用者！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if ("".equals(ftxtaSqlDet.getValue().toString().trim())) {
			ftxtaSqlDet.setFocus();
			infoPackage.setsMessage("请输入引用列内容！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		// 检查引用列名称是否重复
		String sCode = null;
		if (iOperationType != SysRefColOperation.rcoAdd)
			sCode = dsRefCol.fieldByName("RefCol_Id").getString();
		if (!sysRefColServ.checkRefNameUsed(ftxtfRefColName.getValue()
				.toString(), sCode, Global.loginYear)) {
			ftxtfRefColName.setFocus();
			infoPackage.setsMessage("引用列名称重复！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage = testSql(true);
		if (!infoPackage.getSuccess())
			return infoPackage;
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public InfoPackage nextOperate() throws Exception {
		InfoPackage infoPackage = CheckFillInfo();
		if (!infoPackage.getSuccess())
			return infoPackage;
		saveInfo();// 保存填写信息到DataSet
		return infoPackage;
	}

	/**
	 * 检查SQL，是否正确
	 * 
	 * @throws Exception
	 * 
	 * @throws
	 * 
	 */
	public InfoPackage testSql(boolean bBuildDataSet) throws Exception {
		String sDetSql = ftxtaSqlDet.getValue().toString().trim();
		sDetSql = SysRefColPub.ReplaceRefColFixFlag(sDetSql.toUpperCase());
		InfoPackage infoPackage = checkSQLValid(sDetSql);
		if (!infoPackage.getSuccess())
			return infoPackage;
		try {
			if (bBuildDataSet)
				dsDetSql = buildSQLQueryDataSet(sDetSql);
			else
				sysRefColServ.exeSql(sDetSql);
		} catch (Exception e) {
			infoPackage.setsMessage(e.getMessage());
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 检查SQL的有效性和无害语句
	 * 
	 */
	private InfoPackage checkSQLValid(String sSql) {
		InfoPackage infoPackage = new InfoPackage();
		String sSqlTmp = sSql.toUpperCase().trim();
		if (sSqlTmp.indexOf("SELECT") != 0) {
			infoPackage.setsMessage("引用列SQL语句必须以select开头！");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (isSpecialLetter(sSqlTmp, "DROP")
				|| isSpecialLetter(sSqlTmp, "ALTER")
				|| isSpecialLetter(sSqlTmp, "CREATE")
				|| isSpecialLetter(sSqlTmp, "DELETE")
				|| isSpecialLetter(sSqlTmp, "INSERT")
				|| isSpecialLetter(sSqlTmp, "UPDATE")) {
			// if (sSqlTmp.indexOf("DROP") >= 0 || sSqlTmp.indexOf("ALTER") >= 0
			// || sSqlTmp.indexOf("CREATE") >= 0
			// || sSqlTmp.indexOf("DELETE") >= 0
			// || sSqlTmp.indexOf("INSERT") >= 0
			// || sSqlTmp.indexOf("UPDATE") >= 0) {
			infoPackage
					.setsMessage("引用列不能含有操作表关键字（Drop、Alter、Create）\n和更新表的关键字(Delete、Insert、Update)");
			ftxtaSqlDet.setFocus();
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * 创建引用列查询数据集
	 * 
	 * @param sDetSql
	 * @throws Exception
	 */
	private DataSet buildSQLQueryDataSet(String sDetSql) throws Exception {
		return sysRefColServ.exeSqlDs(sDetSql);
	}

	private void saveInfo() throws Exception {
		dsRefCol.fieldByName("RefCol_Name")
				.setValue(ftxtfRefColName.getValue());
		dsRefCol.fieldByName("Data_Owner").setValue(
				new Integer(fcbxDataOwner.getSelectedIndex()));
		dsRefCol.fieldByName("Sql_Det").setValue(
				ftxtaSqlDet.getValue().toString().trim());
		dsRefCol.fieldByName("SELECT_KIND").setValue(frdoSelectKind.getValue());
		dsRefCol.fieldByName("set_year").setValue(Global.loginYear);
		dsRefCol.fieldByName(ISysRefCol.RG_CODE).setValue(
				Global.getCurrRegion());
		dsRefCol.fieldByName("name").setValue(
				"[" + dsRefCol.fieldByName("refcol_id").getString() + "] "
						+ ftxtfRefColName.getValue());
	}

	/**
	 * 设置所有控件不可编辑
	 * 
	 */
	private void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(this, false);
	}

	/**
	 * 设置所有控件可编辑
	 * 
	 */
	private void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(this, true);
		ftxtfRefColID.setEditable(false);
		ftxtfRefColKind.setEditable(false);
	}

	/**
	 * 判断是否含有searchStr特殊字符
	 * 
	 * @param str
	 * @param searchStr
	 * @return
	 */
	private boolean isSpecialLetter(String str, String searchStr) {
		int beginIndex = 0;
		int len = searchStr.length();
		while (true) {
			int index = str.substring(beginIndex).indexOf(searchStr);
			if (index == -1) {
				return false;
			}

			int preIndex = index - 1;
			int backIndex = index + len;
			if (preIndex != -1) {
				if (!Character.isWhitespace(str.charAt(preIndex))) {
					beginIndex = backIndex;
					continue;
				}
			}
			if (backIndex == str.length()) {
				if (!Character.isWhitespace(str.charAt(backIndex))) {
					beginIndex = backIndex;
					continue;
				}
			}
			return true;
		}
	}
}
