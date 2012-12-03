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

import com.foundercy.pf.control.Compound;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.util.XMLData;
import gov.nbcs.rp.common.datactrl.Record;
import gov.nbcs.rp.common.janino.JavaEvaluator;
import gov.nbcs.rp.common.js.JSEvaluator;
import gov.nbcs.rp.common.ui.tree.CustomTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fr.report.cellElement.Formula;

/**
 * <p>
 * Title: Common
 * </p>
 * <p>
 * Description: ���õĹ����࣬����һЩͨ�õķ���
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: �㽭����
 * </p>
 * 
 * @author qzc
 * @version 1.0
 */
/**
 * The class Common.  
 *
 * @author qj
 * @version 1.0, Jun 3, 2009
 * @since fb6.3.70
 */
public class Common {

	/**
	 * ����Ӧ�ó���Ļ�����������
	 */
	public static ResourceBundle COMMON_CONFIG;

	/**
	 * ���ڵ��ı���ʽ����
	 */
	private static java.text.DateFormat df;

	/**
	 * Java�������ͺ�SQL.Types��ӳ��
	 */
	private static Map SQL_TYPE_MAPPING = new HashMap();

	private static Map SQL_TYPE_MAPPING_STRING = new HashMap();

	/**
	 * �ַ���ͷ�����ֽ�β�ĸ����ַ���������FFF189��
	 */
	private static Pattern NUM_STR_COMPLEX = Pattern.compile("\\D*(\\d+)\\D*",
			Pattern.CASE_INSENSITIVE);

	private static Pattern STR_NUM_COMPLEX = Pattern.compile("\\d*(\\D+)\\d*",
			Pattern.CASE_INSENSITIVE);

	/**
	 * debug����
	 */
	public static final boolean RUNTIME_DEBUG = false;

	private static Pattern NUMBER_PATTERN = Pattern
	// .compile("[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}");
			// .compile("[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}|[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}E?\\d+|[+-]{0,1}(\\.{0,1}\\d+){0,1}");
			.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)(E\\d+)?");

	private static Pattern INTEGER_PATTERN = Pattern.compile("[+-]{0,1}\\d+");

	private static Pattern ZERO_VALUE_PATTERN = Pattern
			.compile("[+-]{0,1}0+\\.{0,1}0*");
	
	private static Pattern NEGATIVE_VALUE_PATTERN = Pattern
			.compile("-(\\d+(\\.\\d*)?|\\.\\d+)(E\\d+)?");

	private static Pattern DATA_FIELD_PATTERN = Pattern.compile("[N|C|D|F]\\d+",
			Pattern.CASE_INSENSITIVE);

	private static Pattern NUMBER_FIELD_PATTERN = Pattern.compile("[N|F]\\d+",
			Pattern.CASE_INSENSITIVE);

	private static Pattern FLOAT_FIELD_PATTERN = Pattern.compile("F\\d+",
			Pattern.CASE_INSENSITIVE);

	private static Pattern INTEGER_FIELD_PATTERN = Pattern.compile("N\\d+",
			Pattern.CASE_INSENSITIVE);
	
	public static boolean isNumber(String str) {
		return NUMBER_PATTERN.matcher(str).matches();
	}
	
	public static boolean isDataFieldName(String str) {
		return DATA_FIELD_PATTERN.matcher(str).matches();
	}
	
	public static boolean isNumberFieldName(String str) {
		return NUMBER_FIELD_PATTERN.matcher(str).matches();
	}
	
	public static boolean isFloatFieldName(String str) {
		return FLOAT_FIELD_PATTERN.matcher(str).matches();
	}
	
	public static boolean isIntegerFieldName(String str) {
		return INTEGER_FIELD_PATTERN.matcher(str).matches();
	}

	public static boolean isInteger(String str) {
		return INTEGER_PATTERN.matcher(str).matches();
	}

	public static boolean isZeroNumber(Object o) {
		return ZERO_VALUE_PATTERN.matcher(nonNullStr(o)).matches();
	}

	public static boolean isNegativeNumber(Object o) {
		return NEGATIVE_VALUE_PATTERN.matcher(nonNullStr(o)).matches();
	}

	static {
		SQL_TYPE_MAPPING.put(Integer.class, new Integer(Types.INTEGER));
		SQL_TYPE_MAPPING.put(Long.class, new Integer(Types.BIGINT));
		SQL_TYPE_MAPPING.put(Byte.class, new Integer(Types.CHAR));
		SQL_TYPE_MAPPING.put(Character.class, new Integer(Types.CHAR));
		SQL_TYPE_MAPPING.put(Float.class, new Integer(Types.FLOAT));
		SQL_TYPE_MAPPING.put(Double.class, new Integer(Types.DOUBLE));
		SQL_TYPE_MAPPING.put(Short.class, new Integer(Types.SMALLINT));
		SQL_TYPE_MAPPING.put(String.class, new Integer(Types.VARCHAR));
		SQL_TYPE_MAPPING.put(BigDecimal.class, new Integer(Types.DECIMAL));
		SQL_TYPE_MAPPING.put(Number.class, new Integer(Types.NUMERIC));
		SQL_TYPE_MAPPING.put(BigInteger.class, new Integer(Types.BIGINT));
		SQL_TYPE_MAPPING.put(Date.class, new Integer(Types.DATE));
		SQL_TYPE_MAPPING.put(java.sql.Date.class, new Integer(Types.DATE));
		SQL_TYPE_MAPPING.put(Timestamp.class, new Integer(Types.TIMESTAMP));
		SQL_TYPE_MAPPING.put(new Integer(Types.INTEGER), Integer.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.BIGINT), Long.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.CHAR), Byte.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.CHAR), Character.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.FLOAT), Float.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.DOUBLE), Double.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.SMALLINT), Short.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.VARCHAR), String.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.DECIMAL), BigDecimal.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.NUMERIC), Number.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.BIGINT), BigInteger.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.DATE), Date.class);
		SQL_TYPE_MAPPING.put(new Integer(Types.TIMESTAMP), Timestamp.class);
	}

	static {
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.INTEGER), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.BIGINT), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.CHAR), "�ַ���");

		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.FLOAT), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DOUBLE), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.SMALLINT), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.VARCHAR), "�ַ���");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DECIMAL), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.NUMERIC), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.BIGINT), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.DATE), "����");
		SQL_TYPE_MAPPING_STRING.put(String.valueOf(Types.TIMESTAMP), "�ַ���");
	}

	public static int getSQLTypeSymbol(Class clazz) {
		Integer i = (Integer) SQL_TYPE_MAPPING.get(clazz);
		if (i != null) {
			return i.intValue();
		}
		return -1;
	}

	public static Class getJavaType(int fromSqlType) {
		return (Class) SQL_TYPE_MAPPING.get(new Integer(fromSqlType));
	}

	public static String getStringType(int fromSqlType) {
		return (String) SQL_TYPE_MAPPING_STRING
				.get(String.valueOf(fromSqlType));
	}

	static {
		COMMON_CONFIG = ResourceBundle
				.getBundle("gov/nbcs/rp/common/common");
		df = new java.text.SimpleDateFormat(COMMON_CONFIG
				.getString("java.util.Date.format"));
	}

	private static Pattern wideStrPattern = Pattern.compile("[\u4e00-\u9fa5]+");

	/**
	 * ��Ҫͨ��һ���ַ��������ɲ���ֵ��ʱ�򣬿��������������ʽ��ģʽ��
	 */
	private static Pattern TRUE_PATTERN = Pattern.compile(
			"(\\s*[\\d&&[^0]]+\\s*)|(.*��.*)|(.*��.*)|(.*true.*)",
			Pattern.CASE_INSENSITIVE);

	/**
	 * �ж�һ�����ַ�����null����""��
	 * 
	 * @param s
	 *            ���жϵ��ַ���
	 * @return �жϽ��
	 */
	public static boolean isNullStr(String s) {
		return (s == null) || (s.length() <= 0);
	}

	/**
	 * �������е�ÿ���ַ�������滻һ���ַ����е�ÿ��ģʽ�� <br>
	 * ����<br>
	 * String s = "a#b#c#"; <br>
	 * s = replaceWithArray(s,"#",new String[]{"12","34","56"}); <br>
	 * ���s�������� "a12b34c56"
	 * 
	 * @param src
	 *            ���滻�����Ĵ�
	 * @param regex
	 *            ģʽ��
	 * @param arr
	 *            �����滻���ַ���
	 * @return ��������ַ���
	 */
	public static String replaceWithArray(String src, String regex,
			String arr[]) {
		if ((src != null) && (src.length() > 0)) {
			for (int i = 0; i < arr.length; i++) {
				if ((arr[i] != null) && (arr[i].length() > 0)) {
					src = src.replaceFirst(regex, arr[i]);
				}
			}
		}
		return src;
	}

	/**
	 * �ж�һ���ַ����Ƿ��ǿ��ַ������ַ�(Ŀǰ���ؾ��Ǻ���)
	 * 
	 * @param s
	 *            ���жϵ��ַ���
	 * @return �жϽ��
	 */
	public static boolean isWideString(String s) {
		return wideStrPattern.matcher(s).matches();
	}

	/**
	 * ͨ��һ�����������ɲ���ֵ�������ڸö�������ʵ�ֵ�toString()����
	 * 
	 * @param value
	 */
	public static boolean estimate(Object value) {
		return TRUE_PATTERN.matcher(nonNullStr(value)).matches();
	}

	/**
	 * ΪSQL���������ӱ���ģʽ���û���
	 * 
	 * @param sql
	 * @return
	 */
	public static String getTableName(String tableName) {
		return COMMON_CONFIG.getString("sql.table.schema") + '.' + tableName;
	}

	/**
	 * ���ɷ�ҳ��ѯ���������
	 * 
	 * @param startRow
	 * @param pageSize
	 * @return
	 */
	public static String getPageQrySQL(String sql, int startRow, int pageSize) {
		String cond = COMMON_CONFIG.getString("sql.rownum.condition");
		return cond.replaceAll("%SQL%", sql).replaceAll("%START%",
				String.valueOf(startRow)).replaceAll("%SIZE%",
				String.valueOf(pageSize));
	}

	/**
	 * ת������Ϊ����SQL����Ҫ�����ݣ������ַ���"ABC"����>"'ABC'"
	 * 
	 * @param o
	 *            ��ת��������
	 * @return ת�����������
	 */
	public static Object convertToSQLType(Object o) {
		if (o != null) {
			String regex = COMMON_CONFIG.getString("sql.replace.regex");
			//			Class c = o.getClass();
			// ��Formula�Ĵ��� by qj 2011-6-17 ����02:52:51
			if ((o instanceof String) || (o instanceof Formula)) {
				return "'" + o.toString().replaceAll("'", "''") + "'";
			} else if (o instanceof Character) {
				return "'" + o.toString() + "'";
			} else if (o instanceof java.util.Date) {
				String s = df.format((Date) o);
				return COMMON_CONFIG.getString("java.util.Date").replaceAll(
						regex, s);
			}
			return o;
		}
		return "''";
	}

	// public static Object convertToSQLTypeAndConvertNull(Object o,Class
	// valType) {
	//
	// String regex = COMMON_CONFIG.getString("sql.replace.regex");
	// Class c = o.getClass();
	// if (c.hashCode() == String.class.hashCode()) {
	// return "'" + o.toString().replaceAll("'","''").trim() + "'";
	// }else if(c.hashCode() == Character.class.hashCode()) {
	// return "'" + o.toString() + "'";
	// } else if (o instanceof java.util.Date) {
	// String s = df.format((Date) o);
	// return COMMON_CONFIG.getString("java.util.Date").replaceAll(
	// regex, s);
	// }
	// return o;
	//
	// }

	/**
	 * ת������Ϊ����SQL����Ҫ�Ĳ�ѯ�����������ַ���"ABC"����>"a='ABC'"
	 * 
	 * @param o
	 *            ��ת��������
	 * @return ת�����������
	 */
	public static Object convertToSQLCondition(String fieldName, Object o) {
		String prefix = fieldName + '=';
		Object value = convertToSQLType(o);
		if (o != null) {
			Class c = o.getClass();
			if ((c.hashCode() == String.class.hashCode())
					|| (c.hashCode() == Character.class.hashCode())) {
				if (Common.isNullStr(o.toString())) {
					return fieldName + " is null";
				}
			}
			return prefix + value;
		}
		return fieldName + " is null";
	}

	/**
	 * 15λ���֤��תΪ18λ
	 * 
	 * @param idCard
	 * @return
	 */
	public static String idCard15To18(String idCard) {
		if ((idCard != null) && (idCard.length() == 15)) {
			int W[] = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
					8, 4, 2, 1 };
			char A[] = new char[] { '1', '0', 'x', '9', '8', '7', '6', '5',
					'4', '3', '2' };
			idCard = idCard.substring(0, 6) + "19" + idCard.substring(6);
			int s = 0;
			for (int i = 0; i < 17; i++) {
				int j = ((idCard.charAt(i)) - 0x30) * W[i];
				s += j;
			}
			s = s % 11;
			return idCard + A[s];
		}
		return idCard;
	}

	/**
	 * �ɸ����ļ��������ɱ��ػ����ļ��������紫��conf������conf_zh_CN.properties
	 * 
	 * @param fName
	 *            �ļ�����
	 * @return ���ػ����ļ���
	 */
	public static String getLocalePropertiesFile(String fName) {
		return fName + "_" + Locale.getDefault().getLanguage() + "_"
				+ Locale.getDefault().getCountry() + ".properties";
	}

	public static boolean isEqual(Object o1, Object o2) {
		if (o1 == null) {
//			if (o2 == null)
//				return true;
//			if (o2 != null)
//				return false;
			return o2 == null;
		} else {
			if (o2 == null) {
				return false;
			} else {
				return (o1 == o2) || o1.equals(o2);
			}
		}
//		return false;
	}

	/**
	 * ��һ�����󷵻طǿյ��ַ���
	 * 
	 * @param o
	 *            ����Ķ���
	 * @return ���ɵ��ַ���
	 */
	public static String nonNullStr(Object o) {
		return nonNullStr(o, "");
	}

	public static String nonNullStr(Object o, String def) {
		String s = o == null ? def : o.toString();
		if (s.equalsIgnoreCase("null")) {
			s = "";
		}
		return s;
	}

	/**
	 * ��Դ��ɫֵAlpha���뵽Ŀ����ɫֵ��ȥ
	 */
	public static Color alphaBlending(Color dst, Color src, float alpha) {
		int r = alphaBlending(dst.getRed(), src.getRed(), alpha);
		int g = alphaBlending(dst.getGreen(), src.getGreen(), alpha);
		int b = alphaBlending(dst.getBlue(), src.getBlue(), alpha);
		return new Color(r, g, b);
	}

	/**
	 * ��Դ��ɫֵAlpha���뵽Ŀ����ɫֵ��ȥ
	 */
	public static int alphaBlending(int dst, int src, float alpha) {
		return (int) (alpha * dst + (1 - alpha) * src);
	}

	/**
	 * ��SQL��ѯ�����ת��ΪList<Map>���ݽṹ
	 * 
	 * @param rs
	 *            SQL��ѯ�����
	 * @return
	 * @throws Exception
	 */
	public static List convertResultSet(ResultSet rs) throws Exception {
		String columns[] = parseColumn(rs);
		List result = new LinkedList();
		while (rs.next()) {
			Map record = new XMLData();
			for (int i = 0; i < columns.length; i++) {
				record.put(columns[i], rs.getObject(i));
			}
		}
		return result;
	}

	/**
	 * ���ַ����ֵĸ��ϴ�����ȡ�����֣�����F18��������18��
	 * 
	 * @param complexStr
	 * @return
	 */
	public static String trimToNumber(String complexStr) {
		Matcher mat = NUM_STR_COMPLEX.matcher(complexStr);
		mat.find();
		return mat.group(1);
	}

	public static String trimNumberToStr(String complexStr) {
		Matcher mat = STR_NUM_COMPLEX.matcher(complexStr);
		mat.find();
		return mat.group(1);
	}

	/**
	 * �ɸ�ʽ���ַ���������־�����Ϣ
	 * 
	 * @param number
	 * @return
	 */
	public static int getScale(String format) {
		return new DecimalFormat(format).getMaximumFractionDigits();
	}

	/**
	 * ���ؽ�����е������ֶ���
	 * 
	 * @param rs
	 *            SQL��ѯ�����
	 * @return
	 * @throws Exception
	 */
	public static String[] parseColumn(ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		String[] columns = new String[meta.getColumnCount()];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = meta.getColumnName(i);
		}
		return columns;
	}

	// �ж��Ƿ���Ӣ�Ļ����ֻ��»���
	public static boolean IsValidIdent(String s) {
		Pattern eName = Pattern.compile("[a-z0-9A-Z_]+");
		return eName.matcher(s).matches();
	}

	/**
	 * ���ؽ�����еĵ�һ����¼�е�һ���ֶΣ� XXL
	 * 
	 * @param lstData
	 * @param sField
	 * @return
	 */
	public static String getAStringField(List lstData, String sField) {
		if ((lstData == null) || (lstData.size() == 0) || (sField == null)
				|| sField.equals("")) {
			return null;
		}
		XMLData aData = (XMLData) lstData.get(0);
		return getAStringField(aData, sField);

	}

	/**
	 * ȡ��һMap�е�һ�ֶε��ַ�ֵ XXL
	 * 
	 * @param aData
	 * @param sField
	 * @return
	 */
	public static String getAStringField(Map aData, String sField) {
		if (aData == null) {
			return null;
		}
		if (aData.get(sField) == null) {
			return null;
		}
		return aData.get(sField).toString();
	}

	/**
	 * ���ɵ�ǰ���εĿؼ�
	 * 
	 * @param text
	 * @return
	 */
	public static FLabel getBatchNoLabel(String text) {
		FLabel fBatchNo = new FLabel();
		fBatchNo.setText(text);
		Font font = new Font("����", Font.BOLD, 12);
		fBatchNo.setFont(font);

		return fBatchNo;
	}

	/**
	 * ���ݷָ��־{}��[]�ָ���ַ������� by ymq Split string.
	 * 
	 * @param aSource
	 *            the a source
	 * 
	 * @return the string[]
	 */
	public static String[] splitString(String aSource) {
		//	String elements[] = UntPub.formulaSplitter.split(aSource); // �ù�ʽ������ʽformulaSplitter������
		List result = new ArrayList();
		String st = aSource;
		if (!isNullStr(st)) {
			int i = st.indexOf("{");
			while (i >= 0) {
				int b = st.indexOf("}");
				result.add(st.substring(i + 1, b));
				st = st.substring(b + 1, st.length());
				i = st.indexOf("{");
			}
		}
		//		for (int i = 0; i < elements.length; i++) {
		//			if (!Common.isNullStr(elements[i].trim()) // elements[i]!=null
		//					// &&
		//					// elements[i].length()>0
		//					&& UntPub.refElementPattern.matcher(elements[i]).matches()) { // ������ʽ��ƥ��
		//				result.add(elements[i].replaceAll(UntPub.GS_FORMULALEFT_RIGHT,
		//						"").trim()); // ��GS_FORMULALEFT_RIGHT�滻Ϊ��
		//			}
		//		}
		String ret_str[] = new String[result.size()];
		System.arraycopy(result.toArray(), 0, ret_str, 0, ret_str.length);
		return ret_str;

	}

	// ������ͨ��ǰ����0ת��Ϊָ�����ȵ��ַ��� by ymq
	public static String getStrID(BigDecimal aint, int alength) {
		String strID = String.valueOf(aint);
		int len = alength - strID.length();
		for (int i = 0; i < len; i++) {
			strID = "0" + strID;
		}
		return strID;
	}

	// ���ַ��������л�ȡĿ���ַ����ĵ�һ��λ��
	public static int GetValuePosInArray(String aVaule, String[] aArray) {
		int result = -1;
		for (int i = 0; i < aArray.length; i++) {
			if (aArray[i] == aVaule) {
				result = i;
				break;
			}
		}
		return result;
	}

	// �����������л�ȡĿ�������ĵ�һ��λ��
	public static int GetValuePosInArray(int aVaule, int[] aArray) {
		int result = -1;
		// ���Ŀ��ֵΪ0�Ҳ����ڵ�һ������Ϊ������
		if ((aVaule == 0) && (aArray.length >= 0) && (aArray[0] != 0)) {
			return result;
		}
		for (int i = 0; i < aArray.length; i++) {
			if (aArray[i] == aVaule) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**������Ϣ�������㹫ʽ����������ж� JavaScript�﷨���磺{C1}=='����' && {F1}=100*/
	public static boolean getCheckFilter(String filter, Map m) throws Exception {
		if (isNullStr(filter) || (m == null)) {
			return false;
		}
		if ("1=1".equals(filter) || "1==1".equals(filter)) {
			return true;
		}

		return estimate(JSEvaluator.evaluate(filter, m));
	}

	public static boolean getCheckFilter(String filter, Record r)
			throws Exception {
		if (isNullStr(filter) || r.isEmpty()) {
			return false;
		}
		//		JudgeLogicExpressionInMap dd = new JudgeLogicExpressionInMap(m,filter);
		//	    return dd.getResult();
		return estimate(JSEvaluator.evaluate(filter, r));
	}

	// /������Ϣ�������㹫ʽ����������ж� Java�﷨���磺"��".equals(C1) || "��".equals(B3)
	public static boolean getCheckFilter2(String filter, Map m)
			throws Exception {
		if (isNullStr(filter) || m.isEmpty()) {
			return false;
		}
		//		JudgeLogicExpressionInMap dd = new JudgeLogicExpressionInMap(m,filter);
		//	    return dd.getResult();
		return estimate(JavaEvaluator.evaluate(filter, m, Boolean.TYPE));
	}

	/**
	 * ����Java�﷨���ַ����ȽϺ�������ֵΪ�ַ�������ֵΪ������
	 * 
	 * @param leftValue
	 * @param rightVarName
	 * @return
	 */
	public static String getStringCompareExp(String leftValue,
			String rightVarName) {
		return "\"" + leftValue + "\".equals(" + rightVarName + ")";
	}

	/**
	 * ����Java�﷨���ַ����ȽϺ�������ֵ����ֵ��Ϊ������
	 * 
	 * @param leftVarName
	 * @param rightVarName
	 * @return
	 */
	public static String getStringCompareExp2(String leftVarName,
			String rightVarName) {
		return leftVarName + "!=null&&" + leftVarName + ".equals("
				+ rightVarName + ")";
	}

	/**
	 * ������ֵĺ���,ֻ֧��0~10
	 * 
	 * author:����ϼ
	 */
	static public String GetNumberOfHz(int iNumber) {
		String Gs_NumberOfHz = "��һ�����������߰˾�ʮ"; // ���ִ�
		if ((iNumber >= 0) || (iNumber <= 10)) {
			return Gs_NumberOfHz.substring(iNumber, 1 + iNumber);
		}
		return null;
	}

	/**
	 * ���ã��޸ĸ��ؼ������ӿؼ��Ŀɱ༭״̬
	 * 
	 * @param aParentControl���ؼ�
	 * 
	 * @param bBoolean�Ƿ�ɱ༭
	 *            true���ɱ༭,false,���ɱ༭
	 * 
	 */
	public static void changeChildControlsEditMode(Compound aParentControl,
			boolean bBoolean) {
		// ��ÿؼ�
		List listControls = aParentControl.getSubControls();
		if (listControls==null) {
			return;
		}
		for (int i = 0; i < listControls.size(); i++) {
			if (listControls.get(i) instanceof FTextField) {
				((FTextField) listControls.get(i)).setEditable(bBoolean); // FTextField,��setEditable
			} else if (listControls.get(i) instanceof FTextArea) {
				((FTextArea) listControls.get(i)).setEditable(bBoolean); // FTextArea,��setEditable
			} else if (listControls.get(i) instanceof FRadioGroup) {
				((FRadioGroup) listControls.get(i)).setEditable(bBoolean); // FRadioGroup,��setEditable
			} else if (listControls.get(i) instanceof Compound) {
				changeChildControlsEditMode((Compound) listControls.get(i), // �ݹ飬���ӿؼ�
						bBoolean);
			} else if (listControls.get(i) instanceof CustomTree) {
				((CustomTree) listControls.get(i))
						.setIsCheckBoxEnabled(bBoolean);
			} else if (listControls.get(i) instanceof FLabel) {
			} else {
				((Component) listControls.get(i)).setEnabled(bBoolean); // �����ؼ�,��setEnabled
			}
		}
	}

	/**
	 * ���ݼ������ַ���ǰ����ӿո�.
	 * 
	 * @param level
	 *            the level
	 * @param src
	 *            the src
	 * @return the level string
	 */
	public static String indentByLevel(int level, String src) {
		for (int i = 0; i < level; i++) {
			src = "  " + src;
		}
		return src;

	}

	/**
	 * �����ַ��滻�����ַ�
	 * @param filter
	 * @param oldChar
	 * @param newChar
	 * @return String
	 */
	public static String replaceSign(String filter, char oldChar, char newChar) {
		if (filter == null) {
			return null;
		}
		char[] f = filter.toCharArray();
		StringBuffer reString = new StringBuffer();
		for (int i = 0; (f != null) && (i < f.length); i++) {
			if (f[i] == oldChar) {
				reString.append(newChar);
			} else {
				reString.append(f[i]);
			}
		}
		return reString.toString();
	}
	
	/**
	 * �ж��Ƿ�Ϊ���
	 */
	public static boolean isYear(String year){
		if((year==null)||"".equals(year)) {
			return false;
		}
		year = year.trim();
		if(year.length()!=4) {
			return false;
		}
		
//		if(year.charAt(0)!='2') return false;
		try{
			Integer.parseInt(year);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	/**
	 * Compares the.
	 * 
	 * @param d1
	 *            the d1
	 * @param d2
	 *            the d2
	 * @param scale
	 *            the scale
	 * @return -1, 0 or 1 as this BigDecimal is numerically less than, equal to,
	 *         or greater than val.
	 */
	public static int compare(double d1, double d2, int scale) {
		BigDecimal d1Value = new BigDecimal(d1).setScale(scale,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal d2Value = new BigDecimal(d2).setScale(scale,
				BigDecimal.ROUND_HALF_UP);
		return d1Value.compareTo(d2Value);
	}
	
	/** The Constant ZeroDecimal. */
	public static final BigDecimal ZeroDecimal = new BigDecimal(0.0);
	
	/**
	 * ��ȡ�ַ���
	 * @param str ԭ�ַ���
	 * @param beginSymbol ��ʼ���
	 * @param endSymbol �������
	 * @return
	 */
	public static String getSubString(String str ,String beginSymbol,String endSymbol ){
		int beginIndex = str.indexOf(beginSymbol) ;
		int endIndex = str.indexOf(endSymbol) ;
		return str.substring(beginIndex + 1 , endIndex) ;
	}
	
	/**
	 * ��ȡ�ַ���
	 * @param str ԭ�ַ���
	 * @param beginSymbol ��ʼ���
	 * @return
	 */
	public static String getSubString(String str ,String beginSymbol){
		int beginIndex = str.indexOf(beginSymbol) ;
		return str.substring(beginIndex + 1 ) ;
	}

}
