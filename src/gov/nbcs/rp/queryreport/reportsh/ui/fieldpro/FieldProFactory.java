/**
 * @# FieldProFactory.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;

/**
 * 功能说明:工厂类
1.4.2
 */
public class FieldProFactory {

	public static final String PRE = "ff";
	
	public static final String TOTAL_TITLE="　小计　"; 

	public static IFieldProvider getFieldProvider(String type) {
		if (IFilterProvider.TYPE_ACCT.equals(type)) {
			return new AcctFieldProvider();
		} else if (IFilterProvider.TYPE_ACCT_TYPE.equals(type)) {
			return new AcctTypeFieldProvider();
		} else if (IFilterProvider.TYPE_ACCT_JJ.equals(type)) {
			return new AcctJJFieldProvider();
		} else if (IFilterProvider.TYPE_PRJSORT.equals(type))
			return new PrjSortFieldProvider();
		else if (IFilterProvider.TYPE_PROJECT.equals(type))
			return new ProjectFieldProvider();
		else if (IFilterProvider.TYPE_DEP.equals(type))
			return new DepFieldProvider();
		else if (IFilterProvider.TYPE_DIV_KIND.equals(type))
			return new DivKindFieldProvider();
		else if (IFilterProvider.TYPE_DIV_FMKIND.equals(type))
			return new DivFMKindFieldProvider();
		else if (IFilterProvider.TYPE_FUN.equals(type))
			return new FunFieldProvider();
		else if (IFilterProvider.TYPE_PRJSTATUS.equals(type))
			return new PrjStatusFieldProvider();
	
		return null;
	}
	
//	public static SearchObj genTotalHeader(DataSet dsHeader, String sumField,
//			int colStartIndex) throws Exception {
//		SearchObj searchObj = new SearchObj();
//		String lvl = BaseUtil.stufCharPre("" + (colStartIndex + 1), 4, '0');
//		searchObj.setLastIndex(1 + colStartIndex);
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue(lvl);
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("总计");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue(PRE + "0000");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("是");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(120));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue(
//				"#,###.##");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("浮点型");
//
//		searchObj.setDsHeader(dsHeader);
//
//		String fieldName = PRE + "0000";
//		searchObj.setFieldExp(fieldName);
//		searchObj.setSumExp("sum(" + sumField + ") as " + fieldName);
//		searchObj.setShowFieldExp("sum(" + fieldName + ") as " + fieldName);
//
//		return searchObj;
//	}

	/**
	 * 生成表头字段，查询语句等信息体
	 * 
	 * @param dsSelect
	 *            选择的信息，如功能科目
	 * @param idField
	 *            信息中的ID字段
	 * @param nameField
	 *            信息中的名称字段
	 * @param sumField
	 *            分组加和的字段
	 * @param parentField
	 *            父亲字段
	 * @param sortField
	 *            排序字段名，兼CODE字段， codeField 要拼写的条件字段名，如 acct_code等
	 * @return
	 * @throws Exception
	 */
	public static SearchObj genHeader(DataSet dsHeader, DataSet dsSelect,
			String idField, String nameField, String parentField,
			String sortField, String sumField, String codeField,
			int colStartIndex) throws Exception {
		SearchObj searchObj = new SearchObj();
		if (dsSelect == null || dsSelect.isEmpty())
			return null;
		// 特殊处理资金来源
		dsSelect.beforeFirst();
		if (dsSelect.next()) {
			dsSelect.insert();
			dsSelect.fieldByName(parentField).setValue("");
			dsSelect.fieldByName(idField).setValue("");
			dsSelect.fieldByName(nameField).setValue(TOTAL_TITLE);
			dsSelect.fieldByName(sortField).setValue("0000");
			dsSelect.fieldByName(codeField).setValue("0000");
		}
		
		//先生成树状
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsSelect, idField, nameField, parentField,
				sortField);
		//遍历并生成临时的字段
		int iCount = node.getChildrenCount();

		List lstLeaf = new ArrayList();

//		for (int i = 0; i < iCount; i++) {
//			treatNode("", i + colStartIndex, node.getChildAt(i), dsHeader,
//					lstLeaf);
//		}
		searchObj.setLastIndex(iCount + colStartIndex);
		//根据叶节点生成字段
		iCount = lstLeaf.size();
		StringBuffer sbField = new StringBuffer();
		StringBuffer sbSumField = new StringBuffer();
		StringBuffer sbShowField = new StringBuffer();
		StringBuffer sbSubTotal = new StringBuffer("sum(");
		String strSubTotal = "";
		for (int i = 0; i < iCount; i++) {
			Node aNode = (Node) lstLeaf.get(i);
			//将级次作为参数放入节点中，以便生成字段
			String fieldName = PRE + aNode.getAttribute("lvl_ID");
			String code = (String) aNode.getSortByValue();

			if (!TOTAL_TITLE.equals(aNode.getText())) {
				// 拼写CASE
				sbSumField.append("sum(").append(" case when ").append(
						codeField).append(" like '").append(code).append(
						"%' then ").append(sumField).append(" else 0 end) as ")
						.append(fieldName).append(",");
				sbField.append(fieldName).append(",");
				sbShowField.append("sum(").append(fieldName).append(") as ").append(fieldName).append(",");
				sbSubTotal.append(fieldName).append("+");
			} else {
				strSubTotal = fieldName;
			}			
		}

		dsHeader.applyUpdate();
		searchObj.setDsHeader(dsHeader);
		searchObj.setFieldExp(sbField.substring(0, sbField.length() - 1));
		searchObj.setSumExp(sbSumField.substring(0, sbSumField.length() - 1));
		searchObj.setShowFieldExp(sbShowField.substring(0, sbShowField.length() - 1));
		searchObj.setSubTotalExp(sbSubTotal.substring(0, sbSubTotal.length() - 1) + ") as " + strSubTotal);
		searchObj.setSubTotalField(strSubTotal);
		return searchObj;
	}

	/**
	 * 生成表头字段，查询语句等信息体
	 * @param dsSelect 选择的信息，如功能科目
	 * @param idField 信息中的ID字段
	 * @param nameField　信息中的名称字段
	 * @param sumField　分组加和的字段
	 * @param parentField　父亲字段
	 * @param sortField　排序字段名，兼CODE字段，
	 * codeField　要拼写的条件字段名，如　acct_code等
	 * @return
	 * @throws Exception
	 */
	public static SearchObj genHeaderByDepart(DataSet dsHeader,
			DataSet dsSelect, String idField, String nameField,
			String parentField, String sortField, String sumField,
			String codeField, int colStartIndex) throws Exception {
		SearchObj searchObj = new SearchObj();
		if (dsSelect == null || dsSelect.isEmpty())
			return null;
		dsSelect.beforeFirst();
		if (dsSelect.next()) {
			dsSelect.beforeFirst();
			if (dsSelect.next()) {
				dsSelect.insert();
				dsSelect.fieldByName(parentField).setValue("");
				dsSelect.fieldByName(idField).setValue("");
				dsSelect.fieldByName(nameField).setValue(TOTAL_TITLE);
				dsSelect.fieldByName(sortField).setValue("0000");
				dsSelect.fieldByName(codeField).setValue("0000");
			}
		}
		
		//先生成树状
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsSelect, idField, nameField, parentField,
				sortField);
		//遍历并生成临时的字段
		int iCount = node.getChildrenCount();

		List lstLeaf = new ArrayList();

//		for (int i = 0; i < iCount; i++) {
//
//			treatNode("", i + colStartIndex, node.getChildAt(i), dsHeader,
//					lstLeaf);
//		}
		searchObj.setLastIndex(iCount + colStartIndex);
		//根据叶节点生成字段
		iCount = lstLeaf.size();
		StringBuffer sbField = new StringBuffer();
		StringBuffer sbSumField = new StringBuffer();
		StringBuffer sbShowField = new StringBuffer();
		StringBuffer sbSubTotal = new StringBuffer("sum(");
		String strSubTotal = "";
		for (int i = 0; i < iCount; i++) {
			Node aNode = (Node) lstLeaf.get(i);
			//将级次作为参数放入节点中，以便生成字段
			String fieldName = PRE + aNode.getAttribute("lvl_ID");
			String code = (String) aNode.getSortByValue();

			if (!TOTAL_TITLE.equals(aNode.getText())) {
				// 拼写CASE
				sbSumField
						.append("sum(")
						.append(" case when ")
						.append(codeField)
						.append(
								" in(select div_code from FB_U_DEPTODIV where dep_code = '")
						.append(code).append("') then ").append(sumField)
						.append(" else 0 end) as ").append(fieldName).append(
								",");
				sbField.append(fieldName).append(",");
				sbShowField.append("sum(").append(fieldName).append(") as ")
						.append(fieldName).append(",");
				sbSubTotal.append(fieldName).append("+");
			} else {
				strSubTotal = fieldName;
			}			
		}

		dsHeader.applyUpdate();
		searchObj.setDsHeader(dsHeader);
		searchObj.setFieldExp(sbField.substring(0, sbField.length() - 1));
		searchObj.setSumExp(sbSumField.substring(0, sbSumField.length() - 1));
		searchObj.setShowFieldExp(sbShowField.substring(0, sbShowField.length() - 1));
		searchObj.setSubTotalExp(sbSubTotal.substring(0, sbSubTotal.length() - 1) + ") as " + strSubTotal);
		searchObj.setSubTotalField(strSubTotal);
		return searchObj;
	}

	//intArr为叶节点的遍数
//	public static void treatNode(String parentLvl, int childIndex, Node node,
//			DataSet dsHeader, List lstLeaf) throws Exception {
//		int iChildCount = node.getChildrenCount();
//		String lvl = parentLvl
//				+ BaseUtil.stufCharPre("" + (childIndex + 1), 4, '0');
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue(lvl);
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue(
//				node.getText());
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("否");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(100));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue(
//				"#,###.##");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("浮点型");
//
//		if (iChildCount == 0) {
//			node.setAttribute("lvl_ID", lvl);
//			lstLeaf.add(node);
//			dsHeader.fieldByName(IBasInputTable.FIELD_ENAME)
//					.setValue(PRE + lvl);
//			dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("是");
//
//		} else {
//			for (int i = 0; i < iChildCount; i++) {
//				treatNode(lvl, i, node.getChildAt(i), dsHeader, lstLeaf);
//			}
//		}
//
//	}

//	public static void createDefaultHeader(DataSet dsHeader) throws Exception {
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue("0000");
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("编码");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("是");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(60));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("字符型");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue("code");
//
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue("0001");
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("名称");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("是");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(170));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("字符型");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue("name");
//
//	}

}
