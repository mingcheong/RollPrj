package gov.nbcs.rp.dicinfo.prjdetail.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.springframework.beans.factory.BeanFactory;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.action.ActionedUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.list.CustomComboBox;
import gov.nbcs.rp.common.ui.list.MyListElement;
import gov.nbcs.rp.common.ui.report.HeaderUtility;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import gov.nbcs.rp.common.ui.report.cell.Cell;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.dicinfo.prjdetail.action.PrjDetailAction;
import gov.nbcs.rp.dicinfo.prjdetail.ibs.IPrjDetail;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FIntegerField;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.framework.systemmanager.FFrame;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.fr.cell.CellSelection;
import com.fr.report.CellElement;
import com.fr.report.PaperSize;
import com.fr.report.io.ExcelImporter;

/**
 * <p>
 * Title:项目明细界面
 * </p>
 * <p>
 * Description:项目明细功能界面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class PrjDetailUI extends FModulePanel implements ActionedUI {

    private static final long serialVersionUID = 1L;

//    private IFormulaOp formulaOp; // 计算公式界面的接口

    private IPrjDetail itserv; // 定额管理的接口

    private IPubInterface pubserv;// 公用接口

    // 数据集
    private DataSet dsCol; // 列信息的 dataset, 数据集根据明细表的改变而改变

    private DataSet dsDetail; // 项目明细的dataset

    private DataSet dsComboDP; // 显示风格的编辑匡

    private PrjDetailAction PrjAction; // 公用函数类

    private int cellFirstTag = 0; // 是否为第一个单元格

    private String[] bmkDel; // 删除的列信息的编号

    private int iDelCount = 0; // 删除记录的个数

    private Object iColTypeSel; // 已经选择的列类型

    private String bmkDetailChange; // dsDetail在改变时当时的位置

    // 编码规则
    private SysCodeRule codeRule; // 明细表的编码规则

    private SysCodeRule codeRule_Col; // 列信息的编码规则

    private SysCodeRule lvlRule; // 级次码的编码规则

    // 状态控制
    // 几种编辑状态
    private int iState = 0; // 当前状态

    private final static int state_Add = 1; // 添加状态COL

    private final static int state_Delete = 3; // 删除状态COL

    private final static int state_Browse = 0; // 浏览状态COL

    private int iState_Detail = 0;

    private final static int state_AddDetail = 4; // 添加 detail

    private final static int state_EditDetail = 5; // 修改 detail

    // 界面元素
    private JFileChooser fileChooser; // 文件选择器

    private ReportUI reportUI; //

    private Node node; // 创建列信息表头时获得的节点信息

    private Cell cellOld; // 点之前保存那个单元格

    private CustomTree treePrjDetail; // 项目明细的tree

    private Report tablePrjDetail; //

    private FTextField textfieldFormula; //

    private FTextField tfName; //

    private FTextField tfPrjDetailName; // 明细表名称

    private FTextField tfColName; // 列名称

    private FTextField tfColFName; // 列全名

    private FRadioGroup rgColType; // 列类型

    private DataSourceRadioGroup rgFieldKind; // 数据来源

    private FTextField tfFormula; // 计算公式

    private FIntegerField tfPreferenceLevel; // 优先级

    private FTextField tfSelValue; // 选取值列表

    private CustomComboBox cbDisplayStyle; // 显示风格

    private JCheckBox chkbIsInput; // 该列必须输入内容

    private CustomComboBox cbColTypeStandard; // 列标准类型

    private FButton btnAdd; // 增加列button

    private FButton btnAddChild; // 增加子级节点列的button

    private FButton btnDelete; // 删除列button

    private FButton btnShowFormulaPanel; // 弹出公式编辑界面

    private FButton btnReadIn; // 导入按钮

    private FTextField tfAcct; // 显示经济科目的编辑匡

    private FButton btnAcct; // 选择经济科目的按钮

    private FSplitPane pnlBase; // 主面板
    
    private int iDefType = -1;  //经济科目的类别 从弹出面板里获取

    /**
     * 设置当前界面元素状态
     * 
     * @param aState
     *            状态
     */
    private void setViewState(int aState) throws Exception {
        setButtonState(aState);
        iState = aState;
        if (aState == state_Browse) {

            textfieldFormula.setEditable(false);

            treePrjDetail.setEnabled(true); // 项目明细的tree

            tfAcct.setEditable(false);

            btnAcct.setEnabled(false);

            tfPrjDetailName.setEditable(false); // 明细表名称

            tfColName.setEditable(false);

            rgColType.setEditable(false); // 列类型

            rgFieldKind.setEditable(false); // 数据来源

            rgFieldKind.setEditable(false);

            tfPreferenceLevel.setEditable(false); // 优先级

            cbDisplayStyle.setEnabled(false); // 显示风格

            chkbIsInput.setEnabled(false); // 该列必须输入内容

            cbColTypeStandard.setEnabled(false); // 列标准类型

            btnAdd.setEnabled(false); // 增加列button

            btnAddChild.setEnabled(false); // 增加子级节点列的button

            btnDelete.setEnabled(false); // 删除列button

            btnShowFormulaPanel.setEnabled(false); // 弹出公式编辑界面

            btnReadIn.setEnabled(false);

        } else {
            treePrjDetail.setEnabled(false); // 项目明细的tree

            tfPrjDetailName.setEditable(true); // 明细表名称

            tfAcct.setEditable(false);

            btnAcct.setEnabled(true);

            tfColName.setEditable(true); // 列全名

            rgColType.setEditable(true); // 列类型

            rgFieldKind.setEditable(true); // 数据来源

            rgFieldKind.setEditable(true);

            tfPreferenceLevel.setEditable(true); // 优先级

            cbDisplayStyle.setEnabled(true); // 显示风格

            chkbIsInput.setEnabled(true); // 该列必须输入内容

            cbColTypeStandard.setEnabled(true); // 列标准类型

            btnAdd.setEnabled(true); // 增加列button

            btnAddChild.setEnabled(true); // 增加子级节点列的button

            btnDelete.setEnabled(true); // 删除列button

            btnShowFormulaPanel.setEnabled(true); // 弹出公式编辑界面

            btnReadIn.setEnabled(true);
        }
    }

    /**
     * 界面初始化函数
     */
    public void initize() {
        try {
            PrjAction = new PrjDetailAction();
            // 主panel的布局方式
            pnlBase = new FSplitPane(); // 功能模块最底层界面
            pnlBase.setOrientation(1);
            pnlBase.setDividerLocation(200);
            /* 面板元素定义 */
            // 左面的项目明细列表面板
            FPanel pnlLeft = new FPanel();
            RowPreferedLayout layLeft = new RowPreferedLayout(1);
            pnlLeft.setLayout(layLeft);
            FScrollPane scrpTree = new FScrollPane();

            pubserv = PubInterfaceStub.getMethod();

            BeanFactory beanfac = BeanFactoryUtil
                    .getBeanFactory("com/foundercy/fiscalbudget/dicinfo/prjdetail/conf/PrjDetailConf.xml");
            itserv = (IPrjDetail) beanfac.getBean("bmys.PrjDetailTreeService");
            dsDetail = itserv.getDataset(IPrjDetail.Table_SIMP_MASTER, null); // 项目明细列表
            dsComboDP = itserv.getComboDPDataset(); // 显示风格
            DataSet dsComboSC = itserv.getComboSCDataset(); // 获取标准列
            codeRule = itserv.getCodeRule(IPrjDetail.DETAILTAB_CODE); // 获得编码规则
            codeRule_Col = itserv.getCodeRule(IPrjDetail.DETAILTABITEM_CODE);
            lvlRule = UntPub.lvlRule;
            treePrjDetail = new CustomTree("项目明细", dsDetail,
                    IPrjDetail.DETAIL_ID, IPrjDetail.DETAIL_NAME, null,
                    codeRule);
            // 右面底层面板的总体定义及布局方式
            FPanel pnlRight = new FPanel(); // 右面的项目明细信息显示信息的底层面板
            RowPreferedLayout layRight = new RowPreferedLayout(1);
            layRight.setColumnWidth(700);
            pnlRight.setLayout(layRight);
            layRight.setRowGap(4);

            // 右面上面面板的总体布局及元素定义
            FPanel pnlRTop = new FPanel(); // 右面面板，分为上下两大部分,上半部分为主信息
            RowPreferedLayout layRTop = new RowPreferedLayout(10);
            pnlRTop.setLayout(layRTop); // 分一列
            tfPrjDetailName = new FTextField("明细表名称");
            this.tfName = tfPrjDetailName;
            tfPrjDetailName.setProportion(0.25f);
            FLabel lbPrjDetail = new FLabel();
            lbPrjDetail.setText("当前项目明细表明细信息");

            // 显示明细表列 table的panel
            FPanel pnlDetailInfo = getTablePanel();

            tablePrjDetail = new Report();
            reportUI = new ReportUI(tablePrjDetail);
            initReport();

            FPanel pnlTable = new FPanel();
            RowPreferedLayout layTable = new RowPreferedLayout(5);
            pnlTable.setLayout(layTable);
            pnlTable.add(reportUI, new TableConstraints(5, 4, true, true));

            btnReadIn = new FButton("bt1", "从文件读取");
            btnReadIn.addActionListener(new ImportBtnActionListener());
            pnlDetailInfo.addControl(pnlTable, new TableConstraints(6, 5,
                    false, false));

            // 加该模块中的BUTTON
            FPanel pnlBlank1 = new FPanel();
            FPanel pnlBlank2 = new FPanel();
            pnlDetailInfo.addControl(pnlBlank1, new TableConstraints(1, 2,
                    false, false));
            pnlDetailInfo.addControl(btnReadIn, new TableConstraints(1, 1,
                    false, false));
            pnlDetailInfo.addControl(pnlBlank2, new TableConstraints(1, 2,
                    false));

            // 右面下面面板的元素定义及总体布局方式
            FPanel pnlURight = new FPanel(); // 右面面板，下半部分为明细列部分
            pnlURight.setTitle("明细列信息");
            pnlURight.setFontSize(12);
            pnlURight.setFontName("宋体");
            pnlURight.setBorder();
            RowPreferedLayout layRUnder = new RowPreferedLayout(6);
            pnlURight.setLayout(layRUnder);
            layRUnder.setRowGap(10);
            tfColName = new FTextField("列名称");
            tfColName.setProportion(0.3f);
            tfColFName = new FTextField("列全称");
            tfColFName.setProportion(0.3f);
            tfColFName.setEditable(false);
            FLabel lbTableTitle = new FLabel();
            lbTableTitle.setText("多层表头字以‘.’分割");

            // 列类型radio选择小面板
            FPanel pnlColType = new FPanel();
            pnlColType.setTitle("列类型");
            RowPreferedLayout layColType = new RowPreferedLayout(1);
            pnlColType.setLayout(layColType);
            rgColType = new FRadioGroup("", 0);
            rgColType.setRefModel("1#带小数的数值+2#整数数值+3#文本+4#日期");
            pnlColType.addControl(rgColType, new TableConstraints(1, 10, true,
                    true));

            // 数据来源 panel
            FPanel pnlDataSource = new FPanel();
            pnlDataSource.setTitle("数据来源");
            RowPreferedLayout layDataSource = new RowPreferedLayout(1);
            pnlDataSource.setLayout(layDataSource);

            // 包括计算公式和选取值的panel
            FPanel pnlCalSel = new FPanel(); // 底层
            RowPreferedLayout layCalSel = new RowPreferedLayout(1);
            pnlCalSel.setLayout(layCalSel);
            FPanel pnlCalculate = new FPanel(); // 计算公式panel **
            pnlCalculate.setTitle("计算公式");
            pnlCalculate.setFontSize(12);
            pnlCalculate.setFontName("宋体");
            pnlCalculate.setTitledBorder();
            RowPreferedLayout layCalculate = new RowPreferedLayout(6);
            pnlCalculate.setLayout(layCalculate);
            tfFormula = new FTextField(""); // 计算公式录入框
            tfFormula.setProportion(0.1f);
            tfFormula.setEditable(false);
            tfPreferenceLevel = new FIntegerField("优先级");
            tfPreferenceLevel.setProportion(0.4f);
            // tfPreferenceLevel.setValue( "0" );
            this.textfieldFormula = tfFormula; // 将公式录入框赋给局部变量
            btnShowFormulaPanel = new FButton("bjs", "…");
            // 添加按钮的监听事件，弹出计算公式设置界面
            btnShowFormulaPanel
                    .addActionListener(new ShowFormulaPanelActionListener());
            FPanel pnlSelValue = new FPanel(); // 选取值panel **
            pnlSelValue.setTitle("选取值列表（以英文分号分割多个值，如'一类信息;二类信息'表示该列有两个选项值）");
            RowPreferedLayout laySelValue = new RowPreferedLayout(4);
            pnlSelValue.setLayout(laySelValue);
            tfSelValue = new FTextField(""); // 选取值的编辑框
            tfSelValue.setProportion(0.05f);
            tfSelValue.setEditable(false);
            // 计算公式和选取值的panel 布局
            pnlCalculate.addControl(tfFormula,
                    new TableConstraints(1, 3, false));
            pnlCalculate.addControl(btnShowFormulaPanel, new TableConstraints(
                    1, 1, false));
            pnlCalculate.addControl(tfPreferenceLevel, new TableConstraints(1,
                    2, false));
            pnlSelValue.addControl(tfSelValue, new TableConstraints(1, 3,
                    false, false));
            pnlCalSel.addControl(pnlCalculate,
                    new TableConstraints(2, 1, false));
            pnlCalSel
                    .addControl(pnlSelValue, new TableConstraints(2, 1, false));

            // 通过调用方法实现数据来源的radiogroup的实现以及它的监听事件的实现
            rgFieldKind = new DataSourceRadioGroup("", "1#录入+2#计算+3#选取",
                    pnlCalculate, pnlSelValue, tfPreferenceLevel, tfFormula,
                    tfSelValue);
            pnlDataSource.addControl(rgFieldKind, new TableConstraints(3, 1,
                    true, true));
            // 包括数据来源与计算公式的面板
            FPanel pnlDataSourceFormula = new FPanel();
            RowPreferedLayout layDataSourceFormula = new RowPreferedLayout(6);
            pnlDataSourceFormula.setLayout(layDataSourceFormula);
            pnlDataSourceFormula.setEnabled(false);

            pnlDataSourceFormula.addControl(pnlDataSource,
                    new TableConstraints(4, 1, false));
            pnlDataSourceFormula.addControl(pnlCalSel, new TableConstraints(6,
                    5, true));

            // 剩下最下面两行的信息
            cbDisplayStyle = new CustomComboBox(dsComboDP, "NAME", "NAME");
            cbDisplayStyle.setTitle("显示风格");
            cbDisplayStyle.setProportion(0.2f);

            chkbIsInput = new JCheckBox("该列必须输入内容");
            // chkbIsInput.setProportion(0.5f);

            cbColTypeStandard = new CustomComboBox(dsComboSC, "CODE", "NAME");
            cbColTypeStandard.setTitle("列标准类型");
            cbColTypeStandard.setProportion(0.2f);

            btnAdd = new FButton("bt2", "增加本级节点");
            btnAddChild = new FButton("bt3", "增加子级节点");
            btnDelete = new FButton("bt4", "删除");

            FPanel BlankPanel = new FPanel();
            FPanel BlankPanel1 = new FPanel();

            // 该两行的布局
            FPanel pnlLast = new FPanel();
            RowPreferedLayout layLast = new RowPreferedLayout(8);
            pnlLast.setLayout(layLast);
            layLast.setRowGap(13);
            pnlLast.addControl(cbDisplayStyle, new TableConstraints(1, 2,
                    false, false));
            FPanel pnlBlank = new FPanel();
            pnlLast.addControl(cbColTypeStandard, new TableConstraints(1, 2,
                    false, false));
            pnlLast.addControl(pnlBlank, new TableConstraints(1, 4, false,
                    false));
            pnlLast.add(chkbIsInput, new TableConstraints(1, 2, false, false));
            pnlLast.addControl(pnlBlank, new TableConstraints(1, 6, false,
                    false));

            pnlLast.addControl(BlankPanel, new TableConstraints(1, 1, false,
                    false));
            pnlLast
                    .addControl(btnAdd,
                            new TableConstraints(1, 1, false, false));
            pnlLast.addControl(btnAddChild, new TableConstraints(1, 1, false,
                    false));
            pnlLast.addControl(btnDelete, new TableConstraints(1, 1, false,
                    false));
            pnlLast.addControl(BlankPanel1, new TableConstraints(1, 1, false,
                    false));

            // 经济科目 vw_fb_acct_economy_prj
            tfAcct = new FTextField();
            tfAcct.setTitle("经济科目");
            tfAcct.setProportion(0.20f);
            btnAcct = new FButton("btnacct", "选择经济科目");
            /* 界面布局 */
            // 右面上半部分面板的总体布局
            pnlRTop.addControl(tfPrjDetailName, new TableConstraints(1, 4,
                    false));
            pnlRTop.addControl(tfAcct, new TableConstraints(1, 4, false));
            pnlRTop.addControl(btnAcct, new TableConstraints(1, 2, false));
            pnlRTop.addControl(lbPrjDetail, new TableConstraints(1, 10, false));
            pnlRTop.addControl(pnlDetailInfo,
                    new TableConstraints(12, 10, true));

            // 右面下面面板的总体布局
            pnlURight.addControl(tfColName, new TableConstraints(1, 2, false,
                    true));
            pnlURight.addControl(tfColFName, new TableConstraints(1, 2, false,
                    true));
            pnlURight.addControl(lbTableTitle, new TableConstraints(1, 2,
                    false, true));
            pnlURight.addControl(pnlColType, new TableConstraints(2, 6, false,
                    true));
            pnlURight.addControl(pnlDataSourceFormula, new TableConstraints(4,
                    6, false, true));
            pnlURight.addControl(pnlLast, new TableConstraints(5, 6, false,
                    true));

            // 总体布局
            scrpTree.addControl(treePrjDetail);
            FLabel lbTreeName = new FLabel();
            lbTreeName.setText("        项目明细");
            pnlLeft.addControl(lbTreeName, new TableConstraints(1, 1, false,
                    true));
            pnlLeft.addControl(scrpTree, new TableConstraints(25, 1, false,
                    true));
            pnlRight.add(pnlRTop, new TableConstraints(10, 1, false, false));

            pnlRight.add(pnlURight, new TableConstraints(27, 1, false, true));
            pnlBase.addControl(pnlLeft);
            FPanel pnlRBasic = new FPanel();
            RowPreferedLayout layRBasic = new RowPreferedLayout(8);

            pnlRBasic.setLayout(layRBasic);
            pnlRBasic.addControl(pnlRight, new TableConstraints(25, 7, true,
                    true));
            // 改变pnlRBsic的间距
            pnlRBasic.setLeftInset(20);
            pnlRBasic.setTopInset(20);
            pnlBase.addControl(pnlRBasic);
            this.add(pnlBase);
            this.createToolBar();
            // dsDetail.addDataChangeListener(new dsDetailDataChangeListener());
            this.treePrjDetail.addMouseListener(new DetailTreeClickListener());
            // reportUI.getGrid().addMouseListener( new tableColListener() );
            btnAdd.addActionListener(new addColActionListener());
            btnAddChild.addActionListener(new addChildColActionListener());
            btnDelete.addActionListener(new delColActionListener());
            reportUI.getGrid().addMouseListener(new FillToColDatasetListener());
            tfColName.getEditor()
                    .addKeyListener(new CNameValueChangeListener());
            setViewState(state_Browse);
            // TreePath path = new TreePath(((DefaultTreeModel) treePrjDetail
            // .getModel()).getPathToRoot(treePrjDetail.getRoot()));
            // treePrjDetail.expandPath(path);
            btnAcct.addMouseListener(new SelAcctMouseClickListener(tfAcct,
                    btnAcct, dsDetail));
            setFocesFirstCell();
            addRgColTypeActionListener();
            // this.treePrjDetail.expandTo(this.treePrjDetail.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取导入表的明细列的面板
     * 
     * @return 导如表的明细列的面板
     */
    private FPanel getTablePanel() {
        FPanel pnlDetailInfo = new FPanel(); // 右上面板上的项目明细信息表的panel
        RowPreferedLayout layRTable = new RowPreferedLayout(5);
        pnlDetailInfo.setLayout(layRTable);
        pnlDetailInfo.setTitle("当前明细表所有列");
        pnlDetailInfo.setFontSize(8);
        pnlDetailInfo.setFontName("宋体");
        CreateChooser();
        return pnlDetailInfo;
    }

    /**
     * 定位到第一个节点
     */
    private void setFocesFirstCell() throws Exception {
        if (dsDetail != null && !dsDetail.isEmpty()) {
            dsDetail.beforeFirst();
            if (dsDetail.next()) {
                String sID = dsDetail.fieldByName(IPrjDetail.DETAIL_CODE)
                        .getString();
                treePrjDetail.expandTo(IPrjDetail.DETAIL_CODE, sID);
            }
        }
    }

    /**
     * 设置计算公式的内容
     * 
     * @param formulaContent
     */
    public void setFormula(String formulaContent) {
        textfieldFormula.setValue(formulaContent);
    }

    /**
     * 明细表树的点击监听事件
     * 
     * @author Administrator
     * 
     */
    private class DetailTreeClickListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            try {
                if (treePrjDetail.getSelectedNode() == null)
                    return;
                if (treePrjDetail.getSelectedNode() != treePrjDetail.getRoot()) {
                    String sNa = dsDetail.fieldByName("DETAIL_NAME")
                            .getString();
                    setTextField(sNa);

                    // 设置经济科目信息
                    String sAcctCode = dsDetail.fieldByName(
                            IPubInterface.ACCT_CODE_JJ).getString();
                    String sAcctName = dsDetail.fieldByName(
                            IPubInterface.ACCT_NAME_JJ).getString();
                    String sAcctValue = null;
                    if (Common.isNullStr(sAcctCode) && !Common.isNullStr(sAcctName))
                    	sAcctValue = sAcctName;
                    if (!Common.isNullStr(sAcctCode) && Common.isNullStr(sAcctName))
                    	sAcctValue = sAcctCode;
                    if (!Common.isNullStr(sAcctCode) && !Common.isNullStr(sAcctName))
                       sAcctValue = (Common.isNullStr(sAcctCode)) ? ""
                            : sAcctCode + "|" + sAcctName;
                    tfAcct.setValue(sAcctValue);

                    // 改变列信息的数据集，并把详细信息赋给FTextField
                    String sDetailCode = dsDetail.fieldByName(
                            IPrjDetail.DETAIL_ID).getString();
                    String sFilter = IPrjDetail.DETAIL_ID + "=" + sDetailCode;
                    dsCol = itserv.getColInfoAccordDetailCode(
                            IPrjDetail.Table_SIMP_COLINFO, sFilter);
                    // 生成praID
                    bmkDel = new String[dsCol.getRecordCount()];
                    iDelCount = 0;
                    PrjDetailAction.createParentIDDependCodeRule(dsCol,
                            IPrjDetail.FIELD_ID, codeRule_Col);

                    TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                            IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                            codeRule_Col, null);
                    createReport(tableHeader);
                    // 把dsCol定位到第一条记录并显示
                    dsCol.beforeFirst();
                    dsCol.next();
                    CellSelection cells = new CellSelection(0, 0);
                    reportUI.setCellSelection(cells);
                    setColDetailInfo(true);
                } else if (treePrjDetail.getSelectedNode() == treePrjDetail
                        .getRoot()) {
                    if (dsCol != null) {
                        dsCol.clearAll();
                        setTextField("");
                        TableHeader tableHeader = HeaderUtility.createHeader(
                                dsCol, IPrjDetail.FIELD_ID,
                                IPrjDetail.FIELD_CNAME, codeRule_Col, null);
                        createReport(tableHeader);
                        setColDetailInfo(false);
                    }
                }
            } catch (Exception eee) {
                eee.printStackTrace();
            }
        }

    }

    /**
     * 添加同级编码 btnAdd的监听事件 增加列
     */
    private class addColActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (dsDetail.eof() | dsDetail.bof())
                    return;
                CellSelection cells = reportUI.getCellSelection();
                if (cells == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请先导入文件");
                    return;
                }
                int col = cells.getColumn();
                int row = cells.getRow();
                CellElement cell = reportUI.getReport()
                        .getCellElement(col, row);
                if (cell == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请先导入文件");
                    return;
                }
                PrjDetailAction.insertCodeDependCell(dsCol, dsDetail
                        .fieldByName(IPrjDetail.DETAIL_CODE).getString(), dsCol
                        .toogleBookmark(), IPrjDetail.FIELD_ID, dsCol
                        .fieldByName(IPrjDetail.FIELD_ID).getString(),
                        codeRule_Col);
                TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                        codeRule_Col, null);
                createReport(tableHeader);
                // dsCol.maskDataChange(true);
                // dsCol.gotoBookmark(bmk);
                // setColDetailInfo(true);
                // dsCol.maskDataChange(false);
                CellSelection cellsS = new CellSelection(col + 1, row);
                reportUI.setCellSelection(cellsS);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * btnAddChild插入子级节点
     */
    private class addChildColActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                CellSelection cells = reportUI.getCellSelection();
                if (cells == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请先导入文件");
                    return;
                }
                int col = cells.getColumn();
                int row = cells.getRow();
                CellElement cell = reportUI.getReport()
                        .getCellElement(col, row);
                if (cell == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请先导入文件");
                    return;
                }
                String sStdType = null;
                if (!Common.isNullStr(Common.nonNullStr(cbColTypeStandard
                        .getValue()))) {
                    sStdType = ((MyListElement) cbColTypeStandard.getValue())
                            .getText();
                    if (!"无".equals(sStdType)) {
                        JOptionPane.showMessageDialog(pnlBase, "标准列不允许增加子节点");
                        return;
                    }
                }
                if (dsDetail.eof() | dsDetail.bof())
                    return;
                Cell cell11 = (Cell) reportUI.getReportContent()
                        .getSelectedCell();
                if (cell11 == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请选择添加位置");
                    return;
                }
                JRadioButton[] radios = rgFieldKind.getRadios();
                if (radios[2].isSelected() || radios[1].isSelected()) {
                    JOptionPane.showMessageDialog(pnlBase, "该列类型不允许添加子节点");
                    return;
                }
                InfoPackage info = new InfoPackage();
                info = PrjDetailAction.insertChildCodeDependCell(dsCol,
                        dsDetail.fieldByName(IPrjDetail.DETAIL_CODE)
                                .getString(), dsCol.toogleBookmark(),
                        IPrjDetail.FIELD_ID, dsCol.fieldByName(
                                IPrjDetail.FIELD_ID).getString(), codeRule_Col);
                if (!info.getSuccess()) {
                    JOptionPane.showMessageDialog(pnlBase, info.getsMessage());
                    return;
                }
                TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                        codeRule_Col, null);
                createReport(tableHeader);
                String sParID = dsCol.fieldByName("PARID").getString();
                dsCol.locate("PARID", sParID);
                // String bmkNow = dsCol.toogleBookmark();
                // setNextSelCell( dsCol, bmkNow , 1 , IPrjDetail.FIELD_ID );
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * btnDelete的监听事件 删除列
     * 
     */
    private class delColActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                dsCol.maskDataChange(true);
                if (dsDetail.eof() | dsDetail.bof())
                    return;
                Cell cell = (Cell) reportUI.getReportContent()
                        .getSelectedCell();
                if (cell == null) {
                    JOptionPane.showMessageDialog(pnlBase, "请选择要删除的节点");
                    return;
                }
                if (isUseInFormula(dsCol.fieldByName(IPrjDetail.FIELD_ENAME)
                        .getString())){
                	JOptionPane.showMessageDialog(Global.mainFrame, "该列已经在公式中使用");
                    return;
                }
                // dsCol.delete();
                String sFieldID = dsCol.fieldByName(IPrjDetail.FIELD_ID)
                        .getString();
                PrjDetailAction.deleteWithChildInfo(dsCol, sFieldID);
                if (bmkDel == null || bmkDel.length == 0) {
                    if (dsCol != null && !dsCol.isEmpty()) {
                        bmkDel = new String[dsCol.getRecordCount()];
                        iDelCount = 0;
                    }
                }
                if (bmkDel != null && iDelCount < bmkDel.length) {
                    bmkDel[iDelCount] = sFieldID;
                    iDelCount++;
                }
                iState = state_Delete;
                TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                        codeRule_Col, null);
                createReport(tableHeader);
                String sPFieldID = dsCol.fieldByName(IPrjDetail.FIELD_ID)
                        .getString();
                dsCol.locate(IPrjDetail.FIELD_ID, sPFieldID);
                setColDetailInfo(true);
                dsCol.maskDataChange(false);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
    
    /**
     * 判断该列是否在公式中使用
     * @return
     * @throws Exception
     */
    private boolean isUseInFormula(String sFieldEName) throws Exception{
    	DataSet dsC = (DataSet)dsCol.clone();
    	if (dsC==null || dsC.isEmpty())
    		return false;
    	dsC.beforeFirst();
    	String ne = null;
    	String nf = null;
    	while(dsC.next()){
    		ne = dsC.fieldByName(IPrjDetail.FIELD_ENAME).getString();
    		nf = dsC.fieldByName(IPrjDetail.FORMULA_DET).getString();
    		if (Common.isNullStr(ne) || Common.isNullStr(nf))
    			continue;
    		if (nf.toUpperCase().indexOf("{"+sFieldEName.toUpperCase()+"}")>=0)
    			return true;
    	}
    	return false;
    }

    /**
     * 创建文件选择器
     * 
     */
    private void CreateChooser() {
        // 创建文件选择器
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        // 设定可用的文件的后缀名
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.getName().endsWith(".xls") ||f.getName().endsWith(".xls".toUpperCase())|| f.isDirectory()) {
                    return true;
                }
                return false;
            }
            public String getDescription() {
                return "所有文件(*.xls)";
            }
        });
    }

    /**
     * EXCEL导入按钮的监听事件
     */
    private class ImportBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // 出现文件选择框
                int returnval = fileChooser.showOpenDialog(Global.mainFrame);
                File file = fileChooser.getSelectedFile();
                if (returnval == JFileChooser.APPROVE_OPTION) {
                    ExcelImporter ei = new ExcelImporter(file);
                    // 显示获取表头界面
                    SelectTableHeader pnlTableHeader = new SelectTableHeader(ei);
                    pnlTableHeader.setSize(Toolkit.getDefaultToolkit()
                            .getScreenSize());
                    pnlTableHeader.setVisible(true);
                    node = null;
                    node = pnlTableHeader.getNode();
                    // createReport(node);
                    fillHeaderToDataset();
                    PrjDetailAction.createParentIDDependCodeRule(dsCol,
                            IPrjDetail.FIELD_ID, codeRule_Col);
                    TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                            IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                            codeRule_Col, null);
                    createReport(tableHeader);
                    reportUI.repaint();
                    iState = state_Add;
                    cellFirstTag = 0;
                    // setColDetailInfo(false);
                    setSelCell();
                    bmkDel = null;
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * 把表头数据填入dataset中
     * 
     */
    private void fillHeaderToDataset() throws Exception {
        // 生成node数组
        if (dsDetail.eof() || dsDetail.bof() || dsDetail.isEmpty()) {
            JOptionPane.showMessageDialog(pnlBase, "请选择明细表");
            return;
        }

        String sDetailCode = dsDetail.fieldByName(IPrjDetail.DETAIL_ID)
                .getString();
        String sFilter = IPrjDetail.DETAIL_ID + "='" + sDetailCode + "'";
        dsCol = itserv.getColInfoAccordDetailCode(
                IPrjDetail.Table_SIMP_COLINFO, sFilter);
        dsCol.clearAll();
        dsCol.maskDataChange(true);
        if (node == null)
            return;
        Node[][] nodeArray = node.toArray();
        String nodeID = null; // node的唯一表示符
        // 循环添数据到dataSet
        for (int i = 1; i < nodeArray.length; i++) {
            for (int j = 0; j < nodeArray[i].length; j++) {
                Node nodeGet = nodeArray[i][j];
                nodeID = nodeGet.getIdentifier().toString();
                CellElement cell = (CellElement) nodeGet.getValue();
                Double columnWidth = new Double(reportUI.getReportContent()
                        .getColumnWidth(cell.getColumn()));
                dsCol.append();
                dsCol.fieldByName("nodeID").setValue(nodeID);
                dsCol.fieldByName(IPrjDetail.DETAIL_CODE).setValue(
                        dsDetail.fieldByName(IPrjDetail.DETAIL_CODE)
                                .getString());
                String sFieldID = PrjAction.createFieldID(dsCol, nodeGet,
                        codeRule_Col, IPrjDetail.FIELD_ID);
                String sLvlID = PrjAction.createLvlID(nodeGet, dsCol, lvlRule,
                        IPrjDetail.LVL_ID);
                dsCol.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlID);
                dsCol.fieldByName(IPrjDetail.FIELD_ID).setValue(sFieldID);
                dsCol.fieldByName(IPrjDetail.FIELD_CNAME).setValue(
                        nodeGet.getText());
                // 生成Field_EName
                String sEName = PrjDetailAction.getFieldEName(dsCol, "F", dsCol
                        .toogleBookmark());
                dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sEName);
                // 生成 FName
                String sParentFName = PrjAction.getParentFName(dsCol, nodeGet);
                String sFName = nodeGet.getText();
                if (!Common.isNullStr(sParentFName))
                    sFName = sParentFName + "." + sFName;
                dsCol.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sFName);
                dsCol.fieldByName(IPrjDetail.SET_YEAR).setValue(
                        Global.loginYear);
                dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue(
                        columnWidth);
                dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue("浮点型");
                dsCol.fieldByName(IPrjDetail.CALL_PRI).setValue("0");
                dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
                dsCol.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
                dsCol.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");
                dsCol.fieldByName(IPrjDetail.SET_YEAR).setValue(
                        Global.loginYear);
                // dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("0");
                dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("72");
                dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue("浮点型");
                dsCol.fieldByName(IPrjDetail.FIELD_KIND).setValue("录入");
                dsCol.fieldByName(IPrjDetail.IS_SUMCOL).setValue("0");
                dsCol.fieldByName(IPrjDetail.IS_HIDECOL).setValue("0");
                dsCol.fieldByName(IPrjDetail.NOTNULL).setValue("0");
                dsCol.fieldByName(IPrjDetail.CALL_PRI).setValue("0");
                dsCol.fieldByName("rg_code").setValue(Global.getCurrRegion());
                JRadioButton[] radios = rgColType.getRadios();
                radios[0].setSelected(true);
            }
        }
        setColDetailInfo(true);
        dsCol.maskDataChange(false);
    }

    /**
     * 
     * 根据右边明细表的选择确定右边明细表名称的信息
     */
    private void setTextField(String sNa) {
        tfName.setValue(sNa);
    }

    /**
     * 创建report
     */
    private void initReport() throws Exception {
        reportUI.clearAll();
        reportUI.getReport().shrinkToFitRowHeight();
        reportUI.getReport().getReportSettings().setPaperSize(
                new PaperSize(2000, 3000));
        reportUI.updateUI();
    }

    /**
     * 建立表头
     * 
     * @param tableHeader
     * @throws Exception
     */
    private void createReport(TableHeader tableHeader) throws Exception {
        tablePrjDetail.removeAllCellElements();
        tableHeader.setFont(new Font("宋体", Font.PLAIN, 12));
        tableHeader.setColor(new Color(250, 228, 184));
        tablePrjDetail.setReportHeader(tableHeader);
        reportUI.setRowHeaderVisible(false);
        reportUI.setColumnHeaderVisible(false);
        reportUI.repaint();
        reportUI.getReportContent().shrinkToFitRowHeight();
        JRadioButton[] radios = rgColType.getRadios();
        radios[0].setSelected(true);
    }

    /**
     * 添加项目明细
     */
    public void doAdd() {
        try {
            MyTreeNode node = (MyTreeNode) treePrjDetail.getSelectedNode();
            if (node == null) {
                JOptionPane.showMessageDialog(pnlBase, "请选择添加节点");
                treePrjDetail.expandAll();
                return;
            }
            // 取父亲节点
            String sParentID = "";
            if (dsDetail != null && !dsDetail.isEmpty() && !dsDetail.eof()
                    && !dsDetail.bof())
                sParentID = dsDetail.fieldByName(IPrjDetail.DETAIL_ID)
                        .getString();
            int iNextCode = codeRule.nextLevelLength(sParentID);

            if (iNextCode < 0)
                sParentID = "";
            bmkDetailChange = dsDetail.toogleBookmark();
            dsDetail.maskDataChange(true);
            dsDetail.append();
            dsDetail.fieldByName("rg_code").setValue(Global.getCurrRegion());
            setColDetailInfo(false);
            iState_Detail = state_AddDetail;
            btnAdd.setEnabled(true);
            btnDelete.setEnabled(true);
            btnAddChild.setEnabled(true);
            // 生成 detail_id
            String sFilter = IPrjDetail.SET_YEAR
                    + "="
                    + Global.loginYear
                    + " and "
                    + (Common.isNullStr(sParentID) ? IPrjDetail.PAR_ID
                            + " is null" : IPrjDetail.PAR_ID + "='" + sParentID
                            + "'");

            // 此时要判断层次码是否有足够多的位数，
            // 如果有则在本级生成子节点，
            // 否则生成同级节点（判断此节点是否有父节点，如果有则父节点相同，否则都无父亲节点）
            String sCode = null;
            tfAcct.setValue("");
            sCode = pubserv.getNodeID(IPrjDetail.Table_SIMP_MASTER,
                    IPrjDetail.DETAIL_ID, sParentID, sFilter, codeRule);
            String sFirstCode = sCode.substring(0, 1);
            if ("0".equals(sFirstCode))
                sCode = sCode.replace('0', '1');
            // 生成lvl_id
            String sLvlID = pubserv.getNodeID(IPrjDetail.Table_SIMP_MASTER,
                    IPrjDetail.LVL_ID, "", sFilter, UntPub.lvlRule);
            dsDetail.fieldByName(IPrjDetail.DETAIL_CODE).setValue(sCode);
            dsDetail.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlID);
            dsDetail.fieldByName(IPrjDetail.SET_YEAR)
                    .setValue(Global.loginYear);
            if (dsCol != null && !dsCol.isEmpty()) {
                dsCol.clearAll();
                TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                        codeRule_Col, null);
                createReport(tableHeader);
            }
            treePrjDetail.setEnabled(false);
            dsDetail.maskDataChange(false);
            setViewState(iState_Detail);
            this.tfAcct.setValue("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置选中的单元各
     * 
     * @throws Exception
     */
    private void setSelCell() throws Exception {
        // TODO
        // dsCol.maskDataChange(true);
        // CellSelection cells = reportUI.getCellSelection();
        // int col = cells.getColumn();
        // int row = cells.getRow();
        // CellElement cell = reportUI.getReport().getCellElement(col,row);
        // if (cell == null)
        // return ;
        // cell.setColumn(0);
        // cell.setRow(0);
        // reportUI.setCellSelection(cell);
        // String colName = Common.nonNullStr(cell.getValue());
        // this.tfColName.setValue(colName);
        // this.tfColFName.setValue(colName);
        Cell cellNow = (Cell) reportUI.getReportContent() // 当前选中的单元格
                .getSelectedCell();
        if (cellNow == null)
            return;
        String bmkNow = cellNow.getBookmark();
        if (dsCol.gotoBookmark(bmkNow)) {
            this.tfColName.setValue(dsCol.fieldByName(IPrjDetail.FIELD_CNAME)
                    .getValue());
            this.tfColFName.setValue(dsCol.fieldByName(IPrjDetail.FIELD_FNAME)
                    .getValue());
        }
    }

    /**
     * 删除项目明细
     */
    public void doDelete() {
        try {
            // 获取fb_p_detail的数据集
            String sFilter = IPrjDetail.DETAIL_TYPE + "='"
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString()
                    + "'";
            DataSet dsDataIsUsed = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.TABLENAME_DETAIL, sFilter);
            // 获取类别对明细的数据集
            String sFilterSortToDetail = IPrjDetail.DETAIL_CODE + "='"
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString()
                    + "'";
            DataSet dsDataSort = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.FB_P_SORT_TO_DETAIL, sFilterSortToDetail);
            if (!dsDataIsUsed.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "该项目明细已经被使用");
                return;
            } else {
                String sID = null;
                if (dsDetail.prior()) {
                    sID = dsDetail.fieldByName(IPrjDetail.DETAIL_ID)
                            .getString();
                    dsDetail.next();
                } else if (dsDetail.next()) {
                    if (dsDetail.next())
                        sID = dsDetail.fieldByName(IPrjDetail.DETAIL_ID)
                                .getString();
                    else
                        sID = "";
                    dsDetail.prior();
                } else
                    sID = "";
                if (JOptionPane.showConfirmDialog(pnlBase, "是否确认删除数据？", "提示",
                        JOptionPane.OK_CANCEL_OPTION) == 0) {
                    dsDetail.delete();
                    dsCol.clearAll();
                    dsDataSort.clearAll();
                    itserv.dsPost(dsDetail, dsCol, dsDataSort);
                    // itserv.dsPost(dsCol); // by ymq 合并到一个事务中
                    // itserv.dsPost(dsDataSort);
                    dsDetail.applyUpdate();
                    dsCol.applyUpdate();
                    dsDataSort.applyUpdate();
                    if (!Common.isNullStr(sID))
                        treePrjDetail.expandTo(IPrjDetail.DETAIL_ID, sID);
                    if (dsDetail.isEmpty() || Common.isNullStr(sID)) {
                        tfPrjDetailName.setValue("");
                        tfAcct.setValue("");
                        setColDetailInfo(false);
                    }
                }
            }
            setViewState(iState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消操作（取消项目明晰和列信息的dataset的操作）
     */
    public void doCancel() {
        try {
            dsDetail.cancel();
            if (dsCol != null) {
                dsCol.cancel();
                TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                        codeRule_Col, null);
                tablePrjDetail.removeAllCellElements();
                tableHeader.setFont(new Font("宋体", Font.PLAIN, 12));
                tableHeader.setColor(new Color(250, 228, 184));
                tablePrjDetail.setReportHeader(tableHeader);
                reportUI.setRowHeaderVisible(false);
                reportUI.setColumnHeaderVisible(false);
                reportUI.repaint();
                reportUI.getReportContent().shrinkToFitRowHeight();
            }
            dsDetail.gotoBookmark(bmkDetailChange);
            if (dsDetail != null && !dsDetail.isEmpty()) {
                String sDetailCode = dsDetail.fieldByName(
                        IPrjDetail.DETAIL_CODE).getString();
                treePrjDetail.expandTo(IPrjDetail.DETAIL_CODE, sDetailCode);
            }
            treePrjDetail.setEnabled(true);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            btnAddChild.setEnabled(false);
            iState = state_Browse;
            iState_Detail = state_Browse;
            setViewState(iState);
            reportUI.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改项目明细
     */
    public void doModify() {
        try {
            if (treePrjDetail.getSelectedNode() == null
                    || treePrjDetail.getSelectedNode() == treePrjDetail
                            .getRoot()) {
                JOptionPane.showMessageDialog(pnlBase, "请选择项目明细");
                return;
            }
            String sFilter = IPrjDetail.DETAIL_TYPE + "="
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString();
            DataSet dsDataIsUsed = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.TABLENAME_DETAIL, sFilter);
            if (!dsDataIsUsed.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "该项目明细已经被使用");
                return;
            } else {
                if (dsDetail.eof()
                        || dsDetail.bof()
                        || dsDetail.isEmpty()
                        || treePrjDetail.getSelectedNode() == treePrjDetail
                                .getRoot()) {
                    JOptionPane.showMessageDialog(pnlBase, "请选择项目明细");
                    return;
                }
                dsDetail.edit();
                iState_Detail = state_EditDetail;
                bmkDetailChange = dsDetail.toogleBookmark();
                treePrjDetail.setEnabled(false);
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnAddChild.setEnabled(true);
                setViewState(iState_Detail);
                int iChildNum = 0;
                iChildNum = PrjDetailAction.getChildNum(dsCol, dsCol
                        .fieldByName(IPrjDetail.FIELD_ID).getString());
                if (iChildNum > 0)
                    setPanelInfoEnable(false);
                else {
                    setPanelInfoEnable(true);
                    setColDetailInfo(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存项目明细和列信息
     */
    public void doSave() {
        try {
            // 判断detail与dsCol是否有修改，如果有则保存信息
            if (Common.isNullStr(tfName.getValue().toString().trim())) {
                JOptionPane.showMessageDialog(pnlBase, "明细表名称不能为空！");
                return;
            }
            if (dsCol.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "没有可保存的明细表列！");
                return;
            }
//            if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue()))) {
//                JOptionPane.showMessageDialog(pnlBase, "经济科目不能为空！");
//                return;
//            }
            if (dsDetail.eof() | dsDetail.bof())
                return;
            InfoPackage infoReturn = new InfoPackage();
            infoReturn = saveColInfoFromPanel(dsCol.toogleBookmark());
            // 保存DSCOL
            if (infoReturn.getSuccess() == false) {
                JOptionPane
                        .showMessageDialog(pnlBase, infoReturn.getsMessage());
                return;
            }
            infoReturn = checkDetail(dsCol.toogleBookmark()); // 检查数据合法性
            if (infoReturn.getSuccess() == false) {
                JOptionPane
                        .showMessageDialog(pnlBase, infoReturn.getsMessage());
                return;
            }
            infoReturn = checkStdType();
            if (infoReturn.getSuccess() == false) {
                JOptionPane
                        .showMessageDialog(pnlBase, infoReturn.getsMessage());
                return;
            }
            if (itserv.checkDetailName(dsDetail.fieldByName(
                    IPrjDetail.DETAIL_CODE).getString(),
                    (String) tfPrjDetailName.getValue())) {
                JOptionPane.showMessageDialog(pnlBase, "明细表名称已经被使用，请修改！");
                return;
            }
            String acctcode = Common.nonNullStr(tfAcct.getValue());
            infoReturn = checkIsRepeat(dsCol, "FIELD_FNAME");
            if (!infoReturn.getSuccess()) {
                JOptionPane
                        .showMessageDialog(pnlBase, infoReturn.getsMessage());
                return;
            }
            dsDetail.fieldByName(IPrjDetail.DETAIL_NAME).setValue(
                    tfPrjDetailName.getValue());
            if (!Common.isNullStr(acctcode)) {
				int acpos = acctcode.indexOf("|");
				switch(iDefType){
				case -1:
					dsDetail.fieldByName("ACCT_CODE_JJ").setValue(
							acctcode.substring(0, acpos));
					dsDetail.fieldByName("ACCT_NAME_JJ").setValue(
							acctcode.substring(acpos + 1, acctcode.length()));
					break;
				case 0:
					dsDetail.fieldByName("ACCT_CODE_JJ").setValue(
							acctcode.substring(0, acpos));
					dsDetail.fieldByName("ACCT_NAME_JJ").setValue(
							acctcode.substring(acpos + 1, acctcode.length()));
					break;
				case 1:
					dsDetail.fieldByName("ACCT_CODE_JJ").setValue(
							acctcode);
					break;
				case 2:
					dsDetail.fieldByName("ACCT_NAME_JJ").setValue(
							acctcode);
					break;
				default:
					dsDetail.fieldByName("ACCT_CODE_JJ").setValue("");
				    dsDetail.fieldByName("ACCT_NAME_JJ").setValue("");
					break;
				}
			}
            itserv.saveColData(dsDetail, dsCol);
            // 下面是置状态到浏览情况下
            dsCol.applyUpdate();
            dsDetail.applyUpdate();
            treePrjDetail.setDataSet(dsDetail);
            treePrjDetail.reset();
            iState_Detail = state_Browse;
            treePrjDetail.setEnabled(true);
            btnAdd.setEnabled(false);
            btnAddChild.setEnabled(false);
            btnDelete.setEnabled(false);
            dsCol.edit();
            String sDCode = dsDetail.fieldByName(IPrjDetail.DETAIL_ID)
                    .getString();
            treePrjDetail.expandTo(IPrjDetail.DETAIL_CODE, sDCode);
            setViewState(state_Browse);
            bmkDel = null;
            iDelCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查字段在公式中是否使用
     * 
     * @param ds
     * @param fieldName
     *            英文字段
     * @param fieldValue
     *            要检查的字段值
     * @param formulaName
     *            公式的字段名称
     * @return 检查结果
     * @throws Exception
     */
    private InfoPackage checkIsInFormula(DataSet ds, String fieldName,
            String fieldValue, String formulaName, String ViewName)
            throws Exception {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        if (ds == null || ds.isEmpty())
            return info;
        DataSet dsClone = (DataSet) ds.clone();
        dsClone.beforeFirst();
        while (dsClone.next()) {
            String formulaValue = dsClone.fieldByName(formulaName).getString();
            String cname = dsClone.fieldByName(ViewName).getString();
            int pos = formulaValue.indexOf("{" + fieldValue + "}");
            if (pos >= 0) {
                info.setSuccess(false);
                info.setsMessage("该字段已经在公式“" + cname + "”中使用");
                return info;
            }
        }
        return info;
    }

    /**
     * 判断是否有重复的名称
     * 
     * @return
     * @throws Exception
     */
    private InfoPackage checkIsRepeat(DataSet ds, String fieldname)
            throws Exception {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        if (ds == null || ds.isEmpty() || ds.getRecordCount() == 1)
            return info;
        DataSet dsClone = (DataSet) ds.clone();
        String[] arrayName1 = new String[ds.getRecordCount()];
        String[] arrayName2 = new String[ds.getRecordCount()];
        String[] arrayName3 = new String[ds.getRecordCount()];
        String[] arrayName4 = new String[ds.getRecordCount()];
        int m = 0;
        dsClone.beforeFirst();
        while (dsClone.next()) {
            arrayName2[m] = dsClone.fieldByName(fieldname).getString();
            arrayName1[m] = dsClone.fieldByName(fieldname).getString();
            arrayName3[m] = dsClone.fieldByName("LVL_ID").getString();
            arrayName4[m] = dsClone.fieldByName("LVL_ID").getString();
            m++;
        }
        for (int j = 0; j < arrayName1.length; j++) {
            for (int k = 0; k < arrayName2.length; k++) {
                if (Common.isEqual(arrayName1[j], arrayName2[k])
                        && arrayName3[j].length() == arrayName4[k].length()) {
                    // 如果相等
                    if (j != k) {
                        info.setsMessage("“" + arrayName1[j] + "”有重复！");
                        info.setSuccess(false);
                        return info;
                    }
                }
            }
        }
        return info;
    }

    /**
     * 把此时dsCol的信息对应到界面上并逐一显示出来
     * 
     * @param 是否把界面上清空
     */
    private void setColDetailInfo(boolean isNotNullInfo) throws Exception {
        if (dsCol == null)
            return;
        dsCol.maskDataChange(true);
        String bmk = dsCol.toogleBookmark();
        String sColName = "";
        String sColType = "";
        String sFieldKind = "";
        String sFormula = "";
        String sCallPri = "";
        String sPickkValue = "";
        String sDisPlay = "";
        String sNotNull = "";
        String sColStdType = "";
        String sFName = "";
        if (!dsCol.isEmpty() && !dsCol.eof() && !dsCol.bof()
                && isNotNullInfo == true) {
            sColName = dsCol.fieldByName(IPrjDetail.FIELD_CNAME).getString();
            sColType = dsCol.fieldByName(IPrjDetail.DATA_TYPE).getString();
            sFieldKind = dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString();
            // 设置计算公式的时候也必须反转一下
            String sFormulaMid = dsCol.fieldByName(IPrjDetail.FORMULA_DET)
                    .getString();
            sFormula = pubserv.replaceTextExDs(sFormulaMid, 0, dsCol,
                    IPrjDetail.FIELD_ENAME, IPrjDetail.FIELD_FNAME);
            sCallPri = dsCol.fieldByName(IPrjDetail.CALL_PRI).getString();
            sPickkValue = dsCol.fieldByName(IPrjDetail.PICK_VALUES).getString();
            sDisPlay = dsCol.fieldByName(IPrjDetail.DISPLAY_FORMAT).getString();
            sNotNull = dsCol.fieldByName(IPrjDetail.NOTNULL).getString();
            sColStdType = "0"
                    + dsCol.fieldByName(IPrjDetail.STD_TYPE).getString();
            sFName = dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString();
        }
        tfColName.setValue(sColName);
        tfColFName.setValue(sFName);
        JRadioButton[] radiosaa = rgColType.getRadios();
        // 设置列类型
        if (sColType.equals("浮点型")) {
            String sFilter = "CVALUE=='带小数的数值'";
            DataSet ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            cbDisplayStyle.setDataSet(ds);
            cbDisplayStyle.reset();
            rgColType.setValue("1 ");
            radiosaa[0].setSelected(true);
        } else if (sColType.equals("整型")) {
            String sFilter = "CVALUE=='整数'";
            DataSet ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            cbDisplayStyle.setDataSet(ds);
            cbDisplayStyle.reset();
            rgColType.setValue("2");
            radiosaa[1].setSelected(true);
        } else if (sColType.equals("字符型")) {
            cbDisplayStyle.setDataSet(dsComboDP);
            cbDisplayStyle.reset();
            rgColType.setValue("3");
            radiosaa[2].setSelected(true);
        } else if (sColType.equals("日期型")) {
            String sFilter = "CVALUE=='日期'";
            DataSet ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            cbDisplayStyle.setDataSet(ds);
            cbDisplayStyle.reset();
            rgColType.setValue("4");
            radiosaa[3].setSelected(true);
        } else {
            rgColType.setValue("1 ");
            radiosaa[0].setSelected(true);
        }
        JRadioButton[] radios = rgFieldKind.getRadios();
        // 设置数据来源
        if (sFieldKind.equals("录入")) {
            rgFieldKind.setValue("1");
            radios[0].setSelected(true);
            if (iState != state_Browse) {
                btnShowFormulaPanel.setEnabled(false);
                this.tfFormula.setEditable(false);
                this.tfSelValue.setEditable(false);
            } else {
                btnShowFormulaPanel.setEnabled(false);
                this.tfFormula.setEditable(false);
                this.tfSelValue.setEditable(false);
            }
        } else if (sFieldKind.equals("计算")) {
            rgFieldKind.setValue("2 ");
            radios[1].setSelected(true);
            if (iState != state_Browse) {
                btnShowFormulaPanel.setEnabled(true);
                this.tfSelValue.setEnabled(false);
                this.tfFormula.setEditable(true);
            } else {
                btnShowFormulaPanel.setEnabled(false);
                this.tfFormula.setEditable(false);
                this.tfSelValue.setEditable(false);
            }
        } else if (sFieldKind.equals("选取")) {
            rgFieldKind.setValue("3");
            radios[2].setSelected(true);
            if (iState != state_Browse) {
                btnShowFormulaPanel.setEnabled(false);
                this.tfSelValue.setEditable(true);
                this.tfFormula.setEditable(false);
            } else {
                btnShowFormulaPanel.setEnabled(false);
                this.tfFormula.setEditable(false);
                this.tfSelValue.setEditable(false);
            }
        } else {
            rgFieldKind.setValue("1");
            radios[0].setSelected(true);
            btnShowFormulaPanel.setEnabled(false);
            this.tfSelValue.setEditable(false);
            this.tfFormula.setEditable(false);
        }
        tfFormula.setValue(sFormula);
        tfPreferenceLevel.setValue(sCallPri);
        tfSelValue.setValue(sPickkValue);
        // 设置几个combobox
        cbDisplayStyle.setSelectedValue(sDisPlay);
        cbColTypeStandard.setSelectedValue(sColStdType);

        // 设置"该列必须输入"
        if (sNotNull.equals("0"))
            chkbIsInput.setSelected(false);
        else
            chkbIsInput.setSelected(true);
        if (!isNotNullInfo)
            tfName.setValue("");
        dsCol.gotoBookmark(bmk);
        dsCol.maskDataChange(false);
    }

    /**
     * 从面板获取列明细信息
     */
    private InfoPackage saveColInfoFromPanel(String bmk) throws Exception {
        dsCol.maskDataChange(true);
        InfoPackage infoReturn = new InfoPackage();
        infoReturn.setSuccess(true);
        // if (dsCol.getState() == DataSet.FOR_DELETE)
        // return infoReturn;
        dsCol.gotoBookmark(bmk);
        boolean bBmkDelHaveContent = false;
        int ialength = 0;

        if (bmkDel != null && bmk.length() != 0) {
            int inum = bmkDel.length;
            for (int i = 0; i < inum; i++) {
                if (bmkDel[i] != null) {
                    ialength++;
                }
            }
            String[] bmkt = new String[ialength];
            for (int i = 0; i < ialength; i++)
                bmkt[i] = bmkDel[i];
            bmkDel = bmkt;
        }
        if (bmkDel != null && bmkDel.length != 0) {
            for (int i = 0; i < bmkDel.length; i++)
                if (!Common.isNullStr(bmkDel[i])) {
                    bBmkDelHaveContent = true;
                    break;
                }
            if (bBmkDelHaveContent == true) {
                Arrays.sort(bmkDel);
                int idex = Arrays.binarySearch(bmkDel, dsCol.fieldByName(
                        IPrjDetail.FIELD_ID).getString());
                if (idex > 0)
                    return infoReturn;
            }
        }
        dsCol.edit();
        Object sOldColName = dsCol.fieldByName(IPrjDetail.FIELD_CNAME)
                .getValue();
        String sID = dsCol.fieldByName(IPrjDetail.FIELD_ID).getString();
        Object oNewColName = tfColName.getValue();
        Object oNewColFName = tfColFName.getValue();
        String sNewFName = (oNewColFName == null) ? "" : oNewColFName
                .toString();
        String sNewColName = (oNewColName == null) ? "" : oNewColName
                .toString();
        dsCol.fieldByName(IPrjDetail.FIELD_CNAME).setValue(sNewColName);
        if (!Common.isEqual(sOldColName, sNewColName))
            PrjDetailAction.modifyChildFullName(dsCol, dsCol, sNewFName,
                    new String[] { sID }, codeRule_Col);
        dsCol.fieldByName(IPrjDetail.FIELD_FNAME).setValue(
                tfColFName.getValue());
        // 其他
        dsCol.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
        dsCol.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");
        dsCol.fieldByName(IPrjDetail.PRIMARY_PROPFIELD).setValue("");
        // 优先级
        dsCol.fieldByName(IPrjDetail.CALL_PRI).setValue(
                (tfPreferenceLevel.getValue() == null) ? "0"
                        : tfPreferenceLevel.getValue());
        // 设置列类型,ename的值
        String sColType = (rgColType.getValue() == null) ? "" : rgColType
                .getValue().toString();
        int iDataTypeTag = Integer.parseInt(Common.isNullStr(sColType) ? "1"
                : sColType);
        String sFieldEName = null;
        String sDataTypeValue = null;
        switch (iDataTypeTag) {
        case 1:
            sDataTypeValue = "浮点型";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "F", bmk);
            break;
        case 2:
            sDataTypeValue = "整型";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "N", bmk);
            break;
        case 3:
            sDataTypeValue = "字符型";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "C", bmk);
            break;
        case 4:
            sDataTypeValue = "日期型";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "D", bmk);
            break;
        }
        dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue(sDataTypeValue);

        if ((Common.isNullStr(dsCol.fieldByName("PARID").getString().trim()) && dsCol
                .fieldByName("LVL_ID").getString().length() == 4)
                || !Common.isNullStr(dsCol.fieldByName("PARID").getString()
                        .trim()))
            // 如果父亲节点为空，并且是最顶级节点
            // 或者父级节点不为空
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sFieldEName);
        else
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue("");
        // 数据来源
        if (rgFieldKind.getValue() == null) {
            JRadioButton radios[] = rgFieldKind.getRadios();
            radios[0].setSelected(true);
        }
        int iFieldKindTag = Integer.parseInt(rgFieldKind.getValue().toString());
        String sFieldKindValue = null;
        switch (iFieldKindTag) {
        case 1:
            // TODO
            sFieldKindValue = "录入";
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue("");
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue("");
            // 计算公式
            break;
        case 2:
            sFieldKindValue = "计算";
            Object oFormula = tfFormula.getValue();
            String sFormula = (oFormula == null) ? "" : oFormula.toString();
            String sFormulaData = pubserv.replaceTextExDs(sFormula, 0, dsCol,
                    IPrjDetail.FIELD_FNAME, IPrjDetail.FIELD_ENAME);
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue(sFormulaData);
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue("");
            break;
        case 3:
            sFieldKindValue = "选取";
            // 选取值
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue(
                    tfSelValue.getValue());
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue("");
            break;
        }
        dsCol.fieldByName(IPrjDetail.FIELD_KIND).setValue(sFieldKindValue);
        // 设置列宽
        Cell cell = (Cell) reportUI.getReportContent().getSelectedCell();
        Double dbCellWith = new Double("0");
        if (cell != null)
            dbCellWith = new Double(reportUI.getReport().getColumnWidth(
                    cell.getColumn()));
        dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue(dbCellWith);

        // 显示格式
        dsCol.fieldByName(IPrjDetail.DISPLAY_FORMAT).setValue(
                cbDisplayStyle.getValue());
        // 是否可以为空
        if (chkbIsInput.isSelected())
            dsCol.fieldByName(IPrjDetail.NOTNULL).setValue("1");
        else
            dsCol.fieldByName(IPrjDetail.NOTNULL).setValue("0");
        // 列标准类型
        String sStdType = null;
        if (!Common.isNullStr(Common.nonNullStr(cbColTypeStandard.getValue()))) {
            sStdType = ((MyListElement) cbColTypeStandard.getValue()).getId();
            dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue(
                    Common.isNullStr(sStdType) ? "0" : sStdType);
        }
        dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue(sStdType);
        // 求和列默认为零
        dsCol.fieldByName(IPrjDetail.IS_SUMCOL).setValue("0");
        dsCol.fieldByName(IPrjDetail.IS_HIDECOL).setValue("0");
        // 如果为支出总计,则修改ename
        if ("01".equals(sStdType) || "1".equals(sStdType)) {
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue("Total_Prices");
        }
        infoReturn = checkDetail(bmk);
        dsCol.maskDataChange(false);
        return infoReturn;
    }

    /**
     * 获取本级编码长度(如2|5,01002),则返回3
     * 
     * @return 结果
     * @param aCodeRule
     *            编码长度
     */
    public int getCodeRulelength(SysCodeRule aCodeRule) {
        int iLastTag = aCodeRule.originRuleStr().lastIndexOf("|");
        int iCodeRuleLength = Integer.parseInt(aCodeRule.originRuleStr()
                .substring(iLastTag));
        return iCodeRuleLength;
    }

    /**
     * 检查保存时明细信息是否符合要求
     */
    private InfoPackage checkDetail(String bmk) throws Exception {
        dsCol.maskDataChange(true);
        String bmkBefore = dsCol.toogleBookmark();
        dsCol.gotoBookmark(bmk);
        InfoPackage info = new InfoPackage();
        if (tfColName.getValue() == null) {
            info.setSuccess(false);
            info.setsMessage("请输入列名称！");
            return info;
        }
        if (("计算".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString()))
                && "日期型".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                        .getString())) {
            info.setSuccess(false);
            info.setsMessage("列'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'属于日期类型，不能通过计算获得内容！");
            return info;
        }
        if ((dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString().equals("计算"))
                && (dsCol.fieldByName(IPrjDetail.DATA_TYPE).getString()
                        .equals("字符型"))) {
            info.setSuccess(false);
            info.setsMessage("列'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'属于文本类型，不能通过计算获得内容！");
            return info;
        }
        if (Common.isNullStr(dsCol.fieldByName(IPrjDetail.FORMULA_DET)
                .getString().trim())
                && ("计算".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND)
                        .getString()))) {
            info.setSuccess(false);
            info.setsMessage("请输入列'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'的计算公式");
            return info;
        }
        if (Common
                .isNullStr(dsCol.fieldByName(IPrjDetail.CALL_PRI).getString())
                && ("计算".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND)
                        .getString()))) {
            info.setSuccess(false);
            info.setsMessage("请设置列'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'的公式优先级");
            return info;
        }
        if (Integer.parseInt(Common.isNullStr(dsCol.fieldByName(
                IPrjDetail.CALL_PRI).getString()) ? "0" : dsCol.fieldByName(
                IPrjDetail.CALL_PRI).getString()) > dsCol.getRecordCount()) {
            info.setSuccess(false);
            info.setsMessage("计算优先级数字超出了列数，请调整！");
            return info;
        }
        if ("选取".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString())
                && Common.isNullStr(dsCol.fieldByName(IPrjDetail.PICK_VALUES)
                        .getString().trim())) {
            info.setSuccess(false);
            info.setsMessage("请输入列'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'的选取值列表");
            return info;
        }
        if ("选取".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString())) {
            String sPickValue = dsCol.fieldByName(IPrjDetail.PICK_VALUES)
                    .getString();
            char[] sPickValues = new char[sPickValue.length()];
            sPickValue.getChars(0, sPickValue.length(), sPickValues, 0); // 从
            // 第一个开始
            int iLen = sPickValue.length();
            if (iLen > 1) {
                if (';' == sPickValues[0] || ';' == sPickValues[iLen - 1]) {
                    info.setSuccess(false);
                    info.setsMessage("选取值列表不能以';'开头或结束");
                    return info;
                }
                String sDataType = dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                        .getString();
                if ("浮点型".equals(sDataType) || "整型".equals(sDataType)) {
                    char[] sLimit = { '0', '1', '2', '3', '4', '5', '6', '7',
                            '8', '9', ';', '.' };
                    Arrays.sort(sLimit);
                    for (int i = 1; i < iLen; i++) {
                        if (Arrays.binarySearch(sLimit, sPickValues[i]) < 0) {
                            info.setSuccess(false);
                            info.setsMessage("选取值列表中只能出现数字！");
                            return info;
                        }
                    }
                    String[] value = sPickValue.split(";");
                    for (int i = 0; i < value.length; i++) {
                        if (!Common.isNumber(value[i])) {
                            info.setSuccess(false);
                            info.setsMessage("选取值列表中只能出现数字！");
                            return info;
                        }
                    }
                }
            }
        }
        int iStdTypeNum = 0;
        iStdTypeNum = getStdTypeNum(dsCol);
        if ("01".equals(dsCol.fieldByName(IPrjDetail.STD_TYPE).getString())
                || "1".equals(dsCol.fieldByName(IPrjDetail.STD_TYPE)
                        .getString())) {
            if (iStdTypeNum > 1) {
                info.setSuccess(false);
                info.setsMessage("只能标识一列标准列为'支出总计'!");
                return info;
            }
            if ("字符型".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                    .getString())
                    || "日期型".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                            .getString())) {
                info.setSuccess(false);
                info.setsMessage("标准列'支出总计'必须为数值类型！!");
                return info;
            }
        } else {
            String[] arrayKind = { "2", "3" };
            Arrays.sort(arrayKind);
            int index = Arrays.binarySearch(arrayKind, dsCol.fieldByName(
                    IPrjDetail.STD_TYPE).getString());
            if (index > 0
                    && !"日期型".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                            .getString())) {
                info.setSuccess(false);
                info.setsMessage("标准列'开始年'或'结束年'必须为日期类型");
                return info;
            }
        }
        // 判断ename是否合法
        // dsENameLimit.beforeFirst();
        // String[] arryEName = new String[dsENameLimit.getRecordCount()+1];
        // int iCount = 0;
        // while( dsENameLimit.next() ){
        // arryEName[iCount] =
        // dsENameLimit.fieldByName("COLUMN_NAME").getString();
        // iCount++;
        // }
        // arryEName[iCount]= "Total_Prices";
        // Arrays.sort( arryEName );
        // if (Arrays.binarySearch(arryEName, dsCol.fieldByName(
        // IPrjDetail.FIELD_ENAME).getString())<0){
        // info.setSuccess(false);
        // info.setsMessage("列超出范围！");
        // return info;
        // }
        dsCol.gotoBookmark(bmkBefore);
        dsCol.maskDataChange(false);
        info.setSuccess(true);
        return info;
    }

    /**
     * 将修改的数据保存到dataset中
     * 
     * @author Administrator
     * 
     */
    private class FillToColDatasetListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            try {
                dsCol.maskDataChange(true);
                InfoPackage info = new InfoPackage();
                Cell cellNow = (Cell) reportUI.getReportContent() // 当前选中的单元格
                        .getSelectedCell();
                String bmkNow = cellNow.getBookmark();
                String bmkOld = null;
                // 如果为空，则默认为第一个单元格
                if (cellFirstTag == 0) {
                    cellOld = (Cell) reportUI.getReportContent()
                            .getCellElement(0, 0);
                    cellFirstTag = 1;
                }
                // 如果点之前有已经点过cell,则把这个cell对应的值保存到dsCol中
                // if (iState != state_Delete
                if (cellOld != null) {
                    int iRowOld = cellOld.getRow();
                    int iColumnOld = cellOld.getColumn();
                    CellSelection cellSelect = new CellSelection(iRowOld,
                            iColumnOld);
                    bmkOld = cellOld.getBookmark();
                    if (iState_Detail != state_Browse) {
                        info = saveColInfoFromPanel(bmkOld);
                        if (info.getSuccess() == false) {
                            JOptionPane.showMessageDialog(pnlBase, info
                                    .getsMessage());
                            reportUI.setCellSelection(cellSelect);
                            setColDetailInfo(true);
                            return;
                        }
                    }
                }
                dsCol.maskDataChange(false);
                dsCol.gotoBookmark(bmkNow);
                setColDetailInfo(true);
                cellOld = cellNow;
                int iChildNum = 0;
                iChildNum = PrjDetailAction.getChildNum(dsCol, dsCol
                        .fieldByName(IPrjDetail.FIELD_ID).getString());
                if (iChildNum > 0) // 如果选择的单元格有子节点，则只允许修改名称
                    setPanelInfoEnable(false);
                else
                    setPanelInfoEnable(true);
                // JRadioButton[] radiofk = rgColType.getRadios();
                // if (radiofk[3].isSelected() == true
                // && iState_Detail != state_Browse)
                // rgFieldKind.setEnabled(false);
                // else
                // rgFieldKind.setEnabled(true);
                // 控制显示计算公式按钮
                JRadioButton[] radios = rgFieldKind.getRadios();
                if (radios[1].isSelected() == true
                        && iState_Detail != state_Browse)
                    btnShowFormulaPanel.setEnabled(true);
                else
                    btnShowFormulaPanel.setEnabled(false);
                iColTypeSel = rgColType.getValue();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * 获取当前项目明细列的标准列为总计支出的记录数
     */
    private int getStdTypeNum(DataSet ds) throws Exception {
        String bmk = ds.toogleBookmark();
        ds.maskDataChange(true);
        int num = 0;
        ds.beforeFirst();
        while (ds.next()) {
            // if (ds.getState() == DataSet.FOR_DELETE)
            // continue;
            if ("1".equals(ds.fieldByName(IPrjDetail.STD_TYPE).getString())
                    || "01".equals(ds.fieldByName(IPrjDetail.STD_TYPE)
                            .getString()))
                num++;
        }
        ds.gotoBookmark(bmk);
        ds.maskDataChange(false);
        return num;
    }

    /**
     * 检查只能设置一列支出总计
     * 
     * @return
     * @throws Exception
     */
    private InfoPackage checkStdType() throws Exception {
        // 在保存的时候循环dscol,查询设置只能设置一次支出总计
        InfoPackage info = new InfoPackage();
        int iStdTypeNum = 0;
        iStdTypeNum = getStdTypeNum(dsCol);
        if (iStdTypeNum != 1) {
            info.setSuccess(false);
            info.setsMessage("必须设置一个标准列为'支出总计'!");
            return info;
        }
        info.setSuccess(true);
        return info;
    }

    /**
     * 设置TOOLBAR上按钮的状态
     * 
     * @param aState
     */
    private void setButtonState(int aState) throws Exception {
        List controls = this.getToolbarPanel().getSubControls();
        boolean isEditEnable;
        boolean isSaveEnable;
        if (aState == state_Browse) {
            isEditEnable = true;
            isSaveEnable = false;
        } else {
            isEditEnable = false;
            isSaveEnable = true;
        }
//        if (!WfStub.getMethod().getCanEditStru()) {
//            isEditEnable = false;
//            isSaveEnable = false;
//        }
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
     * 获取生成计算公式tree的dataset
     * 
     * @param dsPost
     * @return
     * @throws Exception
     */
    private DataSet getFormulaData(DataSet dsPost, String aFieldID)
            throws Exception {
        DataSet dsMid = (DataSet) dsPost.clone();
        DataSet ds = DataSet.create();
        // 获取所有的PARID字段，并存储到数组中
        dsMid.beforeFirst();
        String[] sParID = new String[dsMid.getRecordCount()];
        int i = 0;
        while (dsMid.next()) {
            sParID[i] = dsMid.fieldByName("PARID").getString();
            i++;
        }
        int iLength = 0;
        for (int j = 0; j < sParID.length; j++) {
            if (sParID[j] != null && !Common.isNullStr(sParID[j]))
                iLength++;
        }
        String[] array = new String[iLength];
        int iNum = 0;
        for (int j = 0; j < sParID.length; j++) {
            if (sParID[j] != null && !Common.isNullStr(sParID[j])) {
                array[iNum] = sParID[j];
                iNum++;
            }
        }
        sParID = array;
        Arrays.sort(sParID);
        dsMid.beforeFirst();
        while (dsMid.next()) {
            if (Arrays.binarySearch(sParID, dsMid.fieldByName(
                    IPrjDetail.FIELD_ID).getString()) >= 0)
                continue;
            if (dsMid.fieldByName(IPrjDetail.FIELD_ENAME).getString().equals(
                    "Total_Prices"))
                continue;
            if (dsMid.fieldByName(IPrjDetail.DATA_TYPE).getString().equals(
                    "字符型"))
                continue;
            if (dsMid.fieldByName(IPrjDetail.DATA_TYPE).getString().equals(
                    "日期型"))
                continue;
            String sFieldID = dsMid.fieldByName(IPrjDetail.FIELD_ID)
                    .getString();
            if (!Common.isEqual(sFieldID, aFieldID)) {
                ds.append();
                ds.fieldByName("PARID").setValue(
                        dsMid.fieldByName("").getString());
                ds.fieldByName(IPrjDetail.FIELD_ID).setValue(
                        dsMid.fieldByName(IPrjDetail.FIELD_ID).getString());
                ds.fieldByName(IPrjDetail.FIELD_FNAME).setValue(
                        dsMid.fieldByName(IPrjDetail.FIELD_FNAME).getString());
            }
        }
        ds.applyUpdate();
        return ds;
    }

    /**
     * cname改变fname随着发生改变 并且表头相关的单元格也发生改变
     * 同时如果该节点有子节点的话则修改所有该节点的子节点的fname中对应该节点的名称
     */
    private class CNameValueChangeListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            keyTyped(e);
        }

        public void keyReleased(KeyEvent e) {
            keyTyped(e);
        }

        public void keyTyped(KeyEvent e) {
            if (iState == 0)
                return;
            String sNewLastValue = tfColName.getValue() == null ? ""
                    : tfColName.getValue().toString();
            Cell cell = (Cell) reportUI.getReportContent().getSelectedCell();
            if (cell == null || dsCol.isEmpty())
                return;
            if (dsCol == null || dsCol.isEmpty() || dsCol.bof() || dsCol.eof())
                return;
            try {
                String sOldValue = dsCol.fieldByName(IPrjDetail.FIELD_FNAME)
                        .getString();
                int idex = sOldValue.lastIndexOf(".");
                String sNewValue = (Common.nonNullStr(tfColFName.getValue()));
                if (idex > 0) {
                    String sPreValue = sOldValue.substring(0, idex);
                    sNewValue = sPreValue + "." + sNewLastValue;
                } else {
                    sNewValue = sNewLastValue;
                }
                tfColFName.setValue(sNewValue);
                cell.setValue(sNewLastValue);
                // }
                reportUI.repaint();
            } catch (Exception ek) {
                ek.printStackTrace();
            }
        }

    }

    /**
     * 设置经济科目按钮的监听事件
     * 
     * @author Administrator
     */
    private class SelAcctMouseClickListener extends MouseAdapter {
        private FTextField tfName;

        private FButton btn;

        private DataSet ds;

        public SelAcctMouseClickListener(FTextField tfPost, FButton btnPost,
                DataSet dsPost) {
            this.tfName = tfPost;
            this.btn = btnPost;
            this.ds = dsPost;
        }

        public void mouseClicked(MouseEvent e) {
            if (btn.isEnabled() == false)
                return;
            String sFullValue = (tfName.getValue() == null) ? "" : tfName
                    .getValue().toString();
            try {
				// 这个地方要判断两种情况，增加的时候肯定是非自定义的
				// 修改的时候则要判断库里的数据是否bsi_id为空，如果不为空，则肯定是非自定义
				boolean isDefine = false;
				if (iState == state_Add)
					isDefine = true;
				else {
					if (dsDetail == null || dsDetail.isEmpty()
							|| dsDetail.bof() || dsDetail.eof())
						isDefine = false;
					if (Common.isNullStr(dsDetail.fieldByName("BSI_ID")
							.getString()))
						isDefine = true;
				}
				if (!isDefine)
					iDefType = -1;
				else{
					String dcode = dsDetail.fieldByName("ACCT_CODE_JJ").getString();
					String dname = dsDetail.fieldByName("ACCT_NAME_JJ").getString();
					if (Common.isNullStr(dcode)
							&& !Common.isNullStr(dname)){
						iDefType = 2;
					}
					else if (!Common.isNullStr(dcode)
							&& Common.isNullStr(dname)){
						iDefType = 1;
					}
					else if (Common.isNullStr(dcode) && Common.isNullStr(dname)){
						iDefType = 3;
					}else{
						iDefType = 0;
					}
				}
				SelAcct ss = new SelAcct(sFullValue, isDefine,iDefType);
				Tools.centerWindow(ss);
				ss.setVisible(true);
				String sTName = (ss.getValue() == null) ? "" : ss.getValue()
						.toString();
				tfName.setValue(sTName);
				String[] sReturnValue = new String[3];
				sReturnValue = ss.getReturnValue();
				if (sReturnValue != null) {
					ds.fieldByName(IPubInterface.BSI_ID).setValue(
							sReturnValue[0]);
					ds.fieldByName(IPubInterface.ACCT_CODE_JJ).setValue(
							sReturnValue[1]);
					ds.fieldByName(IPubInterface.ACCT_NAME_JJ).setValue(
							sReturnValue[2]);
				}
				iDefType = ss.getDefineType();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
        }
    }

    /**
	 * 空操作
	 */
    public void doInsert() {
        doNothing();
    }

    public void doClose() {
        ((FFrame) Global.mainFrame).closeMenu();
    }

    private void doNothing() {
    }

    /**
     * 设置面板信息是否可编辑( 根据选中的cell是否有子节点 )
     * 
     * @param isEnable
     */
    private void setPanelInfoEnable(boolean isEnable) {
        if (isEnable == false) {
            rgColType.setEditable(false); // 列类型

            rgFieldKind.setEditable(false); // 数据来源

            rgFieldKind.setEditable(false);

            tfPreferenceLevel.setEditable(false); // 优先级

            cbDisplayStyle.setEnabled(false); // 显示风格

            chkbIsInput.setEnabled(false); // 该列必须输入内容

            cbColTypeStandard.setEnabled(false); // 列标准类型

            btnShowFormulaPanel.setEnabled(false); // 弹出公式编辑界面

        } else {
            if (iState == state_Browse)
                return;
            rgColType.setEditable(true); // 列类型

            rgFieldKind.setEditable(true); // 数据来源

            rgFieldKind.setEditable(true);

            tfPreferenceLevel.setEditable(true); // 优先级

            cbDisplayStyle.setEnabled(true); // 显示风格

            chkbIsInput.setEnabled(true); // 该列必须输入内容

            cbColTypeStandard.setEnabled(true); // 列标准类型

            JRadioButton[] radios = rgFieldKind.getRadios();
            if (radios[1].isSelected() == true)
                btnShowFormulaPanel.setEnabled(true);
            else
                btnShowFormulaPanel.setEnabled(false);
        }

    }

    // ** 弹出计算公式的编辑界面的监听类
    private class ShowFormulaPanelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String selColType = rgColType.getValue().toString();
                String selFieldKind = rgFieldKind.getValue().toString();
                String sCName = dsCol.fieldByName(IPrjDetail.FIELD_CNAME)
                        .getString();
                if ("3".equals(selColType) && "2".equals(selFieldKind)) {
                    JOptionPane.showMessageDialog(pnlBase, "列“" + sCName
                            + "”属于文本类型,不能通过计算获得");
                    JRadioButton[] radios = rgColType.getRadios();
                    radios[0].setSelected(true);
                    radios[1].setSelected(false);
                    radios[2].setSelected(false);
                    return;
                }
//                FormulaPanel pp = new FormulaPanel(false, formulaOp);
//                Tools.centerWindow(pp);
//                String sFieldID = dsCol.fieldByName(IPrjDetail.FIELD_ID)
//                        .getString();
//                DataSet dsFormulaCol = getFormulaData(dsCol, sFieldID);
//                CustomTree treeFormula = new CustomTree("可选列", dsFormulaCol,
//                        IPrjDetail.FIELD_ID, IPrjDetail.FIELD_FNAME, "PARID",
//                        null);
//                pp.setTreField(IPrjDetail.FIELD_ID, IPrjDetail.FIELD_FNAME);
//                pp.initTree(treeFormula);
//                Object oFormula = tfFormula.getValue();
//                pp.setFormula((oFormula == null) ? "" : oFormula.toString());
//                pp.setVisible(true);
//                tfFormula.setValue(pp.getFormula());
            } catch (Exception ek) {
                ek.printStackTrace();
            }
        }
    }

    /**
     * 给列类型面板添加监听事件
     * 
     */
    private void addRgColTypeActionListener() throws Exception {
        // 列类型的监听事件
        JRadioButton[] radios = rgColType.getRadios();
        for (int i = 0; i < radios.length; i++) {
            radios[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // 控制显示格式
                    try {
                        if (rgColType.isEditable() == false
                                || rgColType.isEnabled() == false)
                            return;
                        InfoPackage info = new InfoPackage();
                        // info = CheckTypeIsUseInFormula();
                        info = checkIsInFormula(dsCol, IPrjDetail.FIELD_ENAME,
                                dsCol.fieldByName(IPrjDetail.FIELD_ENAME)
                                        .getString(), IPrjDetail.FORMULA_DET,
                                IPrjDetail.FIELD_CNAME);
                        if (info.getSuccess() == false) {
                            // JOptionPane.showMessageDialog(pnlBase,
                            // "该项在公式中已经被使用，不能修改类型");
                            JOptionPane.showMessageDialog(pnlBase, info
                                    .getsMessage());
                            if (iColTypeSel != null) {
                                rgColType.setValue(iColTypeSel);
                                return;
                            }
                        }
                        setCanEditByColType();
                        iColTypeSel = rgColType.getValue();
                        tfFormula.setValue("");
                        tfFormula.setEditable(false);
                        tfSelValue.setValue("");
                        tfSelValue.setEditable(false);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            });
        }
    }

    // /**
    // * 检查该列类型是否已经在公式中设置，如果设置了，则不允许修改，否则可以修改
    // */
    // private InfoPackage CheckTypeIsUseInFormula() throws Exception {
    // InfoPackage info = new InfoPackage();
    // info.setSuccess(true);
    // String sFilter = IPrjDetail.FORMULA_DET + " is not null and "
    // + IPrjDetail.DETAIL_CODE + "='"
    // + dsDetail.fieldByName(IPrjDetail.DETAIL_CODE).getString()
    // + "'";
    // DataSet dsTemp = itserv.getDataset(IPrjDetail.Table_SIMP_COLINFO,
    // sFilter);
    // dsTemp.beforeFirst();
    // while (dsTemp.next()) {
    // String sField_EName = dsCol.fieldByName(IPrjDetail.FIELD_ENAME)
    // .getString();
    // String sFormula = dsTemp.fieldByName(IPrjDetail.FORMULA_DET)
    // .getString();
    // int ip = sFormula.indexOf(sField_EName);
    // if (ip >= 0) {
    // info.setSuccess(false);
    // return info;
    // }
    // }
    // return info;
    // }

    /**
     * 根据数据类型控制与之关联项显示
     */
    private void setCanEditByColType() throws Exception {
        String selValue = rgColType.getValue().toString();
        if (iState == state_Browse) {
            setEnableByFieldKind(false, true, false, false);
            return;
        }
        DataSet ds = dsComboDP;
        if ("1".equals(selValue)) {
            // 控制显示风格
            String sFilter = "CVALUE=='带小数的数值'";
            ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            setEnableByFieldKind(true, true, false, false);
        } else if ("2".equals(selValue)) {
            String sFilter = "CVALUE=='整数'";
            ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            setEnableByFieldKind(true, true, false, false);
        } else if ("3".equals(selValue)) {
            setEnableByFieldKind(true, true, false, false);
        } else if ("4".equals(selValue)) {
            String sFilter = "CVALUE=='日期'";
            ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            setEnableByFieldKind(false, true, false, false);
            rgFieldKind.setCalPanelEnabled(false);
            rgFieldKind.setSelPanelEnabled(false);
            rgFieldKind.setFormula("");
            rgFieldKind.setSel("");
        }
        cbDisplayStyle.setDataSet(ds);
        cbDisplayStyle.reset();
    }

    /**
     * 根据数据类型控制数据来源的编辑项是否可用
     * 
     * @param bFieldKind
     * @param b1
     * @param b2
     * @param b3
     */
    private void setEnableByFieldKind(boolean bFieldKind, boolean b1,
            boolean b2, boolean b3) {
        JRadioButton[] radiofk = rgFieldKind.getRadios();
        rgFieldKind.setEditable(bFieldKind);
        radiofk[0].setSelected(b1);
        radiofk[1].setSelected(b2);
        radiofk[2].setSelected(b3);
    }

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

    // private void printFrameAction() {
    // Toolkit kit = Toolkit.getDefaultToolkit(); // 获取工具箱
    // Properties props = new Properties();
    // props.put("awt.print.printer", "durango"); // 设置打印属性
    // props.put("awt.print.numCopies", "2");
    // if (kit != null) {
    // // 获取工具箱自带的打印对象
    // // PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);
    // JobAttributes aa = new JobAttributes();
    // PageAttributes bb = new PageAttributes();
    // PrintJob printJob = kit.getPrintJob(this,"print reportUI",aa,bb );
    // // PrintJob printJob = kit.getPrintJob(reportUI,"PRINT
    // FRAME",props,null);
    // if (printJob != null) {
    // Graphics pg = printJob.getGraphics(); // 获取打印对象的图形环境
    // if (pg != null) {
    // try {
    // this.printAll(pg); // 打印该窗体及其所有的组件
    // } finally {
    // pg.dispose(); // 注销图形环境
    // }
    // }
    // printJob.end(); //结束打印作业
    // }
    // }
    // }
}
