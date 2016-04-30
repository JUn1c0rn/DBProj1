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

public class DBproj1 {
	private static void init(String fname){//initialize
		List<String> dataList=new ArrayList<String>();
	}
	
	private static void run1(){//Threshold Algorithm
		
	}
	
	private static void run2(){//Naive priority algorithm
		
	}
	
	private static int fv(){
		int result = 0;
		
		return result;
	}
	
	public static void main(String[] args){
		String command = null;
		int k, n;
		k = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		
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
			}
		}
	}
	
}
