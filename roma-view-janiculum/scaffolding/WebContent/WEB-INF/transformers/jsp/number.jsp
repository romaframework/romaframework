<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
	if(!("raw".equals(part)||"label".equals(part))){
%>
	<div class="<%=JaniculumWrapper.cssClass(component, "number", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
	
	<input id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "number", null)%>" style="<%=JaniculumWrapper.isValid(component)?"":"border-color:red;"%><%=JaniculumWrapper.inlineStyle(component, null)%>" type="text" 
	name="<%=JaniculumWrapper.fieldName(component)%>" value="<%=JaniculumWrapper.formatNumberContent(component)==null?"":JaniculumWrapper.formatNumberContent(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
	<%
	boolean existsChangeEvent=false;
	for(String event: JaniculumWrapper.availableEvents(component)){
		%> on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		<%if("change".equals(event)){ 
			existsChangeEvent=true;
		}
	}
	if(!existsChangeEvent){
		%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
		<%} %>
		/>
	<%if(!JaniculumWrapper.isValid(component)){%>
		<span class="<%=JaniculumWrapper.cssClass(component, "number", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span>
	<%} %>
	
	</div>
<%}else if("raw".equals(part)){ %><%=JaniculumWrapper.formatNumberContent(component)==null?"":JaniculumWrapper.formatNumberContent(component)%><%
}else if("label".equals(part)){%>
<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "number", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%}%>