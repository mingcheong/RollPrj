package gov.nbcs.rp.sys.sysiaestru.ui;

import java.util.Iterator;
import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;

/**
 * �ʽ���Դ��Ӧ���뱣��

 * 
 */
public class PFSToIncItemSave {
	private PFSToIncItem pfsToIncItem;

	DataSet dsPayOutFS; // ֧���ʽ���Դ

	DataSet dsIncItem; // ������Ŀ���ݼ�

	CustomTree ftreePayOutFS;// ֧���ʽ���Դ��

	CustomTree ftreeIncItem;// ������Ŀ

	MyTreeNode nodeIncItem[]; // ������Ŀѡ������ڵ�

	ISysIaeStru sysIaeStruServ = SysIaeStruI.getMethod();

	public PFSToIncItemSave(PFSToIncItem pfsToIncItem) {
		this.pfsToIncItem = pfsToIncItem;
		this.dsPayOutFS = pfsToIncItem.dsPayOutFS;
		this.dsIncItem = pfsToIncItem.dsIncItem;
		this.ftreePayOutFS = pfsToIncItem.ftreePayOutFS;
		this.ftreeIncItem = pfsToIncItem.ftreeIncItem;

	}

	public void save() throws Exception {
		// ɾ��֧���ʽ���Դ��������Ŀ��Ӧ��ϵ
		if (!pfsToIncItem.dsPfsToIncitem.isEmpty()) {
			pfsToIncItem.dsPfsToIncitem.clearAll();
		}
		// ����֧���ʽ���Դ��������Ŀ��Ӧ��ϵ
		String sPfsCode = dsPayOutFS.fieldByName(IPayOutFS.PFS_CODE)
				.getString();
		String setYear = dsPayOutFS.fieldByName("SET_YEAR").getString();

		nodeIncItem = pfsToIncItem.ftreeIncItem.getSelectedNodes(true);
		for (int i = 0; i < nodeIncItem.length; i++) {
			dsIncItem.gotoBookmark(nodeIncItem[i].getBookmark());
			String sInctypeCode = dsIncItem.fieldByName(IIncType.INCTYPE_CODE)
					.getString();
			pfsToIncItem.dsPfsToIncitem.append();
			pfsToIncItem.dsPfsToIncitem.fieldByName(IPayOutFS.PFS_CODE)
					.setValue(sPfsCode);
			pfsToIncItem.dsPfsToIncitem.fieldByName(IIncType.INCTYPE_CODE)
					.setValue(sInctypeCode);
			pfsToIncItem.dsPfsToIncitem.fieldByName("SET_YEAR").setValue(
					setYear);
		}

		String sSqlIncTypeCode = getSqlDetValue(ftreeIncItem);
		dsPayOutFS.fieldByName("SQL_DET").setValue(sSqlIncTypeCode);

		if ("".equals(sSqlIncTypeCode))
			dsPayOutFS.fieldByName(IPayOutFS.INCCOL_ENAME).setValue("");
		else
			dsPayOutFS.fieldByName(IPayOutFS.INCCOL_ENAME)
					.setValue("INC_MONEY");
		pfsToIncItem.dsPfsToIncitem = sysIaeStruServ.savePfsToItem(setYear, "",
				sPfsCode, DataSetUtil
						.subDataSet(dsPayOutFS, DataSet.FOR_UPDATE),
				pfsToIncItem.dsPfsToIncitem);
		dsPayOutFS.applyUpdate();
	}

	/**
	 * ����������Ŀѡ�нڵ��������֯fb_iae_payout_fundsource����sql_detֵ
	 * 
	 * @param tree
	 * @return
	 * @throws Exception
	 */
	public static String getSqlDetValue(CustomTree tree) throws Exception {
		List lstCode = SysUntPub.getLeafNodeCode(tree, tree.getDataSet(),
				IIncType.INCTYPE_CODE);
		if (lstCode == null || lstCode.isEmpty())
			return "";
		String sSqlIncTypeCode = "";
		for (Iterator it = lstCode.iterator(); it.hasNext();) {
			String sInctypeCode = it.next().toString();
			if ("".equals(sSqlIncTypeCode) || sSqlIncTypeCode == null)
				sSqlIncTypeCode = "INCTYPE_CODE = '" + sInctypeCode + "'";
			else
				sSqlIncTypeCode = sSqlIncTypeCode + " OR INCTYPE_CODE = '"
						+ sInctypeCode + "'";
		}
		if (!"".equals(sSqlIncTypeCode)) {
			sSqlIncTypeCode = "(" + sSqlIncTypeCode + ")";
		}
		MyTreeNode myTreeNode = (MyTreeNode) tree.getRoot();
		MyPfNode myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
			return "1=1";
		} else {
			return sSqlIncTypeCode;
		}

	}
}
