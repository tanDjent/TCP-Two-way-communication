package com.thinking.machines.tcp.server.event;
import com.thinking.machines.tcp.server.pojo.*;
public interface TCPListener
{
public byte[] onBytesReceived(Packet packet);
}
