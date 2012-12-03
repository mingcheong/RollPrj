/**
 * @# repDisplay.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JSplitPane;


import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;

import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.queryreport.reportsh.ibs.IRepDisplay;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.IFieldProvider;
import gov.nbcs.rp.queryreport.reportsh.ui.fieldpro.SearchObj;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.util.Global;

/**Ԥ����ۺϲ�ѯ�����ݴ�һ����ͼ������һ��������ǵĴ���ͼ��
 * �ɸ����û����ã���̬�ز�������ͺ�������á���Щ������һ����Ϣ��������У��羭�ÿ�Ŀ������
 * ��Ŀ�ȡ������������ҪҪ���ϻ��ܡ�
 * ����Ľ��������
 * ����һ�˺������ṩ����������չ�ṩ�����ֱ��������ݺ������չ�����ɱ�ͷ��SUM��䡣�����Ҫ�ṩ
 * ������书�ܡ�
 * �Ƚ�������������ݷ�����ʱ���У��ٽ������ϻ��ܵĲ���
 * ����˵��:

 */
public class RepDisplayUI extends RpModulePanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -31751676273619133L;

	private FundSourcePanel pnlFund;

	private VSelectPanel pnlDiv;

//	private TableListPanel tblPnl;

	private JSplitPane spnlBack;

	/**
	 * �����Ի���
	 */
	private FilterDlg filterDlg = null;

	private FieldProDlg fieldDlg = null;

	IRepDisplay server;

	private String lastGroupFieldName;

	private String last2GroupFieldName;

	public void initize() {
		try {
			this.createToolBar();
			server = RepDispStub.getMethod();
			filterDlg = new FilterDlg();
			fieldDlg = new FieldProDlg();

			//����һ��ָ�
			spnlBack = new JSplitPane();
			spnlBack.setOrientation(JSplitPane.VERTICAL_SPLIT);
			pnlDiv = new VSelectPanel();
			spnlBack.add(pnlDiv, JSplitPane.TOP);
			pnlDiv.setPreferredSize(new Dimension(150, 200));

			FPanel spnlBottom = new FPanel();
			spnlBottom.setLayout(new BorderLayout());

			pnlFund = new FundSourcePanel();
			pnlFund.setPreferredSize(new Dimension(1, 1));

			//			UserInfoEx user = FlowStub.getMethod().getUserInfoExObj(
			//					Global.getUserId());
			FPanel pnlTitle = new FPanel();
		
			pnlTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel lblTitle = new FLabel();
			lblTitle.setText("�ʽ���Դѡ��");
			pnlTitle.add(lblTitle);
			spnlBottom.add(lblTitle, BorderLayout.NORTH);
			spnlBottom.add(pnlFund, BorderLayout.CENTER);
			spnlBack.add(spnlBottom, JSplitPane.BOTTOM);

			spnlBottom.setPreferredSize(new Dimension(250, 500));
			JSplitPane spnlAll = new JSplitPane();
			spnlAll.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			spnlAll.add(spnlBack, JSplitPane.LEFT);
//			tblPnl = new TableListPanel();
//			spnlAll.add(tblPnl, JSplitPane.RIGHT);
//			spnlAll.setDividerLocation(200);

			this.setLayout(new BorderLayout());
			this.add(spnlAll, BorderLayout.CENTER);

			spnlBack.setDividerLocation(400);
		} catch (Exception e) {
			new MessageBox("��ʼ������ʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}

	}

	/**
	 * ת���ɲ�ѯ
	 */
	public void doAdd() {
		//���
		String err = checkSelect();
		if (!Common.isNullStr(err)) {
			MessageBox mb = new MessageBox(err, MessageBox.INFOMATION,
					MessageBox.BUTTON_OK);
			mb.setVisible(true);
			mb.dispose();
			return;
		}
//		try {
//			BaseUtil.threadRun(Global.mainFrame, "���ڲ�ѯ����......", this);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			new Thread() {
//				public void run() {
//					try {
//						String userID = Global.getUserId();
//						server.finalTreat(userID);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//			}.start();
//		}

	}

//	public void doExpExcel() {
//		tblPnl.doExport();
//	}
//
//	public void doPrint() {
//		tblPnl.doPrint();
//	}

	public String checkSelect() {
		String sErr = pnlDiv.check();
		if (!Common.isNullStr(sErr))
			return sErr;
		sErr = pnlFund.check();
		if (!Common.isNullStr(sErr))
			return sErr;
		sErr = fieldDlg.getPnlPro().check();
		if (!Common.isNullStr(sErr))
			return sErr;
		return "";

	}

	/**
	 * ���ñ�ͷ
	 */
	public void doDelete() {

		fieldDlg.show();
		if (Common.isNullStr(fieldDlg.getPnlPro().check()))
			try {
				List lstV = fieldDlg.getPnlPro().getVList();
				List lstH = fieldDlg.getPnlPro().getHList();
				String filter = filterDlg.getFilter("");
				String divFilter = getDivFilter();
				filter = filter + " and " + divFilter;

				//ƴд��ͷ
				DataSet dsHeader = DataSet.create();
				// FieldProFactory.createDefaultHeader(dsHeader);

				lastGroupFieldName = null;
				last2GroupFieldName = null;
				int iIndex = 0;
				// ��������ͷ
				for (Iterator itr = lstV.iterator(); itr.hasNext();) {
					IFieldProvider aVPro = (IFieldProvider) itr.next();
					String ename = null;
					if (aVPro.getCodeFieldEName() != null) {
						dsHeader.append();
						String strLvlId = "000" + iIndex;
						ename = aVPro.getCodeFieldEName();
						setHeader(dsHeader, strLvlId,
								aVPro.getCodeFieldCName(), ename);
						iIndex++;
					}
					if (aVPro.getNameFieldEName() != null) {
						dsHeader.append();
						String strLvlId = "000" + iIndex;
						ename = aVPro.getNameFieldEName();
						setHeader(dsHeader, strLvlId,
								aVPro.getNameFieldCName(), ename);
						iIndex++;
					}

					if (aVPro.getExtFields() != null) {
						String[][] extFields = aVPro.getExtFields();
						for (int i = 0; i < extFields.length; i++) {
							String[] ef = extFields[i];
							if (ef != null) {
								String fieldEName = ef[0];
								String fieldCName = ef[1];

								dsHeader.append();
								String strLvlId = "000" + iIndex;
								setHeader(dsHeader, strLvlId, fieldCName,
										fieldEName);
								iIndex++;
							}
						}
					}

					last2GroupFieldName = lastGroupFieldName;
					lastGroupFieldName = ename;
				}

				int iCount = lstH.size();

				if (iCount > 0) {
					for (int i = 0; i < iCount; i++) {
						IFieldProvider aVPro = (IFieldProvider) lstH.get(i);
						SearchObj obj = aVPro.getSearchObj(dsHeader, iIndex,
								IRepDisplay.MONEY_FIELD);
						//�޸���ʼ����
						iIndex = obj.getLastIndex();

					}
				} else {
//					SearchObj obj = FieldProFactory.genTotalHeader(dsHeader,
//							IRepDisplay.MONEY_FIELD, iIndex);
//					// �޸���ʼ����
//					iIndex = obj.getLastIndex();
				}
//				tblPnl.refreshData(dsHeader, new ArrayList());

			} catch (Exception e) {
				new MessageBox("��ѯʧ��!", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
				e.printStackTrace();
			} finally {
				
			}
	}

	private void doQuery() {
		String userID = Global.getUserId();
		try {

			List lstV = fieldDlg.getPnlPro().getVList();
			List lstH = fieldDlg.getPnlPro().getHList();
			String filter = filterDlg.getFilter("");
			String divFilter = getDivFilter();
			filter = filter + " and " + divFilter;

			List lstSearchObj = new ArrayList();
			List lstFun = server.getFunfields(pnlFund.getSelectSource());
			//ƴд��ͷ
			DataSet dsHeader = DataSet.create();
			// FieldProFactory.createDefaultHeader(dsHeader);

			lastGroupFieldName = null;
			last2GroupFieldName = null;
			int iIndex = 0;
			// ��������ͷ
			for (Iterator itr = lstV.iterator(); itr.hasNext();) {
				IFieldProvider aVPro = (IFieldProvider) itr.next();
				String ename = null;
				if (aVPro.getCodeFieldEName() != null) {
					dsHeader.append();
					String strLvlId = "000" + iIndex;
					ename = aVPro.getCodeFieldEName();
					setHeader(dsHeader, strLvlId, aVPro.getCodeFieldCName(),
							ename);
					iIndex++;
				}
				if (aVPro.getNameFieldEName() != null) {
					dsHeader.append();
					String strLvlId = "000" + iIndex;
					ename = aVPro.getNameFieldEName();
					setHeader(dsHeader, strLvlId, aVPro.getNameFieldCName(),
							ename);
					iIndex++;
				}

				if (aVPro.getExtFields() != null) {
					String[][] extFields = aVPro.getExtFields();
					for (int i = 0; i < extFields.length; i++) {
						String[] ef = extFields[i];
						if (ef != null) {
							String fieldEName = ef[0];
							String fieldCName = ef[1];

							dsHeader.append();
							String strLvlId = "000" + iIndex;
							setHeader(dsHeader, strLvlId, fieldCName,
									fieldEName);
							iIndex++;
						}
					}
				}

				last2GroupFieldName = lastGroupFieldName;
				lastGroupFieldName = ename;
			}

			// ��1���ʽ���
			String firstMoneyFieldName = null;

			int iCount = lstH.size();
			String subTotalField = "";
			String sumFields = "";
			String fields = "";
			String showFields = "";

			if (iCount > 0) {
				for (int i = 0; i < iCount; i++) {
					IFieldProvider aVPro = (IFieldProvider) lstH.get(i);
					SearchObj obj = aVPro.getSearchObj(dsHeader, iIndex,
							IRepDisplay.MONEY_FIELD);
					//�޸���ʼ����
					iIndex = obj.getLastIndex();
					showFields += obj.getShowFieldExp() + ",";
					subTotalField += obj.getSubTotalExp() + ",";
					fields = fields + obj.getFieldExp() + ",";
					sumFields = sumFields + obj.getSumExp() + ",";

					if (i == (iCount - 1)) {
						firstMoneyFieldName = obj.getSubTotalField();
					}

					lstSearchObj.add(obj);
				}
			} else {
//				SearchObj obj = FieldProFactory.genTotalHeader(dsHeader,
////						IRepDisplay.MONEY_FIELD, iIndex);
//				// �޸���ʼ����
//				iIndex = obj.getLastIndex();
//				showFields += obj.getShowFieldExp() + ",";
//				// subTotalField += obj.getSubTotalExp() + ",";
//				fields = fields + obj.getFieldExp() + ",";
//				sumFields = sumFields + obj.getSumExp() + ",";
//
//				firstMoneyFieldName = obj.getFieldExp();
//
//				lstSearchObj.add(obj);
			}
			showFields = showFields.substring(0, showFields.length() - 1);
			fields = fields.substring(0, fields.length() - 1);
			sumFields = sumFields.substring(0, sumFields.length() - 1);

			iCount = lstV.size();
			// String sql = "";
			StringBuffer sHeaderSql = new StringBuffer();
			//			StringBuffer sFilterSql = new StringBuffer();
			//			StringBuffer sGroupBySql = new StringBuffer();
			//			StringBuffer sOrderBySql = new StringBuffer();

			// �����һ���������⣬���λ����
			int aheadVColXHLength = 0;
			int lastVColXHLength = 0;

			// ĩ���Ƿ���ָ��������
			boolean hasSpecifiedOrder = false;
			boolean isOrderByMoney = false;
			boolean isGroupUp = false;

			StringBuffer sbSql = new StringBuffer();
			String sqlSum;
			for (int i = 0; i < iCount; i++) {
				IFieldProvider aHPro = (IFieldProvider) lstV.get(i);
				sHeaderSql.append(","
						+ (aHPro.isGroupUp() ? aHPro.getIndentMaxHeaderSql()
								: aHPro.getMaxHeaderSql()));
				//				sFilterSql.append(" or " + aHPro.getFilterSql());
				//				sGroupBySql.append("," + aHPro.getGroupBySql());
				//				sOrderBySql.append("," + aHPro.getOrderBySql());		

				// ����ʾǰ���� by qinj at Oct 23, 2009
				int topCount = 0;
				if (i == 0 && filterDlg.getTopCount() > 0) {
					topCount = filterDlg.getTopCount();
				}

				sqlSum = aHPro.getSqlGroup(fields, sumFields,
						IRepDisplay.TEMP_TABLE, lstV, i, topCount);
				if (Common.isNullStr(sqlSum)) {
					MessageBox mb = new MessageBox("ȡ�ò�ѯ���ʧ��!",
							MessageBox.INFOMATION, MessageBox.BUTTON_OK);
					mb.setVisible(true);
					mb.dispose();
					return;
				}
				// System.out.println(sqlSum);
				sbSql.append(" union all ").append(sqlSum);

				if (i < iCount - 1) {
					aheadVColXHLength += aHPro.getCurrLvlLength();
				} else {
					lastVColXHLength = aHPro.getCurrLvlLength();
					isOrderByMoney = aHPro.isOrderByMoney();
					hasSpecifiedOrder = aHPro.hasSpecifiedOrder();
					isGroupUp = aHPro.isGroupUp();
				}
			}

			// ����ʾǰ���У���ĩ�������� by qinj at Oct 23, 2009
			if (!hasSpecifiedOrder && filterDlg.getTopCount() > 0) {
				isOrderByMoney = true;
			}

			String sSql = "select " + sHeaderSql.substring(1) + ","
					+ subTotalField + showFields + " from ("
					+ sbSql.substring(10) + ") a group by a.xh";
			// ��������򣬲�֧��ĩ���İ����λ���
			if (!isGroupUp && isOrderByMoney) {
				sSql += " order by substr(a.xh, 1, " + aheadVColXHLength
						+ ") || to_char(999999999999 - 100*"
						+ firstMoneyFieldName + ") || substr(a.xh, "
						+ (aheadVColXHLength + 1) + ", " + lastVColXHLength
						+ ")";
			} else {
				sSql += " order by a.xh";
			}

			//���������������е����ݶ�������ʱ����
			//		server.finalTreat(userID);
			if (!fieldDlg.getPnlPro().isSelectFun())
				server.insertToTempTable((String) lstFun.get(1), filter,
						(String) lstFun.get(0), Global.getUserId());
			else
				server.insertToTempTableIncFun((String) lstFun.get(1), filter,
						(String) lstFun.get(0), Global.getUserId(), divFilter);

			// System.out.println(sSql);
			List lstData = QueryStub.getClientQueryTool().findBySql(sSql);

			// ����ʾ����ֵ��Ϊ0���� by qinj at Oct 23, 2009
			if (filterDlg.isCleanZeroColumn()) {
				if (lstData != null && lstData.size() > 0) {
					Map r0 = (Map) lstData.get(0);
					dsHeader.edit();
					dsHeader.afterLast();
					List lstNeedDel = new ArrayList();
					while (dsHeader.prior()) {
						String fieldName = dsHeader.fieldByName("field_ename")
								.getString();
						if (fieldName.startsWith("ff")
								&& "0".equals(r0.get(fieldName))) {
							//							dsHeader.delete();
							lstNeedDel.add(dsHeader.fieldByName("lvl_id")
									.getString());
						}
					}
					deleteCol(dsHeader, lstNeedDel);
					dsHeader.applyUpdate();
				}
			}
			//�����Ϣȫ��ʾ�Ĳ���ѡ�У�����
			if (filterDlg.isShowAllInfo()) {
				stuffEmptyValue(lstData, lstV);
			}

			// ����ʾǰ���� by qinj at Oct 23, 2009
			// ��֧��ĩ���İ����λ���
			if (!isGroupUp && filterDlg.getTopCount() > 0) {
				pickTopN(lstData, lastGroupFieldName, last2GroupFieldName,
						firstMoneyFieldName, filterDlg.getTopCount());
			}

//			tblPnl.setBatchNo(filterDlg.getBatchNo());
//			tblPnl.setDataType(filterDlg.getDataType());
//			tblPnl.refreshData(dsHeader, lstData);

		} catch (Exception e) {
			new MessageBox("��ѯʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		} finally {
			// ��Ϊ��ѯ��������һ���߳�ִ��������� by qinj at Oct 24, 2009
			// try {
			// server.finalTreat(userID);
			// } catch (Exception e) {
			// e.printStackTrace();
			//			}
		}
	}

	private void pickTopN(List lstData, String lastGroupFieldName,
			String last2GroupFieldName, String topCol, int topCount) {
		if (last2GroupFieldName == null || lastGroupFieldName == null)
			return;
		Iterator itr = lstData.iterator();
		// �����ܼ�
		if (itr.hasNext())
			itr.next();

		int idxTop = 0;
		while (itr.hasNext()) {
			Map m = (Map) itr.next();
			if (Common.isNullStr((String) m.get(last2GroupFieldName))) {
				idxTop++;
			} else {
				idxTop = 0;
			}
			if (idxTop > topCount) {
				itr.remove();
			}
		}
	}

	private void setHeader(DataSet dsHeader, String strLvlId, String cname,
			String ename) throws Exception {
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue(
//				strLvlId.substring(strLvlId.length() - 4));
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue(cname);
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(160));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("�ַ���");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue(ename);
	}

	//ת������������
	public void doCancel() {

		filterDlg.show();
	}

	public String getDivFilter() {
		List lstDiv = pnlDiv.getSelectDiv();
//		return BaseUtil.getInExpress(lstDiv, "div_code", "div_code", true);
		return null;

	}

	public void doRun() {
		doQuery();

	}

	/**
	 * ����λ��ʾԤ����ƽ��
	 */
	public void doSave() {
		// tblPnl.viewBudget();
//		tblPnl.viewPayout();
	}

	public CustomTree getFtreDivName() {
		return pnlDiv.getTreeDiv();
	}

	/**
	 * ����ɷ����¼������Ŀ�ֵ
	 * ��������˳��ȡ�������ṩ����ȡ����ʾ���У��ٰ�˳�������ݣ���鵽��ֵ��һ�У�����¼��mapPass�У�����
	 * �����Ĳ���·�����У���¼�´���ÿ�е�ֵ���ټ����һ�У�����ǿհ����кŲ���mapPass ��������ǿ����¼��ֵ�������¼��
	 * �������
	 * @param lstData
	 * @param lstVPro
	 */
	private void stuffEmptyValue(List lstData, List lstVPro) throws Exception {
		//�洢��Ҫ�����е�ID
		if (lstData == null || lstData.isEmpty())
			return;
		Map mapPass = new HashMap();
		mapPass.put("" + 0, null);
		int dataCount = lstData.size();
		int vproCount = lstVPro.size();
		for (int i = 0; i < vproCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstVPro.get(i);
			//ȡ����ʾ����
			List lstFields = getShowFields(aPro);
			List lstLastValue = null;//��¼��һ�п��õ�����ֵ
			for (int j = 0; j < dataCount; j++) {
				//��������ų���������
				if (mapPass.containsKey("" + j))
					continue;
				//ȡ�ô��е�����
				List curValue = getRowValues(lstFields, (Map) lstData.get(j));
				//����ǿգ����������е����ݸ���
				if (curValue == null) {
					if (lstLastValue == null) {
						throw new Exception("û��ȡ����һ�ε�����!");
					}
					writeValue(lstLastValue, lstFields, (Map) lstData.get(j));
				} else {//�������ֵ��Ϊ�գ���ֵ��lstLastValue
					lstLastValue = curValue;
					//����ӵ������Ļ�����
					mapPass.put("" + j, null);
				}
			}
		}

	}

	/**ȡ����չ�ֵ���*/
	private List getShowFields(IFieldProvider aPro) {
		//һ����ʾ���ֶ��� codefield,namefield,ext
		List lstField = new ArrayList();
		if (aPro.getCodeFieldEName() != null) {
			lstField.add(aPro.getCodeFieldEName());
		}
		if (aPro.getNameFieldEName() != null) {
			lstField.add(aPro.getNameFieldEName());
		}
		if (aPro.getExtFields() != null) {
			String[][] strArr = aPro.getExtFields();
			for (int i = 0; i < strArr.length; i++) {
				lstField.add(strArr[i][0]);

			}
		}
		return lstField;
	}

	/**ȡ������ָ���е�ֵ�����ȫ�ǿգ��򷵻ؿ�*/
	private List getRowValues(List lstFields, Map mapRow) {
		boolean isNotEmpty = false;
		int iCount = lstFields.size();
		List lstValue = new ArrayList();
		for (int i = 0; i < iCount; i++) {
			Object value = mapRow
					.get(lstFields.get(i).toString().toLowerCase());

			lstValue.add(value);
			if (value != null && !"".equals(value))
				isNotEmpty = true;
		}
		if (!isNotEmpty)
			return null;
		return lstValue;

	}

	private void writeValue(List lstValue, List lstFields, Map mapRow) {
		int iCount = lstFields.size();
		for (int i = 0; i < iCount; i++) {
			mapRow.put(lstFields.get(i), lstValue.get(i));
		}
	}

	/**ɾ��ָ�����У����������ϼ�*/
	private void deleteCol(DataSet dsHeader, List lstLvl) {
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		try {
			Map lstToDel = new HashMap();

			Node node = hg.generate(dsHeader, "lvl_id", SysCodeRule
					.createClient(new int[] { 4, 8, 12, 16, 20, 24 }),
					"lvl_id ");
			Map mapLvlToNode = new HashMap();
			putNodes(node, mapLvlToNode);
			int iCount = lstLvl.size();
			for (int i = 0; i < iCount; i++) {
				String curLvl = (String) lstLvl.get(i);
				Node curNode = (Node) mapLvlToNode.get(curLvl);
				lstToDel.put(curLvl, null);
				Node parent = curNode.getParent();
				while (parent != null && parent.getChildrenCount() == 1) {
					lstToDel.put(parent.getSortByValue(), null);
					parent.deleteSubNode(parent.getChildAt(0));
					parent = parent.getParent();

				}
			}
			if (!lstToDel.isEmpty()) {
				dsHeader.afterLast();
				while (dsHeader.prior()) {
					if (lstToDel.containsKey(dsHeader.fieldByName("lvl_id")
							.getString()))
						dsHeader.delete();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void putNodes(Node node, Map mapLvlToNode) {
		mapLvlToNode.put(node.getSortByValue(), node);
		if (node.getChildrenCount() != 0) {
			int iCount = node.getChildrenCount();
			for (int i = 0; i < iCount; i++) {
				putNodes(node.getChildAt(i), mapLvlToNode);
			}
		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
