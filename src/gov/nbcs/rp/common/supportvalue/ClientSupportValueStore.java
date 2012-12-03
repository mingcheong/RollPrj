/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.supportvalue;

import com.foundercy.pf.util.BeanFactoryUtil;
import gov.nbcs.rp.common.datactrl.Query;
import gov.nbcs.rp.common.datactrl.QueryStub;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;


public class ClientSupportValueStore extends SupportValueStore {

	/** The Constant ClinetRealTimeSVID. */
	private static final String ClinetRealTimeSVID = "rp.client-realtime-supportvalues";

	/** The realtime_support_types. */
	protected List realtimeSupportTypes;

	/**
	 * Instantiates a new client support value store.
	 */
	public ClientSupportValueStore() {
		try {
			// 0��ʾ�ṹ���ÿ���1��ʾ�ṹ���ùء�
			// realtime_support_types.add("EDITSTRULOCK");
			BeanFactory bf = BeanFactoryUtil
					.getBeanFactory(SV_Config_XML_Location);
			realtimeSupportTypes = (List) bf.getBean(ClinetRealTimeSVID);

			support_value_context = SupportValueContext.newClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.supportvalue.SupportValueStore#getSupportValue(java.lang.String)
	 */
	public SupportValue getSupportValue(String supType) {
		if (realtimeSupportTypes.contains(supType.toUpperCase())) {
			return getRealTimeSupportValue(supType);
		}
		return getCachedSupportValue(supType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.SupportValueStore#getQueryTool()
	 */
	protected Query getQueryTool() {
		return QueryStub.getClientQueryTool();
	}
}
