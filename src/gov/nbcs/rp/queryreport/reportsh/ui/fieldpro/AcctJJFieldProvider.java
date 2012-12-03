/**
 * @# AcctJJprovider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.AbastractFilterPanel;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class AcctJJFieldProvider extends AbstractFieldProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -775100117475875618L;

	private CustomTree treAcctJJ;

	private FComboBox cbxLvl;
	
	private FCheckBox chkGroupUp;

	FScrollPane spnlTree;

	SearchObj obj;

	public static String REF_LEVEL = IFilterProvider.LVL_1 + "#类+"
			+ IFilterProvider.LVL_2 + "#款+" + IFilterProvider.LVL_3 + "#项";

	public String check() {
		if (isHorFieldPro()) {
			if (treAcctJJ.getSelectedNodeCount(true) < 1)
				return "没有选择经济科目!";
		}
		return null;
	}

	public String getFilter(String align) {
		// TODO Auto-generated method stub
		return null;
	}

	void init() {
		this.setTitle("经济科目");
		try {
			
			cbxLvl = new FComboBox();
			cbxLvl.setTitle("级次选择");
			cbxLvl.setRefModel(REF_LEVEL);
			cbxLvl.setValue(IFilterProvider.LVL_1);
			cbxLvl.setPreferredSize(new Dimension(100, 20));
			cbxLvl.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent arg0) {
					setTypeChange(IFilterProvider.TYPE_ACCT_JJ, ""
							+ arg0.getNewValue());
				}
			});
			cbxLvl.setEnabled(false);
			cbxLvl.getEditor().addMouseListener(comCick);
			
			chkGroupUp = new FCheckBox();
			chkGroupUp.setTitle(" 按级次汇总");
			chkGroupUp.setTitlePosition("right");
			chkGroupUp.setEnabled(false);
			
			DataSet ds = AbastractFilterPanel.getSelectDs(
					IFilterProvider.TYPE_ACCT_JJ, IFilterProvider.LVL_1);
			treAcctJJ = new CustomTree("经济科目", ds, IPubInterface.BSI_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BSI_PARENT_ID,
					null, IPubInterface.ACCT_CODE_JJ, true);
			treAcctJJ.setIsCheckBoxEnabled(false);
			if (ds.getRecordCount() < 200)
				treAcctJJ.expandAll();
			spnlTree = new FScrollPane(treAcctJJ);
			getBodyPanel().add(cbxLvl, BorderLayout.NORTH);
			FPanel pnl = new FPanel();
			pnl.setLayout(new BorderLayout());
			pnl.add(chkGroupUp, BorderLayout.NORTH);
			pnl.add(spnlTree, BorderLayout.CENTER);
			getBodyPanel().add(pnl, BorderLayout.CENTER);
			
			cbxLvl.addMouseListener(comCick);
			chkGroupUp.addMouseListener(comCick);
			spnlTree.addMouseListener(comCick);
			treAcctJJ.addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("初始化经济科目选择失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		treAcctJJ.cancelSelected(false);
		treAcctJJ.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlTree.setVisible(USE_HOR.equals(cbxUseType.getValue()));
		chkGroupUp.setVisible(USE_VER.equals(cbxUseType.getValue()));
		cbxLvl.setVisible(enable);
	}

	public String getHHeaderSql() {
		return " acct_code_jj,acct_name_jj ";
	}

	public String getMaxHeaderSql() {

		return " max(acct_code_jj) acct_code_jj,max(acct_name_jj) acct_name_jj ";
	}

	public String getIndentMaxHeaderSql() {

		return " max(acct_code_jj) acct_code_jj," +
				"substr('            ',0,decode(len(max(acct_code_jj)),3,0,5,2,7,4,0))||max(acct_name_jj) acct_name_jj ";
	}

	public String getHHeaderSqlAlias() {
		return " b.acct_code_jj,b.acct_name_jj ";
	}

	public String getEmptyHHeaderSql() {
		return " '' acct_code_jj,'' acct_name_jj ";
	}
	
	public String getTotalHHeaderSql() {
		return " '总计' acct_code_jj,'' acct_name_jj ";
	}

	public String getCodeFieldEName() {
		return "acct_code_jj";
	}

	public String getCodeFieldCName() {
		return "经济科目编码";
	}

	public String getNameFieldEName() {
		return "acct_name_jj";
	}

	public String getNameFieldCName() {
		return "经济科目名称";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

		obj = FieldProFactory.genHeader(dsHeader, getSelectDs(treAcctJJ),
				IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
				IPubInterface.BSI_PARENT_ID, IPubInterface.ACCT_CODE_JJ,
				sumFields, IPubInterface.ACCT_CODE_JJ, colBeginIndex);
		obj.setTableName(tableName);
		return obj;
	}

//	public String getSqlGroup(String fields, String sumFields, String tableName, List lstV, int idx, int topCount) {
//		//		String sSql = "select " + getHHeaderSql() + "," + fields + " from "
//		//				+ tableName + getFilter()+" group by acct_code_jj,acct_name_jj order by acct_code_jj ";
//		//		return sSql;
//		LevelProvider lvlPro = new LevelProvider(SysCodeRule
//				.createClient(new int[] { 3, 5, 7 }), "0000000");
//		try {
//			String currLvl = "0000000";
//			try {
//				int iLvl = getCurrLvlLength();
//				currLvl = BaseUtil.stufCharPre("", iLvl, '0');
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			return genGroupSql(lvlPro, currLvl, "acct_code_jj", fields,
//					sumFields, "VW_FB_ACCT_ECONOMY", "acct_code_jj", "acct_name_jj", idx, lstV, this);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "";
//		}
//	}
	
	public int getMaxLvlLength() {
		return 7;
	}

	public int getCurrLvlLength() {
		return Integer.parseInt(cbxLvl.getValue().toString());
	}
	
	public String getFilterSql() {
		return " acct_code_jj is not null ";
	}
	
	public String getGroupBySql() {
		return "acct_code_jj";
	}
	
	public String getOrderBySql() {
		return "acct_code_jj";
	}

	public void setTypeChange(String type, String lvl) {
		try {

			spnlTree.remove(treAcctJJ);
			DataSet ds = AbastractFilterPanel.getSelectDs(type, lvl);
			treAcctJJ = new CustomTree("经济科目", ds, IPubInterface.BSI_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BSI_PARENT_ID,
					null, IPubInterface.ACCT_CODE_JJ, true);
			treAcctJJ.setIsCheckBoxEnabled(true);
			spnlTree.getViewport().add(treAcctJJ);
			if (ds.getRecordCount() < 200)
				treAcctJJ.expandAll();

		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();

		}
	}

	public void setEnabled(boolean isEnabled) {
		treAcctJJ.setEnabled(isEnabled);
		cbxLvl.setEnabled(!USE_NO_USE.equals(cbxUseType.getValue()));
		chkGroupUp.setEnabled(USE_VER.equals(cbxUseType.getValue()));
		treAcctJJ.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled) {
			treAcctJJ.cancelSelected(false);
		}
	}

	public boolean isGroupUp() {
		return chkGroupUp.getValue() != null
				&& ((Boolean) chkGroupUp.getValue()).booleanValue();
	}

}
