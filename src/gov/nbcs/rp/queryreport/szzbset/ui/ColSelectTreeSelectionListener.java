package gov.nbcs.rp.queryreport.szzbset.ui;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;

/**
 * ������֧�ܱ����ã�����Ϣ��TreeSelectionListenerg�¼�
 * 
 * @author qzc
 * 
 */
public class ColSelectTreeSelectionListener implements TreeSelectionListener {

	SzzbSet szzbSet;

	public ColSelectTreeSelectionListener(SzzbSet szzbSet) {
		this.szzbSet = szzbSet;
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		try {
			if (szzbSet.dsHeader.isEmpty())
				return;

			String sFieldType = szzbSet.dsHeader.fieldByName(
					IQrBudget.FIELD_TYPE).getString();

			szzbSet.setMaskTypeChange(true);// ����ֵ
			szzbSet.ftitPnlBaseInfo.frdoFtype.setValue(sFieldType);// modified
			// by XXL
			// ��ʾ�ĺ�ֵ��ͬ
			Vector sFormat = (Vector) szzbSet.getXmlFormat().get(
					szzbSet.ftitPnlBaseInfo.frdoFtype.getRefModel()
							.getNameByValue(sFieldType));
			// �������ã����ʱ���ᴥ���䶯�¼��������ڴ��ֶ�����
			szzbSet.ftitPnlBaseInfo.fcbxSFormate.setRefModel(sFormat);
			// 20090922

			// ��������
			Object oValue = szzbSet.dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
					.getValue();
			if (oValue != null
					&& "��".equals(szzbSet.dsHeader.fieldByName(
							IQrBudget.IS_HIDECOL).getString())) {
				szzbSet.ftitPnlBaseInfo.fchkHideFlag.setValue(Boolean.TRUE);
			} else {
				szzbSet.ftitPnlBaseInfo.fchkHideFlag.setValue(Boolean.FALSE);
			}
			// ��ʾ��ʽ
			oValue = szzbSet.dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT)
					.getValue();

			if (oValue == null)
				szzbSet.ftitPnlBaseInfo.fcbxSFormate.setValue("");
			else
				szzbSet.ftitPnlBaseInfo.fcbxSFormate.setValue(oValue);
			szzbSet.setMaskTypeChange(false);// ����ֵ

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, "��֧�ܱ���Ϣ������ʾ����Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			szzbSet.setMaskTypeChange(false);// ����ֵ
		}

	}
}
