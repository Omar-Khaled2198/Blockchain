package com.company.P2P;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

import static com.company.BlockchainApp.blockChain;
import static com.company.BlockchainApp.me;
import static com.company.BlockchainApp.peers;

public class ReceivePeer extends Thread
{
    private int bufLength = 1000;
    private MulticastSocket socket;
    private InetAddress address ;
    private DatagramPacket packet;

    public ReceivePeer(int portNumber)
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
        System.out.println("ReceivePeer is running...");

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
                            boolean check=false;
                            Peer peer = (Peer) in.readObject();
                            for(int i=0;i<peers.size();i++)
                                if(peer.getName().equals(peers.get(i).getName()))
                                    check=true;
                            if(!check)
                            {
                                peers.add(peer);
                                System.out.println("New User Connected to network: "+peer.getName());
                                new SendPeer(4446,me,2,packet.getAddress()).start();
                                new SendBlockChain(4449,blockChain,packet.getAddress()).start();
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
    }

}
