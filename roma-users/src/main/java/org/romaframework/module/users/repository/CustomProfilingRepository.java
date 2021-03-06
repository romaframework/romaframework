package org.romaframework.module.users.repository;

import java.util.List;

import org.romaframework.core.repository.PersistenceAspectRepository;
import org.romaframework.module.users.domain.CustomProfiling;

/**
 * Repository class for CustomProfiling entity. By default it extends the
 * PersistenceAspectRepository class that delegates the execution of all
 * commands to the PersistenceAspect.
 * <br/><br/>
 * This class was generated by Roma Meta Framework CRUD wizard.       
 */ 
public class CustomProfilingRepository extends PersistenceAspectRepository<CustomProfiling> {
  @Override
  public List<CustomProfiling> getAll() {
    return getAll(CustomProfiling.class);
  }
}
