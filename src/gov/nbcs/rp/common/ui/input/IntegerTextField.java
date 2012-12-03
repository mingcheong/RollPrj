package gov.nbcs.rp.common.ui.input;

import com.foundercy.pf.control.FTextField;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * 只可以输入数字
 * 
 * @author qzc
 * 
 */
public class IntegerTextField extends FTextField implements KeyListener {
	public IntegerTextField() {
		super();
		super.getEditor().addKeyListener(this);
	}

	public IntegerTextField(boolean arg0) {
		super(arg0);
		super.getEditor().addKeyListener(this);
	}

	public IntegerTextField(String arg0, int arg1) {
		super(arg0, arg1);
		super.getEditor().addKeyListener(this);
	}

	public IntegerTextField(String arg0) {
		super(arg0);
		super.getEditor().addKeyListener(this);
	}

	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (((c < '0') || (c > '9')) && (c != '\b')) {
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
