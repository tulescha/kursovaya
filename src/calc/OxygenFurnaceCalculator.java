package calc;

import java.util.*;

public class OxygenFurnaceCalculator implements Calculator {
    @Override
    public StepResult calculate(Map<String, Double> p) {
        List<String> steps = new ArrayList<>();

        double mass = p.getOrDefault("mass", 1000.0);
        double oxygenFlow = p.getOrDefault("oxygenFlow", 300.0);
        double t0 = p.getOrDefault("tInitial", 25.0);

        steps.add("1️⃣ Приняты данные: масса = " + mass + " кг, расход O₂ = " + oxygenFlow + " м³/ч, T₀ = " + t0 + " °C");

        double k = 2500;
        double Q = k * mass;
        steps.add("2️⃣ Энергия нагрева Q = " + Q + " кДж");

        double c = 0.84;
        double deltaT = Q / (mass * c);
        double tFinal = t0 + deltaT;
        steps.add("3️⃣ Расчёт конечной температуры T₁ = " + tFinal + " °C");

        double time = mass / oxygenFlow;
        steps.add("4️⃣ Время продувки t = " + time + " ч");

        double oxygenUsed = oxygenFlow * time;
        steps.add("5️⃣ Общий расход кислорода = " + oxygenUsed + " м³");

        Map<String, Double> result = new LinkedHashMap<>();
        result.put("Final Temperature (°C)", tFinal);
        result.put("Oxygen Used (m³)", oxygenUsed);
        result.put("Process Time (h)", time);

        steps.add("✅ Расчёт завершён");

        return new StepResult(result, steps);
    }
}