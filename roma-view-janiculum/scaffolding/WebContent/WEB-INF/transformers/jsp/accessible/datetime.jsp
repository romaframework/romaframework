<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Set"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
%>
<div class="<%=JaniculumWrapper.cssClass(component, "datetime", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<input id="<%=JaniculumWrapper.id(component, "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>" 
	value="<%=JaniculumWrapper.formatDateContent(component)%>" <%=JaniculumWrapper.disabled(component)?" disabled='disabled'":""%> />
<input id="<%=JaniculumWrapper.id(component, "time")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>_time" 
value="<%=JaniculumWrapper.formatDateContent(component, "HH:mm:ss")%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>/>

<%if(!JaniculumWrapper.isValid(component)){%>
	<span class="<%=JaniculumWrapper.cssClass(component,  "datetime", "validation_message")%></span>"></span>
<%}%>
</div>