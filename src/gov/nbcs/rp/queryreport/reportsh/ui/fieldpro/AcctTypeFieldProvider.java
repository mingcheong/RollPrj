/**
 * @# AcctProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * ����˵��:���ܿ�Ŀ���ʵĴ���
 *<P> Copyright 

 * @since java 1.4.2
 */
public class AcctTypeFieldProvider extends AbstractFieldProvider {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4569816001984723158L;

	private CustomTree treAcctType;

	FScrollPane spnlTree;

	SearchObj obj;

	public String check() {
		if (isHorFieldPro()) {
			if (treAcctType.getSelectedNodeCount(true) < 1)
				return "û��ѡ���ܿ�Ŀ����!";
		}
		return null;
	}
	
	private DataSet createAcctTypeDs() throws Exception {
		DataSet ds = DataSet.createClient();
		ds.edit();
		ds.append();
		ds.fieldByName("acct_type_code").setValue("11");
		ds.fieldByName("acct_type_name").setValue("����Ԥ��֧��");

		ds.append();
		ds.fieldByName("acct_type_code").setValue("22");
		ds.fieldByName("acct_type_name").setValue("һ��Ԥ��֧��");
		
		ds.applyUpdate();
		
		return ds;		
	}

	void init() {
		try {
			this.setTitle("��Ŀ����");
			DataSet ds = createAcctTypeDs();
				
			treAcctType = new CustomTree("��Ŀ����", ds, "acct_type_code",
					"acct_type_name", IPubInterface.BS_PARENT_ID, null,
					"acct_type_code", true);
			treAcctType.setIsCheckBoxEnabled(true);
			treAcctType.expandAll();

			spnlTree = new FScrollPane(treAcctType);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);
			
			spnlTree.addMouseListener(comCick);
			treAcctType.addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("��ʼ�����ܿ�Ŀ����ѡ��ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		treAcctType.cancelSelected(false);
		treAcctType.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlTree.setVisible(USE_HOR.equals(cbxUseType.getValue()));
	}

	public String getHHeaderSql() {

		return " acct_type_code,acct_type_name ";
	}

	public String getMaxHeaderSql() {

		return " max(acct_type_code) acct_type_code,max(acct_type_name) acct_type_name ";
	}

	public String getIndentMaxHeaderSql() {

		return " max(acct_type_code) acct_type_code," +
				"max(acct_type_name) acct_type_name ";
	}

	public String getHHeaderSqlAlias() {

		return " b.acct_type_code,b.acct_type_name ";
	}

	public String getEmptyHHeaderSql() {
		return " '' acct_type_code,'' acct_type_name ";
	}
	
	public String getTotalHHeaderSql() {
		return " '�ܼ�' acct_type_code,'' acct_type_name ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return "��Ŀ���ʱ���";
	}

	public String getNameFieldEName() {
		return "acct_type_name";
	}

	public String getNameFieldCName() {
		return "��Ŀ��������";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

		DataSet selectDs = getSelectDs(treAcctType);
//		// ����С����
//		selectDs.beforeFirst();
//		if (selectDs.next()) {
//			selectDs.insert();
//			Map subtotalData = new HashMap();
//			// subtotalData.put();
//			// subtotalData.put();
//			// subtotalData.put();
//			selectDs.setOriginData(subtotalData);
//		}
		obj = FieldProFactory.genHeader(dsHeader, selectDs,
				"acct_type_code", "acct_type_name",
				IPubInterface.BS_PARENT_ID, "acct_type_code", sumFields,
				"acct_type_code", colBeginIndex);
		obj.setTableName(tableName);
		return obj;
	}
	
	public String getFilterSql() {
		return " acct_type_code is not null ";
	}
	
	public String getGroupBySql() {
		return "acct_type_code,acct_type_name";
	}
	
	public String getOrderBySql() {
		return "acct_type_code";
	}

	public void setEnabled(boolean isEnabled) {
		treAcctType.setEnabled(isEnabled);
//		cbxLvl.setEnabled(isEnabled);
		treAcctType.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled) {
			treAcctType.cancelSelected(false);
		}
	}

	public int getCurrLvlLength() {
		return 2;
	}
	
	public int getMaxLvlLength() {
		return 2;
	}

}
