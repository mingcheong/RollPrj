/**
 * @# FieldProFactory.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.fieldpro;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;

/**
 * ����˵��:������
1.4.2
 */
public class FieldProFactory {

	public static final String PRE = "ff";
	
	public static final String TOTAL_TITLE="��С�ơ�"; 

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
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("�ܼ�");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue(PRE + "0000");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(120));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue(
//				"#,###.##");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("������");
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
	 * ���ɱ�ͷ�ֶΣ���ѯ������Ϣ��
	 * 
	 * @param dsSelect
	 *            ѡ�����Ϣ���繦�ܿ�Ŀ
	 * @param idField
	 *            ��Ϣ�е�ID�ֶ�
	 * @param nameField
	 *            ��Ϣ�е������ֶ�
	 * @param sumField
	 *            ����Ӻ͵��ֶ�
	 * @param parentField
	 *            �����ֶ�
	 * @param sortField
	 *            �����ֶ�������CODE�ֶΣ� codeField Ҫƴд�������ֶ������� acct_code��
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
		// ���⴦���ʽ���Դ
		dsSelect.beforeFirst();
		if (dsSelect.next()) {
			dsSelect.insert();
			dsSelect.fieldByName(parentField).setValue("");
			dsSelect.fieldByName(idField).setValue("");
			dsSelect.fieldByName(nameField).setValue(TOTAL_TITLE);
			dsSelect.fieldByName(sortField).setValue("0000");
			dsSelect.fieldByName(codeField).setValue("0000");
		}
		
		//��������״
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsSelect, idField, nameField, parentField,
				sortField);
		//������������ʱ���ֶ�
		int iCount = node.getChildrenCount();

		List lstLeaf = new ArrayList();

//		for (int i = 0; i < iCount; i++) {
//			treatNode("", i + colStartIndex, node.getChildAt(i), dsHeader,
//					lstLeaf);
//		}
		searchObj.setLastIndex(iCount + colStartIndex);
		//����Ҷ�ڵ������ֶ�
		iCount = lstLeaf.size();
		StringBuffer sbField = new StringBuffer();
		StringBuffer sbSumField = new StringBuffer();
		StringBuffer sbShowField = new StringBuffer();
		StringBuffer sbSubTotal = new StringBuffer("sum(");
		String strSubTotal = "";
		for (int i = 0; i < iCount; i++) {
			Node aNode = (Node) lstLeaf.get(i);
			//��������Ϊ��������ڵ��У��Ա������ֶ�
			String fieldName = PRE + aNode.getAttribute("lvl_ID");
			String code = (String) aNode.getSortByValue();

			if (!TOTAL_TITLE.equals(aNode.getText())) {
				// ƴдCASE
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
	 * ���ɱ�ͷ�ֶΣ���ѯ������Ϣ��
	 * @param dsSelect ѡ�����Ϣ���繦�ܿ�Ŀ
	 * @param idField ��Ϣ�е�ID�ֶ�
	 * @param nameField����Ϣ�е������ֶ�
	 * @param sumField������Ӻ͵��ֶ�
	 * @param parentField�������ֶ�
	 * @param sortField�������ֶ�������CODE�ֶΣ�
	 * codeField��Ҫƴд�������ֶ������硡acct_code��
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
		
		//��������״
		HierarchyListGenerator hg = HierarchyListGenerator.getInstance();
		Node node = hg.generate(dsSelect, idField, nameField, parentField,
				sortField);
		//������������ʱ���ֶ�
		int iCount = node.getChildrenCount();

		List lstLeaf = new ArrayList();

//		for (int i = 0; i < iCount; i++) {
//
//			treatNode("", i + colStartIndex, node.getChildAt(i), dsHeader,
//					lstLeaf);
//		}
		searchObj.setLastIndex(iCount + colStartIndex);
		//����Ҷ�ڵ������ֶ�
		iCount = lstLeaf.size();
		StringBuffer sbField = new StringBuffer();
		StringBuffer sbSumField = new StringBuffer();
		StringBuffer sbShowField = new StringBuffer();
		StringBuffer sbSubTotal = new StringBuffer("sum(");
		String strSubTotal = "";
		for (int i = 0; i < iCount; i++) {
			Node aNode = (Node) lstLeaf.get(i);
			//��������Ϊ��������ڵ��У��Ա������ֶ�
			String fieldName = PRE + aNode.getAttribute("lvl_ID");
			String code = (String) aNode.getSortByValue();

			if (!TOTAL_TITLE.equals(aNode.getText())) {
				// ƴдCASE
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

	//intArrΪҶ�ڵ�ı���
//	public static void treatNode(String parentLvl, int childIndex, Node node,
//			DataSet dsHeader, List lstLeaf) throws Exception {
//		int iChildCount = node.getChildrenCount();
//		String lvl = parentLvl
//				+ BaseUtil.stufCharPre("" + (childIndex + 1), 4, '0');
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue(lvl);
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue(
//				node.getText());
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(100));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue(
//				"#,###.##");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("������");
//
//		if (iChildCount == 0) {
//			node.setAttribute("lvl_ID", lvl);
//			lstLeaf.add(node);
//			dsHeader.fieldByName(IBasInputTable.FIELD_ENAME)
//					.setValue(PRE + lvl);
//			dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
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
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("����");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(60));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("�ַ���");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue("code");
//
//		dsHeader.append();
//		dsHeader.fieldByName(IBasInputTable.LVL_ID).setValue("0001");
//		dsHeader.fieldByName(IBasInputTable.FIELD_CNAME).setValue("����");
//		dsHeader.fieldByName(IBasInputTable.IS_LEAF).setValue("��");
//		dsHeader.fieldByName(IBasInputTable.FIELD_COLUMN_WIDTH).setValue(
//				new Integer(170));
//		dsHeader.fieldByName(IBasInputTable.FIELD_DISFORMAT).setValue("");
//		dsHeader.fieldByName(IBasInputTable.FIELD_TYPE).setValue("�ַ���");
//		dsHeader.fieldByName(IBasInputTable.FIELD_ENAME).setValue("name");
//
//	}

}
