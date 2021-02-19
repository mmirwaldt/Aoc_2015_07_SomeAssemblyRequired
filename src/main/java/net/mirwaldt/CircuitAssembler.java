package net.mirwaldt;

import java.util.NoSuchElementException;
import java.util.SortedMap;

public interface CircuitAssembler {
    void assemble(String circuit);
    SortedMap<String, Integer> evaluate();
    int evaluate(String varName) throws NoSuchElementException;
}
