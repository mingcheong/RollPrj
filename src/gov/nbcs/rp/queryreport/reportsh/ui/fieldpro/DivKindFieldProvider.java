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
public class DivKindFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246570813697832166L;

	private String tableName = "VW_FB_U_PAYOUT_DETAIL_SH";

	private CustomTree treDivType;

	private FScrollPane spnlBach;

	public String check() {
		if (isHorFieldPro())
			if (treDivType.getSelectedNodeCount(true) < 1)
				return "没有选择单位类型！";
		return null;

	}

	void init() {
	



	}

	

	public void reset() {
		super.reset();
		treDivType.cancelSelected(false);
		treDivType.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlBach.setVisible(USE_HOR.equals(cbxUseType.getValue()));
	}

	public String getHHeaderSql() {
		return " div_kind_code, div_kind ";
	}

	public String getMaxHeaderSql() {

		return " max(div_kind_code) div_kind_code,max(div_kind) div_kind ";
	}

	public String getIndentMaxHeaderSql() {
		return getMaxHeaderSql();
	}

	public String getEmptyHHeaderSql() {
		return " '' div_kind_code,'' div_kind ";
	}
	
	public String getTotalHHeaderSql() {
		return " '' div_kind_code,'总计' div_kind ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return null;
	}

	public String getNameFieldEName() {
		return "div_kind";
	}

	public String getNameFieldCName() {
		return "单位类型";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

	return null;
	}
	
	public String getFilterSql() {
		return " div_kind_code is not null ";
	}
	
	public String getGroupBySql() {
		return " div_kind_code,div_kind";
	}
	
	public String getOrderBySql() {
		return " div_kind_code";
	}

	public void setEnabled(boolean isEnabled) {
		treDivType.setEnabled(isEnabled);
		treDivType.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled)
			treDivType.cancelSelected(false);
	}
	
	public int getCurrLvlLength() {
		return 3;
	}
	
	public int getMaxLvlLength() {
		return 3;
	}

}
