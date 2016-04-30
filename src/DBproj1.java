import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
//tian ye super sb
=======

// wang jue da sb

>>>>>>> origin/master
public class DBproj1 {
	private static void init(String fname){//initialize
		List<String> dataList=new ArrayList<String>();
	}
	
	private static void run1(){//Threshold Algorithm
		
	}
	
	private static void run2(int[][] table, int[] vector){//Naive priority algorithm
		
	}
	
	private static int fv(int[] attr, int[] vector){
		int result = 0;
		
		return result;
	}
	
	public static void main(String[] args){
		String command = null;
		int k, n;
		k = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		
		int[][] table;
		int[] vector = new int[n];
		while(true){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("topk>");
			try{
				command = br.readLine();
			}catch(IOException e){
				
			}
			String[] commandline = command.split(" ");
			if (commandline[0].equals("init")) {
				init(commandline[1]);
			}else if(commandline[0].equals("run1")){
				
			}else if(commandline[0].equals("run2")){
				int len = commandline.length;
				if (len-1 != n) {
					System.out.println("Error: not enough values\n");
				}else{
					for(int i=0; i<n; i++){
						vector[i] = Integer.parseInt(commandline[i+1]);
					}
				}
			}
		}
	}
}
