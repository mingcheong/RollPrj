package gov.nbcs.rp.queryreport.definereport.ui;

public class CompareType {
	static String COMPARE_TYPE = "=#����+<>#������+>#����+>=#���ڵ���+<#С��+<=#С�ڵ���+like#����(like)+not like#������(not like)+in#������(in)+not in#��������(not in)";

	public static String COMPARE_TYPE_NUM = "=#����+<>#������+>#����+>=#���ڵ���+<#С��+<=#С�ڵ���";

	static String IN_TYPE = "in";

	static String NOTIN_TYPE = "not int";

	public static String getComparTypeName(String comparTypeValue) {
		if (comparTypeValue == null)
			return "";
		String[] comparTypeArray = COMPARE_TYPE.split("\\+");
		int length = comparTypeArray.length;
		if (length == 0)
			return "";
		String[] value;
		for (int i = 0; i < length; i++) {
			value = comparTypeArray[i].split("\\#");
			if (comparTypeValue.equalsIgnoreCase(value[0]))
				return value[1];
		}
		return "";
	}
}
