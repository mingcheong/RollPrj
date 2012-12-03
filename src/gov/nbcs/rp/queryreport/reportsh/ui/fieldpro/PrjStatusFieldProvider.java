/**
 * @# PrjStatusFieldProvider.java <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.BorderLayout;


import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;


public class PrjStatusFieldProvider extends AbstractFieldProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246570813697832166L;

	private String tableName = "VW_FB_U_PAYOUT_DETAIL_SH";

	private CustomTree trePrjStatus;

	private FScrollPane spnlBach;

	public String check() {
		if (isHorFieldPro())
			if (trePrjStatus.getSelectedNodeCount(true) < 1)
				return "û��ѡ����Ŀ״̬��";
		return null;

	}

	void init() {
		DataSet ds;
		try {
//			ds = getDivKindSelectData();

//			trePrjStatus = new CustomTree("��Ŀ״̬", ds, IBasInputTable.LVL_ID,
//					IBasInputTable.FIELD_CNAME, IBasInputTable.PARENT_ID, null,
//					IBasInputTable.FIELD_CODE, true);
			spnlBach = new FScrollPane(trePrjStatus);
			this.getBodyPanel().add(spnlBach, BorderLayout.CENTER);
			spnlBach.addMouseListener(comCick);
			trePrjStatus.addMouseListener(comCick);
			setTitle("��Ŀ״̬");

		} catch (Exception e) {
			new MessageBox("��ʼ����Ŀ״̬ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

//	private static DataSet getDivKindSelectData() throws Exception {
//		DataSet ds = DataSet.create();
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("0000");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("����ͬ��");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("prj_status_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("001");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("0001");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("�ɵ���");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("prj_status_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("002");
//
//		ds.append();
//		ds.fieldByName(IBasInputTable.LVL_ID).setValue("0002");
//		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("���ϱ�");
//		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("prj_status_code");
//		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("003");
//
////		ds.append();
////		ds.fieldByName(IBasInputTable.LVL_ID).setValue("0003");
////		ds.fieldByName(IBasInputTable.FIELD_CNAME).setValue("����");
////		ds.fieldByName(IBasInputTable.FIELD_ENAME).setValue("prj_status_code");
////		ds.fieldByName(IBasInputTable.FIELD_CODE).setValue("004");
//		return ds;
//	}

	public void reset() {
		super.reset();
		trePrjStatus.cancelSelected(false);
		trePrjStatus.repaint();

	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		spnlBach.setVisible(USE_HOR.equals(cbxUseType.getValue()));
	}

	public String getHHeaderSql() {
		return " prj_status ";
	}

	public String getMaxHeaderSql() {

		return " max(prj_status) prj_status ";
	}

	public String getIndentMaxHeaderSql() {
		return getMaxHeaderSql();
	}

	public String getEmptyHHeaderSql() {
		return " '' prj_status ";
	}

	public String getTotalHHeaderSql() {
		return " '�ܼ�' prj_status ";
	}

	public String getCodeFieldEName() {
		return null;
	}

	public String getCodeFieldCName() {
		return null;
	}

	public String getNameFieldEName() {
		return "prj_status";
	}

	public String getNameFieldCName() {
		return "��Ŀ״̬";
	}

//	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
//			String sumFields) throws Exception {
//
//		SearchObj obj = FieldProFactory.genHeader(dsHeader,
//				getSelectDs(trePrjStatus), IBasInputTable.LVL_ID,
//				IBasInputTable.FIELD_CNAME, IBasInputTable.PARENT_ID,
//				IBasInputTable.FIELD_CODE, sumFields, "prj_status",
//				colBeginIndex);
//		obj.setTableName(tableName);
//		return obj;
//	}
	
	public String getFilterSql() {
		return " prj_status is not null ";
	}
	
	public String getGroupBySql() {
		return "prj_status";
	}
	
	public String getOrderBySql() {
		return "prj_status";
	}

	public void setEnabled(boolean isEnabled) {
		trePrjStatus.setEnabled(isEnabled);
		trePrjStatus.setIsCheckBoxEnabled(isEnabled);
		if (!isEnabled)
			trePrjStatus.cancelSelected(false);
	}
	
	public int getCurrLvlLength() {
		return 3;
	}
	
	public int getMaxLvlLength() {
		return 3;
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
