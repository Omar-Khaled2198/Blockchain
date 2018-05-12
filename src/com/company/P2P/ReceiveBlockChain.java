package com.company.P2P;

import com.company.Model.BlockChain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

import static com.company.BlockchainApp.blockChainPool;

public class ReceiveBlockChain extends Thread
{
    private int bufLength = 8000;
    private MulticastSocket socket;
    private InetAddress address ;
    private DatagramPacket packet;

    public ReceiveBlockChain(int portNumber)
    {
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
        System.out.println("ReceiveBlockChain is running...");

        while (true)
        {
            try
            {
                byte[] buf = new byte[bufLength];

                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

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
                            BlockChain blockChain = (BlockChain) in.readObject();
                            blockChainPool.add(blockChain);
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
    }

}
