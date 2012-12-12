package gov.nbcs.rp.query.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;
import java.util.Map;




public abstract interface IQrBudget
{
	public abstract DataSet getDivDataPop(String paramString, int paramInt) throws Exception;


	public abstract DataSet getDepToDivData(String paramString, int paramInt1, boolean paramBoolean, int paramInt2) throws Exception;


	public abstract DataSet findProjectByPara(String[] paramArrayOfString1, Map paramMap, String[] paramArrayOfString2, String paramString) throws Exception;


	public abstract DataSet find_alllife(String[] paramArrayOfString1, Map paramMap, String[] paramArrayOfString2, String paramString) throws Exception;


	public abstract String findkmdm(String paramString) throws Exception;


	public abstract void gunDongNextyearLXXM(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4) throws Exception;


	public abstract void gunDongNextyear(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4) throws Exception;


	public abstract void DeleteTempPro(String paramString1, String paramString2, String paramString3) throws Exception;


	public abstract void finishSameBudget(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) throws Exception;


	public abstract void changeHs(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) throws Exception;


	public abstract void insertTempBudget(String paramString1, String paramString2, String paramString3, String paramString4) throws Exception;


	public abstract DataSet findRPBUDGET(String[] divCodes, String dataType, String fpType, String setYear, String rgCode) throws Exception;

}
