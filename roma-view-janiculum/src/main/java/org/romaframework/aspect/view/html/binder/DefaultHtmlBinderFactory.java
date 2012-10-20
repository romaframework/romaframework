package org.romaframework.aspect.view.html.binder;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.ViewHtmlBinderFactory;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.type.Stream;
import org.romaframework.core.domain.type.TreeNode;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class DefaultHtmlBinderFactory implements ViewHtmlBinderFactory {

	private static Log									log										= LogFactory.getLog(DefaultHtmlBinderFactory.class);

	private Map<String, HtmlViewBinder>	binders;

	private TextBinder									textBinder						= new TextBinder();
	private NumberBinder								numberBinder					= new NumberBinder();
	private SingleSelectionBinder				singleSelectionBinder	= new SingleSelectionBinder();
	private MultiSelectionBinder				multiSelectionBinder	= new MultiSelectionBinder();
	private UploadBinder								uploadBinder					= new UploadBinder();
	private CheckBinder									checkBinder						= new CheckBinder();
	private DateBinder									dateBinder						= new DateBinder();
	private SearchResultBinder					searchResultBinder		= new SearchResultBinder();

	private HtmlViewBinder							treeNodeBinder				= new TreeNodeBinder();

	public Map<String, HtmlViewBinder> getBinders() {
		return binders;
	}

	public void setBinders(final Map<String, HtmlViewBinder> binders) {
		this.binders = binders;
	}

	public HtmlViewBinder getBinder(final HtmlViewRenderable renderable) {
		if (renderable instanceof HtmlViewContentComponent) {
			SchemaClassElement element = ((HtmlViewGenericComponent) renderable).getSchemaElement();
			if (element instanceof SchemaField)
				return getBinderBySchemaField((SchemaField) element);
		}

		return NullBinder.getInstance();

	}

	private HtmlViewBinder getBinderBySchemaField(SchemaField element) {
		// TODO:Verify to user element.getClassInfo();
		HtmlViewBinder result = null;
		SchemaClassDefinition elementType = element.getType();
		if (elementType != null) {
			SchemaClass type = elementType.getSchemaClass();
			if (type.isAssignableAs(Roma.schema().getSchemaClass(String.class))) {
				result = textBinder;
			} else if (type.isAssignableAs(Roma.schema().getSchemaClass(Boolean.class))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Boolean.TYPE))) {
				result = checkBinder;
			} else if (type.isAssignableAs(Roma.schema().getSchemaClass(Date.class))) {
				result = dateBinder;
			} else if (type.isAssignableAs(Roma.schema().getSchemaClass(Number.class))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Integer.TYPE))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Long.TYPE))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Float.TYPE))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Double.TYPE))
					|| type.isAssignableAs(Roma.schema().getSchemaClass(Short.TYPE))) {
				result = numberBinder;
			} else if (type.isAssignableAs(Roma.schema().getSchemaClass(Stream.class))) {
				result = uploadBinder;
			} else if (type.isAssignableAs(Roma.schema().getSchemaClass(TreeNode.class))) {
				result = treeNodeBinder;
			}
		}
		if (result == null && SchemaHelper.isMultiValueObject(element)) {
			String selectionFieldName = (String) element.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			if (selectionFieldName != null) {
				SchemaField selectionSchemaField = element.getEntity().getField(selectionFieldName);
				if (selectionSchemaField == null) {
					log.error("Invalid selection field (" + selectionFieldName + ") for field '" + element.toString() + "'");
					result = NullBinder.getInstance();
				} else if (SchemaHelper.isMultiValueObject(selectionSchemaField)
						&& !element.getEmbeddedType().equals(selectionSchemaField.getType().getSchemaClass())) {
					result = multiSelectionBinder;
				} else {
					result = singleSelectionBinder;
				}
			}
		}

		if (result == null) {
			result = searchResultBinder;// NullBinder.getInstance();
		}
		return result;
	}

}
