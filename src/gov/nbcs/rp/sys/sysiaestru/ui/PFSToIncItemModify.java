package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * �ʽ���Դ��Ӧ�����޸�

 * 
 */
public class PFSToIncItemModify {
	private PFSToIncItem pfsToIncItem;

	// ���ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ;

	public PFSToIncItemModify(PFSToIncItem pfsToIncItem) {
		this.pfsToIncItem = pfsToIncItem;
		this.sysIaeStruServ = pfsToIncItem.sysIaeStruServ;
	}

	public void modify() throws HeadlessException, Exception {
		// int iCurBatchNo = PubInterfaceStub.getMethod().getCurBatchNO();
		// int iPayOutUpLoadBatch = PubInterfaceStub.getMethod()
		// .getPayoutUploadBatch();
		// if (iCurBatchNo >= iPayOutUpLoadBatch) {
		// JOptionPane.showMessageDialog(pfsToIncItem,
		// "��ǰ���δ��ڵ���֧��Ԥ��������Σ��������޸�!", "��ʾ",
		// JOptionPane.INFORMATION_MESSAGE);
		// return;
		// }

		// �ж��ʽ���Դ��������Ŀ�Ƿ�����ȡ����ϵ��������ȡ����ϵ����ʾ�Ƿ�ȷ���޸�
		int count = pfsToIncItem.ftreeIncItem.getSelectedNodeCount(true);
		if (count == 1) {
			// �õ�ѡ�е�������Ŀ������Դ
			List lstIsInc = SysUntPub.getLeafNodeCode(
					pfsToIncItem.ftreeIncItem, pfsToIncItem.ftreeIncItem
							.getDataSet(), IIncType.IS_INC);
			if (lstIsInc != null && !lstIsInc.isEmpty()) {
				// �ж��Ƿ��֧��Ԥ���ȡ��
				if (Integer.parseInt(lstIsInc.get(0).toString()) == 2) {
					if (JOptionPane.showConfirmDialog(pfsToIncItem,
							"������Ŀ�����ô�֧��Ԥ����֧���ʽ���Դȡ����ȷ���޸Ĵ˹�ϵ��", "��ʾ",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
						return;
				}
			}

		}

		String sEname = pfsToIncItem.dsPayOutFS.fieldByName("PFS_Ename")
				.getString();
		if (!sysIaeStruServ.judgePfsEnameUse(sEname, Global.loginYear)) {
			if (JOptionPane.showConfirmDialog(pfsToIncItem,
					"�޸ĸö�Ӧ��ϵ���п��ܵ������е�������֧�����ݲ�ƽ�⣬�Ƿ������", "��ʾ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
		}
		pfsToIncItem.dsPayOutFS.edit();
	}
}
