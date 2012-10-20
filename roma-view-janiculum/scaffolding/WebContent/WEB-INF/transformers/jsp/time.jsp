<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	
%>
<div class="<%=JaniculumWrapper.cssClass(component, "time", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">	
	<%
	boolean existsChangeEvent = true;
	if( !JaniculumWrapper.isValid(component)){%>
		<input id="<%=JaniculumWrapper.id(component, "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>_time" value="<%=JaniculumWrapper.formatDateContent(component)%>" <%if( JaniculumWrapper.disabled(component)){%> disabled="disabled" <%}%> 
		<%
		existsChangeEvent = false;
		for(String event: JaniculumWrapper.availableEvents(component)){
		%>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>_time', '<%=event%>')"
		<%if("change".equals(event)){
			existsChangeEvent = true;
		}%>
		<%}%>
		<%if(!existsChangeEvent){%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time');"
		<%}%>
		/>
		<span class="<%=JaniculumWrapper.cssClass(component, "time", "validation_message")%>"></span>
	<%}else{%>	
	<input id="<%=JaniculumWrapper.id(component, "time")%>" class="<%=JaniculumWrapper.cssClass(component, "time", "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>_time" value="<%=JaniculumWrapper.formatDateContent(component, "HH:MM:ss")%>" <%if( JaniculumWrapper.disabled(component)){%> disabled="disabled" <%}%> 
	<%
		existsChangeEvent = false;
	for(String event: JaniculumWrapper.availableEvents(component) ){
	%>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>_time', '<%=event%>')"
		<%if( "change".equals(event)){
			existsChangeEvent=true;
		}}%>
		<%if(!existsChangeEvent){%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time');"
		<%}%>
	/>
	<%}%>
</div>

<roma:addjs>
jQuery('#<%=JaniculumWrapper.id(component, "time")%>').timeEntry({spinnerImage: 'static/themes/default/image/spinnerDefault.png', show24Hours: true, showSeconds: true});
</roma:addjs>
