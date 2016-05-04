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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	
	private static int[] run1(int [][] Table, int[] vector, Btree [] Btrees, int TopK){//Threshold Algorithm
		int vecLen = vector.length;
		int numBtrees = Btrees.length;
		int K = TopK;
		int MaximumLine = 8000;
		int rowIdInt = 0;
		int fvResult = 0;
		int threshold = 0;
		int []rowIds = new int[K];
		TreeMap<Integer, Integer> queueTopK = new TreeMap<Integer, Integer>();
		//for i from 0 to numBtrees - 1, 
		//	find the first unchecked object j in Btrees[i]
		//	find the position/rowId of object j in Btree of Id
		//	according to the rowId to find object in Table,and find the int[] attr
		// 	calculate the  fv(int[] attr, int[] vector)
		//	compare with candidate in queueTopK, and insert if necessary
		//calculate the Threshold in the Iteration m by using fv(int[] attr, int[] vector)
		// on the line m, and compare each value in queueTopK
		//if all value are bigger or equal to Threshold, then return the topK rowId in int[]
		for(int j = 1; j <= MaximumLine; j++){//for how many lines
			//computer the threshold
			for(int i = 0; i < vector.length; i++){
				threshold += vector[i]*Table[Integer.parseInt(Btrees[i].getRowId(j, Btrees[i].getRoot(), Btrees[i].height()))][j+1];
			}
			for(int i = 0; i < vector.length; i++){
				rowIdInt = Integer.parseInt(Btrees[i].getRowId(j, Btrees[i].getRoot(), Btrees[i].height()));
				fvResult = fv(Table[rowIdInt],vector);
				if(queueTopK.size() < K){
					queueTopK.put(fvResult, rowIdInt);
				}
				if(queueTopK.size() == K){
					if(queueTopK.firstKey()>threshold){
						//return these rowId;
						Iterator it = queueTopK.keySet().iterator();
						int iteInt = K-1;
						while(it.hasNext()){
							rowIds[iteInt--] = queueTopK.get(it.next());
						}
						return rowIds;
					}
					//compare the last key of the queueTopK
					if(queueTopK.firstKey()<fvResult){
						queueTopK.remove(queueTopK.firstKey());
						queueTopK.put(fvResult, rowIdInt);
					}
					if(queueTopK.firstKey()>threshold){
						//return these rowId;
						Iterator it = queueTopK.keySet().iterator();
						int iteInt = K-1;
						while(it.hasNext()){
							rowIds[iteInt--] = queueTopK.get(it.next());
						}
						return rowIds;
					}
				}
			}
		}
		return rowIds;
		
	}
	private static int[] run2(int[][] table, int[] vector, int k){//Naive priority algorithm
		int farray[] = null;
		farray = new int[table.length];
		
		for(int i = 0; i < table.length; i++){
				farray[i] = fv(table[i], vector);
		}
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for(int i = 0; i < table.length; i++){
			farray[i] = fv(table[i], vector);
			map.put(Integer.toString(i), farray[i]);
	    }
       
        //将map.entrySet()转换成list  
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());  
       
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {  
            //降序排序  
            @Override  
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {  
                //return o1.getValue().compareTo(o2.getValue());  
                return o2.getValue().compareTo(o1.getValue());  
            }  
        });  
        
    	int i = 0;
        int sortedId[] = new int[table.length];

        for (Map.Entry<String, Integer> mapping : list) { 
       	       //System.out.println(mapping.getKey() + ":" + mapping.getValue()); 
       	       sortedId[i] = Integer.parseInt(mapping.getKey());
       	       i++;   	       
        	}
        return sortedId;

	}
	
	private static int fv(int[] attr, int[] vector){
		int sum = 0;	
		for (int i = 0; i < vector.length; i++) {
		        sum += attr[i+1] * vector[i];
		}		
		return sum;
	}
	
	public static void main(String[] args){
		String command = null;
		String[] arrtibutes = null;
		
		int k, n;
		//k = Integer.parseInt(args[0]);
		//n = Integer.parseInt(args[1]);
		k=5;
		n=5;
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
				int[] sortedId = run2(table, vector, k);
				for(int i=0; i<table[0].length; i++){
					System.out.print(arrtibutes[i]+"\t");
				}
				System.out.print("\n");
				for(int m=0; m<k;m++){
		        	  for(int q=0;q<table[0].length;q++){
		        		  System.out.print(table[sortedId[m]][q]+"\t");  		  
		        	  }
		    		  System.out.print("\n");   		          	  
		         } 
			}else if (commandline[0].equals("exit")) {
				break;
			}
		}
	}
}
