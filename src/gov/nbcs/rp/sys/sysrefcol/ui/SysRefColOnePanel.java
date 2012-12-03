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
 * �����й���,����������Ϣ���ã���һ��ҳ�棩

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

	// ����DetSql��ѯ��DataSet
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
		ftxtfRefColID = new FTextField("�����б�ţ�");
		ftxtfRefColID.setProportion(0.4f);
		ftxtfRefColID.setEditable(false);

		ftxtfRefColName = new FTextField("���������ƣ�");
		ftxtfRefColName.setProportion(0.24f);

		ftxtfRefColKind = new FTextField("���������ͣ�");
		ftxtfRefColKind.setProportion(0.4f);
		ftxtfRefColKind.setEditable(false);

		fcbxDataOwner = new FComboBox("������ʹ���ߣ�");
		fcbxDataOwner.setProportion(0.24f);
		Vector vector = new Vector();
		for (int i = 0; i < sysRefColPub.ARRAY_REFCOL_DATA_OWNER.length; i++)
			vector.add(i, sysRefColPub.ARRAY_REFCOL_DATA_OWNER[i]);
		fcbxDataOwner.setRefModel(vector);
		fcbxDataOwner.setSelectedIndex(-1);

		// ѡȡ��ʽPanel
		FPanel fpnlSelectKind = new FPanel();
		fpnlSelectKind.setLayout(new RowPreferedLayout(1));
		fpnlSelectKind.setTitle(" ѡȡ��ʽ ");
		frdoSelectKind = new FRadioGroup("", FRadioGroup.HORIZON);
		frdoSelectKind.setProportion(0.15f);
		frdoSelectKind.setRefModel("0#�б�+1#���ṹ");
		frdoSelectKind.setValue("0");
		fpnlSelectKind.addControl(frdoSelectKind, new TableConstraints(1, 1,
				true, true));

		// ����������
		FTabbedPane ftabPnlCtrlSQL = new FTabbedPane();
		FTitledPanel ftitPnlCtrlSQL1 = new FTitledPanel();
		ftitPnlCtrlSQL1.setLayout(new RowPreferedLayout(1));
		ftabPnlCtrlSQL.addControl("����������", ftitPnlCtrlSQL1);
		ftxtaSqlDet = new FTextArea();
		ftxtaSqlDet.setTitleVisible(false);
		ftitPnlCtrlSQL1.addControl(ftxtaSqlDet, new TableConstraints(1, 1,
				true, true));

		// �����б��
		this.addControl(ftxtfRefColID, new TableConstraints(1, 1, false, true));
		// ����������
		this.addControl(ftxtfRefColName,
				new TableConstraints(1, 2, false, true));
		// ѡȡ��ʽPanel
		this
				.addControl(fpnlSelectKind, new TableConstraints(2, 1, false,
						true));
		// ����������
		this.addControl(ftxtfRefColKind,
				new TableConstraints(1, 1, false, true));
		// ������ʹ����
		this.addControl(fcbxDataOwner, new TableConstraints(1, 2, false, true));
		// ����������
		this.addControl(ftabPnlCtrlSQL, new TableConstraints(1, 4, true, true));
	}

	/**
	 * ��ʾ��¼��Ϣ,����Dataset����
	 * 
	 * @param dsRefCol
	 * @param sOperationType,��������,add,mod,show
	 */
	public void showInfo(DataSet dsRefCol, int iOperationType) throws Exception {
		this.dsRefCol = dsRefCol;
		this.iOperationType = iOperationType;
		ftxtfRefColID.setValue(dsRefCol.fieldByName("RefCol_id").getString());
		if (dsRefCol.fieldByName("RefCol_Kind").getInteger() == 0)
			ftxtfRefColKind.setValue("�û��Զ���");
		else if (dsRefCol.fieldByName("RefCol_Kind").getInteger() == 1)
			ftxtfRefColKind.setValue("ϵͳ����");
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
		// �鿴��ϸ�������пؼ����ɱ༭
		if (iOperationType == SysRefColOperation.rcoView) {
			setAllControlEnabledFalse();
		} else {
			setAllControlEnabledTrue();
		}
	}

	/**
	 * �ж���д����Ϣ�Ƿ���������ȷ
	 * 
	 * @throws Exception
	 * 
	 */
	private InfoPackage CheckFillInfo() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
		if ("".equals(ftxtfRefColName.getValue().toString().trim())) {
			ftxtfRefColName.setFocus();
			infoPackage.setsMessage("���������������ƣ�");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if (fcbxDataOwner.getValue() == null) {
			fcbxDataOwner.setFocus();
			infoPackage.setsMessage("��ѡ��������ʹ���ߣ�");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		if ("".equals(ftxtaSqlDet.getValue().toString().trim())) {
			ftxtaSqlDet.setFocus();
			infoPackage.setsMessage("���������������ݣ�");
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		// ��������������Ƿ��ظ�
		String sCode = null;
		if (iOperationType != SysRefColOperation.rcoAdd)
			sCode = dsRefCol.fieldByName("RefCol_Id").getString();
		if (!sysRefColServ.checkRefNameUsed(ftxtfRefColName.getValue()
				.toString(), sCode, Global.loginYear)) {
			ftxtfRefColName.setFocus();
			infoPackage.setsMessage("�����������ظ���");
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
		saveInfo();// ������д��Ϣ��DataSet
		return infoPackage;
	}

	/**
	 * ���SQL���Ƿ���ȷ
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
	 * ���SQL����Ч�Ժ��޺����
	 * 
	 */
	private InfoPackage checkSQLValid(String sSql) {
		InfoPackage infoPackage = new InfoPackage();
		String sSqlTmp = sSql.toUpperCase().trim();
		if (sSqlTmp.indexOf("SELECT") != 0) {
			infoPackage.setsMessage("������SQL��������select��ͷ��");
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
					.setsMessage("�����в��ܺ��в�����ؼ��֣�Drop��Alter��Create��\n�͸��±�Ĺؼ���(Delete��Insert��Update)");
			ftxtaSqlDet.setFocus();
			infoPackage.setSuccess(false);
			return infoPackage;
		}
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	/**
	 * ���������в�ѯ���ݼ�
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
	 * �������пؼ����ɱ༭
	 * 
	 */
	private void setAllControlEnabledFalse() {
		Common.changeChildControlsEditMode(this, false);
	}

	/**
	 * �������пؼ��ɱ༭
	 * 
	 */
	private void setAllControlEnabledTrue() {
		Common.changeChildControlsEditMode(this, true);
		ftxtfRefColID.setEditable(false);
		ftxtfRefColKind.setEditable(false);
	}

	/**
	 * �ж��Ƿ���searchStr�����ַ�
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
