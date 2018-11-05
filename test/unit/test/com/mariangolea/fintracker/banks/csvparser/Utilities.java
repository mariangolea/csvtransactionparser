package test.com.mariangolea.fintracker.banks.csvparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania.BTParser;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.ing.INGParser;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.csv.CSVFormat;
import org.junit.rules.TemporaryFolder;

public class Utilities {

    public static String createFolder(TemporaryFolder folder, final String folderName) {
        try {
            return folder.newFolder("test").getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String[] constructMockCSVContentForING() {
        INGParser parser = new INGParser();
        List<String> texts = new ArrayList<>();
        texts.add("Gibberish");
        texts.add("More Gibberish");
        texts.add(parser.bank.transactionsNumberLabel + "," + 2 + " Tranzactii.");
        texts.add(parser.bank.relevantContentHeaderLine);
        List<String> mockData = constructSimplestPositiveLinesInputING();
        for (String line : mockData) {
            texts.add(line);
        }
        texts.add("Gibberish");

        return texts.toArray(new String[texts.size()]);
    }

    public String[] constructMockCSVContentForBT() {
        BTParser parser = new BTParser();
        List<String> texts = new ArrayList<>();
        texts.add("Gibberish");
        texts.add("More Gibberish");
        texts.add(parser.bank.transactionsNumberLabel + "," + 2 + " Tranzactii.");
        texts.add(parser.bank.relevantContentHeaderLine);
        List<String> mockData = constructSimplestPositiveLinesInputBT();
        for (String line : mockData) {
            texts.add(line);
        }
        texts.add("Gibberish");

        return texts.toArray(new String[texts.size()]);
    }

    public static Collection<BankTransaction> constructMockDefaultTransactionsForCategorizer(final UserPreferencesInterface userPrefs) {
        Collection<BankTransaction> transactions = new ArrayList<>();
        populateUserPrefsWithCompanyAndGroupData(userPrefs);

        transactions.add(createTransaction(createDate(6, 2016), BigDecimal.ZERO, BigDecimal.ONE, "  Carrefour SRL  Romania"));
        transactions.add(createTransaction(createDate(1, 2017), BigDecimal.ZERO, BigDecimal.ONE, "Auchan Romania SRL"));
        transactions.add(createTransaction(createDate(5, 2018), BigDecimal.ZERO, BigDecimal.ONE, "Limited Petrom SA"));
        transactions.add(createTransaction(createDate(5, 2018), BigDecimal.TEN, BigDecimal.ZERO, "Employer Company SRL"));
        transactions.add(createTransaction(createDate(5, 2018), BigDecimal.TEN, BigDecimal.ZERO, "Auchan Romania"));
        transactions.add(createTransaction(createDate(2, 2019), BigDecimal.TEN, BigDecimal.ZERO, "Employer Company SRL"));
        transactions.add(createTransaction(createDate(2, 2016), BigDecimal.ZERO, BigDecimal.ONE, "  No Category..."));

        return transactions;
    }

    public static void populateUserPrefsWithCompanyAndGroupData(final UserPreferencesInterface userPrefs) {
        userPrefs.setCompanyDisplayName("  Carrefour SRL ", "Carrefour");
        userPrefs.setCompanyDisplayName("Petrom SA", "Petrom");
        userPrefs.setCompanyDisplayName("Auchan Romania", "Auchan");
        userPrefs.setCompanyDisplayName("Employer Company SRL", "Employer");

        userPrefs.appendDefinition("Food", createList("Auchan", "Carrefour"));
        userPrefs.appendDefinition("Fuel", createList("Petrom"));
        userPrefs.appendDefinition("Existential", createList("Food", "Fuel"));
        userPrefs.appendDefinition("Revenues", createList("Employer"));
    }

    public static Date createDate(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        //depending on when these tests are ran, day int might be larger than supported by set month.
        //so set a day that is supported by all months!
        cal.set(Calendar.DAY_OF_MONTH, 2);
        return cal.getTime();
    }

    public static Date createDate(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        //depending on when these tests are ran, day int might be larger than supported by set month.
        //so set a day that is supported by all months!
        cal.set(Calendar.DAY_OF_MONTH, 2);
        return cal.getTime();
    }

    public static BankTransaction createTransaction(final Date completed, final BigDecimal credit, final BigDecimal debit, final String description) {
        return new BankTransaction(completed, completed, credit, debit, description, Arrays.asList("one", "two"));
    }

    public File writeCSVFile(File csv, final String... records) {
        try (BufferedWriter printer = new BufferedWriter(new FileWriter(csv))) {
            for (String record : records) {
                printer.write(record);
                CSVFormat.EXCEL.getRecordSeparator();
                printer.write(CSVFormat.EXCEL.getRecordSeparator());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return csv;
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

    public static Collection<String> createList(final String... args) {
        Collection<String> list = new ArrayList<>();
        for (String arg : args) {
            list.add(arg);
        }

        return list;
    }
}
