package org.actus.webapp.repositories;

import org.springframework.data.repository.CrudRepository;

import org.actus.webapp.models.ScenarioData;

public interface ScenarioRepository extends CrudRepository<ScenarioData, String> {
    ScenarioData save(ScenarioData scenario);
    ScenarioData findByScenarioId(String scenarioId);
    void deleteByScenarioId(String scenarioId);
}
