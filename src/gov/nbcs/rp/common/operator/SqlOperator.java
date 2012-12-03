/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.operator;

import java.util.Map;

/**
 * 自定义数据脚本执行模块（服务端）.
 * 
 * @author 1218
 */
public class SqlOperator {

	/**
	 * Handle the.
	 * 
	 * @param bizType
	 *            the biz type
	 * @param param
	 *            the param 
	 * @return the string
	 */
	public String handle(int bizType, String objectId, String divCode, int batchNo, int dataType, Map param) {
		// 判断在fb_u_sql中查找符合bizType的sql语句

		// 当条件allow_cond满足时，执行sql_lines脚本
		
		
		// 当条件allow_cond不满足时，返回DENY_HINT文字提示
		

		// 返回执行结果说明
		
		return null;
	}
}
