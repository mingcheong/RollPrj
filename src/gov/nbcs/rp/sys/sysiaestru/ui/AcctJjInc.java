package gov.nbcs.rp.sys.sysiaestru.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.StatefulData;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.sys.sysiaestru.ibs.IPayOutType;
import gov.nbcs.rp.sys.sysiaestru.ibs.ISysIaeStru;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTitledPanel;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.Global;

/**
 * <p>
 * Title:收支科目挂接明细客户端主界面
 * 
 * </p>
 * <p>
 * Description:收支科目挂接明细客户端主界面
 * 

 */
public class AcctJjInc extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	FTabbedPane ftabPnlAcctjjInc;

	CustomTree ftreeAcctJJ;// 经济科目

	CustomTree ftreeIncAcctitem; // 收入预算科目

	CustomTree ftreIncomeSubItem;// 收费项目

	DataSet dsAcctJJ;// 经济科目

	DataSet dsAcctitem;// 收入预算科目

	DataSet dsIncome;// 收费项目

	FPanel rightPanel;

	String sSaveType;// 修改类型

	FPanel fpnlIncomeSumItem; // 收费项目panel

	FScrollPane fsPnlIncomeSubItem;// 收费项目

	FPanel fpnlSubType;

	FRadioGroup frdoSubTypeJJ;// 当前科目子科目类型（经济科目）

	FRadioGroup frdoSubTypeInc;// 当前科目子科目类型(收入预算科目）

	String sBSI_ID; // 经济科目ID

	String sIN_BS_ID; // 收入预算科目ID

	// 连接数据库接口
	ISysIaeStru sysIaeStruServ = null;

	IPubInterface iPubInterface = null;

	public AcctJjInc() {
	}

	public void initize() {
		try {
			// 定义数据库接口
			sysIaeStruServ = SysIaeStruI.getMethod();
			// 公共方法数据接口
			iPubInterface = PubInterfaceStub.getMethod();
			// 设置分栏
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(260);
			this.add(fSplitPane);

			// 建左边信息
			ftabPnlAcctjjInc = new FTabbedPane();
			fSplitPane.addControl(ftabPnlAcctjjInc);

			FTitledPanel ftitPnlAcctj = new FTitledPanel();
			FTitledPanel ftitPnlInc = new FTitledPanel();
			ftabPnlAcctjjInc.addControl("收入预算科目", ftitPnlInc);
			// ftabPnlAcctjjInc.addControl("支出经济科目", ftitPnlAcctj);
			// 支出经济科目
			ftitPnlAcctj.setLayout(new RowPreferedLayout(1));
			FScrollPane fsPaneAcctjj = new FScrollPane();
			ftitPnlAcctj.addControl(fsPaneAcctjj, new TableConstraints(1, 1,
					true, true));

			ftreeAcctJJ = new CustomTree(IPayOutType.ACCTITEM_JJ_ROOT, null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null);
			ftreeAcctJJ.setSortKey(IPubInterface.ACCT_CODE_JJ);
			ftreeAcctJJ.expandAll();
			fsPaneAcctjj.addControl(ftreeAcctJJ);

			// 收入预算科目
			ftitPnlInc.setLayout(new RowPreferedLayout(1));
			FScrollPane fsPaneIncCol = new FScrollPane();
			ftitPnlInc.addControl(fsPaneIncCol, new TableConstraints(1, 1,
					true, true));
			// 收入预算科目树
//			ftreeIncAcctitem = new CustomTree(IIncType.ACCTITEM_INC_ROOT, null,
//					IPubInterface.IN_BS_ID, IPubInterface.ACCT_JJ_FNAME,
//					IPubInterface.IN_BS_PARENT_ID, null);
//			ftreeIncAcctitem.setSortKey(IPubInterface.ACCT_CODE_INC);
			ftreeIncAcctitem.expandAll();
			fsPaneIncCol.addControl(ftreeIncAcctitem);

			// 建右边详细信息
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(350);
			rightPanel.setLayout(rLayout);
			// 右边编辑区的详细信息
			{
				FLabel flblSubAtr = new FLabel();
				flblSubAtr.setTitle("科目属性");
				flblSubAtr.setHorizontalAlignment(SwingConstants.CENTER);
				// 科目类型
				fpnlSubType = new FPanel();
				fpnlSubType.setLayout(new RowPreferedLayout(1));
				fpnlSubType.setTitle(" 当前科目子科目类型 ");
				fpnlSubType.setFontSize(this.getFont().getSize());
				fpnlSubType.setFontName(this.getFont().getName());
				fpnlSubType.setTitledBorder();

				frdoSubTypeJJ = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoSubTypeJJ.setRefModel("0#无    +1#用户自增     ");
				frdoSubTypeInc = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoSubTypeInc.setRefModel("0#无+1#用户自增+2#固定选择");
				subTypeMouseListener(frdoSubTypeInc);
				if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
					fpnlSubType.addControl(frdoSubTypeInc,
							new TableConstraints(1, 1, true));
				} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {
					fpnlSubType.addControl(frdoSubTypeJJ, new TableConstraints(
							1, 1, true));
				}
				// 收费项目
				fpnlIncomeSumItem = new FPanel();
				fpnlIncomeSumItem.setLayout(new RowPreferedLayout(1));
				fpnlIncomeSumItem.setFontSize(this.getFont().getSize());
				fpnlIncomeSumItem.setFontName(this.getFont().getName());
				fpnlIncomeSumItem.setTitle("子项目固定选择列表");
				fsPnlIncomeSubItem = new IncomeSubItem();
				fpnlIncomeSumItem.addControl(fsPnlIncomeSubItem,
						new TableConstraints(1, 1, true, true));

				rightPanel.addControl(flblSubAtr, new TableConstraints(1, 1,
						false, false));
				rightPanel.addControl(fpnlSubType, new TableConstraints(2, 1,
						false, false));
				rightPanel.addControl(fpnlIncomeSumItem, new TableConstraints(
						2, 1, true, false));
			}

			ftabPnlAcctjjInc.addChangeListener(new addChangeListener());
			// 设编辑区控件不可编辑
			Common.changeChildControlsEditMode(rightPanel, false);
			frdoSubTypeInc.setEditable(false);
			this.createToolBar();
			// 设置按钮状态
			SetActionStatus setActionStatus = new SetActionStatus(dsAcctJJ,
					this, ftreeAcctJJ);
			setActionStatus.setState(false, true);
			// 显示右边的页面信息
			if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
				fpnlIncomeSumItem.setVisible(true);
			} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {
				fpnlIncomeSumItem.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "显示收支科目挂明细界面发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void modulePanelActived() {
		if (dsAcctJJ != null)
			return;
		String sFilter = "set_year =" + Global.loginYear;
		try {
			// 经济科目
			dsAcctJJ = iPubInterface.getAcctEconomy(sFilter);
			ftreeAcctJJ.setDataSet(dsAcctJJ);
			ftreeAcctJJ.reset();
			// 收入预算科目
//			dsAcctitem = iPubInterface.getAcctInc(sFilter);
//			ftreeIncAcctitem.setDataSet(dsAcctitem);
//			ftreeIncAcctitem.reset();
//			// 收费项目
//			dsIncome = iPubInterface.getIncSubItem(sFilter);
//			ftreIncomeSubItem.setDataSet(dsIncome);
			ftreIncomeSubItem.reset();
			dsAcctJJ
					.addStateChangeListener(new AcctJjStateChangeListener(this));
			dsAcctitem.addStateChangeListener(new AcctIncStateChangeListener(
					this));
			ftreeAcctJJ
					.addTreeSelectionListener(new AcctJjTreeSelectionListener(
							this));
			ftreeIncAcctitem
					.addTreeSelectionListener(new AcctIncTreeSelectionListener(
							this));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "加载收支科目挂明细数据发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 收费项目面板
	 * 
	 * @author Administrator
	 * 
	 */
	public class IncomeSubItem extends FScrollPane {

		private static final long serialVersionUID = 1L;

		public IncomeSubItem() throws Exception {
			super();
//			ftreIncomeSubItem = new CustomTree("收费项目", null,
//					IPubInterface.IncSubItem_ID,
//					IPubInterface.IncSubItem_FNAME,
//					IPubInterface.IncSubItem_PARENT_ID, null);
//			ftreIncomeSubItem.setSortKey(IPubInterface.IncSubItem_Code);
//			ftreIncomeSubItem.setIsCheck(true);
			ftreIncomeSubItem.setIsCheckBoxEnabled(false);
			this.addControl(ftreIncomeSubItem);
		}
	}

	public class addChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			try {
				if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
					if ((dsAcctJJ.getState() & StatefulData.DS_BROWSE) != StatefulData.DS_BROWSE) {
						if (JOptionPane.showConfirmDialog(AcctJjInc.this,
								"确定不保存编辑后的信息??", "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							ftabPnlAcctjjInc.setSelectedIndex(0);
							return;
						} else {
							dsAcctJJ.cancel();
							AcctJjInc.this.isDataSaved = true;
							ftreeAcctJJ.expandTo(IPubInterface.BSI_ID, sBSI_ID);
						}
					}
				} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {

					if ((dsAcctitem.getState() & StatefulData.DS_BROWSE) != StatefulData.DS_BROWSE) {
						if (JOptionPane.showConfirmDialog(AcctJjInc.this,
								"确定不保存编辑后的信息??", "提示",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							ftabPnlAcctjjInc.setSelectedIndex(1);
							return;
						} else {
							dsAcctitem.cancel();
							AcctJjInc.this.isDataSaved = true;
//							ftreeIncAcctitem.expandTo(IPubInterface.IN_BS_ID,
//									sIN_BS_ID);
						}
					}
				}
				if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
					fpnlSubType.removeAll();
					fpnlSubType.setLayout(new RowPreferedLayout(1));
					fpnlSubType.addControl(frdoSubTypeInc,
							new TableConstraints(1, 1, true));
					fpnlIncomeSumItem.setVisible(true);
				} else {
					fpnlSubType.removeAll();
					fpnlSubType.setLayout(new RowPreferedLayout(1));
					fpnlSubType.addControl(frdoSubTypeJJ, new TableConstraints(
							1, 1, true));
					fpnlIncomeSumItem.setVisible(false);
				}
				fpnlSubType.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(AcctJjInc.this, "切换页面发生错误，错误信息："
						+ e1.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	public void doAdd() {
	}

	public void doDelete() {
	}

	public void doCancel() {
		try {
			if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
				dsAcctitem.cancel();
			} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {
				dsAcctJJ.cancel();
			}
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "取消编辑发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void doInsert() {

	}

	public void doModify() {
		try {
			AcctJjIncModify acctJjIncModify = new AcctJjIncModify(this);
			if (!acctJjIncModify.modify())
				return;
			this.isDataSaved = false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "修改信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
		try {
			AcctJjIncSave acctJjIncSave = new AcctJjIncSave(this);
			acctJjIncSave.save();
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "保存信息发生错误，错误信息："
					+ e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doClose() {
		((FFrame) Global.mainFrame).closeMenu();
	}

	private void subTypeMouseListener(FRadioGroup fRadioGroup) {
		JRadioButton radios[] = fRadioGroup.getRadios();
		for (int i = 0; i < radios.length; i++) {
			radios[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {
						AcctIncSetState acctIncSetState = new AcctIncSetState(
								AcctJjInc.this);
						acctIncSetState.setState();
					}
				}
			});
		}

	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
