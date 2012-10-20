<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

	if (part==null || "".equals(part) || "all".equals(part)|| "content".equals(part)){   %>

	<div class="<%=JaniculumWrapper.cssClass(component, "select", null)%>" id="<%=JaniculumWrapper.id(component, null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
			<%if(JaniculumWrapper.getChildren(component)!=null){%>
				<label for="<%=JaniculumWrapper.id(component, "select")%>" style="display: none;"><%=JaniculumWrapper.i18NLabel(component)%></label>
				<select id="<%=JaniculumWrapper.id(component, "select")%>"
				name="<%=JaniculumWrapper.fieldName(component)%>"
				<%if(JaniculumWrapper.disabled(component)){%> disabled="disabled" <%}%>
				style="<%if(!JaniculumWrapper.isValid(component)){%>border-color:red;<%} %>"
				>
				
				<option value="<%=JaniculumWrapper.fieldName(component)%>_-1" ></option>
				<%
				int i = 0;
				boolean selected = false;
				for(Object o:JaniculumWrapper.getChildren(component)){
					HtmlViewRenderable opt = (HtmlViewRenderable)o;
				
				%>
				
					<option value="<%=JaniculumWrapper.fieldName(component)%>_<%=i%>" <%if(JaniculumWrapper.isSelected(component, i)){%>	selected="selected"	<%} %>> 
					 <%=JspTransformerHelper.raw(opt) %>
					</option>	
					
				<%
					i++;
				} %>
				</select>
				
				<%if(!JaniculumWrapper.isValid(component) ){ %>
                    <span class="<%=JaniculumWrapper.cssClass(component, "select", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span> 
                <%} 
                }%>
	</div>
<%} 
if ("label".equals(part) ){   %>

	<%=JaniculumWrapper.i18NLabel(component)%>
<%} %>
