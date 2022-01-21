package net.mirwaldt.aoc.year2015.day07;

import java.util.NoSuchElementException;
import java.util.SortedMap;

public interface Expression {
    int eval();

    class Variable implements Expression {
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

    class Value implements Expression {
        private final int value;

        public Value(int value) {
            this.value = value;
        }

        @Override
        public int eval() {
            return value & 0xFFFF;
        }
    }

    class Not implements Expression {
        private final Expression expression;

        public Not(Expression expression) {
            this.expression = expression;
        }

        @Override
        public int eval() {
            return (~expression.eval()) & 0xFFFF;
        }
    }

    class LShift implements Expression {
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

    class RShift implements Expression {
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

    class And implements Expression {
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

    class Or implements Expression {
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
