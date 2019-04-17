package com.demo.induction.tp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

public class XmlTransactionProcessorTest {
private XmlTransactionProcessor xmlTransactionProcessor;

    @Test
    @DisplayName("Should test all the transaction processor methods and add violation description if exists inside xml file")
    public void shouldThrowExceptionIfInvalidTransactionType() throws IOException {
        this.readXml();
    }

private void readXml() throws IOException {
    xmlTransactionProcessor = new XmlTransactionProcessor();
    InputStream stream = null;
    try {
        stream = new FileInputStream(new File("/home/dipak/Documents/Financial transaction error.xml"));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    xmlTransactionProcessor.importTransactions(stream);
    xmlTransactionProcessor.getImportedTransactions();
    xmlTransactionProcessor.validate();
    boolean b = xmlTransactionProcessor.isBalanced();
    if (b) {
        System.out.println("Provided Transaction Is Balanced");
    } else {
        System.out.println("Provided Transaction Is Not Balanced");
    }
}
}
