/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:������Ŀ������Ӳ�����
 * </p>
 * <p>
 * Description:������Ŀ������Ӳ�����

 */
public class IncTypeAdd {
	// ������Ŀ������ͻ�����������
	private IncType incType = null;

	// ������Ŀ������
	private IncTypeObj incTypeObj = null;

	// �������ݿ�ӿ�
	private ISysIaeStru sysIaeStruServ = null;

	// �����������ݽӿ�
	private IPubInterface iPubInterface = null;

	/**
	 * ���캯��
	 * 
	 * @param incType
	 *            ������Ŀ������ͻ�����������
	 */
	public IncTypeAdd(IncType incType) {
		this.incType = incType;
		this.incTypeObj = incType.incTypeObj;
		this.sysIaeStruServ = incType.sysIaeStruServ;
		iPubInterface = PubInterfaceStub.getMethod();
	}

	/**
	 * ����������Ŀ����������
	 * 
	 * @return �������Ӳ����Ƿ�ɹ���true:�ɹ���false��ʧ��
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public boolean add() throws NumberFormatException, Exception {
		// �ж����ӣ��ǲ������ӵ�һ��Ҷ���ӣ�������ӵ�һ��Ҷ�ڵ㣬�����ڵ�Ĳ�����Ϣ�����ӽڵ�
		if (incTypeObj.end_flag == 1)
			incType.sSaveType = "addfirstson";
		else
			incType.sSaveType = "add";

		// �ж��ڽڵ������ӵ�һ���ӽڵ㣬����ڵ��ѱ�ʹ�ã����������ӽڵ�
		if ("addfirstson".equals(incType.sSaveType)) {
			InfoPackage infoPackage = sysIaeStruServ.judgeIncTypeEnableDel(
					incTypeObj.inctype_code, Global.loginYear);
			if (!infoPackage.getSuccess()) {
				JOptionPane.showMessageDialog(incType, infoPackage
						.getsMessage()
						+ "�����������ӽڵ㡣", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}

		// ����Զ����ɵı���
		String sIncTypeCode = iPubInterface.getMaxCode("fb_iae_inctype",
				"inctype_CODE", "set_Year = " + Global.loginYear,
				ISysIaeStru.iCodeLen);
		// �жϱ����Ƿ��óɹ�
		if (sIncTypeCode == null) {
			JOptionPane.showMessageDialog(incType, "�Զ����ɱ���ʧ�ܣ�����ʧ�ܣ�", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		// ����Զ����ɵĲ����
		String sLvlIdCode;
		if ("".equals(incTypeObj.lvl_id)) {

			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inctype", "LVL_ID",
					incTypeObj.lvl_id, "set_Year = " + Global.loginYear
							+ " and par_id is null", incType.lvlIdRule);
		} else {
			sLvlIdCode = iPubInterface.getNodeID("fb_iae_inctype", "LVL_ID",
					incTypeObj.lvl_id, "set_Year = " + Global.loginYear
							+ " and par_id ='" + incTypeObj.lvl_id + "'",
					incType.lvlIdRule);
		}
		// �жϲ�����Ƿ��óɹ�
		if (sLvlIdCode == null) {
			JOptionPane.showMessageDialog(incType, "�޷�������һ�����룬�ѵ��������Ӽ��Σ�",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		incType.dsIncType.append();
		// �ж�����ѡ�еĽڵ��Ƿ���Ҷ�ڵ�,������Ҷ�ڵ㣬��������Ϣ������һ��Ҷ�ڵ�
		incType.dsIncType.maskDataChange(true);
		incType.ftxtPriCode.setValue(sIncTypeCode);// ����
		incType.ftxtfIncTypeCode.setValue(sLvlIdCode);// ��α���
		incType.ftxtfIncTypeName.setValue("");// ����
		if ("addfirstson".equals(incType.sSaveType)) {// ���ӵ�һ���ӽڵ�
		} else {// �����ӽڵ�
			// ����
			incType.flstIncTypeKind.setSelectedIndex(0);
			((JCheckBox) incType.fchkIsMid.getEditor()).setSelected(false);
			incType.fchkIsMid.setValue("0");
			incType.frdoIsInc.setValue("0");
		}
		incType.dsIncType.fieldByName("INCTYPE_CODE").setValue(sIncTypeCode);
		incType.dsIncType.fieldByName("END_FLAG").setValue(new Integer(1));
		incType.dsIncType.fieldByName("set_Year").setValue(Global.loginYear);
		incType.dsIncType.fieldByName("RG_CODE").setValue(
				Global.getCurrRegion());
		incType.dsIncType.maskDataChange(false);
		incType.ftxtfIncTypeName.setFocus();
		return true;
	}
}
