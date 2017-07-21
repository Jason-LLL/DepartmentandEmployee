<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
<table border="0" width="600px">
<tr>
<td align="center" style="font-size:24px; color:#666"> 员工添加</td>
</tr>
<tr>
<td align="right" > 
<a href="javascript:document.getElementById('saveForm2').submit()">保存</a> &nbsp;&nbsp;
<a href="javascript:history.go(-1)">退回</a>
</td>
</tr>
</table>
<br/>

<s:form id="saveForm2"  action="employee_save" method="post" namespace="/" theme="simple">
<table border='0' cellpadding="0"  cellspacing="10">
<tr>
<td>姓名：</td>
<td><s:textfield name="ename"/> </td>
<td>性别：</td>
<td><s:radio   name="sex"  list="{'男','女'}"/></td>
</tr>
<tr>
<td>用户名：</td>
<td><s:textfield name="username"/> </td>
<td>密码：</td>
<td><s:password name="password"/></td>
</tr>
<tr>
<td>出生日期：</td>
<td><s:textfield name="birthday" /></td>
<td>入职时间：</td>
<td><s:textfield  name="joinDate"/></td>
</tr>

<tr>
<td>所属部门：</td>
<td>
<s:select name="department.did" list="list" listKey="did" listValue="dname" headerKey="" headerValue="--请选择--">
</s:select>
</td>
<td>编号：</td>
<td><s:textfield name="eno"/></td>
</tr>
</table>
</s:form>

</body>
</html>
