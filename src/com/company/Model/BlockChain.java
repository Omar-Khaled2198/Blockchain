package com.company.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;

import static com.company.BlockchainApp.blockChainPool;

public class BlockChain implements Serializable
{
    public ArrayList<Block>blocks;
    public int difficulty;

    public BlockChain()
    {
        blocks = new ArrayList<>();
    }
    public void addBlock(Block block)
    {
        blocks.add(block);
    }
    public void printBlockChain()
    {
        for(int i=0;i<blocks.size();i++)
        {
            System.out.println("{");
            System.out.println("\tBlock#"+(i+1));
            System.out.println("\tHash: "+blocks.get(i).hash);
            System.out.println("\tPrevious Hash: "+blocks.get(i).previousHash);
            for(int x=0;x<blocks.get(i).transactions.size();x++)
            {
                System.out.println("\t{");
                Transaction transaction=blocks.get(i).transactions.get(x);
                System.out.println("\t\tTransaction#"+(x+1));
                String temp=Base64.getEncoder().encodeToString(transaction.sender.getEncoded());
                System.out.println("\t\tSender: "+temp);
                temp=Base64.getEncoder().encodeToString(transaction.receiver.getEncoded());
                System.out.println("\t\tReceiver: "+temp);
                System.out.println("\t\tAmound: "+transaction.value);
                System.out.println("\t}");
            }
            System.out.println("}");
        }
    }
    public boolean isValid()
    {
        Block current,previous;
        for(int i=1;i<blocks.size();i++)
        {
            previous=blocks.get(i-1);
            current=blocks.get(i);
            if(!current.hash.equals(current.generateHash()))
                return false;
            if(!current.previousHash.equals(previous.hash))
                return false;
        }
        return true;
    }

    public int size()
    {
        return blocks.size();
    }

    public String getPrevHash()
    {
        return blocks.get(blocks.size()-1).previousHash;
    }

    public void Update()
    {
        int size=0,index=-1;
        for(int i=0;i<blockChainPool.size();i++)
        {
            if(blockChainPool.get(i).size()>size&&blockChainPool.get(i).isValid())
            {
                index=i;
                size=blockChainPool.get(i).size();
            }
        }
        if(index!=-1)
        {
            blocks=blockChainPool.get(index).blocks;
            blockChainPool.clear();
        }
    }
}
