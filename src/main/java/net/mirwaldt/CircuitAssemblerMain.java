package net.mirwaldt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;

public class CircuitAssemblerMain {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of("input.txt"), StandardCharsets.US_ASCII);

        final CircuitAssembler circuitAssemblerInPartOne = new DefaultCircuitAssembler();
        assembleCircuit(lines, circuitAssemblerInPartOne);
        final SortedMap<String, Integer> valuesByNameInPartOne = circuitAssemblerInPartOne.evaluate();
        System.out.println(valuesByNameInPartOne.get("a")); // result - 16076


        final CircuitAssembler circuitAssemblerInPartTwo = new DefaultCircuitAssembler();
        assembleCircuit(lines, circuitAssemblerInPartTwo);
        circuitAssemblerInPartTwo.assemble(valuesByNameInPartOne.get("a") + " -> b");
        final SortedMap<String, Integer> valuesByNameInPartTwo = circuitAssemblerInPartTwo.evaluate();
        System.out.println(valuesByNameInPartTwo.get("a")); // result - 2797
    }

    private static void assembleCircuit(List<String> lines, CircuitAssembler circuitAssemblerInPartOne) {
        for (String line : lines) {
            try {
                circuitAssemblerInPartOne.assemble(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
