package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * ��֧�ṹ���������ݿ�ӿ�
 * 

 * 
 */
public class SysIaeStruI {;
	public static ISysIaeStru getMethod() {
		ISysIaeStru sysIaeStruServ = (ISysIaeStru) ServiceFactory
		.getServiceInterface("fb.sysIaeStruService");
		return sysIaeStruServ;
	}
	
	
}
