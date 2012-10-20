<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%
	Map<String, Object> ctx = (Map<String, Object>) request.getAttribute(RequestConstants.CURRENT_CONTEXT_IN_TRANSFORMER);
	JaniculumWrapper janiculum = (JaniculumWrapper)ctx.get(JspTransformer.JANICULUM);
	pageContext.setAttribute("janiculum", janiculum);
	String part = (String) ctx.get(JspTransformer.PART_TO_PRINT);
	pageContext.setAttribute("part", part);

	if (!("label".equals(part) || "raw".equals(part) ) ){   %>
	<div class="<%=janiculum.cssClass(null)%>" style="<%=janiculum.inlineStyle(null)%>" id="<%=janiculum.id(null)%>">
		<p>
		<input id="<%=janiculum.id("content")%>" style="<%=janiculum.inlineStyle(null)%>" type="text" name="<%=janiculum.fieldName()%>" value="<%=janiculum.content(true)!""%>" 
		<%if(janiculum.disabled()){%> disabled="disabled"
		<%}
			boolean existsChangeEvent=false;
			for(String event: janiculum.availableEvents()){
				%> 
				on<%=event%>="romaFieldChanged('<%=janiculum.fieldName()%>'); romaEvent('<%=janiculum.fieldName()%>', '<%=event%>')"
				<%if("change".equals(event)){ 
					existsChangeEvent=true;
				}
			}
			if(!existsChangeEvent){
		%>
		onchange="romaFieldChanged('<%=janiculum.fieldName()%>')"
		onkeyup="romaAutoEvent('<%=janiculum.fieldName()%>', 'search')"
		<%	} %>
		 />
		<%if(!janiculum.isValid()){%>
			<span class="<%=janiculum.cssClass("validation_message")%>"><%=janiculum.validationMessage()%></span>	
		<%} %>
		<button type="submit">Go</button>
		</p>
	</div>
	
	<%
		JspTransformerHelper.addJs(janiculum.id(TransformerConstants.PART_ALL), "new searchfield('"+janiculum.id("content")+"');")
	}
	if("raw".equals(part)){
	%>

	<%=janiculum.content(true)!""%>
	<%} else if("label".equals(part)){
	%>
		<label id="<%=janiculum.id("label")%>" class="<%=janiculum.cssClass("label")%>" for="<%=janiculum.id("content")%>"><%=janiculum.i18NLabel()%></label>
	<% } %>