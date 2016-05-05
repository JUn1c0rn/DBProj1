import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Btree<Key extends Comparable<Key>, Value>  {
    private static final int M = 4;    // max children per B-tree node = M-1

    private Node root;             // root of the B-tree
    private int HT;                // height of the B-tree
    private int N;                 // number of key-value pairs in the B-tree
    private int Sequence;          // help to find the top Xth in current B-tree
    
    
    private File file;
    private BufferedWriter fileout;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children
        private Node(int k) { m = k; }             // create a node with k children
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private Object value;
        private Node next;     // helper field to iterate over array entries
        public Entry(Comparable key, Object value, Node next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
    }

    // constructor
    public Btree() { 
    	root = new Node(0); 
    	Sequence = 0;
    	HT = 0;
    	file = new File("src/btree.txt");
    	try{
    		fileout = new BufferedWriter(new FileWriter(file));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	}

    // return number of key-value pairs in the B-tree
    public int size() { return N; }

    // return height of B-tree
    public int height() { return HT; }

    public Node getRoot(){
    	return root;
    }
    // search for given key, return associated value; return null if no such key
    public Value get(Key key) { return search(root, key, HT); }
    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    // insert key-value pair
    // add code to check for duplicate keys
    public void put(Key key, Value value) {
        Node u = insert(root, key, value, HT); 
        N++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        HT++;
    }


    private Node insert(Node h, Key key, Value value, int ht) {
        int j;
        Entry t = new Entry(key, value, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, value, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
       
        else         return split(h);
    }

    // split node in half
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        return t;    
    }

    // for debugging
    public String toString() {
        return toString(root, HT, "") + "\n";
    }
    private String toString(Node h, int ht, String indent) {
        String s = "";
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s += indent + "(" + children[j].key + ")\n";
                s += toString(children[j].next, ht-1, indent + "     ");
            }
        }
        return s;
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
    //pjm get the No.X element in Attribute Btree Ai
    public void setSequence (int sequence){
    	this.Sequence = sequence;
    }
    public String getRowId(int sequence, Node node, int ht){
		String rowId = null;
		try{
			writeLock.lock();
			try{
				fileout.write("Level #"+ht+", Node's key is "+ node.children[node.m-1].key+", and sequence is "+sequence +"\n");
				fileout.flush();
			}finally{
				writeLock.unlock();
			}
		
    	if(ht == 0){
			if(sequence <= node.m){
				writeLock.lock();
				try{
					fileout.write("return value "+node.children[node.m - sequence-1].value+"\n");
					fileout.flush();
				}finally{
					writeLock.unlock();
				}
				return (String)node.children[node.m - sequence-1].value;
			}else {
				//System.out.println("2, "+sequence + " "+node.m);
				Sequence -= node.m;
				return null;
			}
		}
		else {
			for(int i=node.m-1; i>=0; i--){
				rowId = getRowId(Sequence, node.children[i].next, ht-1);
				if(rowId != null) return rowId;
			}
		}
			}catch(Exception e){
				e.printStackTrace();
		}
		return rowId;
	}
    private int findRowId(int sequence, Node node){
    	
    	return 0;
    }
}
