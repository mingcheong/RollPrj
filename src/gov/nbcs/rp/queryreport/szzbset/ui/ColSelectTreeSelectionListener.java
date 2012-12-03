package gov.nbcs.rp.queryreport.szzbset.ui;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;

/**
 * 设置收支总表设置，列信息树TreeSelectionListenerg事件
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

			szzbSet.setMaskTypeChange(true);// 设置值
			szzbSet.ftitPnlBaseInfo.frdoFtype.setValue(sFieldType);// modified
			// by XXL
			// 显示的和值不同
			Vector sFormat = (Vector) szzbSet.getXmlFormat().get(
					szzbSet.ftitPnlBaseInfo.frdoFtype.getRefModel()
							.getNameByValue(sFieldType));
			// 更新引用，因此时不会触发变动事件，所以在此手动设置
			szzbSet.ftitPnlBaseInfo.fcbxSFormate.setRefModel(sFormat);
			// 20090922

			// 该列隐藏
			Object oValue = szzbSet.dsHeader.fieldByName(IQrBudget.IS_HIDECOL)
					.getValue();
			if (oValue != null
					&& "是".equals(szzbSet.dsHeader.fieldByName(
							IQrBudget.IS_HIDECOL).getString())) {
				szzbSet.ftitPnlBaseInfo.fchkHideFlag.setValue(Boolean.TRUE);
			} else {
				szzbSet.ftitPnlBaseInfo.fchkHideFlag.setValue(Boolean.FALSE);
			}
			// 显示格式
			oValue = szzbSet.dsHeader.fieldByName(IQrBudget.FIELD_DISFORMAT)
					.getValue();

			if (oValue == null)
				szzbSet.ftitPnlBaseInfo.fcbxSFormate.setValue("");
			else
				szzbSet.ftitPnlBaseInfo.fcbxSFormate.setValue(oValue);
			szzbSet.setMaskTypeChange(false);// 设置值

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(szzbSet, "收支总表信息设置显示列信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			szzbSet.setMaskTypeChange(false);// 设置值
		}

	}
}
