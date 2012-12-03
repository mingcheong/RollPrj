/*
 * $Id: SelectPrjDetailPanel.java,v 1.2 2010/06/08 12:33:13 xiexianglong Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */

package gov.nbcs.rp.sys.prjsort.ui;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.prjsort.ibs.IPrjDetail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;


/**
 * <p>
 * Title:��Ŀ��ϸ��ѡȡ��
 * </p>

 * </p>
 * 
 * @author Ǯ�Գ�
 * @version 1.2
 */

public class SelectPrjDetailPanel extends FDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ��Ŀ��ϸ��
	private CustomTree treePreDetail;

	// ��Ŀ��ϸ�б����ݼ�
	private DataSet ds;

	// ���ص���Ŀ��ϸ����
	private String[] bmkPrjDetail;

	// ������
	public SelectPrjDetailPanel(DataSet dsRec, DataSet dsDetail) {
		super(Global.mainFrame); // ָ�����常��
		try {
			ds = dsDetail;
			// ������Ŀ��ϸ��
			treePreDetail = new CustomTree("��Ŀ��ϸ", ds, IPrjDetail.DETAIL_ID,
					IPrjDetail.DETAIL_NAME, IPrjDetail.PAR_ID, null);
			treePreDetail.setIsCheckBoxEnabled(true);
			treePreDetail.setIsCheck(true);

			// ����TREE
			MyTreeNode root = (MyTreeNode) treePreDetail.getRoot(); // ���ڵ�
			Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
			while (enumeration.hasMoreElements()) { // ��ʼ����
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (node != root) { // ���˵����ڵ�
					String bk1 = node.getBookmark(); // ��ȡ�ýڵ��BOOKMARK
					ds.gotoBookmark(bk1);
					String sCode = ds.fieldByName(IPrjDetail.DETAIL_CODE)
							.getString();
					if (dsRec.isEmpty()) {
						PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
						pNode.setIsSelect(false); // ���øýڵ�Ϊѡȡ״̬
					}
					dsRec.beforeFirst();
					while (dsRec.next()) {
						if (sCode.equals(dsRec.fieldByName(
								IPrjDetail.DETAIL_CODE).getString())) { // ����ýڵ��봫�ݹ�����dataset�����е�ֵ��ȣ�������ѡȡ״̬
							PfTreeNode pNode = (PfTreeNode) node
									.getUserObject(); //
							pNode.setIsSelect(true); // ���øýڵ�Ϊѡȡ״̬
						} else {
							PfTreeNode pNode = (PfTreeNode) node
									.getUserObject(); //
							pNode.setIsSelect(false); // ���øýڵ�Ϊѡȡ״̬
						}
					}
				}
			}

			// ����Ԫ�ض���
			FPanel pnlBase = new FPanel();
			FPanel pnlUnder = new FPanel(); // ����Ű�ť�����
			FButton btnOK = new FButton();
			btnOK.setText("ȷ��");
			FButton btnCancel = new FButton();
			btnCancel.setText("ȡ��");
			pnlUnder.addControl(btnOK);
			pnlUnder.addControl(btnCancel);
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlBase.setLayout(layBase);
			FScrollPane pnlTree = new FScrollPane();
			pnlTree.addControl(treePreDetail);
			pnlBase.addControl(pnlTree,
					new TableConstraints(14, 1, false, true));

			pnlBase
					.addControl(pnlUnder,
							new TableConstraints(2, 1, true, true));
			this.setSize(300, 400); // ���ô����С
			this.setResizable(false); // ���ô����С�Ƿ�ɱ�
			this.getContentPane().add(pnlBase);
			this.dispose(); // ��������Զ�������
			this.setTitle("��Ŀ��ϸѡ�����"); // ���ô������
			this.setModal(true); // ���ô���ģ̬��ʾ

			// ȡ����ť�ļ����¼����رյ�ǰҳ��
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						setSelectDetailBMK();
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			// tree�ļ����¼���������ѡ��Ľڵ����Ϣ
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						setSelectDetailBMK();
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��ǰ����ϸ���ݼ��ı�ǩ
	 * @return
	 */
	public String[] getSelectDetailBMK() {
		return bmkPrjDetail;
	}

	private void setSelectDetailBMK() throws Exception {
		ds.beforeFirst();
		MyTreeNode[] nodes = treePreDetail.getSelectedNodes(true);
		MyTreeNode root = (MyTreeNode) treePreDetail.getRoot();
		bmkPrjDetail = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != root) { // ���˵����ڵ�
				// ѡ��ýڵ��detail_code
				// ,��sort_to_detail��Ƚ�,������,���¼sort_detail_code��bmk,��������
				bmkPrjDetail[i] = nodes[i].getBookmark();
			}
		}
	}
}