/**
 * @# PrjectFiledProvider.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.awt.Dimension;
import java.awt.FlowLayout;

import gov.nbcs.rp.common.datactrl.DataSet;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.MessageBox;

/**
 * ����˵��:��Ŀ�ṩ
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class ProjectFieldProvider extends AbstractFieldProvider {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private static final Dimension PREFERRED_SIZE_SINGLE = new Dimension(180,
			22);
	
	private FCheckBox chkShowPrjCode;
	
	private FCheckBox chkShowSort;

	// �����ֶ�
	FComboBox cbxOrderBy;

	public static final String TYPEREF = USE_NO_USE + "#������+" + USE_VER + "#����";
	
	private static final String ORDER_BY_PRJ_CODE = "prj_code";
	public static final String ORDER_BY_PRJ_MONEY = "prj_money";
	
	public static final String ORDER_BY_REF = ORDER_BY_PRJ_CODE + "#��Ŀ����+"
	// + ORDER_BY_SORT_ALL + "#���������"
			// + ORDER_BY_SORT_CHARGE + "#���ܲ��������"
			// + ORDER_BY_SORT_DIV + "#��λ�����"
			+ ORDER_BY_PRJ_MONEY + "#��Ŀ���";

	public String check() {
		return null;
	}

	void init() {
		try {
			setTitle("��Ŀ");
			this.getBodyPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
			
			cbxUseType.setRefModel(TYPEREF);
			cbxUseType.setValue(USE_NO_USE);
			
			chkShowPrjCode = new FCheckBox();
			chkShowPrjCode.setTitle("��ʾ��Ŀ����");
			chkShowPrjCode.setTitlePosition("right");
			chkShowPrjCode.setPreferredSize(PREFERRED_SIZE_SINGLE);
			this.getBodyPanel().add(chkShowPrjCode);
			
			chkShowSort = new FCheckBox();
			chkShowSort.setTitle("��ʾ�����");
			chkShowSort.setTitlePosition("right");
			chkShowSort.setPreferredSize(PREFERRED_SIZE_SINGLE);
			// this.getBodyPanel().add(chkShowSort);

			cbxOrderBy = new FComboBox();
			cbxOrderBy.setTitle("��������");
			// cbxOrderBy.setTitleVisible(false);
			cbxOrderBy.setRefModel(ORDER_BY_REF);
			cbxOrderBy.setValue(ORDER_BY_PRJ_CODE);
			cbxOrderBy.setPreferredSize(PREFERRED_SIZE_SINGLE);
			this.getBodyPanel().add(cbxOrderBy);
			
			chkShowPrjCode.addMouseListener(comCick);
			chkShowPrjCode.getEditor().addMouseListener(comCick);			
			chkShowSort.addMouseListener(comCick);
			chkShowSort.getEditor().addMouseListener(comCick);
			cbxOrderBy.addMouseListener(comCick);
			cbxOrderBy.getEditor().addMouseListener(comCick);
		} catch (Exception e) {
			new MessageBox("��ʼ����Ŀʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		reset();

	}

	public void reset() {
		super.reset();
		chkShowPrjCode.setValue(Boolean.FALSE);
	}

	public void setStatusByUseType(boolean enable) {
		super.setStatusByUseType(enable);
		chkShowPrjCode.setVisible(enable);
		chkShowSort.setVisible(enable);
		cbxOrderBy.setVisible(enable);
	}

	public String getHHeaderSql() {
		return " prj_code,prj_name,nvl(sort_charge, '��δ����' || prj_code) sort_charge ";
	}

	public String getMaxHeaderSql() {

		return " max(prj_code) prj_code,max(prj_name) prj_name,max(sort_charge) sort_charge ";
	}

	public String getIndentMaxHeaderSql() {
		return getMaxHeaderSql();
	}

	public String getTotalHHeaderSql() {
		boolean b = chkShowPrjCode.getValue() != null
				&& ((Boolean) chkShowPrjCode.getValue()).booleanValue();
		return b ? " '�ܼ�' prj_code,'' prj_name,'' sort_charge "
				: " '' prj_code,'�ܼ�' prj_name,'' sort_charge ";
	}

	public String getEmptyHHeaderSql() {
		return " '' prj_code,'' prj_name,'' sort_charge ";
	}

	public String getCodeFieldEName() {
		boolean b = chkShowPrjCode.getValue() != null
				&& ((Boolean) chkShowPrjCode.getValue()).booleanValue();
		return b ? "prj_code" : null;
	}

	public String getCodeFieldCName() {
		return "��Ŀ����";
	}

	public String getNameFieldEName() {
		return "prj_name";
	}

	public String getNameFieldCName() {
		return "��Ŀ����";
	}

	public String[][] getExtFields() {
		return null;
	}

	public SearchObj getSearchObj(DataSet dsHeader, int colBeginIndex,
			String sumFields) throws Exception {
		return null;
	}
	
	public String getFilterSql() {
		return " prj_code is not null ";
	}
	
	public String getGroupBySql() {
		return "prj_code,prj_name,nvl(sort_charge, '��δ����' || prj_code)";
	}

	public String getOrderBySql() {
		return  "prj_code";
	}

	public boolean isOrderByMoney() {
		return ORDER_BY_PRJ_MONEY.equals(cbxOrderBy.getValue());
	}

	public boolean hasSpecifiedOrder() {
		return true;
	}

	public void setEnabled(boolean isEnabled) {
	}
	
	public int getCurrLvlLength() {
		return 24;
	}
	
	public int getMaxLvlLength() {
		return 24;
	}

}
