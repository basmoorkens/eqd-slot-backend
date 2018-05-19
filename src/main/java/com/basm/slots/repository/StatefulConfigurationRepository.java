package com.basm.slots.repository;

import com.basm.slots.model.StatefulConfiguration;
import org.springframework.data.repository.CrudRepository;

public interface StatefulConfigurationRepository extends CrudRepository<StatefulConfiguration, Long> {

    public StatefulConfiguration findByName(final String name);

}
