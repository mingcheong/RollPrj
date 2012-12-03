package gov.nbcs.rp.input.ui;

/**
*
* <p>Title:</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: �㽭�������޹�˾</p>
* <p>CreateData 2007-10-17--t15:38:46</p>
* @author dean
* @version 1.0
*
*/


import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import mappingfiles.sysdb.RpDwkzs;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDecimalField;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.gl.viewer.FInputPanel;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;


/**
 * ��λ������
 * @author Administrator
 *
 */
public class PrjDwkzsInfoDlg extends JDialog {

   private static final long serialVersionUID = 1L;

   public FInputPanel dwkzsInfoPanel;

   public static final String IS_ADD = "Add";

   public static final String IS_EDIT = "Edit";


   // ��ҳ������� ���ӣ��޸ģ��鿴
   private static String doStates = IS_ADD;
   
   //��
   private FDecimalField   ybys = null;//һ��Ԥ��
   private FDecimalField   jjys = null;//����Ԥ��
   private FDecimalField   qtys = null;//����Ԥ��
   private FDecimalField   sjys = null;//�ϼ�Ԥ��
   private FTextArea bz = null;
   
   PrjDwkzsMainUI prjMain = null;
   CustomTree treeEn = null;
   CustomTable kzsTable = null;
   
   private String dwkzsid = "";
   
   public PrjDwkzsInfoDlg(PrjDwkzsMainUI main,CustomTree treeEn,CustomTable kzsTable,DataSet dsXm) {
       super(Global.mainFrame, true);
       this.setTitle("��λ������");
       prjMain = main;
       this.treeEn = treeEn;
       this.kzsTable = kzsTable;
       initial();
   }

   private void initial() {
   	try
		{
   			FPanel mainPanel = new FPanel();
   			RowPreferedLayout lay = new RowPreferedLayout(1);
   			mainPanel.setLayout(lay);
   			
   			ybys = new FDecimalField("һ��Ԥ��");
   			jjys = new FDecimalField("����Ԥ��");
   			qtys = new FDecimalField("����Ԥ��");
   			sjys = new FDecimalField("�ϼ�Ԥ��");
   			bz = new FTextArea("��ע");
   			
   			if(PrjDwkzsInfoDlg.getDoStates().equals(PrjDwkzsInfoDlg.IS_EDIT)){
   				int count = kzsTable.getTable().getRowCount();
   				for (int i = 0 ;i<count;i++){
   					Object check = kzsTable.getTable().getValueAt(i, 0);
   					if(((Boolean)check).booleanValue()){
   						if(kzsTable.getDataSet().gotoBookmark(kzsTable.rowToBookmark(i))){
   							dwkzsid = kzsTable.getDataSet().fieldByName("dwkzsid").getString();
   							break;
   						}
   					}
   				}
   				
   				List kzsList = PrjInputStub.getMethod().getDwkzsByDwkzsId(dwkzsid);
   				Map m = (Map) kzsList.get(0);
   				
   				this.ybys.setValue(m.get("f2")==null?"":m.get("f2").toString());
   				this.jjys.setValue(m.get("f3")==null?"":m.get("f3").toString());
   				this.qtys.setValue(m.get("f6")==null?"":m.get("f6").toString());
   				this.sjys.setValue(m.get("f7")==null?"":m.get("f7").toString());
   				this.bz.setValue(m.get("bz")==null?"":m.get("bz").toString());
   			}
   			
   			mainPanel.addControl(ybys, new TableConstraints(1, 1, false, true));
   			mainPanel.addControl(jjys, new TableConstraints(1, 1, false, true)); 
   			mainPanel.addControl(qtys, new TableConstraints(1, 1, false, true)); 
   			mainPanel.addControl(sjys, new TableConstraints(1, 1, false, true)); 
   			mainPanel.addControl(bz, new TableConstraints(6, 1, false, true)); 
   			
   			FPanel buttonPanel = new FPanel();
   			FButton okButton = new FButton("okButton", "�� ��");
   			okButton.addMouseListener(new MouseAdapter() {

   	            public void mouseClicked(MouseEvent e) {
   	                if (e.getClickCount() == 1) {   	                   


   	                        // ���
   	                        if (doStates.equals(IS_ADD)) {
   	                        	if((ybys.getValue() == null) || ybys.getValue().equals("") ||
   	                        		(jjys.getValue() == null) || jjys.getValue().equals("") ||
   	                        		(qtys.getValue() == null) || qtys.getValue().equals("") ||
   	                        		(sjys.getValue() == null) || sjys.getValue().equals("")){
   	                        		JOptionPane.showMessageDialog(null, "����д������");
   	                        		return;
   	                        	}
   	                        	
   	                        	try {
   	                        		MyPfNode enID = (MyPfNode)treeEn.getSelectedNode().getUserObject();
   	                        		Map m = new HashMap();
									m = (Map)PrjInputStub.getMethod().getEnCode(enID.getValue()).get(0);
									String enCode = m.get("chr_code").toString();
									
									RpDwkzs rpDwkzs = new RpDwkzs();
									rpDwkzs.setSetYear(Long.valueOf(Global.loginYear));
									rpDwkzs.setDwkzsid(UUIDRandom.generate());
									rpDwkzs.setEnId(enID.getValue());
									rpDwkzs.setF2((Double)ybys.getValue());
									rpDwkzs.setF3((Double)jjys.getValue());
									rpDwkzs.setF6((Double)qtys.getValue());
									rpDwkzs.setF7((Double)sjys.getValue());
									rpDwkzs.setRgCode(Global.getCurrRegion());
									rpDwkzs.setLrrDm(Global.getUserId());
									rpDwkzs.setLrrq(new Date());
									rpDwkzs.setXgrDm(Global.getUserId());
									rpDwkzs.setXgrq(new Date());
									rpDwkzs.setBz((String)bz.getValue());
									rpDwkzs.setEnCode(enCode);
									
									List saveList = new ArrayList();
									saveList.add(rpDwkzs);
									
									InfoPackage info = PrjInputStub.getMethod().saveXmjl(saveList);//���������
									if(info.getSuccess()){
										new MessageBox("����ɹ�!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
		   	                        	PrjDwkzsInfoDlg.this.dispose();
									}
									DataSet dsKzs = PrjInputStub.getMethod().getDwkzsByEnId(Global.loginYear,Global.getCurrRegion(),enID.getValue());
									kzsTable.setDataSet(dsKzs);
									kzsTable.reset();
									
								} catch (Exception e1) {
									new MessageBox("����ʧ��!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
									e1.printStackTrace();
								}
   	                        	
   	                        } else if (doStates.equals(IS_EDIT)) {
   	                        	//edit
   	                        	try {
   	                        		MyPfNode enID = (MyPfNode)treeEn.getSelectedNode().getUserObject();
									DataSet ds = PrjInputStub.getMethod().getDwkzsByDwkzsIdForDs(dwkzsid);
									if(!ds.locate("dwkzsid", dwkzsid)) {
										return;
									}
									ds.edit();
									ds.locate("dwkzsid", dwkzsid);
									ds.fieldByName("f2").setValue(ybys.getValue()==null?"":ybys.getValue().toString());
									ds.fieldByName("f3").setValue(jjys.getValue()==null?"":jjys.getValue().toString());
									ds.fieldByName("f6").setValue(qtys.getValue()==null?"":qtys.getValue().toString());
									ds.fieldByName("f7").setValue(sjys.getValue()==null?"":sjys.getValue().toString());
									ds.fieldByName("bz").setValue(bz.getValue()==null?"":bz.getValue().toString());

									InfoPackage info = PrjInputStub.getMethod().editDwkzs(ds);
									
									if(info.getSuccess()){
										new MessageBox("����ɹ�!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
										PrjDwkzsInfoDlg.this.dispose();
									}
									DataSet dsKzs = PrjInputStub.getMethod().getDwkzsByEnId(Global.loginYear,Global.getCurrRegion(),enID.getValue());
									kzsTable.setDataSet(dsKzs);
									kzsTable.reset();
								} catch (Exception ee) {
									ee.printStackTrace();
									new MessageBox("����ʧ��!", MessageBox.MESSAGE,MessageBox.OK).setVisible(true);
								}
   	                        }
   	                }
   	            }
   	        });
   			FButton cancelButton = new FButton("cencelButton", "ȡ ��");
   			cancelButton.addMouseListener(new MouseAdapter() {
   	            public void mouseClicked(MouseEvent e) {
   	                PrjDwkzsInfoDlg.this.dispose();
   	            }
   	        });
   			buttonPanel.addControl(okButton);
   			buttonPanel.addControl(cancelButton);
   			mainPanel.addControl(buttonPanel);
   			mainPanel.setLeftInset(40);
   			mainPanel.setTopInset(40);
   			mainPanel.setRightInset(100);
	        this.getContentPane().add(mainPanel);
		}catch(Exception e)
		{
			
		}

   }

   public static String getDoStates() {
       return doStates;
   }

   public static void setDoStates(String doStates) {
       PrjDwkzsInfoDlg.doStates = doStates;
   }

}
