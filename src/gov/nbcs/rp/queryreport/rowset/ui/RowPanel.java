/**
 * @# RowPanel.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gov.nbcs.rp.basinfo.common.BOCache;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ui.DefineReport;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.rowset.ibs.IRowSet;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:全行列表显示的面板，包含了行列表，按钮等
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public class RowPanel extends FPanel implements ITitleChangedListener {

	/**
	 * 
	 */
	private String reportID;

	private static final long serialVersionUID = 1166570966836624584L;

	private RowTable tblRows;

	private List lstRows;// 以LIST形式存储

	private XMLData xmlRows;// 以xmldata方式存储键为ROWID

	private List lstXMLRows;

	private List lstXMLCons;

	private XMLData xmlTableSource;

	private XMLData xmlFields;

	private List selectChangeListeners = new ArrayList();

	private List titleChangeListener = new ArrayList();

	private boolean isAdd;// 是否是增加

	private XMLData xmlReport;

	int rowCount = 500;// 用于新增时的ITEMID

	private ReportInfoPanel lstType;

	private DefineReport defineReport;

	private void initUI() {
		tblRows = new RowTable(this);
		tblRows.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting())
					fireSelectChange();

			}

		});
		this.setLayout(new RowPreferedLayout(15));
		this.add(tblRows, new TableConstraints(20, 13, true, true));
		this.add(new ButtonPanel(), new TableConstraints(20, 2, true, true));

	}

	/**
	 * 初始化报表 注：查询时一定要以行的itemID,和条件的lineNum字段排序
	 * 
	 * @param lstRows查询出的行信息
	 * @param lstCons//查询出的字段信息
	 * @param reportID
	 */
	public RowPanel(List lstRows, List lstCons, String reportID,
			XMLData xmlTableSource, XMLData xmlFields, XMLData report,
			boolean isAdd, ReportInfoPanel lstType, DefineReport defineReport)
			throws Exception {

		this.reportID = reportID;
		this.isAdd = isAdd;
		if (isAdd)
			this.reportID = "addTempCode";
		this.lstXMLCons = lstCons;
		this.lstXMLRows = lstRows;
		this.xmlTableSource = xmlTableSource;
		this.xmlFields = xmlFields;
		this.xmlReport = report;
		this.lstType = lstType;
		this.defineReport = defineReport;
		initData();
		initUI();
	}

	private void initData() {
		if (lstXMLRows != null && lstXMLRows.size() > 0) {
			int iCount = lstXMLRows.size();

			// 初始化行
			for (int i = 0; i < iCount; i++) {
				RowInfo aRow = new RowInfo((XMLData) lstXMLRows.get(i), null);
				aRow.addTitleChangeedListener(this);
				addRow(aRow);
			}
			// 初始化条件
			if (lstXMLCons != null && lstXMLCons.size() > 0) {
				iCount = lstXMLCons.size();
				for (int i = 0; i < iCount; i++) {
					Condition aCon = new Condition((XMLData) lstXMLCons.get(i));
					Object obj = xmlRows.get(aCon.getItemID());
					if (obj == null)
						continue;
					((RowInfo) obj).addCons(aCon);
				}

			}
		} else {
			lstRows = new ArrayList();
			lstXMLRows = new ArrayList();
			lstXMLCons = new ArrayList();
			xmlRows = new XMLData();
		}
	}

	/**
	 * 添加一个条件
	 * 
	 * @param aCon
	 * @return
	 */
	public boolean addCon(Condition aCon) {
		String ItemID = aCon.getItemID();
		Object obj = xmlRows.get(ItemID);
		if (obj == null)
			return false;
		((RowInfo) obj).addCons(aCon);
		return true;
	}

	/**
	 * 添加一新默认行
	 * 
	 */
	public void addDefualtRow() {
		if (Common.isNullStr(this.reportID)) {
			return;
		}
		RowInfo aRow = new RowInfo();
		aRow.setReportID(this.reportID);
		aRow.setItemID(getNextRowID());
		aRow.setItem("未指定标题");
		aRow.setMeasureType(RowInfo.MEASURE_SUM);
		aRow.addTitleChangeedListener(this);
		int iCount = titleChangeListener.size();
		if (iCount > 0) {
			for (int i = 0; i < iCount; i++) {
				aRow
						.addTitleChangeedListener((ITitleChangedListener) titleChangeListener
								.get(i));
			}
		}

		addRow(aRow);
	}

	/**
	 * 取得当前的行
	 * 
	 * @return
	 */
	public RowInfo getcurRow() {
		XMLData aData = tblRows.getCurrentRow();
		if (aData != null) {
			String itemID = Common
					.getAStringField(aData, RowInfo.FIELD_ITEM_ID);
			Object obj = xmlRows.get(itemID);
			if (obj != null)
				return (RowInfo) obj;
		}
		return null;
	}

	/**
	 * 删除一行，则要在缓存中都要删除
	 * 
	 */

	public void delCurRow() {
		XMLData aData = tblRows.getCurrentRow();
		if (aData != null) {
			String itemID = Common
					.getAStringField(aData, RowInfo.FIELD_ITEM_ID);
			Object obj = xmlRows.get(itemID);
			if (obj != null) {
				xmlRows.remove(itemID);
				lstRows.remove(obj);
			}
			tblRows.deleteSelectedRows();
		}

	}

	/**
	 * 添加一行
	 * 
	 * @param aRow
	 */
	private void addRow(RowInfo aRow) {
		if (aRow == null)
			return;
		if (lstRows == null)
			lstRows = new ArrayList();
		if (xmlRows == null)
			xmlRows = new XMLData();
		lstRows.add(aRow);
		xmlRows.put(aRow.getItemID(), aRow);
		XMLData aData = aRow.getXML();
		aData.put(RowTable.FIELD_DISP, aRow.getTitle(xmlTableSource,
				(List) xmlFields.get(aRow.getSourceID())));
		if (tblRows != null)
			tblRows.addRow(aData);
		// tblRows.updateUI();
	}

	/**
	 * 下一个行标
	 * 
	 * @return
	 */
	private String getNextRowID() {
		rowCount = rowCount + 1;
		return String.valueOf(rowCount);
	}

	public void upward() {
		tblRows.moveUpCurrentRow();
	}

	public void downward() {
		tblRows.moveDownCurrentRow();
	}

	public void save() throws Exception {
		try {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

			// 如果是新增则要重新设置报表ID;
			{

				if (isAdd) {

			
				}

			}
			// --------------
			// 检查主表信息
			String sErr = checkReport();
			if (!sErr.equals("")) {

				JOptionPane.showMessageDialog(this.getParent().getParent(),
						sErr, "提示", JOptionPane.INFORMATION_MESSAGE);// this,
				// );
				return;
			}
			// -------------
			int iCount = lstRows.size();
			List lstSql = new ArrayList();
			for (int i = 0; i < iCount; i++) {
				RowInfo cuRow = ((RowInfo) lstRows.get(i));
				// 将界面中的顺序转换成视图顺序
				String row = String.valueOf(tblRows
						.convertModelRowIndexToView(i) + 1);
				cuRow.setItemID(row);
				try {
					List lstTemp = cuRow.getInsertSql(Global.getSetYear(), row,
							TableSourceServer.getATabelSource(cuRow
									.getSourceID()));
					// 通过数据库来验证SQL语句查询的正确性
					// 检查查询语句的正确信息 add by XXL 20080919
					if (cuRow.getMeasureType() == RowInfo.MEASURE_SQL) {
						String sSql = cuRow.getSql();
						sSql = sSql.replaceAll(RowInfo.PARAM_DIV, " 1=2 ");

						sSql = sSql.replaceAll(RowInfo.PARAM_BATCH, "0");
						sSql = sSql.replaceAll(RowInfo.PARAM_DATA_TYPE, "1");

						sSql = sSql.replaceAll(RowInfo.PARAM_DATA_VER, "0");

						sSql = sSql.replaceAll(RowInfo.PARAM_SET_YEAR,
								Global.loginYear);
						sSql = sSql.replaceAll(RowInfo.PARAM_TABLE, "");
						try {
							((IRowSet) BOCache.getBO("fb.rowSetService"))
									.executeQuery(sSql);
						} catch (Exception e) {
							throw new Exception("查询语句不正确，请检查，" + e.getMessage());
						}
					}
					lstSql.addAll(lstTemp);
				} catch (Exception e) {
					// 定位错误
					tblRows.setSelectRow(tblRows.convertModelRowIndexToView(i));
					throw e;
				}

			}

			((IRowSet) BOCache.getBO("fb.rowSetService"))
					.saveRows(lstSql, reportID, Global.getSetYear(), xmlReport,
							lstType.getType());
			isAdd = false;

			// 修改或增加节点时，刷新树节点
			defineReport
					.refreshNodeEdit(xmlReport, lstType.getType(), reportID);

			JOptionPane.showMessageDialog(this.getParent().getParent(),
					"数据保存成功!");
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	public List getLstXMLRows() {
		return lstXMLRows;
	}

	public XMLData getXmlRows() {
		return xmlRows;
	}

	public void titleChanged(RowInfo aRow, boolean isItem) {
		if (isItem)
			tblRows.getCurrentRow().put(RowInfo.FIELD_ITEM, aRow.getItem());
		else
			tblRows.getCurrentRow().put(
					RowTable.FIELD_DISP,
					aRow.getTitle(xmlTableSource, (List) xmlFields.get(aRow
							.getSourceID())));
		tblRows.updateUI();

	}

	public XMLData getXmlTableSource() {
		return xmlTableSource;
	}

	public void setXmlTableSource(XMLData xmlTableSource) {
		this.xmlTableSource = xmlTableSource;
	}

	class ButtonPanel extends FPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3181231136289822344L;

		public ButtonPanel() {
			super();
			initUI();
		}

		private void initUI() {

			this.setBottomInset(3);
			this.setTopInset(3);
			this.setLeftInset(3);
			this.setRightInset(3);
			// this.setLayout(new RowPreferedLayout(1));
			this.setLayout(new BorderLayout());

			Box aBox = Box.createVerticalBox();
			aBox.add(Box.createVerticalStrut(150));

			FButton btnAdd = new FButton();
			btnAdd.setTitle("增加");
			btnAdd.addActionListener(new BtnClick("ADD"));
			FButton btnDel = new FButton();
			btnDel.setText("删除");
			btnDel.addActionListener(new BtnClick("DEL"));
			FButton btnUp = new FButton();
			btnUp.setText("向上");
			btnUp.addActionListener(new BtnClick("UP"));
			FButton btnDown = new FButton();
			btnDown.setText("向下");
			btnDown.addActionListener(new BtnClick("DOWN"));
			FButton btnSave = new FButton();
			btnSave.addActionListener(new BtnClick("SAVE"));
			btnSave.setText("保存");
			// FButton btnRefresh = new FButton();
			// btnRefresh.setText("刷新");
			// btnRefresh.addActionListener(new BtnClick("REFRESH"));

			aBox.add(btnAdd);
			aBox.add(btnDel);
			aBox.add(btnUp);
			aBox.add(btnDown);

			aBox.add(Box.createVerticalStrut(30));
			aBox.add(btnSave);
			// aBox.add(btnRefresh);
			// this.add(btnAdd, new TableConstraints(2, 1, true, true));
			// this.add(btnDel, new TableConstraints(2, 1, true, true));
			// this.add(btnUp, new TableConstraints(2, 1, true, true));
			// this.add(btnDown, new TableConstraints(2, 1, true, true));
			// this.add(new JLabel(), new TableConstraints(2, 1, true, true));
			// this.add(btnSave, new TableConstraints(2, 1, true, true));
			// this.add(btnRefresh, new TableConstraints(2, 1, false, true));
			this.add(aBox, BorderLayout.CENTER);
		}

		class BtnClick implements ActionListener {
			String ID;

			public BtnClick(String ID) {
				this.ID = ID;
			}

			public void actionPerformed(ActionEvent e) {
				if (("ADD").equals(ID)) {
					addDefualtRow();
				} else if ("DEL".equals(ID)) {
					delCurRow();
				} else if ("UP".equals(ID)) {
					upward();
				} else if ("DOWN".equals(ID))
					downward();
				else if ("SAVE".equals(ID)) {
					try {
						save();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(RowPanel.this.getParent()
								.getParent(), "保存数据失败!原因 " + e1.getMessage(),
								"提示", JOptionPane.ERROR_MESSAGE);// this, );
						e1.printStackTrace();
					}
				}

			}

		}
	}

	public void addSelectChangeListener(IRowSelectchangeListener l) {
		this.selectChangeListeners.add(l);
	}

	void fireSelectChange() {
		if (selectChangeListeners.size() > 0) {
			int iCount = selectChangeListeners.size();
			for (int i = 0; i < iCount; i++) {
				((IRowSelectchangeListener) selectChangeListeners.get(i))
						.selectChange(this.getcurRow(), tblRows.isOnDrag());
			}
		}
	}

	public XMLData getXmlFields() {
		return xmlFields;
	}

	public void addTitleChangeedListener(ITitleChangedListener arg1) {
		titleChangeListener.add(arg1);
		if (lstRows == null)
			return;
		int iCount = lstRows.size();
		for (int i = 0; i < iCount; i++) {
			((RowInfo) lstRows.get(i)).addTitleChangeedListener(arg1);
		}

	}

	private void setReportID(String sReportID) {
		this.reportID = sReportID;
		if (lstRows == null)
			return;
		int iCount = lstRows.size();
		for (int i = 0; i < iCount; i++) {
			RowInfo aRow = (RowInfo) lstRows.get(i);
			aRow.setReportID(sReportID);
		}

	}

	/**
	 * 检查报表信息是否合格
	 * 
	 * @return
	 */
	private String checkReport() {
		// if (xmlReport.get(IQrBudget.REPORT_ID) == null
		// || xmlReport.get(IQrBudget.LVL_ID) == null)
		// return "未指定报表ID";
		if (xmlReport.get(IQrBudget.REPORT_ID) == null)// 去除了报表级次ID
			return "未指定报表ID";
		Object obj = xmlReport.get(IQrBudget.REPORT_CNAME);
		if (obj == null || Common.isNullStr(obj.toString()))
			return "报表名称不可以为空!";

		obj = xmlReport.get(IQrBudget.TITLE);
		if (obj == null || Common.isNullStr(obj.toString()))
			return "报表标题不可以为空!";

		obj = xmlReport.get(IQrBudget.FIELD_COLUMN);
		try {
			if ((obj == null || Common.isNullStr(obj.toString()) || Integer
					.parseInt(obj.toString()) < 1))
				return "报表的显示列数设置不正确";
		} catch (NumberFormatException e) {
			return "报表的显示列数设置不正确";
		}
		if (lstType.getType().size() == 0)
			return "请选择报表类型!";

		return "";
	}
}
