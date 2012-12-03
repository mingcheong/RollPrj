/**
 * 
 */
package gov.nbcs.rp.queryreport.qrbudget.ui;

/**
 * <p>
 * Title:对比表条件设置
 * </p>
 * <p>
 * Description:对比表条件设置

 * @version 6.2.40
 */
public class ConditionObj {

	static public String PRE_YEAR = "上年";

	static public String CUR_YEAR = "本年";

	private String oneYear;

	private String oneBatchNo;

	private String oneDataType;

	private String twoYear;

	private String twoBatchNo;

	private String twoDataType;

	public ConditionObj() {
		oneYear = PRE_YEAR;
		oneBatchNo = "1";
		oneDataType = "0";
		twoYear = CUR_YEAR;
		twoBatchNo = "1";
		twoDataType = "0";
	}

	public String getOneBatchNo() {
		return oneBatchNo;
	}

	public void setOneBatchNo(String oneBatchNo) {
		this.oneBatchNo = oneBatchNo;
	}

	public String getOneDataType() {
		return oneDataType;
	}

	public void setOneDataType(String oneDataType) {
		this.oneDataType = oneDataType;
	}

	public String getOneYear() {
		return oneYear;
	}

	public void setOneYear(String oneYear) {
		this.oneYear = oneYear;
	}

	public String getTwoBatchNo() {
		return twoBatchNo;
	}

	public void setTwoBatchNo(String twoBatchNo) {
		this.twoBatchNo = twoBatchNo;
	}

	public String getTwoDataType() {
		return twoDataType;
	}

	public void setTwoDataType(String twoDataType) {
		this.twoDataType = twoDataType;
	}

	public String getTwoYear() {
		return twoYear;
	}

	public void setTwoYear(String twoYear) {
		this.twoYear = twoYear;
	}

}
