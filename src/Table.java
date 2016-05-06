
public class Table {
	public int col, row;
	public Table(int r, int c) {
		// TODO Auto-generated constructor stub
		this.col = c;
		this.row = r;
	}
	
	public int[][] value =  new int[row][col];
	public String[] attrbutes = new String[col];
}
