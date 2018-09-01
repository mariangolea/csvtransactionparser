package com.mariangolea.fintracker.banks.pdfparser.ui;

import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;

/**
 *
 * @author mariangolea@gmail.com
 */
public class TransactionGroupCellRenderer extends JLabel implements ListCellRenderer<BankTransactionGroup> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

    public TransactionGroupCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends BankTransactionGroup> list, BankTransactionGroup value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = "<html><font color=\"blue\">" + value.getGroupIdentifier() + "</font><br>";
        text += value.getTotalAmount() + "<br></html>";
        setText(text);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setBorder(new EtchedBorder());
        return this;
    }
}
