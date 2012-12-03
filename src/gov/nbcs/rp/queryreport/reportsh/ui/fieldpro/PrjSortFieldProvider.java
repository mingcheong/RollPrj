/**
 * @# PrjectFiledProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.reportsh.ui.RepDispStub;
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
 * 功能说明:项目类型提供
 *<P> Copyright 

 */
public class PrjSortFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246573213697832166L;

	private String tableName = "VW_FB_U_PAYOUT_DETAIL_SH";

	private CustomTree trePrjtype;

	private FComboBox cbxLvl;
	
	private FCheckBox chkGroupUp;

	private FScrollPane spnlBach;

	public static final String ref = "3#一级+6#二级";
	
	public String check() {
		if (isHorFieldPro())
			if (trePrjtype.getSelectedNodeCount(true) < 1)
				return "没有选择项目类别！";
		return null;
	}

	void init() {
		setTitle("项目类别");
		try {
			cbxLvl = new FComboBox();
			cbxLvl.setTitle("级次选择");
			cbxLvl.setRefModel(ref);
			cbxLvl.setValue("3");
			cbxLvl.setPreferredSize(new Dimension(100, 20));
			cbxLvl.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent arg0) {
					setTypeChange(IFilterProvider.TYPE_PRJSORT, ""
							+ arg0.getNewValue());
				}
			});
			cbxLvl.setEnabled(false);
			
			chkGroupUp = new FCheckBox();
			chkGroupUp.setTitle("按级次汇总");
			chkGroupUp.setTitlePosition("right");
			getBodyPanel().add(chkGroupUp, BorderLayout.NORTH);
			
			DataSet ds = RepDispStub.getMethod().getPrjSortDs(3);

//			trePrjtype = new CustomTree("项目类别", ds, IBasInputTable.FIELD_CODE,
//					IBasInputTable.FIELD_CNAME, IBasInputTable.PARENT_ID, null,
//					IBasInputTable.FIELD_CODE, true);
			spnlBach = new FScrollPane(trePrjtype);

			getBodyPanel().add(cbxLvl, BorderLayout.NORTH);
			FPanel pnl = new FPanel();
			pnl.setLayout(new BorderLayout());
			pnl.add(chkGroupUp, BorderLayout.NORTH);
			pnl.add(spnlBach, BorderLayout.CENTER);
			getBodyPanel().add(pnl, BorderLayout.CENTER);

			cbxLvl.addMouseListener(comCick);
			cbxLvl.getEditor().addMouseListener(comCick);
			spnlBach.addMouseListener(comCick);
			chkGroupUp.addMouseListener(comCick);
			trePrjtype.addMouseListener(comCick);
			trePrjtype.expandAll();
		} catch (Exception e) {
			new MessageBox("初始化项目类别失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		trePrjtype.cancelSelected(false);
		trePrjtype.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		chkGroupUp.setVisible(USE_VER.equals(cbxUseType.getValue()));
		spnlBach.setVisible(USE_HOR.equals(cbxUseType.getValue()));
		cbxLvl.setVisible(enable);
	}

	public String getHHeaderSql() {
		return " prjsort_code,prjsort_name ";
	}

	public String getMaxHeaderSql() {

		return " max(prjsort_code) prjsort_code,max(prjsort_name) prjsort_name ";
	}

	public String getIndentMaxHeaderSql() {

		return " max(prjsort_code) prjsort_code," +
				"substr('            ',0,(len(max(prjsort_code))/3-1)*2)||max(prjsort_name) prjsort_name ";
	}

	public String getHHeaderSqlAlias() {
		return " b.prjsort_code,b.prjsort_name ";
	}

	public String getEmptyHHeaderSql() {
		return " '' prjsort_code,'' prjsort_name ";
	}
	
	public String getTotalHHeaderSql() {
		return " '' prjsort_code,'总计' prjsort_name ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return "项目类别编码";
	}

	public String getNameFieldEName() {
		return "prjsort_name";
	}

	public String getNameFieldCName() {
		return "项目类别";
	}

	public void setTypeChange(String type, String lvl) {
		try {

			spnlBach.remove(trePrjtype);
			DataSet ds = AbastractFilterPanel.getSelectDs(type, lvl);

//			trePrjtype = new CustomTree("项目类别", ds, IBasInputTable.FIELD_CODE,
//					IBasInputTable.FIELD_CNAME, IBasInputTable.PARENT_ID, null,
//					IBasInputTable.FIELD_CODE, true);
			if (ds.getRecordCount() < 200)
				trePrjtype.expandAll();
			trePrjtype.setIsCheckBoxEnabled(true);
			spnlBach.getViewport().add(trePrjtype);

		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();

		}
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

       return null;
	}
//
//	public String getSqlGroup(String fields, String sumFields, String tableName, List lstV, int idx, int topCount) {
//		LevelProvider lvlPro = new LevelProvider(SysCodeRule
//				.createClient(new int[] { 3, 6 }), "000000");
//		try {
//			String currLvl = "000000";
//			try {
//				int iLvl = getCurrLvlLength();
////				currLvl = BaseUtil.stufCharPre("", iLvl, '0');
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			return genGroupSql(lvlPro, currLvl, "PRJSORT_CODE",
//					fields, sumFields, "FB_E_PRJSORT", "PRJSORT_CODE", "PRJSORT_NAME", idx, lstV, this);
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
//	
	public int getMaxLvlLength() {
		return 6;
	}

	public int getCurrLvlLength() {
		return Integer.parseInt(cbxLvl.getValue().toString());
	}
	
	public String getFilterSql() {
		return " prjsort_code is not null ";
	}
	
	public String getGroupBySql() {
		return "prjsort_code";
	}
	
	public String getOrderBySql() {
		return "prjsort_code";
	}

	public void setEnabled(boolean isEnabled) {
		trePrjtype.setEnabled(isEnabled);
		cbxLvl.setEnabled(!USE_NO_USE.equals(cbxUseType.getValue()));
		chkGroupUp.setEnabled(USE_VER.equals(cbxUseType.getValue()));
		trePrjtype.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled)
			trePrjtype.cancelSelected(false);
	}

	public boolean isGroupUp() {
		return chkGroupUp.getValue() != null
				&& ((Boolean) chkGroupUp.getValue()).booleanValue();
	}

}
