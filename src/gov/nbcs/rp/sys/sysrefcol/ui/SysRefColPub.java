package gov.nbcs.rp.sys.sysrefcol.ui;

import com.foundercy.pf.util.Global;

/**

 * 
 */
public class SysRefColPub {
	final String ARRAY_REFCOL_DATA_OWNER[] = { "����", "������Ϣ", "��Ŀ", "����Ԥ��",
			"������Ϣ", "����֧��ϵͳ", "����" };

	/**
	 * �滻Sql����е���Ϣ
	 * 
	 * @param sSql
	 * @return
	 * @throws Exception
	 */
	static public String ReplaceRefColFixFlag(String sSql) throws Exception {
		String sSqlTmp = sSql;
		sSqlTmp = sSqlTmp.replaceAll("#SET_YEAR#", Global.loginYear);
//		sSqlTmp = sSqlTmp.replaceAll("#BATCH_NO#", String
//				.valueOf(PubInterfaceStub.getMethod().getCurBatchNO()));
		return sSqlTmp;
	}
}
