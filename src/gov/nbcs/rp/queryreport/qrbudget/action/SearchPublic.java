package gov.nbcs.rp.queryreport.qrbudget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget;
import gov.nbcs.rp.queryreport.qrbudget.ibs.IQrBudget.DivObject;
import gov.nbcs.rp.queryreport.qrbudget.ui.QrBudget;
import com.foundercy.pf.control.MessageBox;

public class SearchPublic {
	QrBudget qrBudget;

	public SearchPublic(QrBudget qrBudget) {
		this.qrBudget = qrBudget;

	}

	/**
	 * �õ���λ��ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void getDivWhere(List lstDept, List lstDiv, int iUserType,
			CustomTree tree) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// �жϸ��ڵ��Ƿ�ѡ��
		myTreeNode = (MyTreeNode) tree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
			return;
		}
		if (iUserType == 1) {// �õ�ҵ���Ҳ�ѯ����
			int iChildCount = tree.getRoot().getChildCount();
			for (int i = 0; i < iChildCount; i++) {
				myTreeNode = (MyTreeNode) tree.getRoot().getChildAt(i);
				myPfNode = (MyPfNode) (myTreeNode).getUserObject();

				if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
					continue;
				if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
					lstDept.add(myPfNode.getValue());
					continue;
				}
				if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
					getSelectDiv(myTreeNode, lstDiv, tree);
				}
			}
		} else {// ��λ
			int iChildCount = tree.getRoot().getChildCount();
			for (int i = 0; i < iChildCount; i++) {
				myTreeNode = (MyTreeNode) tree.getRoot().getChildAt(i);
				getSelectDiv(myTreeNode, lstDiv, tree);
			}
		}
	}

	/**
	 * �õ���λ��ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public void getDivWhere(List lstDept, List lstDiv, int iUserType)
			throws Exception {
		getDivWhere(lstDept, lstDiv, iUserType, qrBudget.getFtreDivName());
	}

	/**
	 * �õ�ѡ�еĵ�λ��ʹ���˵ݹ�
	 * 
	 * @param myTreeNode
	 * @param lstDiv
	 * @throws Exception
	 */
	private static void getSelectDiv(MyTreeNode myTreeNode, List lstDiv,
			CustomTree tree) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		DataSet dsDivName = tree.getDataSet();
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				DivObject divObject = new DivObject();
				// �õ���λ����
				dsDivName.gotoBookmark(myTreeNodeTmp.getBookmark());
				divObject.sDivCode = dsDivName.fieldByName(IQrBudget.DIV_CODE)
						.getString();
				if (myPfNode.getIsLeaf()) {
					divObject.isLeaf = true;
				} else {
					divObject.isLeaf = false;
				}
				lstDiv.add(divObject);
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDiv(myTreeNodeTmp, lstDiv, tree);
			}
		}
	}

	/**
	 * �õ���ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public InfoPackage getFilter() throws Exception {
		InfoPackage infoPackage = new InfoPackage();
//		if (qrBudget.getFpnlToolBar().getCbxDataType().getSelectedIndex() < 0) {
//			infoPackage.setsMessage("��ѡ���������͡�");
//			infoPackage.setSuccess(false);
//			return infoPackage;
//		}

//		Map dataTypeInfo = qrBudget.getFpnlToolBar().getDataTypeInfo();
//		if (dataTypeInfo.get("BatchNO") == null) {
//			infoPackage.setsMessage("ȡ������,ȡ������ֵΪ�ա�");
//			infoPackage.setSuccess(false);
//			return infoPackage;
//		}
//		int iBatchNO = Integer.parseInt(dataTypeInfo.get("BatchNO").toString());
//		String sStatusWhere = " AND Batch_No= " + String.valueOf(iBatchNO);

//		if (dataTypeInfo.get("DataType") == null) {
//			infoPackage.setsMessage("ȡ������,ȡ����������ֵΪ�ա�");
//			infoPackage.setSuccess(false);
//			return infoPackage;
//		}
//		int iDataType = Integer.parseInt(dataTypeInfo.get("DataType")
//				.toString());
//		sStatusWhere = sStatusWhere + " AND Data_Type="
//				+ String.valueOf(iDataType);

//		infoPackage.setsMessage(sStatusWhere);
		infoPackage.setsMessage("");
		infoPackage.setSuccess(true);
		return infoPackage;
	}

	public void getDivNameWhere(List lstDivName, int iUserType)
			throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// �жϸ��ڵ��Ƿ�ѡ��
		myTreeNode = (MyTreeNode) qrBudget.getFtreDivName().getRoot();
		if (iUserType == 1) {// �õ�ҵ���Ҳ�ѯ����
			int iChildCount = qrBudget.getFtreDivName().getRoot()
					.getChildCount();

			// ����ȫѡ��ȫ��δѡ���
			myPfNode = (MyPfNode) (myTreeNode).getUserObject();
			if (myPfNode.getSelectStat() == MyPfNode.SELECT
					|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
				for (int i = 0; i < iChildCount; i++) {
					myTreeNode = (MyTreeNode) qrBudget.getFtreDivName()
							.getRoot().getChildAt(i);
					lstDivName.add(myTreeNode.getUserObject().toString());
				}
				return;
			}

			for (int i = 0; i < iChildCount; i++) {
				myTreeNode = (MyTreeNode) qrBudget.getFtreDivName().getRoot()
						.getChildAt(i);
				myPfNode = (MyPfNode) (myTreeNode).getUserObject();

				if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
					continue;
				if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
					lstDivName.add(myTreeNode.getUserObject().toString());
					continue;
				}
				if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
					getSelectDivName(myTreeNode, lstDivName);
				}
			}
		} else {// ��λ
			myTreeNode = (MyTreeNode) qrBudget.getFtreDivName().getRoot();
			myPfNode = (MyPfNode) (myTreeNode).getUserObject();

			int iChildCount = qrBudget.getFtreDivName().getRoot()
					.getChildCount();
			DataSet dsDivName = qrBudget.getFtreDivName().getDataSet();

			// ����ȫѡ��ȫ��δѡ���
			if (myPfNode.getSelectStat() == MyPfNode.SELECT
					|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
				for (int i = 0; i < iChildCount; i++) {
					myTreeNode = (MyTreeNode) qrBudget.getFtreDivName()
							.getRoot().getChildAt(i);
					dsDivName.gotoBookmark(myTreeNode.getBookmark());
					lstDivName.add(dsDivName
							.fieldByName(IPubInterface.DIV_NAME).getString());
				}
				return;
			}

			for (int i = 0; i < iChildCount; i++) {
				myTreeNode = (MyTreeNode) qrBudget.getFtreDivName().getRoot()
						.getChildAt(i);
				myPfNode = (MyPfNode) (myTreeNode).getUserObject();
				if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
					dsDivName.gotoBookmark(myTreeNode.getBookmark());
					lstDivName.add(dsDivName
							.fieldByName(IPubInterface.DIV_NAME).getString());
					continue;
				} else if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
					getSelectDivName(myTreeNode, lstDivName);
				}
			}
		}

	}

	private void getSelectDivName(MyTreeNode myTreeNode, List lstDivName)
			throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		DataSet dsDivName = qrBudget.getFtreDivName().getDataSet();
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				DivObject divObject = new DivObject();
				// �õ���λ����
				dsDivName.gotoBookmark(myTreeNodeTmp.getBookmark());
				divObject.sDivCode = dsDivName.fieldByName(IQrBudget.DIV_CODE)
						.getString();
				if (myPfNode.getIsLeaf()) {
					divObject.isLeaf = true;
				} else {
					divObject.isLeaf = false;
				}
				lstDivName.add(dsDivName.fieldByName(IPubInterface.DIV_NAME)
						.getString());
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDivName(myTreeNodeTmp, lstDivName);
			}
		}
	}

	public String getDivNameValue() throws Exception {
		List lstDivName = new ArrayList();
		getDivNameWhere(lstDivName, qrBudget.getIUserType());
		String sDivName = "";
		for (int i = 0; i < lstDivName.size(); i++) {
			if ("".equals(sDivName)) {
				sDivName = lstDivName.get(i).toString();
			} else if (i == lstDivName.size() - 1) {
				sDivName = sDivName + "��" + lstDivName.get(i);
			} else {
				sDivName = sDivName + "��" + lstDivName.get(i);
			}
		}
		return sDivName;
	}

	/**
	 * �õ���ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getBatchNo() throws Exception {
//		if (qrBudget.getFpnlToolBar().getCbxDataType().getSelectedIndex() < 0) {
//			new MessageBox("��ѡ����������!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
//					.show();
//			return -1;
//
//		}
//		Map dataTypeInfo = qrBudget.getFpnlToolBar().getDataTypeInfo();
//		return Integer.parseInt(dataTypeInfo.get("BatchNO").toString());
		return 0;

	}

	/**
	 * �õ���ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getDataType() throws Exception {
//		if (qrBudget.getFpnlToolBar().getCbxDataType().getSelectedIndex() < 0) {
//			new MessageBox("��ѡ����������!", MessageBox.MESSAGE, MessageBox.BUTTON_OK)
//					.show();
//			return -1;
//
//		}
//		Map dataTypeInfo = qrBudget.getFpnlToolBar().getDataTypeInfo();
		return 0;
//		return Integer.parseInt(dataTypeInfo.get("DataType").toString());

	}

	/**
	 * �õ�ѡ�е�λ������ɵ����
	 * 
	 * @param searchPublic
	 * @return
	 * @throws Exception
	 */
	public String getDivWhere() throws Exception {
		MyPfNode myPfNode = (MyPfNode) qrBudget.getFtreDivName().getRoot()
				.getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
			StringBuffer filter = new StringBuffer();
			// �õ���λ
			List lstDept = new ArrayList();
			List lstDiv = new ArrayList();
			getDivWhere(lstDept, lstDiv, qrBudget.getIUserType());
			int size = lstDept.size();
			for (int i = 0; i < size; i++) {
				if (!Common.isNullStr(filter.toString())) {
					filter.append(",");
				}
				filter.append(lstDept.get(i).toString());
			}
			size = lstDiv.size();
			for (int i = 0; i < size; i++) {
				if (!Common.isNullStr(filter.toString())) {
					filter.append(",");
				}
				filter.append(((DivObject) lstDiv.get(i)).sDivCode);
			}
			return filter.toString();
		} else {
			return "";
		}
	}
}
