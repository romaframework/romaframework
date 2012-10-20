<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="java.util.Collection"%>
<%@page
	import="org.romaframework.aspect.view.html.component.HtmlViewGenericComponent"%>
<%@page
	import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page
	import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page
	import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page
	import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page
	import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page
	import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page
	import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%
	HtmlViewRenderable component = (HtmlViewRenderable) request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);

	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
%>

<div id="<%=JaniculumWrapper.id(component, null)%>"
	class="<%=JaniculumWrapper.cssClass(component, "table", null)%>"
	style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
	<div>
		<div>
			<table id="<%=JaniculumWrapper.id(component, "content")%>"
				class="<%=JaniculumWrapper.cssClass(component, "table", "content")%>">
				<thead id="<%=JaniculumWrapper.id(component, "header")%>"
					class="<%=JaniculumWrapper.cssClass(component, "table", "header")%>">
					<tr
						class="<%=JaniculumWrapper.cssClass(component, "table", "header_row")%>">
						<%
							if (JaniculumWrapper.selectionAviable(component)) {
								if (JaniculumWrapper.isMultiSelection(component)) {
						%>
						<th class="table_selection"># <input class="table_selection"
							name="<%=JaniculumWrapper.fieldName(component)%>_-1"
							type="hidden" value="1" /></th>
						<%
							}
								if (JaniculumWrapper.isSingleSelection(component)) {
						%>
						<th id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_-1"><span>
								<input class="table_selection"
								name="<%=JaniculumWrapper.fieldName(component)%>" type="radio"
								value="<%=JaniculumWrapper.fieldName(component)%>_-1"
								onclick="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaSendAjaxRequest()" />
						</span></th>
						<%
							}
							}
							for (String header : JaniculumWrapper.headers(component)) {
						%><th><%=header%></th>
						<%
							}
						%>
					</tr>
				</thead>
				<tbody>
					<%
						int rowIndex = 0;
						String evenOdd = "";
						for (Object r : JaniculumWrapper.getChildren(component)) {
							HtmlViewGenericComponent rows = (HtmlViewGenericComponent) r;
							if (rowIndex % 2 == 0) {
								evenOdd = "even";
							} else {
								evenOdd = "odd";
							}
					%>

					<tr id="<%=JaniculumWrapper.id(component, "row")%>_<%=rowIndex%>"
						class="<%=JaniculumWrapper.tableRowCssClass(component, "table", rowIndex)%> <%=evenOdd%>">
						<%
							if (JaniculumWrapper.selectionAviable(component)) {
						%>
						<td
							id="<%=JaniculumWrapper.id(component, "selectionCheck")%>_<%=rowIndex%>"
							class="table_selection"><span> <%
 	if (JaniculumWrapper.isMultiSelection(component)) {
 %>
								<input
								class="<%=JaniculumWrapper.cssClass(component, "table", "selection")%>"
								name="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>"
								<%if (JaniculumWrapper.isSelected(component, rowIndex)) {%>
								checked="checked" <%}%> type="checkbox"
								onclick="romaMultiSelectChanged('<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>'); romaSendAjaxRequest()" />
								<%
									}
											if (JaniculumWrapper.isSingleSelection(component)) {
								%> <input
								class="<%=JaniculumWrapper.cssClass(component, "table", "selection")%>"
								name="<%=JaniculumWrapper.fieldName(component)%>" type="radio"
								<%if (JaniculumWrapper.isSelected(component, rowIndex)) {%>
								checked="checked" <%}%>
								value="<%=JaniculumWrapper.fieldName(component)%>_<%=rowIndex%>"
								onclick="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaSendAjaxRequest()" />
								<%
									}
								%>
						</span></td>
						<%
							}
								for (HtmlViewGenericComponent column : rows.getChildrenFilled()) {
						%>

						<td
							class="<%=JaniculumWrapper.cssSpecificClass(column, "table", null)%>">
							<%=JspTransformerHelper.raw(column)%>
						</td>
						<%
							}
						%>
					</tr>
					<%
						rowIndex++;
						}
					%>

				</tbody>
			</table> <%
 	if (!JaniculumWrapper.disabled(component) && JaniculumWrapper.selectionAviable(component)) {
 %>
		</div>
		<div><button
			name="<%=JaniculumWrapper.event(component, "add")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.add.label")%>"
			id="<%=JaniculumWrapper.id(component, "add_button")%>"
			class="table_actions_add"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'add')" ></button>
			<button type="button"
			name="<%=JaniculumWrapper.event(component, "edit")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.edit.label")%>"
			id="<%=JaniculumWrapper.id(component, "edit_button")%>"
			class="table_actions_edit"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'edit')" ></button>
			<button type="button"
			name="<%=JaniculumWrapper.event(component, "view")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.view.label")%>"
			id="<%=JaniculumWrapper.id(component, "view_button")%>"
			class="table_actions_view"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'view')" ></button>
			<button type="button"
			name="<%=JaniculumWrapper.event(component, "remove")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.remove.label")%>"
			id="<%=JaniculumWrapper.id(component, "remove_button")%>"
			class="table_actions_remove"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'remove')" ></button>
			<button type="button"
			name="<%=JaniculumWrapper.action(component, "up")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.up.label")%>"
			id="<%=JaniculumWrapper.id(component, "up_button")%>"
			class="table_actions_up"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'up')" ></button>
			<button type="button"
			name="<%=JaniculumWrapper.action(component, "down")%>"
			value="<%=JaniculumWrapper.i18N(component, "Object.down.label")%>"
			id="<%=JaniculumWrapper.id(component, "down_button")%>"
			class="table_actions_down"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'down')" >
			</button>
			<%
				}
			%></div>
	</div>
</div>
<roma:addjs>
$('#<%=JaniculumWrapper.id(component, "content")%>').tablesorter({ headers: { 0: { sorter: false } } });
</roma:addjs>
