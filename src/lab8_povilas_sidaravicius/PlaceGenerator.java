/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab8_povilas_sidaravicius;

import GUI.KsSwing;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author PovilasSid
 */
public class PlaceGenerator {

    private static Random ag = new Random();
    private static DecimalFormat f = new DecimalFormat("##.00000");
    private static Place[] placesArray;
    private static String[] idArray;
    private static int startBase = 0, endBase = 0;
    private static boolean beginning = true;
    public static double MIN_LAT = 0;
    public static double MAX_LAT = 90;
    public static double MIN_LON = -180;
    public static double MAX_LON = 180;
    private int number = 0;
    private int idNumber = 0;

    private static int serNr = 100;
    

    public static Place[] generatePlaceArray(int number) {
        //Atsitiktinių generatorius
        String[][] am = { // galimų vietu duomenu masyvas
            {"KTU Statybos rumai", "akadem", "univer", "moksl", "architek", "statyb"},
            {"KTU SC", "akadem", "univer", "moksl", "IT", "spaus","komp"},
            {"KTU Elektronikos rumai", "akadem", "univer", "moksl", "base", "konf", "valg"},
            {"KTU Dizaino fukultetas", "akadem", "univer", "dizain", "menas", "daile", "mada"},
            {"KTU centriniai rūmai", "akadem", "univer", "atstovybe", "centras", "administracija"},
            {"PC Akropolis", "parduotuves", "batai", "rubai", "maistas", "elektronika", "maxima"},
            {"Žalgerio arena", "renginiai", "koncertai", "krepsinis", "spektakliai", "show"},
            {"Kauno sporto halė", "sportas", "atletika", "stadijonas", "koncertai"},
            {"PC Savas", "parduotuves", "batai", "rubai", "maistas", "sultys", "indai", "rimi"},
            {"PC Dainava", "parduotuves", "maistas", "turgus", "rimi"}
        };
        
        placesArray = new Place[number];
        idArray = new String[number];
        ag.setSeed(1957);
        double randomLat;
        double randomLon;
        for (int i = 0; i < number; i++) {
            int ma = ag.nextInt(am.length);        // indeksas  0..
            int mo = ag.nextInt(am[ma].length - 1) + 1;// tipo indeksas 1..
            randomLat = MIN_LAT + (MAX_LAT - MIN_LAT) * ag.nextDouble();
            randomLon = MIN_LON + (MAX_LON - MIN_LON) * ag.nextDouble();
            placesArray[i] = new Place(am[ma][0], randomLat, randomLon, ag.nextInt(1500 - 10 + 1) + 10, am[ma][mo]);
            idArray[i] = "A" + serNr++;
        }
        Collections.shuffle(Arrays.asList(placesArray));
        Collections.shuffle(Arrays.asList(idArray));
        return placesArray;
    }

    public Place[] makeAndSell(int number, int genNr) throws KsSwing.MyException {
        if (number > genNr) {
            throw new KsSwing.MyException("makeAndSell error", 100);
        }
        placesArray = generatePlaceArray(genNr);
        this.number = number;
        return Arrays.copyOf(placesArray, number);
    }

    public Place parduotiIsSandelio() throws KsSwing.MyException {
        if (number < placesArray.length) {
            return placesArray[number++];
        } else {
            throw new KsSwing.MyException("");
        }
    }
    public String formuotiAutoID() throws KsSwing.MyException {
        if (idNumber >= idArray.length) {
            idNumber = 0;
        }
        return idArray[idNumber++];
    }

    public Place[] grazintiAutoMasyva() {
        return placesArray;
    }

    public String[] grazintiAutoIDMasyva() {
        return idArray;
    }
    public static Place[] generateAndMix(int aibe, int aibesImtis,
            double isbarstKoef) throws KsSwing.MyException {
        placesArray = generatePlaceArray(aibe);
        return ismaisyti(placesArray, aibesImtis, isbarstKoef);
    }

    //Galima paduoti masyvą išmaišymui iš išorės
    public static Place[] ismaisyti(Place[] autoBaze,
            int kiekis, double maisymoDalis) throws KsSwing.MyException {
        if (autoBaze == null) {
            throw new NullPointerException("Null pointeris ismaisyti() metode");
        }
        if (kiekis <= 0) {
            throw new KsSwing.MyException(kiekis + "", 0);
        }
        if (autoBaze.length < kiekis * 3) {
            throw new KsSwing.MyException(autoBaze.length + " > " + kiekis + "*3", 3);
        }
        if ((maisymoDalis < 0) || (maisymoDalis > 1)) {
            throw new KsSwing.MyException(maisymoDalis + "", 2);
        }
        Place temp;
        //Kad medis augtų ne į vieną pusę, pradinė generuojama aibė paimama
        //ne iš masyvo pradžios, o iš atsitiktinės vietos, ir perrašoma į 
        //pradžią sukeičiant elementus
        int index = 0;
        if (autoBaze.length - (kiekis * 2) > 0) {
            index = ag.nextInt(autoBaze.length - (kiekis * 2));
        }
        index += kiekis;
        for (int i = 0; i < kiekis; i++) {
            temp = autoBaze[i];
            autoBaze[i] = autoBaze[index];
            autoBaze[index] = temp;
            index++;
        }
        //Likusi masyvo dalis surūšiuojama
        Arrays.sort(autoBaze, kiekis, autoBaze.length);
        //Išmaišoma spausdinama aibės imtis
        int j1 = (int) (kiekis * maisymoDalis / 2.0) + 1;
        int j2 = (kiekis - j1) < 0 ? 0 : kiekis - j1;
        if (j1 > 1) {
            Collections.shuffle(Arrays.asList(autoBaze).subList(0, j1));
        }
        if (j2 < (kiekis - 1)) {
            Collections.shuffle(Arrays.asList(autoBaze).subList(j2, kiekis));
        }
        //Išmaišoma likusi aibės imtis
        j1 = (int) ((autoBaze.length + kiekis) * maisymoDalis / 2.0) + 1;
        j2 = (autoBaze.length + kiekis - j1) < 0 ? 0 : autoBaze.length + kiekis - j1;
        if (j1 > kiekis + 1) {
            Collections.shuffle(Arrays.asList(autoBaze).subList(kiekis + 1, j1));
        }
        if (j2 < (autoBaze.length - 1)) {
            Collections.shuffle(Arrays.asList(autoBaze).subList(j2, autoBaze.length));
        }
        startBase = kiekis;
        endBase = autoBaze.length - 1;
        PlaceGenerator.placesArray = autoBaze;
        return Arrays.copyOf(placesArray, kiekis);
    }

    public static Place imtiIsBazes() throws KsSwing.MyException {
        if ((endBase - startBase) < 0) {
            throw new KsSwing.MyException(endBase - startBase + "", 4);
        }
        //Vieną kartą automobilis imamas iš masyvo pradzios, kitą kartą - iš galo.
        if (beginning) {
            beginning = false;
            return placesArray[startBase++];
        } else {
            beginning = true;
            return placesArray[endBase--];
        }
    }
}
