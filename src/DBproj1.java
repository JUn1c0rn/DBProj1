import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class DBproj1 {
	private static List<String> init(String fname){//initialize
		List<String> dataList=new ArrayList<String>();
		BufferedReader br=null;
		System.out.println(fname);
        try { 
        	File file = new File(fname);
            br = new BufferedReader(new FileReader(file));
            String line = null;
            //br.readLine();
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
                //System.out.println(line);
                //System.out.println(fname);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return dataList;
	}
	
	private static void run1(){//Threshold Algorithm
		
	}
	
	private static List run2(int[][] table, int[] vector){//Naive priority algorithm
		List resList = null;
		
		return resList;
	}
	
	private static int fv(int[] attr, int[] vector){
		int result = 0;
		
		return result;
	}
	
	public static void main(String[] args){
		String command = null;
		String[] arrtibutes = null;
		
		int k, n;
		//k = Integer.parseInt(args[0]);
		//n = Integer.parseInt(args[1]);
		k=5;
		n=10;
		int[][] table = null;
		int[] vector = new int[n];
		while(true){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("topk>");
			try{
				command = br.readLine();
			}catch(IOException e){
				
			}
			String[] commandline = command.split(" ");
			
			int row=0, col=0;
			if (commandline[0].equals("init")) {
				String filename = "/Users/JUnicorn/Documents/workspace/DBProj1/src/NBA.csv";
				List<String> dataList = init(filename);
				if(dataList!=null && !dataList.isEmpty()){
					//System.out.println(command);
		            row = dataList.size()-1;
		            arrtibutes = dataList.get(0).split(",");
		            col = arrtibutes.length;
		            table = new int[row][col];
		            for(int i=0; i<row; i++){
		            	String[] tuple = dataList.get(i+1).split(",");
		            	for(int j=0; j<col; j++){
		            		table[i][j] = Integer.parseInt(tuple[j]);
		            		//System.out.println(table[i][j]);
		            	}
		            }
		        }
				
				Btree<String, String> ptree = new Btree<String, String>();
				Btree<String, String>[] strees=new Btree[col-1];
				for(int tt=0; tt<col-1; tt++){
					strees[tt] = new Btree<String, String>();
				}
				for(int i=0; i<row; i++){
					for(int j=0; j<col; j++){
						if(j==0){
							ptree.put(String.valueOf(table[i][j]), String.valueOf(i));
						}else{
							strees[j-1].put(String.valueOf(table[i][j]), String.valueOf(table[i][0]));
						}
					}
				}
				/*for(int i=0; i<col-1; i++){
					System.out.println(strees[i].size());
				}*/
				
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
				List reslist = run2(table, vector);
			}else if (commandline[0].equals("exit")) {
				break;
			}
		}
	}
}
