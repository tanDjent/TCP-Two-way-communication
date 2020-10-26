package com.thinking.machines.tcp.server.pojo;
public class Packet
{
private String clientIP;
private byte[] bytes;

public Packet(String clientIP,byte[] bytes)
{
this.clientIP=clientIP;
this.bytes=bytes;
}

public String getClientIP()
{
return this.clientIP;
}

public byte[] getBytes()
{
return this.bytes;
}
}
