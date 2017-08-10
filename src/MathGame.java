import java.io.*;
import java.util.*;
import java.net.*;

public class MathGame {
    private static final int numPlayers = 2;
    private static final int numDigits = 5;
    private static final int targetRange = 100;
    private static ArrayList<Player> players;
    private static ServerSocket server;
    private static int target = 0;
    private static int[] digits = null;
    public static void main(String[] args) throws Exception {
        players = new ArrayList<Player>();
        server = new ServerSocket(7777);
        while(players.size() < numPlayers) {
            players.add(new Player(server.accept()));
       }
        while(!System.console().readLine().equals("quit")) {
        nextRound();
        }
        for(Player p : players) {
            p.out.println("quit");
            p.socket.close();
        }
        server.close();
    }
    private static void nextRound() throws IOException {
        int[] nums = genNums();
        target = nums[nums.length-1];
        digits = Arrays.copyOf(nums,nums.length-1);
        for(int i = 0;i<players.size();i++) {
            Player p = players.get(i);
            p.out.println(asString(nums));
            long t1 = System.nanoTime();
            p.response = p.in.readLine();
            p.time = System.nanoTime()-t1;
            try {
                char[] responseDigits = p.response.replaceAll("\\D","").toCharArray();
                if(responseDigits.length != digits.length) throw new RuntimeException();
                Arrays.sort(digits);
                Arrays.sort(responseDigits);
                for(int d = 0;d<digits.length;d++) {
                    if((int)responseDigits[d]-48 != digits[d]) throw new RuntimeException();
                }
                p.result = Eval.eval(p.response);
                p.score = Math.abs(p.result - target);
            }
            catch(RuntimeException e) {
                p.result = Double.POSITIVE_INFINITY;
                p.score = Double.POSITIVE_INFINITY;
            }
        }
        Collections.sort(players);
        printScores();
        
    }
    private static void printScores() {
        System.out.println("Target: " + target);
        System.out.println("Digits: " + asString(digits));
        for(int i = 0;i<players.size();i++) {
            Player p = players.get(i);
            System.out.printf("%d.%s: %s=%.2f Score:%.2f t=%.4fs%n",i+1,p.name,p.response,p.result,p.score,p.time/1e9);
        }
    }
    private static int[] genNums() {
        int[] nums =  new int[numDigits+1];
        for(int i  = 0;i<numDigits;i++) nums[i] = (int)Math.floor(Math.random()*10);
        nums[numDigits] = (int)Math.floor(Math.random()*targetRange);
        return nums;
    }
    private static String asString(int[] nums) {
        String s = "";
        for(int i = 0;i<nums.length;i++) {
            s += nums[i];
            if(i < nums.length-1) s += " ";
        }
        return s;
    }
}

class Player implements Comparable<Player> {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String response = "";
    String name = "";
    double result = 0.0;
    double score = 0.0;
    long time = 0;
    public Player(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(),true);
        name = in.readLine();
    }
    public int compareTo(Player p2) {
        int score = Double.compare(this.score,p2.score);
        if(score != 0) return score;
        return Long.compare(this.time,p2.time);
    }
}
