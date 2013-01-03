package studijosKTU;

import java.util.Arrays;



/**
 *
 * @author Juliaus
 */
public class MapTiesinis<Key, Value> implements MapADTp<Key, Value> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final HashType DEFAULT_HASH_TYPE = HashType.Division;

    @Override
    public int emptyElementsNumber() {
        int number =0;
        for(int i=0;i<table.length;i++)
        {
            if (table[i] == null) {
               number++;
            }
        }
        return number;
    }

    @Override
    public double chainLengthAverage() {
        if (table == null) {
            throw new NullPointerException("Null pointer");
        }
         int sum=0;
         int number=0;
        for(int i=0;i<table.length;i++)
        {
            if (table[i] != null) {
                number++;
                sum++;
                Node<Key, Value> elem = table[i];
                while(elem.next != null)
                {                    
                    elem = elem.next;
                    sum++;
                }
            }
        }
        return sum/(double)number;
    
    }

    @Override
    public String chainList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum HashType {

        Division, Multiplication, JavaCollectionsF
    }
    //Maišos lentelė
    private Node<Key, Value>[] table;
    //Lentelėje esančių raktas-reikšmė porų kiekis
    private int size = 0;
    //Apkrovimo faktorius
    private float loadFactor;
    //Maišos metodas
    private HashType ht;
    //--------------------------------------------------------------------------
    //Maišos lentelės įvertinimo parametrai
    //--------------------------------------------------------------------------
    //Maksimalus suformuotos maišos lentelės grandinėlės ilgis
    private int maxChainSize = 1;
    //Permaišymų kiekis
    private int rehashesCounter = 0;
    //Paskutinės patalpintos poros grandinėlės indeksas maišos lentelėje
    private int lastUpdatedChain = 0;
    //Lentelės grandinėlių skaičius
    private int chainsCounter = 0;
    //Einamas poros indeksas maišos lentelėje
    private int index = 0;

    /* Klasėje sukurti 4 perkloti konstruktoriai, nustatantys atskirus maišos
     * lentelės parametrus. Jei kuris nors parametras nėra nustatomas -
     * priskiriama standartinė reikšmė.
     */
    public MapTiesinis() {
        this(DEFAULT_HASH_TYPE);
    }

    public MapTiesinis(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public MapTiesinis(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public MapTiesinis(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public MapTiesinis(int initialCapacity, float loadFactor, HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: "
                    + initialCapacity);
        }
        if ((loadFactor < 0) || (loadFactor > 1)) {
            throw new IllegalArgumentException("Illegal load factor: "
                    + loadFactor);
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }

    /**
     * Patikrinama ar atvaizdis yra tuščias.
     *
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Grąžinamas atvaizdyje esančių porų kiekis.
     *
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Išvalomas atvaizdis.
     *
     */
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        lastUpdatedChain = 0;
        maxChainSize = 0;
        rehashesCounter = 0;
        chainsCounter = 0;
    }

    /**
     * Patikrinama ar pora egzistuoja atvaizdyje.
     *
     * @param key raktas.
     */
    @Override
    public boolean contains(Key key) {
        return get(key) != null;
    }

    public boolean containsValue(Value value) {
        if (value == null) {
            throw new NullPointerException("Null value in containsValue");
        }
        for(Node<Key, Value> elm:table){
            if (elm == null) {
                    if ((elm.value).equals(value)) return true;             
            }
        }
        return false;
    }
    
        public int containsValueX(Value value) {
        if (value == null) {
            throw new NullPointerException("Null value in containsValue");
        }
        int i = 0;
        for(Node<Key, Value> elm:table){
            if (elm != null) {
                i++;
                    if ((elm.value).equals(value)) return i;             
            }
            else{
                i = 0;
            }
        }
        return -1;
    }
    

    
    /**
     * Atvaizdis papildomas nauja pora.
     * 	@param key raktas,
     *  @param value reikšmė.
     */
    @Override
    public Value put(Key key, Value value) {
        if (key == null || value == null) {
            throw new NullPointerException("Null pointer in put");
        }
        index = findPosition(key);
        if (index == -1) {
            rehash(table[index]);
            put(key, value);
            return value;
        }
        if (table[index] == null) {
            table[index] = new Node<Key, Value>(key, value, null);
            size++;
            if (size > table.length *loadFactor) {
                rehash(table[index]);
            }
        } else {
            table[index].value = value;
        }
        return value;
    }

    
    private int findPosition(Key key) {
        int index = hash(key, ht);
        int index0 = index;
        for (int j = 0; j < table.length; j++) {
            if (table[index] == null || table[index].key.equals(key)) {
                return index;
            }
            index = (index0 + j) % table.length;
        }
        return -1;
    }
    /**
     * Grąžinama atvaizdžio poros reikšmė.
     *
     * @param key raktas.
     */
    @Override
    public Value get(Key key) {
        if (key == null) {
            throw new NullPointerException("Null pointer in get");
        }
        index = findPosition(key);
        
        //Node<Key, Value> node = getInChain(key, table[index]);
        return (table[index]!= null) ? table[index].value : null;
    }

    /**
     *  Pora pašalinama iš atvaizdžio.
     *
     * @param key raktas.
     */
    @Override
    public Value remove(Key key) {
        if (key == null) {
            throw new NullPointerException("Null pointer in remove");
        }
        //Node<Key, Value> previous = null;
        int position = findPosition(key);
        if(table[position]!=null)
        {
            Value value = table[position].value;
            table[position]=null;
            size--;     
            return value;
        } else {
            return null;             
       
        }
    }
    

//==============================================================================
// Permaišymas
//==============================================================================
    private void rehash(Node<Key, Value> node) {
        MapTiesinis mapKTU =
                new MapTiesinis(table.length * 2, loadFactor, ht);
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                if (table[i].equals(node)) {
                    lastUpdatedChain = i;
                }
                mapKTU.put(table[i].key, table[i].value);
                table[i] = table[i].next;
            }
        }
        table = mapKTU.table;
        maxChainSize = mapKTU.maxChainSize;
        chainsCounter = mapKTU.chainsCounter;
        rehashesCounter++;
    }

//==============================================================================
//Maišos funkcijos skaičiavimas: pagal rakto maišos kodą apskaičiuojamas
//atvaizdžio poros indeksas maišos lentelės masyve
//==============================================================================
    private int hash(Key key, HashType ht) {
        //System.out.println(ht.toString());
        int h = key.hashCode();
        switch (ht) {
            case Division:
                return Math.abs(h) % table.length;
            case Multiplication:
                double k = (Math.sqrt(5) - 1) / 2;
                return (int) (((k * Math.abs(h)) % 1) * table.length);
            case JavaCollectionsF:
                h ^= (h >>> 20) ^ (h >>> 12);
                h = h ^ (h >>> 7) ^ (h >>> 4);
                return h & (table.length - 1);
            default:
                return Math.abs(h) % table.length;
        }
    }

//==============================================================================
//Paieška vienoje grandinėlėje
//==============================================================================
    /*private Node getInChain(Key key, Node node) {
        int chainSize = 0;
        if (key == null) {
            throw new NullPointerException("Null pointer in getInChain");
        }
        for (Node<Key, Value> n = node; n != null; n = n.next) {
            chainSize++;
            if ((n.key).equals(key)) {
                return node;
            }
        }
        maxChainSize = Math.max(maxChainSize, chainSize + 1);
        return null;
    }*/

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Node<Key, Value> n = table[i]; n != null; n = n.next) {
                    result += n.toString() + "\n";
                }
            }
        }
        return result;
    }

    /**
     * Grąžina maksimalų grandinėlės ilgį.
     *
     * @return Maksimalus grandinėlės ilgis.
     */
    @Override
    public int getMaxChainSize() {
        return maxChainSize;
    }

    /**
     * Grąžina formuojant maišos lentelę įvykusių permaišymų kiekį.
     *
     * @return Permaišymų kiekis.
     */
    @Override
    public int getRehashesCounter() {
        return rehashesCounter;
    }

    /**
     * Grąžina maišos lentelės talpą.
     *
     * @return Maišos lentelės talpa.
     */
    @Override
    public int getTableCapacity() {
        return table.length;
    }

    /**
     * Grąžina paskutinės papildytos grandinėlės indeksą.
     *
     * @return Paskutinės papildytos grandinėlės indeksas.
     */
    @Override
    public int getLastUpdatedChain() {
        return lastUpdatedChain;
    }

    /**
     * Grąžina grandinėlių kiekį.
     *
     * @return Grandinėlių kiekis.
     */
    @Override
    public int getChainsCounter() {
        return chainsCounter;
    }

    /**
     * Grąžina maišos lentelės modelį, skirtą atvaizdavimui JTable objekte
     * @param delimiter Celės elemento kirtiklis
     * @return Grąžina AbstractTableModel klasės objektą.
     */
    @Override
    public HashTableModel<Key, Value> getModel(String delimiter) {
        return new HashTableModel<Key, Value>(delimiter, this.table, this.maxChainSize);
    }
}
