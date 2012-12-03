/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;

/**
 * <p>
 * Title:ѡ��������Ŀ���ڵ���ʾ����Ԥ���Ŀ���շ���Ŀ��Ӧ��ϵ
 * </p>
 * <p>
 * Description:
 * </p>

 */
public class IncTypeToIncColTreSelListener implements TreeSelectionListener {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	/**
	 * 
	 */
	public IncTypeToIncColTreSelListener(IncType incType) {
		this.incType = incType;
	}

	public void valueChanged(TreeSelectionEvent e) {
		DataSet dsIncCol = incType.fpnlIncCol.ftreeIncColumn.getDataSet();
		if (dsIncCol == null || dsIncCol.isEmpty() || dsIncCol.bof()
				|| dsIncCol.eof() || incType.dsInccolumnToInc == null) {
			SetSelectTree
					.setIsNoCheck(incType.fpnlChoiceRela.ftreIncomeSubItem);
			SetSelectTree.setIsNoCheck(incType.fpnlChoiceRela.ftreeIncAcctitem);
			return;
		}
		try {
			// ������Ŀ����
			String sInccolCode = dsIncCol.fieldByName(IIncColumn.INCCOL_CODE)
					.getString();
			if (!incType.dsInccolumnToInc.locate(IIncColumn.INCCOL_CODE,
					sInccolCode)) {
				// ����Ԥ���Ŀ
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreeIncAcctitem);
				// �շ���Ŀ
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreIncomeSubItem);
				return;
			}
			String tollFilter = incType.dsInccolumnToInc.fieldByName(
					IIncType.TOLL_FILTER).getString();
			List tollFilterValue = getFiLterValue(tollFilter);
			// ����Ԥ���Ŀ
			Object objValue = tollFilterValue.get(0);
			if (objValue == null) {
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreeIncAcctitem);
			} else {
				List lstAcctIncCode = (List) objValue;
				String[] sAcctIncIDArr = SysUntPub.getIdWithCode(
						lstAcctIncCode, incType.fpnlChoiceRela.ftreeIncAcctitem
								.getDataSet(), "","");
				SetSelectTree.setIsCheck(
						incType.fpnlChoiceRela.ftreeIncAcctitem, sAcctIncIDArr);
			}
			// �շ���Ŀ
			objValue = tollFilterValue.get(1);
			if (objValue == null) {
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreIncomeSubItem);
			} else {
				List lstTollCode = (List) objValue;
				String[] sTollIDArr = SysUntPub.getIdWithCode(lstTollCode,
						incType.fpnlChoiceRela.ftreIncomeSubItem.getDataSet(),
						"",
						"");
				SetSelectTree.setIsCheck(
						incType.fpnlChoiceRela.ftreIncomeSubItem, sTollIDArr);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incType,
					"��ʾ����Ԥ���Ŀ���շ���Ŀ��Ӧ��ϵ�������󣬴�����Ϣ��" + e1.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * �õ��ֶε�ֵ,���List,�磺(ACCT_CODE_INC = '?' or ACCT_CODE_INC like '?%') and
	 * (TOLL_CODE=? and TOLL_CODE like '?%')
	 * 
	 * @param ds
	 * @param sFieldName
	 * @return [0] ����Ԥ���Ŀ [1]�շ���Ŀ
	 * @throws Exception
	 */
	private List getFiLterValue(String tollFilter) throws Exception {
		// �滻��Ӧֵ�����������ʽ
		String tollFilterTmp = StringUtils.replace(tollFilter.toUpperCase(),
				"AND", "OR");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, "(", "");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, ")", "");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, " LIKE ", " = ");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, "%", "");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, "'", "");
		tollFilterTmp = StringUtils.replace(tollFilterTmp, " IN ", " = ");

		List lstAcctIncCode = null;
		List lstTollCode = null;
		String[] tollFilterArr = tollFilterTmp.split("OR");
		int len = tollFilterArr.length;
		for (int i = 0; i < len; i++) {
			String tollFilterField = tollFilterArr[i];
			int index = tollFilterField.indexOf("=");

			if (index == -1)
				continue;
			String filedName = tollFilterField.substring(0, index - 1).trim();
			String fieldValue = tollFilterField.substring(index + 1).trim();
			String[] fieldValueArr = StringUtils.split(fieldValue, ",");
			List lstFieldValue = Arrays.asList(fieldValueArr);
			if ("".equals(filedName)) {
				if (lstAcctIncCode == null)
					lstAcctIncCode = new ArrayList();
				lstAcctIncCode.addAll(lstFieldValue);
			} else if (IIncType.TOLL_CODE.equals(filedName)) {
				if (lstTollCode == null)
					lstTollCode = new ArrayList();
				lstTollCode.addAll(lstFieldValue);
			}
		}
		List lstResult = new ArrayList();
		lstResult.add(lstAcctIncCode);
		lstResult.add(lstTollCode);
		return lstResult;
	}
}
