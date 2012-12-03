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
 * Title:��Ŀ��ϸ����
 * </p>
 * <p>
 * Description:��Ŀ��ϸ���ܽ���
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author Ǯ�Գ�
 * @version 1.0
 */

public class PrjDetailUI extends FModulePanel implements ActionedUI {

    private static final long serialVersionUID = 1L;

//    private IFormulaOp formulaOp; // ���㹫ʽ����Ľӿ�

    private IPrjDetail itserv; // �������Ľӿ�

    private IPubInterface pubserv;// ���ýӿ�

    // ���ݼ�
    private DataSet dsCol; // ����Ϣ�� dataset, ���ݼ�������ϸ��ĸı���ı�

    private DataSet dsDetail; // ��Ŀ��ϸ��dataset

    private DataSet dsComboDP; // ��ʾ���ı༭��

    private PrjDetailAction PrjAction; // ���ú�����

    private int cellFirstTag = 0; // �Ƿ�Ϊ��һ����Ԫ��

    private String[] bmkDel; // ɾ��������Ϣ�ı��

    private int iDelCount = 0; // ɾ����¼�ĸ���

    private Object iColTypeSel; // �Ѿ�ѡ���������

    private String bmkDetailChange; // dsDetail�ڸı�ʱ��ʱ��λ��

    // �������
    private SysCodeRule codeRule; // ��ϸ��ı������

    private SysCodeRule codeRule_Col; // ����Ϣ�ı������

    private SysCodeRule lvlRule; // ������ı������

    // ״̬����
    // ���ֱ༭״̬
    private int iState = 0; // ��ǰ״̬

    private final static int state_Add = 1; // ���״̬COL

    private final static int state_Delete = 3; // ɾ��״̬COL

    private final static int state_Browse = 0; // ���״̬COL

    private int iState_Detail = 0;

    private final static int state_AddDetail = 4; // ��� detail

    private final static int state_EditDetail = 5; // �޸� detail

    // ����Ԫ��
    private JFileChooser fileChooser; // �ļ�ѡ����

    private ReportUI reportUI; //

    private Node node; // ��������Ϣ��ͷʱ��õĽڵ���Ϣ

    private Cell cellOld; // ��֮ǰ�����Ǹ���Ԫ��

    private CustomTree treePrjDetail; // ��Ŀ��ϸ��tree

    private Report tablePrjDetail; //

    private FTextField textfieldFormula; //

    private FTextField tfName; //

    private FTextField tfPrjDetailName; // ��ϸ������

    private FTextField tfColName; // ������

    private FTextField tfColFName; // ��ȫ��

    private FRadioGroup rgColType; // ������

    private DataSourceRadioGroup rgFieldKind; // ������Դ

    private FTextField tfFormula; // ���㹫ʽ

    private FIntegerField tfPreferenceLevel; // ���ȼ�

    private FTextField tfSelValue; // ѡȡֵ�б�

    private CustomComboBox cbDisplayStyle; // ��ʾ���

    private JCheckBox chkbIsInput; // ���б�����������

    private CustomComboBox cbColTypeStandard; // �б�׼����

    private FButton btnAdd; // ������button

    private FButton btnAddChild; // �����Ӽ��ڵ��е�button

    private FButton btnDelete; // ɾ����button

    private FButton btnShowFormulaPanel; // ������ʽ�༭����

    private FButton btnReadIn; // ���밴ť

    private FTextField tfAcct; // ��ʾ���ÿ�Ŀ�ı༭��

    private FButton btnAcct; // ѡ�񾭼ÿ�Ŀ�İ�ť

    private FSplitPane pnlBase; // �����
    
    private int iDefType = -1;  //���ÿ�Ŀ����� �ӵ���������ȡ

    /**
     * ���õ�ǰ����Ԫ��״̬
     * 
     * @param aState
     *            ״̬
     */
    private void setViewState(int aState) throws Exception {
        setButtonState(aState);
        iState = aState;
        if (aState == state_Browse) {

            textfieldFormula.setEditable(false);

            treePrjDetail.setEnabled(true); // ��Ŀ��ϸ��tree

            tfAcct.setEditable(false);

            btnAcct.setEnabled(false);

            tfPrjDetailName.setEditable(false); // ��ϸ������

            tfColName.setEditable(false);

            rgColType.setEditable(false); // ������

            rgFieldKind.setEditable(false); // ������Դ

            rgFieldKind.setEditable(false);

            tfPreferenceLevel.setEditable(false); // ���ȼ�

            cbDisplayStyle.setEnabled(false); // ��ʾ���

            chkbIsInput.setEnabled(false); // ���б�����������

            cbColTypeStandard.setEnabled(false); // �б�׼����

            btnAdd.setEnabled(false); // ������button

            btnAddChild.setEnabled(false); // �����Ӽ��ڵ��е�button

            btnDelete.setEnabled(false); // ɾ����button

            btnShowFormulaPanel.setEnabled(false); // ������ʽ�༭����

            btnReadIn.setEnabled(false);

        } else {
            treePrjDetail.setEnabled(false); // ��Ŀ��ϸ��tree

            tfPrjDetailName.setEditable(true); // ��ϸ������

            tfAcct.setEditable(false);

            btnAcct.setEnabled(true);

            tfColName.setEditable(true); // ��ȫ��

            rgColType.setEditable(true); // ������

            rgFieldKind.setEditable(true); // ������Դ

            rgFieldKind.setEditable(true);

            tfPreferenceLevel.setEditable(true); // ���ȼ�

            cbDisplayStyle.setEnabled(true); // ��ʾ���

            chkbIsInput.setEnabled(true); // ���б�����������

            cbColTypeStandard.setEnabled(true); // �б�׼����

            btnAdd.setEnabled(true); // ������button

            btnAddChild.setEnabled(true); // �����Ӽ��ڵ��е�button

            btnDelete.setEnabled(true); // ɾ����button

            btnShowFormulaPanel.setEnabled(true); // ������ʽ�༭����

            btnReadIn.setEnabled(true);
        }
    }

    /**
     * �����ʼ������
     */
    public void initize() {
        try {
            PrjAction = new PrjDetailAction();
            // ��panel�Ĳ��ַ�ʽ
            pnlBase = new FSplitPane(); // ����ģ����ײ����
            pnlBase.setOrientation(1);
            pnlBase.setDividerLocation(200);
            /* ���Ԫ�ض��� */
            // �������Ŀ��ϸ�б����
            FPanel pnlLeft = new FPanel();
            RowPreferedLayout layLeft = new RowPreferedLayout(1);
            pnlLeft.setLayout(layLeft);
            FScrollPane scrpTree = new FScrollPane();

            pubserv = PubInterfaceStub.getMethod();

            BeanFactory beanfac = BeanFactoryUtil
                    .getBeanFactory("com/foundercy/fiscalbudget/dicinfo/prjdetail/conf/PrjDetailConf.xml");
            itserv = (IPrjDetail) beanfac.getBean("bmys.PrjDetailTreeService");
            dsDetail = itserv.getDataset(IPrjDetail.Table_SIMP_MASTER, null); // ��Ŀ��ϸ�б�
            dsComboDP = itserv.getComboDPDataset(); // ��ʾ���
            DataSet dsComboSC = itserv.getComboSCDataset(); // ��ȡ��׼��
            codeRule = itserv.getCodeRule(IPrjDetail.DETAILTAB_CODE); // ��ñ������
            codeRule_Col = itserv.getCodeRule(IPrjDetail.DETAILTABITEM_CODE);
            lvlRule = UntPub.lvlRule;
            treePrjDetail = new CustomTree("��Ŀ��ϸ", dsDetail,
                    IPrjDetail.DETAIL_ID, IPrjDetail.DETAIL_NAME, null,
                    codeRule);
            // ����ײ��������嶨�弰���ַ�ʽ
            FPanel pnlRight = new FPanel(); // �������Ŀ��ϸ��Ϣ��ʾ��Ϣ�ĵײ����
            RowPreferedLayout layRight = new RowPreferedLayout(1);
            layRight.setColumnWidth(700);
            pnlRight.setLayout(layRight);
            layRight.setRowGap(4);

            // ���������������岼�ּ�Ԫ�ض���
            FPanel pnlRTop = new FPanel(); // ������壬��Ϊ�������󲿷�,�ϰ벿��Ϊ����Ϣ
            RowPreferedLayout layRTop = new RowPreferedLayout(10);
            pnlRTop.setLayout(layRTop); // ��һ��
            tfPrjDetailName = new FTextField("��ϸ������");
            this.tfName = tfPrjDetailName;
            tfPrjDetailName.setProportion(0.25f);
            FLabel lbPrjDetail = new FLabel();
            lbPrjDetail.setText("��ǰ��Ŀ��ϸ����ϸ��Ϣ");

            // ��ʾ��ϸ���� table��panel
            FPanel pnlDetailInfo = getTablePanel();

            tablePrjDetail = new Report();
            reportUI = new ReportUI(tablePrjDetail);
            initReport();

            FPanel pnlTable = new FPanel();
            RowPreferedLayout layTable = new RowPreferedLayout(5);
            pnlTable.setLayout(layTable);
            pnlTable.add(reportUI, new TableConstraints(5, 4, true, true));

            btnReadIn = new FButton("bt1", "���ļ���ȡ");
            btnReadIn.addActionListener(new ImportBtnActionListener());
            pnlDetailInfo.addControl(pnlTable, new TableConstraints(6, 5,
                    false, false));

            // �Ӹ�ģ���е�BUTTON
            FPanel pnlBlank1 = new FPanel();
            FPanel pnlBlank2 = new FPanel();
            pnlDetailInfo.addControl(pnlBlank1, new TableConstraints(1, 2,
                    false, false));
            pnlDetailInfo.addControl(btnReadIn, new TableConstraints(1, 1,
                    false, false));
            pnlDetailInfo.addControl(pnlBlank2, new TableConstraints(1, 2,
                    false));

            // ������������Ԫ�ض��弰���岼�ַ�ʽ
            FPanel pnlURight = new FPanel(); // ������壬�°벿��Ϊ��ϸ�в���
            pnlURight.setTitle("��ϸ����Ϣ");
            pnlURight.setFontSize(12);
            pnlURight.setFontName("����");
            pnlURight.setBorder();
            RowPreferedLayout layRUnder = new RowPreferedLayout(6);
            pnlURight.setLayout(layRUnder);
            layRUnder.setRowGap(10);
            tfColName = new FTextField("������");
            tfColName.setProportion(0.3f);
            tfColFName = new FTextField("��ȫ��");
            tfColFName.setProportion(0.3f);
            tfColFName.setEditable(false);
            FLabel lbTableTitle = new FLabel();
            lbTableTitle.setText("����ͷ���ԡ�.���ָ�");

            // ������radioѡ��С���
            FPanel pnlColType = new FPanel();
            pnlColType.setTitle("������");
            RowPreferedLayout layColType = new RowPreferedLayout(1);
            pnlColType.setLayout(layColType);
            rgColType = new FRadioGroup("", 0);
            rgColType.setRefModel("1#��С������ֵ+2#������ֵ+3#�ı�+4#����");
            pnlColType.addControl(rgColType, new TableConstraints(1, 10, true,
                    true));

            // ������Դ panel
            FPanel pnlDataSource = new FPanel();
            pnlDataSource.setTitle("������Դ");
            RowPreferedLayout layDataSource = new RowPreferedLayout(1);
            pnlDataSource.setLayout(layDataSource);

            // �������㹫ʽ��ѡȡֵ��panel
            FPanel pnlCalSel = new FPanel(); // �ײ�
            RowPreferedLayout layCalSel = new RowPreferedLayout(1);
            pnlCalSel.setLayout(layCalSel);
            FPanel pnlCalculate = new FPanel(); // ���㹫ʽpanel **
            pnlCalculate.setTitle("���㹫ʽ");
            pnlCalculate.setFontSize(12);
            pnlCalculate.setFontName("����");
            pnlCalculate.setTitledBorder();
            RowPreferedLayout layCalculate = new RowPreferedLayout(6);
            pnlCalculate.setLayout(layCalculate);
            tfFormula = new FTextField(""); // ���㹫ʽ¼���
            tfFormula.setProportion(0.1f);
            tfFormula.setEditable(false);
            tfPreferenceLevel = new FIntegerField("���ȼ�");
            tfPreferenceLevel.setProportion(0.4f);
            // tfPreferenceLevel.setValue( "0" );
            this.textfieldFormula = tfFormula; // ����ʽ¼��򸳸��ֲ�����
            btnShowFormulaPanel = new FButton("bjs", "��");
            // ��Ӱ�ť�ļ����¼����������㹫ʽ���ý���
            btnShowFormulaPanel
                    .addActionListener(new ShowFormulaPanelActionListener());
            FPanel pnlSelValue = new FPanel(); // ѡȡֵpanel **
            pnlSelValue.setTitle("ѡȡֵ�б���Ӣ�ķֺŷָ���ֵ����'һ����Ϣ;������Ϣ'��ʾ����������ѡ��ֵ��");
            RowPreferedLayout laySelValue = new RowPreferedLayout(4);
            pnlSelValue.setLayout(laySelValue);
            tfSelValue = new FTextField(""); // ѡȡֵ�ı༭��
            tfSelValue.setProportion(0.05f);
            tfSelValue.setEditable(false);
            // ���㹫ʽ��ѡȡֵ��panel ����
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

            // ͨ�����÷���ʵ��������Դ��radiogroup��ʵ���Լ����ļ����¼���ʵ��
            rgFieldKind = new DataSourceRadioGroup("", "1#¼��+2#����+3#ѡȡ",
                    pnlCalculate, pnlSelValue, tfPreferenceLevel, tfFormula,
                    tfSelValue);
            pnlDataSource.addControl(rgFieldKind, new TableConstraints(3, 1,
                    true, true));
            // ����������Դ����㹫ʽ�����
            FPanel pnlDataSourceFormula = new FPanel();
            RowPreferedLayout layDataSourceFormula = new RowPreferedLayout(6);
            pnlDataSourceFormula.setLayout(layDataSourceFormula);
            pnlDataSourceFormula.setEnabled(false);

            pnlDataSourceFormula.addControl(pnlDataSource,
                    new TableConstraints(4, 1, false));
            pnlDataSourceFormula.addControl(pnlCalSel, new TableConstraints(6,
                    5, true));

            // ʣ�����������е���Ϣ
            cbDisplayStyle = new CustomComboBox(dsComboDP, "NAME", "NAME");
            cbDisplayStyle.setTitle("��ʾ���");
            cbDisplayStyle.setProportion(0.2f);

            chkbIsInput = new JCheckBox("���б�����������");
            // chkbIsInput.setProportion(0.5f);

            cbColTypeStandard = new CustomComboBox(dsComboSC, "CODE", "NAME");
            cbColTypeStandard.setTitle("�б�׼����");
            cbColTypeStandard.setProportion(0.2f);

            btnAdd = new FButton("bt2", "���ӱ����ڵ�");
            btnAddChild = new FButton("bt3", "�����Ӽ��ڵ�");
            btnDelete = new FButton("bt4", "ɾ��");

            FPanel BlankPanel = new FPanel();
            FPanel BlankPanel1 = new FPanel();

            // �����еĲ���
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

            // ���ÿ�Ŀ vw_fb_acct_economy_prj
            tfAcct = new FTextField();
            tfAcct.setTitle("���ÿ�Ŀ");
            tfAcct.setProportion(0.20f);
            btnAcct = new FButton("btnacct", "ѡ�񾭼ÿ�Ŀ");
            /* ���沼�� */
            // �����ϰ벿���������岼��
            pnlRTop.addControl(tfPrjDetailName, new TableConstraints(1, 4,
                    false));
            pnlRTop.addControl(tfAcct, new TableConstraints(1, 4, false));
            pnlRTop.addControl(btnAcct, new TableConstraints(1, 2, false));
            pnlRTop.addControl(lbPrjDetail, new TableConstraints(1, 10, false));
            pnlRTop.addControl(pnlDetailInfo,
                    new TableConstraints(12, 10, true));

            // ���������������岼��
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

            // ���岼��
            scrpTree.addControl(treePrjDetail);
            FLabel lbTreeName = new FLabel();
            lbTreeName.setText("        ��Ŀ��ϸ");
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
            // �ı�pnlRBsic�ļ��
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
     * ��ȡ��������ϸ�е����
     * 
     * @return ��������ϸ�е����
     */
    private FPanel getTablePanel() {
        FPanel pnlDetailInfo = new FPanel(); // ��������ϵ���Ŀ��ϸ��Ϣ���panel
        RowPreferedLayout layRTable = new RowPreferedLayout(5);
        pnlDetailInfo.setLayout(layRTable);
        pnlDetailInfo.setTitle("��ǰ��ϸ��������");
        pnlDetailInfo.setFontSize(8);
        pnlDetailInfo.setFontName("����");
        CreateChooser();
        return pnlDetailInfo;
    }

    /**
     * ��λ����һ���ڵ�
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
     * ���ü��㹫ʽ������
     * 
     * @param formulaContent
     */
    public void setFormula(String formulaContent) {
        textfieldFormula.setValue(formulaContent);
    }

    /**
     * ��ϸ�����ĵ�������¼�
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

                    // ���þ��ÿ�Ŀ��Ϣ
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

                    // �ı�����Ϣ�����ݼ���������ϸ��Ϣ����FTextField
                    String sDetailCode = dsDetail.fieldByName(
                            IPrjDetail.DETAIL_ID).getString();
                    String sFilter = IPrjDetail.DETAIL_ID + "=" + sDetailCode;
                    dsCol = itserv.getColInfoAccordDetailCode(
                            IPrjDetail.Table_SIMP_COLINFO, sFilter);
                    // ����praID
                    bmkDel = new String[dsCol.getRecordCount()];
                    iDelCount = 0;
                    PrjDetailAction.createParentIDDependCodeRule(dsCol,
                            IPrjDetail.FIELD_ID, codeRule_Col);

                    TableHeader tableHeader = HeaderUtility.createHeader(dsCol,
                            IPrjDetail.FIELD_ID, IPrjDetail.FIELD_CNAME,
                            codeRule_Col, null);
                    createReport(tableHeader);
                    // ��dsCol��λ����һ����¼����ʾ
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
     * ���ͬ������ btnAdd�ļ����¼� ������
     */
    private class addColActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (dsDetail.eof() | dsDetail.bof())
                    return;
                CellSelection cells = reportUI.getCellSelection();
                if (cells == null) {
                    JOptionPane.showMessageDialog(pnlBase, "���ȵ����ļ�");
                    return;
                }
                int col = cells.getColumn();
                int row = cells.getRow();
                CellElement cell = reportUI.getReport()
                        .getCellElement(col, row);
                if (cell == null) {
                    JOptionPane.showMessageDialog(pnlBase, "���ȵ����ļ�");
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
     * btnAddChild�����Ӽ��ڵ�
     */
    private class addChildColActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                CellSelection cells = reportUI.getCellSelection();
                if (cells == null) {
                    JOptionPane.showMessageDialog(pnlBase, "���ȵ����ļ�");
                    return;
                }
                int col = cells.getColumn();
                int row = cells.getRow();
                CellElement cell = reportUI.getReport()
                        .getCellElement(col, row);
                if (cell == null) {
                    JOptionPane.showMessageDialog(pnlBase, "���ȵ����ļ�");
                    return;
                }
                String sStdType = null;
                if (!Common.isNullStr(Common.nonNullStr(cbColTypeStandard
                        .getValue()))) {
                    sStdType = ((MyListElement) cbColTypeStandard.getValue())
                            .getText();
                    if (!"��".equals(sStdType)) {
                        JOptionPane.showMessageDialog(pnlBase, "��׼�в����������ӽڵ�");
                        return;
                    }
                }
                if (dsDetail.eof() | dsDetail.bof())
                    return;
                Cell cell11 = (Cell) reportUI.getReportContent()
                        .getSelectedCell();
                if (cell11 == null) {
                    JOptionPane.showMessageDialog(pnlBase, "��ѡ�����λ��");
                    return;
                }
                JRadioButton[] radios = rgFieldKind.getRadios();
                if (radios[2].isSelected() || radios[1].isSelected()) {
                    JOptionPane.showMessageDialog(pnlBase, "�������Ͳ���������ӽڵ�");
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
     * btnDelete�ļ����¼� ɾ����
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
                    JOptionPane.showMessageDialog(pnlBase, "��ѡ��Ҫɾ���Ľڵ�");
                    return;
                }
                if (isUseInFormula(dsCol.fieldByName(IPrjDetail.FIELD_ENAME)
                        .getString())){
                	JOptionPane.showMessageDialog(Global.mainFrame, "�����Ѿ��ڹ�ʽ��ʹ��");
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
     * �жϸ����Ƿ��ڹ�ʽ��ʹ��
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
     * �����ļ�ѡ����
     * 
     */
    private void CreateChooser() {
        // �����ļ�ѡ����
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        // �趨���õ��ļ��ĺ�׺��
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.getName().endsWith(".xls") ||f.getName().endsWith(".xls".toUpperCase())|| f.isDirectory()) {
                    return true;
                }
                return false;
            }
            public String getDescription() {
                return "�����ļ�(*.xls)";
            }
        });
    }

    /**
     * EXCEL���밴ť�ļ����¼�
     */
    private class ImportBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // �����ļ�ѡ���
                int returnval = fileChooser.showOpenDialog(Global.mainFrame);
                File file = fileChooser.getSelectedFile();
                if (returnval == JFileChooser.APPROVE_OPTION) {
                    ExcelImporter ei = new ExcelImporter(file);
                    // ��ʾ��ȡ��ͷ����
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
     * �ѱ�ͷ��������dataset��
     * 
     */
    private void fillHeaderToDataset() throws Exception {
        // ����node����
        if (dsDetail.eof() || dsDetail.bof() || dsDetail.isEmpty()) {
            JOptionPane.showMessageDialog(pnlBase, "��ѡ����ϸ��");
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
        String nodeID = null; // node��Ψһ��ʾ��
        // ѭ�������ݵ�dataSet
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
                // ����Field_EName
                String sEName = PrjDetailAction.getFieldEName(dsCol, "F", dsCol
                        .toogleBookmark());
                dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sEName);
                // ���� FName
                String sParentFName = PrjAction.getParentFName(dsCol, nodeGet);
                String sFName = nodeGet.getText();
                if (!Common.isNullStr(sParentFName))
                    sFName = sParentFName + "." + sFName;
                dsCol.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sFName);
                dsCol.fieldByName(IPrjDetail.SET_YEAR).setValue(
                        Global.loginYear);
                dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue(
                        columnWidth);
                dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue("������");
                dsCol.fieldByName(IPrjDetail.CALL_PRI).setValue("0");
                dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
                dsCol.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
                dsCol.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");
                dsCol.fieldByName(IPrjDetail.SET_YEAR).setValue(
                        Global.loginYear);
                // dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("0");
                dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("72");
                dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue("������");
                dsCol.fieldByName(IPrjDetail.FIELD_KIND).setValue("¼��");
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
     * �����ұ���ϸ���ѡ��ȷ���ұ���ϸ�����Ƶ���Ϣ
     */
    private void setTextField(String sNa) {
        tfName.setValue(sNa);
    }

    /**
     * ����report
     */
    private void initReport() throws Exception {
        reportUI.clearAll();
        reportUI.getReport().shrinkToFitRowHeight();
        reportUI.getReport().getReportSettings().setPaperSize(
                new PaperSize(2000, 3000));
        reportUI.updateUI();
    }

    /**
     * ������ͷ
     * 
     * @param tableHeader
     * @throws Exception
     */
    private void createReport(TableHeader tableHeader) throws Exception {
        tablePrjDetail.removeAllCellElements();
        tableHeader.setFont(new Font("����", Font.PLAIN, 12));
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
     * �����Ŀ��ϸ
     */
    public void doAdd() {
        try {
            MyTreeNode node = (MyTreeNode) treePrjDetail.getSelectedNode();
            if (node == null) {
                JOptionPane.showMessageDialog(pnlBase, "��ѡ����ӽڵ�");
                treePrjDetail.expandAll();
                return;
            }
            // ȡ���׽ڵ�
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
            // ���� detail_id
            String sFilter = IPrjDetail.SET_YEAR
                    + "="
                    + Global.loginYear
                    + " and "
                    + (Common.isNullStr(sParentID) ? IPrjDetail.PAR_ID
                            + " is null" : IPrjDetail.PAR_ID + "='" + sParentID
                            + "'");

            // ��ʱҪ�жϲ�����Ƿ����㹻���λ����
            // ��������ڱ��������ӽڵ㣬
            // ��������ͬ���ڵ㣨�жϴ˽ڵ��Ƿ��и��ڵ㣬������򸸽ڵ���ͬ�������޸��׽ڵ㣩
            String sCode = null;
            tfAcct.setValue("");
            sCode = pubserv.getNodeID(IPrjDetail.Table_SIMP_MASTER,
                    IPrjDetail.DETAIL_ID, sParentID, sFilter, codeRule);
            String sFirstCode = sCode.substring(0, 1);
            if ("0".equals(sFirstCode))
                sCode = sCode.replace('0', '1');
            // ����lvl_id
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
     * ����ѡ�еĵ�Ԫ��
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
        Cell cellNow = (Cell) reportUI.getReportContent() // ��ǰѡ�еĵ�Ԫ��
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
     * ɾ����Ŀ��ϸ
     */
    public void doDelete() {
        try {
            // ��ȡfb_p_detail�����ݼ�
            String sFilter = IPrjDetail.DETAIL_TYPE + "='"
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString()
                    + "'";
            DataSet dsDataIsUsed = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.TABLENAME_DETAIL, sFilter);
            // ��ȡ������ϸ�����ݼ�
            String sFilterSortToDetail = IPrjDetail.DETAIL_CODE + "='"
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString()
                    + "'";
            DataSet dsDataSort = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.FB_P_SORT_TO_DETAIL, sFilterSortToDetail);
            if (!dsDataIsUsed.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "����Ŀ��ϸ�Ѿ���ʹ��");
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
                if (JOptionPane.showConfirmDialog(pnlBase, "�Ƿ�ȷ��ɾ�����ݣ�", "��ʾ",
                        JOptionPane.OK_CANCEL_OPTION) == 0) {
                    dsDetail.delete();
                    dsCol.clearAll();
                    dsDataSort.clearAll();
                    itserv.dsPost(dsDetail, dsCol, dsDataSort);
                    // itserv.dsPost(dsCol); // by ymq �ϲ���һ��������
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
     * ȡ��������ȡ����Ŀ����������Ϣ��dataset�Ĳ�����
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
                tableHeader.setFont(new Font("����", Font.PLAIN, 12));
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
     * �޸���Ŀ��ϸ
     */
    public void doModify() {
        try {
            if (treePrjDetail.getSelectedNode() == null
                    || treePrjDetail.getSelectedNode() == treePrjDetail
                            .getRoot()) {
                JOptionPane.showMessageDialog(pnlBase, "��ѡ����Ŀ��ϸ");
                return;
            }
            String sFilter = IPrjDetail.DETAIL_TYPE + "="
                    + dsDetail.fieldByName(IPrjDetail.DETAIL_ID).getString();
            DataSet dsDataIsUsed = itserv.getColInfoAccordDetailCode(
                    IPrjDetail.TABLENAME_DETAIL, sFilter);
            if (!dsDataIsUsed.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "����Ŀ��ϸ�Ѿ���ʹ��");
                return;
            } else {
                if (dsDetail.eof()
                        || dsDetail.bof()
                        || dsDetail.isEmpty()
                        || treePrjDetail.getSelectedNode() == treePrjDetail
                                .getRoot()) {
                    JOptionPane.showMessageDialog(pnlBase, "��ѡ����Ŀ��ϸ");
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
     * ������Ŀ��ϸ������Ϣ
     */
    public void doSave() {
        try {
            // �ж�detail��dsCol�Ƿ����޸ģ�������򱣴���Ϣ
            if (Common.isNullStr(tfName.getValue().toString().trim())) {
                JOptionPane.showMessageDialog(pnlBase, "��ϸ�����Ʋ���Ϊ�գ�");
                return;
            }
            if (dsCol.isEmpty()) {
                JOptionPane.showMessageDialog(pnlBase, "û�пɱ������ϸ���У�");
                return;
            }
//            if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue()))) {
//                JOptionPane.showMessageDialog(pnlBase, "���ÿ�Ŀ����Ϊ�գ�");
//                return;
//            }
            if (dsDetail.eof() | dsDetail.bof())
                return;
            InfoPackage infoReturn = new InfoPackage();
            infoReturn = saveColInfoFromPanel(dsCol.toogleBookmark());
            // ����DSCOL
            if (infoReturn.getSuccess() == false) {
                JOptionPane
                        .showMessageDialog(pnlBase, infoReturn.getsMessage());
                return;
            }
            infoReturn = checkDetail(dsCol.toogleBookmark()); // ������ݺϷ���
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
                JOptionPane.showMessageDialog(pnlBase, "��ϸ�������Ѿ���ʹ�ã����޸ģ�");
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
            // ��������״̬����������
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
     * ����ֶ��ڹ�ʽ���Ƿ�ʹ��
     * 
     * @param ds
     * @param fieldName
     *            Ӣ���ֶ�
     * @param fieldValue
     *            Ҫ�����ֶ�ֵ
     * @param formulaName
     *            ��ʽ���ֶ�����
     * @return �����
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
                info.setsMessage("���ֶ��Ѿ��ڹ�ʽ��" + cname + "����ʹ��");
                return info;
            }
        }
        return info;
    }

    /**
     * �ж��Ƿ����ظ�������
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
                    // ������
                    if (j != k) {
                        info.setsMessage("��" + arrayName1[j] + "�����ظ���");
                        info.setSuccess(false);
                        return info;
                    }
                }
            }
        }
        return info;
    }

    /**
     * �Ѵ�ʱdsCol����Ϣ��Ӧ�������ϲ���һ��ʾ����
     * 
     * @param �Ƿ�ѽ��������
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
            // ���ü��㹫ʽ��ʱ��Ҳ���뷴תһ��
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
        // ����������
        if (sColType.equals("������")) {
            String sFilter = "CVALUE=='��С������ֵ'";
            DataSet ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            cbDisplayStyle.setDataSet(ds);
            cbDisplayStyle.reset();
            rgColType.setValue("1 ");
            radiosaa[0].setSelected(true);
        } else if (sColType.equals("����")) {
            String sFilter = "CVALUE=='����'";
            DataSet ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            cbDisplayStyle.setDataSet(ds);
            cbDisplayStyle.reset();
            rgColType.setValue("2");
            radiosaa[1].setSelected(true);
        } else if (sColType.equals("�ַ���")) {
            cbDisplayStyle.setDataSet(dsComboDP);
            cbDisplayStyle.reset();
            rgColType.setValue("3");
            radiosaa[2].setSelected(true);
        } else if (sColType.equals("������")) {
            String sFilter = "CVALUE=='����'";
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
        // ����������Դ
        if (sFieldKind.equals("¼��")) {
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
        } else if (sFieldKind.equals("����")) {
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
        } else if (sFieldKind.equals("ѡȡ")) {
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
        // ���ü���combobox
        cbDisplayStyle.setSelectedValue(sDisPlay);
        cbColTypeStandard.setSelectedValue(sColStdType);

        // ����"���б�������"
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
     * ������ȡ����ϸ��Ϣ
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
        // ����
        dsCol.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
        dsCol.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");
        dsCol.fieldByName(IPrjDetail.PRIMARY_PROPFIELD).setValue("");
        // ���ȼ�
        dsCol.fieldByName(IPrjDetail.CALL_PRI).setValue(
                (tfPreferenceLevel.getValue() == null) ? "0"
                        : tfPreferenceLevel.getValue());
        // ����������,ename��ֵ
        String sColType = (rgColType.getValue() == null) ? "" : rgColType
                .getValue().toString();
        int iDataTypeTag = Integer.parseInt(Common.isNullStr(sColType) ? "1"
                : sColType);
        String sFieldEName = null;
        String sDataTypeValue = null;
        switch (iDataTypeTag) {
        case 1:
            sDataTypeValue = "������";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "F", bmk);
            break;
        case 2:
            sDataTypeValue = "����";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "N", bmk);
            break;
        case 3:
            sDataTypeValue = "�ַ���";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "C", bmk);
            break;
        case 4:
            sDataTypeValue = "������";
            sFieldEName = PrjDetailAction.getFieldEName(dsCol, "D", bmk);
            break;
        }
        dsCol.fieldByName(IPrjDetail.DATA_TYPE).setValue(sDataTypeValue);

        if ((Common.isNullStr(dsCol.fieldByName("PARID").getString().trim()) && dsCol
                .fieldByName("LVL_ID").getString().length() == 4)
                || !Common.isNullStr(dsCol.fieldByName("PARID").getString()
                        .trim()))
            // ������׽ڵ�Ϊ�գ�����������ڵ�
            // ���߸����ڵ㲻Ϊ��
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sFieldEName);
        else
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue("");
        // ������Դ
        if (rgFieldKind.getValue() == null) {
            JRadioButton radios[] = rgFieldKind.getRadios();
            radios[0].setSelected(true);
        }
        int iFieldKindTag = Integer.parseInt(rgFieldKind.getValue().toString());
        String sFieldKindValue = null;
        switch (iFieldKindTag) {
        case 1:
            // TODO
            sFieldKindValue = "¼��";
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue("");
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue("");
            // ���㹫ʽ
            break;
        case 2:
            sFieldKindValue = "����";
            Object oFormula = tfFormula.getValue();
            String sFormula = (oFormula == null) ? "" : oFormula.toString();
            String sFormulaData = pubserv.replaceTextExDs(sFormula, 0, dsCol,
                    IPrjDetail.FIELD_FNAME, IPrjDetail.FIELD_ENAME);
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue(sFormulaData);
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue("");
            break;
        case 3:
            sFieldKindValue = "ѡȡ";
            // ѡȡֵ
            dsCol.fieldByName(IPrjDetail.PICK_VALUES).setValue(
                    tfSelValue.getValue());
            dsCol.fieldByName(IPrjDetail.FORMULA_DET).setValue("");
            break;
        }
        dsCol.fieldByName(IPrjDetail.FIELD_KIND).setValue(sFieldKindValue);
        // �����п�
        Cell cell = (Cell) reportUI.getReportContent().getSelectedCell();
        Double dbCellWith = new Double("0");
        if (cell != null)
            dbCellWith = new Double(reportUI.getReport().getColumnWidth(
                    cell.getColumn()));
        dsCol.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue(dbCellWith);

        // ��ʾ��ʽ
        dsCol.fieldByName(IPrjDetail.DISPLAY_FORMAT).setValue(
                cbDisplayStyle.getValue());
        // �Ƿ����Ϊ��
        if (chkbIsInput.isSelected())
            dsCol.fieldByName(IPrjDetail.NOTNULL).setValue("1");
        else
            dsCol.fieldByName(IPrjDetail.NOTNULL).setValue("0");
        // �б�׼����
        String sStdType = null;
        if (!Common.isNullStr(Common.nonNullStr(cbColTypeStandard.getValue()))) {
            sStdType = ((MyListElement) cbColTypeStandard.getValue()).getId();
            dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue(
                    Common.isNullStr(sStdType) ? "0" : sStdType);
        }
        dsCol.fieldByName(IPrjDetail.STD_TYPE).setValue(sStdType);
        // �����Ĭ��Ϊ��
        dsCol.fieldByName(IPrjDetail.IS_SUMCOL).setValue("0");
        dsCol.fieldByName(IPrjDetail.IS_HIDECOL).setValue("0");
        // ���Ϊ֧���ܼ�,���޸�ename
        if ("01".equals(sStdType) || "1".equals(sStdType)) {
            dsCol.fieldByName(IPrjDetail.FIELD_ENAME).setValue("Total_Prices");
        }
        infoReturn = checkDetail(bmk);
        dsCol.maskDataChange(false);
        return infoReturn;
    }

    /**
     * ��ȡ�������볤��(��2|5,01002),�򷵻�3
     * 
     * @return ���
     * @param aCodeRule
     *            ���볤��
     */
    public int getCodeRulelength(SysCodeRule aCodeRule) {
        int iLastTag = aCodeRule.originRuleStr().lastIndexOf("|");
        int iCodeRuleLength = Integer.parseInt(aCodeRule.originRuleStr()
                .substring(iLastTag));
        return iCodeRuleLength;
    }

    /**
     * ��鱣��ʱ��ϸ��Ϣ�Ƿ����Ҫ��
     */
    private InfoPackage checkDetail(String bmk) throws Exception {
        dsCol.maskDataChange(true);
        String bmkBefore = dsCol.toogleBookmark();
        dsCol.gotoBookmark(bmk);
        InfoPackage info = new InfoPackage();
        if (tfColName.getValue() == null) {
            info.setSuccess(false);
            info.setsMessage("�����������ƣ�");
            return info;
        }
        if (("����".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString()))
                && "������".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                        .getString())) {
            info.setSuccess(false);
            info.setsMessage("��'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'�����������ͣ�����ͨ�����������ݣ�");
            return info;
        }
        if ((dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString().equals("����"))
                && (dsCol.fieldByName(IPrjDetail.DATA_TYPE).getString()
                        .equals("�ַ���"))) {
            info.setSuccess(false);
            info.setsMessage("��'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'�����ı����ͣ�����ͨ�����������ݣ�");
            return info;
        }
        if (Common.isNullStr(dsCol.fieldByName(IPrjDetail.FORMULA_DET)
                .getString().trim())
                && ("����".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND)
                        .getString()))) {
            info.setSuccess(false);
            info.setsMessage("��������'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'�ļ��㹫ʽ");
            return info;
        }
        if (Common
                .isNullStr(dsCol.fieldByName(IPrjDetail.CALL_PRI).getString())
                && ("����".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND)
                        .getString()))) {
            info.setSuccess(false);
            info.setsMessage("��������'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'�Ĺ�ʽ���ȼ�");
            return info;
        }
        if (Integer.parseInt(Common.isNullStr(dsCol.fieldByName(
                IPrjDetail.CALL_PRI).getString()) ? "0" : dsCol.fieldByName(
                IPrjDetail.CALL_PRI).getString()) > dsCol.getRecordCount()) {
            info.setSuccess(false);
            info.setsMessage("�������ȼ����ֳ������������������");
            return info;
        }
        if ("ѡȡ".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString())
                && Common.isNullStr(dsCol.fieldByName(IPrjDetail.PICK_VALUES)
                        .getString().trim())) {
            info.setSuccess(false);
            info.setsMessage("��������'"
                    + dsCol.fieldByName(IPrjDetail.FIELD_FNAME).getString()
                    + "'��ѡȡֵ�б�");
            return info;
        }
        if ("ѡȡ".equals(dsCol.fieldByName(IPrjDetail.FIELD_KIND).getString())) {
            String sPickValue = dsCol.fieldByName(IPrjDetail.PICK_VALUES)
                    .getString();
            char[] sPickValues = new char[sPickValue.length()];
            sPickValue.getChars(0, sPickValue.length(), sPickValues, 0); // ��
            // ��һ����ʼ
            int iLen = sPickValue.length();
            if (iLen > 1) {
                if (';' == sPickValues[0] || ';' == sPickValues[iLen - 1]) {
                    info.setSuccess(false);
                    info.setsMessage("ѡȡֵ�б�����';'��ͷ�����");
                    return info;
                }
                String sDataType = dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                        .getString();
                if ("������".equals(sDataType) || "����".equals(sDataType)) {
                    char[] sLimit = { '0', '1', '2', '3', '4', '5', '6', '7',
                            '8', '9', ';', '.' };
                    Arrays.sort(sLimit);
                    for (int i = 1; i < iLen; i++) {
                        if (Arrays.binarySearch(sLimit, sPickValues[i]) < 0) {
                            info.setSuccess(false);
                            info.setsMessage("ѡȡֵ�б���ֻ�ܳ������֣�");
                            return info;
                        }
                    }
                    String[] value = sPickValue.split(";");
                    for (int i = 0; i < value.length; i++) {
                        if (!Common.isNumber(value[i])) {
                            info.setSuccess(false);
                            info.setsMessage("ѡȡֵ�б���ֻ�ܳ������֣�");
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
                info.setsMessage("ֻ�ܱ�ʶһ�б�׼��Ϊ'֧���ܼ�'!");
                return info;
            }
            if ("�ַ���".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                    .getString())
                    || "������".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                            .getString())) {
                info.setSuccess(false);
                info.setsMessage("��׼��'֧���ܼ�'����Ϊ��ֵ���ͣ�!");
                return info;
            }
        } else {
            String[] arrayKind = { "2", "3" };
            Arrays.sort(arrayKind);
            int index = Arrays.binarySearch(arrayKind, dsCol.fieldByName(
                    IPrjDetail.STD_TYPE).getString());
            if (index > 0
                    && !"������".equals(dsCol.fieldByName(IPrjDetail.DATA_TYPE)
                            .getString())) {
                info.setSuccess(false);
                info.setsMessage("��׼��'��ʼ��'��'������'����Ϊ��������");
                return info;
            }
        }
        // �ж�ename�Ƿ�Ϸ�
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
        // info.setsMessage("�г�����Χ��");
        // return info;
        // }
        dsCol.gotoBookmark(bmkBefore);
        dsCol.maskDataChange(false);
        info.setSuccess(true);
        return info;
    }

    /**
     * ���޸ĵ����ݱ��浽dataset��
     * 
     * @author Administrator
     * 
     */
    private class FillToColDatasetListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            try {
                dsCol.maskDataChange(true);
                InfoPackage info = new InfoPackage();
                Cell cellNow = (Cell) reportUI.getReportContent() // ��ǰѡ�еĵ�Ԫ��
                        .getSelectedCell();
                String bmkNow = cellNow.getBookmark();
                String bmkOld = null;
                // ���Ϊ�գ���Ĭ��Ϊ��һ����Ԫ��
                if (cellFirstTag == 0) {
                    cellOld = (Cell) reportUI.getReportContent()
                            .getCellElement(0, 0);
                    cellFirstTag = 1;
                }
                // �����֮ǰ���Ѿ����cell,������cell��Ӧ��ֵ���浽dsCol��
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
                if (iChildNum > 0) // ���ѡ��ĵ�Ԫ�����ӽڵ㣬��ֻ�����޸�����
                    setPanelInfoEnable(false);
                else
                    setPanelInfoEnable(true);
                // JRadioButton[] radiofk = rgColType.getRadios();
                // if (radiofk[3].isSelected() == true
                // && iState_Detail != state_Browse)
                // rgFieldKind.setEnabled(false);
                // else
                // rgFieldKind.setEnabled(true);
                // ������ʾ���㹫ʽ��ť
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
     * ��ȡ��ǰ��Ŀ��ϸ�еı�׼��Ϊ�ܼ�֧���ļ�¼��
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
     * ���ֻ������һ��֧���ܼ�
     * 
     * @return
     * @throws Exception
     */
    private InfoPackage checkStdType() throws Exception {
        // �ڱ����ʱ��ѭ��dscol,��ѯ����ֻ������һ��֧���ܼ�
        InfoPackage info = new InfoPackage();
        int iStdTypeNum = 0;
        iStdTypeNum = getStdTypeNum(dsCol);
        if (iStdTypeNum != 1) {
            info.setSuccess(false);
            info.setsMessage("��������һ����׼��Ϊ'֧���ܼ�'!");
            return info;
        }
        info.setSuccess(true);
        return info;
    }

    /**
     * ����TOOLBAR�ϰ�ť��״̬
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
     * ��ȡ���ɼ��㹫ʽtree��dataset
     * 
     * @param dsPost
     * @return
     * @throws Exception
     */
    private DataSet getFormulaData(DataSet dsPost, String aFieldID)
            throws Exception {
        DataSet dsMid = (DataSet) dsPost.clone();
        DataSet ds = DataSet.create();
        // ��ȡ���е�PARID�ֶΣ����洢��������
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
                    "�ַ���"))
                continue;
            if (dsMid.fieldByName(IPrjDetail.DATA_TYPE).getString().equals(
                    "������"))
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
     * cname�ı�fname���ŷ����ı� ���ұ�ͷ��صĵ�Ԫ��Ҳ�����ı�
     * ͬʱ����ýڵ����ӽڵ�Ļ����޸����иýڵ���ӽڵ��fname�ж�Ӧ�ýڵ������
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
     * ���þ��ÿ�Ŀ��ť�ļ����¼�
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
				// ����ط�Ҫ�ж�������������ӵ�ʱ��϶��Ƿ��Զ����
				// �޸ĵ�ʱ����Ҫ�жϿ���������Ƿ�bsi_idΪ�գ������Ϊ�գ���϶��Ƿ��Զ���
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
	 * �ղ���
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
     * ���������Ϣ�Ƿ�ɱ༭( ����ѡ�е�cell�Ƿ����ӽڵ� )
     * 
     * @param isEnable
     */
    private void setPanelInfoEnable(boolean isEnable) {
        if (isEnable == false) {
            rgColType.setEditable(false); // ������

            rgFieldKind.setEditable(false); // ������Դ

            rgFieldKind.setEditable(false);

            tfPreferenceLevel.setEditable(false); // ���ȼ�

            cbDisplayStyle.setEnabled(false); // ��ʾ���

            chkbIsInput.setEnabled(false); // ���б�����������

            cbColTypeStandard.setEnabled(false); // �б�׼����

            btnShowFormulaPanel.setEnabled(false); // ������ʽ�༭����

        } else {
            if (iState == state_Browse)
                return;
            rgColType.setEditable(true); // ������

            rgFieldKind.setEditable(true); // ������Դ

            rgFieldKind.setEditable(true);

            tfPreferenceLevel.setEditable(true); // ���ȼ�

            cbDisplayStyle.setEnabled(true); // ��ʾ���

            chkbIsInput.setEnabled(true); // ���б�����������

            cbColTypeStandard.setEnabled(true); // �б�׼����

            JRadioButton[] radios = rgFieldKind.getRadios();
            if (radios[1].isSelected() == true)
                btnShowFormulaPanel.setEnabled(true);
            else
                btnShowFormulaPanel.setEnabled(false);
        }

    }

    // ** �������㹫ʽ�ı༭����ļ�����
    private class ShowFormulaPanelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String selColType = rgColType.getValue().toString();
                String selFieldKind = rgFieldKind.getValue().toString();
                String sCName = dsCol.fieldByName(IPrjDetail.FIELD_CNAME)
                        .getString();
                if ("3".equals(selColType) && "2".equals(selFieldKind)) {
                    JOptionPane.showMessageDialog(pnlBase, "�С�" + sCName
                            + "�������ı�����,����ͨ��������");
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
//                CustomTree treeFormula = new CustomTree("��ѡ��", dsFormulaCol,
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
     * �������������Ӽ����¼�
     * 
     */
    private void addRgColTypeActionListener() throws Exception {
        // �����͵ļ����¼�
        JRadioButton[] radios = rgColType.getRadios();
        for (int i = 0; i < radios.length; i++) {
            radios[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // ������ʾ��ʽ
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
                            // "�����ڹ�ʽ���Ѿ���ʹ�ã������޸�����");
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
    // * �����������Ƿ��Ѿ��ڹ�ʽ�����ã���������ˣ��������޸ģ���������޸�
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
     * �����������Ϳ�����֮��������ʾ
     */
    private void setCanEditByColType() throws Exception {
        String selValue = rgColType.getValue().toString();
        if (iState == state_Browse) {
            setEnableByFieldKind(false, true, false, false);
            return;
        }
        DataSet ds = dsComboDP;
        if ("1".equals(selValue)) {
            // ������ʾ���
            String sFilter = "CVALUE=='��С������ֵ'";
            ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            setEnableByFieldKind(true, true, false, false);
        } else if ("2".equals(selValue)) {
            String sFilter = "CVALUE=='����'";
            ds = DataSetUtil.filterBy(dsComboDP, sFilter);
            setEnableByFieldKind(true, true, false, false);
        } else if ("3".equals(selValue)) {
            setEnableByFieldKind(true, true, false, false);
        } else if ("4".equals(selValue)) {
            String sFilter = "CVALUE=='����'";
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
     * �����������Ϳ���������Դ�ı༭���Ƿ����
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
    // Toolkit kit = Toolkit.getDefaultToolkit(); // ��ȡ������
    // Properties props = new Properties();
    // props.put("awt.print.printer", "durango"); // ���ô�ӡ����
    // props.put("awt.print.numCopies", "2");
    // if (kit != null) {
    // // ��ȡ�������Դ��Ĵ�ӡ����
    // // PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);
    // JobAttributes aa = new JobAttributes();
    // PageAttributes bb = new PageAttributes();
    // PrintJob printJob = kit.getPrintJob(this,"print reportUI",aa,bb );
    // // PrintJob printJob = kit.getPrintJob(reportUI,"PRINT
    // FRAME",props,null);
    // if (printJob != null) {
    // Graphics pg = printJob.getGraphics(); // ��ȡ��ӡ�����ͼ�λ���
    // if (pg != null) {
    // try {
    // this.printAll(pg); // ��ӡ�ô��弰�����е����
    // } finally {
    // pg.dispose(); // ע��ͼ�λ���
    // }
    // }
    // printJob.end(); //������ӡ��ҵ
    // }
    // }
    // }
}
