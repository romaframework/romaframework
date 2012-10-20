package org.romaframework.module.users.repository;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.core.repository.PersistenceAspectRepository;
import org.romaframework.module.users.domain.BaseProfile;

/**
 * Repository class for BaseGroup entity. By default it extends the PersistenceAspectRepository class that delegates the execution
 * of all commands to the PersistenceAspect. <br/>
 * <br/>
 * This class was generated by Roma Meta Framework CRUD wizard.
 */
public class BaseProfileRepository extends PersistenceAspectRepository<BaseProfile> {

	@Override
	public void delete(Object[] iObjects) {
		List<BaseProfile> profiles = new ArrayList<BaseProfile>();
		List<BaseProfile> remainingProfiles = new ArrayList<BaseProfile>();

		BaseProfile profile;
		for (Object o : iObjects) {
			profile = (BaseProfile) o;
			profiles.add(profile);
		}

		int deleted = 1;
		while (deleted > 0) {
			deleted = 0;
			remainingProfiles.clear();
			for (BaseProfile p : profiles) {
				try {
					delete(p);
					deleted++;
				} catch (Exception e) {
					remainingProfiles.add(p);
				}
			}
			profiles = remainingProfiles;
		}
	}
}