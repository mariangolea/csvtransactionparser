/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class TestUtilities {

	public TemporaryFolder folder = new TemporaryFolder();

	/**
	 * Constructs pdf text content as expected by the parser. <br>
	 * Adds bank swift code, relevant text delimiter, and line separator.
	 *
	 * @param bank
	 *            bank
	 * @return whole pdf page text content
	 */
	public String[] constructMockPDFSinglePageTextContentForBank(Bank bank) {
		List<String> texts = new ArrayList<>();
		texts.add("Gibberish");
		texts.add(bank.swiftCode + "More Gibberish");
		texts.add(bank.relevantContentHeaderLine);
		List<String> mockData = constructSimplestPositiveLinesInput(bank);
		for (String line : mockData) {
			texts.add(line);
		}
		texts.add("Gibberish");

		return texts.toArray(new String[texts.size()]);
	}

	public File writeSinglePagePDFFile(Bank bank, File toCreate) {
		String[] singlePageTexts = constructMockPDFSinglePageTextContentForBank(bank);
		return writePDFFile(bank, toCreate, singlePageTexts);
	}

	public File writeTwoPagesPDFFile(Bank bank, File toCreate) {
		String[] singlePageTexts = constructMockPDFSinglePageTextContentForBank(bank);
		return writePDFFile(bank, toCreate, singlePageTexts, singlePageTexts);
	}

	/**
	 * Writes a single page pdf file with received text.
	 *
	 * @param bank
	 *            bank
	 * @param pagesTexts
	 *            string for each page
	 * @return pdf file on disk, may be null
	 */
	public File writePDFFile(Bank bank, File pdf, final String[]... pagesTexts) {
		PDDocument doc = constructPDFDocument(bank, pagesTexts);

		// verify that a file can be created in temp folder.
		if (pdf == null) {
			return null;
		}
		// delete since it is recreated by pdfbox.
		pdf.delete();

		boolean success = false;
		try {
			doc.save(pdf);
			doc.close();
			success = true;
		} catch (IOException ex) {
			Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
		}
		return success ? pdf : null;
	}

	/**
	 * Creates a sibling PDF file.
	 *
	 * @param text
	 *            initial text file
	 * @return sibling pdf file, may be null
	 */
	private PDDocument constructPDFDocument(Bank bank, String[]... pagesTexts) {
		PDDocument document = new PDDocument();
		try {
			for (String[] pageTexts : pagesTexts) {
				PDPage page = new PDPage();
				document.addPage(page);
				PDFont font = PDType1Font.HELVETICA_BOLD;
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
				contentStream.setFont(font, 12);
				int y = 700;
				int x = 100;
				contentStream.beginText();
				contentStream.newLineAtOffset(100, y);
				// TODO Marian: although new line characters are being written, opening the pdf
				// file for view only shows the first line.
				// Needs some tweaking so that pdf files can also be inspected visually!
				for (String line : pageTexts) {
					contentStream.showText(line);
					y -= 200;
					contentStream.newLineAtOffset(100, y);
				}
				contentStream.endText();
				contentStream.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(TestUtilities.class.getName()).log(Level.SEVERE, null, ex);
		}
		return document;
	}

	/**
	 * Creates a standard set of lines corresponding to 1 of each types of
	 * transactions for a specific bank. <br>
	 * These lines can be written to a text file, then attempted to read from a PDF
	 * file.
	 *
	 * @param bank
	 *            bank
	 * @return list of string lines
	 */
	public List<String> constructSimplestPositiveLinesInput(final Bank bank) {
		switch (bank) {
		case ING:
			return constructSimplestPositiveLinesInputING();
		default:
			throw new RuntimeException("No test support for chosen bank: " + bank.name());
		}
	}

	private List<String> constructSimplestPositiveLinesInputING() {
		// length needs to cover for correct header, all operations, and a extra useless
		// string which has to be recognized as such.
		List<String> lines = new ArrayList<>();
		String completedDate = "18 august 2018";
		String amount = "1.230,6";
		int extraLines = 0;
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
				mainLine = operation.desc + completedDate + " " + amount;
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

			if (INGParser.OperationID.CASH_WITHDRAWAL == operation) {
				lines.add("Point less");
				lines.add("Point less");
				lines.add("Pointless: 18-08-2018");
			} else {
				for (int i = 0; i < extraLines; i++) {
					lines.add("Point less");
				}
			}
		}

		lines.add("Pointless");
		return lines;
	}
}
