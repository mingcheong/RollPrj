package gov.nbcs.rp.common.js;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.Record;
import gov.nbcs.rp.common.js.javascript.Context;
import gov.nbcs.rp.common.js.javascript.Script;
import gov.nbcs.rp.common.js.javascript.Scriptable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JSEvaluator {
    private static Map executorCache = new MyMap();

    /**
     * ����javascript�ű��ļ����ƻ�ȡһ��JSEvaluator����
     *
     * @param jsFileUri
     *            javascript�ű��ļ���
     * @return
     */
    public static JSEvaluator getScriptExecutor(String jsFileUri)
            throws Exception {
        JSEvaluator eval = (JSEvaluator) executorCache.get(jsFileUri);
        if (eval == null) {
            eval = new JSEvaluator(jsFileUri);
            executorCache.put(jsFileUri, eval);
        }
        return eval;
    }

    private String jsContent;

    private Map methodScriptCache = new MyMap();

    protected JSEvaluator(String jsFileUri) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(jsFileUri)));
        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        this.jsContent = buffer.toString();
    }

    public Object invoke(String methodName) throws Exception {
        return invoke(methodName, null);
    }

    /**
     * ����һ��JS�еĺ���
     *
     * @param methodName
     *            ��������
     * @param params
     *            ����Ĳ�����
     * @return
     * @throws Exception
     */
    public Object invoke(String methodName, Object[] params) throws Exception {
        String methodBody = getMethodBody(this.jsContent, methodName);
        String paramNames[] = getParamNames(params);
        String invokeStr = (String) methodScriptCache.get(methodName);
        if (invokeStr == null) {
            invokeStr = getInvokeString(methodBody, methodName, paramNames);
            methodScriptCache.put(methodName,invokeStr);
        }
        Map vars = new HashMap();
        for (int i = 0; i < params.length; i++) {
            vars.put(paramNames[i], params[i]);
        }
        return JSEvaluator.evaluate(invokeStr, vars);
    }

    /**
     * ��js�ű����ݺͷ�������÷�����
     *
     * @param jsFileContent
     * @param methodName
     * @return
     */
    protected String getMethodBody(String jsFileContent, String methodName) {
        Pattern jsFuncNamePattern = Pattern.compile(".*(function\\s*"
                + methodName + "\\s*\\().*");
        Matcher mat = jsFuncNamePattern.matcher(jsFileContent);
        mat.find();
        String jsFuncPartName = mat.group(1);
        int funcStart = jsFileContent.indexOf(jsFuncPartName);
        int leftBracket = 0, rightBracket = 0;
        int i = funcStart;
        for (; i < jsFileContent.length(); i++) {
            char c = jsFileContent.charAt(i);
            if (c == '{') {
				leftBracket++;
			}
            if (c == '}') {
				rightBracket++;
			}
            if ((leftBracket > 0) && (rightBracket > 0)
                    && (leftBracket == rightBracket)) {
                break;
            }
        }
        return jsFileContent.substring(funcStart, i + 1);
    }

    /**
     * ���ݵ��õķ��������������б�����һ�����ú����Ľű���
     *
     * @param methodName
     * @param paramNames
     * @return
     */
    protected String getInvokeString(String methodBody, String methodName,
            String[] paramNames) {
        StringBuffer sb = new StringBuffer();
        sb.append(methodBody);
        sb.append('\n');
        sb.append(methodName);
        sb.append('(');
        for (int i = 0; i < paramNames.length; i++) {
            sb.append(paramNames[i]);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * ���ݲ���ֵ�����ɲ������ֱ�
     *
     * @param params
     * @return
     */
    protected String[] getParamNames(Object[] params) {
        if (params == null) {
			return null;
		}
        String names[] = new String[params.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = "JSEvaluator" + i;
        }
        return names;
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @return
     */
    public static Object evaluate(InputStream jsContent) throws Exception {
        return evaluate(jsContent, null, null)[0];
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @param values
     *            ���ʽ����Ԫ�ص�ֵ�����磬����б��ʽa>b,��ôvaluesӦ������a=?,b=?��
     * @return
     */
    public static Object evaluate(InputStream jsContent, Map values)
            throws Exception {
        return evaluate(jsContent, values, null)[0];
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @param values
     *            ���ʽ����Ԫ�ص�ֵ�����磬���a>b,��ôvaluesӦ������a=?,b=?��
     * @param resultsVar
     *            ��Ҫ��ȡ�ļ������ı�������
     * @return
     */
    public static Object[] evaluate(InputStream jsContent, Map values,
            String[] resultsVar) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                jsContent));
        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return evaluate(buffer.toString(), values, resultsVar);
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @return
     */
    public static Object evaluate(String jsContent) {
        return evaluate(jsContent, null, null)[0];
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @param values
     *            ���ʽ����Ԫ�ص�ֵ�����磬����б��ʽa>b,��ôvaluesӦ������a=?,b=?��
     * @return
     */
    public static Object evaluate(String jsContent, Map values) {
        return evaluate(jsContent, values, null)[0];
    }

    /**
     * ����һ��Javascript�Ľű�
     *
     * @param jsContent
     *            �ű�����
     * @param values
     *            ���ʽ����Ԫ�ص�ֵ�����磬���a>b,��ôvaluesӦ������a=?,b=?��
     * @param resultsVar
     *            ��Ҫ��ȡ�ļ������ı�������
     * @return
     */
    public static Object[] evaluate(String jsContent, Map values,
            String[] resultsVar) {
        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();
        writeParameters(scope, values);
        try {
            Object result = ctx.evaluateString(scope, jsContent, "<cmd>", 1,
                    null);
            if ((resultsVar == null) || (resultsVar.length <= 0)) {
                return new Object[] { result };
            } else {
                Object results[] = new Object[resultsVar.length];
                for (int i = 0; i < results.length; i++) {

              	results[i] = scope.get(resultsVar[i], scope);
                }
                return results;
            }
        } finally {
            Context.exit();
        }
    }

    /**
     * ��ָ����record����js�ű�
     *
     * @param jsContent
     *            �ű�����
     * @param record
     *            ���ʽ����Ԫ�صļ�¼
     * @return
     */
    public static Object evaluate(String jsContent, Record record) {
        return evaluate(jsContent, record, null)[0];
    }

    /**
     * ��ָ����record����js�ű�
     *
     * @param jsContent
     * @param record
     * @param resultsVar
     * @return
     */
    public static Object[] evaluate(String jsContent, Record record,
            String[] resultsVar) {
        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();
        writeParameters(scope, record);
        try {
        	Script script = JavaScriptFactory.getInstance(ctx, jsContent);
        	Object result = script.exec(ctx, scope);
            if ((resultsVar == null) || (resultsVar.length <= 0)) {
                return new Object[] { result };
            } else {
                Object results[] = new Object[resultsVar.length];
                for (int i = 0; i < results.length; i++) {

              	results[i] = scope.get(resultsVar[i], scope);
                }
                return results;
            }
        } finally {
            Context.exit();
        }
    }
    public static Object evaluateNumber(String jsContent, Record record) {
        return evaluateNumber(jsContent, record, null)[0];
    }
    public static Object[] evaluateNumber(String jsContent, Record record,
            String[] resultsVar) {
        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();
        writeNumberParameters(scope, record);
        try {
        	Script script = JavaScriptFactory.getInstance(ctx, jsContent);
        	Object result = script.exec(ctx, scope);
            if ((resultsVar == null) || (resultsVar.length <= 0)) {
                return new Object[] { result };
            } else {
                Object results[] = new Object[resultsVar.length];
                for (int i = 0; i < results.length; i++) {

              	results[i] = scope.get(resultsVar[i], scope);
                }
                return results;
            }
        } finally {
            Context.exit();
        }
    }

    /**
     * ��һ��Mapӳ���м��صı�������ֵ��Ϊjs�ű��ı�������д��
     *
     * @param scope
     * @param values
     */
    public static void writeParameters(Scriptable scope, Map values) {
        if ((values != null) && !values.isEmpty()) {
            for (Iterator it = values.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                Object value = values.get(key);
                if(value !=null){
                	value = (""+value).trim();
                }
                scope.put(Common.nonNullStr(key), scope, value);
            }
        }
    }

    /**
     * ���ƶ�record�е����ݴ���scope
     *
     * @param scope
     * @param record
     */
    public static void writeParameters(Scriptable scope, Record record) {
		if ((record != null) && !record.isEmpty()) {
			for (Iterator it = record.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				if (!DataSet.RECORD_STATE_ID.equals(key)) {
					Field field = (Field) record.get(key);
					if (field != null) {
						Object value = field.getValue();
						if (value != null) {
							value = value.toString().trim();
						}
						scope.put(Common.nonNullStr(key), scope, value);
					}
				}
			}
		}
	}
    public static void writeNumberParameters(Scriptable scope, Record record) {
		if ((record != null) && !record.isEmpty()) {
			for (Iterator it = record.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				if (!DataSet.RECORD_STATE_ID.equals(key)) {
					Field field = (Field) record.get(key);
					if (field != null) {
						Object value = field.getValue();
						if ((value != null)
//								&& field.getValueType() != null
//								&& field.getValueType().isAssignableFrom(Number.class)
								&& Common.isNumber(value.toString())) {
							try {
								value = field.getNumber();
							} catch (Exception e) {
								value = new Integer(0);
							}
						} else {
							value = new Integer(0);
						}
						scope.put(Common.nonNullStr(key), scope, value);
					}
				}
			}
		}
	}

    public static void main(String args[]) {
        Map values = new HashMap();
        values.put("ACCT_CODE","2010101");
        values.put("ACCT_CODE_JJ","30304");
        values.put("C9", "רҵ������λ[��]ʮ��");
//        Object bool = JSEvaluator.evaluate("(ACCT_CODE.indexOf('2010101')>=0 && " +
//                "ACCT_CODE_JJ.indexOf('30304')>=0) || (ACCT_CODE.indexOf('2010101')>=0 " +
//                "&& ACCT_CODE_JJ.indexOf('3019904')>=0)",values);
        String dd ="C9=='רҵ������λ[��]ʮ��' && ACCT_CODE.indexOf('035')!=0 && ACCT_CODE.indexOf('026')!=0";
        Object bool = JSEvaluator.evaluate(dd,values);
        System.out.println(bool);

    }
}
