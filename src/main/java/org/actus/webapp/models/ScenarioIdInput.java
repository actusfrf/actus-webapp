package org.actus.webapp.models;

public class ScenarioIdInput {
    private String scenarioId;

    public ScenarioIdInput() {
    }

    public ScenarioIdInput(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioIdInput{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append('}');
        return sb.toString();
    }

}