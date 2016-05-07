import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	private static Table join(String[] filenames){
		Table joinall = null;
		
		int filenum = filenames.length-1;
		Table[] alltable = new Table[filenum];
		for (int i = 0; i < filenum; i++) {
			String filename = "src/"+filenames[i+1];
			List<String> dataList = init(filename);
			if(dataList!=null && !dataList.isEmpty()){
				int row = dataList.size()-1;
		        String[] attributes = dataList.get(0).split(",");
		        int col = attributes.length;
		        
		        alltable[i] = new Table(row, col);
		        alltable[i].attrbutes = attributes;
		        for(int j=0; j<row; j++){
		        	String[] tuple = dataList.get(j+1).split(",");
		            for(int k=0; k<col; k++){
		            	alltable[i].value[j][k] = Integer.parseInt(tuple[k]);
		            }
		        }
		   }
		}
		joinall = alltable[0];
		for (int i=1; i<filenum; i++){
			joinall = join_two(joinall, alltable[i]);
		}
		
		return joinall;
	}
	
	private static Table join_two(Table table1, Table table2){
		int row = table1.row * table2.row;
		int col = table1.col + table2.col;
		Table joinedtable = new Table(row, col);
		for (int i=0; i<table1.row; i++){
			for (int j=0; j<table2.row; j++){
				for (int m=0; m<table1.col; m++){
					joinedtable.value[i*table2.row+j][m] = table1.value[i][m];
				}
				for (int n=0; n<table2.col; n++){
					joinedtable.value[i*table2.row+j][table1.col+n] = table2.value[j][n];
				}
			}
		}
		
		for (int i=0; i<table1.col; i++){
			joinedtable.attrbutes[i] = table1.attrbutes[i];
		}
		for (int j=0; j<table2.col; j++){
			joinedtable.attrbutes[table1.col+j] = table2.attrbutes[j];
		}
		
		return joinedtable;
	}
	
	private static int[] run1(int [][] Table, int[] vector, Btree [] Btrees, int TopK){//Threshold Algorithm
		int vecLen = vector.length;
		int numBtrees = Btrees.length;
		int K = TopK;
		int MaximumLine = 8340;
		int rowIdInt = 0;
		int fvResult = 0;
		int threshold = 0;
		int []rowIds = new int[K];
		Map<Integer, Integer> queueTopK = new HashMap<Integer, Integer>();
		//for i from 0 to numBtrees - 1, 
		//	find the first unchecked object j in Btrees[i]
		//	find the position/rowId of object j in Btree of Id
		//	according to the rowId to find object in Table,and find the int[] attr
		// 	calculate the  fv(int[] attr, int[] vector)
		//	compare with candidate in queueTopK, and insert if necessary
		//calculate the Threshold in the Iteration m by using fv(int[] attr, int[] vector)
		// on the line m, and compare each value in queueTopK
		//if all value are bigger or equal to Threshold, then return the topK rowId in int[]
		//System.out.println(queueTopK.size());
		try{
			File writename = new File("src/log.txt");
			BufferedWriter out = new BufferedWriter( new FileWriter(writename));
			out.write("vector lenght is "+vector.length+"\n");
		for(int j = 0; j < MaximumLine; j++){//for how many lines
			//computer the threshold
			threshold = 0;
				for(int i = 0; i < vector.length; i++){	
					rowIdInt = Btrees[i].getRowId(j, Btrees[i].getRoot(), Btrees[i].height());
					//System.out.println(rowIdInt+" and "+"");
					threshold += vector[i]*Table[rowIdInt][i+1];
					//out.write("#"+i+" Threshold now is "+threshold+"\n");
				}
		
				out.write(" Threshold is "+threshold+"\n");
				out.flush();
			//rowIdInt = Btrees[0].getRowId(j-1, Btrees[0].getRoot(), Btrees[0].height());
			
			for(int i = 0; i < vector.length; i++){
				rowIdInt = Btrees[i].getRowId(j, Btrees[i].getRoot(), Btrees[i].height());
				boolean queueTopKAlradyHasIt = false;
				for(Integer key: queueTopK.keySet()){
					if(rowIdInt==key) {
						queueTopKAlradyHasIt = true;
					};
				}
				if(queueTopKAlradyHasIt == true) continue;		
				fvResult = fv(Table[rowIdInt],vector);
				if(queueTopK.size() < K){
					queueTopK.put(rowIdInt, fvResult);
					out.write("#"+queueTopK.size()+" put "+rowIdInt+", "+fvResult+" into TopK queue"+"\n");
					continue;
				}
				if(queueTopK.size() == K){
					//find the (key, value) with the smallest value
					//compare with the threshold
					//if larger than threshold, sort, store into rowIds. and return it.
					//if the smallest < fvResult, remove it, and put (rowIdInt, fvResult) into Hashmap
					//if larger than threshold, sort, store into rowIds. and return it.
					//reference:http://blog.csdn.net/tjcyjd/article/details/11111401
					//reference:http://www.cnblogs.com/hxsyl/p/3331095.html
				
					List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(queueTopK.entrySet());
					if(SmallestValue(queueTopK)>= threshold){
						
						Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){
							@Override
							public int compare(Entry<Integer, Integer>o1, Entry<Integer, Integer>o2){
								return o2.getValue() - o1.getValue();
							}});
						int iteInt = 0;
						out.write(iteInt+"@1Found!\n");
						out.flush();
						for (Map.Entry<Integer, Integer> mapping : list) { 
							rowIds[iteInt++] = mapping.getKey();
				        }						
						return rowIds;
					}
					if(SmallestValue(queueTopK)<fvResult){
						out.write("1size"+queueTopK.size()+"\n");
						queueTopK.remove(KeyWithSmallestValue(queueTopK));
						out.write("2size"+queueTopK.size()+"\n");
						for(Integer key: queueTopK.keySet()){
							out.write(key+" "+queueTopK.get(key)+"\n");
							out.flush();
						}
						out.write(rowIdInt+" "+fvResult+"\n");
						queueTopK.put(rowIdInt, fvResult);
						out.write("3size"+queueTopK.size()+"\n");
					}
					if(SmallestValue(queueTopK)>= threshold){
						//sort(queueTopK)
						Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){
							@Override
							public int compare(Entry<Integer, Integer>o1, Entry<Integer, Integer>o2){
								return o2.getValue() - o1.getValue();
							}});
						int iteInt = 0;
						out.write(iteInt+"@2Found!\n");
						out.flush();
						for (Map.Entry<Integer, Integer> mapping : list) { 
							rowIds[iteInt++] = mapping.getKey();
				        }	
						return rowIds;
					}
				}
			}
		}
		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return rowIds;
	}
 
	private static int SmallestValue(Map<Integer, Integer> hashmap){
		int small = 100000;
		for(Integer value: hashmap.values()){
			if(value <  small){
				small = value;
			}
		}
		return small;
	}
	
	private static int KeyWithSmallestValue(Map<Integer, Integer> hashmap){
		int small = 100000;
		int key = 0;
		for(Map.Entry<Integer, Integer> entry: hashmap.entrySet()){
			if(entry.getValue()<small){
				small = entry.getValue();
				key = entry.getKey();
			}
		}
		return key;
	}
	private static int[] run2(int[][] table, int[] vector, int k){//Naive priority algorithm
		int farray[] = null;
		farray = new int[table.length];
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < table.length; i++){
			farray[i] = fv(table[i], vector);
			map.put(i, farray[i]);
	    }
         
        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());  
       
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {  
        	
            @Override  
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {  
                //return o1.getValue().compareTo(o2.getValue());  
                return o2.getValue()- o1.getValue();  
            }  
        });  
        
    	int i = 0;
        int sortedId[] = new int[table.length];

        for (Map.Entry<Integer, Integer> mapping : list) { 
       	       //System.out.println(mapping.getKey() + ":" + mapping.getValue()); 
       	       sortedId[i] = mapping.getKey();
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
		String[] attributes = null;
		
		int k, n;
		//k = Integer.parseInt(args[0]);
		//n = Integer.parseInt(args[1]);
		k=5;
		n=5;
		//int[][] table = null;
		Table table = null;
		int[] vector = new int[n];
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("topk>");
		try{
			command = br.readLine();
		}catch(IOException e){
		}
		String[] commandline = command.split(" ");
		int row=0, col=0;
		if (commandline[0].equals("init")) {
			/*String filename = "src/"+commandline[1];
			List<String> dataList = init(filename);
			if(dataList!=null && !dataList.isEmpty()){
		        row = dataList.size()-1;
		        attributes = dataList.get(0).split(",");
		        col = attributes.length;
		        table = new int[row][col];
		        for(int i=0; i<row; i++){
		        	String[] tuple = dataList.get(i+1).split(",");
		            for(int j=0; j<col; j++){
		            	table[i][j] = Integer.parseInt(tuple[j]);
		            }
		        }
		   }*/
			table = join(commandline);
			attributes = table.attrbutes;
			row = table.row;
			col = table.col;
		
			Btree<Integer, Integer> ptree = new Btree<Integer, Integer>();
			Btree<Integer, Integer>[] strees = new Btree[col-1];
			for(int tt=0; tt<col-1; tt++){
				strees[tt] = new Btree<Integer, Integer>();
			}
			for(int i=0; i<row; i++){
				for(int j=0; j<col; j++){
					if(j==0){
						ptree.put(table.value[i][j],i);
					}else{
						strees[j-1].put(table.value[i][j], table.value[i][0]);
					}
				}
			}

			while(true){
				System.out.println("Start...");
				System.out.print("topk>");
				try{
					command = br.readLine();
				}catch(IOException e){	
				}
				commandline = command.split(" ");
				if(commandline[0].equals("run1")){
					int len = commandline.length;
				
					for (int i=0; i<len-1; i++){
						vector[i] = Integer.parseInt(commandline[i+1]);
					}
					PrintTopK(run1(table.value, vector, strees, k), table.value, attributes, k, vector);		
				}else if(commandline[0].equals("run2")){
					int len = commandline.length;
					if (len-1 != n) {
						System.out.println("Error: not enough values\n");
					}else{
						for(int i=0; i<n; i++){
							vector[i] = Integer.parseInt(commandline[i+1]);
						}
					}
					PrintTopK(run2(table.value, vector, k), table.value, attributes, k, vector);
				}else if (commandline[0].equals("exit")) {
					break;
				}
			}
		}
	}
	public static void PrintTopK (int[] sortedId, int [][] table, String [] attributes, int k, int []vector){
		for(int i=0; i<table[0].length; i++){
			System.out.print(attributes[i]+"\t");
		}
		System.out.print("score\n");
		for(int m=0; m<k;m++){
        	  for(int q=0;q<table[0].length;q++){
        		  System.out.print(table[sortedId[m]][q]+"\t");  
        	  }
        	  System.out.println(fv(table[sortedId[m]],vector));
    		  System.out.print("\n");   		          	  
         } 		
	}
}
