/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import org.apache.commons.csv.CSVFormat;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class TestUtilities {

    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Constructs csv text content as expected by the parser. <br>
     * Adds bank swift code, relevant text delimiter, and line separator.
     *
     * @param bank bank
     * @return whole csv page text content
     */
    public String[] constructMockCSVContentForBank(Bank bank) {
        List<String> texts = new ArrayList<>();
        texts.add("Gibberish");
        texts.add("More Gibberish");
        texts.add(bank.transactionsNumberLabel + "," + 2 + " Tranzactii.");
        texts.add(bank.relevantContentHeaderLine);
        List<String> mockData = constructSimplestPositiveLinesInput(bank);
        for (String line : mockData) {
            texts.add(line);
        }
        texts.add("Gibberish");

        return texts.toArray(new String[texts.size()]);
    }

    /**
     * Writes a single page csv file with received text.
     *
     * @param bank bank
     * @param csv file
     * @param records records
     * @return csv file on disk, may be null
     */
    public File writeCSVFile(Bank bank, File csv, final String... records) {
        try (BufferedWriter printer = new BufferedWriter(new FileWriter(csv))) {
            for (String record : records) {
                printer.write(record);
                CSVFormat.EXCEL.getRecordSeparator();
                printer.write(CSVFormat.EXCEL.getRecordSeparator());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return csv;
    }

    /**
     * Creates a standard set of lines corresponding to 1 of each types of
     * transactions for a specific bank. <br>
     * These lines can be written to a text file, then attempted to read from a
     * CSV file.
     *
     * @param bank bank
     * @return list of string lines
     */
    public List<String> constructSimplestPositiveLinesInput(final Bank bank) {
        switch (bank) {
            case BT:
                return constructSimplestPositiveLinesInputBT();
            case ING:
                return constructSimplestPositiveLinesInputING();
            default:
                throw new RuntimeException("No test support for chosen bank: " + bank.name());
        }
    }

    private List<String> constructSimplestPositiveLinesInputBT() {
        // length needs to cover for correct header, all operations, and a extra useless
        // string which has to be recognized as such.
        List<String> lines = new ArrayList<>();
        String first = "2018-05-16,2018-05-16,\"Plata la POS non-BT cu card VISA;POS 12/05/2018 320220120000    TID:20221206 ECONOMAT SECTOR 5 NR  BUCURESTI RO 41196811 valoare tranzactie: 19.74 RON RRN:813207877600   comision tranzactie 0.00 RON;\",746NVPO18136001O,\"-19.74\",,\"-439.22\"";
        String second = "2018-06-07,2018-06-07,\"Incasare OP;/ROC/acoperire card credit BT//RFB/;Golea Marian;RO36INGB0000999903905289;INGBROBU\",000IACH18158C1CS,,\"5,600.00\",\"46.80\"";
        String third = "2018-07-06,2018-07-06,\"Transfer pentru recuperare restante la creditele/debitele in sold\",746ZBTR181875579,\"-1.00\",,\"59.18\"";
        lines.add(first);
        lines.add(second);
        lines.add(third);
        lines.add("Pointless");
        return lines;
    }

    private List<String> constructSimplestPositiveLinesInputING() {
        // length needs to cover for correct header, all operations, and a extra useless
        // string which has to be recognized as such.
        List<String> lines = new ArrayList<>();
        String first = "12 septembrie 2018,,,Cumparare POS,,\"105,00\",\n"
                + ",,,Nr. card: xxxx xxxx xxxx 3811,,,\n"
                + ",,,Terminal: CHAMPION OUTLET DEP  RO  DOMNESTI,,,\n"
                + ",,,Data: 08-09-2018 Autorizare: 001440,,,";
        String second = "14 iunie 2018,,,Rambursare rata card credit,,,\"255,00\"\n"
                + ",,,Ordonator: Golea Alexandra,,,\n"
                + ",,,Din contul: RO43INGB0000999907933185,,,\n"
                + ",,,Detalii: Plata credit,,,\n"
                + ",,,Referinta: 46,,,";
        String third = "15 august 2018,,,Realimentare (debitare directa),,,\"50,00\"\n"
                + ",,,Ordonator: Golea Alexandra,,,\n"
                + ",,,Din contul: RO43INGB0000999907933185,,,\n"
                + ",,,\"Principal: 30,43\",,,\n"
                + ",,,\"Dobanda: 19,57\",,,";
        lines.add(first);
        lines.add(second);
        lines.add(third);
        lines.add("Pointless");
        return lines;
    }
}
