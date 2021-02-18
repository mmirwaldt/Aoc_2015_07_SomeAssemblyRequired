package net.mirwaldt;

import java.util.*;

public class PartOneCircuitAssembler implements CircuitAssembler {

    private final SortedMap<String, Expression> expressionByVars = new TreeMap<>();

    @Override
    public void assemble(String circuit) {
        final String[] tokens = circuit.trim().split(" ");
        if(tokens.length == 3) {
            expressionByVars.put(tokens[2], new Value(Integer.parseInt(tokens[0])));
        } else if(tokens.length == 4 && tokens[0].equals("NOT")) {
            expressionByVars.put(tokens[3], new Not(new Variable(expressionByVars, tokens[1])));
        } else if(tokens.length == 5) {
            final String operator = tokens[1];
            final String left = tokens[0];
            final String right = tokens[2];
            final String result = tokens[4];
            if(operator.equals("AND")) {
                expressionByVars.put(result,
                        new And(new Variable(expressionByVars, left),
                                new Variable(expressionByVars, right)));
            } else if(operator.equals("OR")) {
                expressionByVars.put(result,
                        new Or(new Variable(expressionByVars, left),
                                new Variable(expressionByVars, right)));
            } else if(operator.equals("LSHIFT")) {
                expressionByVars.put(result,
                        new LShift(new Variable(expressionByVars, left), Integer.parseInt(right)));
            } else if(operator.equals("RSHIFT")) {
                expressionByVars.put(result,
                        new RShift(new Variable(expressionByVars, left), Integer.parseInt(right)));
            } else {
                throw new IllegalArgumentException("Unknown operator '" + operator + "'");
            }
        } else {
            throw new IllegalArgumentException("Wrong syntax in circuit '" + circuit + "'");
        }
    }

    @Override
    public SortedMap<String, Integer> evaluate() {
        final SortedMap<String, Integer> result = new TreeMap<>();
        for(Map.Entry<String, Expression> expressionByVarEntry : expressionByVars.entrySet()) {
            result.put(expressionByVarEntry.getKey(), expressionByVarEntry.getValue().eval());
        }
        return result;
    }

    interface Expression {
        int eval();
    }

    static class Variable implements Expression {
        private final SortedMap<String, Expression> expressionByVars;
        private final String variableName;

        public Variable(SortedMap<String, Expression> expressionByVars, String variableName) {
            this.expressionByVars = expressionByVars;
            this.variableName = variableName;
        }

        @Override
        public int eval() {
            final Expression expression = expressionByVars.get(variableName);
            if(expression == null) {
                throw new NoSuchElementException("No expression for variable '" + variableName + "'.");
            }
            final int value = expression.eval() & 0xFFFF;
            expressionByVars.put(variableName, new Value(value));
            return value;
        }
    }

    static class Value implements Expression {
        private final int value;

        public Value(int value) {
            this.value = value;
        }

        @Override
        public int eval() {
            return value & 0xFFFF;
        }
    }

    static class Not implements Expression {
        private final Expression expression;

        public Not(Expression expression) {
            this.expression = expression;
        }

        @Override
        public int eval() {
            return (~expression.eval()) & 0xFFFF;
        }
    }

    static class LShift implements Expression {
        private final Expression expression;
        private final int numberOfBits;

        public LShift(Expression expression, int numberOfBits) {
            this.expression = expression;
            this.numberOfBits = numberOfBits;
        }

        @Override
        public int eval() {
            return (expression.eval() << numberOfBits) & 0xFFFF;
        }
    }

    static class RShift implements Expression {
        private final Expression expression;
        private final int numberOfBits;

        public RShift(Expression expression, int numberOfBits) {
            this.expression = expression;
            this.numberOfBits = numberOfBits;
        }

        @Override
        public int eval() {
            return (expression.eval() >> numberOfBits) & 0xFFFF;
        }
    }

    static class And implements Expression {
        private final Expression left;
        private final Expression right;

        public And(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int eval() {
            return (left.eval() & right.eval()) & 0xFFFF;
        }
    }

    static class Or implements Expression {
        private final Expression left;
        private final Expression right;

        public Or(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int eval() {
            return (left.eval() | right.eval()) & 0xFFFF;
        }
    }
}
