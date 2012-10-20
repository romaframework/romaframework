<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%><%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

	if (!("raw".equals(part) || "label".equals(part))){   %>
	<div class="<%=JaniculumWrapper.cssClass(component, "richtext", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
		<textarea id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "richtext", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" 
		name="<%=JaniculumWrapper.fieldName(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%></textarea> 
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "richtext", "validation_message")%>"></span>	
		<%} %>
	</div>
<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%} 
if("label".equals(part)){
%>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "richtext", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%} 
%>