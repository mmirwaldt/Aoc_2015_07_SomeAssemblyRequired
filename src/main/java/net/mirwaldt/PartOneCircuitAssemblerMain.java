package net.mirwaldt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;

public class PartOneCircuitAssemblerMain {
    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Path.of("input.txt"), StandardCharsets.US_ASCII);
        final CircuitAssembler partOneCircuitAssembler = new PartOneCircuitAssembler();
        for (String line : lines) {
            try {
                partOneCircuitAssembler.assemble(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final SortedMap<String, Integer> valuesByName = partOneCircuitAssembler.evaluate();
        System.out.println(valuesByName.get("a"));
    }
}
