package com.company.P2P;

import com.company.Model.BlockChain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendBlockChain extends Thread
{
    private InetAddress address;
    private MulticastSocket socket;
    private int  destPort;
    private BlockChain blockChain;

    public SendBlockChain(int portNumber, BlockChain blockChain,InetAddress address) throws IOException
    {
        this.socket  =new MulticastSocket(portNumber);
        this.address = InetAddress.getByName("230.0.0.1");
        this.destPort=portNumber;
        this.blockChain=blockChain;
    }

    public void run()
    {
        System.out.println("SendingBlockChain is running...");

        if(blockChain.blocks.size()>0){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(blockChain);
            out.flush();
            byte[] data = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, address,destPort);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {

        }}
        System.out.println("SendingBlockChain is stopping...");
        socket.close();
    }
}
