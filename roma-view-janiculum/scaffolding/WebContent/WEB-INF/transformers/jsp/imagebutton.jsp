<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
			
	if(!("raw".equals(part) || "label".equals(part))){
    	if(JaniculumWrapper.content(component)!=null){%>
        <div class="<%=JaniculumWrapper.cssClass(component, "imagebutton", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
        
            <img id="<%=JaniculumWrapper.id(component, "content")%>" src="<%=JaniculumWrapper.contextPath()%>/image.png?imagePojo=<%=JaniculumWrapper.imageId(component)%>"
            <%for(String event:JaniculumWrapper.availableEvents(component)){ %> 
            on<%=event%>="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
            <%} %>
            />
    
        </div>
    <% }
}

if("label".equals(part)){%>
    <label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "imagebutton", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<% }%>
