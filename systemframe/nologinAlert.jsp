<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<jsp:directive.page import="com.foundercy.pf.framework.webstartbean.PortalConfig"/>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>警告</title>
</head>
 
<body bgcolor="#F7F7F7">
<img src="images/warning.gif" width="86" height="83" /><span  style="font-size:14px;">用户验证没有通过，请重新&nbsp;&nbsp;</span><a href="<%=PortalConfig.getLoginUrl()%>" target="_blank" style="color: #0000FF">登陆</a> 
</body>
</html>

