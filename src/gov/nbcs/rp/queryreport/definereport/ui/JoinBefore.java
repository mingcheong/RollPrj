package gov.nbcs.rp.queryreport.definereport.ui;

public class JoinBefore {
	static String JOIN_BEFORE = "and#≤¢«“(AND)+or#ªÚ’ﬂ(OR)";

	public static String getJoinBeforeName(String joinBeforeValue) {
		String[] joinBeforeArray = JOIN_BEFORE.split("\\+");
		int length = joinBeforeArray.length;
		if (length == 0)
			return "";
		String[] value;
		for (int i = 0; i < length; i++) {
			value = joinBeforeArray[i].split("\\#");
			if (joinBeforeValue.equalsIgnoreCase(value[0]))
				return value[1];
		}
		return "";
	}

}
