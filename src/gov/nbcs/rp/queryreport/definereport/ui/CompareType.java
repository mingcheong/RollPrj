package gov.nbcs.rp.queryreport.definereport.ui;

public class CompareType {
	static String COMPARE_TYPE = "=#等于+<>#不等于+>#大于+>=#大于等于+<#小于+<=#小于等于+like#包含(like)+not like#不包含(not like)+in#在里面(in)+not in#不在里面(not in)";

	public static String COMPARE_TYPE_NUM = "=#等于+<>#不等于+>#大于+>=#大于等于+<#小于+<=#小于等于";

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
