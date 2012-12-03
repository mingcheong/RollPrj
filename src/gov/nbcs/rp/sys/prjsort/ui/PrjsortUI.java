
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

    // �����
    FSplitPane pnlBase;

    // ��Ŀ��������
    private FTextField tfName;

    // ��Ŀ����˵��
    private FTextField tfRemark;

    // add by srm 2011-03-17 �������ѡ��
    private FComboBox cbInputSet;

    // ��Ŀ���
    private DataSet ds;

    // ��Ŀ�������̶ȵ�ѡ�����
    private FRadioGroup rgPrjSort;

    // �����ӿ�
    private IPubInterface pubserv;

    private CustomTree tree; // ��Ŀ����tree�б�

    private FPanel ContentPanel; // �༭���

    private int iTag; // ��ǰ�༭״̬ 1Ϊ�����0Ϊ�༭

    private String sNodeID; // ���ݻ�ȡ�ı���������ص��������ɵı���

    private String sParentID; // ���ڵ�

    private IPrjSort itserv; // �ӿ�

    private String sSortID; // ��Ŀ������ˮ��

    private String sPreFName; // ���׽ڵ��FName

    private String sSortNameOld; // �޸�ǰ����Ŀ�������

    private String sSortNameNew; // �޸ĺ����Ŀ�������

    private String bmk; // ���浱ǰ��dataset��bookmark

    private FRadioGroup rgJJ;

    private DataSet dsSortToDetail; // ��ϸ��ȷ����������ϸ�����ݼ� 
    
    private CustomTree treeDetail; // ��Ŀ��ϸ��
    
    private DataSet dsAcctJJ ;//���ÿ�Ŀ����
    private CustomTree ftreeAcctJJ ;//���ÿ�Ŀ����s
    //��ž��ÿ�Ŀ�Ĺ�������
    private FScrollPane fsAcctJJPane = new FScrollPane();

    private IPrjDetail itservDetail; // ��Ŀ��ϸ�ӿ�

    private DataSet dsDetail; // ��Ŀ��ϸ���ݼ�

    /**
     * �ⲿ��ֵ���ı�TEXTFIELD�� ֵ
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
     * ���ñ༭״̬
     * 
     * @param iState
     *            �༭״̬
     */
    private void setViewState(int iState) {
        if (iState == 0) {
            // ���״̬
            Common.changeChildControlsEditMode(ContentPanel, false);
            tree.setEnabled(true);
            treeDetail.setEnabled(false);
            treeDetail.setIsCheckBoxEnabled(false);
            iTag = 0;
        }
        if (iState == 1) {
            // �༭״̬ 1:��� 2���޸�
            Common.changeChildControlsEditMode(ContentPanel, true);
            iTag = 1; 
            tree.setEnabled(false);
            treeDetail.setEnabled(true);
            treeDetail.setIsCheckBoxEnabled(true);
        }
        if (iState == 2) {
            // �༭״̬ 1:��� 2���޸�
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
     * ����ѡ���tree�����ڱ༭������ʾ��ص���Ϣ
     * 
     * @throws Exception
     */
    private void getData() throws Exception {
        String aName; // �������
        String aNote; // ���˵��
        String aDegree;// �����ϸ���̶�
        String aJJValue;// ���ÿ�Ŀ����
        String aSortID; // ������
        String aInputSet;// ����ֵ
        // ���������ݼ�Ϊ�գ���ѽ�����Ϣ���
        if (ds.isEmpty() || ds.eof() || ds.bof()) {
            setTextField("", "", "", "", "");
            return;
        }
        aName = ds.fieldByName(IPrjSort.PRJSORT_NAME).getString();
        aNote = Common.nonNullStr(ds.fieldByName(IPrjSort.NOTE).getValue());
        // �����עΪ��������Ϊ��
        aDegree = ds.fieldByName(IPrjSort.DETAIL_DEGREE).getString();
        // �����עΪ��������Ϊ��
        aJJValue = ds.fieldByName(IPrjSort.JJTYPE).getString();
        aSortID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
        aInputSet = ds.fieldByName("input_set_id").getString();
        if ((tree.getSelectedNode() == null) || (tree.getSelectedNode() == tree.getRoot())){
            // �����û��ѡ��ڵ��ѡ����ڵ㣬�������Ϣ���
            setTextField("", "", "", "", ""); 
            setAcctJJTreeSelect("");
        }
        else {
            // ���ÿ�����Ϣ�ڽ�����ʾ
            setTextField(aName, aNote, aDegree, aJJValue, aInputSet);
            // ��λ��������
            setTreeDetailSel(aSortID);
            //��Ӧ�ľ��ÿ�Ŀ
            setAcctJJTreeSelect(aSortID);
        }
    }

    /**
     * �����ʼ��
     */
    public void initize() {
        // �����
        pnlBase = new FSplitPane();
        pnlBase.setOrientation(1);
        pnlBase.setDividerLocation(300);
        try {
            // ��������Ԫ�ض��� 
            FPanel pnlLeft = new FPanel(); // �������
            // pnlLeft.setTitle("��ǰ��Ŀ����б�");
            RowPreferedLayout leftLayout = new RowPreferedLayout(1);
            leftLayout.setColumnWidth(200);
            pnlLeft.setLayout(leftLayout);
            // tree�ŵ�scrollPane�ϣ�ʹ������������
            FScrollPane treePanel = new FScrollPane();

            BeanFactory beanfac = BeanFactoryUtil
                    .getBeanFactory("gov/nbcs/rp/sys/prjsort/conf/PrjSortConf.xml");
            itserv = (IPrjSort) beanfac.getBean("rpPrjSortTreeService");
            ds = itserv.getDataset();

            pubserv = PubInterfaceStub.getMethod(); // ��ù����ӿڵĽӿ�
            SysCodeRule codeRule = itserv.getCodeRule(); // ��ȡ��Ŀ���Եı������

            tree = new CustomTree("��Ŀ���", ds, IPrjSort.PRJSORT_CODE,
                    IPrjSort.PRJSORT_NAME, null, codeRule);
             
            treePanel.addControl(tree);
            FLabel lbTreeName = new FLabel();
            lbTreeName.setText("    ��ǰ��Ŀ����б�");
            pnlLeft.addControl(lbTreeName, new TableConstraints(1, 1, false,
                    true));
            pnlLeft.add(treePanel, new TableConstraints(1, 1, true, true)); // ��������ϵ���Ŀ������

            FPanel RightPanel = new FPanel(); // �������
            RightPanel.setTitle("��ǰ��Ŀ�����ϸ��Ϣ");

            /* �ұ������Ԫ�ض��� */
            RowPreferedLayout RightLayout = new RowPreferedLayout(2); // ��Ϊ���� 
            RowPreferedLayout ContentLayout = new RowPreferedLayout(2); 
            tfName = new FTextField("��Ŀ�������"); // ��һ�е�һ��
            tfName.setProportion(0.3f);
            tfName.setMaxLength(50); 
            rgPrjSort = new FRadioGroup("", 1);
            rgPrjSort
                    .setRefModel("1#������ϸ+2#ֻ��д�����ÿ�Ŀ����Ŀ�������ݵ���Ŀ��ϸԤ��+3#ֻ����ÿ�Ŀ���ʽ���Դ����Ŀ��ϸԤ��+4#���߶���");
            rgPrjSort.setProportion(0); // 5
            FPanel rgPrjSortPanel = new FPanel();
            RowPreferedLayout rgPrjSortLayout = new RowPreferedLayout(2);
            rgPrjSortPanel.setLayout(rgPrjSortLayout);
            rgPrjSortPanel.setTitle("��Ŀ��ϸ���̶�");
            rgPrjSortPanel.addControl(rgPrjSort, new TableConstraints(4, 2,
                    true, true)); // ��ϸ���̶��б�
            // ��Ŀ���˵�� ռһ��һ�� �����е�һ��
            tfRemark = new FTextField("��Ŀ���˵��");
            tfRemark.setProportion(0.3f);
            tfRemark.setMaxLength(150);

            // ֧�����ÿ�Ŀ��Χ
            rgJJ = new FRadioGroup("", 0);
            rgJJ.setRefModel("1#����Ŀ��ϸ֧��+2#���о��ÿ�Ŀ");
            rgJJ.setProportion(0);
            FPanel pnlJJ = new FPanel();
            RowPreferedLayout layJJ = new RowPreferedLayout(1);
            pnlJJ.setLayout(layJJ);
            pnlJJ.setTitle("֧�����ÿ�Ŀ��Χ");
            pnlJJ.addControl(rgJJ, new TableConstraints(1, 1, false, true));
            
            //����  ֧�����ÿ�Ŀ��Χ �ĵ����¼�
            addAcctJJMouseListener();
            
            //���Ӿ��ÿ�Ŀ   
			ftreeAcctJJ = new CustomTree("���ÿ�Ŀ", null,
					IPubInterface.BSI_ID, IPubInterface.ACCT_JJ_FNAME,
					IPubInterface.BSI_PARENT_ID, null);
			ftreeAcctJJ.setSortKey(IPubInterface.ACCT_CODE_JJ); 
			ftreeAcctJJ.setIsCheck(true); 
			ftreeAcctJJ.setAutoscrolls(false);
			//���þ��ÿ�Ŀ
            setAcctJJTree(); 
            fsAcctJJPane.addControl(ftreeAcctJJ);
			pnlJJ.addControl(fsAcctJJPane, new TableConstraints(9, 1, false, true)); 

            // add by srm 2011-03-17 �������ѡ��
            cbInputSet = new FComboBox("�������ѡ��");
            cbInputSet.setProportion(0.3f);
            cbInputSet.setRefModel(itserv.GetInputSetString());

            /* �ұ���岼�� */
            ContentPanel = new FPanel();
            RightPanel.setLayout(RightLayout); // �����������Ϊ����
            ContentPanel.setLayout(ContentLayout); // �����������ַ�Ϊһ��

            /* ��������һ�в��� */
            ContentPanel.addControl(tfName, new TableConstraints(1, 2, false,
                    false)); // �ӵ�һ����Ŀ�������
            ContentPanel.addControl(rgPrjSortPanel, new TableConstraints(5, 2,
                    false, false)); // �ӵڶ�����Ŀ��ϸ���̶�
//          add by srm 2011-03-17 �����������ѡ��
            ContentPanel.addControl(cbInputSet, new TableConstraints(1, 2,
                    false, false)); 
            ContentPanel.addControl(pnlJJ, new TableConstraints(11, 2, false,
                    false));
            
            // ��Ŀ�������ϸ�Ķ�Ӧ��ϵ�ŵ���һ������ģ������ 

            // ���������ı߾� 
            RightPanel.setLeftInset(20);
            RightPanel.setTopInset(20);

            /* ���������沼�֣���һ�з����ݣ�������������Ϊ�� */
            RightPanel.addControl(ContentPanel, new TableConstraints(18, 2,
                    false, false));
            RightPanel.addControl(getDetailPanel(), new TableConstraints(10, 2,
                    true, false));
            pnlBase.addControl(pnlLeft);
            pnlBase.addControl(RightPanel);
            this.add(pnlBase);
            this.createToolBar();
            setViewState(0); // ��ʼ����ɰѱ༭�������Ϊ���״̬
            TreePath path = new TreePath(((DefaultTreeModel) tree.getModel())
                    .getPathToRoot(tree.getRoot()));
            tree.expandPath(path);
            addDetailListener();
            // ���任ʱҪ��ӵļ����¼�
            ds.addDataChangeListener(new DataChangeListener() {
                private static final long serialVersionUID = 1L;

                public void onDataChange(DataChangeEvent e) throws Exception {
                    if ((e.type() == DataChangeEvent.CURSOR_MOVED)
                            && (e.getSender() != null)
                            && ((e.getSender() instanceof TreeNode) || (e
                                    .getSender() instanceof DataSet))) {
                        // �ж����ݵĸı��Ƿ������������
                        // �������ݵ�������ʾ
                        getData();
                    }
                }
            });
            // ���������ɱ༭
            tree.setEditable(false);
            treeDetail.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ѡ����Ŀ��ϸ�����
     * 
     * @return
     */
    private FPanel getDetailPanel() throws Exception {
        FPanel pnl = new FPanel();
        pnl.setTitle("��Ŀ��ϸ�б�");
        RowPreferedLayout layDetail = new RowPreferedLayout(1);
        pnl.setLayout(layDetail);
        FScrollPane pnlDetailTree = new FScrollPane();
        pnlDetailTree.addControl(getDetailTree());
        pnl.addControl(pnlDetailTree, new TableConstraints(1, 1, true, true));
        return pnl;
    }

    /**
     * �����Ŀ��ϸ���ݼ�
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
                    null); // ��Ŀ��ϸ�б�
        }
        return dsDetail;
    }

    /**
     * �����Ŀ������ϸ���ݼ� 
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
     * ����ѡ��������൵ 
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
            // ѡ��ѡ��Ľڵ�
            if (nodes[i] != root) {
                // ���˵����ڵ�
                // ѡ��ýڵ��detail_code
                // ,��sort_to_detail��Ƚ�,������,���¼sort_detail_code��bmk,��������
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
        // ����ѡ����ϸ�ڵ���Ϣ������ 
        itserv.ExecuteSql(list);
        String sFilter = "PRJSORT_CODE='" + sortCode + "'";
        dsSortToDetail = itservDetail
                .getDataset("fb_p_sort_to_detail", sFilter);
        dsSortToDetail.applyUpdate(); 
    }
    
    
    /**
     * ��ȡר�����Ծ��ÿ�Ŀ�����ڱ���
     * 
     * @throws Exception
     */
    private List getPrjSortToAcctJJ(String PRJSORT_CODE) throws Exception { 
    	//�������еĽڵ㣬��Ϊר��¼��ʱ����ֱ�Ӵ������ڵ�
        MyTreeNode[] nodes = ftreeAcctJJ.getSelectedNodes(false); 
        List list = new ArrayList(); 
        for (int i = 0; i < nodes.length; i++) {
            // ѡ��ѡ��Ľڵ� 
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
     * ����������ϸ��ѡ��
     * 
     * @throws Exception
     */
    private void setTreeDetailSel(String sortID) throws Exception {
        dsSortToDetail = getDsSortToDetail(sortID);
        // ����TREE
        MyTreeNode root = (MyTreeNode) treeDetail.getRoot(); // ���ڵ�
        Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
        while (enumeration.hasMoreElements()) { // ��ʼ����
            MyTreeNode node = (MyTreeNode) enumeration.nextElement();
            if (node != root) { // ���˵����ڵ�
                String bk1 = node.getBookmark(); // ��ȡ�ýڵ��BOOKMARK
                dsDetail.gotoBookmark(bk1);
                String sCode = dsDetail.fieldByName(IPrjDetail.DETAIL_CODE)
                        .getString();
                if (dsSortToDetail.isEmpty()) {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(false); // ���øýڵ�Ϊѡȡ״̬
                    continue;
                }
                String[] sDCodes = getDataSelDetailCode();
                Arrays.sort(sDCodes);
                int idex = Arrays.binarySearch(sDCodes, sCode);
                if (idex >= 0) {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(true); // ���øýڵ�Ϊѡȡ״̬
                } else {
                    PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
                    pNode.setIsSelect(false); // ���øýڵ�Ϊѡȡ״̬
                }
            }
        }
        treeDetail.repaint();
    }

    /**
     * ��ȡ����sort��Ӧ��������ϸ���ݼ����������ϸ����
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
     * ѡ����Ŀ��ϸ��
     * 
     * @return
     * @throws Exception
     */
    private CustomTree getDetailTree() throws Exception {
        if (treeDetail == null) {
            treeDetail = new CustomTree("��Ŀ��ϸ", getDsDtail(),
                    IPrjDetail.DETAIL_ID, IPrjDetail.DETAIL_NAME,
                    IPrjDetail.PAR_ID, null);
            treeDetail.setIsCheckBoxEnabled(true);
            treeDetail.setIsCheck(true);
            treeDetail.reset();
        }
        return treeDetail;
    }

    /**
     * ��Ӳ���
     */
    public void doAdd() {
        // ��Ӳ���
        MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
        if (nodeSel == null) {
            JOptionPane.showMessageDialog(null, "��ѡ��ڵ�");
            return;
        }
        // ���ɱ�������
        if (!ds.isEmpty() && !ds.eof() && !ds.bof()) {
			bmk = ds.toogleBookmark();
		}
        String sFilter = null; // ��������
        try { // �޸ĸ��ڵ��endflag��Ϣ
            // ���ɱ�������
            if ((tree.getSelectedNode() != tree.getRoot())
                    && (tree.getSelectedNode().getChildCount() <= 0)) {
				//                if (!PrjDetailAction.checkUseInPrj(
//                        PrjDetailAction.check_PrjSort, ds.fieldByName(
//                                IPrjSort.PRJSORT_CODE).getString())) {
//                    JOptionPane.showMessageDialog(null, "����Ŀ����Ѿ���ʹ��");
//                    return;
//                }
				if (tree.getSelectedNode() == tree.getRoot()) {
				    // ���ѡ����Ǹ��ڵ�
				    sParentID = "";
				    sPreFName = ""; // �ýڵ��FPreNAMEΪ��
				    sFilter = IPrjSort.SET_YEAR + "=" + Global.loginYear
				            + " AND trim(" + IPrjSort.PAR_ID + ") IS NULL";
				} else {
				    // ���ѡ��Ĳ��Ǹ��ڵ�
				    // ��ȡ��ǰ����Ϊ���ڵ�
				    sParentID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
				    // ����ȫ��
				    sPreFName = ds.fieldByName(IPrjSort.PRJSORT_FNAME).getString();
				    if (Common.isNullStr(sPreFName)
				            || "null".equalsIgnoreCase(sPreFName)) {
						// ������ڵ��fnameΪ�գ��򽫸��ڵ�name������
				        sPreFName = ds.fieldByName(IPrjSort.PRJSORT_NAME)
				                .getString()
				                + "_";
					} else {
						// ������ڵ�fname��Ϊ�գ��򽫸��ڵ��fname������
				        sPreFName = sPreFName + "_";
					}
				    // ���ɻ�ȡ�ڵ����Ĺ�������
				    sFilter = IPrjSort.SET_YEAR + "=" + Global.loginYear + " AND "
				            + IPrjSort.PAR_ID + "=" + "'" + sParentID + "'"; // ��������
				}
			}
            // ����������
            sNodeID = pubserv.getNodeID(IPrjSort.Table_Name,
                    IPrjSort.PRJSORT_CODE, sParentID, sFilter, itserv
                            .getCodeRule());
            if (sNodeID == null) {
                JOptionPane.showMessageDialog(this, "���ݱ���������ɵı���Ϊ��\n��������������");
                return;
            }
            if (sNodeID.indexOf("0")==0){
            	sNodeID = "7"+ sNodeID.substring(1,sNodeID.length());
            }
            // ������ˮID
            int maxID = itserv.getMaxID();
            sSortID = Common.getStrID(new BigDecimal(maxID),
                    IPrjSort.iPrjSortIDLength);
            ds.maskDataChange(true);
            ds.append();
            setTextField("", "", "", "", "");
            JRadioButton radios[] = rgPrjSort.getRadios();
            radios[0].setSelected(true);
            setViewState(1); // ����Ϊ�༭״̬
            rgJJ.setEditable(false);
            rgJJ.setEnabled(false);
            tfName.setFocus();
            ds.maskDataChange(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ������
     */
    public void doDelete() {
        // ɾ������
        try {
            MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
            if ((nodeSel == null) || (nodeSel == tree.getRoot())) {
                JOptionPane.showMessageDialog(null, "��ѡ��ڵ�");
                return;
            }
            if (nodeSel.getChildCount() > 0) {
                JOptionPane.showMessageDialog(null, "����ɾ���ӽڵ�");
                return;
            }
            // ���ɱ�������
            if (!ds.isEmpty() && !ds.eof() && !ds.bof()) {
				bmk = ds.toogleBookmark();
			} else {
				return;
			}
//            if (!PrjDetailAction.checkUseInPrj(PrjDetailAction.check_PrjSort,
//                    ds.fieldByName(IPrjSort.PRJSORT_CODE).getString())) {
//                JOptionPane.showMessageDialog(null, "����Ŀ����Ѿ���ʹ��");
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
            // ���ݸýڵ㱣���丸�׽ڵ�
            MyTreeNode ParentNode = tree.getSelectedNode();
            // �����丸�ڵ�
            if ((ParentNode != null) && (ParentNode != tree.getRoot())) {
                String sPreID = ds.fieldByName(IPrjSort.PAR_ID).getString();
                // dataset�︸�ڵ���Ϣ
                if (tree.getSelectedNode().getChildCount() == 0) {
                    // ����ýڵ����ӽڵ�
                    if (JOptionPane.showConfirmDialog(null, "�Ƿ�ȷ��ɾ����Ϣ", "��ʾ��Ϣ",
                            2) == 0) {
                        // �Ƿ�ȷ��ɾ����Ϣ
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
					JOptionPane.showMessageDialog(null, "����ɾ���ӽڵ�");
				}
            }
            ds.maskDataChange(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ȡ������
     */
    public void doCancel() {
        // ȡ������
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
     * �޸Ĳ���
     */
    public void doModify() {
        // �޸Ĳ���
        MyTreeNode nodeSel = (MyTreeNode) tree.getSelectedNode();
        if ((nodeSel == null) || (nodeSel == tree.getRoot())) {
            JOptionPane.showMessageDialog(null, "��ѡ��ڵ�");
            return;
        }
        // ���ɱ�������
        try {
//            if (!PrjDetailAction.checkUseInPrj(PrjDetailAction.check_PrjSort,
//                    ds.fieldByName(IPrjSort.PRJSORT_CODE).getString())) {
//                JOptionPane.showMessageDialog(null, "����Ŀ����Ѿ���ʹ��");
//                return;
//            }
            if ((tree.getSelectedNode() != null)
                    && (tree.getSelectedNode() != tree.getRoot())) {
                // ���ѡ���˽ڵ㲢��ѡ����ǲ����ڵ���ִ���޸Ĳ���
                // ����dataset��ǰ��bookmark
                if (tree.getSelectedNode().getParent() == tree.getRoot()) {
					sPreFName = ""; // �ýڵ��FPreNAMEΪ��
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
                // ����Ϊ�༭״̬
                setViewState(2);
                sNodeID = ds.fieldByName(IPrjSort.PRJSORT_CODE).getString();
                sSortID = ds.fieldByName(IPrjSort.PRJSORT_ID).getString();
                sParentID = ds.fieldByName(IPrjSort.PAR_ID).getString();
                String sDValue = ds.fieldByName(IPrjSort.DETAIL_DEGREE)
                        .getString();
                // ��¼��ʱ����Ŀ������ƣ��ͺ�����޸ĺ����Ŀ����������Ƚ�
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
     * �������
     */
    public void doSave() {
        // ����
        ds.maskDataChange(true);
        String bmk = ds.toogleBookmark();
        String sNodeIDNew = sNodeID;
        if (Common.isNullStr(Common.nonNullStr(tfName.getValue()))) {
            JOptionPane.showMessageDialog(null, "������Ʋ���Ϊ��");
            return;
        }
        // add by srm 2011-03-17 ����Ҫ����������
        if (Common.isNullStr(Common.nonNullStr(cbInputSet.getValue()))) {
            JOptionPane.showMessageDialog(null, "������ò���Ϊ��");
            return;
        }
        try {
            // ��ȡ��ϸ
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
                // ���ѡ��ҽ���ϸ�������ѡ����ϸ
                MyTreeNode[] nodes = this.treeDetail.getSelectedNodes(true);
                if (nodes.length == 0) {
                    JOptionPane.showMessageDialog(pnlBase, "����ҽ���ϸ");
                    return;
                }
            }
            if ((iTag == 1) || (iTag == 2)) {
                // ��ǰʱ�༭״̬�ű���
                // ���浱ǰ�ڵ���Ϣ
                // �����ɵı��봫��dataset
                ds.fieldByName(IPrjSort.PRJSORT_CODE).setValue(sNodeID);
                ds.fieldByName(IPrjSort.PRJSORT_ID).setValue(sSortID);
                // ȫ��
                ds.fieldByName(IPrjSort.PRJSORT_FNAME).setValue(
                        sPreFName + tfName.getValue().toString());
                // �޸ĺ����Ŀ�������
                sSortNameNew = tfName.getValue().toString();
                ds.fieldByName(IPrjSort.PRJSORT_NAME).setValue(sSortNameNew);
                ds.fieldByName(IPrjSort.NOTE).setValue(tfRemark.getValue());
                ds.fieldByName(IPrjSort.DETAIL_DEGREE).setValue(
                        String.valueOf(value));
                ds.fieldByName("rg_code").setValue(Global.getCurrRegion());
                // ��ȡ��ϸ
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
                // �޸ĸ��ڵ�end_flag��Ϣ
                if (!Common.isNullStr(ds.fieldByName(IPrjSort.PAR_ID)
                        .getString())) {
                    // ����ýڵ��и��ڵ㣬���޸��丸�׽ڵ��end_flag��ʶ
                    ds.locate(IPrjSort.PRJSORT_CODE, ds.fieldByName(
                            IPrjSort.PAR_ID).getString());
                    ds.edit();
                    ds.fieldByName(IPrjSort.END_FLAG).setValue("0");
                    ds.gotoBookmark(bmkAdd);
                }
                if ((iTag == 2) && !sSortNameOld.equals(sSortNameNew)) {
                    // ������޸�״̬�����漰���޸��丸�ڵ�����ƣ����������ӽڵ��fname��Ҫ�����ı�
                    ds.gotoBookmark(bmk);
                    // ��¼��ǰ��ѡ�ڵ�
                    MyTreeNode parentNode = tree.getSelectedNode();
                    Enumeration allNodes = parentNode.breadthFirstEnumeration();
                    while (allNodes.hasMoreElements()) {
                        // ѭ����ǰ�ڵ�������ӽڵ�
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
                // ������Ϣ����������
                itserv.dsPost(ds);
                ds.applyUpdate();
                if ((value != 1) && (value!=3)){
                    saveDsSortToDetail(bmk); 
                }
                
                //����ר�����Ծ��ÿ�Ŀ
                //ֻ����  ���о��ÿ�Ŀ������²ű���
                if("2".equals(rgJJ.getValue())){ 
                    String sortCode = ds.fieldByName("PRJSORT_Code").getString();
                    itserv.savePrjSortToAcctJJ(sortCode,getPrjSortToAcctJJ(sortCode)); 
                }
                
                // ����Ϊ���״̬
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
     * ��ȡ���ÿ�Ŀ�����Ѿ�ѡ��Ҳ���
     */
    public void setAcctJJTree(){
    	//���ÿ�Ŀ 
		try {
			dsAcctJJ = itserv.getPrjAcctJJ(GlobalEx.loginYear);
			ftreeAcctJJ.setDataSet(dsAcctJJ);
			ftreeAcctJJ.reset();
		} catch (Exception e) { 
			e.printStackTrace();
		}  
    }
    
    /**
     * ���þ��ÿ�Ŀ��Ӧ��ѡ�� 
     * @throws Exception
     */
    private void setAcctJJTreeSelect(String PRJSORT_CODE) throws Exception {
    	//�����
    	ftreeAcctJJ.cancelSelected(false);
    	if((PRJSORT_CODE==null)||"".equals(PRJSORT_CODE)) {
			return;
		}
    	//��ȫ�����
    	//��ȡ�Ѿ�ѡ��ľ��ÿ�Ŀ 
        DataSet dsPrjToAcctJJ = itserv.getPrjSortToAcctJJ(PRJSORT_CODE);
        if (dsAcctJJ.isEmpty()) {
			return;
		}
        // ����TREE
        MyTreeNode root = (MyTreeNode) ftreeAcctJJ.getRoot(); // ���ڵ�
        Enumeration enumeration = root.breadthFirstEnumeration(); // ������ȱ�����ö�ٱ���
        while (enumeration.hasMoreElements()) { // ��ʼ����
            MyTreeNode node = (MyTreeNode) enumeration.nextElement();
            if (node != root) { // ���˵����ڵ�
                String bk1 = node.getBookmark(); // ��ȡ�ýڵ��BOOKMARK  
                if(dsAcctJJ.gotoBookmark(bk1)){
                	//�����Ƿ����
                	dsPrjToAcctJJ.beforeFirst();
                	while(dsPrjToAcctJJ.next()){
                		if(dsPrjToAcctJJ.fieldByName("ACCT_CODE_JJ").getValue().equals(dsAcctJJ.fieldByName("ACCT_CODE_JJ").getValue())){
                			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); 
                            pNode.setIsSelect(true); // ���øýڵ�Ϊѡȡ״̬
                		}
                	} 
                }  
            }
        }
        ftreeAcctJJ.repaint();
    }
    
    /**
     * ���� ֧�����ÿ�Ŀ��Χ �����¼�
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
     * ���ð�ť״̬
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
            if ("�ر�".equals(btnGet.getText())) {
                btnGet.setEnabled(true);
            }
            if ("����".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("�޸�".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("ɾ��".equals(btnGet.getText())) {
                btnGet.setEnabled(isEditEnable);
            }
            if ("����".equals(btnGet.getText())) {
                btnGet.setEnabled(isSaveEnable);
            }
            if ("ȡ��".equals(btnGet.getText())) {
                btnGet.setEnabled(isSaveEnable);
            }

        }
    }

    /**
     * �رյ�ǰ���
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
     * ���ƾ��ÿ�Ŀ��Χ�Ƿ���ʾ
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
     * ���þ��ÿ�Ŀ��Χ����ʾ
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
     * ��ϸ���ļ����¼�
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
