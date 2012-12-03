/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.util.BeanFactoryUtil;


import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;


/**
 * <p>
 * Title:自定义表设置联接数据接口
 * </p>
 * <p>
 * Description:自定义表设置联接数据接口
 * </p>
 * <p>
 *
 */
public class DefineReportI {
	public static IDefineReport getMethod() {
		
		
			BeanFactory beanfac = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/queryreport/definereport/conf/DefineReportConf.xml");
			IDefineReport defineReportServ = (IDefineReport) beanfac.getBean("rp.defineReportService");
			return defineReportServ;
		
		
		
	}

}
