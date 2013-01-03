/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package studijosKTU;

/**
 *
 * @author warchy
 */
public class Node<Key, Value> {

    //Raktas
    Key key;
    //Reikšmė
    Value value;
    //Rodyklė į sekantį grandinėlės mazgą
    Node<Key, Value> next;

    Node() {
    }

    Node(Key key, Value value, Node<Key, Value> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}