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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class InetAddressField extends JTextField {
    public static Pattern iAddrSplitter = Pattern.compile("\\.");

    public static final String IPV2_STR = "   .   .   .   ";

    private StringBuffer content = new StringBuffer(IPV2_STR);

    public InetAddressField() {
        super();
        super.setText(content.toString());

        super.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                int caretPos = getCaretPosition();
                if ((c >= '0') && (c <= '9')
                        && ((caretPos != 3) && (caretPos != 7) && (caretPos != 11))) {
                    if (setString(c, caretPos)) {
                        setCaretPosition(caretPos + 1);
                    }
                }
                if (c == '.') {
                    setCaretPosition(getCaretPosBySegment(getIAddrCaretSegment(caretPos)));
                }
                e.setKeyChar('\0');
            }

            public void keyPressed(KeyEvent e) {
                int caretPos = InetAddressField.this.getCaretPosition();
                switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE:
                    delString(caretPos - 1);
                    InetAddressField.this.setCaretPosition(caretPos - 1);
                    break;
                case KeyEvent.VK_DELETE:
                    delString(caretPos);
                    InetAddressField.this.setCaretPosition(caretPos+1);
                    break;
                case KeyEvent.VK_LEFT:
                    InetAddressField.this.setCaretPosition(caretPos - 1);
                    break;
                case KeyEvent.VK_RIGHT:
                    InetAddressField.this.setCaretPosition(caretPos + 1);
                    break;
                case KeyEvent.VK_ENTER:
                    System.out.println(getText());
                    break;
                }
                e.setKeyCode(0);
            }
        });
    }

    public void setCaretPosition(int pos) {
        if ((pos >= 0) && (pos < content.length())) {
            super.setCaretPosition(pos);
        }
    }

    protected void delString(int pos) {
        if ((pos >= 0) && (pos < content.length())) {
            if (this.content.charAt(pos) != '.') {
                this.content.setCharAt(pos, ' ');
                this.setText(content.toString());
            }
        }
    }

    public String getText() {
        String s = "";
        String arr[] = iAddrSplitter.split(content);
        for(int i=0;i<arr.length;i++) {
            s += Integer.parseInt(arr[i].trim())+".";
        }
        s = s.substring(0,s.length()-1);
        return s;
    }

    protected boolean setString(char c, int caretPos) {
        if ((caretPos >= 0) && (caretPos < content.length())) {
            int seg = getIAddrCaretSegment(caretPos);
            char backup_c = content.charAt(caretPos);
            content.setCharAt(caretPos, c);
            String arr[] = iAddrSplitter.split(content);
            int value = Integer.parseInt(arr[seg].trim());
            if (value <= 255) {
                setText(content.toString());
                return true;
            } else {
                content.setCharAt(caretPos, backup_c);
                return false;
            }
        }
        return false;
    }

    protected int getCaretPosBySegment(int seg) {
        switch (seg) {
        case 0:
            return 4;
        case 1:
            return 8;
        case 2:
            return 12;
        case 3:
            return getCaretPosition();
        }
        return 0;
    }

    protected int getIAddrCaretSegment(int caretPos) {
        if ((caretPos >= 0) && (caretPos < 4)) {
            return 0;
        } else if ((caretPos >= 4) && (caretPos < 8)) {
            return 1;
        } else if ((caretPos >= 8) && (caretPos < 12)) {
            return 2;
        } else if (caretPos >= 12) {
            return 3;
        }
        return -1;
    }

    public static void main(String args[]) {
        JFrame frm = new JFrame();
        frm.setSize(400, 300);
        frm.getContentPane().add(new InetAddressField());
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
