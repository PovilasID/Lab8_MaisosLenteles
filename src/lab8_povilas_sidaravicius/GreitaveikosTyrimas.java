package lab8_povilas_sidaravicius;

import LaboraiDemo.*;
import GUI.*;
import GUI.KsSwing.MyException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JTextArea;
import studijosKTU.*;

/**
 * @author eimutis
 */
public class GreitaveikosTyrimas extends Thread {
    
    static ResourceBundle rb;
    static JTextArea ta;
    static JButton[] btns;
    static String[] tyrimųVardai = {"add0.75", "add0.25", "rem0.75", "rem0.25", "get0.75", "get0.25"};
    static int[] tiriamiKiekiai = {10000, 20000, 40000, 80000};
    static MapKTUx<String, Place> pMap =
            new MapKTUx(new String(), new Place(), 10, 0.75f, MapKTU.HashType.Division);
    static MapKTUx<String, Place> pMap2 =
            new MapKTUx(new String(), new Place(), 10, 0.25f, MapKTU.HashType.Division);
    static Queue<String> chainsSizes = new LinkedList<String>();
    static String str;
    static PlaceGenerator gamyba = new PlaceGenerator();
    
    public GreitaveikosTyrimas(JTextArea ta, JButton[] btns, ResourceBundle rb) {
        GreitaveikosTyrimas.rb = rb;
        GreitaveikosTyrimas.ta = ta;
        GreitaveikosTyrimas.btns = btns;
    }
    
    @Override
    public void run() {
        chainsSizes.clear();
        for (JButton btn : btns) {
            btn.setEnabled(false);
        }
        SisteminisTyrimas();
        for (JButton btn : btns) {
            btn.setEnabled(true);
        }
    }
    
    public void SisteminisTyrimas() {
        try {
            Timekeeper tk = new Timekeeper(tiriamiKiekiai, ta);
            chainsSizes.add("   kiekis      " + tyrimųVardai[0] + "   " + tyrimųVardai[1]);
            for (int k : tiriamiKiekiai) {
                Place[] autoArray = gamyba.makeAndSell(k, k);
                String[] autoIdArray = gamyba.grazintiAutoIDMasyva();
                pMap.clear();
                pMap2.clear();
                tk.startAfterPause();
                tk.start();
                
                for (Place a : autoArray) {
                    pMap.put(gamyba.formuotiAutoID(), a);
                }
                tk.finish(tyrimųVardai[0]);
                
                str = "   " + k + "          " + pMap.getMaxChainSize();
                for (Place a : autoArray) {
                    pMap2.put(gamyba.formuotiAutoID(), a);
                }
                tk.finish(tyrimųVardai[1]);
                str += "         " + pMap2.getMaxChainSize();
                chainsSizes.add(str);
                for (String s : autoIdArray) {
                    pMap.remove(s);
                }
                tk.finish(tyrimųVardai[2]);
                for (String s : autoIdArray) {
                    pMap2.remove(s);
                }
                tk.finish(tyrimųVardai[3]);
                for (String s : autoIdArray) {
                    pMap2.get(s);
                }
                tk.finish(tyrimųVardai[4]);
                for (String s : autoIdArray) {
                    pMap2.get(s);
                }
                tk.finish(tyrimųVardai[5]);
                tk.seriesFinish();
            }
            KsSwing.oun(ta, "", rb.getStringArray("msgs")[7]);            
            KsSwing.setFormatStartOfLine(false);
            KsSwing.oun(ta, chainsSizes);
            KsSwing.setFormatStartOfLine(true);
        } catch (MyException e) {
            KsSwing.oun(ta, e.getMessage());
        }
    }
}
