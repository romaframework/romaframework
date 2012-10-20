<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.core.schema.SchemaField"%>
<%@page import="org.romaframework.aspect.validation.feature.ValidationFieldFeatures"%>
<%@page import="org.romaframework.aspect.view.feature.ViewFieldFeatures"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewContentComponent"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	
	if(!("raw".equals(part)||"label".equals(part))){
%>
	<div class="<%=JaniculumWrapper.cssClass(component, "textarea", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
		<textarea id="<%=JaniculumWrapper.id(component, "content")%>" style="<%if(!JaniculumWrapper.isValid(component)){%>border-color:red;<%}%><%=JaniculumWrapper.inlineStyle(component, null)%>" name="<%=JaniculumWrapper.fieldName(component)%>" <%if(JaniculumWrapper.disabled(component)){%> disabled="disabled" <%} %> 
		<% 
			boolean existsChangeEvent=false;
			for(String event: JaniculumWrapper.availableEvents(component)){
				%>
				on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
				<%if("change".equals(event)){	
					existsChangeEvent = true;
				}
			}
			if(!existsChangeEvent){
				%> onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
		<%} %>
		><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%></textarea>
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "textarea", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span>
		<%} %>
	
	</div>
	<%
	SchemaField field = ((HtmlViewContentComponent)component).getSchemaField();
	if(field.isSettedFeature(ValidationFieldFeatures.MAX)){
		Integer max = ((HtmlViewContentComponent)component).getSchemaField().getFeature(ValidationFieldFeatures.MAX);
		if(max!=null && max>0){
			%>
			<roma:addjs>
			jQuery('#<%=JaniculumWrapper.id(component, "content") %>').keyup(function(){
			    value = ""+jQuery('#<%=JaniculumWrapper.id(component, "content") %>').val();
			    if(value.length> <%=max %>){
			        jQuery('#<%=JaniculumWrapper.id(component, "content") %>').val(value.substring(0, <%=max %>));
			        romaFieldChanged('${janiculum.fieldName()}');
			    } 
			});
			</roma:addjs>
			<%
		}
	}
%>
	
<%}
	if("raw".equals(part)){
%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%
	}else if("label".equals(part)){
		%><label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "textarea", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label><%
	}%>