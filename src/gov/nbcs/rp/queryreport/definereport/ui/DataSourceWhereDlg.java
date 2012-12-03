/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * <p>
 * Title:����Դ��������ֵѡ��Ի���
 * </p>
 * <p>
 * Description:����Դ��������ֵѡ��Ի���
 * </p>
 * <p>

 */
public class DataSourceWhereDlg extends FDialog {

	private static final long serialVersionUID = 1L;

	/* ����ֵ�� */
	private CustomTree ftreWhereValue = null;

	private Map mapWhereValue = null;

	private String resultValue = null;

	private String whereValue;

	public DataSourceWhereDlg(DataSourceSet dataSourceSet, Map mapWhereValue,
			String whereValue) {
		super(dataSourceSet);
		this.mapWhereValue = mapWhereValue;
		this.whereValue = whereValue;
		this.setSize(500, 375);
		this.setTitle("ѡ��");
		this.setModal(true);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
					"��ʾ���淢�����󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void init() throws Exception {
		ftreWhereValue = new CustomTree("����", null, "code", "name", null, null,
				"code", true);
		ftreWhereValue.setIsCheckBoxEnabled(true);
		FScrollPane fspnlWhereValue = new FScrollPane(ftreWhereValue);
		// ���塰ȷ������ť
		FButton okBtn = new FButton("okBtn", "ȷ��");
		okBtn.addActionListener(new OkActionListener());
		// ���塱ȡ������ť
		FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
		// ʵ�֡�ȡ������ť����¼�
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				DataSourceWhereDlg.this.setVisible(false);
			}
		});

		// ���尴ť���
		FFlowLayoutPanel btnFpnl = new FFlowLayoutPanel();
		// ���ÿ�����ʾ
		btnFpnl.setAlignment(FlowLayout.RIGHT);
		// ��ȷ������ť���밴ť���
		btnFpnl.addControl(okBtn);
		// ��ȡ������ť���밴ť���
		btnFpnl.addControl(cancelBtn);

		// ��������弰����
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRlay = new RowPreferedLayout(1);
		mainRlay.setRowHeight(30);
		mainPnl.setLayout(mainRlay);
		// ����Դ�����������
		mainPnl.add(fspnlWhereValue, new TableConstraints(1, 1, true, true));
		// ��ť�����������
		mainPnl.add(btnFpnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// ��ʾ����Ϣ
		showTreeInfo();
		// �������ڵ�Ϊѡ��״̬
		if (!Common.isNullStr(whereValue) && mapWhereValue != null) {
			boolean isDiffer = SubFieldSetDialog.isDiffer(mapWhereValue);
			if (isDiffer) {
				SubFieldSetDialog.setNodeSelect(this.getLvlWithPri(whereValue),
						ftreWhereValue);
			} else {
				SubFieldSetDialog.setNodeSelect(whereValue, ftreWhereValue);
			}
		}
	}

	/**
	 * ����ֵ�Ͳ����ֵ��ͬʱ����������ֵ�ò����ֵ(����ֵ�ַ�����:021,022,023)
	 * 
	 * @param sPriValue
	 * @return
	 * @throws Exception
	 */
	private String getLvlWithPri(String sPriValue) throws Exception {
		String sLvlValue = "";
		String[] priArr = sPriValue.split(",");
		int len = priArr.length;
		for (int i = 0; i < len; i++) {
			DataSet dsEnum = (DataSet) mapWhereValue
					.get(IDefineReport.ENUM_DATA);
			String sLvlField = mapWhereValue.get(ISysRefCol.LVL_FIELD)
					.toString();
			String sPriField = mapWhereValue.get(ISysRefCol.PRIMARY_FIELD)
					.toString();
			if (dsEnum.locate(sPriField, priArr[i])) {
				sLvlValue = DefinePubOther.addComma(sLvlValue);
				sLvlValue += dsEnum.fieldByName(sLvlField).getString();
			}
		}
		return sLvlValue;
	}

	/**
	 * ��ʾ����Ϣ
	 * 
	 */
	private void showTreeInfo() {

		if (mapWhereValue == null)
			return;
		String sLvlField = mapWhereValue.get(ISysRefCol.LVL_FIELD).toString();
		// refcol_name
		String sRefcolName = mapWhereValue.get(ISysRefCol.REFCOL_NAME)
				.toString();
		// lvl_style
		String sLvlStyle = mapWhereValue.get(ISysRefCol.LVL_STYLE).toString();
		// ö��ԴDataSet
		DataSet dsEnum = (DataSet) mapWhereValue.get(IDefineReport.ENUM_DATA);

		// ���ø�����
		ftreWhereValue.setRootName(sRefcolName);
		// ����DataSet
		ftreWhereValue.setDataSet(dsEnum);
		// ����IdName
		ftreWhereValue.setIdName(sLvlField);
		// ���ñ������
		ftreWhereValue.setCodeRule(SysCodeRule.createClient(sLvlStyle));
		// ����SortKey
		ftreWhereValue.setSortKey(sLvlField);
		try {
			ftreWhereValue.reset();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
					"��ѯ���ݳ��ִ���,������Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * ȷ����ť����¼�
	 * 
	 */
	private class OkActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String priField = mapWhereValue.get(ISysRefCol.PRIMARY_FIELD)
					.toString();
			boolean isDiffer = SubFieldSetDialog.isDiffer(mapWhereValue);

			try {
				String filterValue = "";
				List lstValue = getWhere(ftreWhereValue, isDiffer, priField);
				if (lstValue == null) {
					JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
							"��ѡ������ֵ!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				for (Iterator itValue = lstValue.iterator(); itValue.hasNext();) {
					String value = (String) itValue.next();
					filterValue = DefinePubOther.addComma(filterValue);
					filterValue += value;
				}
				resultValue = filterValue;
				DataSourceWhereDlg.this.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
						"�õ�����ֵ�������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * �������ڵ㣬�õ���ѯ����
	 * 
	 * @param customTree��
	 * @param listCode�����б�
	 * @param listName�����б�
	 * @param isDiffer�����Ͳ�����Ƿ�ͬһ�ֶΣ���ͬtrue,��ͬfalse
	 * @param priField�����ֶ�
	 * @throws Exception
	 */
	private List getWhere(CustomTree customTree, boolean isDiffer,
			String priField) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// �жϸ��ڵ��Ƿ�ѡ��
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		List lstValue = new ArrayList();

		getSelectDiv(customTree.getDataSet(), myTreeNode, lstValue, isDiffer,
				priField);
		return lstValue;
	}

	/**
	 * �õ�ѡ�нڵ���룬ʹ���˵ݹ�
	 * 
	 * @param myTreeNode���ڵ�
	 * @param lstCode�����б�
	 * @param lstName�����б�
	 * @param isDiffer�����Ͳ�����Ƿ�ͬһ�ֶΣ���ͬtrue,��ͬfalse
	 * @param priField�����ֶ�
	 * @throws Exception
	 * @throws Exception
	 */
	private void getSelectDiv(DataSet ds, MyTreeNode myTreeNode, List lstValue,
			boolean isDiffer, String priField) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				// �����Ͳ�����Ƿ�ͬһ�ֶ�
				if (isDiffer) {// ��ͬ
					lstValue.add(getLeafValue(ds, myTreeNodeTmp, priField));
				} else {// ��ͬ
					lstValue.add(myPfNode.getValue());
				}
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDiv(ds, myTreeNodeTmp, lstValue, isDiffer, priField);
			}
		}
	}

	/**
	 * �õ��ڵ�ֵ
	 * 
	 * @param ds���ݼ�
	 * @param node�ڵ�
	 * @param priField�����ֶ�
	 * @return
	 * @throws Exception
	 */
	private String getLeafValue(DataSet ds, MyTreeNode node, String priField)
			throws Exception {
		String curBookmark = ds.toogleBookmark();
		// �ж��Ƿ�Ҷ�ڵ�
		if (node.isLeaf()) {
			ds.gotoBookmark(node.getBookmark());
			return ds.fieldByName(priField).getString();
		}

		StringBuffer sFileter = new StringBuffer();
		MyTreeNode myTreeNodeTmp;
		Enumeration enume = node.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			myTreeNodeTmp = (MyTreeNode) enume.nextElement();
			if (!myTreeNodeTmp.isLeaf())
				continue;
			ds.gotoBookmark(myTreeNodeTmp.getBookmark());
			if (!Common.isNullStr(sFileter.toString())) {
				sFileter.append(",");
			}
			sFileter.append(ds.fieldByName(priField).getString());
		}

		ds.gotoBookmark(curBookmark);
		return sFileter.toString();
	}

	public String getResultValue() {
		return resultValue;
	}

}
