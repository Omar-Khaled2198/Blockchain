package com.company.P2P;

import com.company.Model.Transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

import static com.company.BlockchainApp.myWallet;
import static com.company.BlockchainApp.transactionPool;

public class ReceiveTransaction extends Thread
{

    private int bufLength = 1000;
    private MulticastSocket socket;
    private InetAddress address ;
    private DatagramPacket packet;

    public ReceiveTransaction(int portNumber){
        try {
            socket  = new MulticastSocket(portNumber);
            address = InetAddress.getByName("230.0.0.1");
            socket.joinGroup(address);

        } catch (IOException e) {
            System.err.println("Could not create multicastSocket socket.");
        }
    }

    public void run()
    {
        System.out.println("ReceiveTransaction is running...");

        while (true)
        {
            try
            {
                byte[] buf = new byte[bufLength];

                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                //This block of code make the peer not receiving from it self
                Enumeration e= NetworkInterface.getNetworkInterfaces();
                String ipAddress="";
                while (e.hasMoreElements())
                {
                    NetworkInterface n = (NetworkInterface) e.nextElement();
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements())
                    {
                        InetAddress i = (InetAddress) ee.nextElement();
                        ipAddress=i.getHostAddress();
                        if(ipAddress.substring(0,3).equals("192")) break;
                    }
                    if(ipAddress.substring(0,3).equals("192")) break;
                }

                if(!ipAddress.equals(packet.getAddress().toString().substring(1)))
                {
                    ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
                    ObjectInput in = null;

                    try
                    {
                        in = new ObjectInputStream(bis);
                        try
                        {
                            Transaction transaction = (Transaction) in.readObject();
                            if(transaction.verifiySignature())
                            {
                                if(transaction.receiver.equals(myWallet.publicKey))
                                    myWallet.UTXOs.put(transaction.transactionId,transaction);
                                transactionPool.updateOrAddTransaction(transaction);
                                System.out.println("Received Transaction Successfully");
                            }
                            else
                            {
                                System.out.println("Discard Invalid Transaction");
                            }
                        }
                        catch (ClassNotFoundException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    finally
                    {
                        try
                        {
                            if (in != null)
                            {
                                in.close();
                            }
                        }
                        catch (IOException ex)
                        {
                            // ignore close exception
                        }
                    }
                }
                else
                    {

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        //termination
    }

}
