package com.company;

import com.company.Model.*;
import com.company.P2P.*;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockchainApp
{
    public static BlockChain blockChain;
    public static TransactionPool transactionPool;
    public static ArrayList<Peer>peers;
    public static ArrayList<BlockChain>blockChainPool;
    public static Wallet myWallet;
    public static float minimumTransaction = 0.1f;
    public static Peer me;

    public static void main(String[] args) throws IOException
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        blockChain=new BlockChain();
        myWallet = new Wallet();
        transactionPool= new TransactionPool();
        peers=new ArrayList<>();
        blockChainPool=new ArrayList<>();
        Wallet base = new Wallet();
        Transaction genesisTransaction=new Transaction(base.publicKey,myWallet.publicKey,100f);
        genesisTransaction.generateSignature(base.privateKey);
        myWallet.UTXOs.put(genesisTransaction.transactionId,genesisTransaction);
        me=new Peer("Omar",myWallet.publicKey);
        new ReceivePeer(4446).start();
        new ReceiveTransaction(4447).start();
        new ReceiveBlock(4448).start();
        new ReceiveBlockChain(4449).start();
        new SendPeer(4446,me,1).start();
        Scanner scanner=new Scanner(System.in);
        while(true)
        {
            int choice;
            System.out.println("What Do You Want:\n" +
                    "1-Print Blockchain\n" +
                    "2-Print Transactions\n" +
                    "3-Make Transaction\n"+
                    "4-Mining\n" +
                    "5-My Balance\n");
            choice=scanner.nextInt();
            if(choice==1)
            {
                blockChain.Update();
                blockChain.printBlockChain();
            }
            else if(choice==2)
            {
                transactionPool.printTransactions();
            }
            else if(choice==3)
            {
                for(int i=0;i<peers.size();i++)
                    System.out.println(i+1+"-"+peers.get(i).getName());
                System.out.println("Choose Receiver: ");
                choice=scanner.nextInt();
                if(choice>peers.size())
                    continue;
                Peer receiver=null;
                for(int i=0;i<peers.size();i++)
                {
                    if(i==choice-1)
                    {
                        receiver=peers.get(i);
                        break;
                    }
                }
                System.out.println("Enter the Amount: ");
                float amount=scanner.nextFloat();
                Transaction transaction = null;
                if(myWallet.canCreate(amount))
                {

                    transaction=myWallet.newTransaction(receiver.getPublicKey(),amount);
                    transactionPool.updateOrAddTransaction(transaction);
                }
                new SendTransaction(4447,transaction).start();
            }
            else if(choice==4)
            {
                Block block;
                if(blockChain.size()==0)
                    block=new Block("0");
                else
                    block=new Block(blockChain.getPrevHash());

                for(int i=0;i<transactionPool.getTransactionList().size();i++)
                    block.addTransaction(transactionPool.getTransactionList().get(i));

                block.mineBlock(1);
                blockChain.addBlock(block);

                new SendBlock(4448,block).start();
            }
            else if(choice==5)
            {
                System.out.printf("Your Balance: %f\n",myWallet.getBalance());
            }
            System.out.println("=======================================");

        }

    }
}
