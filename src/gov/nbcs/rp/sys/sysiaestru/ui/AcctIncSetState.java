package gov.nbcs.rp.sys.sysiaestru.ui;

/**
 * <p>
 * Title:��֧��Ŀ�ҽ���ϸ,���ݵ�ǰ�ӿ�Ŀ���ͣ�������״̬����ѡ������ѡ��
 * 
 * </p>
 * <p>
 * Description:��֧��Ŀ�ҽ���ϸ,���ݵ�ǰ�ӿ�Ŀ���ͣ�������״̬����ѡ������ѡ��
 * 

 */
public class AcctIncSetState {
	private AcctJjInc acctJjInc = null;

	/**
	 * ���캯��
	 * 
	 * @param acctJjInc
	 *            ��֧��Ŀ�ҽ���ϸ�ͻ�����������
	 */
	public AcctIncSetState(AcctJjInc acctJjInc) {
		this.acctJjInc = acctJjInc;
	}

	/**
	 * ���ݵ�ǰ�ӿ�Ŀ���ͣ�������״̬����ѡ������ѡ������
	 */
	public void setState() {
		if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 0) {
			if ("2".equals(acctJjInc.frdoSubTypeInc.getValue().toString())) {
				acctJjInc.ftreIncomeSubItem.setIsCheckBoxEnabled(true);
			} else {
				acctJjInc.ftreIncomeSubItem.setIsCheckBoxEnabled(false);
				// �����ѡ��
				SetSelectTree.setIsNoCheck(acctJjInc.ftreIncomeSubItem);
			}
		} else if (acctJjInc.ftabPnlAcctjjInc.getSelectedIndex() == 1) {

		}

	}
}
