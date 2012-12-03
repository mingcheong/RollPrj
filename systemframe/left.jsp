<%@page contentType="text/html;charset=GBK" import="java.util.*"%>
<%@ page import="com.foundercy.pf.framework.interfaces.ISysFrame"%>
<%@ page import="com.foundercy.pf.util.sessionmanager.SessionUtil"%> 

<%
  String role_id = "{39FE6292-5B52-11DB-A476-826EFBF06BDC}";
  ISysFrame is = (ISysFrame)SessionUtil.getServerBean("sys.sysFrameService");
  List vTopMenu = is.getEnabledMenusByRoleForBS(role_id,"2006","201","");
  //List vTopMenu = (List)session.getAttribute("menulist");
  String name;
  String strID;
  int topI=0;

  response.setHeader("Pragma", "No-cache");
  response.setHeader("Cache-Control", "no-cache");
  response.setDateHeader("Expires", -1);
%>
<html>
<head>
<title>left</title>
<style type="text/css">
<!--
a {  font-size: 9pt; color: #000000; text-decoration: none}
body {  font-size: 9pt; color: #000000; text-decoration: none}
table {  font-size: 9pt; color: #000000; text-decoration: none}
a:link {  font-size: 9pt; color: #000000; text-decoration: none}
a:hover {  font-size: 9pt; color: #0000CC; text-decoration: none}
a:active {  font-size: 9pt; color: #CC0000}
-->
</style>

<SCRIPT language="javascript">
var last_id=0;
function show_submenu(id)
{
  if(id==0){

    for(i=1;i<=max_id;i++){
      eval("menu"+i+".style.display='none';");
      eval("td_menu"+i+".height=1;");
    }
    eval("menu"+id+".style.display='block';");
    eval("td_menu"+id+".height='100%';");
	return;
  }
  if(last_id!=id){
    for(i=0;i<=id;i++){
      eval("menu"+i+".style.display='none';");
      eval("td_menu"+i+".height=1;");
    }
    eval("menu"+last_id+".style.display='none';");
    eval("td_menu"+last_id+".height=1;");
    eval("menu"+id+".style.display='block';");
    eval("td_menu"+id+".height='100%';");
    last_id=id;
    return;
  }
}

function loginAct(cool){
  document.loginform.action.value=cool;
  document.loginform.submit();
}
</SCRIPT>
</head>
<body leftmargin="0" topmargin="0">
<table border=0 cellpadding=0 cellspacing=0 height="100%" width="112%">
 <tbody>
 

 <tr>
      <td background="photo/bt.gif" align=middle height=25 noWrap
   onClick=javascript:show_submenu(0);
   style="BORDER-BOTTOM: rgb(12,12,12) 1px solid; BORDER-LEFT: rgb(253,244,249) 0px solid; BORDER-RIGHT: rgb(12,12,12) 1px solid; BORDER-TOP: rgb(253,244,249) 0px solid; CURSOR: hand"
   width="100%"><font style="FONT-SIZE: 9pt">œµÕ≥…Ë÷√</font></td>
 </tr>
 <tr>
   <td height="100%" id=td_menu0>
     <div id=menu0 style="DISPLAY: none">
       <table border=0 cellpadding=0 cellspacing=0 height="100%" width="100%">
         <tbody>
         <tr>
           <td background="photo/bg.gif" valign="top">
               <h1><iframe frameborder=0 height="100%" src="include/system.jsp" width="100%"></iframe></h1>
           </td>
         </tr>
         </tbody>
       </table>
     </div>
   </td>
 </tr>
  <%
    //for(topI=0;topI<2;topI++){
      //Menu topMenu=(Menu)vTopMenu.get(topI);
      for(Iterator it = vTopMenu.iterator();it.hasNext();) {
      	Map menu = (Map)it.next();
        strID=(String)menu.get("navigate_code");
        String level_num = (String)menu.get("level_num");
        if(level_num.equals("0")){
        	name=(String)menu.get("navigate_name");
  %>
 <tr>
      <td background="photo/bt.gif" align="middle" height=25 noWrap
   onClick=javascript:show_submenu(<%=topI+1%>);
   style="BORDER-BOTTOM: rgb(12,12,12) 1px solid; BORDER-LEFT: rgb(253,244,249) 0px solid; BORDER-RIGHT: rgb(12,12,12) 1px solid; BORDER-TOP: rgb(253,244,249) 0px solid; CURSOR: hand"
   width="100%"><font style="FONT-SIZE: 9pt"><%=name%></font></td>
 </tr>
 <tr>
   <td height="100%" id=td_menu<%=topI+1%>>
     <div id=menu<%=topI+1%> style="DISPLAY: none">
       <table border=0 cellpadding=0 cellspacing=0 height="100%" width="100%">
         <tbody>
         <tr>
           <td background="image/bg.gif" valign="top">
               <h1><iframe frameborder=0 height="100%" src="include/i_menu.jsp?strID=<%=strID%>" width="100%"></iframe></h1>
           </td>
         </tr>
         </tbody>
       </table>
     </div>
   </td>
 </tr>
 <%topI+=1; 
 }
 
 }%>

 
 </tbody>
 <script language="javascript">
  var max_id=<%=topI%>;
</script>
</table>
<script language="javascript">
menu0.style.display='block';
td_menu0.height='100%';
</script>

</body>
</html>

<%out.flush();%>