package com.thinking.machines.tcp.server;
import java.io.*;
import com.thinking.machines.tcp.server.event.*;
import com.thinking.machines.tcp.server.pojo.*;
import java.net.*;
import java.util.*;

public class TCPServer implements ProcessListener
{
private ServerSocket serverSocket;
private int portNumber;
private TCPListener tcpListener;
private HashMap<String,Processor> processors=new HashMap<>();
public TCPServer(int portNumber)
{
this.serverSocket=null;
this.portNumber=portNumber;
}
public void start(TCPListener tcpListener) throws IOException
{
this.tcpListener=tcpListener;
this.serverSocket=new ServerSocket(this.portNumber);
this.startAccepting();
}
private void startAccepting() throws IOException
{
Socket socket;
Processor processor;
String processorId;
while(true)
{
socket=serverSocket.accept();
processorId=java.util.UUID.randomUUID().toString();
processor=new Processor(socket,this,tcpListener,processorId);
processors.put(processorId,processor);

processor.start();
}
}

public void onCompleted(Processor processor)
{
processors.remove(processor.getId());
}

}