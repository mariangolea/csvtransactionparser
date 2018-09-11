package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroup;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

/**
 * Adding up countless small time transactions in a single category is boring.
 * This UI class allows users to see all transactions, group them accordingly
 * and get the total sum easily.
 *
 * @author mariangolea@gmail.com
 */
public class CsvParserUICategorizer extends JPanel {

    protected final JTextPane feedbackPane = new JTextPane();
    private final JTextPane inResponseLabel = new JTextPane();
    private final JTextPane outResponseLabel = new JTextPane();
    private final DefaultListModel<BankTransactionGroup> computerModel = new DefaultListModel<>();
    private JList<BankTransactionGroup> inListView;
    protected final DefaultListModel<BankTransactionGroup> inModel = new DefaultListModel<>();
    protected final DefaultListModel<BankTransactionGroup> outModel = new DefaultListModel<>();
    private JList<BankTransactionGroup> outListView;
    private final List<CsvFileParseResponse> parsedTransactionsCopy = new ArrayList<>();
    private final UserPreferencesHandler preferences = new UserPreferencesHandler();
    private UserPreferences userPrefs;

    protected static final String START_PARSE_MESSAGE = "Started parsing the selected CSV file ...";
    protected static final String FINISHED_PARSING_CSV_FILE = "Finished parsing the CSV file: ";

    public CsvParserUICategorizer() {
        userPrefs = preferences.loadUserPreferences();
        createUI();
    }

    protected void loadData(final List<CsvFileParseResponse> parsedTransactions) {
        inModel.addListDataListener(new TransactionGroupListSelectionListener(inListView, inResponseLabel));
        inListView.addListSelectionListener(new TransactionGroupListSelectionListener(inListView, inResponseLabel));
        outModel.addListDataListener(new TransactionGroupListSelectionListener(outListView, outResponseLabel));
        outListView.addListSelectionListener(new TransactionGroupListSelectionListener(outListView, outResponseLabel));

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
            appendReportText(csvFileResponse);
        });
    }

    private void appendReportText(CsvFileParseResponse fileResponse) {
        if (fileResponse.allOK) {
            return;
        }
        String multiLine = "Found errors when parsing file: \r\n" + fileResponse.csvFile.getAbsolutePath() + "\r\n";
        for (String line : fileResponse.unprocessedStrings) {
            multiLine += line + "\r\n";
        }

        try {
            feedbackPane.getStyledDocument().insertString(feedbackPane.getStyledDocument().getLength(), multiLine, null);
        } catch (BadLocationException ex) {
            Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV file extensions only", "csv", "ps");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File csvFile = chooser.getSelectedFile();
            userPrefs.setCSVInputFolder(csvFile.getParent());
            preferences.storePreferences(userPrefs);
            startParsingCsvFile(csvFile);
        }
    }

    protected void startParsingCsvFile(final File csvFile) {
        try {
            feedbackPane.getStyledDocument().insertString(feedbackPane.getStyledDocument().getLength(), START_PARSE_MESSAGE, null);
        } catch (BadLocationException ex) {
            Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(() -> {
            BankCSVTransactionParser fac = new BankCSVTransactionParser();
            final List<CsvFileParseResponse> res = new ArrayList<>();
            res.add(fac.parseTransactions(csvFile));
            SwingUtilities.invokeLater(() -> {
                try {
                    feedbackPane.getStyledDocument().insertString(feedbackPane.getStyledDocument().getLength(), FINISHED_PARSING_CSV_FILE + csvFile.getAbsolutePath(), null);
                    loadData(res);
                    this.parsedTransactionsCopy.clear();
                    this.parsedTransactionsCopy.addAll(res);

                } catch (BadLocationException ex) {
                    Logger.getLogger(CsvParserUICategorizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }

    private JScrollPane createTransactionView(BankTransaction.Type type) {
        JList<BankTransactionGroup> listView = new JList<>(BankTransaction.Type.IN == type ? inModel : outModel);
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
        return scrollPane;
    }
}
