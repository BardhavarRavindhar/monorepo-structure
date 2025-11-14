package com.example.wallet.service;

import com.example.wallet.model.Transaction;
import com.example.wallet.model.Wallet;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);

    /**
     * Generates a month-wise transaction history PDF for a specific month and year
     */
    public byte[] generateMonthlyTransactionPdf(
            Wallet wallet,
            List<Transaction> transactions,
            int year,
            int month) throws DocumentException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add title and header information
        addDocumentHeader(document, wallet, year, month);

        // Group transactions by day and add to document
        addTransactionsTable(document, transactions);

        // Add summary section
        addSummarySection(document, transactions);

        document.close();
        return outputStream.toByteArray();
    }

    private void addDocumentHeader(Document document, Wallet wallet, int year, int month) throws DocumentException {
        // Title
        Paragraph title = new Paragraph("Experta Transaction History", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Wallet info table
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);

        // Month and Year
        YearMonth yearMonth = YearMonth.of(year, month);
        String monthYear = yearMonth.getMonth().toString() + " " + yearMonth.getYear();

        addInfoRow(infoTable, "Period:", monthYear);
        addInfoRow(infoTable, "Wallet Owner:", wallet.getOwner());
        addInfoRow(infoTable, "Wallet ID:", wallet.getId());
        addInfoRow(infoTable, "Currency:", wallet.getCurrency());
        addInfoRow(infoTable, "Current Balance:", wallet.getBalance().toString());
        addInfoRow(infoTable, "Generated On:",
                LocalDateTime.now().format(DATE_FORMATTER));

        document.add(infoTable);
        document.add(Chunk.NEWLINE);
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Changed from ALIGN_RIGHT to ALIGN_LEFT

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.ALIGN_LEFT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTransactionsTable(Document document, List<Transaction> transactions) throws DocumentException {
        Paragraph heading = new Paragraph("Transaction Details", HEADER_FONT);
        heading.setSpacingBefore(20);
        heading.setSpacingAfter(10);
        document.add(heading);

        if (transactions.isEmpty()) {
            document.add(new Paragraph("No transactions found for this period.", NORMAL_FONT));
            return;
        }

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Set column widths
        float[] columnWidths = {2.5f, 1.5f, 1f, 1.5f, 3.5f};
        table.setWidths(columnWidths);

        // Add table headers
        addTableHeader(table);

        // Sort transactions by date
        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .toList();

        // Add transaction data
        for (Transaction tx : sortedTransactions) {
            addTransactionRow(table, tx);
        }

        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Date & Time", "Type", "Amount", "Balance", "Description"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(new Color(211, 211, 211)); // Light gray
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addTransactionRow(PdfPTable table, Transaction tx) {
        // Date
        PdfPCell dateCell = new PdfPCell(new Phrase(
                tx.getTimestamp().format(DATE_FORMATTER), NORMAL_FONT));

        // Type
        PdfPCell typeCell = new PdfPCell(new Phrase(tx.getType().toString(), NORMAL_FONT));

        // Amount (color based on type)
        Font redFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        redFont.setColor(Color.RED);

        Font greenFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        greenFont.setColor(Color.GREEN);

        Phrase amountPhrase;
        if (tx.getType() == Transaction.TransactionType.WITHDRAWAL) {
            amountPhrase = new Phrase("-" + tx.getAmount().toString(), redFont);
        } else if (tx.getType() == Transaction.TransactionType.DEPOSIT) {
            amountPhrase = new Phrase("+" + tx.getAmount().toString(), greenFont);
        } else {
            amountPhrase = new Phrase(tx.getAmount().toString(), NORMAL_FONT);
        }

        PdfPCell amountCell = new PdfPCell(amountPhrase);

        // Balance
        PdfPCell balanceCell = new PdfPCell(new Phrase(
                tx.getBalanceAfter().toString(), NORMAL_FONT));

        // Description
        PdfPCell descCell = new PdfPCell(new Phrase(tx.getDescription(), NORMAL_FONT));

        // Add cells to table
        table.addCell(dateCell);
        table.addCell(typeCell);
        table.addCell(amountCell);
        table.addCell(balanceCell);
        table.addCell(descCell);
    }

    private void addSummarySection(Document document, List<Transaction> transactions) throws DocumentException {
        Paragraph heading = new Paragraph("Summary", HEADER_FONT);
        heading.setSpacingBefore(20);
        heading.setSpacingAfter(10);
        document.add(heading);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(70);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        // Calculate summary values
        BigDecimal totalDeposits = transactions.stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.DEPOSIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWithdrawals = transactions.stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.WITHDRAWAL)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netChange = totalDeposits.subtract(totalWithdrawals);

        // Add summary rows
        Font greenFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        greenFont.setColor(Color.GREEN);

        Font redFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        redFont.setColor(Color.RED);

        addSummaryRow(table, "Total Deposits:", totalDeposits.toString(), greenFont);
        addSummaryRow(table, "Total Withdrawals:", totalWithdrawals.toString(), redFont);

        Font netChangeFont = netChange.compareTo(BigDecimal.ZERO) >= 0 ? greenFont : redFont;
        addSummaryRow(table, "Net Change:", netChange.toString(), netChangeFont);

        document.add(table);
    }

    private void addSummaryRow(PdfPTable table, String label, String value, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}