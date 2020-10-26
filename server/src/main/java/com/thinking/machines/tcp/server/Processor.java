package com.thinking.machines.tcp.server;
import com.thinking.machines.tcp.server.event.*;
import com.thinking.machines.tcp.server.pojo.*;
import java.net.*;
import java.io.*;
public class Processor implements Runnable
{
private ProcessListener processListener;
private Socket socket;
private Thread thread;
private TCPListener tcpListener;
private String _id;
public Processor(Socket socket,ProcessListener processListener,TCPListener tcpListener,String _id)
{
this.socket=socket;
this.processListener=processListener;
this.tcpListener=tcpListener;
this._id=_id;
}
public String getId()
{
return this._id;
}
public void start()
{
thread=new Thread(this);
thread.start();
}
public void run()
{
ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
byte header[]=new byte[10];
InputStream inputStream;
OutputStream outputStream;
int e,f;
int lengthOfRequestBytes;
int byteCount,bytesRead,bytesSent;
int lengthOfResponseBytes,numberOfBytesToWrite;
int bufferSize;
byte requestBytes[],responseBytes[],ack[];
byte temp[]=new byte[1024];
bufferSize=1024;
ack=new byte[1];
try
{
inputStream=socket.getInputStream();
outputStream=socket.getOutputStream();
//read bytes that contain length of request bytes
inputStream.read(header);
lengthOfRequestBytes=0;
f=1;
e=9;
while(e>=0)
{
lengthOfRequestBytes=lengthOfRequestBytes+(f*header[e]);
f=f*10;
e--;
}
ack[0]=1;
outputStream.write(ack,0,ack.length);
outputStream.flush();
//read requestBytes
byteCount=0;
bytesRead=0;
while(true)
{
byteCount=inputStream.read(temp);
if(byteCount<0) break;
bytesRead+=byteCount;
byteArrayOutputStream.write(temp,0,byteCount);
ack[0]=1;
outputStream.write(ack,0,ack.length);
outputStream.flush();
if(bytesRead==lengthOfRequestBytes) break;
}

requestBytes=byteArrayOutputStream.toByteArray();
// determine clientIP

String clientIP=socket.getRemoteSocketAddress().toString();

// create object of packet pack it with clientIP and bytes

Packet packet=new Packet(clientIP,requestBytes);

// invoke onBytesReceived through tcpListener and send responseBytes

responseBytes=this.tcpListener.onBytesReceived(packet);

//send header that contains length of reponseBytes to be sent

lengthOfResponseBytes=responseBytes.length;
e=9;
f=lengthOfResponseBytes;
while(e>=0) {
header[e]=(byte)(f%10);
f=f/10;
e--; }
outputStream.write(header,0,10);
outputStream.flush();
inputStream.read(ack);

// send the responseBytes

bytesSent=0;
ack[0]=0;

while(bytesSent<lengthOfResponseBytes) 
{
numberOfBytesToWrite=bufferSize;
if(bytesSent+bufferSize>lengthOfResponseBytes) {
numberOfBytesToWrite=lengthOfResponseBytes-bytesSent; }
outputStream.write(responseBytes,bytesSent,numberOfBytesToWrite);
outputStream.flush();
inputStream.read(ack);
bytesSent+=bufferSize; 
}
processListener.onCompleted(this);
}catch(Exception exception)
{
System.out.println(exception); //problem
}


}


}