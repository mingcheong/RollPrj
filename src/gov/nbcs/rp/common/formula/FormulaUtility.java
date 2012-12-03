package gov.nbcs.rp.common.formula;

/**
 * @author л��ݥ
 * 
 * @version ����ʱ�䣺Apr 10, 20123:32:51 PM
 * 
 * @Description
 */
/**
 * Copyright ���շ�����Ԫ ��Ȩ���� ����Ԥ����ϵͳ
 * 
 * @author ������
 * @version 1.0
 */
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.FormulaTool;
import gov.nbcs.rp.common.MyHashSet;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;




public class FormulaUtility
{
	static final String ROW_FORMULA_ELEMENT_SYMBOL = "#ROW_FORMULA_ELEMENT_SYMBOL#";

	static final Pattern rowFormulaElementPattern = Pattern.compile(".+" + ROW_FORMULA_ELEMENT_SYMBOL + ".+");

	static final Pattern rowFormulaElementSplitter = Pattern.compile(ROW_FORMULA_ELEMENT_SYMBOL);

	private boolean negativeIsZero = false;



	public boolean isNegativeIsZero()
	{
		return negativeIsZero;
	}


	public void setNegativeIsZero(boolean negativeIsZero)
	{
		this.negativeIsZero = negativeIsZero;
	}


	/**
	 * ��֯���DataSet���м���Ĺ�ʽԪ������
	 * 
	 * @param bookmark
	 *            �б���ǩ
	 * @param element
	 *            ��ʽ����Ԫ����
	 * @return
	 */
	public static final String genRowFormulaElementName(String bookmark, String elementName)
	{
		return bookmark + ROW_FORMULA_ELEMENT_SYMBOL + elementName;
	}


	/**
	 * �ӿ��еĹ�ʽԪ����������ȡ��Ԫ�ر��������
	 */
	public static final String pickUpElementName(String rowFormulaElementName)
	{
		if (rowFormulaElementPattern.matcher(rowFormulaElementName).matches()) { return rowFormulaElementSplitter.split(rowFormulaElementName)[1]; }
		return rowFormulaElementName;
	}


	/**
	 * �ӿ��еĹ�ʽԪ����������ȡ���б���ǩ
	 */
	public static final String pickUpBookmark(String rowFormulaElementName)
	{
		if (rowFormulaElementPattern.matcher(rowFormulaElementName).matches()) { return rowFormulaElementSplitter.split(rowFormulaElementName)[0]; }
		return null;
	}



	DataSet ds;

	/**
	 * �Թ�ʽ������Ϊ��ֵ�����й����ֶ���Ϊֵ
	 */
	Map formulaAssoc = new MyMap();

	/**
	 * �Ͱ󶨵�DataSet��صĹ�ʽ����
	 */
	TreeSet formulaSet = new TreeSet(new FormulaComparator());

	Set formulaElements = new MyHashSet();

	/**
	 * ��ʽ���������Ŀ���
	 */
	private boolean maskRecalcute = false;



	public void maskRecalcute(boolean maskRecalcute)
	{
		this.maskRecalcute = maskRecalcute;
	}


	public FormulaUtility(DataSet ds)
	{
		this.ds = ds;
		this.ds.addDataChangeListener(new DataChangeListener()
		{// ��¼�仯��ʱ�������Ҫ���й�ʽ����

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;



					/*
					 * (non-Javadoc)
					 * 
					 * @see com.foundercy.fiscalbudget.common.datactrl.event.DataChangeListener#onDataChange(com.foundercy.fiscalbudget.common.datactrl.event.DataChangeEvent)
					 */
					public void onDataChange(DataChangeEvent event) throws Exception
					{
						if (event.type() == DataChangeEvent.FIELD_MODIRED && !maskRecalcute)
						{// ������ֶ�ֵ�޸ĵ��¼�
							Field field = (Field) event.getSource();
							String fieldEName = field.getName();
							if (field.isLatestModified())
							{
								startCalculate(fieldEName);
							}
						}
					}
				});
	}


	/**
	 * Starts the calculate. ��ָ��fieldEname��ʼ�����й�ʽ����
	 * 
	 * @param fieldEName
	 *            the field e name
	 * @throws Exception
	 *             the exception
	 */
	protected void startCalculate(String fieldEName) throws Exception
	{
		// ���㱾�ֶ�ֱ�Ӳ���Ĺ�ʽ�����������Ӱ���ֶ�
		List resultRowFields = recalculate(fieldEName);// �й�ʽ����

		String rowFormulaEleName = FormulaUtility.genRowFormulaElementName(FormulaUtility.this.ds.toogleBookmark(), fieldEName);
		for (Iterator itr = resultRowFields.iterator(); itr.hasNext();)
		{
			String s = (String) itr.next();
			// �ų�����
			if (!s.equals(rowFormulaEleName))
			{
				s = s.substring(s.lastIndexOf("#") + 1);
				recalculate(s);
			}
		}
	}


	/**
	 * ��һ���ֶο�ʼ�������������й�ʽ
	 * 
	 * @return ���������Ӱ����й�ʽ�ֶ�
	 */
	protected List recalculate(String fieldName) throws Exception
	{
		List resultRowFields = new ArrayList();
		// ��ʽԪ����
		String rowFormulaEleName = FormulaUtility.genRowFormulaElementName(ds.toogleBookmark(), fieldName);
		resultRowFields.add(rowFormulaEleName);
		if (formulaElements.contains(fieldName))
		{
			// �ǹ̶��й�ʽ
			calc(resultRowFields, fieldName);
		}
		if (formulaElements.contains(rowFormulaEleName))
		{
			// �̶��й�ʽ
			calc(resultRowFields, rowFormulaEleName);
		}
		return resultRowFields;
	}


	/**
	 * Calc. ����ָ���ֶ�
	 * 
	 * @param resultRowFields
	 *            the result row fields
	 * @param fieldName
	 *            the field name
	 * @throws Exception
	 *             the exception
	 */
	private void calc(List resultRowFields, String fieldName) throws Exception
	{
		Collection resultFields = new HashSet();
		resultFields.add(fieldName);
		for (Iterator it = formulaSet.iterator(); it.hasNext();)
		{
			FormulaObject fo = (FormulaObject) it.next();
			if (containsFieldList(fo, resultFields))
			{
				Map values = getFormulaValues(fo);
				// ����ֶ���
				String resultFieldName = fo.getResultField();// �´�ѭ���ñ��ν���ֶ���ȥ���Ƿ�����ع�ʽ�Ա��μ�����Ϊ������
				Object result = null;
				if (negativeIsZero)
				{
					// ����Ԫ�����и�ֵ��������Ϊ-1
					boolean hasNagetive = hasNegative(values);
					if (hasNagetive)
					{
						result = new BigDecimal(-1);
					}
					else
					{
						result = calcFormula(fo, values);
					}
				}
				else
				{
					result = calcFormula(fo, values);
				}
				writeResult(resultFieldName, result);
				// Common.consoleLogger().debug(ds
				// .toogleBookmark() + "|" + fieldName + ":=" + result);
				resultRowFields.add(FormulaUtility.genRowFormulaElementName(ds.toogleBookmark(), resultFieldName));
				resultFields.add(resultFieldName);
			}
		}
	}


	/**
	 * Checks for negative.
	 * 
	 * @param values
	 *            the values
	 * @return true, if successful
	 */
	private boolean hasNegative(Map values)
	{
		for (Iterator valItr = values.values().iterator(); valItr.hasNext();)
		{
			Object val = (Object) valItr.next();
			if (val instanceof BigDecimal)
			{
				BigDecimal dec = (BigDecimal) val;
				if (dec.compareTo(UntPub.ZERO_DECIMAL) < 0) { return true; }
			}
			else if (val instanceof Double)
			{
				Double dbl = (Double) val;
				if (dbl.doubleValue() < 0) { return true; }
			}
			else if (val instanceof Integer)
			{
				Integer integer = (Integer) val;
				if (integer.intValue() < 0) { return true; }
			}
		}
		return false;
	}


	/**
	 * Contains field list.
	 * 
	 * @param fo
	 *            the fo
	 * @param fields
	 *            the fields
	 * @return true, if successful
	 */
	protected boolean containsFieldList(FormulaObject fo, Collection fields)
	{
		for (Iterator it = fields.iterator(); it.hasNext();)
		{
			if (((Set) formulaAssoc.get(fo)).contains(it.next())) { return true; }
		}
		return false;
	}


	/**
	 * ��һ�����ݼ�������һ����ʽ
	 * 
	 * @parem formulaData ���й�ʽ��Ϣ�����ݼ�
	 * @param resultField
	 *            �������ֶ�
	 * @param expressionField
	 *            ��ʽ���ʽ
	 * @param priorityField
	 *            ��ʽ���ȼ�
	 */
	public static FormulaObject createFormulaObject(final String resultField, final String expression, final int priority)
	{
		return new FormulaObjectImpl(resultField, expression, priority);
	}


	/**
	 * ��������д�ص�DataSet��ȥ
	 */
	protected void writeResult(String fieldName, Object result) throws Exception
	{
		this.maskRecalcute(true);
		String bookmark = pickUpBookmark(fieldName);
		String elementName = pickUpElementName(fieldName);
		if (bookmark != null)
		{
			ds.maskDataChange(true);
			String tmpBookmark = ds.toogleBookmark();
			ds.gotoBookmark(bookmark);
			ds.maskDataChange(false);
			ds.fieldByName(elementName).setValue(result);
			ds.maskDataChange(true);
			ds.gotoBookmark(tmpBookmark);
			ds.maskDataChange(false);
		}
		else
		{
			ds.fieldByName(elementName).setValue(result);
		}
		this.maskRecalcute(false);
	}


	/**
	 * ��ȡ��ʽ������Ҫ�ĸ���Ԫ�ض�Ӧֵ
	 */
	protected Map getFormulaValues(FormulaObject fo) throws Exception
	{
		Map values = new MyMap();
		Set assocFields = (Set) this.formulaAssoc.get(fo);
		for (Iterator it = assocFields.iterator(); it.hasNext();)
		{
			String fieldName = (String) it.next();
			String bookmark = pickUpBookmark(fieldName);
			String elementName = pickUpElementName(fieldName);
			if (bookmark != null)
			{
				ds.maskDataChange(true);
				String tmpBookmark = ds.toogleBookmark();
				ds.gotoBookmark(bookmark);
				Object value = ds.fieldByName(elementName).getValue();
				values.put(fieldName, value);
				ds.gotoBookmark(tmpBookmark);
				ds.maskDataChange(false);
			}
			else
			{
				Object value = ds.fieldByName(elementName).getValue();
				values.put(fieldName, value);
			}
		}
		return values;
	}


	/**
	 * Process value.
	 * 
	 * @param value
	 *            the value
	 * @return the object
	 */
	private Object processValue(Object value)
	{
		Object res;
		if (value instanceof BigDecimal)
		{
			BigDecimal dec = (BigDecimal) value;
			if (dec.compareTo(UntPub.ZERO_DECIMAL) < 0)
			{
				dec = new BigDecimal(0.0);
			}
			res = dec;
		}
		else if (value instanceof Double)
		{
			Double dbl = (Double) value;
			if (dbl.doubleValue() < 0)
			{
				dbl = new Double(0.0);
			}
			res = dbl;
		}
		else if (value instanceof Integer)
		{
			Integer integer = (Integer) value;
			if (integer.intValue() < 0)
			{
				integer = new Integer(0);
			}
			res = integer;
		}
		else
		{
			res = value;
		}
		return res;
	}


	/**
	 * �õ����ȼ���ֵ������ȼ���ͣ��Ĺ�ʽ
	 * 
	 * @return
	 */
	public FormulaObject getLastFormula()
	{
		return (FormulaObject) formulaSet.last();
	}


	/**
	 * �õ����ȼ���ֵ��С�����ȼ���ߣ��Ĺ�ʽ
	 * 
	 * @return
	 */
	public FormulaObject getFirstFormula()
	{
		return (FormulaObject) formulaSet.first();
	}


	/**
	 * ����һ����ʽ
	 * 
	 * @param fo
	 */
	protected Object calcFormula(FormulaObject fo, Map values) throws Exception
	{
		Object result = FormulaTool.getValue(fo.getExpression(), values);
		values.put(fo.getResultField(), result);// ��������ʽ�ļ�����Ҳ���뵽���㹫ʽ��Ҫ���ֶ�ֵӳ����ȥ��
		// ������һ���ͱ��μ������йصĹ�ʽ�Ϳ����ñ��ν�����м���
		return result;
	}


	/**
	 * ���һ����ʽ����
	 * 
	 * @param formulaObj
	 */
	public void addFormula(FormulaObject formulaObj)
	{
		String formula = formulaObj.getExpression();
		String formulaElementArray[] = Common.splitString(formula);
		Collection formulaElementSet = Arrays.asList(formulaElementArray);
		formulaElements.addAll(formulaElementSet);
		formulaAssoc.put(formulaObj, new MyHashSet(formulaElementSet));
		formulaSet.add(formulaObj);
	}



	/**
	 * ��ʽ���ȼ�������Ҫ�ıȽϹ���
	 */
	class FormulaComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			int i1 = ((FormulaObject) o1).getPriority();
			int i2 = ((FormulaObject) o2).getPriority();
			return i1 >= i2 ? 1 : -1;
		}
	}

}
