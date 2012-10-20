<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "link", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<% if(JaniculumWrapper.isField(component)){%>
<a id="<%=JaniculumWrapper.id(component, "content")%>" value="<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>" href="javascript:void(component, 0)"
<%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":"" %>
<%
for(String event:JaniculumWrapper.availableEvents(component)){
	%>on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"<%
} %>
>
<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>
</a>
<%} else if(JaniculumWrapper.isAction(component)){%>

<a id="<%=JaniculumWrapper.id(component, "content")%>" value="<%=JaniculumWrapper.i18NLabel(component)%>" href="javascript:void(0)" title="<%=JaniculumWrapper.i18NHint(component)%>"
<%=JaniculumWrapper.disabled(component)? "disabled=\"disabled\"":""%>
onclick="romaAction('<%=JaniculumWrapper.actionName(component)%>')" >
<%=JaniculumWrapper.i18NLabel(component)%>
</a><%} %>
</div>