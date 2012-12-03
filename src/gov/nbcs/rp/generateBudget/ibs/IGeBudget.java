package gov.nbcs.rp.generateBudget.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

import java.util.List;
import java.util.Map;

public abstract interface IGeBudget {
	public abstract DataSet getDivTreePop(String paramString, int paramInt)
			throws Exception;

	public abstract DataSet getDepToDivData(String paramString, int paramInt1,
			boolean paramBoolean, int paramInt2) throws Exception;

	public abstract DataSet findProjectByPara(String[] paramArrayOfString1,
			String paramString, String queryType) throws Exception;

	public abstract String findkmdm(String paramString) throws Exception;

	public abstract void gunDongNextyearLXXM(String paramString1,
			String[] paramArrayOfString, String paramString2,
			String paramString3, String paramString4) throws Exception;

	public abstract void gunDongNextyear(String paramString1,
			String[] paramArrayOfString, String paramString2,
			String paramString3, String paramString4) throws Exception;

	public abstract void DeleteTempPro(String paramString1,
			String paramString2, String paramString3) throws Exception;

	public abstract void finishSameBudget(String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5) throws Exception;

	public abstract void changeHs(String paramString1, String paramString2,
			String paramString3, String paramString4, String lBudgetData)
			throws Exception;

	public abstract String insertTempBudget(String set_year, String batch_no,
			String data_type, List paramString4) throws Exception;

	public abstract DataSet findRPBUDGET(String[] paramArrayOfString,
			Map paramMap, String paramString) throws Exception;

	public abstract DataSet findDoProjectByPara(String[] divCodes,
			String loginYear, String fiter1, String queryType);

	public abstract void cancleBudget(String loginYear, String whereSql);

	public abstract void setDivToDiv(List lBudgetData, String en_id,
			String en_code, String en_name) throws Exception;

	public abstract void setGLToDiv(List lBudgetData, String en_id,
			String en_code, String en_name) throws Exception;

}
