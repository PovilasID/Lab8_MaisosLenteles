package lab8_povilas_sidaravicius;

import GUI.KsSwing.MyException;
import GUI.MainWindow;
import java.util.Locale;
import studijosKTU.Ks;

/*
 * Darbo Atlikimo tvarka - čia yra pradinė klasė:
 */
public class VykdymoModulis {

    public static void main(String[] args) {
        try {
            Locale.setDefault(Locale.US); // suvienodiname skaičių formatus
            PlaceTest.seriesTest();
            MainWindow.createAndShowGUI();
        } catch (MyException ex) {
            Ks.ou(ex.getMessage());
        }
    }
}