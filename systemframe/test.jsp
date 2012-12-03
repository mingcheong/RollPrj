<%@ page language="java" pageEncoding="UTF-8" import="java.util.*"%>
<% 
// 得到文件名字和路径 
String filename = (String)request.getAttribute("strFileName"); 

// 设置响应头和下载保存的文件名 
response.setContentType("APPLICATION/OCTET-STREAM"); 
response.setHeader("Content-Disposition", 
"attachment; filename=\"" + filename + "\""); 

// 打开指定文件的流信息 
java.io.FileInputStream fileInputStream = 
new java.io.FileInputStream("c:\\" + filename); 

// 写出流信息 
int i; 
while ((i=fileInputStream.read()) != -1) { 
out.write(i); 
} 
fileInputStream.close(); 
out.close(); 
%> 

