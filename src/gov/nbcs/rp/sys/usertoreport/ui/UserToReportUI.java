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
 * Title:用户对报表的主界面
 * </p>
 * <p>
 * Description:

 * @version 1.0
 */
public class UserToReportUI extends ThreeSeperatorModule {

    private static final long serialVersionUID = -2746068779122128469L;

    // 用户树
    FTree userTree = new FTree();

    // 用户树的数据
    List userds;

    // 服务接口
    private IUserToReport iU2Report;

    // 报表树
    private CustomTree reportTree;

    // 用户对报表数据集
    private DataSet curUserReportDS;

    // 保存动作
    private IURDao urDao;

    // 用来存放当 需要将报表与之挂勾的用户列表
    private List userList = new ArrayList();

    // 是否保存
    private boolean isSave = true;

    // 已经选中的报表
    private List selectedReportList = new ArrayList();

    // 切换用户时,当前选中的报表
    private List changeReportList = new ArrayList();

    /**
     * 超类中的抽象方法
     */
    public void setLeftPanelInfo() {
        this.leftPanel.setLayout(new RowPreferedLayout(1));
    }

    /**
     * 超类中的方法
     */
    public void setRightPanelInfo() {
        this.rightPanel.setLayout(new RowPreferedLayout(1));

    }

    /**
     * 超类中的方法 在此实现自己想做的操作
     */
    public void doWhatYouWantToDo() {
        iU2Report = UserToReportStub.getMethod();
        urDao = new URDaoImp(iU2Report);
        // 左面版内容
        try {
            userds = urDao.loadUserInfo();
            userTree
                    .setDataBySpecCode(userds, "user_code", "user_name", "所有用户");
        } catch (Exception e) {
            log.error("构建用户树时出错，CustomTree的异常！");
        }
        leftPanel.addControl(new FScrollPane(userTree), new TableConstraints(1,
                1, true, true));

        // 右面板内容
        try {
            reportTree = new CustomTree("所有报表", null, ConstVariable.KEY_ID,
                    ConstVariable.REPORT_NAME, ConstVariable.PARENT_ID, null,
                    ConstVariable.LVL_ID);
            reportTree.setDataSet(iU2Report.getReport("(0,1,2)"));    		
    		reportTree.reset();
        } catch (Exception e1) {
            log.error("构建用户树时出错，CustomTree的异常！");
        }
		
        reportTree.setIsCheck(true);
        reportTree.setIsCheckBoxEnabled(true);
        rightPanel.addControl(new FScrollPane(reportTree),
                new TableConstraints(1, 1, true, true));

        // 增加监听器
        userTree.addMouseListener(new UserTreeMouseListener(this, iU2Report));
        reportTree.addMouseListener(new ReportTreeMouseListener(this));
    }

    /*
     * 保存到数据库存中
     */
    public void doSave() {
        super.doSave();
        // 需要将报表与之挂勾的用户
        if (userList.size() == 0) {
            JOptionPane.showMessageDialog(Global.mainFrame, "没有选择用户，请选择用户！");
            return;
        }
        // 报表中，用户所选中的结点
        MyTreeNode[] selectNodes = reportTree.getSelectedNodes(false);
        urDao.setUserList(userList);
        urDao.setSelectNodes(selectNodes);
        try {
            urDao.doSave();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(this, "数据保存失败!", "保存操作",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 将按钮置为可用
        List controls = this.getToolbarPanel().getSubControls();
        for (int i = 0; i < controls.size(); i++) {
            FButton btns = (FButton) controls.get(i);
            // -----------------------------------
            if ("保存".equals(btns.getText()))
                btns.setEnabled(true);
        }
        JOptionPane.showMessageDialog(this, "数据保存成功!", "保存操作",
                JOptionPane.INFORMATION_MESSAGE);
        //
        isSave = true;
    }

    /**
     * 设置已经选中的结点
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
     * 设置工具条上按钮状态
     * 
     * @param isLeaf
     *            是否是树形结构的 叶子结点
     */
    public void setButtonOnToolBarState() {
        /*
         * 点增加 保存 取消 可用 点修改 保存 取消 可用 点叶结点 修改 删除可用 点根目录 只有 增加可用
         */
        List controls = this.getToolbarPanel().getSubControls();
        for (int i = 0; i < controls.size(); i++) {
            FButton btns = (FButton) controls.get(i);
            // -----------------------------------
            if ("增加".equals(btns.getText()))
                btns.setEnabled(false);
            if ("修改".equals(btns.getText()))
                btns.setEnabled(false);
            if ("删除".equals(btns.getText()))
                btns.setEnabled(false);
            if ("取消".equals(btns.getText()))
                btns.setEnabled(false);
            if ("保存".equals(btns.getText()))
                btns.setEnabled(false);
            if ("特例单位".equals(btns.getText()))
                btns.setEnabled(false);

            switch (currentState) {
            case VarCons.STATE_BRO:
                if ("增加".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("保存".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_ADD:
                if ("取消".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("保存".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_SAVE:
                if ("增加".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("保存".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_EDIT:
                if ("取消".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("保存".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_DELE:
                if ("增加".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_CANL:

                break;
            case VarCons.STATE_SELECT:
                if ("增加".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("修改".equals(btns.getText()))
                    btns.setEnabled(true);
                if ("删除".equals(btns.getText()))
                    btns.setEnabled(true);
                break;
            case VarCons.STATE_SELECT_PARENT:
                if ("增加".equals(btns.getText()))
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
