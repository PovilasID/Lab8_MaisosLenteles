/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab8_povilas_sidaravicius;


import java.util.Locale;
import java.util.Random;
import GUI.KsSwing.MyException;
import studijosKTU.*;
import studijosKTU.MapKTU.HashType;

/**
 *
 * @author PovilasSid
 */
public class PlaceTest {
    
    public static void main(String[] args) throws MyException {
        Locale.setDefault(Locale.US); // suvienodiname skaičių formatus
        seriesTest();
    }
    
    public static void seriesTest() throws MyException{
        Place p1 = new Place("KTU Statybos rumai", 54.90596, 23.956235, 5, "akadem");
        Place p2 = new Place("KTU SC", 54.904923, 23.957029, 8, "akadem");
        Place p3 = new Place("KTU Elektronikos rumai", 54.903964, 23.958064, 10, "akadem");
        Place p4 = new Place("KTU Dizaino fukultetas", 54.901068, 23.960451, 6, "akadem");
        Place p5 = new Place("KTU centriniai rūmai", 54.898986, 23.91284, 7, "akadem");
        Place p6 = new Place("PC Akropolis", 54.891582, 23.919145, 6, "pc");
        Place p7 = new Place("Žalgerio arena", 54.890308, 23.914456, 8, "arena");
        Place p8 = new Place("Kauno sporto halė", 54.896111, 23.935833, 7, "arena");
        Place p9 = new Place("PC Savas", 54.92258, 23.963379, 6, "pc");
        Place p10 = new Place("PC Dainava", 54.917736, 23.96677, 4, "pc");

        Place[] placeArray = {p9, p7, p8, p5, p1, p6};

        //Raktų masyvas
        String[] plId = {"101", "102", "103", "104", "105", "106", "107", "108", "109"};

        Ks.oun("Vietų Aibė:");
        int id = 0;
        MapKTUx<String, Place> pMap =
                new MapKTUx(new String(), new Place(), HashType.Division);
        for (Place a : placeArray) {
            pMap.put(plId[id++], a);
        }
        
        pMap.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Ar egzistuoja pora atvaizdyje?");
        Ks.oun(pMap.contains(plId[6]));
        Ks.oun(pMap.contains(plId[7]));
        Ks.oun("Pašalinamos poros iš atvaizdžio:");
        Ks.oun(pMap.remove(plId[1]));
        Ks.oun(pMap.remove(plId[7]));
        pMap.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Atliekame porų paiešką atvaizdyje:");
        Ks.oun(pMap.get(plId[2]));
        Ks.oun(pMap.get(plId[7]));
        Ks.oun("Išspausdiname atvaizdžio poras String eilute:");
        Ks.ounn(pMap);
    }
}
