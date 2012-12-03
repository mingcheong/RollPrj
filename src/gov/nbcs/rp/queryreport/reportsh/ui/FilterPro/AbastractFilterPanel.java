
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.BevelBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.queryreport.reportsh.ui.RepDispStub;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;

/**
 * 功能说明:
 *<P> Copyright 

 * @since java 1.4.2
 */
public abstract class AbastractFilterPanel extends FPanel implements
		IFilterProvider {
	private static Map mapType = new HashMap();

	FPanel pnlTitle;

	FLabel lblTitle;

	FPanel pnlBody;

	public AbastractFilterPanel() {
		pnlTitle = new FPanel();
		pnlTitle.setBorder();
		pnlTitle.setPreferredSize(new Dimension(90, 28));
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);

		pnlTitle.setLayout(fl);
		lblTitle = new FLabel();
		lblTitle.setText("数据");
//		pnlTitle.add(lblTitle);
//		pnlTitle.setBackground(BaseUtil.cTitle);

		this.setLayout(new BorderLayout());
		this.add(pnlTitle, BorderLayout.NORTH);
		pnlBody = new FPanel();
		pnlBody.setLayout(new BorderLayout());
		this.add(pnlBody, BorderLayout.CENTER);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(200, 385));
		this.addAncestorListener(new AncestorListener() {

			public void ancestorAdded(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			public void ancestorRemoved(AncestorEvent event) {
				mapType.clear();

			}

		});

		init();

	}

	public abstract void init();

	public boolean isSelect() {
		return true;
	}

	public FPanel getBodyPanel() {
		return pnlBody;
	}

	public abstract String getFilter(String align);

	public FPanel getFilterPanel() {
		return this;
	}

	public abstract void reset();

	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public static DataSet getSelectDs(String type, String lvl) {
		try {
			String key = type + "_" + lvl;
			if (mapType.containsKey(key)) {
				return (DataSet) mapType.get(key);
			}
			DataSet ds = null;
			String filter = "set_year=" + Global.getSetYear();
			if (type.equals(TYPE_ACCT)) {
				filter = filter + " and length(acct_code)<=" + lvl;
				ds = PubInterfaceStub.getMethod().getAcctFunc(filter);
			} else if (type.equals(TYPE_ACCT_JJ)) {
				filter = filter + " and length(acct_code_JJ)<=" + lvl;
				ds = PubInterfaceStub.getMethod().getAcctEconomy(filter);
			} else if (type.equals(TYPE_DEP)) {
				ds = PubInterfaceStub.getMethod().getDepToDivData(
						Global.getSetYear(), Integer.parseInt(lvl), true);
			} else if (type.equals(TYPE_DIV)) {
				ds = PubInterfaceStub.getMethod().getDivDataByMyRight(
						Global.getSetYear(), Integer.parseInt(lvl), true);
			} else if (type.equals(TYPE_FUN)) {
				ds = RepDispStub.getMethod().getFunderSource(
						Integer.parseInt(lvl));
			} else if (type.equals(TYPE_PRJSORT)) {
				ds = RepDispStub.getMethod()
						.getPrjSortDs(Integer.parseInt(lvl));
			} 
			mapType.put(key, ds);

			return ds;
		} catch (Exception e) {
			new MessageBox("取得数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		return null;
	}

	public static void clear() {
		mapType.clear();
	}

	public static String getLikeExp(String[] code, String fieldName) {
		if (code == null || code.length < 1)
			return null;
		int iCount = code.length;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iCount; i++) {
			sb.append(fieldName).append(" like '").append(code[i]).append("%'")
					.append(" or ");
		}
		return "(" + sb.substring(0, sb.length() - 3) + ")";
	}

	public static String getInExpress(String[] code, String expField,
			boolean isIn) {
		if (code == null || code.length == 0)
			return "";
		int iCount = code.length;
		String inExp = " not in ";
		if (isIn)
			inExp = " in ";
		int pageCount = (iCount - 1) / 400 + 1;
		StringBuffer sqlAll = new StringBuffer("(");
		int ser = 0;
		for (int i = 0; i < pageCount; i++) {
			sqlAll.append(expField).append(inExp).append(" (");
			for (int j = 0; j < 400; j++) {
				ser = i * 400 + j;
				if (ser < iCount)
					sqlAll.append("'" + code[j]).append("',");
			}
			sqlAll.delete(sqlAll.length() - 1, sqlAll.length()).append(")")
					.append(isIn ? " or  " : " and ");
		}
		return sqlAll.substring(0, sqlAll.length() - 4) + ")";
	}

//	public static DataSet getFBTypeDs() throws Exception {
//		DataSet ds = DataSet.create();
//		//人员经费
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("001");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("人员经费支出");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("001");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("002");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("公用经费支出");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("002");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("003");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("项目支出");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("003");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("004");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("待分配");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("004");
//		return ds;
//	}
//
//	public static DataSet getAcctTypeDs() throws Exception {
//		DataSet ds = DataSet.create();
//		//人员经费
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("22");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("一般预算支出");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("22");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("11");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("基金预算支出");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("budget_type_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("11");
//		return ds;
//	}

}
