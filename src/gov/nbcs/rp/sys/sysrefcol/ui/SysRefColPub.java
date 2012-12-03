package gov.nbcs.rp.sys.sysrefcol.ui;

import com.foundercy.pf.util.Global;

/**

 * 
 */
public class SysRefColPub {
	final String ARRAY_REFCOL_DATA_OWNER[] = { "公用", "基础信息", "项目", "部门预算",
			"基本信息", "决策支持系统", "其他" };

	/**
	 * 替换Sql语句中的信息
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
