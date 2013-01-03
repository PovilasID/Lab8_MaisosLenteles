/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package studijosKTU;

/**
 *
 * @author PovilasSid
 */
public interface MapADTx<Key, Value> extends MapADTp<Key, Value> {

    public Value put(String dataString);

    public void load(String fName);

    public void save(String fName);

    public void println();

    public void println(String title);
    
}
