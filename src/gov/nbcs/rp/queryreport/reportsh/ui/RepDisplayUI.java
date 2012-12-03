/**
 * @# repDisplay.java    <文件名>
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

/**预算的综合查询，数据从一张视图中来，一般就是我们的大视图。
 * 可根据用户设置，动态地产生纵向和横向的设置。这些设置是一组信息的组合排列，如经济科目，功能
 * 科目等。横向的如有需要要向上汇总。
 * 总体的解决方案：
 * 将做一此横向功能提供器和纵向扩展提供器，分别来负责纵横向的扩展，生成表头和SUM语句。横向的要提供
 * 分组语句功能。
 * 先将条件分组后将数据放入临时表中，再进行向上汇总的操作
 * 功能说明:

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
	 * 条件对话框
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

			//最后的一块分隔
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
			lblTitle.setText("资金来源选择");
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
			new MessageBox("初始化界面失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}

	}

	/**
	 * 转换成查询
	 */
	public void doAdd() {
		//检查
		String err = checkSelect();
		if (!Common.isNullStr(err)) {
			MessageBox mb = new MessageBox(err, MessageBox.INFOMATION,
					MessageBox.BUTTON_OK);
			mb.setVisible(true);
			mb.dispose();
			return;
		}
//		try {
//			BaseUtil.threadRun(Global.mainFrame, "正在查询数据......", this);
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
	 * 设置表头
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

				//拼写表头
				DataSet dsHeader = DataSet.create();
				// FieldProFactory.createDefaultHeader(dsHeader);

				lastGroupFieldName = null;
				last2GroupFieldName = null;
				int iIndex = 0;
				// 纵向分组表头
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
						//修改起始的列
						iIndex = obj.getLastIndex();

					}
				} else {
//					SearchObj obj = FieldProFactory.genTotalHeader(dsHeader,
//							IRepDisplay.MONEY_FIELD, iIndex);
//					// 修改起始的列
//					iIndex = obj.getLastIndex();
				}
//				tblPnl.refreshData(dsHeader, new ArrayList());

			} catch (Exception e) {
				new MessageBox("查询失败!", e.getMessage(), MessageBox.ERROR,
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
			//拼写表头
			DataSet dsHeader = DataSet.create();
			// FieldProFactory.createDefaultHeader(dsHeader);

			lastGroupFieldName = null;
			last2GroupFieldName = null;
			int iIndex = 0;
			// 纵向分组表头
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

			// 第1个资金列
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
					//修改起始的列
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
//				// 修改起始的列
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

			// 除最后一个纵向列外，序号位长度
			int aheadVColXHLength = 0;
			int lastVColXHLength = 0;

			// 末级是否有指定的排序
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

				// 仅显示前几行 by qinj at Oct 23, 2009
				int topCount = 0;
				if (i == 0 && filterDlg.getTopCount() > 0) {
					topCount = filterDlg.getTopCount();
				}

				sqlSum = aHPro.getSqlGroup(fields, sumFields,
						IRepDisplay.TEMP_TABLE, lstV, i, topCount);
				if (Common.isNullStr(sqlSum)) {
					MessageBox mb = new MessageBox("取得查询语句失败!",
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

			// 仅显示前几行，按末级金额倒排序 by qinj at Oct 23, 2009
			if (!hasSpecifiedOrder && filterDlg.getTopCount() > 0) {
				isOrderByMoney = true;
			}

			String sSql = "select " + sHeaderSql.substring(1) + ","
					+ subTotalField + showFields + " from ("
					+ sbSql.substring(10) + ") a group by a.xh";
			// 按金额排序，不支持末级的按级次汇总
			if (!isGroupUp && isOrderByMoney) {
				sSql += " order by substr(a.xh, 1, " + aheadVColXHLength
						+ ") || to_char(999999999999 - 100*"
						+ firstMoneyFieldName + ") || substr(a.xh, "
						+ (aheadVColXHLength + 1) + ", " + lastVColXHLength
						+ ")";
			} else {
				sSql += " order by a.xh";
			}

			//根据条件，将所有的数据都放入临时表中
			//		server.finalTreat(userID);
			if (!fieldDlg.getPnlPro().isSelectFun())
				server.insertToTempTable((String) lstFun.get(1), filter,
						(String) lstFun.get(0), Global.getUserId());
			else
				server.insertToTempTableIncFun((String) lstFun.get(1), filter,
						(String) lstFun.get(0), Global.getUserId(), divFilter);

			// System.out.println(sSql);
			List lstData = QueryStub.getClientQueryTool().findBySql(sSql);

			// 不显示所有值均为0的列 by qinj at Oct 23, 2009
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
			//如果信息全显示的参数选中，则处理
			if (filterDlg.isShowAllInfo()) {
				stuffEmptyValue(lstData, lstV);
			}

			// 仅显示前几行 by qinj at Oct 23, 2009
			// 不支持末级的按级次汇总
			if (!isGroupUp && filterDlg.getTopCount() > 0) {
				pickTopN(lstData, lastGroupFieldName, last2GroupFieldName,
						firstMoneyFieldName, filterDlg.getTopCount());
			}

//			tblPnl.setBatchNo(filterDlg.getBatchNo());
//			tblPnl.setDataType(filterDlg.getDataType());
//			tblPnl.refreshData(dsHeader, lstData);

		} catch (Exception e) {
			new MessageBox("查询失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		} finally {
			// 改为查询结束后起一个线程执行清除任务 by qinj at Oct 24, 2009
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
		// 跳过总计
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
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("是");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(160));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("字符型");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue(ename);
	}

	//转换成设置条件
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
	 * 按单位显示预算编制结果
	 */
	public void doSave() {
		// tblPnl.viewBudget();
//		tblPnl.viewPayout();
	}

	public CustomTree getFtreDivName() {
		return pnlDiv.getTreeDiv();
	}

	/**
	 * 填充由分组下级产生的空值
	 * 方法：按顺序取得纵你提供器，取得显示的列，再按顺序检查数据，检查到有值的一行，并记录到mapPass中，以让
	 * 后续的操作路过此行，记录下此行每列的值，再检查下一行，如果是空白且行号不在mapPass ，如果不是空则记录下值继续向下检查
	 * 如此类推
	 * @param lstData
	 * @param lstVPro
	 */
	private void stuffEmptyValue(List lstData, List lstVPro) throws Exception {
		//存储不要填充的行的ID
		if (lstData == null || lstData.isEmpty())
			return;
		Map mapPass = new HashMap();
		mapPass.put("" + 0, null);
		int dataCount = lstData.size();
		int vproCount = lstVPro.size();
		for (int i = 0; i < vproCount; i++) {
			IFieldProvider aPro = (IFieldProvider) lstVPro.get(i);
			//取得显示的列
			List lstFields = getShowFields(aPro);
			List lstLastValue = null;//记录下一行可用的数据值
			for (int j = 0; j < dataCount; j++) {
				//如果是在排除的则跳过
				if (mapPass.containsKey("" + j))
					continue;
				//取得此行的数据
				List curValue = getRowValues(lstFields, (Map) lstData.get(j));
				//如果是空，则用上面行的数据覆盖
				if (curValue == null) {
					if (lstLastValue == null) {
						throw new Exception("没有取到上一次的数据!");
					}
					writeValue(lstLastValue, lstFields, (Map) lstData.get(j));
				} else {//如果此行值不为空，则赋值给lstLastValue
					lstLastValue = curValue;
					//并添加到跳过的缓存中
					mapPass.put("" + j, null);
				}
			}
		}

	}

	/**取得所展现的列*/
	private List getShowFields(IFieldProvider aPro) {
		//一般显示的字段有 codefield,namefield,ext
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

	/**取得所有指定列的值，如果全是空，则返回空*/
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

	/**删除指定的列，并考虑其上级*/
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
