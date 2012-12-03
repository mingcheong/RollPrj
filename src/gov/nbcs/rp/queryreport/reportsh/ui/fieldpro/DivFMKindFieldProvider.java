/**
 * @# ProjProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;

/**
 * 功能说明:
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class DivFMKindFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246570813697832166L;

	private String tableName = "VW_FB_U_PAYOUT_DETAIL_SH";

	private CustomTree treDivKind;

	private FScrollPane spnlBach;

	public String check() {
		if (isHorFieldPro())
			if (treDivKind.getSelectedNodeCount(true) < 1)
				return "没有选择财政管理方式！";
		return null;

	}

	void init() {
		
		reset();

	}

	

	public void reset() {
		super.reset();
		treDivKind.cancelSelected(false);
		treDivKind.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlBach.setVisible(USE_HOR.equals(cbxUseType.getValue()));
	}

	public String getHHeaderSql() {
		return " div_fmkind_code, div_fmkind ";
	}

	public String getMaxHeaderSql() {

		return " max(div_fmkind_code) div_fmkind_code,max(div_fmkind) div_fmkind ";
	}

	public String getIndentMaxHeaderSql() {
		return getMaxHeaderSql();
	}

	public String getEmptyHHeaderSql() {
		return " '' div_fmkind_code, '' div_fmkind ";
	}

	public String getTotalHHeaderSql() {
		return " '' div_fmkind_code, '总计' div_fmkind ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return null;
	}

	public String getNameFieldEName() {
		return "div_fmkind";
	}

	public String getNameFieldCName() {
		return "财政管理方式";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

	
		return null;
	}
	
	public String getFilterSql() {
		return " div_fmkind_code is not null ";
	}
	
	public String getGroupBySql() {
		return "div_fmkind_code,div_fmkind";
	}
	
	public String getOrderBySql() {
		return "div_fmkind_code";
	}

	public void setEnabled(boolean isEnabled) {
		treDivKind.setEnabled(isEnabled);
		treDivKind.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled)
			treDivKind.cancelSelected(false);
	}
	
	public int getCurrLvlLength() {
		return 3;
	}
	
	public int getMaxLvlLength() {
		return 3;
	}

}
