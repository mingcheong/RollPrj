/**
 * @# FunFieldProvider.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import gov.nbcs.rp.common.datactrl.DataSet;
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
 * 功能说明:资金来源
 *<P> Copyright 
 * <P>All rights reserved.
2
 */
public class FunFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4569816001984723158L;

	private CustomTree treAcct;

	private FComboBox cbxLvl;
	
	private FCheckBox chkGroupUp;

	FScrollPane spnlTree;

	SearchObj obj;

	private static final String ref = "1#一级+2#二级+3#三级+4#四级+5#五级";

	public String check() {
		if (isHorFieldPro()) {
			if (treAcct.getSelectedNodeCount(true) < 1)
				return "没有选择资金来源!";
		}
		return null;
	}

	void init() {
		try {
			cbxLvl = new FComboBox();
			cbxLvl.setTitle("级次选择");
			cbxLvl.setRefModel(ref);
			cbxLvl.setValue("1");
			cbxLvl.setPreferredSize(new Dimension(100, 20));
			cbxLvl.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent arg0) {
					setTypeChange(IFilterProvider.TYPE_FUN, ""
							+ arg0.getNewValue());
				}
			});
			cbxLvl.getEditor().addMouseListener(comCick);
			cbxLvl.setEnabled(false);
			
			chkGroupUp = new FCheckBox();
			chkGroupUp.setTitle(" 按级次汇总");
			chkGroupUp.setTitlePosition("right");
			chkGroupUp.setEnabled(false);

			this.setTitle("资金来源");
			DataSet ds = AbastractFilterPanel.getSelectDs(
					IFilterProvider.TYPE_FUN, "1");
			treAcct = new CustomTree("资金来源", ds, "lvl_id", "pfs_name",
					"par_id", null, "lvl_id", true);
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
			new MessageBox("初始化资金来源选择失败!", e.getMessage(), MessageBox.ERROR,
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
		return " pfs_lvl_id,pfs_name ";
	}

	public String getMaxHeaderSql() {

		return " max(pfs_lvl_id) pfs_lvl_id,max(pfs_name) pfs_name ";
	}

	public String getIndentMaxHeaderSql() {

		return " max(pfs_lvl_id) pfs_lvl_id," +
				"substr('            ',0,(len(max(pfs_lvl_id))/4-1)*2)||max(pfs_name) pfs_name ";
	}

	public String getHHeaderSqlAlias() {
		return " b.lvl_id pfs_lvl_id,b.pfs_name ";
	}

	public String getTotalHHeaderSql() {
		return " '总计' pfs_lvl_id,'' pfs_name ";
	}
	
	public String getEmptyHHeaderSql() {
		return " '' pfs_lvl_id,'' pfs_name ";
	}

	public String getCodeFieldEName() {
		return "pfs_lvl_id";
	}

	public String getCodeFieldCName() {
		return "资金来源编码";
	}

	public String getNameFieldEName() {
		return "pfs_name";
	}

	public String getNameFieldCName() {
		return "资金来源名称";
	}

//	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
//			String sumFields) throws Exception {
//
//		obj = FieldProFactory.genHeader(dsHeader, getSelectDs(treAcct),
//				IBasInputTable.LVL_ID, "pfs_name", "par_id", "lvl_id",
//				sumFields, "pfs_lvl_id", colBeginIndex);
//		obj.setTableName(tableName);
//		return obj;
//	}
//
//	public String getSqlGroup(String fields, String sumFields, String tableName, List lstV, int idx, int topCount) {
//		LevelProvider lvlPro = new LevelProvider(UntPub.lvlRule,
//				"00000000000000000000");
//		try {
//			String currLvl = "00000000000000000000";
//			try {
//				int iLvl = getCurrLvlLength();
//				currLvl = BaseUtil.stufCharPre("", iLvl, '0');
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			return genGroupSql(lvlPro, currLvl, "pfs_lvl_id",
//					fields, sumFields, "fb_iae_payout_fundsource", "lvl_id", "pfs_name", idx, lstV, this);
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
		return 20;
	}

	public int getCurrLvlLength() {
		return Integer.parseInt(cbxLvl.getValue().toString()) * 4;
	}
	
	public String getFilterSql() {
		return " pfs_lvl_id is not null ";
	}
	
	public String getGroupBySql() {
		return "pfs_lvl_id";
	}
	
	public String getOrderBySql() {
		return "pfs_lvl_id";
	}

	public void setTypeChange(String type, String lvl) {
		try {

			spnlTree.remove(treAcct);
			DataSet ds = AbastractFilterPanel.getSelectDs(type, lvl);

			treAcct = new CustomTree("资金来源", ds, "lvl_id", "pfs_name",
					"par_id", null, "lvl_id", true);
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
		chkGroupUp.setEnabled(USE_VER.equals(cbxUseType.getValue()));
		cbxLvl.setEnabled(!USE_NO_USE.equals(cbxUseType.getValue()));
	}

	public boolean isGroupUp() {
		return chkGroupUp.getValue() != null
				&& ((Boolean) chkGroupUp.getValue()).booleanValue();
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
