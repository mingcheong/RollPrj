package gov.nbcs.rp.common.js;

import gov.nbcs.rp.common.js.javascript.Context;
import gov.nbcs.rp.common.js.javascript.Script;

import java.util.HashMap;
import java.util.Map;


public class JavaScriptFactory {

	private static JavaScriptFactory javaScriptFactory = new JavaScriptFactory();

	public static Script getInstance(Context ctx, String strScript) {
		return javaScriptFactory.getScript(ctx, strScript);
	}

	/**
	 * js脚本Script缓存
	 */
	private static Map scriptCache = new HashMap();

	private JavaScriptFactory() {
	}

	public synchronized Script getScript(Context ctx, String strScript) {
		if (scriptCache.containsKey(strScript)) {
			return (Script) scriptCache.get(strScript);
		} else {
			//qinj 缓冲数量过大时清空
			if(scriptCache.size() > 8192) {
				scriptCache.clear();
			}
			Script sr = ctx.compileString(strScript, "<cmd>", 1, null);
			scriptCache.put(strScript, sr);
			return sr;
		}

	}
}

