import java.io.*;
import java.util.*;
public class TestEval {

    public static double eval(final String str) {
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
		    x = parseFactor();
		    if (func.equals("sqrt")) x = Math.sqrt(x);
		    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
		    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
		    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
		    else throw new RuntimeException("Unknown function: " + func);
		} else {
		    throw new RuntimeException("Unexpected: " + (char)ch);
		}

		if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

		return x;
	    }
	}.parse();
    }

    public static String format(int[] nums) {
	String s = "";
	for(int i = 0;i<nums.length;i++) {
	    s+=nums[i];
	    if(i < nums.length-1) s+=" ";
	}
	return s;
    }
    
    public static void main(String[] args) throws Exception {
	double totalScore = 0;
	int invalid = 0;
	int testNo = 0;
	Scanner tests = new Scanner(new File("testFile.txt"));
	Scanner exps = new Scanner(new File("outputs/"+args[0]));
	File output = new File("scores/"+args[0]+".out");
	PrintWriter out = new PrintWriter(output);
	while(tests.hasNextLine() && exps.hasNextLine()) {
	    testNo++;
	    int[] testDigits = new int[5];
	    for(int i = 0;i<5;i++) {
		testDigits[i] = tests.nextInt();
	    }
	    String digitString = format(testDigits);
	    double target = tests.nextDouble();
	    tests.nextLine();
	    String expression = exps.nextLine();
	    double result,score;
	    try {
		if(expression.contains(".")) throw new RuntimeException("Expression can't contain '.'");
		char[] expDigits = expression.replaceAll("\\D","").toCharArray();
                if(expDigits.length != testDigits.length) throw new RuntimeException("Didn't use exactly 1 of each number");
                Arrays.sort(testDigits);
                Arrays.sort(expDigits);
                for(int d = 0;d<testDigits.length;d++) {
                    if((int)expDigits[d]-48 != testDigits[d]) throw new RuntimeException("Didn't use exactly 1 of each number");
                }
                result = eval(expression);
                score = Math.abs(result - target);
		totalScore += score;
		out.println(score);
	    }
	    catch(RuntimeException e) {
		invalid++;
		result = Double.NaN;
		score = Double.POSITIVE_INFINITY;
		System.err.printf("Error (Test #%d): %s %s=>%s | %s%n",testNo,digitString,target,expression,e.getMessage());
	    }
	    
	    
	}
        System.out.println("Tests Evaluated: " + testNo);
	out.close();
	if(invalid > 0) {
	    output.delete();
	}
       
	
    }
}
   

