package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.sysiaestru.ibs.IIncType;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutFS;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * �ʽ���Դ��Ӧ����������
 * 

 * 
 */
public class PFSToIncItem extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// ֧���ʽ���Դ
	DataSet dsPayOutFS = null;

	// ������Ŀ���ݼ�
	DataSet dsIncItem = null;

	// ֧���ʽ���Դ��������Ŀ���ݼ�
	DataSet dsPfsToIncitem = null;

	// ֧���ʽ���Դ��
	CustomTree ftreePayOutFS = null;

	// ������Ŀ
	CustomTree ftreeIncItem = null;

	String sBookmark = null;

	// ������ĿPanel
	FPanel fnlIncItem = null;

	// ���ݿ�ӿ�
	ISysIaeStru sysIaeStruServ;

	public PFSToIncItem() {

	}

	public void initize() {
		try {
			// �������ݿ�ӿ�
			sysIaeStruServ = SysIaeStruI.getMethod();
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setResizeWeight(0.2);
			this.add(fSplitPane);

			// �������Ϣ
			FPanel LeftPanel = new FPanel();
			fSplitPane.addControl(LeftPanel);
			LeftPanel.setLayout(new RowPreferedLayout(1));
			// ֧���ʽ���ԴLabel
			FLabel flblPfs = new FLabel();
			flblPfs.setTitle("֧���ʽ���Դ");
			flblPfs.setHorizontalAlignment(SwingConstants.CENTER);
			// ��֧���ʽ���Դ��
			dsPayOutFS = sysIaeStruServ.getPayOutFSTre(Integer
					.parseInt(Global.loginYear));
			ftreePayOutFS = new CustomTree(IPayOutFS.PFS_ROOT, dsPayOutFS,
					IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID, null);
			ftreePayOutFS.expandAll();
			FScrollPane fsPanePfs = new FScrollPane();
			fsPanePfs.addControl(ftreePayOutFS);

			LeftPanel.addControl(flblPfs, new TableConstraints(1, 1, false));
			LeftPanel.addControl(fsPanePfs, new TableConstraints(1, 1, true));

			// ������ĿPanel
			fnlIncItem = new FPanel();
			fSplitPane.addControl(fnlIncItem);
			fnlIncItem.setLayout(new RowPreferedLayout(1));
			FLabel flblIncItem = new FLabel();
			flblIncItem.setTitle("��Ӧ������Ŀ");
			flblIncItem.setHorizontalAlignment(SwingConstants.CENTER);
			dsIncItem = sysIaeStruServ.getIncTypeTre(Integer
					.parseInt(Global.loginYear));
			ftreeIncItem = new CustomTree("������Ŀ", dsIncItem, IIncType.LVL_ID,
					ISysIaeStru.NAME, IIncType.PAR_ID, null, IIncType.LVL_ID,
					true);
			ftreeIncItem.addMouseListener(new IncItemTreMouseListener());
			ftreeIncItem.setIsCheckBoxEnabled(true);
			ftreeIncItem.expandAll();
			FScrollPane fsPaneIncItem = new FScrollPane();
			fsPaneIncItem.addControl(ftreeIncItem);
			fnlIncItem.addControl(flblIncItem,
					new TableConstraints(1, 1, false));
			fnlIncItem.addControl(fsPaneIncItem, new TableConstraints(1, 1,
					true));

			dsPayOutFS
					.addStateChangeListener(new PFSToIncItemStateChangeListener(
							this));
			ftreePayOutFS
					.addTreeSelectionListener(new PFSToIncItemTreeSelectionListener(
							this));
			// ��༭���ؼ����ɱ༭
			Common.changeChildControlsEditMode(fnlIncItem, false);
			this.createToolBar();
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutFS,
					this, ftreePayOutFS);
			setActionStatus.setState(false);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ�ʽ���Դ��������淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doAdd() {

	}

	public void doDelete() {

	}

	public void doCancel() {
		try {
			dsPayOutFS.cancel();
			dsPayOutFS.gotoBookmark(sBookmark);
			ftreePayOutFS.expandTo("lvl_id", dsPayOutFS.fieldByName("lvl_id")
					.getString());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ȡ���ʽ���Դ������༭�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doInsert() {

	}

	public void doModify() {
		try {
			sBookmark = dsPayOutFS.toogleBookmark();
			PFSToIncItemModify pfsToIncItemModify = new PFSToIncItemModify(this);
			pfsToIncItemModify.modify();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�޸��ʽ���Դ��������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
		try {
			PFSToIncItemSave pfsToIncItemSave = new PFSToIncItemSave(this);
			pfsToIncItemSave.save();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "�����ʽ���Դ��������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	private class IncItemTreMouseListener extends MouseAdapter {

		public void mousePressed(MouseEvent evt) {
			if (evt.getButton() != 1
					|| !(evt.getSource() instanceof CustomTree))
				return;
			CustomTree tree = (CustomTree) evt.getSource();
			// �ж�checkbox���Ƿ���Ա༩
			if (!tree.getIsCheckBoxEnabled())
				return;

			int row = tree.getRowForLocation(evt.getX(), evt.getY());
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
			}

			try {
				// �ж�֧���ʽ���Դ�Ƿ���¼��
				if (dsPayOutFS != null && !dsPayOutFS.isEmpty()) {
					if (dsPayOutFS.fieldByName(IPayOutFS.DATA_SOURCE)
							.getInteger() == 0) {
						// �ж�ѡ�еĽڵ��Ƿ������ô������ʽ���Դȡ��
						if (dsIncItem.fieldByName(IIncType.IS_INC).getInteger() == 2) {
							String pfsCode = dsPayOutFS.fieldByName(
									IPayOutFS.PFS_CODE).getString();
							String incTypeCode = dsIncItem.fieldByName(
									IIncType.INCTYPE_CODE).getString();
							DataSet dsPfsToInc = sysIaeStruServ
									.getPfsToIncCode(incTypeCode,
											Global.loginYear);
							if (dsPfsToInc != null && !dsPfsToInc.isEmpty()) {
								dsPfsToInc.beforeFirst();
								while (dsPfsToInc.next()) {
									String pfsCodeTmp = dsPfsToInc.fieldByName(
											IPayOutFS.PFS_CODE).getString();
									// �ж���������Ŀ�Ķ�Ӧ��ϵ�Ƿ��ʽ���Դ
									if (pfsCode.equals(pfsCodeTmp)) {
										continue;
									}
									JOptionPane.showMessageDialog(
											PFSToIncItem.this,
											"��������Ŀ�����ô�����֧���ʽ���Դ��ȡ������ѡ������������Ŀ!",
											"��ʾ",
											JOptionPane.INFORMATION_MESSAGE);
									return;
								}
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(PFSToIncItem.this,
							"��������֧���ʽ���Դ!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(PFSToIncItem.this, "��ѡ�����󣬴�����Ϣ��"
						+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
