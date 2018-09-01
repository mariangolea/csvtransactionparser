package com.mariangolea.fintracker.banks.pdfparser.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;

/**
 * Uses apache's PDFBox to read the text from a pdf file containing transactions
 * report from any supported banks.
 *
 * @author mariangolea@gmail.com
 */
public final class BankPDFTransactionParser {

	/**
	 * Parse contained bank transactions in associated pdf file object.
	 *
	 * @param file
	 *            has to represent the path to a pdf file containing bank generated
	 *            transactions report.
	 * @return parse response, may be null.
	 */
	public PdfFileParseResponse parseTransactions(final File file) {
		PdfFileParseResponse fileResponse = null;
		try (PDDocument document = loadPDFDocument(file)) {
			fileResponse = constructResponse(document, file);
		} catch (IOException ex) {
			Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return fileResponse;
	}

	protected PdfFileParseResponse constructResponse(final PDDocument document, final File correspondingFile) {
		PdfFileParseResponse fileResponse = null;
		if (document == null) {
			return fileResponse;
		}

		// page indices start at 1.
		String firstPage = getText(document, 1);
		if (firstPage == null) {
			return fileResponse;
		}

		BankTextReportParser parser = null;
		// identify parser based on bank swift code being expected in first page header.
		for (Bank bank : Bank.values()) {
			if (firstPage.contains(bank.swiftCode)) {
				parser = BankPDFParserFactory.getInstance(bank);
				break;
			}
		}

		if (parser != null) {
			List<PdfPageParseResponse> result = new ArrayList<>();
			PdfPageParseResponse response = parser.parseTransactions(firstPage);
			response.setPageNumber(1);
			result.add(response);
			for (int i = 2; i <= document.getNumberOfPages(); i++) {
				firstPage = getText(document, i);
				response = parser.parseTransactions(firstPage);
				response.setPageNumber(i);
				result.add(response);
			}
			fileResponse = new PdfFileParseResponse(correspondingFile, result);
		} else {
			Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null,
					new Exception("No parser defined for file: " + correspondingFile.getAbsolutePath()));
		}

		return fileResponse;
	}

	/**
	 * Load the pdf document content associated to received file.
	 *
	 * @param file
	 *            pdf file
	 * @return may be null
	 */
	public PDDocument loadPDFDocument(final File file) {
		PDDocument pdfDocument = null;
		try {
			pdfDocument = PDDocument.load(file);
		} catch (IOException ex) {
			Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return pdfDocument;
	}

	/**
	 * Get the String content from a specific page number in a pdf document.
	 *
	 * @param pdfDocument
	 *            pdf document content
	 * @param pageNumber
	 *            page number
	 * @return may be null;
	 */
	public String getText(PDDocument pdfDocument, int pageNumber) {
		String text = null;
		try {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(pageNumber);
			pdfStripper.setEndPage(pageNumber);
			text = pdfStripper.getText(pdfDocument);
		} catch (IOException ex) {
			Logger.getLogger(BankPDFTransactionParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return text;
	}
}
