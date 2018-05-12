package com.company.Model;

import com.company.Utilization;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Transaction implements Serializable
{
    public String transactionId;
    public PublicKey sender;
    public PublicKey receiver;
    public float value;
    public byte[] signature;

    public Transaction()
    {

    }
    public Transaction(PublicKey from, PublicKey to, float value)
    {
        this.sender = from;
        this.receiver = to;
        this.value = value;
        this.transactionId=generateId();
    }
    private String generateId()
    {
        String input=Base64.getEncoder().encodeToString(sender.getEncoded())+
                Base64.getEncoder().encodeToString(receiver.getEncoded())+Float.toString(value);
       return Utilization.SHA256(input);
    }

    public void generateSignature(PrivateKey privateKey)
    {
        String data = Base64.getEncoder().encodeToString(sender.getEncoded())+
                Base64.getEncoder().encodeToString(receiver.getEncoded())+Float.toString(value);
        signature = Utilization.ECDSASig(privateKey,data);
    }

    public boolean verifiySignature()
    {
        String data=Base64.getEncoder().encodeToString(sender.getEncoded())+
            Base64.getEncoder().encodeToString(receiver.getEncoded())+Float.toString(value);
        return Utilization.verifySignature(sender, data, signature);
    }



}
