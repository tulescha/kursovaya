package calc;

import java.util.List;
import java.util.Map;

public class StepResult {
    private Map<String, Double> results;
    private List<String> steps;

    public StepResult(Map<String, Double> results, List<String> steps) {
        this.results = results;
        this.steps = steps;
    }

    public Map<String, Double> getResults() { return results; }
    public List<String> getSteps() { return steps; }
}
