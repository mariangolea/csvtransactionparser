package com.mariangolea.fintracker.banks.pdfparser.parsers;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTextReportParser;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction.Type;
import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;

/**
 * PDF parser for ING bank.
 *
 * @author mariangolea@gmail.com
 */
public class INGParser implements BankTextReportParser {

	private enum OperationID {
		CASH_WITHDRAWAL("Retragere numerar"), RATE_CREDIT("Rata Credit"), ASIGURARI_GENERALE(
				"Prima asigurari generale"), INCASARE("Incasare"), COMISION(
						"Comision plata anticipata"), RECALCULARI_CREDITE(
								"Recalculari aferente creditului"), TRANSFER_HOMEBANK(
										"Transfer Home'Bank"), PRIMA_ASIGURARE_ING(
												"Prima asigurare ING Credit Protect"), PRIMA_ASIGURARE_VIATA(
														"Prima asigurare de viata (credite)");

		private final String desc;

		private OperationID(String desc) {
			this.desc = desc;
		}

		private static OperationID getOperationID(final String operationLine) {
			for (OperationID id : OperationID.values()) {
				if (operationLine.contains(id.desc)) {
					return id;
				}
			}

			return null;
		}
	}

	public static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO")
			.setLanguageTag("ro-RO").build();

	private static final String COLUMN_NAMES_LINE = "Debit CreditDetalii tranzactieData";
	private final NumberFormat numberFormat = NumberFormat.getInstance(ROMANIAN_LOCALE);
	private final DateFormat romanianDateFormat = DateFormat.getDateInstance(DateFormat.LONG, ROMANIAN_LOCALE);
	private final DateFormat startDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public PdfPageParseResponse parseTransactions(String pdfPage) {
		PdfPageParseResponse response = new PdfPageParseResponse();
		Map<String, List<BankTransaction>> result = new HashMap<>();
		for (OperationID operation : OperationID.values()) {
			result.put(operation.desc, new ArrayList<>());
		}

		String relevant = pdfPage.substring(pdfPage.indexOf(COLUMN_NAMES_LINE) + COLUMN_NAMES_LINE.length());
		String[] split = relevant.split("\\r\\n");
		List<String> unrecognizedStrings = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			String temp = split[i];
			if (temp.isEmpty()) {
				continue;
			}
			OperationID operation = OperationID.getOperationID(temp);
			if (operation != null) {
				String[] operationDetails = temp.split(operation.desc);
				if (operation == OperationID.PRIMA_ASIGURARE_VIATA) {
					operationDetails = temp.split("Prima asigurare de viata \\(credite\\)");
				}
				Float amount;
				Date completedDate;
				Date startDate;
				String desc;
				BankTransaction transaction;
				switch (operation) {
				case RATE_CREDIT:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = operation.desc;
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 1;
					break;
				case CASH_WITHDRAWAL:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					String thirdLine = split[i + 3];
					startDate = parseStartDate(thirdLine.split(" ")[1]);
					desc = split[i + 2];
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 3;
					break;
				case ASIGURARI_GENERALE:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = split[i + 2] + split[i + 3];
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 4;
					break;
				case INCASARE:
					String amountString = operationDetails[1].substring(operationDetails[1].lastIndexOf(" ") + 1);
					amount = parseAmount(amountString);
					completedDate = parseCompletedDate(
							operationDetails[1].substring(0, operationDetails[1].indexOf(amountString)));
					startDate = completedDate;
					desc = split[i + 3];
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.IN);
					result.get(operation.desc).add(transaction);
					i = i + 3;
					break;
				case COMISION:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = operation.desc;
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 1;
					break;
				case PRIMA_ASIGURARE_ING:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = operation.desc;
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 1;
					break;
				case PRIMA_ASIGURARE_VIATA:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = operation.desc;
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					break;
				case RECALCULARI_CREDITE:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = split[i + 2];
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 3;
					break;
				case TRANSFER_HOMEBANK:
					amount = parseAmount(operationDetails[0]);
					completedDate = parseCompletedDate(operationDetails[1]);
					startDate = completedDate;
					desc = split[i + 4];
					transaction = new BankTransaction(operation.desc, completedDate, startDate, amount, desc, Type.OUT);
					result.get(operation.desc).add(transaction);
					i = i + 5;
					break;
				}
			} else {
				unrecognizedStrings.add(temp);
			}
		}

		List<BankTransactionGroup> groups = new ArrayList<>();
		for (String operationID : result.keySet()) {
			List<BankTransaction> transactions = result.get(operationID);
			if (transactions != null && !transactions.isEmpty()) {
				groups.add(new BankTransactionGroup(operationID, result.get(operationID)));
			}
		}

		response.transactionGroups.addAll(groups);
		response.unrecognizedStrings.addAll(unrecognizedStrings);
		return response;
	}

	protected Date parseCompletedDate(final String dateString) {
		Date completedDate = null;
		try {
			completedDate = romanianDateFormat.parse(dateString);
		} catch (ParseException ex) {
			Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return completedDate;
	}

	protected Date parseStartDate(final String dateString) {
		Date completedDate = null;
		try {
			completedDate = startDateFormat.parse(dateString);
		} catch (ParseException ex) {
			Logger.getLogger(INGParser.class.getName()).log(Level.SEVERE, null, ex);
		}

		return completedDate;
	}

	protected Float parseAmount(final String amountString) {
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
