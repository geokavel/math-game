import java.util.*;
import java.io.*;
import java.net.*;

public class JavaPlayer {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("",7777);
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        out.println("JavaPlayer");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input;
        while(!(input = in.readLine()).equals("quit")) {
        Scanner s = new Scanner(input);
            LinkedList<Integer> digits = new LinkedList<Integer>();
            int target = 0;
            while(s.hasNextInt()) {
                int i = s.nextInt();
                if(s.hasNextInt()) digits.add(i);
                else target = i;
            }
            Collections.shuffle(digits);
            Iterator<Integer> it = digits.iterator();
            String str = "";
            while(it.hasNext()) {
                str+=it.next();
                double r = Math.random();
                if(it.hasNext()) {
                    if(r<.25)str+="+";
                    else if(r<.5)str+="-";
                }
            }
            out.println(str);
        }
    }
}
