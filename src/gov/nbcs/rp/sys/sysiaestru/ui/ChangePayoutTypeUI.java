/**
 * 
 */
package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:���ÿ�Ŀ�ı�֧����Ŀ���ѡ��֧����Ŀ���Ի���
 * </p>
 * <p>
 * Description:���ÿ�Ŀ�ı�֧����Ŀ���ѡ��֧����Ŀ���Ի���
 * </p>
 * <p>

 */
public abstract class ChangePayoutTypeUI extends FDialog {

	private static final long serialVersionUID = 1L;

	/** ֧����Ŀ���dataset */
	protected DataSet dsPayoutKind;

	/** ֧����Ŀ����� */
	private CustomTree trePaypoutKind;

	/** ����ѡ�е�֧����Ŀ����¼ */
	private Map resultValue = null;

	public ChangePayoutTypeUI(DataSet dsPayoutKind) {

		super(Global.mainFrame);
		this.setSize(400, 360);
		this.setResizable(false);
		this.dispose();
		this.setModal(true);
		this.setTitle("ѡ��֧����Ŀ���");

		this.dsPayoutKind = (DataSet) dsPayoutKind.clone();
		try {
			// ��ʼ������
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame, "��ʾ���淢�����󣬴�����Ϣ:"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * ��ʼ������
	 * 
	 * @throws Exception
	 * 
	 */
	private void init() throws Exception {
		// FLabel flblTitle = new FLabel();
		// flblTitle.setTitle("ѡ��֧����Ŀ���");
		// flblTitle.setHorizontalAlignment(FLabel.CENTER);

		trePaypoutKind = new CustomTree(IPayOutType.PAYOUTKIND_ROOT,
				this.dsPayoutKind, IPayOutType.LVL_ID, ISysIaeStru.NAME,
				IPayOutType.PAR_ID, null);
		trePaypoutKind.setSortKey(IPayOutType.LVL_ID);
		trePaypoutKind.expandAll();
		FScrollPane fScroll = new FScrollPane(trePaypoutKind);
		FPanel fPanelReportCol = new FPanel();
		fPanelReportCol.setTopInset(5);
		fPanelReportCol.setLeftInset(5);
		fPanelReportCol.setRightInset(5);
		fPanelReportCol.setLayout(new RowPreferedLayout(1));
		fPanelReportCol.addControl(fScroll);

		// ��ťPanel
		FFlowLayoutPanel choosePanel = new FFlowLayoutPanel();
		choosePanel.setAlignment(FlowLayout.CENTER);
		FButton fbtnOk = new FButton("fbtnOk", "ȷ��");
		fbtnOk.addActionListener(new OkActionListener());

		FButton fbtnCancel = new FButton("fbtnCancel", "ȡ��");
		fbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChangePayoutTypeUI.this.dispose();
			}
		});
		choosePanel.addControl(fbtnOk);
		choosePanel.addControl(fbtnCancel);

		RowPreferedLayout rLay = new RowPreferedLayout(1);
		rLay.setRowHeight(15);
		this.getContentPane().setLayout(rLay);
		// this.getContentPane().add(flblTitle,
		// new TableConstraints(1, 1, false, true));
		this.getContentPane().add(fPanelReportCol,
				new TableConstraints(1, 1, true, true));
		this.getContentPane().add(choosePanel,
				new TableConstraints(2, 1, false, true));
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 */
	private class OkActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (!check()) {
				return;
			}
			try {
				resultValue = dsPayoutKind.getOriginData();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Global.mainFrame,
						"�ı�֧����Ŀ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
			ChangePayoutTypeUI.this.dispose();
		}
	}

	/**
	 * ����Ƿ��µ�֧����Ŀ�����ԭ֧����Ŀ����ϵ
	 * 
	 * @param payoutKindCode
	 */
	public abstract String checkChoice(String payoutKindCode);

	/**
	 * ���ѡ�е�֧����Ŀ���
	 * 
	 * @return
	 */
	private boolean check() {

		MyTreeNode node = trePaypoutKind.getSelectedNode();
		if (node == trePaypoutKind.getRoot() || !node.isLeaf()) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ĩ��֧����Ŀ���!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		try {
			// ���ݽڵ�bookmark��λDataSet��¼
			if (!dsPayoutKind.gotoBookmark(node.getBookmark())) {
				return false;
			}
			String payoutKindCode = dsPayoutKind.fieldByName(
					IPayOutType.PAYOUT_KIND_CODE).getString();
			// ����Ƿ��µ�֧����Ŀ�����ԭ֧����Ŀ����ϵ
			String strErr = checkChoice(payoutKindCode);
			if (!Common.isNullStr(strErr)) {
				JOptionPane.showMessageDialog(Global.mainFrame, strErr, "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Global.mainFrame,
					"�ı�֧����Ŀ��������󣬴�����Ϣ��" + e.getMessage(), "��ʾ",
					JOptionPane.ERROR_MESSAGE);
		}

		return true;
	}

	public Map getResultValue() {
		return resultValue;
	}

}
