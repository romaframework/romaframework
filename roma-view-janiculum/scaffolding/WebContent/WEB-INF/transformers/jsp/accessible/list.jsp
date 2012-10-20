<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "list", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<%
String selection = "single";
if(!JaniculumWrapper.isMultiSelection(component)){
	selection = "multiple";
}
%>
        <table>
        <tr>
        <td class="list_box">
        <select id="<%=JaniculumWrapper.id(component, "content")%>" name="<%=JaniculumWrapper.fieldName(component)%>" class="<%=JaniculumWrapper.cssClass(component, "list", "content")%>"  size="5" selection="<%=selection%>" >
        <%
        int rowIndex = 0;
        for(Object o:JaniculumWrapper.getChildren(component)){
        	HtmlViewRenderable opt = (HtmlViewRenderable) o;
        %>
                    <option id="<%=JaniculumWrapper.id(component, "item")%>_<%=rowIndex%>" value="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>"
                        <%=JaniculumWrapper.isSelected(component, rowIndex)?"selected=\"selected\"":""%>
                    >
                    <%=JspTransformerHelper.raw(opt) %>
                    </option>
            <%
            rowIndex++;
        }
            %>   
        </select>
        <%if(!JaniculumWrapper.isValid(component)){%>
          <span class="<%=JaniculumWrapper.cssClass(component, "list", "validation_message")%>"><%=JaniculumWrapper.validationMessage(component)==null?"Invalid":JaniculumWrapper.validationMessage(component)%></span> 
        <% } %>
        </td>
    <%if(JaniculumWrapper.disabled(component) == false && JaniculumWrapper.selectionAviable(component)){%>
        <td>
        <table id="<%=JaniculumWrapper.id(component, "list_actions")%>">
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "add")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "add_button")%>" class="table_actions_add" />
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "edit")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "edit_button")%>" class="table_actions_edit" />
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "view")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "view_button")%>" class="table_actions_view" />
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "remove")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "remove_button")%>" class="table_actions_remove"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "up")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "up_button")%>" class="table_actions_up" />
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" name="<%=JaniculumWrapper.event(component, "down")%>" value="" 
                    id="<%=JaniculumWrapper.id(component, "down_button")%>" class="table_actions_down"/>
                </td>       
            </tr>       
        </table>
        </td>
        </tr>   
    <%} %>
    </table>
</div>