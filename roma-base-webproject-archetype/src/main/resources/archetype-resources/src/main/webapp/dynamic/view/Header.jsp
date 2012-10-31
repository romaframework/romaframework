#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@ page buffer="none"%>
<%@page import="org.romaframework.core.*"%>
<%@page import="org.romaframework.core.schema.SchemaHelper"%>
<%@page import="org.romaframework.aspect.session.*"%>
<%@page import="org.romaframework.aspect.i18n.*"%>
<%@page import="org.romaframework.core.config.ApplicationConfiguration"%>
<%@page import="org.romaframework.aspect.authentication.AuthenticationAspect"%>
<%
    String appName = Utility.getCapitalizedString(Roma.component(ApplicationConfiguration.class).getApplicationName());
    SessionInfo sess = Roma.session().getActiveSessionInfo();
%>
<table style="width:100%"" style="border-bottom: 1px solid ${symbol_pound}9DBBC6;" class="class_Header">
	<tr>
		<td align="left"
			style="width: 150px;height: 75px; background-image: url(<%=request.getContextPath()%>/static/themes/default/image/logo.jpg);background-repeat: no-repeat;background-color: rgb(255, 255, 255);background-position: 20px 5px; border-color: rgb(255, 255, 255);border: 0;"></td>
		<td>
		<h1><%=appName%></h1>
		</td>
		<td>
		<table>
			<tr>
				<td>User:</td>
				<td><%=sess.getAccount()%></td>
			</tr>
			<tr>
				<td>Logged On:</td>
				<td><%=Roma.i18n().getDateTimeFormat().format(sess.getCreated())%></td>
			</tr>
		</table>
		</td>
		<td align="center" style="width: 100px;">
			<roma:action name="home" />
		</td>
		<td align="center" style="width: 100px;">
			<roma:action name="changePassword" /> 
		</td>
		<td align="center" style="width: 100px;">
			<roma:action name="controlPanel" /> 
		</td>
		<td align="center" style="width: 100px;">
			<a href="<%=request.getContextPath()%>/dynamic/logout.jsp" title="logout">
			<img src="<%=request.getContextPath()%>/static/themes/default/image/logout.png"
			style="border:0;" class="img_logout" alt="Logout" /> 
			</a> 
		</td>
	</tr>
</table>

