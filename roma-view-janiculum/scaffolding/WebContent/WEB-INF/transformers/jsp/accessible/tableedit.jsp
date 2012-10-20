<%@page import="java.util.Collection"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewGenericComponent"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
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

	Collection<String> headers = JaniculumWrapper.headers(component);
	
	%>

    <div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "tableedit", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
        <table id="<%=JaniculumWrapper.id(component, "content")%>"  class="<%=JaniculumWrapper.cssClass(component, "tableedit", "content")%>">
            <thead id="<%=JaniculumWrapper.id(component, "header")%>" class="<%=JaniculumWrapper.cssClass(component, "tableedit", "header")%>">
                <tr class="<%=JaniculumWrapper.cssClass(component, "tableedit", "header_row")%>">
                    <%if(JaniculumWrapper.selectionAviable(component)){
                    	if(JaniculumWrapper.isMultiSelection(component)){
                    
                    %>	<th class="table_selection"># <input class="table_selection" name="<%=JaniculumWrapper.fieldName(component)%>_-1" type="hidden" 
                                value="1" /></th>
                        <%}
                    	if( JaniculumWrapper.isSingleSelection(component)){%>
                        
                                <th id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_-1"> 
                                <input class="table_selection" name="<%=JaniculumWrapper.fieldName(component)%>" type="radio" 
                                value="<%=JaniculumWrapper.fieldName(component)%>_-1" /> 
                                </th>
                        <%}
                    }
                    for(String header: headers){
                    
                    %>
                        <th><%=header%></th>
                    <%} %>
                </tr>
            </thead>
            <tbody>
            <%
            	int rowIndex = 0;
            for(Object r:JaniculumWrapper.getChildren(component)){
            	HtmlViewGenericComponent rows = (HtmlViewGenericComponent)r;
            
            %>
                    <tr id="<%=JaniculumWrapper.id(component, "row")%>_<%=rowIndex%>" class="<%=JaniculumWrapper.tableRowCssClass(component, "tableedit", rowIndex)%>">
                        <%if(JaniculumWrapper.selectionAviable(component)){%>
                            <td id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_<%=rowIndex%>" class="table_selection">
                                 
                                <%if(JaniculumWrapper.isMultiSelection(component)){%>
                                    <input class="<%=JaniculumWrapper.cssClass(component, "tableedit", "selection")%>" name="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>" 
                                    <%if(JaniculumWrapper.isSelected(component, rowIndex)){%> checked="checked" <%} %>  type="checkbox" />
                                <%}
                                if(JaniculumWrapper.isSingleSelection(component)){
                                %>
                                
                                    <input class="<%=JaniculumWrapper.cssClass(component, "tableedit", "selection")%>" name="<%=JaniculumWrapper.fieldName(component)%>" type="radio" 
                                    <%if(JaniculumWrapper.isSelected(component, rowIndex)){%> checked="checked" <%} %> value="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>" />
                                <%} %>
              
                             </td>
                        <%}
                        for(HtmlViewGenericComponent column:rows.getChildrenFilled()){
                        %>
                        
                            <td class="<%=JaniculumWrapper.cssSpecificClass(column, "tableedit", null)%>">
                            <% JspTransformerHelper.delegate(column, null,pageContext.getOut()); %>
                            </td>
                        <%} %>
                    </tr>
                    <%rowIndex++; 
                    }%>
            </tbody>
        </table>
        <%if(JaniculumWrapper.disabled(component) == false  && JaniculumWrapper.selectionAviable(component)){%>
            <table id="<%=JaniculumWrapper.id(component, "table_actions")%>">
                <tr>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "add")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.add.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "add_button")%>" class="table_actions_add" />
                    </td>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "remove")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.remove.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "remove_button")%>" class="table_actions_remove"/>
                    </td>           
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.action(component, "up")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.up.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "up_button")%>" class="table_actions_up"/>
                    </td>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.action(component, "down")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.down.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "down_button")%>" class="table_actions_down" />
                    </td>       
                </tr>   
            </table>    
        <%} %>
    </div>
<%
%>