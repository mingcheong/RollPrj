/**
 * @# AcctProvider.java    <文件名>
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
 * 功能说明:功能科目的处理
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class AcctFieldProvider extends AbstractFieldProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4569816001984723158L;

	private CustomTree treAcct;

	private FComboBox cbxLvl;
	
	private FCheckBox chkGroupUp;

	FScrollPane spnlTree;

	SearchObj obj;

	public static String REF_LEVEL = IFilterProvider.LVL_1 + "#类+"
			+ IFilterProvider.LVL_2 + "#款+" + IFilterProvider.LVL_3 + "#项";

	public String check() {
		if (isHorFieldPro()) {
			if (treAcct.getSelectedNodeCount(true) < 1)
				return "没有选择功能科目!";
		}
		return null;
	}

	void init() {
		try {
			cbxLvl = new FComboBox();
			cbxLvl.setTitle("级次选择");
			cbxLvl.setRefModel(REF_LEVEL);
			cbxLvl.setValue(IFilterProvider.LVL_1);
			cbxLvl.setPreferredSize(new Dimension(100, 20));
			cbxLvl.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent arg0) {
					setTypeChange(IFilterProvider.TYPE_ACCT, ""
							+ arg0.getNewValue());
				}
			});
			cbxLvl.getEditor().addMouseListener(comCick);
			cbxLvl.setEnabled(false);
			
			chkGroupUp = new FCheckBox();
			chkGroupUp.setTitle(" 按级次汇总");
			chkGroupUp.setTitlePosition("right");
			chkGroupUp.setEnabled(false);

			this.setTitle("功能科目");
			DataSet ds = AbastractFilterPanel.getSelectDs(
					IFilterProvider.TYPE_ACCT, IFilterProvider.LVL_1);
			treAcct = new CustomTree("功能科目", ds, IPubInterface.BS_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BS_PARENT_ID, null,
					IPubInterface.ACCT_CODE, true);
			treAcct.setIsCheckBoxEnabled(false);
			if (ds.getRecordCount() < 200)
				treAcct.expandAll();

			spnlTree = new FScrollPane(treAcct);
			getBodyPanel().add(cbxLvl, BorderLayout.NORTH);
			FPanel pnl = new FPanel();
			pnl.setLayout(new BorderLayout());
			pnl.add(chkGroupUp, BorderLayout.NORTH);
			pnl.add(spnlTree, BorderLayout.CENTER);
			getBodyPanel().add(pnl, BorderLayout.CENTER);
			cbxLvl.addMouseListener(comCick);
			chkGroupUp.addMouseListener(comCick);
			spnlTree.addMouseListener(comCick);
			treAcct.addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("初始化功能科目选择失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		treAcct.cancelSelected(false);
		treAcct.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlTree.setVisible(USE_HOR.equals(cbxUseType.getValue()));
		chkGroupUp.setVisible(USE_VER.equals(cbxUseType.getValue()));
		cbxLvl.setVisible(enable);
	}

	public String getHHeaderSql() {

		return " acct_code,acct_name ";
	}

	public String getMaxHeaderSql() {

		return " max(acct_code) acct_code,max(acct_name) acct_name ";
	}

	public String getIndentMaxHeaderSql() {

		return " max(acct_code) acct_code," +
				"substr('            ',0,decode(len(max(acct_code)),3,0,5,2,7,4,0))||max(acct_name) acct_name ";
	}

	public String getHHeaderSqlAlias() {

		return " b.acct_code,b.acct_name ";
	}

	public String getEmptyHHeaderSql() {
		return " '' acct_code,'' acct_name ";
	}
	
	public String getTotalHHeaderSql() {
		return " '总计' acct_code,'' acct_name ";
	}

	public String getCodeFieldEName() {
		return "acct_code";
	}

	public String getCodeFieldCName() {
		return "功能科目编码";
	}

	public String getNameFieldEName() {
		return "acct_name";
	}

	public String getNameFieldCName() {
		return "功能科目名称";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

		DataSet selectDs = getSelectDs(treAcct);
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
				IPubInterface.BS_ID, IPubInterface.ACCT_FNAME,
				IPubInterface.BS_PARENT_ID, IPubInterface.ACCT_CODE, sumFields,
				IPubInterface.ACCT_CODE, colBeginIndex);
		obj.setTableName(tableName);
		return obj;
	}
	
	public int getCurrLvlLength() {
		return Integer.parseInt(cbxLvl.getValue().toString());
	}

//	public String getSqlGroup(String fields, String sumFields, String tableName, List lstV, int idx, int topCount) {
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
//			return genGroupSql(lvlPro, currLvl, "acct_code", fields,
//					sumFields, "VW_FB_ACCT_FUNC", "acct_code", "acct_name", idx, lstV, this);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "";
//		}
//
//		/*	String sSql = "select " + getHHeaderSql() + "," + fields + " from "
//						+ tableName + getFilter()
//						+ " group by acct_code,acct_name order by acct_code ";
//				return sSql;*/
//	}
	
	public int getMaxLvlLength() {
		return 7;
	}
	
	public String getFilterSql() {
		return " acct_code is not null ";
	}
	
	public String getGroupBySql() {
		return "acct_code";
	}
	
	public String getOrderBySql() {
		return "acct_code";
	}

	public void setTypeChange(String type, String lvl) {
		try {

			spnlTree.remove(treAcct);
			DataSet ds = AbastractFilterPanel.getSelectDs(type, lvl);

			treAcct = new CustomTree("功能科目", ds, IPubInterface.BS_ID,
					IPubInterface.ACCT_FNAME, IPubInterface.BS_PARENT_ID, null,
					IPubInterface.ACCT_CODE, true);
			if (ds.getRecordCount() < 200)
				treAcct.expandAll();
			treAcct.setIsCheckBoxEnabled(true);
			spnlTree.getViewport().add(treAcct);

		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();

		}
	}

	public void setEnabled(boolean isEnabled) {
		treAcct.setEnabled(isEnabled);
		treAcct.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled) {
			treAcct.cancelSelected(false);
		}
		cbxLvl.setEnabled(!USE_NO_USE.equals(cbxUseType.getValue()));
		chkGroupUp.setEnabled(USE_VER.equals(cbxUseType.getValue()));
	}

	public boolean isGroupUp() {
		return chkGroupUp.getValue() != null
				&& ((Boolean) chkGroupUp.getValue()).booleanValue();
	}

}
