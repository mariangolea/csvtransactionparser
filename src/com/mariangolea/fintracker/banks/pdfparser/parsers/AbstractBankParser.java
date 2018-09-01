/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.pdfparser.parsers;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;

/**
 * Common behavior of all bank pdf parsers.
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public abstract class AbstractBankParser implements BankTextReportParser {

	protected static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO")
			.setLanguageTag("ro-RO").build();
	private final DateFormat romanianDateFormat = DateFormat.getDateInstance(DateFormat.LONG, ROMANIAN_LOCALE);

	private final Bank bank;
	private final NumberFormat numberFormat;
	private final DateFormat startDateFormat;

	AbstractBankParser(final Bank bank, final DateFormat startDateFormat, final NumberFormat numberFormat) {
		Objects.requireNonNull(bank);
		Objects.requireNonNull(startDateFormat);
		Objects.requireNonNull(numberFormat);

		this.bank = bank;
		this.startDateFormat = startDateFormat;
		this.numberFormat = numberFormat;
	}

	public abstract List<String> getListOfSupportedTransactionIDs();

	public abstract BankTransaction parseTransaction(List<String> toConsume);

	@Override
	public PdfPageParseResponse parseTransactions(String pdfPage) {
		if (pdfPage == null) {
			return null;
		}
		int relevantLineIndex = pdfPage.indexOf(bank.relevantContentHeaderLine);
		if (relevantLineIndex < 0) {
			return null;
		}

		String relevant = pdfPage.substring(relevantLineIndex + bank.relevantContentHeaderLine.length());
		String[] split = relevant.split(bank.lineSeparator);

		PdfPageParseResponse response = parsePageResponse(Arrays.asList(split));
		return response;
	}

	public PdfPageParseResponse parsePageResponse(List<String> split) {
		PdfPageParseResponse response = new PdfPageParseResponse();
		Map<String, List<BankTransaction>> result = new HashMap<>();
		for (String operation : getListOfSupportedTransactionIDs()) {
			result.put(operation, new ArrayList<>());
		}
		List<String> toConsume = new ArrayList<>(split);
		List<String> unrecognizedStrings = new ArrayList<>();
		BankTransaction transaction;
		int consumedLines;
		while (!toConsume.isEmpty()) {
			transaction = parseTransaction(toConsume);
			if (transaction == null) {
				if (toConsume.get(0) != null && !toConsume.get(0).isEmpty()) {
					unrecognizedStrings.add(toConsume.get(0));
				}
				consumedLines = 1;
			} else {
				result.get(transaction.getTitle()).add(transaction);
				consumedLines = transaction.getOriginalPDFContentLinesNumber();
			}
			toConsume = toConsume.subList(consumedLines, toConsume.size());
		}

		List<BankTransactionGroup> groups = new ArrayList<>();
		result.keySet().forEach((operationID) -> {
			List<BankTransaction> transactions = result.get(operationID);
			if (transactions != null && !transactions.isEmpty()) {
				BankTransactionGroup group = new BankTransactionGroup(bank.swiftCode, operationID,
						transactions.get(0).getType());
				group.addTransactions(transactions);
				groups.add(group);
			}
		});

		response.transactionGroups.addAll(groups);
		response.unrecognizedStrings.addAll(unrecognizedStrings);
		return response;
	}

	public Date parseCompletedDate(final String dateString) {
		Date completedDate = null;
		try {
			completedDate = romanianDateFormat.parse(dateString);
		} catch (ParseException ex) {
			Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return completedDate;
	}

	public Date parseStartDate(final String dateString) {
		Date completedDate = null;
		try {
			completedDate = startDateFormat.parse(dateString);
		} catch (ParseException ex) {
			Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return completedDate;
	}

	public Float parseAmount(final String amountString) {
		Float amount = null;
		try {
			Number attempt = numberFormat.parse(amountString);
			if (null != attempt) {
				amount = attempt.floatValue();
			}
		} catch (ParseException ex) {
			Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return amount;
	}

}
