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

<div class="<%=JaniculumWrapper.cssClass(component, "datetime", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">

<%if(!JaniculumWrapper.isValid(component)){%>
	<input id="<%=JaniculumWrapper.id(component, "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>" 
	value="<%=JaniculumWrapper.formatDateContent(component)%>" <%=JaniculumWrapper.disabled(component)?" disabled='disabled'":""%>
	<%
	boolean existsChangeEvent=false;
	for(String event: JaniculumWrapper.availableEvents(component)){
		if(!"change".equals(event)){
	%>
			on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
	<%	}else{
		existsChangeEvent = true;
		}
	} 
	if(existsChangeEvent){
	%>
	
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'change')"
	<%}else{ %>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
	<%} %> 
		/>
	<span class="<%=JaniculumWrapper.cssClass(component,  "datetime", "validation_message")%></span>"></span>
<%}else{ %>
<input id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "datetime", "content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>" 
value="<%=JaniculumWrapper.formatDateContent(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
<%
	boolean existsChangeEvent=false;
	for(String event: JaniculumWrapper.availableEvents(component)){
		if(!"change".equals(event)){
%> 
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		
<%
		}else{
			existsChangeEvent = true;
		}
	}
	if(existsChangeEvent){
%>
		
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'change')"
<%
	}else{
%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
<%
	}
%>
		 />
		 
<input id="<%=JaniculumWrapper.id(component, "time")%>" class="<%=JaniculumWrapper.cssClass(component, "datetime","content")%>" type="text" name="<%=JaniculumWrapper.fieldName(component)%>_time" 
value="<%=JaniculumWrapper.formatDateContent(component, "HH:mm:ss")%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
<%
	existsChangeEvent=false;
	for(String event:JaniculumWrapper.availableEvents(component)){
		if(!"change".equals(event)){
%> 
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>_time', '<%=event%>')"
<%
		}else{
			existsChangeEvent=true;
		}
	} 
	if(existsChangeEvent){
%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>_time', 'change')"
<%
	}else{
%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>_time')"
<%
	}
%> 
		/>
<%}

%>
<roma:addjs>
jQuery('#<%=JaniculumWrapper.id(component, "content") %>').datepicker({ dateFormat: 'dd/mm/yy' });
jQuery('#<%=JaniculumWrapper.id(component, "time") %>').timeEntry({spinnerImage: '<%=JaniculumWrapper.contextPath() %>/static/themes/default/image/spinnerDefault.png', show24Hours: true, showSeconds: true});
</roma:addjs>
</div>