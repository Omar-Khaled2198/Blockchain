package com.company.Model;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.company.BlockchainApp.minimumTransaction;

public class Wallet
{
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public ConcurrentHashMap<String,Transaction> UTXOs = new ConcurrentHashMap<String,Transaction>();

    public Wallet()
    {
        generateKeys();
    }

    public void generateKeys()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public float getBalance()
    {
        float total = 0;
        for (Map.Entry<String, Transaction> item: UTXOs.entrySet())
        {
            Transaction UTXO = item.getValue();
            total += UTXO.value ;
        }
        return total;
    }

    public Transaction newTransaction(PublicKey receiver,float value)
    {
        if(getBalance() < value)
        {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        float total = 0;
        for (Map.Entry<String, Transaction> item: UTXOs.entrySet())
        {
            Transaction UTXO = item.getValue();
            UTXOs.remove(UTXO.transactionId);
            total += UTXO.value;
            if(total >= value) break;
        }

        float remain=total-value;
        Transaction transaction = new Transaction(publicKey, receiver , value);
        transaction.generateSignature(privateKey);
        Transaction change= new Transaction(publicKey,publicKey,remain);
        transaction.generateSignature(privateKey);
        UTXOs.put(change.transactionId,change);
        return transaction;
    }

    public boolean canCreate(float value)
    {
        if(value<minimumTransaction)
        {
            System.out.println("The transaction is too small!");
            return false;
        }
        float total = 0;
        for (Map.Entry<String, Transaction> item: UTXOs.entrySet())
        {
            Transaction UTXO = item.getValue();
            total += UTXO.value;
            if(total >= value) break;
        }
        if(total>=value)
            return true;
        System.out.println("Your balance is not enough");
        return false;
    }

}
