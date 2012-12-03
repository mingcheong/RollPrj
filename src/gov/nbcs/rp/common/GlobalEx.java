/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import com.foundercy.pf.util.Global;
public class GlobalEx extends Global {
	
	/** ������������. */
	public static final String BELONG_TYPE = "belong_type";
	
	/** ����������λ����. */
	public static final String BELONG_ORG_CODE = "belong_org_code";
	
	/** The Constant BELONG_DIV_LEVEL_NUM. */
	public static final String BELONG_DIV_LEVEL_NUM = "belong_div_level_num";
	
	/** The Constant BELONG_DIV_IS_LEAF. */
	public static final String BELONG_DIV_IS_LEAF = "belong_div_is_leaf";
	
	/** The Constant KEY_USER_ID. */
	public static final String KEY_USER_ID = "##KEY_USER_ID##";

	/**
	 * ���clientContext�е��û���Ϣ
	 */
	private static synchronized void syncClientContext() {
		String keyUserId = (String) clientContext.getAttribute(KEY_USER_ID);
		if (!Global.getUserId().equals(keyUserId)) {
			clientContext.setAttribute(BELONG_TYPE, null);
			clientContext.setAttribute(BELONG_ORG_CODE, null);	
			clientContext.setAttribute(BELONG_DIV_LEVEL_NUM, null);
			clientContext.setAttribute(BELONG_DIV_IS_LEAF, null);			

			clientContext.setAttribute(KEY_USER_ID, Global.getUserId());
		}
	}
		
	/**
	 * ��ȡ��ǰ��¼�û������������ͣ����ڿͻ��ˣ�
	 */
	public static String getBelongType() {
		syncClientContext();
		String belongType = (String) clientContext.getAttribute(BELONG_TYPE);
		if ((belongType == null) || (belongType.length() == 0)) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
			try {
				belongType = PubInterfaceStub.getMethod().getUserBelongType(
						Global.getUserId());
				clientContext.setAttribute(BELONG_TYPE, belongType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongType == null ? "" : belongType;
	}
	
	/**
	 * �������һ�������
	 * @return
	 */
	public static String getBelongDepCode() {
		syncClientContext();
		String belongBranchCode = (String) clientContext.getAttribute(BELONG_ORG_CODE);
		if ((belongBranchCode == null) || (belongBranchCode.length() == 0)) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
			try {
				belongBranchCode = PubInterfaceStub.getMethod()
						.getBranchCode(Global.getUserBeLong());
				clientContext.setAttribute(BELONG_ORG_CODE, belongBranchCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongBranchCode == null ? "" : belongBranchCode;}
	
	/**
	 * ��ȡ��ǰ��¼�û�����������λ���루���ڿͻ��ˣ�
	 * 
	 * @return
	 */
	public static String getBelongDivCode() {
		syncClientContext();
		String belongDivCode = (String) clientContext.getAttribute(BELONG_ORG_CODE);
		if ((belongDivCode == null) || (belongDivCode.length() == 0)) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
			try {
				belongDivCode = PubInterfaceStub.getMethod()
						.getEnDivCode(Global.getUserBeLong());
				clientContext.setAttribute(BELONG_ORG_CODE, belongDivCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return belongDivCode == null ? "" : belongDivCode;
	}
	
	public static int getBelongDivLevelNum() {
		syncClientContext();
		Integer s = (Integer) clientContext.getAttribute(BELONG_DIV_LEVEL_NUM);
		if (s == null) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
			try {
				int s1 = PubInterfaceStub.getMethod()
						.getEnDivLevelNum(Global.getUserBeLong());
				s = new Integer(s1);
				clientContext.setAttribute(BELONG_DIV_LEVEL_NUM, s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s == null ? 0 : s.intValue();
	}
	
	public static int getBelongDivIsLeaf() {
		syncClientContext();
		Integer s = (Integer) clientContext.getAttribute(BELONG_DIV_IS_LEAF);
		if (s == null) {
			// ���û�м�¼����������ȡ���������ڿͻ���
			// ��Ϊƽ̨����δȡ����ֵ������һ�λ�ȡ���ڷ�������ʱ����ֵΪnull
			try {
				int s1 = PubInterfaceStub.getMethod()
						.getEnDivLevelNum(Global.getUserBeLong());
				s = new Integer(s1);
				clientContext.setAttribute(BELONG_DIV_IS_LEAF, s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s == null ? 1 : s.intValue();
	}

	/**
	 * ��ǰ��¼�û��Ƿ�����û������ڿͻ��ˣ�.
	 * 
	 * @return true, if ��ǰ��¼�û� is �����û�
	 */
	public static boolean isFisVis() {
		return UntPub.FIS_VIS.equals(getBelongType());
	}
	
	/**
	 * ��ǰ��½�û��Ƿ����ܲ����û����û��ͻ��ˣ�
	 * 
	 * @return true, if ��ǰ��¼�û� is ���ܲ����û�
	 */
	public static boolean isChargeDiv() {
		if (!isFisVis()) {
			// �ж�������������λΪele_enterprise.level_num=1
			return getBelongDivLevelNum() == 1;
		}
		return false;
	}
	
	/**
	 * �Ƿ�ĩ����λ
	 * 
	 * @return true, if ��ǰ��¼�û� is ĩ����λ�û�
	 */
	public static boolean isLeafDiv() {
		if (!isFisVis()) {
			// �ж�������������λΪele_enterprise.is_leaf=1
			return getBelongDivIsLeaf() == 1;
		}
		return false;
	}

	/**
	 * Checks if is at online server.
	 * 
	 * @return true, if is at online server
	 */
	public static boolean isServer() {
		return (Global.loginmode == 0) && (Global.loginYear == null);
	}

	/**
	 * Checks if is at online client.
	 * 
	 * @return true, if is at online client
	 */
	public static boolean isClient() {
		return (Global.loginmode == 0) && (Global.loginYear != null);
	}

	/**
	 * Checks if is offline.
	 * 
	 * @return true, if is offline
	 */
	public static boolean isOffline() {
		return Global.loginmode == 1;
	}
}
