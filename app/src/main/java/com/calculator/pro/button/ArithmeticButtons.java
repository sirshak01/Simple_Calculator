package com.calculator.pro.button;

public class ArithmeticButtons {

    private StringBuilder input = new StringBuilder();

    public String inputNumber(String v) {
        if (v == null) return getDisplay();

        // Prevent starting with operators (except minus for negative numbers)
        if (input.length() == 0) {
            if (v.equals("+") || v.equals("*") || v.equals("/") ) {
                return getDisplay();
            }
        }

        // Prevent double operators like ++, --, **, //
        if (input.length() > 0) {
            char last = input.charAt(input.length() - 1);
            if (isOperator(last) && isOperator(v.charAt(0))) {
                input.setCharAt(input.length() - 1, v.charAt(0));
                return getDisplay();
            }
        }

        input.append(v);
        return getDisplay();
    }

    public String getDisplay() {
        return input.length() == 0 ? "0" : input.toString();
    }

    public String clear() {
        input.setLength(0);
        return "0";
    }

    public String backspace() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
        }
        return getDisplay();
    }

    public String calculate() {
        try {
            if (input.length() == 0) {
                return "0";
            }

            String expr = input.toString();

            // Prevent ending with operator (auto fix)
            char last = expr.charAt(expr.length() - 1);
            if (isOperator(last)) {
                expr = expr.substring(0, expr.length() - 1);
            }

            double result = eval(expr);

            input.setLength(0);

            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return "Error";
            }

            if (result == (long) result) {
                input.append((long) result);
                return String.valueOf((long) result);
            } else {
                input.append(result);
                return String.valueOf(result);
            }

        } catch (Exception e) {
            input.setLength(0);
            return "Error";
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double eval(String expression) {

        final String expr = expression;

        return new Object() {

            int pos = -1, ch;

            void next() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int c) {
                while (ch == ' ') next();
                if (ch == c) {
                    next();
                    return true;
                }
                return false;
            }

            double parse() {
                next();
                return expression();
            }

            double expression() {
                double x = term();
                while (true) {
                    if (eat('+')) x += term();
                    else if (eat('-')) x -= term();
                    else return x;
                }
            }

            double term() {
                double x = factor();
                while (true) {
                    if (eat('*')) x *= factor();
                    else if (eat('/')) {
                        double d = factor();
                        if (d == 0) throw new RuntimeException("Divide by zero");
                        x /= d;
                    } else return x;
                }
            }

            double factor() {
                if (eat('+')) return factor();
                if (eat('-')) return -factor();

                double x;
                int start = this.pos;

                if (eat('(')) {
                    x = expression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') next();
                    x = Double.parseDouble(expr.substring(start, this.pos));
                } else {
                    throw new RuntimeException("Error");
                }

                return x;
            }

        }.parse();
    }
}
