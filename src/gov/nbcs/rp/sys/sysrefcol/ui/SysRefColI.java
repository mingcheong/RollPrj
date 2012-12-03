package gov.nbcs.rp.sys.sysrefcol.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.util.sessionmanager.SessionUtil;

/**
 * �����й����������ݿ���
 * 

 * 
 */
public class SysRefColI {

	public static ISysRefCol getMethod() {
		ISysRefCol sysRefColServ = (ISysRefCol) ServiceFactory
				.getServiceInterface("rp.sysRefColService");
		return sysRefColServ;
	}

	/**
	 * ����˻�ȡ��ӿ�
	 * 
	 * @return
	 */
	public static ISysRefCol getServerMethod() {
		return (ISysRefCol) SessionUtil.getServerBean("rp.sysRefColService");
	}

}
