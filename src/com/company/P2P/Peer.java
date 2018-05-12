package com.company.P2P;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.PublicKey;

public class Peer implements Serializable
{
    private String name;
    private PublicKey publicKey;
    private InetAddress address;

    public Peer(String name, PublicKey publicKey)
    {
        this.name = name;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

}
