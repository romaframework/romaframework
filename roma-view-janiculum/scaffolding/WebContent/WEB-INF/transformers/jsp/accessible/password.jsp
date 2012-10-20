<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
	if(!("raw".equals(part)||"label".equals(part))){
	%><div class="<%=JaniculumWrapper.cssClass(component, "password", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
		<input id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "password", "content")%>" style="<%=!JaniculumWrapper.isValid(component)?"border-color:red;":""%><%=JaniculumWrapper.inlineStyle(component, null)%>" type="password" name="<%=JaniculumWrapper.fieldName(component)%>" value="<%=JaniculumWrapper.content(component)==null?"":JaniculumWrapper.content(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%> />
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "password", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span>	
		<%} %>
	</div>
<%	} 
	if("raw".equals(part)){
		%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%
	} 
	if("label".equals(part)){%>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "password", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%	} %>