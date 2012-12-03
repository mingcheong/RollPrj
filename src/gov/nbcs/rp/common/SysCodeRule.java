/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import gov.nbcs.rp.common.datactrl.DataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SysCodeRule implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * ��������ƥ��ģʽ���Ƿ���һ���ɲ�ֵģ����������ֱ��ƽ��������
	 */
	private static final Pattern splittedCodePattern = Pattern
			.compile(".+\\|.+");

	/**
	 * �������Ĳ��ģʽ
	 */
	private static final Pattern codeSplitter = Pattern.compile("\\s*\\|\\s*");

	/**
	 * ��ѯ��ȡ��������SQL���
	 */
	private static final String codeRuleSQL = "SELECT * FROM FB_S_CODERULE WHERE SET_YEAR=? AND CODE_TYPE=?";

	private String name;

	private String codeRuleStr;

	private static final int DEFAULT_CODE_RULE = 1;

	/**
	 * ����ÿһ�����볤�ȵ�����
	 */
	private List codeLength = new ArrayList();

	/**
	 * ������������������
	 * 
	 * @param budgetYear
	 * @param codeType
	 *            �����������
	 * @throws Exception
	 */
	public SysCodeRule(int budgetYear, String codeType) throws Exception {
		if ((budgetYear > 0) && (codeType != null)) {
			DataSet ds = DataSet.create();
			ds.setSqlClause(codeRuleSQL);
			ds
					.setQueryParams(new Object[] { new Integer(budgetYear),
							codeType });
			ds.open();
			if (ds.next()) {
				name = ds.fieldByName("code_name").getString();
				codeRuleStr = ds.fieldByName("code_rule").getString();
				if (splittedCodePattern.matcher(codeRuleStr).matches()) {
					String[] ruleArray = codeSplitter.split(codeRuleStr);
					createRuleArray(ruleArray);
				} else {
					createRuleArray(codeRuleStr);
				}
			}
		}
	}

	/**
	 * ������������������
	 * 
	 * @param codeRule
	 *            �����������
	 * @throws Exception
	 */
	public static SysCodeRule createClient(int[] codeRule) {
		try {
			SysCodeRule codeRuleObj = new SysCodeRule(-1, null);
			String codeRuleStr = "";
			for (int i = 0; i < codeRule.length; i++) {
				codeRuleObj.codeLength.add(new Integer(codeRule[i]));
				codeRuleStr += String.valueOf(codeRule[i]) + '|';
			}
			codeRuleObj.codeRuleStr = codeRuleStr.substring(0, codeRuleStr
					.length() - 1);
			return codeRuleObj;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * ������������������
	 * 
	 * @param codeRuleStr
	 *            ��������ַ���
	 * @throws Exception
	 */
	public static SysCodeRule createClient(String codeRuleStr) {
		try {
			SysCodeRule codeRuleObj = new SysCodeRule(-1, null);
			if (splittedCodePattern.matcher(codeRuleStr).matches()) {
				String[] ruleArray = codeSplitter.split(codeRuleStr);
				codeRuleObj.createRuleArray(ruleArray);
			} else {
				codeRuleObj.createRuleArray(new String[] { codeRuleStr });
			}
			codeRuleObj.codeRuleStr = codeRuleStr;
			return codeRuleObj;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * ���ݵ�ǰ����������ı���ֵ
	 * 
	 * @param code
	 * @return
	 */
	public String rootCode(String code) {
		int len = ((Integer) this.codeLength.get(0)).intValue();
		return code.substring(0, len);
	}

	/**
	 * ��õ�ǰ����ļ���
	 * 
	 * @param code
	 * @return ����ֵ
	 */
	public int levelOf(String code) {
		return this.codeLength.indexOf(new Integer(code.length()));
	}

	/**
	 * ��ȡ����������¼�����ĳ���
	 * 
	 * @param code
	 *            ��������
	 * @return
	 */
	public int nextLevelLength(String code) {
		int level = 0;
		if (!Common.isNullStr(code)) {
			level = this.levelOf(code);
			if ((level < 0) || (level == this.codeLength.size() - 1)) {
				level = -1;
			} else {
				level++;
			}
		} else {
			level = 0;
		}
		return level >= 0 ? ((Integer) codeLength.get(level)).intValue() : -1;
	}

	/**
	 * �ɱ��������ȡ�ϼ�����
	 * 
	 * @param code
	 *            ��������
	 * @return
	 */
	public String previous(String code) {
		int idx = levelOf(code);
		if (idx > 0) {
			int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
			return code.substring(0, previousLength);
		}
		return null;
	}

	/**
	 * ��ȡȥ���ϼ������ı���С���룬����001002���ϼ�������001�� �����������001002�ķ���ֵ����002
	 * 
	 * @param code
	 * @return
	 */
	public String concurrent(String code) {
		int idx = levelOf(code);
		if (idx > 0) {
			int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
			return code.substring(previousLength, code.length());
		}
		return code;
	}

	/**
	 * ��ȡͬ������һ������
	 * 
	 * @return
	 */
	public String nextCodeSameLvl(String code) {
		String pCode = "";
		String cCode = "0";
		int idx = levelOf(code);
		int cLength;
		if (idx > 0) {
			int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
			pCode = code.substring(0, previousLength);
			cCode = code.substring(previousLength, code.length());
			cLength = ((Integer) codeLength.get(idx)).intValue()
					- ((Integer) codeLength.get(idx - 1)).intValue();
		} else {
			cLength = ((Integer) codeLength.get(0)).intValue();
		}
		cCode = "" + (Integer.parseInt(cCode) + 1);
		while (cCode.length() < cLength) {
			cCode = "0" + cCode;
		}
		return pCode + cCode;
	}

	/**
	 * ��ȡ�¼��ĵ�һ������
	 * 
	 * @return
	 */
	public String nextCodeNextLvl(String code) {
		String cCode = "1";
		int idx = levelOf(code);
		int cLength = ((Integer) codeLength.get(idx + 1)).intValue()
				- ((Integer) codeLength.get(idx)).intValue();
		while (cCode.length() < cLength) {
			cCode = "0" + cCode;
		}
		return code + cCode;
	}

	/**
	 * ����ϵͳ��������ַ�������
	 * 
	 * @return
	 */
	public String originRuleStr() {
		return this.codeRuleStr;
	}
	
	/**
	 * ���طָ���ַ�����
	 */
	public String[] getOriginRule(){
		return codeSplitter.split(codeRuleStr);
	}

	/**
	 * ����ԭʼ�ı�������List<Integer>
	 * 
	 * @return
	 */
	public List originRules() {
		return this.codeLength;
	}

	protected void createRuleArray(String[] ruleArray) {
		for (int i = 0; i < ruleArray.length; i++) {
			if (Common.isNumber(ruleArray[i])) {
				codeLength.add(new Integer(ruleArray[i]));
			}
		}
	}

	protected void createRuleArray(String ruleStr) {
		for (int i = 0, delta = Integer.parseInt(ruleStr); i < DEFAULT_CODE_RULE; i++, delta += Integer
				.parseInt(ruleStr)) {
			codeLength.add(new Integer(delta));
		}
	}

	public String name() {
		return name;
	}
}
