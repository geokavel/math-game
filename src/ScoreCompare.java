import java.io.*;
import java.util.*;

public class ScoreCompare {
    static int tests;
    static int round = 0;
    static int[] points = new int[]{15,12,10,9,8,7,6,5,4,3,2,1};
    public static void main(String[] args) throws Exception {
        tests = Integer.parseInt(args[0]);
	ArrayList<ProgramScore> programs = new ArrayList<ProgramScore>();
	File folder = new File("scores");
	File[] files = folder.listFiles();
	for(int i = 0;i<files.length;i++) {
	    File f = files[i];
	    Scanner s = new Scanner(f);
	    ProgramScore ps  = new ProgramScore(f.getName());
	    programs.add(ps);
	    while(s.hasNextDouble() && !ps.ready()) {
		ps.add(s.nextDouble());
	    }
        if(!ps.ready() || s.hasNextDouble()) { System.err.println(ps.name+ " has an improper number of test cases."); System.exit(-1);}
	}
 points = Arrays.copyOf(points,programs.size()); 
for(;round<tests;round++) {
    int[] roundPoints = Arrays.copyOf(points,points.length);
     Collections.sort(programs);
     for(int i = 0;i<programs.size();i++) {
	  ProgramScore prg = programs.get(i);
	  int ties = 0;
	 for(int j=i+1;j<programs.size();j++) {
	     if(prg.cur()!=programs.get(j).cur()) break;
	     ties++;
	 }
   	 //System.out.println("Round "+round+": "+ties);
	 if(ties == 0) continue;
	 int sum = 0;
	 for(int j = i;j<= i+ties;j++) {
	     sum += roundPoints[j];
	 }
	 for(int j = i;j<= i+ties;j++) {
	     roundPoints[j] = (int)Math.round(sum/(ties+1.0));
	     }
	 i+= ties;
	 
	 
     }

     for(int i = 0;i<programs.size();i++) {
	 programs.get(i).gameScore += roundPoints[i];
	 System.out.println(programs.get(i).name + ": " +  roundPoints[i]);
     }	  
     System.out.println();
	 
     }
Collections.sort(programs);
     for(int i = 0;i<programs.size();i++) {
	 ProgramScore ps = programs.get(i);
	 System.out.println(ps.name+": "+ps.gameScore);
 }

 }   
 
   
    
}

class ProgramScore implements Comparable<ProgramScore> {
    double[] scores;
    String name;
    int gameScore = 0;
    int addIndex = 0;
    ProgramScore(String name) {
	this.name = name;
	scores = new double[ScoreCompare.tests];
    }
    void add(double d) {
	scores[addIndex] = d;
	addIndex++;
 }
    boolean ready() {
        return addIndex == ScoreCompare.tests;
    }

    double cur() {
	return scores[ScoreCompare.round];
 }

    public int compareTo(ProgramScore other) {
	if(ScoreCompare.round == ScoreCompare.tests) return -Double.compare(this.gameScore,other.gameScore);
	return Double.compare(this.scores[ScoreCompare.round],other.scores[ScoreCompare.round]);
    }
    public String toString() {
	return name+": "+cur();
    }
}
