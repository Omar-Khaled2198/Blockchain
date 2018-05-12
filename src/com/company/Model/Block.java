package com.company.Model;

import com.company.Utilization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Block implements Serializable
{
    public String hash;
    public String previousHash;
    public long timeStamp;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Block(String prevHash)
    {
        this.timeStamp=new Date().getTime();
        this.previousHash=prevHash;
        this.hash=generateHash();
    }

    public String generateHash()
    {
        String input=previousHash+merkleRoot+Long.toString(timeStamp);
        return Utilization.SHA256(input);
    }


    public void mineBlock(int difficulty)
    {
        System.out.println("Mining......");
        merkleRoot= Utilization.getMerkleRoot(transactions);
        String target =new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring( 0, difficulty).equals(target))
        {
            hash=generateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    public void addTransaction(Transaction transaction)
    {
        if(transaction == null)
        {
            System.out.println("Transaction failed to process. Discarded.");
            return;
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
    }

    public boolean isValid()
    {
        for(int i=0;i<transactions.size();i++)
        {
            if(!transactions.get(i).verifiySignature())
                return false;
        }
        return true;
    }




}