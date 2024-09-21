package org.actus.webapp.repositories;

import org.actus.webapp.models.ScenarioData;
import org.springframework.data.repository.CrudRepository;

public interface ScenarioRepository extends CrudRepository<ScenarioData, String> {
    ScenarioData save(ScenarioData scenario);
    ScenarioData findByScenarioId(String scenarioId);
    void deleteByScenarioId(String scenarioId);
}
