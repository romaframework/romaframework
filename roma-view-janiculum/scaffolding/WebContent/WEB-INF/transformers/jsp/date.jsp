<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
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

<div class="<%=JaniculumWrapper.cssClass(component, "date", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">

<input id="<%=JaniculumWrapper.id(component, "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>" 
value="<%=JaniculumWrapper.formatDateContent(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%> 
<%
	boolean existsChangeEvent = false;

	for(String event:JaniculumWrapper.availableEvents(component)){
	%>on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')" <%
        if("change".equals(event)){ 
        	existsChangeEvent=true;
        } %>
	<%} 
	if(!existsChangeEvent){
		%>onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')" <%
	} %> />
<%if(!JaniculumWrapper.isValid(component)){ %>
    <span class="<%=JaniculumWrapper.cssClass(component, "date", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span>
<%} %>
</div>
<roma:addjs>
jQuery('#<%=JaniculumWrapper.id(component, "content") %>').datepicker({ dateFormat: 'dd/mm/yy', yearRange: '1900:2050', changeYear: true, changeMonth: true });
</roma:addjs>