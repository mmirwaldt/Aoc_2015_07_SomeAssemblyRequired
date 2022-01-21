package net.mirwaldt.aoc.year2015.day07;

import java.util.NoSuchElementException;
import java.util.SortedMap;

public interface CircuitAssembler {
    void assemble(String circuit);
    SortedMap<String, Integer> evaluate();
    int evaluate(String varName) throws NoSuchElementException;
}
