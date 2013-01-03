package studijosKTU;

import java.util.Arrays;

/**
 * Porų ("maping'ų") raktas-reikšmė objektų kolekcijos - atvaizdžio realizacija
 * maišos lentele, kolizijas sprendžiant atskirų grandinėlių (angl. separate
 * chaining) metodu. Neužmirškite, jei poros raktas - nuosavos klasės objektas,
 * pvz. klasės Automobilis objektas, klasėje būtina perdengti metodus
 * equals(Object o) ir hashCode().
 *
 * @Užduotis Peržiūrėkite ir išsiaiškinkite pateiktus metodus.
 *
 * @author darius.matulis@ktu.lt
 */
public class MapKTU<Key, Value> implements MapADTp<Key, Value> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final HashType DEFAULT_HASH_TYPE = HashType.Division;

    @Override
    public int containsValueX(Value value) {
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
    private int maxChainSize = 0;
    //Permaišymų kiekis
    private int rehashesCounter = 0;
    //Paskutinės patalpintos poros grandinėlės indeksas maišos lentelėje
    private int lastUpdatedChain = 0;
    //Lentelės grandinėlių skaičius     
    private int chainsCounter = 0;
    //Einamas poros indeksas maišos lentelėje
    private int index = 0;

    /*
     * Klasėje sukurti 4 perkloti konstruktoriai, nustatantys atskirus maišos
     * lentelės parametrus. Jei kuris nors parametras nėra nustatomas -
     * priskiriama standartinė reikšmė.
     */
    public MapKTU() {
        this(DEFAULT_HASH_TYPE);
    }

    public MapKTU(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public MapKTU(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public MapKTU(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public MapKTU(int initialCapacity, float loadFactor, HashType ht) {
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

    /**
     * Atvaizdis papildomas nauja pora.
     *
     * @param key raktas,
     * @param value reikšmė.
     */
    public boolean containsValue(Value value) {
        if (value == null) {
            throw new NullPointerException("Null pointer in put");
        }
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                if (table[i].value == value) {
                    return true;
                }
                Node<Key, Value> elem = table[i];
                while (elem.next != null) {
                    elem = elem.next;
                    if (elem.value == value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String chainList() {
        String ch = "";
        int ilgis = 0;
        int[] ilg = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            ilgis = 0;
            if (table[i] != null) {
                ilgis++;
                Node<Key, Value> elem = table[i];
                while (elem.next != null) {
                    elem = elem.next;
                    ilgis++;
                }
            }
            ilg[i] = ilgis;

        }
        int kiek = 0;
        for (int i = 0; i < size; i++) {
            kiek = 0;
            for (int j : ilg) {
                if (j == i) {
                    kiek++;
                }
            }
            if (kiek != 0) {
                ch = ch + "ilgis: " + i + " kiekis " + kiek + "\n";
            }
        }
        return ch;
    }

    @Override
    public int emptyElementsNumber() {
        int number = 0;
        for (int i = 0; i < table.length; i++) {
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
        return size * 1.0 / (table.length - emptyElementsNumber());
    }

    @Override
    public Value put(Key key, Value value) {
        if (key == null || value == null) {
            throw new NullPointerException("Null pointer in put");
        }
        index = hash(key, ht);
        if (table[index] == null) {
            chainsCounter++;
        }
        Node<Key, Value> node = getInChain(key, table[index]);
        if (node == null) {
            table[index] = new Node<Key, Value>(key, value, table[index]);
            size++;
            if (size > table.length * loadFactor) {
                rehash(table[index]);
            } else {
                lastUpdatedChain = index;
            }
        } else {
            node.value = value;
            lastUpdatedChain = index;
        }
        return value;
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
        index = hash(key, ht);
        Node<Key, Value> node = getInChain(key, table[index]);
        return (node != null) ? node.value : null;
    }

    /**
     * Pora pašalinama iš atvaizdžio.
     *
     * @param key raktas.
     */
    @Override
    public Value remove(Key key) {
        if (key == null) {
            throw new NullPointerException("Null pointer in remove");
        }
        index = hash(key, ht);
        Node<Key, Value> previous = null;
        for (Node<Key, Value> n = table[index]; n != null; n = n.next) {
            if ((n.key).equals(key)) {
                if (previous == null) {
                    table[index] = n.next;
                } else {
                    previous.next = n.next;
                }
                size--;
                if (table[index] == null) {
                    chainsCounter--;
                }
                return n.value;
            }
            previous = n;
        }
        return null;
    }

//==============================================================================
// Permaišymas
//==============================================================================
    private void rehash(Node<Key, Value> node) {
        MapKTU mapKTU =
                new MapKTU(table.length * 2, loadFactor, ht);
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

    int hash(Key key) {
        int h = key.hashCode();

        return Math.abs(h) % table.length;

    }

//==============================================================================
//Paieška vienoje grandinėlėje
//==============================================================================
    private Node getInChain(Key key, Node node) {
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
    }

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
     *
     * @param delimiter Celės elemento kirtiklis
     * @return Grąžina AbstractTableModel klasės objektą.
     */
    @Override
    public HashTableModel<Key, Value> getModel(String delimiter) {
        return new HashTableModel<Key, Value>(delimiter, this.table, this.maxChainSize);
    }
}