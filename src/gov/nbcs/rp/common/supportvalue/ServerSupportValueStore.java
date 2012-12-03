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


/**
 * The Class ServerSupportValueStore.
 * 
 * @author qzc)
 * @version 1.0, Jun 6, 2009
 * @since fb6.3.70
 */
public class ServerSupportValueStore extends SupportValueStore {

	/** The Constant ClinetRealTimeSVID. */
	private static final String ServerCachedSVID = "rp.server-cached-supportvalues";

	/** The realtime_support_types. */
	protected List cachedSupportTypes;

	/**
	 * Instantiates a new client support value store.
	 */
	public ServerSupportValueStore() {
		try {
			// 0��ʾ�ṹ���ÿ���1��ʾ�ṹ���ùء�
			// realtime_support_types.add("EDITSTRULOCK");
			BeanFactory bf = BeanFactoryUtil
					.getBeanFactory(SV_Config_XML_Location);
			cachedSupportTypes = (List) bf.getBean(ServerCachedSVID);

			support_value_context = SupportValueContext.newServer();
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
		// ��Ҫ����ȡֵ�Ĳ���
		if (cachedSupportTypes.contains(supType.toUpperCase())) {
			return getCachedSupportValue(supType);
		}
		return getRealTimeSupportValue(supType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.SupportValueStore#getQueryTool()
	 */
	protected Query getQueryTool() {
		return QueryStub.getQueryTool();
	}
}
