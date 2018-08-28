package com.mariangolea.fintracker.banks.pdfparser;

import com.mariangolea.fintracker.banks.pdfparser.ui.PdfParserUICategorizer;
import javax.swing.JFrame;

/**
 * Starts the UI component for parsing bank pdf transaction reports parser.
 * @author mariangolea@gmail.com
 */
public class BankPdfReportsParser {

    public static void main(String[] args) {
        PdfParserUICategorizer ui = new PdfParserUICategorizer();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
    }
}
