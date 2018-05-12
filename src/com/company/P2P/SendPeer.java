package com.company.P2P;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendPeer extends Thread
{
    private InetAddress group;
    private MulticastSocket socket;
    private int destPort;
    private Peer peer;
    private int type;
    private InetAddress specificPeer;

    public SendPeer(int portNumber, Peer peer,int type,InetAddress specificPeer) throws IOException
    {
        this.socket  =new MulticastSocket(portNumber);
        //this.group = InetAddress.getByName("230.0.0.1");
        this.destPort=portNumber;
        this.peer=peer;
        this.type=type;
        this.specificPeer=specificPeer;
    }

    public SendPeer(int portNumber, Peer peer,int type) throws IOException
    {
        this.socket  =new MulticastSocket(portNumber);
        this.group = InetAddress.getByName("230.0.0.1");
        this.destPort=portNumber;
        this.type=type;
        this.peer=peer;
    }

    public void run()
    {
        System.out.println("SendPeer is running...");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(peer);
            out.flush();
            byte[] data = bos.toByteArray();
            DatagramPacket packet;
            if(type==1)
                packet = new DatagramPacket(data, data.length, group,destPort);
            else
                packet = new DatagramPacket(data, data.length, specificPeer,destPort);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {

        }
        System.out.println("SendPeer is stopping...");
        socket.close();
    }
}
