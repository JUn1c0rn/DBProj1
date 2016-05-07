
public class Table {
	public int col, row;
	public  int[][] value;
	public String[] attrbutes;
	public Table(int r, int c) {
		// TODO Auto-generated constructor stub
		this.col = c;
		this.row = r;
		this.value = new int[row][col];
		this.attrbutes = new String[col];
	}
	
	public void print() {
		for(int i=0; i<col; i++){
			System.out.print(attrbutes[i]+"\t");
		}
		System.out.print("\n");
		
		for(int i=0; i<row; i++){
			for (int j=0; j<col; j++){
				System.out.print(String.valueOf(value[i][j])+"\t");
			}
			System.out.print("\n");
		}
	}
}
