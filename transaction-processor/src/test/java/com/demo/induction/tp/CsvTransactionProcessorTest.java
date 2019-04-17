package com.demo.induction.tp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

public class CsvTransactionProcessorTest {
    private CsvTransactionProcessor csvTransactionProcessor;

    @Test
    @DisplayName("Should test all the transaction processor methods and add violation description if exists inside csv file")
    public void shouldThrowExceptionIfInvalidTransactionType() throws IOException {
        this.readCsv();
    }

   private void readCsv() throws IOException {
        csvTransactionProcessor = new CsvTransactionProcessor();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("/home/dipak/Documents/Financial transaction.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        csvTransactionProcessor.importTransactions(stream);
        csvTransactionProcessor.getImportedTransactions();
        csvTransactionProcessor.validate();
        boolean b=csvTransactionProcessor.isBalanced();
        if (b){
            System.out.println("Provided Transaction Is Balanced");
        }
        else{
            System.out.println("Provided Transaction Is Not Balanced");
        }
    }
}
