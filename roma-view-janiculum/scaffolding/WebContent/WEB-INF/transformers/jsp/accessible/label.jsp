<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	if(!("raw".equals(part) || "label".equals(part))){%>

<div class="<%=JaniculumWrapper.cssClass(component, "label", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>" >
	 <span id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "label", "content")%>" >
    	<%=JaniculumWrapper.formattedContent(component)==null?"":JaniculumWrapper.formattedContent(component)%>
	 </span>
</div>
<% 
	}
if("raw".equals(part)){
	%><%=JaniculumWrapper.formattedContent(component)==null?"":JaniculumWrapper.formattedContent(component)%><%
}
if("label".equals(part)){%>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "label", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%} %>
