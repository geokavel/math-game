import java.io.*;
import java.util.*;
public class TestEval {

   
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
        Eval me = new Eval(args.length == 2 && args[1].equals("deg"));
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
                result = me.eval(expression);
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
   

