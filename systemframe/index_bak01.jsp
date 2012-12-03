<!-- saved from url=(0022)http://internet.e-mail -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=GBK"%>
<%@ page language="java" session="true" import="java.util.*"%>
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
<body>
<form id='first' name='first'>
<img src="images/index.jpg" width="1000" height="579" border="0" usemap="#Map">
<map name="Map">
  <area shape="rect" coords="415,495,479,518" href="http://<%=request.getHeader("Host")%>/systemframe/login?sysapp=001&jnlp=platform">
  <area shape="rect" coords="408,530,445,550" href="jre.exe">
<%
   List list = (List)request.getAttribute("sysapp");
   for(int i=0;list!=null && i<list.size();i++)
   {
      Map map = (Map)list.get(i);
      String appId = (String)map.get("sys_id");
      String appName = (String)map.get("sys_name");
      String url = (String)map.get("url");
      StringBuffer area = new StringBuffer("<area shape='rect' href='"+url+"' ");
	  if(appId.equalsIgnoreCase("111"))
	  {
		area.append("coords='167,269,257,285'");
	  }else if (appId.equalsIgnoreCase("112")) 
	  {
		area.append("coords='298,265,365,285'");
	  }
	  else if(appId.equalsIgnoreCase("115"))
	  {
	     area.append("coords='405,269,467,285'");
	  }	
	  else if(appId.equalsIgnoreCase("121"))
	  {
	     area.append("coords='507,269,570,285'");
	  }
	  else if(appId.equalsIgnoreCase("201"))
	  {
	     area.append("coords='610,269,673,285'");
	  }
	  else if(appId.equalsIgnoreCase("211"))
	  {
	     area.append("coords='713,269,777,285'");
	  }	
	  else if(appId.equalsIgnoreCase("301"))
	  {
	     area.append("coords='817,269,888,285'");
	  }
	  else if(appId.equalsIgnoreCase("002"))
	  {
	     //area.append("coords='758,277,840,309'");
	  }
	  area.append("/>");
	  out.println(area.toString());
	  area = null; 	    		  	   
   }
%>
</map>
</form>
</body>
</html>

