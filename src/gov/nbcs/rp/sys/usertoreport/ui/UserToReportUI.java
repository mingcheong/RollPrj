/*
 * @(#)TestUI.java	Mar 12, 2008
 * 
 * Copyright (c) 2008 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.sys.usertoreport.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.standardkind.ui.common.ThreeSeperatorModule;
import gov.nbcs.rp.standardkind.ui.common.VarCons;
import gov.nbcs.rp.sys.usertoreport.action.IURDao;
import gov.nbcs.rp.sys.usertoreport.action.URDaoImp;
import gov.nbcs.rp.sys.usertoreport.ibs.IUserToReport;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTree;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

/**
 * TestUI.java
 * <p>
 * Title:�û��Ա����������
 * </p>
 * <p>
 * Description:

 * @version 1.0
 */
public class UserToReportUI extends ThreeSeperatorModule {

    private static final long serialVersionUID = -2746068779122128469L;

    // �û���
    FTree userTree = new FTree();

    // �û���������
    List userds;

    // ����ӿ�
    private IUserToReport iU2Report;

    // ������
    private CustomTree reportTree;

    // �û��Ա������ݼ�
    private DataSet curUserReportDS;

    // ���涯��
    private IURDao urDao;

    // ������ŵ� ��Ҫ��������֮�ҹ����û��б�
    private List userList = new ArrayList();

    // �Ƿ񱣴�
    private boolean isSave = true;

    // �Ѿ�ѡ�еı���
    private List selectedReportList = new ArrayList();

    // �л��û�ʱ,��ǰѡ�еı���
    private List changeReportList = new ArrayList();

    /**
     * �����еĳ��󷽷�
     */
    public void setLeftPanelInfo() {
        this.leftPanel.setLayout(new RowPreferedLayout(1));
    }

    /**
     * �����еķ���
     */
    public void setRightPanelInfo() {
        this.rightPanel.setLayout(new RowPreferedLayout(1));

    }

    /**
     * �����еķ��� �ڴ�ʵ���Լ������Ĳ���
     */
    public void doWhatYouWantToDo() {
        iU2Report = UserToReportStub.getMethod();
        urDao = new URDaoImp(iU2Report);
        // ���������
        try {
            userds = urDao.loadUserInfo();
            userTree
                    .setDataBySpecCode(userds, "user_code", "user_name", "�����û�");
        } catch (Exception e) {
            log.error("�����û���ʱ����CustomTree���쳣��");
        }
        leftPanel.addControl(new FScrollPane(userTree), new TableConstraints(1,
                1, true, true));

        // ���������
        try {
            reportTree = new CustomTree("���б���", null, ConstVariable.KEY_ID,
                    ConstVariable.REPORT_NAME, ConstVariable.PARENT_ID, null,
                    ConstVariable.LVL_ID);
            reportTree.setDataSet(iU2Report.getReport("(0,1,2)"));    		
    		reportTree.reset();
        } catch (Exception e1) {
            log.error("�����û���ʱ����CustomTree���쳣��");
        }
		
        reportTree.setIsCheck(true);
        reportTree.setIsCheckBoxEnabled(true);
        rightPanel.addControl(new FScrollPane(reportTree),
                new TableConstraints(1, 1, true, true));

        // ���Ӽ�����
        userTree.addMouseListener(new UserTreeMouseListener(this, iU2Report));
        reportTree.addMouseListener(new ReportTreeMouseListener(this));
    }

    /*
     * ���浽���ݿ����
     */
    public void doSave() {
        super.doSave();
        // ��Ҫ��������֮�ҹ����û�
        if (userList.size() == 0) {
            JOptionPane.showMessageDialog(Global.mainFrame, "û��ѡ���û�����ѡ���û���");
            return;
        }
        // �����У��û���ѡ�еĽ��
        MyTreeNode[] selectNodes = reportTree.getSelectedNodes(false);
        urDao.setUserList(userList);
        urDao.setSelectNodes(selectNodes);
        try {
            urDao.doSave();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(this, "���ݱ���ʧ��!", "�������",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ����ť��Ϊ����
        List controls = this.getToolbarPanel().getSubControls();
        for (int i = 0; i < controls.size(); i++) {
            FButton btns = (FButton) controls.get(i);
            // -----------------------------------
            if ("����".equals(btns.getText()))
                btns.setEnabled(true);
        }
        JOptionPane.showMessageDialog(this, "���ݱ���ɹ�!", "�������",
                JOptionPane.INFORMATION_MESSAGE);
        //
        isSave = true;
    }

    /**
     * �����Ѿ�ѡ�еĽ��
     * 
     * @param selectNodes
     * @throws Exception
     */
    public void expandToSelectedNode(List selectNodes) throws Exception {
    	reportTree.cancelSelected(false);
    	reportTree.repaint();
        for (int i = 0; i < selectNodes.size(); i++) {
            reportTree.expandTo("key_id", (String) selectNodes.get(i));
           
            if (reportTree.getSelectedPfTreeNode() != null)
                reportTree.getSelectedPfTreeNode().setIsSelect(true);
        }
    }

    public FTree getUserTree() {
        return userTree;
    }

    public void setUserTree(FTree userTree) {
        this.userTree = userTree;
    }

    public CustomTree getReportTree() {
        return reportTree;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public DataSet getCurUserReportDS() {
        return curUserReportDS;
    }

    public void setCurUserReportDS(DataSet curUserReportDS) {
        this.curUserReportDS = curUserReportDS;
    }

    /**
     * ���ù������ϰ�ť״̬
     * 
     * @param isLeaf
     *            �Ƿ������νṹ�� Ҷ�ӽ��
     */
    public void setButtonOnToolBarState() {
        /*
         * ������ ���� ȡ�� ���� ���޸� ���� ȡ�� ���� ��Ҷ��� �޸� ɾ������ ���Ŀ¼ ֻ�� ���ӿ���
         */
        List controls = this.getToolbarPanel().getSubControls();
        for (int i = 0; i < controls.size(); i++) {
            FButton btns = (FButton) controls.get(i);
            // -----------------------------------
            if ("����".equals(btns.getText()))
                btns.setEnabled(false);
            if ("�޸�".equals(btns.getText()))
                btns.setEnabled(false);
            if ("ɾ��".equals(btns.getText()))
                btns.setEnabled(false);
            if ("ȡ��".equals(btns.getText()))
                btns.setEnabled(false);
            if ("����".equals(btns.getText()))
                btns.setEnabled(false);
            if ("������λ".equals(btns.getText()))
                btns.setEnabled(false);

            switch (currentState) {
            case VarCons.STATE_BRO:
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_ADD:
                if ("ȡ��".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_SAVE:
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_EDIT:
                if ("ȡ��".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_DELE:
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_CANL:

                break;
            case VarCons.STATE_SELECT:
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("�޸�".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("ɾ��".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_SELECT_PARENT:
                if ("����".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            }
        }

    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean isSave) {
        this.isSave = isSave;
    }

    public List getSelectedReportList() {
        return selectedReportList;
    }

    public List getChangeReportList() {
        return changeReportList;
    }

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
