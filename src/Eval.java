import java.io.*;

public class Eval {
    boolean degrees;
    public Eval(boolean degrees) {
        this.degrees = degrees;
    }
public double eval(final String str) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        //        | number | functionName factor | factor `^` factor

        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // addition
                else if (eat('-')) x -= parseTerm(); // subtraction
                else return x;
            }
        }
        
        double toRadians(double theta) {
            return degrees ? Math.toRadians(theta) : theta;
        }

        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); // multiplication
                else if (eat('/')) x /= parseFactor(); // division
                else return x;
            }
        }

        double parseFactor() {
            if (eat('+')) return parseFactor(); // unary plus
            if (eat('-')) return -parseFactor(); // unary minus

            double x;
            int startPos = this.pos;
            if (eat('(')) { // parentheses
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z') { // functions
                while (ch >= 'a' && ch <= 'z') nextChar();
                String func = str.substring(startPos, this.pos);
                int repeat = 1;
                if(ch == '_') {
                    nextChar();
                    repeat = (int)parseFactor();
                    x = parseFactor();
                }
                else {
                      x = parseFactor();
                }
            
                for(int i = 0;i<repeat;i++) {
                if (func.equals("sqrt")) x = Math.sqrt(x);
                else if (func.equals("sin")) x = Math.sin(toRadians(x));
                else if (func.equals("cos")) x = Math.cos(toRadians(x));
                else if (func.equals("tan")) x = Math.tan(toRadians(x));
                else throw new RuntimeException("Unknown function: " + func);
                }
            } else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

            return x;
        }
    }.parse();
}
public static void main(String[] args) throws IOException {
    boolean degrees = args.length > 0 && args[0].equals("deg");
    Eval me = new Eval(degrees);
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
String str;
    System.out.println();
    System.out.println("Type in your mathematical expression using () + - * / ^ sqrt sin cos tan. Type 'quit' to quit.");
    System.out.println();
while(!(str = in.readLine()).equals("quit")) {
    try {
	System.out.println(me.eval(str));
    }
    catch(RuntimeException e) {
        System.out.println(e.getMessage());
    }
}
}
}
