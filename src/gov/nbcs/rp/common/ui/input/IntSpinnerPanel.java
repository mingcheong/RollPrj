/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.input;

import com.foundercy.pf.control.FPanel;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

public class IntSpinnerPanel extends FPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The isp frozen column. */
	private IntegerSpinner ispFrozenColumn;

	/**
	 * Instantiates a new int spinner panel.
	 */
	public IntSpinnerPanel() {
		this("", "");
	}

	/**
	 * Instantiates a new int spinner panel.
	 */
	public IntSpinnerPanel(String beforeTitle, String afterTitle) {
		ispFrozenColumn = new IntegerSpinner();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		this.setLayout(flowLayout);
		JLabel lblSpinB = new JLabel();
		lblSpinB.setText(beforeTitle);
		JLabel lblSpinE = new JLabel();
		lblSpinE.setText(afterTitle);
		this.add(lblSpinB);
		this.add(ispFrozenColumn);
		this.add(lblSpinE);
	}

	/**
	 * @param listener
	 * @see javax.swing.JSpinner#addChangeListener(javax.swing.event.ChangeListener)
	 */
	public void addChangeListener(ChangeListener listener) {
		ispFrozenColumn.addChangeListener(listener);
	}

	/**
	 * @param model
	 * @see javax.swing.JSpinner#setModel(javax.swing.SpinnerModel)
	 */
	public void setModel(SpinnerModel model) {
		ispFrozenColumn.setModel(model);
	}

	public Object getValue() {
		return ispFrozenColumn.getValue();
	}

}
