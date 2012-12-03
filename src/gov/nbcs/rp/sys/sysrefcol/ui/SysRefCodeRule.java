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
 * 填写编码规则类

 */
public class SysRefCodeRule extends FPanel {

	private static final long serialVersionUID = 1L;

	FTextField ftxtCodeRule;

	IntegerSpinner jspChoiceLen;

	private Pattern codeRuleSplitter = Pattern.compile("\\|"); // 根据"|"拆分字符串

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
		lblChoiceLen.setText("选择编码长度：");
		SpinnerModel modelChoiceLen = new SpinnerNumberModel(0, 0, 100, 1);
		jspChoiceLen = new IntegerSpinner(modelChoiceLen);
		jspChoiceLen.setValue(new Integer(1));
		FButton fbtnAdd = new FButton("fbtnAdd", "增加");
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
		// "<-"按钮
		this.addControl(fbtnDeleteLast,
				new TableConstraints(1, 2, false, false));
		// "<<"按钮
		this
				.addControl(fbtnDeleteAll, new TableConstraints(1, 2, false,
						false));

	}

	/**
	 * 该函数实现的是把spinner的值经过计算加入textfield编辑框中
	 * 
	 * @author Administrator
	 * 
	 */
	private class AddActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String s = jspChoiceLen.getValue().toString();
			// 判断编码填写的是否是数字
			if (!s.matches("\\d+")) {
				return;
			}
			if ("0".equals(s))
				return;

			String sTextField = ftxtCodeRule.getValue().toString(); // 编辑框中的字符串
			StringBuffer string = new StringBuffer();
			int iStrLen = sTextField.length(); // 编辑框中的字符串的长度
			int iInput = Integer.parseInt(s); // 输入字段的值
			if (iStrLen == 0) {
				ftxtCodeRule.setValue(s);
			}
			if (iStrLen == 1) {
				int istr = Integer.parseInt(sTextField); // 编辑字段
				int iValue = istr + iInput; // 计算后的字段值
				string.append(sTextField + "|" + String.valueOf(iValue));
				ftxtCodeRule.setValue(string);
			}
			if (iStrLen > 1) {
				String[] sNewString = codeRuleSplitter.split(sTextField); // 拆分后产生的新的字符串
				int iLast = Integer.parseInt(sNewString[sNewString.length - 1]);
				// 末尾级数
				String sReplace = String.valueOf(iLast + iInput); // 把要添加的级数和末尾级数相加得到新产生的级数
				string.append(sTextField + "|" + sReplace); // 产生新的级数编码
				ftxtCodeRule.setValue(string);
			}
		}
	}

	/**
	 * 该函数实现的是撤消编码规则string的功能
	 * 
	 * @author Administrator
	 * 
	 */
	private class DeleteLastActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			StringBuffer string = new StringBuffer();
			string.append(ftxtCodeRule.getValue()); // 获取字符串
			int iLen = string.length(); // 编辑框中字符得长度
			if (iLen == 0) { // 如果string为空则空操作
			} else {
				if (iLen == 1) { // 如果string只有一个数，则直接删除
					string.deleteCharAt(string.length() - 1);
				} else { // 如果string不为空且不是一个数则执行以下操作
					String sLastTag = String.valueOf(string.charAt(string
							.length() - 2));
					if ("|".equals(sLastTag)) { // string末尾是一个数
						string.delete(string.length() - 2, string.length());
					} else { // string末尾是两个数
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
