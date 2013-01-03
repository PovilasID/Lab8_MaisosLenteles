package studijosKTU;
import javax.swing.table.AbstractTableModel;


public class HashTableModel<Key, Value> extends AbstractTableModel {

    private String delimiter;
    private Node<Key, Value>[] table;
    private int maxChainSize = 0;

    public HashTableModel(String delimiter, Node<Key, Value>[] table, int maxChainSize) {
        this.delimiter = delimiter;
        this.table = table;
        this.maxChainSize = maxChainSize;

    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return "[" + row + "]";
        }
        if (row <= table.length && table[row] != null) {
            int count = 0;
            for (Node<Key, Value> n = (Node<Key, Value>) table[row]; n != null; n = n.next) {
                if (++count == col) {

                    return split(n.toString(), delimiter);
                }
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return (col == 0) ? "#" : "(" + --col + ")";
    }

    @Override
    public int getColumnCount() {
        return maxChainSize + 1;
    }

    @Override
    public int getRowCount() {
        return table.length;
    }

    private String split(String s, String delimiter) {
        int k = s.indexOf(delimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }
    
}
