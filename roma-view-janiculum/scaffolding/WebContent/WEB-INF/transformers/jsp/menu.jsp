<%@page import="org.romaframework.aspect.view.html.component.HtmlViewConfigurableEntityForm"%>
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.core.schema.SchemaField"%>
<%@page import="org.romaframework.core.schema.SchemaAction"%>
<%@page import="org.romaframework.aspect.view.feature.ViewClassFeatures"%>
<%@page import="org.romaframework.aspect.view.feature.ViewFieldFeatures"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewContentComponent"%>
<%@page import="org.romaframework.aspect.view.feature.ViewActionFeatures"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewActionComponent"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "menu", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>" inited="false">
	<%if(JaniculumWrapper.isAction(component)){%>
		<a  id="<%=JaniculumWrapper.id(component, "content")%>" value="<%=JaniculumWrapper.i18NLabel(component)%>" href="javascript:void(0)" title="<%=JaniculumWrapper.i18NHint(component)%>"
		<%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
			onclick="romaAction('<%=JaniculumWrapper.actionName(component)%>')">
			<label for="<%=JaniculumWrapper.id(component, "content")%>" id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "menu", "label")%>">
			<%=JaniculumWrapper.i18NLabel(component)%>
			</label> 
		</a>
	<%} else if(JaniculumWrapper.isField(component)){ %>
			<label for="<%=JaniculumWrapper.id(component, "content")%>" id="<%=JaniculumWrapper.id(component, "label")%>" >
			<%=JaniculumWrapper.i18NLabel(component)%>
			</label> 
	<%} %>
	
	<%if(JaniculumWrapper.haveChildren(component)){%>
	<ul id="<%=JaniculumWrapper.id(component, "content_children")%>">
		<%
		for(Object child:JaniculumWrapper.getChildren(component)){
			if(child instanceof HtmlViewActionComponent || child instanceof HtmlViewContentComponent) {
			if(child instanceof HtmlViewActionComponent){
				SchemaAction action =((HtmlViewActionComponent)child).getActionField(); 
				action.setFeature(ViewActionFeatures.RENDER, "vmenu");
			}else if(child instanceof HtmlViewContentComponent) {
				if(child instanceof HtmlViewConfigurableEntityForm){
					((HtmlViewContentComponent)child).getSchemaObject().setFeature(ViewClassFeatures.RENDER, "vmenu");
				}else if(((HtmlViewContentComponent)child).getSchemaField()!= null){
					SchemaField sf =((HtmlViewContentComponent)child).getSchemaField();
					sf.setFeature(ViewFieldFeatures.RENDER, "vmenu");
				}
			}
		%>
			<li >
					<% JspTransformerHelper.delegate((HtmlViewRenderable)child, null,pageContext.getOut()); %>							
			</li>			
		<%
			}
		} %>
	</ul>
	<%} %>
</div>
<%
boolean isRoot = !JaniculumWrapper.isField(component)&&!JaniculumWrapper.isAction(component);
if(!isRoot){ %>
<roma:addjs>
$('#<%=JaniculumWrapper.id(component,null)%>').mouseenter(function (){
	$('#<%=JaniculumWrapper.id(component, "content_children")%>').css("display","block");
});
$('#<%=JaniculumWrapper.id(component,null)%>').mouseleave(function (){
	$('#<%=JaniculumWrapper.id(component, "content_children")%>').css("display","none");
});
</roma:addjs>
<%} %>