package org.romaframework.module.users.repository;

import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.repository.PersistenceAspectRepository;
import org.romaframework.module.users.domain.BaseAccountStatus;

public class BaseAccountStatusRepository extends PersistenceAspectRepository<BaseAccountStatus> {

	public BaseAccountStatus findByName(String name) {
		QueryByFilter qbf = new QueryByFilter(BaseAccountStatus.class);
		qbf.addItem("name", QueryByFilter.FIELD_EQUALS, name);
		return findFirstByCriteria(qbf);
	}

}
