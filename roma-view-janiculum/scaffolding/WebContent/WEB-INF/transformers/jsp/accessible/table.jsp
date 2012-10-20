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
	%>

    <div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "table", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
        <table id="<%=JaniculumWrapper.id(component, "content")%>"  class="<%=JaniculumWrapper.cssClass(component, "table", "content")%>">
            <thead id="<%=JaniculumWrapper.id(component, "header")%>" class="<%=JaniculumWrapper.cssClass(component, "table", "header")%>">
                <tr class="<%=JaniculumWrapper.cssClass(component, "table", "header_row")%>">
                    <%if(JaniculumWrapper.selectionAviable(component)){
                        if(JaniculumWrapper.isMultiSelection(component)){ %>
                               <th class="table_selection"># <input id="<%=JaniculumWrapper.id(component, null)%>_table_selection" class="table_selection" name="<%=JaniculumWrapper.fieldName(component)%>_-1" type="hidden" 
                                value="1" /><label for="<%=JaniculumWrapper.id(component, null)%>_table_selection" style="display:none;">Table Selection</label></th>
                        <%}
                        if(JaniculumWrapper.isSingleSelection(component)){
                        %>		
                        		<th id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_-1"><span> 
                        		<label for="<%=JaniculumWrapper.id(component, null)%>_table_selection" style="display:none;">Table Selection</label>
                                <input id="<%=JaniculumWrapper.id(component, null)%>_table_selection" class="table_selection" name="<%=JaniculumWrapper.fieldName(component)%>" type="radio" 
                                value="<%=JaniculumWrapper.fieldName(component)%>_-1" /> 
                                </span></th>
                        <%}
                       }
                    for(String header: JaniculumWrapper.headers(component)){
                        %><th><%=header%></th>
                    <%} %>
                </tr>
            </thead>
            <tbody>
            <%
            int  rowIndex = 0;
            String evenOdd = "";
            for(Object r:JaniculumWrapper.getChildren(component)){
            	HtmlViewGenericComponent rows = (HtmlViewGenericComponent)r;
            	if(rowIndex % 2 == 0){
            		evenOdd ="even";
            	}else{
            		evenOdd ="odd";
            	}
            %>
    
                    <tr id="<%=JaniculumWrapper.id(component, "row")%>_<%=rowIndex%>" class="<%=JaniculumWrapper.tableRowCssClass(component, "table", rowIndex)%> <%=evenOdd%>">
                        <%if(JaniculumWrapper.selectionAviable(component)){%>
                            <td id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_<%=rowIndex%>" class="table_selection">
                                <span>
                                <%if(JaniculumWrapper.isMultiSelection(component)){%>
                                	<label for="<%=JaniculumWrapper.id(component, null)%>_table_selection_<%=rowIndex%>" style="display:none;">Table Selection for <%=rowIndex%> row</label>
                                    <input id="<%=JaniculumWrapper.id(component, null)%>_table_selection_<%=rowIndex%>" class="<%=JaniculumWrapper.cssClass(component, "table", "selection")%>" name="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>" 
                                    <%if(JaniculumWrapper.isSelected(component, rowIndex)){%> checked="checked" <%} %>  type="checkbox"  />
                                <%}
                                if(JaniculumWrapper.isSingleSelection(component)){
                                %>
                                	<label for="<%=JaniculumWrapper.id(component, null)%>_table_selection_<%=rowIndex%>" style="display:none;">Table Selection for <%=rowIndex%> row </label>
                                    <input id="<%=JaniculumWrapper.id(component, null)%>_table_selection_<%=rowIndex%>" class="<%=JaniculumWrapper.cssClass(component, "table", "selection")%>" name="<%=JaniculumWrapper.fieldName(component)%>" type="radio" 
                                    <%if(JaniculumWrapper.isSelected(component, rowIndex)){%> checked="checked" <%} %> value="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex %>" />
                                <%} %>
                                </span>
                            </td>
                        <%} 
                        for(HtmlViewGenericComponent column:rows.getChildrenFilled()){
                        %>
                        	<td class="<%=JaniculumWrapper.cssSpecificClass(column, "table", null)%>">
                            <%=JspTransformerHelper.raw(column) %>
                            </td>
                        <%} %>
                    </tr>
                    <%
                    	rowIndex++;
            }
                    %>
                    
            </tbody>
        </table>
        <%if(!JaniculumWrapper.disabled(component) && JaniculumWrapper.selectionAviable(component)){%>
            <table id="<%=JaniculumWrapper.id(component, "table_actions")%>">
                <tr>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "add")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.add.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "add_button")%>" class="table_actions_add"/>
                    </td>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "edit")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.edit.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "edit_button")%>" class="table_actions_edit"/>
                    </td>           
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "view")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.view.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "view_button")%>" class="table_actions_view"/>
                    </td>           
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "remove")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.remove.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "remove_button")%>" class="table_actions_remove" />
                    </td>           
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "up")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.up.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "up_button")%>" class="table_actions_up"/>
                    </td>
                    <td>
                        <input type="button" name="<%=JaniculumWrapper.event(component, "down")%>" value="<%=JaniculumWrapper.i18N(component, "DynaComponent.down.label")%>" 
                        id="<%=JaniculumWrapper.id(component, "down_button")%>" class="table_actions_down" />
                    </td>       
                </tr>       
            </table>
        <%} %>
    </div>
<%//} 
%>