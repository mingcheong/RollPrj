package gov.nbcs.rp.input.ui;



import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.gl.viewer.FInputPanel;
import com.foundercy.pf.util.Global;


public class TbPrjUpdateDlg extends JDialog {

   private static final long serialVersionUID = 1L;

   public FInputPanel tbInfoPanel;

  
   
   TbProjectUI prjMain = null;
   String enID = "";
   FComboBox xmzt = null;
   CustomTable cTable = null;
   
   public TbPrjUpdateDlg(String enID) {
       super(Global.mainFrame, true);
       this.setTitle("批量修改项目状态");
       this.enID = enID;
      
       initial();
   }

   private void initial() {
   	try
		{
   			FPanel mainPanel = new FPanel();
   			RowPreferedLayout lay = new RowPreferedLayout(1);
   			mainPanel.setLayout(lay);
   			FPanel xmxx = new FPanel();
   			RowPreferedLayout xmLay = new RowPreferedLayout(4);
   			xmxx.setLayout(xmLay);
   		

   			
   			List xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear,Global.getCurrRegion());
   			String xmztTmp = "";
   			for (int i = 0; i < xmztList.size(); i++) {
   				Map m = new HashMap();
   				m = (Map) xmztList.get(i);
   				xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
   			}
   			if (xmztTmp.length() > 0) {
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}
   			xmzt = new FComboBox("项目状态");
   			xmzt.setRefModel(xmztTmp);
   			xmzt.setValue("");
   			xmxx.addControl(xmzt, new TableConstraints(1, 1, false, true));
   			
   			DataSet ds = PrjInputStub.getMethod().getXmSortListByEnID(Global.loginYear, Global.getCurrRegion(), enID, "cs");
   			
   			String[] columnText = {"预算单位","项目编码","项目名称"};
   			String[] columnField =  {"div_name","xmbm","xmmc"};
   			
   			cTable = new CustomTable(columnText,columnField,ds,true,null);
   			
   			cTable.reset();
   			FScrollPane spanel = new FScrollPane(cTable);
   			
   			
   			mainPanel.addControl(xmxx, new TableConstraints(1, 1, false, true));
   			mainPanel.addControl(spanel, new TableConstraints(14, 1, false, true));
   			
   			
   			FPanel buttonPanel = new FPanel();
   			FButton okButton = new FButton("okButton", "保 存");
   			okButton.addMouseListener(new MouseAdapter() {

   	            public void mouseClicked(MouseEvent e) {
   	                if (e.getClickCount() == 1) {
   	                	
   	                	try {
   	                		int count = cTable.getTable().getRowCount();
	   	             		int num = 0;
	   	             		for (int i = 0 ;i<count;i++){
	   	             			Object check = cTable.getTable().getValueAt(i, 0);
	   	             			if(((Boolean)check).booleanValue()){
	   	             				num++;
	   	             			}
	   	             		}
	   	             		if(num ==0) {
	   	             			 JOptionPane.showMessageDialog(null, "请选择项目");
	   	             	         return;
	   	             		}
		   	             	for (int i = 0 ;i<count;i++){
		   	             		Object check = cTable.getTable().getValueAt(i, 0);
		   	             		if(((Boolean)check).booleanValue()){
		   	             			if(cTable.getDataSet().gotoBookmark(cTable.rowToBookmark(i))){
		   	             				String xmxh = cTable.getDataSet().fieldByName("xmxh").getString();
		   	             				List listSql = new ArrayList();
		   	             				listSql.add("update rp_xmjl set c6='"+xmzt.getValue()+"' where xmxh = '"+xmxh+"'");
		   	             				PrjInputStub.getMethod().postData(listSql);
		   	             			}
		   	             		}
		   	             	}
		   	             	new MessageBox("批量修改成功!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
		   	                TbPrjUpdateDlg.this.dispose();
                       	} catch (Exception e1) {
							new MessageBox("保存失败!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
							e1.printStackTrace();
						}
   	                }
   	            }
   	        });
   			FButton cancelButton = new FButton("cencelButton", "取 消");
   			cancelButton.addMouseListener(new MouseAdapter() {
   	            public void mouseClicked(MouseEvent e) {
   	                TbPrjUpdateDlg.this.dispose();
   	            }
   	        });
   			buttonPanel.addControl(okButton);
   			buttonPanel.addControl(cancelButton);
   			mainPanel.addControl(buttonPanel);
   			mainPanel.setLeftInset(10);
   			mainPanel.setTopInset(10);
   			mainPanel.setRightInset(10);
	        this.getContentPane().add(mainPanel);
		}catch(Exception e)
		{
			
		}

   }

 
}
