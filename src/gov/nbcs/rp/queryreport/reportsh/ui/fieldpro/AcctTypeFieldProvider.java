/**
 * @# AcctProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * 功能说明:功能科目性质的处理
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
				return "没有选择功能科目性质!";
		}
		return null;
	}
	
	private DataSet createAcctTypeDs() throws Exception {
		DataSet ds = DataSet.createClient();
		ds.edit();
		ds.append();
		ds.fieldByName("acct_type_code").setValue("11");
		ds.fieldByName("acct_type_name").setValue("基金预算支出");

		ds.append();
		ds.fieldByName("acct_type_code").setValue("22");
		ds.fieldByName("acct_type_name").setValue("一般预算支出");
		
		ds.applyUpdate();
		
		return ds;		
	}

	void init() {
		try {
			this.setTitle("科目性质");
			DataSet ds = createAcctTypeDs();
				
			treAcctType = new CustomTree("科目性质", ds, "acct_type_code",
					"acct_type_name", IPubInterface.BS_PARENT_ID, null,
					"acct_type_code", true);
			treAcctType.setIsCheckBoxEnabled(true);
			treAcctType.expandAll();

			spnlTree = new FScrollPane(treAcctType);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);
			
			spnlTree.addMouseListener(comCick);
			treAcctType.addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("初始化功能科目性质选择失败!", e.getMessage(), MessageBox.ERROR,
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
		return " '总计' acct_type_code,'' acct_type_name ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return "科目性质编码";
	}

	public String getNameFieldEName() {
		return "acct_type_name";
	}

	public String getNameFieldCName() {
		return "科目性质名称";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

		DataSet selectDs = getSelectDs(treAcctType);
//		// 增加小计列
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
