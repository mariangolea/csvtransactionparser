package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroup;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author mariangolea@gmail.com
 */
public final class TransactionGroupCellRenderer extends JLabel implements ListCellRenderer<BankTransactionGroup> {

    public TransactionGroupCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends BankTransactionGroup> list,
                                                  BankTransactionGroup value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = "<html><font color=\"blue\">" + value.getGroupIdentifier() + "</font><br>";
        text += value.getTotalAmount().floatValue() + "<br></html>";
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
