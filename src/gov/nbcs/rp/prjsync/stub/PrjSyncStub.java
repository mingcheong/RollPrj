/**
 * @Copyright 浙江易桥版权所有
 *
 * @ProjectName 宁海财政扶持项目系统
 *
 * @aouthor 陈宪标
 *
 * @version 1.0
 */
package gov.nbcs.rp.prjsync.stub;

import gov.nbcs.rp.prjsync.ibs.IPrjSync;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;

/**
 * @author 毛建亮
 *
 * @version 创建时间：2012-6-7 上午10:56:05
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

