package com.company.P2P;

import com.company.Model.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendTransaction extends Thread
{

    private InetAddress group;
    private MulticastSocket socket;
    private int destPort;
    private Transaction transaction;

    public SendTransaction(int portNumber, Transaction transaction) throws IOException
    {
        this.socket  =new MulticastSocket(portNumber);
        this.group = InetAddress.getByName("230.0.0.1");
        this.destPort=portNumber;
        this.transaction=transaction;
    }

    public void run()
    {
        System.out.println("SendTransaction is running...");

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(transaction);
            out.flush();
            byte[] data = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, group,destPort);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {

        }
        System.out.println("SendTransaction is stopping...");
        socket.close();
    }
}

