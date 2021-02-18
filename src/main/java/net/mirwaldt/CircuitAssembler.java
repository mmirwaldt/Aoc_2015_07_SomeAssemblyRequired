package net.mirwaldt;

import java.util.SortedMap;

public interface CircuitAssembler {
    void assemble(String circuit);
    SortedMap<String, Integer> evaluate();
}
