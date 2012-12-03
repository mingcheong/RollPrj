<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
    <a href="upload.jsp">上传明文</a><br><br><br>
    <A href="download.jsp">下载明文</A><BR><br><BR>
    <A href="Main.jsp">上传/下载明文</A><br><br><br>
    <a href="uploadCryptograph.jsp">上传密文</a><br><br><BR>
    <a href="downloadCryptograph.jsp">下载密文</a><BR><BR><BR>
    <A href="MainCryptograph.jsp">上传/下载密文</A><br><br><BR>
  </body>
</html>
