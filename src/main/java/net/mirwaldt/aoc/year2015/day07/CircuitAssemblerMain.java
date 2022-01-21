package net.mirwaldt.aoc.year2015.day07;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CircuitAssemblerMain {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of("input.txt"), StandardCharsets.US_ASCII);

        final CircuitAssembler circuitAssemblerInPartOne = new DefaultCircuitAssembler();
        assembleCircuit(lines, circuitAssemblerInPartOne);
        final int a1 = circuitAssemblerInPartOne.evaluate("a");
        System.out.println(a1); // result - 16076


        final CircuitAssembler circuitAssemblerInPartTwo = new DefaultCircuitAssembler();
        assembleCircuit(lines, circuitAssemblerInPartTwo);
        circuitAssemblerInPartTwo.assemble(a1 + " -> b");
        final int a2 = circuitAssemblerInPartTwo.evaluate("a");
        System.out.println(a2); // result - 2797
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
