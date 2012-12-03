/**
 * @# TableSourceServer.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * ����˵��:�����ֶ���Ϣ�Ļ���
 * <P>
 * Copyright
 * <P>
 * All rights reserved.

 */
public class TableSourceServer {

	private static XMLData dsFields = new XMLData();

	public static String getFieldType(String sObjectName, String fieldName) {
		Object obj = dsFields.get(sObjectName);
		if (obj == null) {
			obj = getATabelSource(sObjectName);
			dsFields.put(sObjectName, obj);
		}
		return (String) ((XMLData) obj).get(fieldName);
	}

	public static XMLData getATabelSource(String sourceID) {
		IDataDictBO dataDictBO = (IDataDictBO) BOCache
				.getBO("fb.dataDictService");
		return dataDictBO.getFieldAndType(sourceID, Global.loginYear);
	}

	public static void clearAllCache() {
		dsFields = new XMLData();
	}

}
