package gov.nbcs.rp.common;


/**
 * 承载消息包裹类
 * 
 */
public class InfoPackage implements Comparable {
	private boolean isSuccess;
	private String sMessage;
	private Object object;
	private int iValue;

	public InfoPackage() {
		this.isSuccess = false;
		this.sMessage = "";
		this.iValue = 0;
	}

	public boolean getSuccess() {
		return this.isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getsMessage() {
		return this.sMessage;
	}

	public void setsMessage(String sMessage) {
		this.sMessage = sMessage;
	}

	public Object getObject() {
		return this.object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	///用于分页的属性和方法
	private int curPage = 1;//当前页

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getCurPage() {
		return this.curPage;
	}

	//每页大小
	private int pageCount = 10000;

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	//总共的行数
	private int totalRow = 0;

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getTotalRow() {
		return this.totalRow;
	}

	//总共页数
	private int totalPage = 0;

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	//实现Comparable接口
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return compareTo(o);
	}

	//覆盖toString方法
	public String toString(InfoPackage s) {
		return s.toString();
	}

	public int getIValue() {
		return iValue;
	}

	public void setIValue(int value) {
		iValue = value;
	}

}
