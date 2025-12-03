package calc;

import java.util.*;

public class LavalNozzleCalculator implements Calculator {
    @Override
    public StepResult calculate(Map<String, Double> p) {
        List<String> steps = new ArrayList<>();

        double p0 = p.getOrDefault("p0", 101325.0);
        double T0 = p.getOrDefault("T0", 300.0);
        double gamma = p.getOrDefault("gamma", 1.4);
        double R = p.getOrDefault("R", 287.0);
        double mdot = p.getOrDefault("massFlow", 1.0);

        steps.add("1) Входные данные: p₀=" + p0 + " Па, T₀=" + T0 + " K, γ=" + gamma);

        double a0 = Math.sqrt(gamma * R * T0);
        steps.add("2) Скорость звука a₀ = " + a0 + " м/с");

        double rho0 = p0 / (R * T0);
        steps.add("3) Плотность при входе ρ₀ = " + rho0 + " кг/м³");

        double Astar = mdot / (rho0 * a0);
        steps.add("4) Критическое сечение A* = " + Astar + " м²");

        double Me = 2.0; // для примера — число Маха в выходе
        double Ae = Astar * (1 / Me) * Math.pow((1 + 0.5 * (gamma - 1) * Me * Me) / (1.2), (gamma + 1) / (2 * (gamma - 1)));
        steps.add("5) Выходное сечение Aₑ = " + Ae + " м²");

        Map<String, Double> result = new LinkedHashMap<>();
        result.put("Speed of Sound (m/s)", a0);
        result.put("Density (kg/m³)", rho0);
        result.put("A*", Astar);
        result.put("A_exit", Ae);

        steps.add("Расчёт завершён");

        return new StepResult(result, steps);
    }
}
