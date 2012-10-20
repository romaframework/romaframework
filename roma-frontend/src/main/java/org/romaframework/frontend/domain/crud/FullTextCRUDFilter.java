package org.romaframework.frontend.domain.crud;

import java.util.Iterator;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItemGroup;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

@SuppressWarnings("unchecked")
public class FullTextCRUDFilter<T> extends CRUDFilter<T> implements ViewCallback {

	@ViewField(label = "Cerca")
	protected String				fullTextSearch;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected boolean				expanded	= false;

	protected T							mockEntity;

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	protected CRUDFilter<T>	advancedFilter;

	public FullTextCRUDFilter(T iEntity) {
		super(iEntity);
		if (iEntity != null) {
			try {
				mockEntity = (T) iEntity.getClass().newInstance();
			} catch (Exception e) {
			}
		}
		try {
			Class<?> filterEntity = iEntity.getClass();
			while (!filterEntity.equals(Object.class)) {
				SchemaClass filterClass = CRUDHelper.getCRUDFilter(filterEntity);
				if (filterClass != null) {
					advancedFilter = (CRUDFilter<T>) filterClass.newInstance();
					advancedFilter.setEntity(iEntity);
					break;
				}
				filterEntity = filterEntity.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@ViewField(visible = AnnotationConstants.FALSE)
	public T getEntity() {
		if (!isExpanded() && mockEntity != null) {
			return mockEntity;
		}
		return advancedFilter.getEntity();
	}

	public void setEntity(T iEntity) {
		try {
			mockEntity = (T) iEntity.getClass().newInstance();
		} catch (Exception e) {
		}
		advancedFilter.setEntity(iEntity);
	}

	@Override
	protected QueryByFilter getAdditionalFilter() {
		if (expanded) {
			return advancedFilter.getAdditionalFilter();
		}
		if (fullTextSearch == null || fullTextSearch.length() == 0) {
			return super.getAdditionalFilter();
		}
		String[] splittato = fullTextSearch.split(" ");
		QueryByFilter query = new QueryByFilter(getEntity().getClass(), QueryByFilter.PREDICATE_AND);
		SchemaObject schema = Roma.session().getSchemaObject(this.getAdvancedFilter());
		for (String token : splittato) {
			QueryByFilterItemGroup subfilter = new QueryByFilterItemGroup(QueryByFilter.PREDICATE_OR);
			Iterator<SchemaField> iterator = schema.getField("entity").getType().getFieldIterator();
			while (iterator.hasNext()) {
				SchemaField field = iterator.next();
				if (String.class.equals(field.getLanguageType()) && Boolean.TRUE.equals(field.getFeature(ViewFieldFeatures.VISIBLE))) {

					if (token.length() > 0) {
						subfilter.addItem(field.getName(), QueryByFilter.FIELD_LIKE, token);
					}
				}
			}
			if (!subfilter.getItems().isEmpty()) {
				query.addItem(subfilter);
			}
		}

		return query;
	}

	public void onDispose() {

	}

	public void onShow() {
		Roma.setFeature(advancedFilter, "resetFilter", ViewActionFeatures.VISIBLE, Boolean.FALSE);
		refresh();
	}

	protected void refresh() {
		Roma.setFeature(this, "fullTextSearch", ViewFieldFeatures.VISIBLE, !expanded);
		Roma.setFeature(this, "advancedFilter", ViewFieldFeatures.VISIBLE, expanded);
		Roma.setFeature(this, "simpleSearch", ViewActionFeatures.VISIBLE, expanded);
		Roma.setFeature(this, "advancedSearch", ViewActionFeatures.VISIBLE, !expanded);
	}

	public void simpleSearch() {
		this.expanded = false;
		refresh();
	}

	public void advancedSearch() {
		this.expanded = true;
		refresh();
	}

	public String getFullTextSearch() {
		return fullTextSearch;
	}

	public void setFullTextSearch(String fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}

	public CRUDFilter<T> getAdvancedFilter() {
		return advancedFilter;
	}

	public void setAdvancedFilter(CRUDFilter<T> advancedFilter) {
		this.advancedFilter = advancedFilter;
	}

	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void resetFilter() {
		fullTextSearch = "";
		if (advancedFilter != null)
			advancedFilter.resetFilter();
		super.resetFilter();
	}

}