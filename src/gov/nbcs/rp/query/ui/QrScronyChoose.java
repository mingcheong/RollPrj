package gov.nbcs.rp.query.ui;

import gov.nbcs.rp.query.action.QrBudgetAction;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class QrScronyChoose extends FDialog
{
  private FComboBox Choose = null;
  private FComboBox Choose2 = null;
  private FTextArea Remark = null;
  String prjCodes = "";
  String xmxhs = "";
  List prjCodelist;
  private static final long serialVersionUID = 1L;

  public QrScronyChoose(String prjCodes, String xmxhs, List prjCodelist)
  {
    super(Global.mainFrame);
    setSize(400, 300);
    setResizable(false);
    setTitle("同步选择");
    getContentPane().add(getBasePanel());
    dispose();
    setModal(true);
    this.prjCodes = prjCodes;
    this.xmxhs = xmxhs;
    this.prjCodelist = prjCodelist;
  }

  private FPanel getBasePanel()
  {
    FPanel pnl = new FPanel();
    RowPreferedLayout lay = new RowPreferedLayout(1);
    pnl.setLayout(lay);

    String ChooseBh = "#一上一下+#二上二下";
    this.Choose = new FComboBox("同步状态");
    this.Choose2 = new FComboBox("状态确认");
    this.Choose.setRefModel(ChooseBh);
    this.Choose2.setRefModel(ChooseBh);
    this.Remark = new FTextArea("备注说明");
    this.Remark.setProportion(0.001F);
    this.Remark.getEditor().setBackground(Color.white);
    pnl.addControl(this.Choose, new TableConstraints(1, 1, false, true));
    pnl.addControl(this.Choose2, new TableConstraints(1, 1, false, true));
    pnl.addControl(this.Remark, new TableConstraints(1, 1, true, true));
    FPanel buttonPanel = new FPanel();
    FButton okButton = new FButton("okButton", "同 步");
    okButton.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1)
        {
//          int typeCode = 1;
          try
          {
            if (QrScronyChoose.this.Choose.getSelectedIndex() == QrScronyChoose.this.Choose2.getSelectedIndex())
            {
              if (QrScronyChoose.this.Choose.getSelectedIndex() == 0)
              {
                QrBudgetAction.getMethod().changeHs(Global.loginYear, "1", "0", "rp_xmjl_ys", "rp_xmsb_ys");
                for (int i = 0; i < QrScronyChoose.this.prjCodelist.size(); i++)
                {
                  String whereSql = " and prj_code ='" + QrScronyChoose.this.prjCodelist.get(i).toString() + "'and set_year='" + Global.loginYear + "'";
                  QrBudgetAction.getMethod().insertTempBudget(Global.loginYear, "1", "0", whereSql);
                }

                QrBudgetAction.getMethod().changeHs(Global.loginYear, "1", "1", "rp_xmjl", "rp_xmsb");
                for (int i = 0; i < QrScronyChoose.this.prjCodelist.size(); i++)
                {
                  String whereSql = " and prj_code ='" + QrScronyChoose.this.prjCodelist.get(i).toString() + "'";
                  QrBudgetAction.getMethod().insertTempBudget(Global.loginYear, "1", "1", whereSql);
                }

              }

              if (QrScronyChoose.this.Choose.getSelectedIndex() == 1)
              {
                QrBudgetAction.getMethod().changeHs(Global.loginYear, "2", "0", "rp_xmjl_ys", "rp_xmsb_ys");
                for (int i = 0; i < QrScronyChoose.this.prjCodelist.size(); i++)
                {
                  String whereSql = " and prj_code ='" + QrScronyChoose.this.prjCodelist.get(i).toString() + "'";
                  QrBudgetAction.getMethod().insertTempBudget(Global.loginYear, "2", "0", whereSql);
                }

                QrBudgetAction.getMethod().changeHs(Global.loginYear, "2", "1", "rp_xmjl", "rp_xmsb");
                for (int i = 0; i < QrScronyChoose.this.prjCodelist.size(); i++)
                {
                  String whereSql = " and prj_code ='" + QrScronyChoose.this.prjCodelist.get(i).toString() + "'";
                  QrBudgetAction.getMethod().insertTempBudget(Global.loginYear, "2", "1", whereSql);
                }

              }

             
            } 
            else
            {
            JOptionPane.showMessageDialog(Global.mainFrame, "状态不一致");
            return;
            }

          }
          catch (Exception e1)
          {
            e1.printStackTrace();
          }
        }

        JOptionPane.showMessageDialog(Global.mainFrame, "同步成功");
      }
    });
    FButton cancelButton = new FButton("cencelButton", "取 消");
    cancelButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        QrScronyChoose.this.dispose();
      }
    });
    buttonPanel.addControl(okButton);
    buttonPanel.addControl(cancelButton);
    pnl.addControl(buttonPanel);
    pnl.setLeftInset(40);
    pnl.setTopInset(40);
    pnl.setRightInset(100);
    getContentPane().add(pnl);

    return pnl;
  }
}