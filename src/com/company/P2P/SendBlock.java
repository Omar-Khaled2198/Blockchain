package com.company.P2P;

import com.company.Model.Block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendBlock extends Thread
{
    private InetAddress group;
    private MulticastSocket socket;
    private int destPort;
    private Block block;

    public SendBlock(int portNumber, Block block) throws IOException
    {
        this.socket  =new MulticastSocket(portNumber);
        this.group = InetAddress.getByName("230.0.0.1");
        this.destPort=portNumber;
        this.block=block;
    }

    public void run()
    {
        System.out.println("SendBlock is running...");

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = null;
                out = new ObjectOutputStream(bos);
                out.writeObject(block);
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
        System.out.println("SendBlock is stopping...");
        socket.close();
    }
}
