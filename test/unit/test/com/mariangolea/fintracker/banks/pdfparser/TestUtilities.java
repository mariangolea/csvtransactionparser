/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import static org.junit.Assert.assertTrue;
import test.com.mariangolea.fintracker.banks.pdfparser.ui.PdfParserUICategorizerTest;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class TestUtilities {

    public List<String> constructSimplestPositiveLinesInput() {
        //length needs to cover for correct header, all operations, and a extra useless string which has to be recognized as such.
        List<String> lines = new ArrayList<>();
        String completedDate = "18 august 2018";
        String startedDate = "16 august 2018";
        String amount = "1.230,6";
        BankTransaction.Type operationType = BankTransaction.Type.OUT;
        int extraLines = 0;
        BankTransaction transaction = null;
        String mainLine = "";
        for (INGParser.OperationID operation : INGParser.OperationID.values()) {
            mainLine = amount + operation.desc + completedDate;
            switch (operation) {
                case RATE_CREDIT:
                    extraLines = 1;
                    break;
                case CASH_WITHDRAWAL:
                    extraLines = 3;
                    break;
                case ASIGURARI_GENERALE:
                    extraLines = 4;
                    break;
                case INCASARE:
                    mainLine = completedDate + operation.desc + amount;
                    operationType = BankTransaction.Type.IN;
                    extraLines = 4;
                    break;
                case COMISION:
                    extraLines = 1;
                    break;
                case PRIMA_ASIGURARE_ING:
                    extraLines = 1;
                    break;
                case PRIMA_ASIGURARE_VIATA:
                    extraLines = 0;
                    break;
                case RECALCULARI_CREDITE:
                    extraLines = 3;
                    break;
                case TRANSFER_HOMEBANK:
                    extraLines = 5;
                    break;
                default:
                    assertTrue("Unhandled operation ID found, need to update test!", false);
                    break;
            }
            lines.add(mainLine);
            for (int i = 0; i < extraLines; i++) {
                lines.add("Point less");
            }
        }

        lines.add("Pointless");
        return lines;
    }

    public String constructMockFirstPDFPageContent(Bank bank) {
        List<String> mock = constructSimplestPositiveLinesInput();
        String mockPDFPage = "Gibberish\\r\\n";
        mockPDFPage = mockPDFPage.concat(bank.swiftCode).concat("\\r\\n");
        mockPDFPage = mockPDFPage.concat(bank.relevantContentHeaderLine).concat("\\r\\n");
        for (String line : mock) {
            mockPDFPage = mockPDFPage.concat(line).concat("\\r\\n");
        }
        mockPDFPage = mockPDFPage.concat("Gibberish\\r\\n");

        return mockPDFPage;
    }

    public File constructSimplestPositiveLinesInputPDFFile(Bank bank) {
        String mock = constructMockFirstPDFPageContent(bank);
        try {
            File temp = File.createTempFile("test", "mock");
            try (FileWriter writer = new FileWriter(temp)) {
                writer.write(mock);
            }
            return constructPDFSinglePageFile(temp);

        } catch (IOException ex) {
            Logger.getLogger(PdfParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue("Could not create a temporary file or failed to write file contents", false);
        }

        return null;
    }

    /**
     * Creates a sibling PDF file.
     *
     * @param text initial text file
     * @return sibling pdf file, may be null
     */
    public PDDocument constructPDFDocument(String text) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        COSStream stream = new COSStream();
        PDStream pdStream = new PDStream(stream);
        try (PrintWriter os = new PrintWriter(new BufferedOutputStream(stream.createOutputStream()))) {
            os.print(text);
        } catch (IOException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        page.setContents(pdStream);
        document.addPage(page);
        return document;
    }

    public File constructPDFSinglePageFile(File textFile) throws IOException, FileNotFoundException {
        String text;
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            text = "";
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        }
        File pdfLocation;
        try (PDDocument pdfDoc = constructPDFDocument(text)) {
            pdfLocation = new File(textFile.getParentFile(), "textFile" + ".pdf");
            pdfDoc.save(pdfLocation);
            pdfDoc.close();
        }

        return pdfLocation;
    }
}
