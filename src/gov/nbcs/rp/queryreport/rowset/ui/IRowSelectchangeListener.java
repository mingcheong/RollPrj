/**
 * @# IRowSelectchangeListener.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

/**
 * 功能说明:选择改变时触发
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public interface IRowSelectchangeListener {

	/**
	 * 
	 * 
	 * @param aRow变动后的行
	 *            信息，
	 * @param isOnDrag，是不是在拖拽期间
	 */
	public void selectChange(RowInfo aRow, boolean isOnDrag);

}
