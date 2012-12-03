
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'Main.jsp' starting page</title>

  </head>
  
  <FRAMESET rows='300,*' cols='*' frameborder='NO' border='0' framespacing='0'>
      <FRAME src="upload.jsp" name="upload"  scrolling="NO" noresize>
      <FRAME src="download.jsp" name="download" noresize>
  </FRAMESET>  
  <noframes>
  <body>
  </body>
  </noframes>
</html>
