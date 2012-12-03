/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncColumn;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */
public class IncTypeRelaTreMouseListener extends MouseAdapter {

	private IncType incType;

	/**
	 * 
	 */
	public IncTypeRelaTreMouseListener(IncType incType) {
		this.incType = incType;
	}

	public void mousePressed(MouseEvent mouseevent) {
		if (mouseevent.getButton() != 1
				|| !(mouseevent.getSource() instanceof CustomTree))
			return;
		CustomTree tree = (CustomTree) mouseevent.getSource();
		// �ж�checkbox���Ƿ���Ա༩
		if (!tree.getIsCheckBoxEnabled())
			return;

		int row = tree.getRowForLocation(mouseevent.getX(), mouseevent.getY());
		if (row < 0) {
			return;
		}

		// �õ�ѡ�еĽڵ�
		TreePath path = tree.getPathForRow(row);
		if (path == null) {
			return;
		}
		MyTreeNode node = (MyTreeNode) path.getLastPathComponent();

		if (node != null) {
			MyPfNode myPfNode = (MyPfNode) node.getUserObject();
			int state = myPfNode.getSelectStat();

			// �ж��Ƿ�δѡ��״̬����̬��
			if (state == MyPfNode.SELECT)
				return;

			node = incType.fpnlIncCol.ftreeIncColumn.getSelectedNode();
			if (node == null) {
				JOptionPane.showMessageDialog(incType,
						"��ѡ��������Ŀĩ�ڵ�,�ٹ�ѡ����Ԥ���Ŀ���շ���Ŀ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			state = ((MyPfNode) node.getUserObject()).getSelectStat();
			// �ж��Ƿ�δѡ��״̬����̬��
			if (node == null || state == MyPfNode.UNSELECT) {
				JOptionPane.showMessageDialog(incType,
						"���ȹ�ѡ��������Ŀĩ�ڵ�,�ٹ�ѡ����Ԥ���Ŀ���շ���Ŀ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (!node.isLeaf()) {
				JOptionPane.showMessageDialog(incType,
						"��ѡ��������Ŀĩ�ڵ�,��ĩ�ڵ㲻��������������Ԥ���Ŀ���շ���Ŀ��Ӧ��ϵ��", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != 1 || !(e.getSource() instanceof CustomTree))
			return;
		String tollFilter = getTollFilter();
		// �õ�������Ŀѡ�нڵ��code
		String inccolCode;
		try {
			DataSet dsIncCol = incType.fpnlIncCol.ftreeIncColumn.getDataSet();
			inccolCode = dsIncCol.fieldByName(IIncColumn.INCCOL_CODE)
					.getString();
			if (incType.dsInccolumnToInc == null) {
				incType.dsInccolumnToInc = DataSet.create();
			}
			if (!incType.dsInccolumnToInc.isEmpty()
					&& incType.dsInccolumnToInc.locate(IIncColumn.INCCOL_CODE,
							inccolCode)) {
				incType.dsInccolumnToInc.edit();
				incType.dsInccolumnToInc.fieldByName(IIncType.TOLL_FILTER)
						.setValue(tollFilter);
			} else {
				incType.dsInccolumnToInc.append();
				incType.dsInccolumnToInc.fieldByName(IIncColumn.INCCOL_CODE)
						.setValue(inccolCode);
				incType.dsInccolumnToInc.fieldByName(IIncType.TOLL_FILTER)
						.setValue(tollFilter);
			}
			incType.dsInccolumnToInc.applyUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(incType, "ѡ���Ӧ��ϵ�������󣬴�����Ϣ��"
					+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ��֯����Ԥ���Ŀ���շѵĲ�ѯ����
	 * 
	 * @return
	 */
	private String getTollFilter() {
		// ����Ԥ���Ŀ����
		String acctIncFilter = getTreeFilter(
				incType.fpnlChoiceRela.ftreeIncAcctitem,
				"");
		// �շ���Ŀ����
		String TollFilter = getTreeFilter(
				incType.fpnlChoiceRela.ftreIncomeSubItem, IIncType.TOLL_CODE);

		if (!Common.isNullStr(acctIncFilter) && Common.isNullStr(TollFilter)) {
			return acctIncFilter;
		} else if (Common.isNullStr(acctIncFilter)
				&& !Common.isNullStr(TollFilter)) {
			return TollFilter;
		}
		if (!Common.isNullStr(acctIncFilter) && !Common.isNullStr(TollFilter)) {
			return "(" + acctIncFilter + ") and (" + TollFilter.toString()
					+ ")";
		}
		return "";
	}

	/**
	 * ��֯���Ĳ�ѯ����
	 * 
	 * @param customTree
	 * @param fieldName
	 * @return
	 */
	private String getTreeFilter(CustomTree customTree, String fieldName) {
		MyTreeNode[] nodes = SysUntPub.getSelectNode(customTree);
		if (nodes == null)
			return "";
		StringBuffer sDivWhereisLeaf = new StringBuffer();
		StringBuffer sDivWhereNoLeaf = new StringBuffer();

		int len = nodes.length;
		for (int i = 0; i < len; i++) {
			MyTreeNode node = nodes[i];
			if (node.isLeaf()) { // Ҷ�ڵ�
				if ("".equals(sDivWhereisLeaf.toString())) {
					sDivWhereisLeaf.append("'" + node.sortKeyValue() + "'");
				} else {
					sDivWhereisLeaf.append(",'" + node.sortKeyValue() + "'");
				}
			} else { // ����Ҷ�ڵ�
				if ("".equals(sDivWhereNoLeaf.toString())) {
					sDivWhereNoLeaf.append(" " + fieldName + " like '"
							+ node.sortKeyValue() + "%'");
				} else {
					sDivWhereNoLeaf.append(" or " + fieldName + " like '"
							+ node.sortKeyValue() + "%'");
				}
			}
		}
		if (!"".equals(sDivWhereisLeaf.toString())) {
			// �ж�ĩ������һ�����Ƕ����һ��ʹ��"=",���ʹ��"in"
			if (StringUtils.indexOf(sDivWhereisLeaf.toString(), ",") == -1) {
				sDivWhereisLeaf.insert(0, " " + fieldName + " = ");
			} else {
				sDivWhereisLeaf.insert(0, " " + fieldName + " in (");
				sDivWhereisLeaf.append(")");
			}
		}

		if ("".equals(sDivWhereisLeaf.toString())
				&& !"".equals(sDivWhereNoLeaf.toString())) {
			return sDivWhereNoLeaf.toString();
		} else if (!"".equals(sDivWhereisLeaf.toString())
				&& "".equals(sDivWhereNoLeaf.toString())) {
			return sDivWhereisLeaf.toString();
		} else if (!"".equals(sDivWhereisLeaf.toString())
				&& !"".equals(sDivWhereNoLeaf.toString())) {
			return sDivWhereNoLeaf.toString() + " or "
					+ sDivWhereisLeaf.toString();
		}
		return "";
	}

}
