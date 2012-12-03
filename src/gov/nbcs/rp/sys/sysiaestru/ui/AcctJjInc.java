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
 * Title:��֧��Ŀ�ҽ���ϸ�ͻ���������
 * 
 * </p>
 * <p>
 * Description:��֧��Ŀ�ҽ���ϸ�ͻ���������
 * 

 */
public class AcctJjInc extends FModulePanel implements ActionedUI {

	private static final long serialVersionUID = 1L;

	FTabbedPane ftabPnlAcctjjInc;

	CustomTree ftreeAcctJJ;// ���ÿ�Ŀ

	CustomTree ftreeIncAcctitem; // ����Ԥ���Ŀ

	CustomTree ftreIncomeSubItem;// �շ���Ŀ

	DataSet dsAcctJJ;// ���ÿ�Ŀ

	DataSet dsAcctitem;// ����Ԥ���Ŀ

	DataSet dsIncome;// �շ���Ŀ

	FPanel rightPanel;

	String sSaveType;// �޸�����

	FPanel fpnlIncomeSumItem; // �շ���Ŀpanel

	FScrollPane fsPnlIncomeSubItem;// �շ���Ŀ

	FPanel fpnlSubType;

	FRadioGroup frdoSubTypeJJ;// ��ǰ��Ŀ�ӿ�Ŀ���ͣ����ÿ�Ŀ��

	FRadioGroup frdoSubTypeInc;// ��ǰ��Ŀ�ӿ�Ŀ����(����Ԥ���Ŀ��

	String sBSI_ID; // ���ÿ�ĿID

	String sIN_BS_ID; // ����Ԥ���ĿID

	// �������ݿ�ӿ�
	ISysIaeStru sysIaeStruServ = null;

	IPubInterface iPubInterface = null;

	public AcctJjInc() {
	}

	public void initize() {
		try {
			// �������ݿ�ӿ�
			sysIaeStruServ = SysIaeStruI.getMethod();
			// �����������ݽӿ�
			iPubInterface = PubInterfaceStub.getMethod();
			// ���÷���
			FSplitPane fSplitPane = new FSplitPane();
			fSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			fSplitPane.setDividerLocation(260);
			this.add(fSplitPane);

			// �������Ϣ
			ftabPnlAcctjjInc = new FTabbedPane();
			fSplitPane.addControl(ftabPnlAcctjjInc);

			FTitledPanel ftitPnlAcctj = new FTitledPanel();
			FTitledPanel ftitPnlInc = new FTitledPanel();
			ftabPnlAcctjjInc.addControl("����Ԥ���Ŀ", ftitPnlInc);
			// ftabPnlAcctjjInc.addControl("֧�����ÿ�Ŀ", ftitPnlAcctj);
			// ֧�����ÿ�Ŀ
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

			// ����Ԥ���Ŀ
			ftitPnlInc.setLayout(new RowPreferedLayout(1));
			FScrollPane fsPaneIncCol = new FScrollPane();
			ftitPnlInc.addControl(fsPaneIncCol, new TableConstraints(1, 1,
					true, true));
			// ����Ԥ���Ŀ��
//			ftreeIncAcctitem = new CustomTree(IIncType.ACCTITEM_INC_ROOT, null,
//					IPubInterface.IN_BS_ID, IPubInterface.ACCT_JJ_FNAME,
//					IPubInterface.IN_BS_PARENT_ID, null);
//			ftreeIncAcctitem.setSortKey(IPubInterface.ACCT_CODE_INC);
			ftreeIncAcctitem.expandAll();
			fsPaneIncCol.addControl(ftreeIncAcctitem);

			// ���ұ���ϸ��Ϣ
			rightPanel = new FPanel();
			fSplitPane.addControl(rightPanel);
			rightPanel.setLeftInset(10);
			rightPanel.setTopInset(10);
			rightPanel.setRightInset(10);
			rightPanel.setBottomInset(10);
			RowPreferedLayout rLayout = new RowPreferedLayout(1);
			rLayout.setColumnWidth(350);
			rightPanel.setLayout(rLayout);
			// �ұ߱༭������ϸ��Ϣ
			{
				FLabel flblSubAtr = new FLabel();
				flblSubAtr.setTitle("��Ŀ����");
				flblSubAtr.setHorizontalAlignment(SwingConstants.CENTER);
				// ��Ŀ����
				fpnlSubType = new FPanel();
				fpnlSubType.setLayout(new RowPreferedLayout(1));
				fpnlSubType.setTitle(" ��ǰ��Ŀ�ӿ�Ŀ���� ");
				fpnlSubType.setFontSize(this.getFont().getSize());
				fpnlSubType.setFontName(this.getFont().getName());
				fpnlSubType.setTitledBorder();

				frdoSubTypeJJ = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoSubTypeJJ.setRefModel("0#��    +1#�û�����     ");
				frdoSubTypeInc = new FRadioGroup("", FRadioGroup.HORIZON);
				frdoSubTypeInc.setRefModel("0#��+1#�û�����+2#�̶�ѡ��");
				subTypeMouseListener(frdoSubTypeInc);
				if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
					fpnlSubType.addControl(frdoSubTypeInc,
							new TableConstraints(1, 1, true));
				} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {
					fpnlSubType.addControl(frdoSubTypeJJ, new TableConstraints(
							1, 1, true));
				}
				// �շ���Ŀ
				fpnlIncomeSumItem = new FPanel();
				fpnlIncomeSumItem.setLayout(new RowPreferedLayout(1));
				fpnlIncomeSumItem.setFontSize(this.getFont().getSize());
				fpnlIncomeSumItem.setFontName(this.getFont().getName());
				fpnlIncomeSumItem.setTitle("����Ŀ�̶�ѡ���б�");
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
			// ��༭���ؼ����ɱ༭
			Common.changeChildControlsEditMode(rightPanel, false);
			frdoSubTypeInc.setEditable(false);
			this.createToolBar();
			// ���ð�ť״̬
			SetActionStatus setActionStatus = new SetActionStatus(dsAcctJJ,
					this, ftreeAcctJJ);
			setActionStatus.setState(false, true);
			// ��ʾ�ұߵ�ҳ����Ϣ
			if (ftabPnlAcctjjInc.getSelectedIndex() == 0) {
				fpnlIncomeSumItem.setVisible(true);
			} else if (ftabPnlAcctjjInc.getSelectedIndex() == 1) {
				fpnlIncomeSumItem.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "��ʾ��֧��Ŀ����ϸ���淢�����󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void modulePanelActived() {
		if (dsAcctJJ != null)
			return;
		String sFilter = "set_year =" + Global.loginYear;
		try {
			// ���ÿ�Ŀ
			dsAcctJJ = iPubInterface.getAcctEconomy(sFilter);
			ftreeAcctJJ.setDataSet(dsAcctJJ);
			ftreeAcctJJ.reset();
			// ����Ԥ���Ŀ
//			dsAcctitem = iPubInterface.getAcctInc(sFilter);
//			ftreeIncAcctitem.setDataSet(dsAcctitem);
//			ftreeIncAcctitem.reset();
//			// �շ���Ŀ
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
			JOptionPane.showMessageDialog(this, "������֧��Ŀ����ϸ���ݷ������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �շ���Ŀ���
	 * 
	 * @author Administrator
	 * 
	 */
	public class IncomeSubItem extends FScrollPane {

		private static final long serialVersionUID = 1L;

		public IncomeSubItem() throws Exception {
			super();
//			ftreIncomeSubItem = new CustomTree("�շ���Ŀ", null,
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
								"ȷ��������༭�����Ϣ??", "��ʾ",
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
								"ȷ��������༭�����Ϣ??", "��ʾ",
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
				JOptionPane.showMessageDialog(AcctJjInc.this, "�л�ҳ�淢�����󣬴�����Ϣ��"
						+ e1.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "ȡ���༭�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "�޸���Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSave() {
		try {
			AcctJjIncSave acctJjIncSave = new AcctJjIncSave(this);
			acctJjIncSave.save();
			this.isDataSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "������Ϣ�������󣬴�����Ϣ��"
					+ e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
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
