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
 * 资金来源对应收入主界面
 * 

 * 
 */
public class PFSToIncItem extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	// 支出资金来源
	DataSet dsPayOutFS = null;

	// 收入项目数据集
	DataSet dsIncItem = null;

	// 支出资金来源对收入项目数据集
	DataSet dsPfsToIncitem = null;

	// 支出资金来源树
	CustomTree ftreePayOutFS = null;

	// 收入项目
	CustomTree ftreeIncItem = null;

	String sBookmark = null;

	// 收入项目Panel
	FPanel fnlIncItem = null;

	// 数据库接口
	ISysIaeStru sysIaeStruServ;

	public PFSToIncItem() {

	}

	public void initize() {
		try {
			// 定义数据库接口
			sysIaeStruServ = SysIaeStruI.getMethod();
			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setResizeWeight(0.2);
			this.add(fSplitPane);

			// 建左边信息
			FPanel LeftPanel = new FPanel();
			fSplitPane.addControl(LeftPanel);
			LeftPanel.setLayout(new RowPreferedLayout(1));
			// 支出资金来源Label
			FLabel flblPfs = new FLabel();
			flblPfs.setTitle("支出资金来源");
			flblPfs.setHorizontalAlignment(SwingConstants.CENTER);
			// 建支出资金来源树
			dsPayOutFS = sysIaeStruServ.getPayOutFSTre(Integer
					.parseInt(Global.loginYear));
			ftreePayOutFS = new CustomTree(IPayOutFS.PFS_ROOT, dsPayOutFS,
					IPayOutFS.LVL_ID, ISysIaeStru.NAME, IPayOutFS.PAR_ID, null);
			ftreePayOutFS.expandAll();
			FScrollPane fsPanePfs = new FScrollPane();
			fsPanePfs.addControl(ftreePayOutFS);

			LeftPanel.addControl(flblPfs, new TableConstraints(1, 1, false));
			LeftPanel.addControl(fsPanePfs, new TableConstraints(1, 1, true));

			// 收入项目Panel
			fnlIncItem = new FPanel();
			fSplitPane.addControl(fnlIncItem);
			fnlIncItem.setLayout(new RowPreferedLayout(1));
			FLabel flblIncItem = new FLabel();
			flblIncItem.setTitle("对应收入项目");
			flblIncItem.setHorizontalAlignment(SwingConstants.CENTER);
			dsIncItem = sysIaeStruServ.getIncTypeTre(Integer
					.parseInt(Global.loginYear));
			ftreeIncItem = new CustomTree("收入项目", dsIncItem, IIncType.LVL_ID,
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
			// 设编辑区控件不可编辑
			Common.changeChildControlsEditMode(fnlIncItem, false);
			this.createToolBar();
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsPayOutFS,
					this, ftreePayOutFS);
			setActionStatus.setState(false);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示资金来源对收入界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "取消资金来源对收入编辑发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "修改资金来源对收入信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
		try {
			PFSToIncItemSave pfsToIncItemSave = new PFSToIncItemSave(this);
			pfsToIncItemSave.save();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "保存资金来源对收入信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
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
			// 判断checkbox框是否可以编缉
			if (!tree.getIsCheckBoxEnabled())
				return;

			int row = tree.getRowForLocation(evt.getX(), evt.getY());
			if (row < 0) {
				return;
			}

			// 得到选中的节点
			TreePath path = tree.getPathForRow(row);
			if (path == null) {
				return;
			}
			MyTreeNode node = (MyTreeNode) path.getLastPathComponent();
			if (node != null) {
				MyPfNode myPfNode = (MyPfNode) node.getUserObject();
				int state = myPfNode.getSelectStat();

				// 判断是否未选中状态（三态）
				if (state == MyPfNode.SELECT)
					return;
			}

			try {
				// 判断支出资金来源是否是录入
				if (dsPayOutFS != null && !dsPayOutFS.isEmpty()) {
					if (dsPayOutFS.fieldByName(IPayOutFS.DATA_SOURCE)
							.getInteger() == 0) {
						// 判断选中的节点是否已设置从其他资金来源取数
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
									// 判断是收入栏目的对应关系是否本资金来源
									if (pfsCode.equals(pfsCodeTmp)) {
										continue;
									}
									JOptionPane.showMessageDialog(
											PFSToIncItem.this,
											"该收入栏目已设置从其他支出资金来源中取数，请选择其他收入栏目!",
											"提示",
											JOptionPane.INFORMATION_MESSAGE);
									return;
								}
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(PFSToIncItem.this,
							"请先设置支出资金来源!", "提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(PFSToIncItem.this, "勾选生错误，错误信息："
						+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
