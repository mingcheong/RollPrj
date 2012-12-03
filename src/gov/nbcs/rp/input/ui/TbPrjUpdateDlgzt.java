package gov.nbcs.rp.input.ui;

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

import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.gl.viewer.FInputPanel;
import com.foundercy.pf.util.Global;


public class TbPrjUpdateDlgzt extends JDialog {

   private static final long serialVersionUID = 1L;

   public FInputPanel tbInfoPanel;

  
   
   TbProjectUI prjMain = null;
   String xmxhs = "";
   FComboBox xmzt = null;
   CustomTable cTable = null;
   
   public TbPrjUpdateDlgzt(String xmxhs) {
       super(Global.mainFrame, true);
       this.setTitle("批量修改项目状态");
       this.xmxhs = xmxhs;
      
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
   			xmxx.addControl(xmzt, new TableConstraints(1, 4, false, true));
   			mainPanel.addControl(xmxx, new TableConstraints(1, 4, false, true));
   			
   			
   			
   			
   			FPanel buttonPanel = new FPanel();
   			FButton okButton = new FButton("okButton", "确定 ");
   			okButton.addMouseListener(new MouseAdapter() {

   	            public void mouseClicked(MouseEvent e) {
   	                if (e.getClickCount() == 1) {
   	                	
   	                	try {
   	                		
		   	             				List listSql = new ArrayList();
		   	             				listSql.add("update rp_xmjl set c6='"+xmzt.getValue()+"' where xmxh in ("+xmxhs+")");
		   	             				PrjInputStub.getMethod().postData(listSql);
		   	             		
		   	             
		   	            
		   	                TbPrjUpdateDlgzt.this.dispose();
		   	         	JOptionPane.showMessageDialog(null, "修改成功");
                       	} catch (Exception e1) {
							new MessageBox("修改失败!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
							e1.printStackTrace();
						}
   	                }
   	            }
   	        });
   			FButton cancelButton = new FButton("cencelButton", "取 消");
   			cancelButton.addMouseListener(new MouseAdapter() {
   	            public void mouseClicked(MouseEvent e) {
   	                TbPrjUpdateDlgzt.this.dispose();
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
