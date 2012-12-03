package gov.nbcs.rp.sys.sysrefcol.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import gov.nbcs.rp.common.ui.input.IntegerSpinner;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * ��д���������

 */
public class SysRefCodeRule extends FPanel {

	private static final long serialVersionUID = 1L;

	FTextField ftxtCodeRule;

	IntegerSpinner jspChoiceLen;

	private Pattern codeRuleSplitter = Pattern.compile("\\|"); // ����"|"����ַ���

	public SysRefCodeRule() {
		super();
		RowPreferedLayout rLay = new RowPreferedLayout(11);
		rLay.setColumnGap(1);
		rLay.setColumnWidth(29);
		this.setLayout(rLay);
		ftxtCodeRule = new FTextField();
		ftxtCodeRule.setTitleVisible(false);
		ftxtCodeRule.setEditable(false);
		FLabel lblChoiceLen = new FLabel();
		lblChoiceLen.setText("ѡ����볤�ȣ�");
		SpinnerModel modelChoiceLen = new SpinnerNumberModel(0, 0, 100, 1);
		jspChoiceLen = new IntegerSpinner(modelChoiceLen);
		jspChoiceLen.setValue(new Integer(1));
		FButton fbtnAdd = new FButton("fbtnAdd", "����");
		fbtnAdd.addActionListener(new AddActionListener());
		FButton fbtnDeleteLast = new FButton("fbtnDeleteLast", "<-");
		fbtnDeleteLast.addActionListener(new DeleteLastActionListener());
		FButton fbtnDeleteAll = new FButton("fbtnDeleteLast", "<<");
		fbtnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ftxtCodeRule.setValue("");
			}
		});

		this.addControl(ftxtCodeRule, new TableConstraints(1, 11, false, true));
		this.addControl(lblChoiceLen, new TableConstraints(1, 3, false, false));
		this.add(jspChoiceLen, new TableConstraints(1, 2, false, false));
		this.addControl(fbtnAdd, new TableConstraints(1, 2, false, false));
		// "<-"��ť
		this.addControl(fbtnDeleteLast,
				new TableConstraints(1, 2, false, false));
		// "<<"��ť
		this
				.addControl(fbtnDeleteAll, new TableConstraints(1, 2, false,
						false));

	}

	/**
	 * �ú���ʵ�ֵ��ǰ�spinner��ֵ�����������textfield�༭����
	 * 
	 * @author Administrator
	 * 
	 */
	private class AddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String s = jspChoiceLen.getValue().toString();
			// �жϱ�����д���Ƿ�������
			if (!s.matches("\\d+")) {
				return;
			}
			if ("0".equals(s))
				return;

			String sTextField = ftxtCodeRule.getValue().toString(); // �༭���е��ַ���
			StringBuffer string = new StringBuffer();
			int iStrLen = sTextField.length(); // �༭���е��ַ����ĳ���
			int iInput = Integer.parseInt(s); // �����ֶε�ֵ
			if (iStrLen == 0) {
				ftxtCodeRule.setValue(s);
			}
			if (iStrLen == 1) {
				int istr = Integer.parseInt(sTextField); // �༭�ֶ�
				int iValue = istr + iInput; // �������ֶ�ֵ
				string.append(sTextField + "|" + String.valueOf(iValue));
				ftxtCodeRule.setValue(string);
			}
			if (iStrLen > 1) {
				String[] sNewString = codeRuleSplitter.split(sTextField); // ��ֺ�������µ��ַ���
				int iLast = Integer.parseInt(sNewString[sNewString.length - 1]);
				// ĩβ����
				String sReplace = String.valueOf(iLast + iInput); // ��Ҫ��ӵļ�����ĩβ������ӵõ��²����ļ���
				string.append(sTextField + "|" + sReplace); // �����µļ�������
				ftxtCodeRule.setValue(string);
			}
		}
	}

	/**
	 * �ú���ʵ�ֵ��ǳ����������string�Ĺ���
	 * 
	 * @author Administrator
	 * 
	 */
	private class DeleteLastActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			StringBuffer string = new StringBuffer();
			string.append(ftxtCodeRule.getValue()); // ��ȡ�ַ���
			int iLen = string.length(); // �༭�����ַ��ó���
			if (iLen == 0) { // ���stringΪ����ղ���
			} else {
				if (iLen == 1) { // ���stringֻ��һ��������ֱ��ɾ��
					string.deleteCharAt(string.length() - 1);
				} else { // ���string��Ϊ���Ҳ���һ������ִ�����²���
					String sLastTag = String.valueOf(string.charAt(string
							.length() - 2));
					if ("|".equals(sLastTag)) { // stringĩβ��һ����
						string.delete(string.length() - 2, string.length());
					} else { // stringĩβ��������
						string.delete(string.length() - 3, string.length());
					}
				}
			}
			ftxtCodeRule.setValue(string);
		}
	}

	public FTextField getFtxtCodeRule() {
		return ftxtCodeRule;
	}

	public IntegerSpinner getJspChoiceLen() {
		return jspChoiceLen;
	}
}
