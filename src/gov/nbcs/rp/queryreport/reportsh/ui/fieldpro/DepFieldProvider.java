/**
 * @# DivFieldProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.AbastractFilterPanel;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;

/**
 * 功能说明:处室提供器
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class DepFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4569816001984723158L;

	private CustomTree treDep;

	// private FComboBox cbxLvl;

	FScrollPane spnlTree;

	SearchObj obj;

	// public static String REF_LEVEL = "0#支出处室+" + "1#主管部门+" + "5#全部单位";

	public String check() {
		if (isHorFieldPro()) {
			if (treDep.getSelectedNodeCount(true) < 1)
				return "没有选择支出处室!";
		}
		return null;
	}

	void init() {
		try {
//			cbxLvl = new FComboBox();
//			cbxLvl.setTitle("级次选择");
//			cbxLvl.setRefModel(REF_LEVEL);
//			cbxLvl.setValue("" + 5);
//			cbxLvl.setPreferredSize(new Dimension(100, 20));
//			cbxLvl.addValueChangeListener(new ValueChangeListener() {
//				public void valueChanged(ValueChangeEvent arg0) {
//					setTypeChange(IFilterProvider.TYPE_DIV, ""
//							+ arg0.getNewValue());
//				}
//			});
//			cbxLvl.setEnabled(false);
			this.setTitle("支出处室");
			DataSet ds = AbastractFilterPanel.getSelectDs(
					IFilterProvider.TYPE_DEP, "" + 0);
//			treDep = new CustomTree("支出处室", ds, "EID", "code_name",
//					IBasInputTable.PARENT_ID, null, IPubInterface.DIV_CODE,
//					true);
			if (ds.getRecordCount() < 200)
				treDep.expandAll();
			treDep.setIsCheckBoxEnabled(true);
			spnlTree = new FScrollPane(treDep);
//			getBodyPanel().add(cbxLvl, BorderLayout.NORTH);
			getBodyPanel().add(spnlTree, BorderLayout.CENTER);

//			cbxLvl.addMouseListener(comCick);
			spnlTree.addMouseListener(comCick);
			treDep.addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("初始化支出处室选择失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		treDep.cancelSelected(false);
		treDep.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlTree.setVisible(USE_HOR.equals(cbxUseType.getValue()));
	}

	public String getHHeaderSql() {
		return " dep_code,dep_name ";
	}

	public String getMaxHeaderSql() {

		return " max(dep_code) dep_code,max(dep_name) dep_name ";
	}

	public String getIndentMaxHeaderSql() {
		return getMaxHeaderSql();
	}

	public String getHHeaderSqlAlias() {
		return " b.dep_code,b.dep_name ";
	}

	public String getEmptyHHeaderSql() {
		return " '' dep_code,'' dep_name ";
	}
	
	public String getTotalHHeaderSql() {
		return " '' dep_code,'总计' dep_name ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return "支出处室编码";
	}

	public String getNameFieldEName() {
		return "dep_name";
	}

	public String getNameFieldCName() {
		return "支出处室";
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {

		//TODO　如果是业务处室，则要特殊处理一下
//		if (cbxLvl.getValue().equals("0")) {
			obj = FieldProFactory.genHeaderByDepart(dsHeader,
					getSelectDs(treDep), "eid", "CODE_NAME",
					"", IPubInterface.DIV_CODE,
					sumFields, IPubInterface.DIV_CODE, colBeginIndex);
//		} else
//			obj = FieldProFactory.genHeader(dsHeader, getSelectDs(treDiv),
//					"eid", "CODE_NAME", IBasInputTable.PARENT_ID,
//					IPubInterface.DIV_CODE, sumFields, IPubInterface.DIV_CODE,
//					colBeginIndex);
		obj.setTableName(tableName);
		return obj;
	}
	
	public String getFilterSql() {
		return " dep_code is not null ";
	}
	
	public String getGroupBySql() {
		return "dep_code,dep_name";
	}
	
	public String getOrderBySql() {
		return "dep_code";
	}

	public void setTypeChange(String type, String lvl) {
		try {

			spnlTree.remove(treDep);
			DataSet ds = AbastractFilterPanel.getSelectDs(type, lvl);

			treDep = new CustomTree("支出处室", ds, "eid", "CODE_NAME",
					"", null, IPubInterface.DIV_CODE,
					true);
			treDep.setIsCheckBoxEnabled(true);
			spnlTree.getViewport().add(treDep);
			if (ds.getRecordCount() < 200)
				treDep.expandAll();

		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();

		}
	}

	public void setEnabled(boolean isEnabled) {
		treDep.setEnabled(isEnabled);
//		cbxLvl.setEnabled(isEnabled);
		treDep.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled) {
			treDep.cancelSelected(false);
		}
	}

	public int getCurrLvlLength() {
		return 2;
	}
	
	public int getMaxLvlLength() {
		return 2;
	}

}
