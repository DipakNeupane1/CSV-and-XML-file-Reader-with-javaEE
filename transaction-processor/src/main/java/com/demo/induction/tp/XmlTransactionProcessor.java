package com.demo.induction.tp;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class XmlTransactionProcessor implements TransactionProcessor {
    List<Element> list = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();
    List<Violation> violations = new ArrayList<>();

    @Override
    public void importTransactions(InputStream is) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(is);
            NodeList nodes = document.getElementsByTagName("Transaction");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node tranNodes = nodes.item(i);
                if (tranNodes.getNodeType() == Node.ELEMENT_NODE) {
                    Element tranElement = (Element) tranNodes;
                    list.add(tranElement);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getImportedTransactions() {
        for (Element s : list) {
            String tranType = s.getAttribute("type").toString();
            String tranAmount = s.getAttribute("amount").toString();
            String narration = s.getAttribute("narration").toString();
            Transaction transaction = new Transaction();
            BigDecimal decimal = new BigDecimal(tranAmount);
            transaction.setType(tranType);
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
        XmlTransactionProcessor xmlTransactionProcessor = new XmlTransactionProcessor();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("/home/dipak/Documents/Financial transaction.xml"));
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
