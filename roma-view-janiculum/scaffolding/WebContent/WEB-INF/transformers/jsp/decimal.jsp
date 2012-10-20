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
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
%>
<%if (!("raw".equals(part) || "label".equals(part))){   %>
	<div class="<%=JaniculumWrapper.cssClass(component, "decimal", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
		<input id="<%=JaniculumWrapper.id(component, "content")%>" type="text" 
		name="<%=JaniculumWrapper.fieldName(component)%>" value="<%=JaniculumWrapper.formatNumberContent(component)==null?"":JaniculumWrapper.formatNumberContent(component)%>" <%=JaniculumWrapper.disabled(component)?" disabled=\"disabled\"":""%> 
		<%
		boolean existsChangeEvent=false;
		for(String event:JaniculumWrapper.availableEvents(component)){
			if("change".equals(event)){
				existsChangeEvent=true;
			}
		%>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		<%
		}
			if(!existsChangeEvent) {
			%> onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"<%
			}
		 %> />
		
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "decimal", "validation_message")%>"></span>	
		<%} %>
	</div>
<%}else if("raw".equals(part)){%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%}
else if("label".equals(part)){ %>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "decimal", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%}
%>
<roma:addjs>
jQuery('#<%= JaniculumWrapper.id(component, "content")%>').keyup(function(component){
	re = /[^0-9\\.,]/ ;
	re2 = /[^0-9\\-]/ ;
	var beginning = jQuery(this).attr("value").substring(0,1);
	while(beginning.match(re2)){
		beginning = beginning.replace(re2, "");
	}
	var end = jQuery(this).attr("value").substring(1);
	while(end.match(re)){
		end = end.replace(re, "");
	}
	jQuery(this).attr("value", beginning+end);
}).keyup();
</roma:addjs>