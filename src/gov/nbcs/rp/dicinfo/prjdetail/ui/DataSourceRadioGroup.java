/**
 * Copyright 浙江易桥 版权所有
 * 
 * 部门预算子系统
 * 
 * @title radioGroup的扩展类，（包括了它的监听事件，根据数据来源来控制计算，选取界面是否可控）
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.dicinfo.prjdetail.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JRadioButton;

import com.foundercy.pf.control.Compound;
import com.foundercy.pf.control.FIntegerField;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;

public class DataSourceRadioGroup extends FRadioGroup {
//	private String stitle; // 名称
//	private String srefModel; // 子项内容
//	private boolean panelEnable;
	private FPanel pnCal;
	private FPanel pnSel;	
	private boolean isFormula;   //是否选择了为计算类型
	private FIntegerField tf ;
	private FTextField tfSel ;
	private FTextField tfFormula ;

	
	protected String getSelectedValue() {
		return this.getValue().toString();
	}
	
	protected void setCalPanelEnabled(boolean panelEnable){
		changeChildControlsEditMode(pnCal,panelEnable);
	}
	protected void setSelPanelEnabled(boolean panelEnable){
		changeChildControlsEditMode(pnSel,panelEnable);
	}
	protected void setIsFormula( boolean aIsFormula){
		isFormula = aIsFormula;
	}
	protected boolean getIsFormula(){
		return isFormula;
	}
	protected void setPValue( String value ){
		tf.setValue( value );
	}
	protected void setFormula( String value ){
		tfFormula.setValue( "" );
	}
	protected void setSel( String value ){
		tfSel.setValue( "" );
	}
	
	public DataSourceRadioGroup(String stitle, String srefModel,FPanel pncal,FPanel pnsel, FIntegerField tf, FTextField tfFormula, FTextField tfSel ) {
		super();
		
//		this.stitle = stitle;
//		this.srefModel = srefModel;
		this.setTitle(stitle);
		this.setRadioLayout(1);
		this.setRefModel(srefModel);
        this.pnCal = pncal;   //计算公式界面
        this.pnSel = pnsel;   //选取值界面
        this.tf = tf;
        this.tfFormula = tfFormula ;
        this.tfSel = tfSel ;

		JRadioButton radios[] = this.getRadios();
        
		for (int i = 0; i < radios.length; i++) {
			radios[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					//System.out.println(getSelectedValue());
					if (e.getClickCount() == 1) {
						if ("1".equals(getSelectedValue())){
							setCalPanelEnabled(false);
							setSelPanelEnabled(false);
							setPValue("0");
							setFormula("");
							setSel("");
						}							
						if ("2".equals(getSelectedValue())){
							setIsFormula(true);
							setCalPanelEnabled(true);
						    setSelPanelEnabled(false);
						    setPValue("1");
						    setSel("");
						}
						if ("3".equals(getSelectedValue())){
							setCalPanelEnabled(false);
						    setSelPanelEnabled(true);
						    setPValue("0");
						    setFormula("");
						}
					}
				}
			});
		}
	}
	
	private static void changeChildControlsEditMode(Compound aParentControl,
			boolean bBoolean) {
		// 获得控件
		List listControls = aParentControl.getSubControls();
		for (int i = 0; i < listControls.size(); i++) {
			// FTextField,用setEditable
			if (listControls.get(i) instanceof FTextField) {
				((FTextField) listControls.get(i)).setEditable(bBoolean);
				//((FTextField) listControls.get(i)).setTitle("");
				// FTextArea,用setEditable
			} else if (listControls.get(i) instanceof FTextArea) {
				((FTextArea) listControls.get(i)).setEditable(bBoolean);
				// FRadioGroup,用setEditable
			} else if (listControls.get(i) instanceof FRadioGroup) {
				((FRadioGroup) listControls.get(i)).setEditable(bBoolean);
				//递归，设子控件
			} else if (listControls.get(i) instanceof Compound) {
				changeChildControlsEditMode((Compound) listControls.get(i),
						bBoolean);
				//其他控件,用setEnabled
			} else
				((Component) listControls.get(i)).setEnabled(bBoolean);
		}
	}

}

