
<%@page contentType="text/html;charset=GBK"%>
<%
  response.setHeader("Pragma", "No-cache");
  response.setHeader("Cache-Control", "no-cache");
  response.setDateHeader("Expires", -1);
%>
 
<html>
<head>
<title>�������Ż�</title>
<script language="javascript">
   window.name="foundercy_main";
   window.status="��ǰ��¼�û��� ";
   function clearsession(){

     window.open("/login.do?step=logout","");

   }
</script>
</head>

<frameset  rows="45,*" frameborder="YES" border="0" framespacing="1">
  <frame name="topFrame" scrolling="NO" noresize src="top.jsp" frameborder="YES">
  <frame name="buttomFrame" src="buttom.jsp">
</frameset>
<noframes><body bgcolor="#FFFFFF">

</body></noframes>
</html>
 