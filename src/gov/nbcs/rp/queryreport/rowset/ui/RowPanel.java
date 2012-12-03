/**
 * @# RowPanel.java    <�ļ���>
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
 * ����˵��:ȫ���б���ʾ����壬���������б���ť��
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

	private List lstRows;// ��LIST��ʽ�洢

	private XMLData xmlRows;// ��xmldata��ʽ�洢��ΪROWID

	private List lstXMLRows;

	private List lstXMLCons;

	private XMLData xmlTableSource;

	private XMLData xmlFields;

	private List selectChangeListeners = new ArrayList();

	private List titleChangeListener = new ArrayList();

	private boolean isAdd;// �Ƿ�������

	private XMLData xmlReport;

	int rowCount = 500;// ��������ʱ��ITEMID

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
	 * ��ʼ������ ע����ѯʱһ��Ҫ���е�itemID,��������lineNum�ֶ�����
	 * 
	 * @param lstRows��ѯ��������Ϣ
	 * @param lstCons//��ѯ�����ֶ���Ϣ
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

			// ��ʼ����
			for (int i = 0; i < iCount; i++) {
				RowInfo aRow = new RowInfo((XMLData) lstXMLRows.get(i), null);
				aRow.addTitleChangeedListener(this);
				addRow(aRow);
			}
			// ��ʼ������
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
	 * ���һ������
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
	 * ���һ��Ĭ����
	 * 
	 */
	public void addDefualtRow() {
		if (Common.isNullStr(this.reportID)) {
			return;
		}
		RowInfo aRow = new RowInfo();
		aRow.setReportID(this.reportID);
		aRow.setItemID(getNextRowID());
		aRow.setItem("δָ������");
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
	 * ȡ�õ�ǰ����
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
	 * ɾ��һ�У���Ҫ�ڻ����ж�Ҫɾ��
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
	 * ���һ��
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
	 * ��һ���б�
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

			// �����������Ҫ�������ñ���ID;
			{

				if (isAdd) {

			
				}

			}
			// --------------
			// ���������Ϣ
			String sErr = checkReport();
			if (!sErr.equals("")) {

				JOptionPane.showMessageDialog(this.getParent().getParent(),
						sErr, "��ʾ", JOptionPane.INFORMATION_MESSAGE);// this,
				// );
				return;
			}
			// -------------
			int iCount = lstRows.size();
			List lstSql = new ArrayList();
			for (int i = 0; i < iCount; i++) {
				RowInfo cuRow = ((RowInfo) lstRows.get(i));
				// �������е�˳��ת������ͼ˳��
				String row = String.valueOf(tblRows
						.convertModelRowIndexToView(i) + 1);
				cuRow.setItemID(row);
				try {
					List lstTemp = cuRow.getInsertSql(Global.getSetYear(), row,
							TableSourceServer.getATabelSource(cuRow
									.getSourceID()));
					// ͨ�����ݿ�����֤SQL����ѯ����ȷ��
					// ����ѯ������ȷ��Ϣ add by XXL 20080919
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
							throw new Exception("��ѯ��䲻��ȷ�����飬" + e.getMessage());
						}
					}
					lstSql.addAll(lstTemp);
				} catch (Exception e) {
					// ��λ����
					tblRows.setSelectRow(tblRows.convertModelRowIndexToView(i));
					throw e;
				}

			}

			((IRowSet) BOCache.getBO("fb.rowSetService"))
					.saveRows(lstSql, reportID, Global.getSetYear(), xmlReport,
							lstType.getType());
			isAdd = false;

			// �޸Ļ����ӽڵ�ʱ��ˢ�����ڵ�
			defineReport
					.refreshNodeEdit(xmlReport, lstType.getType(), reportID);

			JOptionPane.showMessageDialog(this.getParent().getParent(),
					"���ݱ���ɹ�!");
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
			btnAdd.setTitle("����");
			btnAdd.addActionListener(new BtnClick("ADD"));
			FButton btnDel = new FButton();
			btnDel.setText("ɾ��");
			btnDel.addActionListener(new BtnClick("DEL"));
			FButton btnUp = new FButton();
			btnUp.setText("����");
			btnUp.addActionListener(new BtnClick("UP"));
			FButton btnDown = new FButton();
			btnDown.setText("����");
			btnDown.addActionListener(new BtnClick("DOWN"));
			FButton btnSave = new FButton();
			btnSave.addActionListener(new BtnClick("SAVE"));
			btnSave.setText("����");
			// FButton btnRefresh = new FButton();
			// btnRefresh.setText("ˢ��");
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
								.getParent(), "��������ʧ��!ԭ�� " + e1.getMessage(),
								"��ʾ", JOptionPane.ERROR_MESSAGE);// this, );
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
	 * ��鱨����Ϣ�Ƿ�ϸ�
	 * 
	 * @return
	 */
	private String checkReport() {
		// if (xmlReport.get(IQrBudget.REPORT_ID) == null
		// || xmlReport.get(IQrBudget.LVL_ID) == null)
		// return "δָ������ID";
		if (xmlReport.get(IQrBudget.REPORT_ID) == null)// ȥ���˱�����ID
			return "δָ������ID";
		Object obj = xmlReport.get(IQrBudget.REPORT_CNAME);
		if (obj == null || Common.isNullStr(obj.toString()))
			return "�������Ʋ�����Ϊ��!";

		obj = xmlReport.get(IQrBudget.TITLE);
		if (obj == null || Common.isNullStr(obj.toString()))
			return "������ⲻ����Ϊ��!";

		obj = xmlReport.get(IQrBudget.FIELD_COLUMN);
		try {
			if ((obj == null || Common.isNullStr(obj.toString()) || Integer
					.parseInt(obj.toString()) < 1))
				return "�������ʾ�������ò���ȷ";
		} catch (NumberFormatException e) {
			return "�������ʾ�������ò���ȷ";
		}
		if (lstType.getType().size() == 0)
			return "��ѡ�񱨱�����!";

		return "";
	}
}
