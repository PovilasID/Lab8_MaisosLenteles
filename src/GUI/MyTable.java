package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import studijosKTU.MapADTx;
import studijosKTU.HashTableModel;

/**
 * @author PovilasSid
 */
//Klaseje specifikuota apibreztas savybes turinti lentele.
//Naudojama maisos lenteles atvaizdavimui.
public class MyTable extends JTable {

    private int colWidth; //Stulpeliu plotis

    public MyTable() {
    }

    public void setMyTable(MapADTx map, int columnCount, int rowCount, int colWidth, String delimiter) {
        if (map == null) {
            throw new NullPointerException("Map is null");
        }
        if (columnCount <= 0) {
            throw new IllegalArgumentException("Table column count is <=0: " + columnCount);
        }
        if (rowCount <= 0) {
            throw new IllegalArgumentException("Table row Count is <=0: " + rowCount);
        }
        if (colWidth <= 0) {
            throw new IllegalArgumentException("Table column width is <=0: " + colWidth);
        }
        this.colWidth = colWidth;
    
        HashTableModel model = map.getModel(delimiter);
        setModel(model);
        appearance();
    }

    private void appearance() {
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0) {
                getColumnModel().getColumn(i).setPreferredWidth(55);
            } else {
                getColumnModel().getColumn(i).setMaxWidth(colWidth);
                getColumnModel().getColumn(i).setMinWidth(colWidth);
            }
        }
        //Lenteles antrastes
        getTableHeader().setResizingAllowed(false);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        //Celes stilius - Pacentruojame
        DefaultTableCellRenderer toCenter = new DefaultTableCellRenderer();
        toCenter.setHorizontalAlignment(JLabel.CENTER);
        //Nustatome nulinio stulpelio celiu stiliu
        getColumnModel().getColumn(0).setCellRenderer(toCenter);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        //Iscentruojamos antrastes
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        //Nustatomas tooltips'u rodymas
        String value = (String) getValueAt(row, column);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.setToolTipText(value);
        }
        //Zaliai nuspalvinamos celes, kuriose kas nors irasyta
        if (value != null && !value.equals("")) {
            c.setBackground(new Color(204, 255, 204));
        } //Baltai - likusios celes
        else {
            c.setBackground(Color.WHITE);
        }
        return c;
    }
}