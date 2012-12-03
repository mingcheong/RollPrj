<!-- saved from url=(0022)http://internet.e-mail -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=GBK"%>
<%@ page language="java" session="true" import="java.util.*"%>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="com.foundercy.pf.framework.webstartbean.*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>
<%
  out.print(request.getAttribute("framename"));
%>
</title>
<meta http-equiv="page-enter" content="">
<style type="text/css">
.sysapp {
	position: absolute;
	margin-top:270px;
	margin-left:150px;
    padding:4px;
    filter:
        Dropshadow(offx=1,offy=0,color=black)
        Dropshadow(offx=0,offy=1,color=black)
        Dropshadow(offx=0,offy=-1,color=black)
        Dropshadow(offx=-1,offy=0,color=black)
}
a {
	text-decoration:none;
	font:bold 16px "Verdana" ;
	color: white;
}
</style>
<style type="text/css">
<!--
body 
{
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body >
  <span class="sysapp">
<%
   	List list = (List)SysAppFactory.getSysApps();
	SimpleDateFormat format = new SimpleDateFormat("yyyy");
   	String setyear = format.format(new Date());
   	for(int i=0;list!=null && i<list.size();i++)
   	{
	  SysAppBean bean = (SysAppBean)list.get(i);
      String appId = bean.getId();
      String appName = bean.getName();
      String appUrl = "http://"+request.getHeader("Host")+"/systemframe/login";
      String appYear = bean.getSysYears().length>0?bean.getSysYears()[0]:setyear;
      String url = appUrl+"?sysapp="+appId+"&setyear="+appYear;

%>
<a href="<%=url%>" target="_blank"><%=appName%></a><a>&nbsp;&nbsp;|&nbsp;&nbsp;</a>
<%} %>
</span>
<img src="images/index.jpg" width="1000" height="579" border="0" usemap="#Map">
<map name="Map">
  <area shape="rect" coords="415,495,479,518" href="http://<%=request.getHeader("Host")%>/systemframe/login?sysapp=001&jnlp=platform">
  <area shape="rect" coords="408,530,445,550" href="jre.exe">
</map>
</body>
</html>

