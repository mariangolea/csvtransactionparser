package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author mariangolea@gmail.com
 */
public final class TransactionGroupCellRenderer extends JLabel implements ListCellRenderer<BankTransactionAbstractGroup> {

    public TransactionGroupCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends BankTransactionAbstractGroup> list,
            BankTransactionAbstractGroup value, int index, boolean isSelected, boolean cellHasFocus) {
        String[] split = value.toString().split("\n");

        String text = "<html>";
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                text += "<font color=\"blue\">" + split[i] + "</font>";
            } else {
                text += "<br>" + split[i] + "<br>";
            }
        }
        text += "</html>";
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
