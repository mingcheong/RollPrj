package gov.nbcs.rp.sys.sysiaestru.ui;

/**
 * <p>
 * Title:收支科目挂接明细,根据当前子科目类型，设置树状态（可选、不可选）
 * 
 * </p>
 * <p>
 * Description:收支科目挂接明细,根据当前子科目类型，设置树状态（可选、不可选）
 * 

 */
public class AcctIncSetState {
	private AcctJjInc acctJjInc = null;

	/**
	 * 构造函数
	 * 
	 * @param acctJjInc
	 *            收支科目挂接明细客户端主界面类
	 */
	public AcctIncSetState(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
	}

	/**
	 * 根据当前子科目类型，设置树状态（可选、不可选）方法
	 */
	public void setState() {
		if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 0) {
			if ("2".equals(acctJjInc.frdoSubTypeInc.getValue().toString())) {
				acctJjInc.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			} else {
				acctJjInc.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
				// 清空树选择
				SetSelectTree.setIsNoCheck(acctJjInc.ftreIncomeSubItem);
			}
		} else if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 1) {

		}

	}
}
