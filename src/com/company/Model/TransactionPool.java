package com.company.Model;

import java.util.*;

public class TransactionPool
{
    public LinkedHashMap<String, Transaction> transactions;

    public TransactionPool()
    {
        this.transactions = new LinkedHashMap<>();
    }


    public void clear()
    {
        transactions.clear();
    }

    public void updateOrAddTransaction(Transaction transaction)
    {
        transactions.put(transaction.transactionId, transaction);
    }

    public Transaction existingTransaction(String transactionId)
    {
        for(Map.Entry<String, Transaction> entry : transactions.entrySet())
        {
            if(entry.getKey().equals(transactionId))
            {
                return entry.getValue();
            }
        }
        return null;
    }

    public ArrayList<Transaction> getTransactionList()
    {
        ArrayList<Transaction> res = new ArrayList<>();
        int i = 0;
        try
        {
            Iterator<Map.Entry<String, Transaction>> itr = transactions.entrySet().iterator();
            while(itr.hasNext()) {
                Map.Entry<String, Transaction> entry = itr.next();
                Transaction cur = entry.getValue();
                    res.add(cur);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void updateTransactionPool(ArrayList<Transaction> validTransactions)
    {
        for(Transaction transaction : validTransactions)
        {
            if(this.transactions.containsKey(transaction.transactionId))
            {
                this.transactions.remove(transaction.transactionId);
            }
        }
    }

    public void printTransactions()
    {
        if(transactions.size()>0){
        for(Map.Entry<String, Transaction> entry : transactions.entrySet())
        {
            System.out.printf("From: %s\n",Base64.getEncoder().encodeToString(transactions.get(entry.getKey()).sender.getEncoded()));
            System.out.printf("To: %s\n",Base64.getEncoder().encodeToString(transactions.get(entry.getKey()).receiver.getEncoded()));
            System.out.printf("Value: %f\n",transactions.get(entry.getKey()).value);
        }}
    }
}
