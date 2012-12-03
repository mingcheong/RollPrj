
package gov.nbcs.rp.sys.prjsort.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.prjsort.ibs.IPrjDetail;
import gov.nbcs.rp.sys.prjsort.ibs.IPrjSort;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;

public class PrjsortUI extends RpModulePanel implements ActionedUI { 
    private static final long serialVersionUID = 1L;

    // 主面板
    FSplitPane pnlBase;

    // 项目类别的名称
    private FTextField tfName;

    // 项目类别的说明
    private FTextField tfRemark;

    // add by srm 2011-03-17 面板设置选择
    private FComboBox cbInputSet;

    // 项目类别
    private DataSet ds;

    // 项目明晰化程度的选择界面
    private FRadioGroup rgPrjSort;

    // 公共接口
    private IPubInterface pubserv;

    private CustomTree tree; // 项目类别的tree列表

    private FPanel ContentPanel; // 编辑面板

    private int iTag; // 当前编辑状态 1为浏览，0为编辑

    private String sNodeID; // 根据获取的编码规则和相关的条件生成的编码

    private String sParentID; // 父节点

    private IPrjSort itserv; // 接口

    private String sSortID; // 项目类别的流水号

    private String sPreFName; // 父亲节点的FName

    private String sSortNameOld; // 修改前的项目类别名称

    private String sSortNameNew; // 修改后的项目类别名称

    private String bmk; // 保存当前的dataset的bookmark

    private FRadioGroup rgJJ;

    private DataSet dsSortToDetail; // 明细号确定的类别对明细的数据集 
    
    private CustomTree treeDetail; // 项目明细树
    
    private DataSet dsAcctJJ ;//经济科目对象
    private CustomTree ftreeAcctJJ ;//经济科目容器s
    //存放经济科目的滚动容器
    private FScrollPane fsAcctJJPane = new FScrollPane();

    private IPrjDetail itservDetail; // 项目明细接口

    private DataSet dsDetail; // 项目明细数据集

    /**
     * 外部传值，改变TEXTFIELD的 值
     * 
     * @param aName
     * @param aNote
     * @param aDegree
     */
    private void setTextField(String aName, String aNote, String aDegree,
            String aJJValue, String aInputSet) {
        tfName.setValue(aName);
        tfRemark.setValue(aNote);
        rgPrjSort.setValue(aDegree);
        rgJJ.setValue(aJJValue);
        cbInputSet.setValue(aInputSet);
    }

    /**
     * 设置编辑状态
     * 
     * @param iState
     *            编辑状态
     */
    private void setViewState(int iState) {
        if (iState == 0) {
            // 浏览状态
            Common.changeChildControlsEditMode(ContentPanel, false);
            tree.setEnabled(true);
            treeDetail.setEnabled(false);
            treeDetail.setIsCheckBoxEnabled(false);
            iTag = 0;
        }
        if (iState == 1) {
            // 编辑状态 1:添加 2：修改
            Common.changeChildControlsEditMode(ContentPanel, true);
            iTag = 1; 
            tree.setEnabled(false);
            treeDetail.setEnabled(true);
            treeDetail.setIsCheckBoxEnabled(true);
        }
        if (iState == 2) {
            // 编辑状态 1:添加 2：修改
            Common.changeChildControlsEditMode(ContentPanel, true);
            iTag = 2; 
            tree.setEnabled(false);
            treeDetail.setEnabled(true);
            treeDetail.setIsCheckBoxEnabled(true);
        }
        try {
            setButtonState(iState);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * 根据选择的tree数据在编辑界面显示相关的信息
     * 
     * @throws Exception
     */
    private void getData() throws Exception {
        String aName; // 类别名称
        String aNote; // 类别说明
        String aDegree;// 类别明细化程度
        String aJJValue;// 经济科目类型
        String aSortID; // 类别编码
        String aInputSet;// 输入值
        // 如果类别数据集为空，则把界面信息清空
        if (ds.isEmpty() || ds.eof() || ds.bof()) {
            setTextField("", "", "", "", "");
            return;
        }
        aName = ds.fieldByName(IPrjSort.PRJSORT_NAME).getString();
        aNote = Common.nonNullStr(ds.fieldByName(IPrjSort.NOTE).getValue());
        // 如果备注为空则设置为空
        aDegree = ds.fieldByName(IPrjSort.DETAIL_DEGREE).getString();
        // 如果备注为空则设置为空
        aJJValue = ds.fieldByName(IPrjSort.JJTYPE).getString();
        aSortID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
        aInputSet = ds.fieldByName("input_set_id").getString();
        if ((tree.getSelectedNode() == null) || (tree.getSelectedNode() == tree.getRoot())){
            // 如果树没有选择节点或选择根节点，则界面信息晴空
            setTextField("", "", "", "", ""); 
            setAcctJJTreeSelect("");
        }
        else {
            // 设置库里信息在界面显示
            setTextField(aName, aNote, aDegree, aJJValue, aInputSet);
            // 定位到类别编码
            setTreeDetailSel(aSortID);
            //对应的经济科目
            setAcctJJTreeSelect(aSortID);
        }
    }

    /**
     * 界面初始化
     */
    public void initize() {
        // 主面板
        pnlBase = new FSplitPane();
        pnlBase.setOrientation(1);
        pnlBase.setDividerLocation(300);
        try {
            // 左面面板的元素定义 
            FPanel pnlLeft = new FPanel(); // 左面面板
            // pnlLeft.setTitle("当前项目类别列表");
            RowPreferedLayout leftLayout = new RowPreferedLayout(1);
            leftLayout.setColumnWidth(200);
            pnlLeft.setLayout(leftLayout);
            // tree放到scrollPane上，使具有下拉功能
            FScrollPane treePanel = new FScrollPane();

            BeanFactory beanfac = BeanFactoryUtil
                    .getBeanFactory("gov/nbcs/rp/sys/prjsort/conf/PrjSortConf.xml");
            itserv = (IPrjSort) beanfac.getBean("rpPrjSortTreeService");
            ds = itserv.getDataset();

            pubserv = PubInterfaceStub.getMethod(); // 获得公共接口的接口
            SysCodeRule codeRule = itserv.getCodeRule(); // 获取项目属性的编码规则

            tree = new CustomTree("项目类别", ds, IPrjSort.PRJSORT_CODE,
                    IPrjSort.PRJSORT_NAME, null, codeRule);
             
            treePanel.addControl(tree);
            FLabel lbTreeName = new FLabel();
            lbTreeName.setText("    当前项目类别列表");
            pnlLeft.addControl(lbTreeName, new TableConstraints(1, 1, false,
                    true));
            pnlLeft.add(treePanel, new TableConstraints(1, 1, true, true)); // 左面面板上的项目类别的树

            FPanel RightPanel = new FPanel(); // 右面面板
            RightPanel.setTitle("当前项目类别明细信息");

            /* 右边面板上元素定义 */
            RowPreferedLayout RightLayout = new RowPreferedLayout(2); // 分为三列 
            RowPreferedLayout ContentLayout = new RowPreferedLayout(2); 
            tfName = new FTextField("项目类别名称"); // 第一列第一行
            tfName.setProportion(0.3f);
            tfName.setMaxLength(50); 
            rgPrjSort = new FRadioGroup("", 1);
            rgPrjSort
                    .setRefModel("1#不填明细+2#只填写按经济科目分项目费用内容的项目明细预算+3#只填按经济科目分资金来源的项目明细预算+4#两者都填");
            rgPrjSort.setProportion(0); // 5
            FPanel rgPrjSortPanel = new FPanel();
            RowPreferedLayout rgPrjSortLayout = new RowPreferedLayout(2);
            rgPrjSortPanel.setLayout(rgPrjSortLayout);
            rgPrjSortPanel.setTitle("项目明细化程度");
            rgPrjSortPanel.addControl(rgPrjSort, new TableConstraints(4, 2,
                    true, true)); // 明细化程度列表
            // 项目类别说明 占一行一列 第五行第一列
            tfRemark = new FTextField("项目类别说明");
            tfRemark.setProportion(0.3f);
            tfRemark.setMaxLength(150);

            // 支出经济科目范围
            rgJJ = new FRadioGroup("", 0);
            rgJJ.setRefModel("1#按项目明细支出+2#所有经济科目");
            rgJJ.setProportion(0);
            FPanel pnlJJ = new FPanel();
            RowPreferedLayout layJJ = new RowPreferedLayout(1);
            pnlJJ.setLayout(layJJ);
            pnlJJ.setTitle("支出经济科目范围");
            pnlJJ.addControl(rgJJ, new TableConstraints(1, 1, false, true));
            
            //设置  支出经济科目范围 的单击事件
            addAcctJJMouseListener();
            
            //增加经济科目   
			ftreeAcctJJ = new CustomTree("经济科目", null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null);
			ftreeAcctJJ.setSortKey(IPubInterface.ACCT_CODE_JJ); 
			ftreeAcctJJ.setIsCheck(true); 
			ftreeAcctJJ.setAutoscrolls(false);
			//设置经济科目
            setAcctJJTree(); 
            fsAcctJJPane.addControl(ftreeAcctJJ);
			pnlJJ.addControl(fsAcctJJPane, new TableConstraints(9, 1, false, true)); 

            // add by srm 2011-03-17 面板设置选择
            cbInputSet = new FComboBox("面板设置选择");
            cbInputSet.setProportion(0.3f);
            cbInputSet.setRefModel(itserv.GetInputSetString());

            /* 右边面板布局 */
            ContentPanel = new FPanel();
            RightPanel.setLayout(RightLayout); // 右面主界面分为三列
            ContentPanel.setLayout(ContentLayout); // 右面主界面又分为一列

            /* 右面面板第一列布局 */
            ContentPanel.addControl(tfName, new TableConstraints(1, 2, false,
                    false)); // 加第一行项目类别名称
            ContentPanel.addControl(rgPrjSortPanel, new TableConstraints(5, 2,
                    false, false)); // 加第二行项目明细化程度
//          add by srm 2011-03-17 增加面板设置选择
            ContentPanel.addControl(cbInputSet, new TableConstraints(1, 2,
                    false, false)); 
            ContentPanel.addControl(pnlJJ, new TableConstraints(11, 2, false,
                    false));
            
            // 项目类别与明细的对应关系放到了一个单独模块中做 

            // 设置右面板的边距 
            RightPanel.setLeftInset(20);
            RightPanel.setTopInset(20);

            /* 右面主界面布局，第一列放内容，其他两列添满为空 */
            RightPanel.addControl(ContentPanel, new TableConstraints(18, 2,
                    false, false));
            RightPanel.addControl(getDetailPanel(), new TableConstraints(10, 2,
                    true, false));
            pnlBase.addControl(pnlLeft);
            pnlBase.addControl(RightPanel);
            this.add(pnlBase);
            this.createToolBar();
            setViewState(0); // 初始化完成把编辑面板设置为浏览状态
            TreePath path = new TreePath(((DefaultTreeModel) tree.getModel())
                    .getPathToRoot(tree.getRoot()));
            tree.expandPath(path);
            addDetailListener();
            // 类别变换时要添加的监听事件
            ds.addDataChangeListener(new DataChangeListener() {
                private static final long serialVersionUID = 1L;

                public void onDataChange(DataChangeEvent e) throws Exception {
                    if ((e.type() == DataChangeEvent.CURSOR_MOVED)
                            && (e.getSender() != null)
                            && ((e.getSender() instanceof TreeNode) || (e
                                    .getSender() instanceof DataSet))) {
                        // 判断数据的改变是否是由树引起的
                        // 设置数据到界面显示
                        getData();
                    }
                }
            });
            // 设置树不可编辑
            tree.setEditable(false);
            treeDetail.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择项目明细树面板
     * 
     * @return
     */
    private FPanel getDetailPanel() throws Exception {
        FPanel pnl = new FPanel();
        pnl.setTitle("项目明细列表");
        RowPreferedLayout layDetail = new RowPreferedLayout(1);
        pnl.setLayout(layDetail);
        FScrollPane pnlDetailTree = new FScrollPane();
        pnlDetailTree.addControl(getDetailTree());
        pnl.addControl(pnlDetailTree, new TableConstraints(1, 1, true, true));
        return pnl;
    }

    /**
     * 获得项目明细数据集
     * @return
     * @throws Exception
     */
    private DataSet getDsDtail() throws Exception {
        if (dsDetail == null) {
            BeanFactory beanfac = BeanFactoryUtil
                    .getBeanFactory("gov/nbcs/rp/sys/prjdetail/conf/PrjDetailConf.xml");
            itservDetail = (IPrjDetail) beanfac
                    .getBean("bmys.PrjDetailTreeService");
            dsDetail = itservDetail.getDataset(IPrjDetail.Table_SIMP_MASTER,
                    null); // 项目明细列表
        }
        return dsDetail;
    }

    /**
     * 获得项目类别对明细数据集 
     * @param aDetailID
     * @return
     * @throws Exception
     */
    private DataSet getDsSortToDetail(String aSortID) throws Exception {
        String sFilter = "PRJSORT_CODE='" + aSortID + "'";
        dsSortToDetail = itservDetail
                .getDataset("fb_p_sort_to_detail", sFilter);
        return dsSortToDetail;
    }

    /**
     * 保存选择的类别对类档 
     * @throws Exception
     */
    private void saveDsSortToDetail(String bmk) throws Exception {
        if ((dsSortToDetail != null) && !dsSortToDetail.isEmpty()) {
			dsSortToDetail.clearAll();
		}
        String sortCode = ds.fieldByName("PRJSORT_Code").getString();
        dsDetail.beforeFirst();
        MyTreeNode[] nodes = treeDetail.getSelectedNodes(true);
        MyTreeNode root = (MyTreeNode) treeDetail.getRoot();
        String bmkPrjDetail = null;
        List list = new ArrayList();
        list.add("delete from fb_p_sort_to_detail where prjsort_code='"
                + sortCode + "'");
        if ((dsSortToDetail != null) && !dsSortToDetail.isEmpty()) {
			dsSortToDetail.clearAll();
		} 
        for (int i = 0; i < nodes.length; i++) {
            // 选择选择的节点
            if (nodes[i] != root) {
                // 过滤掉根节点
                // 选择该节点的detail_code
                // ,与sort_to_detail相比较,如果相等,则记录sort_detail_code的bmk,保存起来
                bmkPrjDetail = nodes[i].getBookmark();
                if (dsDetail.gotoBookmark(bmkPrjDetail)) {
                    String sDetailID = dsDetail.fieldByName(
                            IPrjDetail.DETAIL_CODE).getString();
                    if (dsSortToDetail == null) {
						getDsSortToDetail(sortCode).append();
					} else {
						dsSortToDetail.append();
					}
                    dsSortToDetail.fieldByName("set_year").setValue(
                            Global.getSetYear());
                    dsSortToDetail.fieldByName("prjsort_code").setValue(
                            sortCode);
                    dsSortToDetail.fieldByName("detail_code").setValue(
                            sDetailID);
                    dsSortToDetail.fieldByName("rg_code").setValue(
                            Global.getCurrRegion());
                    list
                            .add("insert into fb_p_sort_to_detail(set_year,prjsort_code,detail_code,rg_code) values("
                                    + Global.getSetYear()
                                    + ",'"
                                    + sortCode
                                    + "','"
                                    + sDetailID
                                    + "',"
                                    + Global.getCurrRegion() + ")");
                }
            }
        }
        // 保存选择明细节点信息到库里 
        itserv.ExecuteSql(list);
        String sFilter = "PRJSORT_CODE='" + sortCode + "'";
        dsSortToDetail = itservDetail
                .getDataset("fb_p_sort_to_detail", sFilter);
        dsSortToDetail.applyUpdate(); 
    }
    
    
    /**
     * 获取专项类别对经济科目，用于保存
     * 
     * @throws Exception
     */
    private List getPrjSortToAcctJJ(String PRJSORT_CODE) throws Exception { 
    	//保存所有的节点，因为专项录入时可以直接带出父节点
        MyTreeNode[] nodes = ftreeAcctJJ.getSelectedNodes(false); 
        List list = new ArrayList(); 
        for (int i = 0; i < nodes.length; i++) {
            // 选择选择的节点 
        	if(dsAcctJJ.gotoBookmark(nodes[i].getBookmark())){
        		XMLData data  = new XMLData();
        		data.put("PRJSORT_CODE",PRJSORT_CODE);
        		data.put("ACCT_CODE_JJ",dsAcctJJ.fieldByName("ACCT_CODE_JJ").getValue());
        		data.put("RG_CODE",Global.getCurrRegion());
        		
        		list.add(data);
        	}  
        }
        return list;
    }

    /**
     * 设置类别对明细的选择
     * 
     * @throws Exception
     */
    private void setTreeDetailSel(String sortID) throws Exception {
        dsSortToDetail = getDsSortToDetail(sortID);
        // 遍历TREE
        MyTreeNode root = (MyTreeNode) treeDetail.getRoot(); // 根节点
        Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
        while (enumeration.hasMoreElements()) { // 开始遍历
            MyTreeNode node = (MyTreeNode) enumeration.nextElement();
            if (node != root) { // 过滤掉根节点
                String bk1 = node.getBookmark(); // 获取该节点的BOOKMARK
                dsDetail.gotoBookmark(bk1);
                String sCode = dsDetail.fieldByName(IPrjDetail.DETAIL_CODE)
                        .getString();
                if (dsSortToDetail.isEmpty()) {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(false); // 设置该节点为选取状态
                    continue;
                }
                String[] sDCodes = getDataSelDetailCode();
                Arrays.sort(sDCodes);
                int idex = Arrays.binarySearch(sDCodes, sCode);
                if (idex >= 0) {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(true); // 设置该节点为选取状态
                } else {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(false); // 设置该节点为选取状态
                }
            }
        }
        treeDetail.repaint();
    }

    /**
     * 获取库里sort对应的类别对明细数据集里的所有明细编码
     * 
     * @return
     * @throws Exception
     */
    private String[] getDataSelDetailCode() throws Exception {
        String[] sDetailCodes = new String[dsSortToDetail.getRecordCount()];
        dsSortToDetail.beforeFirst();
        int i = 0;
        while (dsSortToDetail.next()) {
            sDetailCodes[i++] = dsSortToDetail.fieldByName("detail_code")
                    .getString();
        }
        return sDetailCodes;
    }

    /**
     * 选择项目明细树
     * 
     * @return
     * @throws Exception
     */
    private CustomTree getDetailTree() throws Exception {
        if (treeDetail == null) {
            treeDetail = new CustomTree("项目明细", getDsDtail(),
                    IPrjDetail.DETAIL_ID, IPrjDetail.DETAIL_NAME,
                    IPrjDetail.PAR_ID, null);
            treeDetail.setIsCheckBoxEnabled(true);
            treeDetail.setIsCheck(true);
            treeDetail.reset();
        }
        return treeDetail;
    }

    /**
     * 添加操作
     */
    public void doAdd() {
        // 添加操作
        MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
        if (nodeSel == null) {
            JOptionPane.showMessageDialog(null, "请选择节点");
            return;
        }
        // 生成本级编码
        if (!ds.isEmpty() && !ds.eof() && !ds.bof()) {
			bmk = ds.toogleBookmark();
		}
        String sFilter = null; // 过滤条件
        try { // 修改父节点的endflag信息
            // 生成本级编码
            if ((tree.getSelectedNode() != tree.getRoot())
                    && (tree.getSelectedNode().getChildCount() <= 0)) {
				//                if (!PrjDetailAction.checkUseInPrj(
//                        PrjDetailAction.check_PrjSort, ds.fieldByName(
//                                IPrjSort.PRJSORT_CODE).getString())) {
//                    JOptionPane.showMessageDialog(null, "该项目类别已经被使用");
//                    return;
//                }
				if (tree.getSelectedNode() == tree.getRoot()) {
				    // 如果选择的是根节点
				    sParentID = "";
				    sPreFName = ""; // 该节点的FPreNAME为空
				    sFilter = IPrjSort.SET_YEAR + "=" + Global.loginYear
				            + " AND trim(" + IPrjSort.PAR_ID + ") IS NULL";
				} else {
				    // 如果选择的不是根节点
				    // 获取当前编码为父节点
				    sParentID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
				    // 生成全名
				    sPreFName = ds.fieldByName(IPrjSort.PRJSORT_FNAME).getString();
				    if (Common.isNullStr(sPreFName)
				            || "null".equalsIgnoreCase(sPreFName)) {
						// 如果父节点的fname为空，则将父节点name赋给它
				        sPreFName = ds.fieldByName(IPrjSort.PRJSORT_NAME)
				                .getString()
				                + "_";
					} else {
						// 如果父节点fname不为空，则将父节点的fname赋给它
				        sPreFName = sPreFName + "_";
					}
				    // 生成获取节点编码的过滤条件
				    sFilter = IPrjSort.SET_YEAR + "=" + Global.loginYear + " AND "
				            + IPrjSort.PAR_ID + "=" + "'" + sParentID + "'"; // 附加条件
				}
			}
            // 生成类别编码
            sNodeID = pubserv.getNodeID(IPrjSort.Table_Name,
                    IPrjSort.PRJSORT_CODE, sParentID, sFilter, itserv
                            .getCodeRule());
            if (sNodeID == null) {
                JOptionPane.showMessageDialog(this, "根据编码规则生成的编码为空\n请检查编码规则设置");
                return;
            }
            if (sNodeID.indexOf("0")==0){
            	sNodeID = "7"+ sNodeID.substring(1,sNodeID.length());
            }
            // 生成流水ID
            int maxID = itserv.getMaxID();
            sSortID = Common.getStrID(new BigDecimal(maxID),
                    IPrjSort.iPrjSortIDLength);
            ds.maskDataChange(true);
            ds.append();
            setTextField("", "", "", "", "");
            JRadioButton radios[] = rgPrjSort.getRadios();
            radios[0].setSelected(true);
            setViewState(1); // 设置为编辑状态
            rgJJ.setEditable(false);
            rgJJ.setEnabled(false);
            tfName.setFocus();
            ds.maskDataChange(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除操作
     */
    public void doDelete() {
        // 删除操作
        try {
            MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
            if ((nodeSel == null) || (nodeSel == tree.getRoot())) {
                JOptionPane.showMessageDialog(null, "请选择节点");
                return;
            }
            if (nodeSel.getChildCount() > 0) {
                JOptionPane.showMessageDialog(null, "请先删除子节点");
                return;
            }
            // 生成本级编码
            if (!ds.isEmpty() && !ds.eof() && !ds.bof()) {
				bmk = ds.toogleBookmark();
			} else {
				return;
			}
//            if (!PrjDetailAction.checkUseInPrj(PrjDetailAction.check_PrjSort,
//                    ds.fieldByName(IPrjSort.PRJSORT_CODE).getString())) {
//                JOptionPane.showMessageDialog(null, "该项目类别已经被使用");
//                return;
//            }
            String sortCode = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
            String sID = null;
            ds.maskDataChange(true);
            if (ds.prior()) {
                sID = ds.fieldByName(IPrjSort.PRJSORT_ID).getString();
                ds.next();
            } else if (ds.next()) {
                if (ds.next()) {
					sID = ds.fieldByName(IPrjSort.PRJSORT_ID).getString();
				} else {
					sID = "";
				}
                ds.prior();
            } else {
				sID = "";
			}
            // 根据该节点保存其父亲节点
            MyTreeNode ParentNode = tree.getSelectedNode();
            // 保存其父节点
            if ((ParentNode != null) && (ParentNode != tree.getRoot())) {
                String sPreID = ds.fieldByName(IPrjSort.PAR_ID).getString();
                // dataset里父节点信息
                if (tree.getSelectedNode().getChildCount() == 0) {
                    // 如果该节点无子节点
                    if (JOptionPane.showConfirmDialog(null, "是否确定删除信息", "提示信息",
                            2) == 0) {
                        // 是否确定删除信息
                        ds.delete();
                        if (ParentNode.getChildCount() == 0) {
                            ds.locate(IPrjSort.PRJSORT_CODE, sPreID);
                            ds.fieldByName(IPrjSort.END_FLAG).setValue("1");
                        }
                        itserv.dsPost(ds);
                        List list = new ArrayList();
                        list
                                .add("delete from fb_p_sort_to_detail where PRJSORT_CODE= '"
                                        + sortCode + "'");
                        itserv.ExecuteSql(list);
                        ds.applyUpdate();
                        tree.expandTo(IPrjSort.PRJSORT_CODE, sID);
                        if (ds.isEmpty()) {
							setTextField("", "", "", "", "");
						}
                    }
                } else {
					JOptionPane.showMessageDialog(null, "请先删除子节点");
				}
            }
            ds.maskDataChange(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消操作
     */
    public void doCancel() {
        // 取消操作
        try {
            ds.maskDataChange(true);
            ds.cancel(); 
            setViewState(0);
            ds.maskDataChange(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doInsert() { 
    }

    /**
     * 修改操作
     */
    public void doModify() {
        // 修改操作
        MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
        if ((nodeSel == null) || (nodeSel == tree.getRoot())) {
            JOptionPane.showMessageDialog(null, "请选择节点");
            return;
        }
        // 生成本级编码
        try {
//            if (!PrjDetailAction.checkUseInPrj(PrjDetailAction.check_PrjSort,
//                    ds.fieldByName(IPrjSort.PRJSORT_CODE).getString())) {
//                JOptionPane.showMessageDialog(null, "该项目类别已经被使用");
//                return;
//            }
            if ((tree.getSelectedNode() != null)
                    && (tree.getSelectedNode() != tree.getRoot())) {
                // 如果选择了节点并且选择的是不根节点则执行修改操作
                // 保存dataset当前的bookmark
                if (tree.getSelectedNode().getParent() == tree.getRoot()) {
					sPreFName = ""; // 该节点的FPreNAME为空
				} else {
                    String bmkNow = tree.getSelectedNode().getBookmark();
                    MyTreeNode nodePar = (MyTreeNode) tree.getSelectedNode()
                            .getParent();
                    String bmkPar = nodePar.getBookmark();
                    ds.maskDataChange(true);
                    ds.gotoBookmark(bmkPar);
                    sPreFName = ds.fieldByName(IPrjSort.PRJSORT_FNAME)
                            .getString()
                            + "_";
                    ds.maskDataChange(false);
                    ds.gotoBookmark(bmkNow);
                }
                bmk = ds.toogleBookmark();
                // 设置为编辑状态
                setViewState(2);
                sNodeID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
                sSortID = ds.fieldByName(IPrjSort.PRJSORT_ID).getString();
                sParentID = ds.fieldByName(IPrjSort.PAR_ID).getString();
                String sDValue = ds.fieldByName(IPrjSort.DETAIL_DEGREE)
                        .getString();
                // 记录此时的项目类别名称，和后面的修改后的项目类别名称做比较
                sSortNameOld = tfName.getValue().toString();
                ds.edit();
                if (Common.isEqual("3", sDValue)||Common.isEqual("4", sDValue)) {
                    rgJJ.setEnabled(true);
                    rgJJ.setEditable(true);
                } else {
                    rgJJ.setEnabled(false);
                    rgJJ.setEditable(false);
                }
                tfName.setFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存操作
     */
    public void doSave() {
        // 保存
        ds.maskDataChange(true);
        String bmk = ds.toogleBookmark();
        String sNodeIDNew = sNodeID;
        if (Common.isNullStr(Common.nonNullStr(tfName.getValue()))) {
            JOptionPane.showMessageDialog(null, "类别名称不能为空");
            return;
        }
        // add by srm 2011-03-17 必须要有设置类型
        if (Common.isNullStr(Common.nonNullStr(cbInputSet.getValue()))) {
            JOptionPane.showMessageDialog(null, "面板设置不能为空");
            return;
        }
        try {
            // 获取明细
            JRadioButton[] radios = rgPrjSort.getRadios();
            int value = 1;
            if (radios[0].isSelected()) {
				value = 1;
			} else if (radios[1].isSelected()) {
				value = 2;
			} else if (radios[2].isSelected()) {
				value = 3;
			} else if (radios[3].isSelected()) {
				value = 4;
			}
            if ((value == 2) || (value == 4)) {
                // 如果选择挂接明细，则必须选择明细
                MyTreeNode[] nodes = this.treeDetail.getSelectedNodes(true);
                if (nodes.length == 0) {
                    JOptionPane.showMessageDialog(pnlBase, "必须挂接明细");
                    return;
                }
            }
            if ((iTag == 1) || (iTag == 2)) {
                // 当前时编辑状态才保存
                // 保存当前节点信息
                // 将生成的编码传给dataset
                ds.fieldByName(IPrjSort.PRJSORT_CODE).setValue(sNodeID);
                ds.fieldByName(IPrjSort.PRJSORT_ID).setValue(sSortID);
                // 全名
                ds.fieldByName(IPrjSort.PRJSORT_FNAME).setValue(
                        sPreFName + tfName.getValue().toString());
                // 修改后的项目类别名称
                sSortNameNew = tfName.getValue().toString();
                ds.fieldByName(IPrjSort.PRJSORT_NAME).setValue(sSortNameNew);
                ds.fieldByName(IPrjSort.NOTE).setValue(tfRemark.getValue());
                ds.fieldByName(IPrjSort.DETAIL_DEGREE).setValue(
                        String.valueOf(value));
                ds.fieldByName("rg_code").setValue(Global.getCurrRegion());
                // 获取明细
                JRadioButton[] radiojs = rgJJ.getRadios();
                int valuej = 2;
                if (radiojs[0].isSelected()) {
					valuej = 1;
				} else if (radiojs[1].isSelected()) {
					valuej = 2;
				}
                ds.fieldByName(IPrjSort.JJTYPE)
                        .setValue(String.valueOf(valuej));
                ds.fieldByName(IPrjSort.INPUT_SET_ID).setValue(
                        cbInputSet.getValue());
                // saveJJType();
                if (iTag == 1) {
					ds.fieldByName(IPrjSort.END_FLAG).setValue("1");
				}
                ds.fieldByName(IPrjSort.PAR_ID).setValue(sParentID);
                ds.fieldByName(IPrjSort.SET_YEAR).setValue(Global.loginYear);
                String bmkAdd = ds.toogleBookmark();
                // 修改父节点end_flag信息
                if (!Common.isNullStr(ds.fieldByName(IPrjSort.PAR_ID)
                        .getString())) {
                    // 如果该节点有父节点，则修改其父亲节点的end_flag标识
                    ds.locate(IPrjSort.PRJSORT_CODE, ds.fieldByName(
                            IPrjSort.PAR_ID).getString());
                    ds.edit();
                    ds.fieldByName(IPrjSort.END_FLAG).setValue("0");
                    ds.gotoBookmark(bmkAdd);
                }
                if ((iTag == 2) && !sSortNameOld.equals(sSortNameNew)) {
                    // 如果是修改状态，则涉及到修改其父节点的名称，它的所有子节点的fname都要发生改变
                    ds.gotoBookmark(bmk);
                    // 记录当前所选节点
                    MyTreeNode parentNode = tree.getSelectedNode();
                    Enumeration allNodes = parentNode.breadthFirstEnumeration();
                    while (allNodes.hasMoreElements()) {
                        // 循环当前节点的所有子节点
                        MyTreeNode node = (MyTreeNode) allNodes.nextElement();
                        if (node != parentNode) {
                            ds.gotoBookmark(node.getBookmark());
                            int idx = ds.fieldByName("PRJSORT_FNAME")
                                    .getString().indexOf(
                                            ds.fieldByName("PRJSORT_NAME")
                                                    .getString()) - 1;
                            String PRJSORT_FNAME = sSortNameNew
                                    + ds.fieldByName(IPrjSort.PRJSORT_FNAME)
                                            .getString().substring(idx);
                            ds.fieldByName(IPrjSort.PRJSORT_FNAME).setValue(
                                    Common.nonNullStr(PRJSORT_FNAME));
                        }
                    }
                }
                // 传递信息到服务器端
                itserv.dsPost(ds);
                ds.applyUpdate();
                if ((value != 1) && (value!=3)){
                    saveDsSortToDetail(bmk); 
                }
                
                //保存专项类别对经济科目
                //只有在  所有经济科目的情况下才保存
                if("2".equals(rgJJ.getValue())){ 
                    String sortCode = ds.fieldByName("PRJSORT_Code").getString();
                    itserv.savePrjSortToAcctJJ(sortCode,getPrjSortToAcctJJ(sortCode)); 
                }
                
                // 设置为浏览状态
                setViewState(0);
                ds.maskDataChange(false);
                tree.setDataSet(ds);
                tree.reset();
                tree.expandTo(IPrjSort.PRJSORT_CODE, sNodeIDNew);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取经济科目，若已经选择，也需打勾
     */
    public void setAcctJJTree(){
    	//经济科目 
		try {
			dsAcctJJ = itserv.getPrjAcctJJ(GlobalEx.loginYear);
			ftreeAcctJJ.setDataSet(dsAcctJJ);
			ftreeAcctJJ.reset();
		} catch (Exception e) { 
			e.printStackTrace();
		}  
    }
    
    /**
     * 设置经济科目对应的选择 
     * @throws Exception
     */
    private void setAcctJJTreeSelect(String PRJSORT_CODE) throws Exception {
    	//先清空
    	ftreeAcctJJ.cancelSelected(false);
    	if((PRJSORT_CODE==null)||"".equals(PRJSORT_CODE)) {
			return;
		}
    	//先全部清空
    	//获取已经选择的经济科目 
        DataSet dsPrjToAcctJJ = itserv.getPrjSortToAcctJJ(PRJSORT_CODE);
        if (dsAcctJJ.isEmpty()) {
			return;
		}
        // 遍历TREE
        MyTreeNode root = (MyTreeNode) ftreeAcctJJ.getRoot(); // 根节点
        Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
        while (enumeration.hasMoreElements()) { // 开始遍历
            MyTreeNode node = (MyTreeNode) enumeration.nextElement();
            if (node != root) { // 过滤掉根节点
                String bk1 = node.getBookmark(); // 获取该节点的BOOKMARK  
                if(dsAcctJJ.gotoBookmark(bk1)){
                	//查找是否存在
                	dsPrjToAcctJJ.beforeFirst();
                	while(dsPrjToAcctJJ.next()){
                		if(dsPrjToAcctJJ.fieldByName("ACCT_CODE_JJ").getValue().equals(dsAcctJJ.fieldByName("ACCT_CODE_JJ").getValue())){
                			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); 
                            pNode.setIsSelect(true); // 设置该节点为选取状态
                		}
                	} 
                }  
            }
        }
        ftreeAcctJJ.repaint();
    }
    
    /**
     * 设置 支出经济科目范围 单击事件
     */
    private void addAcctJJMouseListener(){
    	rgJJ.getRadioByIndex(0).addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent arg0) {
    			fsAcctJJPane.setEnabled(false);
    			ftreeAcctJJ.setEditable(false);
    			ftreeAcctJJ.setEnabled(false);
    			ftreeAcctJJ.setIsCheckBoxEnabled(false); 
			}  
    	});
    	
    	rgJJ.getRadioByIndex(1).addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent arg0) {
    			fsAcctJJPane.setEnabled(true);
    			ftreeAcctJJ.setEditable(true);
    			ftreeAcctJJ.setEnabled(true);
    			ftreeAcctJJ.setIsCheckBoxEnabled(true); 
			}  
    	});
    }

    /**
     * 设置按钮状态
     * 
     * @param aState
     * @throws Exception
     */
    private void setButtonState(int aState) throws Exception {
        List controls = this.getToolbarPanel().getSubControls();
        int state_Browse = 0;
        boolean isEditEnable;
        boolean isSaveEnable;
        if (aState == state_Browse) {
            isEditEnable = true;
            isSaveEnable = false;
        } else {
            isEditEnable = false;
            isSaveEnable = true;
        }
        for (int i = 0; i < controls.size(); i++) {

            FButton btnGet = (FButton) controls.get(i);
            if ("关闭".equals(btnGet.getText())) {
                btnGet.setEnabled(true);
            }
            if ("增加".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("修改".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("删除".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("保存".equals(btnGet.getText())) {
                btnGet.setEnabled(isSaveEnable);
            }
            if ("取消".equals(btnGet.getText())) {
                btnGet.setEnabled(isSaveEnable);
            }

        }
    }

    /**
     * 关闭当前面板
     */
    public void doClose() {
        ((FFrame) Global.mainFrame).closeMenu(); 
    }

    public void doSent() { 
    }

    public void doBack() { 
    }

    public void doReCall() { 
    }

 
    /**
     * 控制经济科目范围是否显示
     * 
     * @throws Exception
     */
    private void addDetailListener() throws Exception {
        JRadioButton[] radios = rgPrjSort.getRadios();
        int num = radios.length;
        for (int i = 0; i < num; i++) {
            radios[i].addMouseListener(new rgDetailMouseClickListener(rgJJ,
                    String.valueOf(i)));
        }
    }

    /**
     * 设置经济科目范围的显示
     * 
     * @param iValue
     * @param radio
     */
    private void setRGJJVis(String iValue, FRadioGroup radio) {
        if (!rgPrjSort.isEditable()) {
			return;
		}
        if ("1".equals(iValue) || "2".equals(iValue)) {
            radio.setValue("2");
        }
        if ("2".equals(iValue)||"3".equals(iValue)) {
            radio.setEditable(true);
            radio.setEnabled(true);
            ftreeAcctJJ.setEditable(true);
            ftreeAcctJJ.setEnabled(true); 
            ftreeAcctJJ.setIsCheckBoxEnabled(true);
            radio.setValue("2");
        } else {
            radio.setEditable(false);
            radio.setEnabled(false);
            ftreeAcctJJ.cancelSelected(false);
            ftreeAcctJJ.setEditable(false);
            ftreeAcctJJ.setEnabled(false);
            ftreeAcctJJ.setIsCheckBoxEnabled(false);
            ftreeAcctJJ.repaint();
        }
    }

    /**
     * 明细面板的监听事件
     * 
     * @author Administrator
     * 
     */
    private class rgDetailMouseClickListener extends MouseAdapter {
        private String iValue;

        private FRadioGroup radio;

        public rgDetailMouseClickListener(FRadioGroup aradio, String aSelValue) {
            this.iValue = aSelValue;
            this.radio = aradio;
        }

        public void mouseClicked(MouseEvent e) {
            setRGJJVis(iValue, radio);
        }
    }

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}
}
