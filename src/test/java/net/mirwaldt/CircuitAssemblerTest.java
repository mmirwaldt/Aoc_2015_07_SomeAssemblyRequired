package net.mirwaldt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CircuitAssemblerTest {
    private static Stream<Arguments> circuitAssembler() {
        return Stream.of(Arguments.of(new DefaultCircuitAssembler()));
    }

    private final int x =  0b0000_0000_0000_0000;
    private final int nx = 0b1111_1111_1111_1111;
    private final int y =  0b1111_1111_1111_1111;
    private final int ny = 0b0000_0000_0000_0000;
    private final int z =  0b1110_1100_0100_0001;
    private final int nz = 0b0001_0011_1011_1110;
    private final int u =  0b0010_0110_1001_0101;
    private final int nu = 0b1101_1001_0110_1010;
    private final int abc = 123;

    private final int xay = 0b0000_0000_0000_0000;
    private final int xaz = 0b0000_0000_0000_0000;
    private final int yaz = z;
    private final int oneay = 1;
    private final int ya2 = 2;


    //                      0b1110_1100_0100_0001;
    //                      0b0010_0110_1001_0101
    private final int zau = 0b0010_0100_0000_0001;

    private final int xoy = y;
    private final int xoz = z;
    private final int yoz = y;
    private final int oneox = 1;
    private final int xo2 = 2;

    //                      0b1110_1100_0100_0001;
    //                      0b0010_0110_1001_0101
    private final int zou = 0b1110_1110_1101_0101;

    private final int xl2 = x;
    //                      0b1111_1111_1111_1111
    private final int yl2 = 0b1111_1111_1111_1100;
    //                      0b1110_1100_0100_0001
    private final int zl2 = 0b1011_0001_0000_0100;
    //                      0b0010_0110_1001_0101
    private final int ul2 = 0b1001_1010_0101_0100;


    private final int xr3 = x;
    //                      0b1111_1111_1111_1111
    private final int yr3 = 0b0001_1111_1111_1111;
    //                      0b1110_1100_0100_0001
    private final int zr3 = 0b0001_1101_1000_1000;
    //                      0b0010_0110_1001_0101
    private final int ur3 = 0b0000_0100_1101_0010;


    private void initVars(CircuitAssembler circuitAssembler) {
        circuitAssembler.assemble( x + " -> x");
        circuitAssembler.assemble(y + " -> y");
        circuitAssembler.assemble(z + " -> z");
        circuitAssembler.assemble(u + " -> u");
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_noCircuit(CircuitAssembler circuitAssembler) {
        assertEquals(0, circuitAssembler.evaluate().size());
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_noCircuit_evalOneVar(CircuitAssembler circuitAssembler) {
        assertThrows(NoSuchElementException.class, () -> circuitAssembler.evaluate("foo"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_directOutput(CircuitAssembler circuitAssembler) {
        circuitAssembler.assemble(x + " -> x");
        circuitAssembler.assemble(abc + " -> abc");
        SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(x, result.get("x"));
        assertEquals(abc, result.get("abc"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_assign(CircuitAssembler circuitAssembler) {
        circuitAssembler.assemble(x + " -> x");
        circuitAssembler.assemble("x -> y");
        SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(x, result.get("x"));
        assertEquals(x, result.get("y"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_NOT(CircuitAssembler circuitAssembler) {
        initVars(circuitAssembler);
        circuitAssembler.assemble( "NOT x -> nx");
        circuitAssembler.assemble("NOT y -> ny");
        circuitAssembler.assemble("NOT z -> nz");
        circuitAssembler.assemble("NOT u -> nu");
        final SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(nx, result.get("nx"));
        assertEquals(ny, result.get("ny"));
        assertEquals(nz, result.get("nz"));
        assertEquals(nu, result.get("nu"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_AND(CircuitAssembler circuitAssembler) {
        initVars(circuitAssembler);
        circuitAssembler.assemble( "x AND y -> xay");
        circuitAssembler.assemble("x AND z -> xaz");
        circuitAssembler.assemble("y AND z -> yaz");
        circuitAssembler.assemble("z AND u -> zau");
        circuitAssembler.assemble( "1 AND y -> oneay");
        circuitAssembler.assemble( "y AND 2 -> ya2");
        final SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(xay, result.get("xay"));
        assertEquals(xaz, result.get("xaz"));
        assertEquals(yaz, result.get("yaz"));
        assertEquals(zau, result.get("zau"));
        assertEquals(oneay, result.get("oneay"));
        assertEquals(ya2, result.get("ya2"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_OR(CircuitAssembler circuitAssembler) {
        initVars(circuitAssembler);
        circuitAssembler.assemble( "x OR y -> xoy");
        circuitAssembler.assemble("x OR z -> xoz");
        circuitAssembler.assemble("y OR z -> yoz");
        circuitAssembler.assemble("z OR u -> zou");
        circuitAssembler.assemble( "1 OR x -> oneox");
        circuitAssembler.assemble( "x OR 2 -> xo2");
        final SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(xoy, result.get("xoy"));
        assertEquals(xoz, result.get("xoz"));
        assertEquals(yoz, result.get("yoz"));
        assertEquals(zou, result.get("zou"));
        assertEquals(oneox, result.get("oneox"));
        assertEquals(xo2, result.get("xo2"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_LSHIFT_2(CircuitAssembler circuitAssembler) {
        initVars(circuitAssembler);
        circuitAssembler.assemble( "x LSHIFT 2 -> xl2");
        circuitAssembler.assemble("y LSHIFT 2 -> yl2");
        circuitAssembler.assemble("z LSHIFT 2 -> zl2");
        circuitAssembler.assemble("u LSHIFT 2 -> ul2");
        final SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(xl2, result.get("xl2"));
        assertEquals(yl2, result.get("yl2"));
        assertEquals(zl2, result.get("zl2"));
        assertEquals(ul2, result.get("ul2"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_RSHIFT_3(CircuitAssembler circuitAssembler) {
        initVars(circuitAssembler);
        circuitAssembler.assemble( "x RSHIFT 3 -> xr3");
        circuitAssembler.assemble("y RSHIFT 3 -> yr3");
        circuitAssembler.assemble("z RSHIFT 3 -> zr3");
        circuitAssembler.assemble("u RSHIFT 3 -> ur3");
        final SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(xr3, result.get("xr3"));
        assertEquals(yr3, result.get("yr3"));
        assertEquals(zr3, result.get("zr3"));
        assertEquals(ur3, result.get("ur3"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_allGates(CircuitAssembler circuitAssembler) {
        circuitAssembler.assemble("123 -> x");
        circuitAssembler.assemble("456 -> y");
        circuitAssembler.assemble("x AND y -> d");
        circuitAssembler.assemble("x OR y -> e");
        circuitAssembler.assemble("x LSHIFT 2 -> f");
        circuitAssembler.assemble("y RSHIFT 2 -> g");
        circuitAssembler.assemble("NOT x -> h");
        circuitAssembler.assemble("NOT y -> i");
        SortedMap<String, Integer> result = circuitAssembler.evaluate();
        assertEquals(72, result.get("d"));
        assertEquals(507, result.get("e"));
        assertEquals(492, result.get("f"));
        assertEquals(114, result.get("g"));
        assertEquals(65412, result.get("h"));
        assertEquals(65079, result.get("i"));
        assertEquals(123, result.get("x"));
        assertEquals(456, result.get("y"));
    }

    @ParameterizedTest
    @MethodSource("circuitAssembler")
    void test_allGates_evalOneVar(CircuitAssembler circuitAssembler) {
        circuitAssembler.assemble("123 -> x");
        circuitAssembler.assemble("456 -> y");
        circuitAssembler.assemble("x AND y -> d");
        circuitAssembler.assemble("x OR y -> e");
        circuitAssembler.assemble("x LSHIFT 2 -> f");
        circuitAssembler.assemble("y RSHIFT 2 -> g");
        circuitAssembler.assemble("NOT x -> h");
        circuitAssembler.assemble("NOT y -> i");
        assertEquals(72, circuitAssembler.evaluate("d"));
        assertEquals(507, circuitAssembler.evaluate("e"));
        assertEquals(492, circuitAssembler.evaluate("f"));
        assertEquals(114, circuitAssembler.evaluate("g"));
        assertEquals(65412, circuitAssembler.evaluate("h"));
        assertEquals(65079, circuitAssembler.evaluate("i"));
        assertEquals(123, circuitAssembler.evaluate("x"));
        assertEquals(456, circuitAssembler.evaluate("y"));
    }
}
