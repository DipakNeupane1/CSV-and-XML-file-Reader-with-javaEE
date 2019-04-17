package com.demo.induction.tp;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CsvTransactionProcessor implements TransactionProcessor {
    List<String> list = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();
    List<Violation> violations = new ArrayList<>();

    @Override
    public void importTransactions(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String tranLine;

        while ((tranLine = br.readLine()) != null) {
            this.list.add(tranLine);
            System.out.println("line is" + tranLine);
        }
    }

    @Override
    public List<Transaction> getImportedTransactions() {
        for (String s : list) {
            String[] strings = s.split(",");
            String type = strings[0];
            String number = strings[1];
            String narration = strings[2];
            Transaction transaction = new Transaction();
            BigDecimal decimal = new BigDecimal(number);
            transaction.setType(type);
            transaction.setAmount(decimal);
            transaction.setNarration(narration);
            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public List<Violation> validate() {
        int order = 0;
        for (Transaction transaction : transactions) {
            Violation violation = new Violation();
            if (!(transaction.getType().equalsIgnoreCase("D") || transaction.getType().equalsIgnoreCase("C"))) {
                violation.setOrder(order);
                violation.setProperty(transaction.getType());
                violation.setDescription("Invalidate transaction type at order " + order);
                violations.add(violation);
                System.out.println("violation count at transaction type :" + violation.getDescription());
            }
            if (transaction.getAmount().intValue() <= 0) {
                violation.setOrder(order);
                violation.setProperty(transaction.getAmount().toPlainString());
                violation.setDescription("Invalidate transaction amount at order " + order);
                System.out.println("violation count at transaction amount :" + violation.getDescription());
                violations.add(violation);
            }
            order++;
        }
        return violations;
    }

    @Override
    public boolean isBalanced() {
        int creditAmountSum = 0;
        int debitAmountSum = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("C")) {
                creditAmountSum = creditAmountSum + transaction.getAmount().intValue();
            }
            if (transaction.getType().equalsIgnoreCase("D")) {
                debitAmountSum = debitAmountSum + transaction.getAmount().intValue();
            }
        }
        if (creditAmountSum == debitAmountSum) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        CsvTransactionProcessor csvTransactionProcessor = new CsvTransactionProcessor();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("/home/dipak/Documents/Financial transaction.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        csvTransactionProcessor.importTransactions(stream);
        csvTransactionProcessor.getImportedTransactions();
        csvTransactionProcessor.validate();
        boolean b = csvTransactionProcessor.isBalanced();
        if (b) {
            System.out.println("Provided Transaction Is Balanced");
        } else {
            System.out.println("Provided Transaction Is Not Balanced");
        }
    }
}
