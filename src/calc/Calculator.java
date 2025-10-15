package calc;

import java.util.Map;

public interface Calculator {
    StepResult calculate(Map<String, Double> params);
}
