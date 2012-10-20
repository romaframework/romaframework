package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaParameter;

public class SchemaEventSearch extends SchemaEvent {

	private static final long	serialVersionUID	= -5781697247853066424L;

	public SchemaEventSearch(SchemaField iField) {
		super(iField, "search", Arrays.asList(new SchemaParameter[] { new SchemaParameter("param1",0, Roma.schema().getSchemaClass(String.class)) }));
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		SchemaClass sc = field.getType().getSchemaClass();
		GenericRepository<?> repo = Roma.repository(sc);
		if (repo != null) {
			if (params != null && params.length != 0 && params[0] instanceof String)
				return repo.search((String) params[0]);
			else
				return repo.getAll();
		} else {
			QueryByFilter filter = new QueryByFilter((Class<?>) sc.getLanguageType());
			filter.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
			return Roma.context().persistence().query(filter);
		}
	}

}
