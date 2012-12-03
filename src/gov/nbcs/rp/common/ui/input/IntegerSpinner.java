package gov.nbcs.rp.common.ui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * 只可以输入数字
 * 
 * @author qzc
 * 
 */
public class IntegerSpinner extends JSpinner implements KeyListener {

	public IntegerSpinner() {
		super();
		((JSpinner.DefaultEditor) super.getEditor()).getTextField()
				.addKeyListener(this);

	}

	public IntegerSpinner(SpinnerModel model) {
		super(model);
		((JSpinner.DefaultEditor) super.getEditor()).getTextField()
				.addKeyListener(this);
	}

	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (((c < '0') || (c > '9')) && (c != '\b') && (c != '-') && (c != '+')) {
			e.setKeyChar('\0');
		}
	}

	public void keyPressed(KeyEvent e) {
		this.keyTyped(e);
	}

	public void keyReleased(KeyEvent e) {
		this.keyTyped(e);
	}

}
