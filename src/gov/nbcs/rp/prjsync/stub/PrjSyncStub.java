/**
 * @Copyright �㽭���Ű�Ȩ����
 *
 * @ProjectName ��������������Ŀϵͳ
 *
 * @aouthor ���ܱ�
 *
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.stub;

import gov.nbcs.rp.prjsync.ibs.IPrjSync;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;

/**
 * @author ë����
 *
 * @version ����ʱ�䣺2012-6-7 ����10:56:05
 *
 * @Description
 */
public class PrjSyncStub {
	public static IPrjSync getMethod() {
		BeanFactory beanfac = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/prjsync/conf/PrjSyncConf.xml");
		IPrjSync itserv = (IPrjSync) beanfac.getBean("rp.PrjSyncService");
		return itserv;
	}
}

