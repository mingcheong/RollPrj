package gov.nbcs.rp.query.ui;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.gl.viewer.FListPanel;
import com.foundercy.pf.util.Global;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.query.action.QrBudgetAction;
import gov.nbcs.rp.query.action.QrSync;
import gov.nbcs.rp.query.ibs.IQrBudget;
import gov.nbcs.rp.sync.ibs.SyncInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SameforTemp extends RpModulePanel
{
  private FSplitPane pnlBase;
  private CustomTree treeDiv = null;
  private DataSet dsDiv;
  private PrjAuditDTO pd = new PrjAuditDTO();

  CustomTree xmTree = null;

  FTextField fxmbm = null;

  FTextField fxmmc = null;

  FComboBox fpc = null;

  FComboBox fpczt = null;

  FListPanel bottomPanel = null;

  FTextArea fSbly = null;

  String[] kmxzListString = null;
  FSplitPane pnlInfoXm;
  private CustomTable table;
  private String[] divCodes;
  private DataSet dsAll;
  private CustomTable tableAll;
  private DataSet dsStep;
  private FButton btnE;

  public void initize()
  {
    try
    {
      initData();
      add(getBasePanel());
      createToolBar();
    }
    catch (Exception ee) {
      ErrorInfo.showErrorDialog(ee, "初始化面板失败");
    }
  }

  private void initData() throws Exception
  {
  }

  public FSplitPane getBasePanel() throws Exception {
    this.pnlBase = new FSplitPane();
    this.pnlBase.setDividerLocation(200);
    this.pnlBase.addControl(getLeftTopPanel());
    this.pnlBase.addControl(getRPanel());
    return this.pnlBase;
  }

  private FPanel getLeftTopPanel()
  {
    FPanel pnlLTop = new FPanel();
    FLabel label = new FLabel();
    label.setText("预算单位");
    RowPreferedLayout lay = new RowPreferedLayout(1);
    pnlLTop.setLayout(lay);
    getDivTree();

    pnlLTop.addControl(getTreePanel(this.treeDiv), new TableConstraints(10, 1, true, true));

    pnlLTop.setTopInset(10);
    pnlLTop.setLeftInset(10);
    pnlLTop.setRightInset(10);
    pnlLTop.setBottomInset(10);
    return pnlLTop;
  }

  private FScrollPane getTreePanel(CustomTree tree)
  {
    FScrollPane pnlTree = new FScrollPane();
    pnlTree.addControl(tree);
    return pnlTree;
  }

  private CustomTree getDivTree()
  {
    try
    {
      this.dsDiv = QrBudgetAction.getMethod().getDivDataPop(Global.loginYear, this.pd.getBathcNo());

      this.treeDiv = new CustomTree("单位信息表", this.dsDiv, "En_ID", "CODE_NAME", "PARENT_ID", null, "DIV_CODE", true);

      this.treeDiv.setIsCheckBoxEnabled(true);
      this.treeDiv.reset();
    } catch (Exception e) {
      ErrorInfo.showErrorDialog(e, "创建单位树失败");
    }
    return this.treeDiv;
  }

  private FPanel getRPanel() {
    FPanel pnlInfo = new FPanel();

    RowPreferedLayout layCF = new RowPreferedLayout(1);
    pnlInfo.setLayout(layCF);

    RowPreferedLayout xmLay = new RowPreferedLayout(8);
    FPanel xmxx = new FPanel();
    xmxx.setLayout(xmLay);
    this.fxmbm = new FTextField("项目编码");
    this.fxmmc = new FTextField("项目名称");

    this.fpc = new FComboBox("批次");
    String pc = "1#一上+2#二上";
    this.fpc.setRefModel(pc);
    this.fpc.setValue("1");

    this.fpczt = new FComboBox("状态");
    String pczt = "0#申报+1#审核";

    this.fpczt.setRefModel(pczt);
    this.fpczt.setValue("0");

    this.fpc.addValueChangeListener(new ValueChangeListener() {
      public void valueChanged(ValueChangeEvent e) {
        SameforTemp.this.doAdd();
      }
    });
    this.fpczt.addValueChangeListener(new ValueChangeListener() {
      public void valueChanged(ValueChangeEvent e) {
        SameforTemp.this.doAdd();
      }
    });
    xmxx.setLeftInset(10);
    xmxx.setRightInset(10);
    xmxx.setBottomInset(10);
    xmxx.setTopInset(10);

    xmxx.addControl(this.fpc, new TableConstraints(1, 1, false, true));
    xmxx.addControl(this.fpczt, new TableConstraints(1, 1, false, true));
    xmxx.addControl(this.fxmbm, new TableConstraints(1, 2, false, true));
    xmxx.addControl(this.fxmmc, new TableConstraints(1, 2, false, true));

    this.pnlInfoXm = new FSplitPane();
    this.pnlInfoXm.setOrientation(0);
    this.pnlInfoXm.setDividerLocation(50);
    this.pnlInfoXm.addControl(xmxx);

    pnlInfo.addControl(this.pnlInfoXm);

    FPanel tabpane = new FPanel();
    try
    {
      RowPreferedLayout lay1 = new RowPreferedLayout(1);
      tabpane.setLayout(lay1);
      String[] columnText = { "单位编码", "单位名称", "项目编码", "项目名称", "金额" };

      String[] columnField = { "div_code", "div_name", "prj_code", "prj_name", "total_money" };

      this.table = new CustomTable(columnText, columnField, null, true, null);
      setTableProp();
      tabpane.addControl(this.table, new TableConstraints(15, 2, true, true));
    }
    catch (Exception ee)
    {
      ErrorInfo.showErrorDialog(ee, "生成项目列表失败");
    }

    this.pnlInfoXm.addControl(tabpane);

    return pnlInfo;
  }

  private void setTableProp() throws Exception
  {
    this.table.getTable().setAutoResizeMode(0);
    this.table.reset();

    this.table.getTable().getColumnModel().getColumn(0).setPreferredWidth(200);
    this.table.getTable().getColumnModel().getColumn(1).setPreferredWidth(200);
    this.table.getTable().getColumnModel().getColumn(2).setPreferredWidth(200);
    this.table.getTable().getColumnModel().getColumn(3).setPreferredWidth(200);
    this.table.getTable().getColumnModel().getColumn(4).setPreferredWidth(200);
    this.table.getTable().getColumnModel().getColumn(5).setPreferredWidth(200);

    this.table.getTable().setAutoResizeMode(0);
    this.table.getTable().setRowHeight(30);
    this.table.getTable().getTableHeader().setBackground(new Color(250, 228, 184));

    this.table.getTable().getTableHeader().setPreferredSize(new Dimension(4, 30));
  }

  public void doAdd()
  {
    try
    {
      this.divCodes = getDivRecords();
      if ((this.divCodes == null) || (this.divCodes.length == 0)) {
        JOptionPane.showMessageDialog(Global.mainFrame, "请选择要查询单位");
        return;
      }

      String str1 = String.valueOf(this.fxmbm.getValue());
      String str2 = String.valueOf(this.fxmmc.getValue());
      String str3 = String.valueOf(this.fpc.getValue());
      String str4 = String.valueOf(this.fpczt.getValue());

      Map parMap = new HashMap();
      parMap.put("fxmbm", str1);
      parMap.put("fxmmc", str2);
      parMap.put("batch_no", str3);
      parMap.put("data_type", str4);

      this.dsAll = QrBudgetAction.getMethod().findRPBUDGET(this.divCodes, parMap, Global.loginYear);

      this.table.setDataSet(this.dsAll);
      setTableProp();
    }
    catch (Exception ee) {
      ErrorInfo.showErrorDialog(ee, "查询出错");
    }
  }

  private String[] getDivRecords() {
    MyTreeNode root = (MyTreeNode)this.treeDiv.getRoot();
    Enumeration enumeration = root.breadthFirstEnumeration();
    int i = 0;
    this.divCodes = new String[this.treeDiv.getSelectedNodeCount(true)];
    while (enumeration.hasMoreElements()) {
      MyTreeNode node = (MyTreeNode)enumeration.nextElement();
      if (node.getChildCount() > 0) {
        continue;
      }
      PfTreeNode pNode = (PfTreeNode)node.getUserObject();
      if (pNode == null) {
        continue;
      }
      if (pNode.getIsSelect())
      {
        this.divCodes[i] = pNode.getValue();
        i++;
      }
    }
    try {
      String bmk = this.dsDiv.toogleBookmark();
      this.dsDiv.maskDataChange(true);
      for (int k = 0; k < this.divCodes.length; k++) {
        if (this.dsDiv.locate("EID", this.divCodes[k])) {
          this.divCodes[k] = this.dsDiv.fieldByName("DIV_CODE").getString();
        }
      }
      this.dsDiv.maskDataChange(false);
      this.dsDiv.gotoBookmark(bmk);
    } catch (Exception ee) {
      ErrorInfo.showErrorDialog(ee, "获取所选单位出错");
    }
    return this.divCodes;
  }

  public void doSave()
  {
    try
    {
   
    	
      String str3 = String.valueOf(this.fpc.getValue());
      String str4 = String.valueOf(this.fpczt.getValue());
      QrSync.getMethod().syncTableByType(Global.loginYear, 1, " and  batch_no='" + str3 + "' and data_type='" + str4 + "'");

      JOptionPane.showMessageDialog(Global.mainFrame, "同步成功");
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(Global.mainFrame, "同步失败");
      e.printStackTrace();
    }
  }

  public void doImpProject()
  {
  }

  public void doDelete() {
    int[] rows = this.table.getTable().getSelectedRows();
    if (rows.length == 0) {
      JOptionPane.showMessageDialog(Global.mainFrame, "请选择具体项目");
      return;
    }
    try
    {
      String xmbms = "";

      for (int i = 0; i < rows.length; i++) {
        Object check = this.table.getTable().getValueAt(rows[i], 0);
        if ((!((Boolean)check).booleanValue()) || 
          (!this.table.getDataSet().gotoBookmark(this.table.rowToBookmark(rows[i])))) {
          continue;
        }
        String xmbm = this.table.getDataSet().fieldByName("prj_code").getString();

        xmbms = xmbms + "'" + xmbm + "',";
      }

      if (xmbms.length() > 0) {
        xmbms = xmbms.substring(0, xmbms.length() - 1);
      }
      String wheresql = " and prj_code in(" + xmbms + ") and batch_no='" + String.valueOf(this.fpc.getValue()) + "' and data_type='" + String.valueOf(this.fpczt.getValue()) + "'";
      QrBudgetAction.getMethod().DeleteTempPro(wheresql, Global.loginYear, GlobalEx.getCurrRegion());
      JOptionPane.showMessageDialog(Global.mainFrame, "删除成功");
      doAdd();
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(Global.mainFrame, "删除失败");
      e.printStackTrace();
    }
  }
}