/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @title 
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell.inputui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;

public class TextUI extends AbstractInputDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3124965834264809L;

	/** The text area. */
	FTextArea textArea;
	FLabel fieldCanHold;
	FLabel fieldMayHold;

	/** The modified. */
	boolean modified = false;

	private FButton okButton;

	private FButton cancelButton;

	public TextUI() {
		super(Global.mainFrame);
		this.setTitle("请录入");
		this.setSize(500, 400);
		this.setResizable(false);
		this.setModal(true);

		// 主窗体上放入panel，作为主布局panel
		FPanel mainPanel = new FPanel();
		RowPreferedLayout mainLayout = new RowPreferedLayout(1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLeftInset(10);
		mainPanel.setRightInset(10);
		mainPanel.setTopInset(0);

		FScrollPane textPanel = new FScrollPane();
		textArea = new FTextArea(false);
		textPanel.addControl(textArea);
		FLabel fgts = new FLabel();
		fieldMayHold = new FLabel();
		FLabel fbts = new FLabel();
		fieldCanHold = new FLabel();

		textArea.addValueChangeListener(new ValueChangeListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.foundercy.pf.control.ValueChangeListener#valueChanged(com
			 * .foundercy.pf.control.ValueChangeEvent)
			 */
			public void valueChanged(ValueChangeEvent arg0) {
				modified = true;
				// 汉字需要单独处理
				String str = textArea.getValue().toString().trim();
				// 只有有限制的字段需要处理
				if (!"无限制".equals(fieldMayHold.getText())) {
					byte[] b = str.getBytes();
					fieldMayHold.setText(Integer.toString(Integer
							.parseInt(fieldCanHold.getText())
							- b.length));
				}

			}

		});

		// "确定"、"取消"按钮
		FFlowLayoutPanel buttonPanel = new FFlowLayoutPanel();
		buttonPanel.setAlignment(FlowLayout.CENTER);
		okButton = new FButton();
		okButton.setText("确定");
		// 监听事件
		okButton.addActionListener(new OkButtonListener());
		cancelButton = new FButton();
		cancelButton.setText("取消");
		// 监听事件
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modified = false;
				dispose();
			}
		});
		buttonPanel.addControl(okButton);
		buttonPanel.addControl(cancelButton);

		// 把元素都加入到mainPanel中
		fgts.setText("最大可录入字符数:");
		fieldCanHold.setFont(new Font("隶书", Font.PLAIN, 20));
		fieldCanHold.setForeground(new Color(255, 0, 0));
		fbts.setText("当前可录入字符数:");
		fieldMayHold.setFont(new Font("隶书", Font.PLAIN, 20));
		fieldMayHold.setForeground(new Color(0, 255, 0));

		JPanel gdPanel = new JPanel();
		FlowLayout gdLayout = new FlowLayout();
		gdLayout.setHgap(50);
		gdLayout.setAlignment(FlowLayout.LEFT);
		gdPanel.setLayout(gdLayout);

		JPanel gdPanel1 = new JPanel();
		// FlowLayout gdLayout1 = new FlowLayout();
		gdLayout.setHgap(1);
		gdLayout.setAlignment(FlowLayout.LEFT);
		gdPanel.setLayout(gdLayout);

		JPanel gdPanel2 = new JPanel();
		// FlowLayout gdLayout2 = new FlowLayout();
		gdLayout.setHgap(1);
		gdLayout.setAlignment(FlowLayout.LEFT);
		gdPanel.setLayout(gdLayout);

		gdPanel1.add(fgts);
		gdPanel1.add(fieldCanHold);
		gdPanel2.add(fbts);
		gdPanel2.add(fieldMayHold);
		gdPanel.add(gdPanel1);
		gdPanel.add(gdPanel2);

		mainPanel.add(gdPanel, new TableConstraints(1, 1, true, true));
		mainPanel.add(textPanel, new TableConstraints(8, 1, true, true));
		mainPanel.addControl(buttonPanel, new TableConstraints(2, 1, false,
				true));
		this.getContentPane().add(mainPanel);
	}

	// 确定按钮的监听事件
	class OkButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			modified = true;
			// 超限处理，超出允许的最大长度，超出部分做截断处理
			if (Integer.parseInt(fieldMayHold.getText()) < 0) {
				JOptionPane.showMessageDialog(Global.mainFrame,
						"当前输入内容的长度超过系统允许的最大限度，请调整文字内容！", "警告！",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				dispatchEvent(new WindowEvent(TextUI.this,
						WindowEvent.WINDOW_CLOSING));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Sets the text value.
	 * 
	 * @param value
	 *            the new text value
	 */
	public void setTextValue(String value) {
		textArea.setValue(value);
	}

	/**
	 * Gets the text value.
	 * 
	 * @return the text value
	 */
	public String getTextValue() {
		if (textArea.getValue() != null) {
			return textArea.getValue().toString();
		} else {
			return "";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nbcs.rp.common.ui.report.cell.inputui.AbstractInputDialog
	 * #getValue()
	 */
	public Object getValue() throws Exception {
		if (isModified()) {
			return getTextValue();
		} else {
			return null;
		}
	}

	public void setfieldCanHold(String value) {
		this.fieldCanHold.setText(value);
	}

	public String getfieldCanHold() {
		return this.fieldCanHold.getText();
	}

	public void setfieldMayHold(String value) {
		this.fieldMayHold.setText(value);
	}

	public String getfieldMayHold() {
		return this.fieldMayHold.getText();
	}

	public void setViewMode(boolean isView) {
		okButton.setVisible(false);
		cancelButton.setTitle("关闭");
		textArea.setEditable(!isView);
	}

}
