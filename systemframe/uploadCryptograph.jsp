
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%
    String username = (String)request.getParameter("user_code");
    String password = (String)request.getParameter("password");
 %>
<html> 
	<head>
		<title>JSP for UploadCryptographForm form</title>
	</head>
	<body>
	<B><FONT size='6'>密文上传：</FONT></B>
		<html:form action="/uploadCryptograph" enctype="multipart/form-data"><P></P><P></P><P></P> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			用&nbsp;&nbsp;户：<html:text property="username" value="<%=username%>"/> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			密&nbsp;&nbsp;码：<html:password property="password" value="<%=password%>"/> <br><br>
			&nbsp;&nbsp;&nbsp;&nbsp;
			文&nbsp;&nbsp;件：<html:file property="photo"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   <html:submit>上传</html:submit><br><br>
		   
		   <logic:notEmpty name="UploadCryptographForm" property="fname">	 
		      <%
		         String s = (String)request.getAttribute("successOrFail");
		         if(s.equalsIgnoreCase("success")){
		      %>    
		     <font size='4' color="red">&nbsp;&nbsp;&nbsp;上传已成功，您上传的文件是:
		       <bean:write name="UploadCryptographForm" property="fname"/>
		      </font>
		      <%
		         }
		         if(s.equalsIgnoreCase("fail")){
		      %>
		      <font size='4' color="red">上传失败,错误信息：
		        <bean:write name="UploadCryptographForm" property="fname"/>
		      </font>
		      <%}%>
		   </logic:notEmpty>		   
		   <P></P>
		</html:form>
	</body>
</html>