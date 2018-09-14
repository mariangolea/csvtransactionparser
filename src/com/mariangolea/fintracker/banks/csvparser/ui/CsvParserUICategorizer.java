package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;

/**
 * Adding up countless small time transactions in a single category is boring.
 * This UI class allows users to see all transactions, group them accordingly
 * and get the total sum easily.
 *
 * @author mariangolea@gmail.com
 */
public class CsvParserUICategorizer extends JPanel {

    protected final JEditorPane feedbackPane = new JTextPane();
    private final JTextPane inResponseLabel = new JTextPane();
    private final JTextPane outResponseLabel = new JTextPane();
    protected final DefaultListModel<BankTransactionAbstractGroup> inModel = new DefaultListModel<>();
    protected final DefaultListModel<BankTransactionAbstractGroup> outModel = new DefaultListModel<>();
    private JList<BankTransactionAbstractGroup> inListView;
    private JList<BankTransactionAbstractGroup> outListView;
    private final List<CsvFileParseResponse> parsedTransactionsCopy = new ArrayList<>();
    private final UserPreferencesHandler preferences = new UserPreferencesHandler();
    private final UserPreferences userPrefs;
    private final List<File> parsedCsvFiles = new ArrayList<>();

    protected static final String START_PARSE_MESSAGE = "Started parsing the selected CSV files ...";
    protected static final String FINISHED_PARSING_CSV_FILES = "Finished parsing the CSV files: ";

    public CsvParserUICategorizer() {
        userPrefs = preferences.loadUserPreferences();
        createUI();
    }

    protected void loadData(final List<CsvFileParseResponse> parsedTransactions) {
        inModel.addListDataListener(new TransactionGroupListSelectionListener(inListView, inResponseLabel));
        inListView.addListSelectionListener(new TransactionGroupListSelectionListener(inListView, inResponseLabel));
        outModel.addListDataListener(new TransactionGroupListSelectionListener(outListView, outResponseLabel));
        outListView.addListSelectionListener(new TransactionGroupListSelectionListener(outListView, outResponseLabel));

        if (parsedTransactions != null) {
            parsedTransactions.forEach(csvFileResponse -> {
                csvFileResponse.parsedTransactionGroups.forEach(transactionGroup -> {
                    switch (transactionGroup.getType()) {
                        case IN:
                            inModel.addElement(transactionGroup);
                            break;
                        case OUT:
                            outModel.addElement(transactionGroup);
                            break;
                    }
                });
            });
        }
    }

    private void createUI() {
        setLayout(new GridBagLayout());

        //set up the transaction drop target and computer label.
        createResponseLabels();
        JScrollPane centerScroll = createFeedbackView();

        //set up the IN and OUT transaction scroll areas.
        JScrollPane scrollPaneIN = createTransactionView(BankTransaction.Type.IN);
        JScrollPane scrollPaneOUT = createTransactionView(BankTransaction.Type.OUT);

        //add components to main panel.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
        add(inResponseLabel, constraints);
        constraints.weighty = 1;
        constraints.gridheight = 9;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(scrollPaneIN, constraints);
        constraints.gridx = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 10;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.BOTH;
        add(centerScroll, constraints);
        constraints.weightx = 1;
        constraints.gridx = 6;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        add(outResponseLabel, constraints);
        constraints.weighty = 1;
        constraints.gridy = 1;
        constraints.gridheight = 9;
        constraints.fill = GridBagConstraints.BOTH;
        add(scrollPaneOUT, constraints);

        setPreferredSize(new Dimension(1000, 600));
    }

    private void createResponseLabels() {
        feedbackPane.setContentType("text/html"); // allow copy to clipboard
        feedbackPane.setEditable(false);
        feedbackPane.setBackground(null);
        inResponseLabel.setContentType("text/html"); // allow copy to clipboard
        inResponseLabel.setEditable(false);
        inResponseLabel.setBackground(null);
        inResponseLabel.setBorder(null);
        outResponseLabel.setContentType("text/html"); // allow copy to clipboard
        outResponseLabel.setEditable(false);
        outResponseLabel.setBackground(null);
        outResponseLabel.setBorder(null);
    }

    public JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic('f');
        JMenuItem load = new JMenuItem("Load data from CSV file.");
        load.setMnemonic('l');
        load.addActionListener((ActionEvent e) -> {
            popCSVFileChooser();
        });
        file.add(load);
        menu.add(file);

        return menu;
    }

    private void popCSVFileChooser() {
        String inputFolder = userPrefs.getCSVInputFolder();
        JFileChooser chooser = new JFileChooser(inputFolder);
        chooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV file extensions only", "csv", "ps");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] csvFiles = chooser.getSelectedFiles();
            userPrefs.setCSVInputFolder(csvFiles[0].getParent());
            preferences.storePreferences(userPrefs);
            startParsingCsvFiles(csvFiles);
            parsedCsvFiles.addAll(Arrays.asList(csvFiles));
        }
    }

    protected void startParsingCsvFiles(final File[] csvFiles) {
        appendReportMessage(START_PARSE_MESSAGE);
        SwingUtilities.invokeLater(() -> {
            BankCSVTransactionParser fac = new BankCSVTransactionParser();
            final List<CsvFileParseResponse> res = new ArrayList<>();
            for (File csvFile : csvFiles) {
                CsvFileParseResponse response = fac.parseTransactions(csvFile);
                if (response != null) {
                    res.add(response);
                }
            }
            SwingUtilities.invokeLater(() -> {
                String endParseMessage = FINISHED_PARSING_CSV_FILES;
                appendReportMessage(FINISHED_PARSING_CSV_FILES);
                loadData(res);
                res.forEach((response) -> {
                    appendReportText(response);
                });
                this.parsedTransactionsCopy.clear();
                this.parsedTransactionsCopy.addAll(res);
            });
        });
    }

    private JScrollPane createTransactionView(BankTransaction.Type type) {
        JList<BankTransactionAbstractGroup> listView = new JList<>(BankTransaction.Type.IN == type ? inModel : outModel);
        listView.setOpaque(true);
        listView.setCellRenderer(new TransactionGroupCellRenderer());
        listView.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listView);
        if (BankTransaction.Type.IN == type) {
            inListView = listView;
        } else {
            outListView = listView;
        }
        listView.setSelectionBackground(Color.lightGray);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), type.name(), TitledBorder.CENTER, TitledBorder.TOP));
        scrollPane.setOpaque(true);
        return scrollPane;
    }

    private JScrollPane createFeedbackView() {
        JScrollPane scrollPane = new JScrollPane(feedbackPane);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Feedback", TitledBorder.CENTER, TitledBorder.TOP));
        feedbackPane.addHyperlinkListener((final HyperlinkEvent e) -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                try {
                    File localFile = new File(e.getURL().toURI());
                    java.awt.Desktop.getDesktop().open(localFile);
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        return scrollPane;
    }

    private void appendReportText(CsvFileParseResponse fileResponse) {
        appendHyperlinkToFile("\n", fileResponse.csvFile, ": ");
        if (fileResponse.allCsvContentProcessed && fileResponse.expectedTransactionsNumber == fileResponse.foundTransactionsNumber) {
            appendReportMessage("ALL OK!");
        } else if (!fileResponse.allCsvContentProcessed) {
            String message = "Find below CSV content lines unprocessed.\n";
            for (String line : fileResponse.unprocessedStrings) {
                message += "\t" + line;
            }
            appendReportMessage(message);
        } else {
            if (fileResponse.expectedTransactionsNumber == 0) {
                //coould not validate because CSV content did not specify an expected amount.
                appendReportMessage("CSV content did not specify a expected amount. Parser found " + fileResponse.foundTransactionsNumber + " transactions.");
            } else {
                appendReportMessage("Mismatch between expected and found transaction numbers (expected: " + fileResponse.expectedTransactionsNumber + ", found: " + fileResponse.foundTransactionsNumber + ").");
            }
        }
    }

    private boolean appendHyperlinkToFile(final String pre, final File sourceFile, final String post) {
        if (sourceFile == null) {
            return true;
        }
        Document doc = feedbackPane.getDocument();
        try {
            if (pre != null && !pre.isEmpty()) {
                doc.insertString(doc.getLength(), pre, null);
            }

            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setUnderline(attrs, true);
            StyleConstants.setForeground(attrs, Color.BLUE);
            attrs.addAttribute(sourceFile.getAbsolutePath(), parsedCsvFiles.indexOf(sourceFile));
            attrs.addAttribute(HTML.Attribute.HREF, sourceFile.toURI().toURL());
            doc.insertString(doc.getLength(), sourceFile.getName(), attrs);

            if (post != null && !post.isEmpty()) {
                doc.insertString(doc.getLength(), post, null);
            }

            return true;
        } catch (MalformedURLException | BadLocationException ex) {
            Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean appendReportMessage(final String message) {
        if (message == null) {
            return true;
        }
        Document doc = feedbackPane.getDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", null);
            return true;
        } catch (BadLocationException ex) {
            Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
