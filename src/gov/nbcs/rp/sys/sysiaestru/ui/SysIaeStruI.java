package gov.nbcs.rp.sys.sysiaestru.ui;

import gov.nbcs.rp.common.ServiceFactory;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * 收支结构，联接数据库接口
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
