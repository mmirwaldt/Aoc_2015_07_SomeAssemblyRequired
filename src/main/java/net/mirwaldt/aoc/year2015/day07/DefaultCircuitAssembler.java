package net.mirwaldt.aoc.year2015.day07;

import java.util.*;

public class DefaultCircuitAssembler implements CircuitAssembler {

    private final SortedMap<String, Expression> expressionByVars = new TreeMap<>();

    @Override
    public void assemble(String circuit) {
        final String[] tokens = circuit.trim().split(" ");
        if(tokens.length == 3) {
            createValueOrVarAssignment(tokens);
        } else if(tokens.length == 4 && tokens[0].equals("NOT")) {
            expressionByVars.put(tokens[3], new Expression.Not(new Expression.Variable(expressionByVars, tokens[1])));
        } else if(tokens.length == 5) {
            createBinaryExpression(tokens);
        } else {
            throw new IllegalArgumentException("Wrong syntax in circuit '" + circuit + "'");
        }
    }

    private void createValueOrVarAssignment(String[] tokens) {
        final Integer anInt = tryParseInt(tokens[0]);
        if(anInt == null) {
            expressionByVars.put(tokens[2], new Expression.Variable(expressionByVars, tokens[0]));
        } else {
            expressionByVars.put(tokens[2], new Expression.Value(anInt));
        }
    }

    private void createBinaryExpression(String[] tokens) {
        final String operator = tokens[1];
        final String left = tokens[0];
        final String right = tokens[2];
        final String result = tokens[4];
        final Integer leftInt = tryParseInt(left);
        final Integer rightInt = tryParseInt(right);
        if(operator.equals("AND")) {
            createAnd(left, right, result, leftInt, rightInt);
        } else if(operator.equals("OR")) {
            createOr(left, right, result, leftInt, rightInt);
        } else if(operator.equals("LSHIFT")) {
            expressionByVars.put(result,
                    new Expression.LShift(new Expression.Variable(expressionByVars, left), rightInt));
        } else if(operator.equals("RSHIFT")) {
            expressionByVars.put(result,
                    new Expression.RShift(new Expression.Variable(expressionByVars, left), rightInt));
        } else {
            throw new IllegalArgumentException("Unknown operator '" + operator + "'");
        }
    }

    private void createOr(String left, String right, String result, Integer leftInt, Integer rightInt) {
        if(leftInt == null && rightInt == null) {
            expressionByVars.put(result,
                    new Expression.Or(new Expression.Variable(expressionByVars, left),
                            new Expression.Variable(expressionByVars, right)));
        } else if(leftInt == null) {
            expressionByVars.put(result,
                    new Expression.Or(new Expression.Variable(expressionByVars, left),
                            new Expression.Value(rightInt)));
        } else {
            expressionByVars.put(result,
                    new Expression.Or(new Expression.Value(leftInt),
                            new Expression.Variable(expressionByVars, right)));
        }
    }

    private void createAnd(String left, String right, String result, Integer leftInt, Integer rightInt) {
        if(leftInt == null && rightInt == null) {
            expressionByVars.put(result,
                    new Expression.And(new Expression.Variable(expressionByVars, left),
                            new Expression.Variable(expressionByVars, right)));
        } else if(leftInt == null) {
            expressionByVars.put(result,
                    new Expression.And(new Expression.Variable(expressionByVars, left),
                            new Expression.Value(rightInt)));
        } else {
            expressionByVars.put(result,
                    new Expression.And(new Expression.Value(leftInt),
                            new Expression.Variable(expressionByVars, right)));
        }
    }

    private Integer tryParseInt(String right) {
        Integer rightInt = null;
        try {
            rightInt = Integer.parseInt(right);
        } catch (NumberFormatException e) {

        }
        return rightInt;
    }

    @Override
    public SortedMap<String, Integer> evaluate() {
        final SortedMap<String, Integer> result = new TreeMap<>();
        for(Map.Entry<String, Expression> expressionByVarEntry : expressionByVars.entrySet()) {
            result.put(expressionByVarEntry.getKey(), expressionByVarEntry.getValue().eval());
        }
        return result;
    }

    @Override
    public int evaluate(String varName) throws NoSuchElementException {
        Expression expression = expressionByVars.get(varName);
        if(expression == null) {
            throw new NoSuchElementException();
        }
        return expression.eval();
    }
}
