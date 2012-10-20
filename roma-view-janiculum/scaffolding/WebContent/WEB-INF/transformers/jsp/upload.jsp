<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	
%>
<div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "upload", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
	<form id="<%=JaniculumWrapper.id(component, "form")%>" action="<%=JaniculumWrapper.contextPath()%>/fileUpload" sent="0" method="post" enctype="multipart/form-data" target="<%=JaniculumWrapper.id(component, "iframe")%>">
		<input name="<%=JaniculumWrapper.fieldName(component)%>" type="file" onchange="jQuery('#<%=JaniculumWrapper.id(component, "form")%>').attr('sent', 1); submit()"/>
	</form>
	<iframe name="<%=JaniculumWrapper.id(component, "iframe")%>" width="0" height="0" frameborder="0" onload="if(jQuery('#<%=JaniculumWrapper.id(component, "form")%>').attr('sent') == '1'){jQuery('#<%=JaniculumWrapper.id(component, "form")%>').attr('sent', 0); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'upload')}"></iframe>
</div>