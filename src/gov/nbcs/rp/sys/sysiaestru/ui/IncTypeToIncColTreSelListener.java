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
 * Title:选择收入栏目树节点显示收入预算科目和收费项目对应关系
 * </p>
 * <p>
 * Description:
 * </p>

 */
public class IncTypeToIncColTreSelListener implements TreeSelectionListener {
	// 收入项目类别管理客户端主界面类
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
			// 收入栏目编码
			String sInccolCode = dsIncCol.fieldByName(IIncColumn.INCCOL_CODE)
					.getString();
			if (!incType.dsInccolumnToInc.locate(IIncColumn.INCCOL_CODE,
					sInccolCode)) {
				// 收入预算科目
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreeIncAcctitem);
				// 收费项目
				SetSelectTree
						.setIsNoCheck(incType.fpnlChoiceRela.ftreIncomeSubItem);
				return;
			}
			String tollFilter = incType.dsInccolumnToInc.fieldByName(
					IIncType.TOLL_FILTER).getString();
			List tollFilterValue = getFiLterValue(tollFilter);
			// 收入预算科目
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
			// 收费项目
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
					"显示收入预算科目和收费项目对应关系发生错误，错误信息：" + e1.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 得到字段的值,组成List,如：(ACCT_CODE_INC = '?' or ACCT_CODE_INC like '?%') and
	 * (TOLL_CODE=? and TOLL_CODE like '?%')
	 * 
	 * @param ds
	 * @param sFieldName
	 * @return [0] 收入预算科目 [1]收费项目
	 * @throws Exception
	 */
	private List getFiLterValue(String tollFilter) throws Exception {
		// 替换相应值，方便解析公式
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
