package com.thinking.machines.tcp.client;
import java.net.*;
import java.io.*;
public class TCPClient
{
private int portNumber;
private String IPAddress;
private Socket socket;
public TCPClient(int portNumber,String IPAddress) throws IOException
{
this.portNumber=portNumber;
this.IPAddress=IPAddress;
this.socket=new Socket(InetAddress.getByName(IPAddress),portNumber);
}
public byte[] send(byte []requestBytes)
{
ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
byte header[]=new byte[10];
InputStream inputStream;
OutputStream outputStream;
int e,f;
int lengthOfRequestBytes;
int byteCount,bytesRead,bytesSent;
int lengthOfResponseBytes;
int numberOfBytesToWrite,bufferSize;
byte responseBytes[],ack[];
byte temp[]=new byte[1024];
bufferSize=1024;
ack=new byte[1];
try{
inputStream=socket.getInputStream();
outputStream=socket.getOutputStream();
//send databytes
lengthOfRequestBytes=requestBytes.length;
e=9;
f=lengthOfRequestBytes;
while(e>=0) {
header[e]=(byte)(f%10);
f=f/10;
e--; }
//send header that contains the length of bytes to be sent
outputStream.write(header,0,10);
outputStream.flush();
inputStream.read(ack);
bytesSent=0;
ack[0]=0;
while(bytesSent<lengthOfRequestBytes) {
numberOfBytesToWrite=bufferSize;
if(bytesSent+bufferSize>lengthOfRequestBytes) {
numberOfBytesToWrite=lengthOfRequestBytes-bytesSent; }
outputStream.write(requestBytes,bytesSent,numberOfBytesToWrite);
outputStream.flush();
inputStream.read(ack);
bytesSent+=bufferSize; }
inputStream.read(header);
ack[0]=1;
outputStream.write(ack,0,ack.length);
outputStream.flush();
lengthOfResponseBytes=0;
f=1;
e=9;
while(e>=0) {
lengthOfResponseBytes=lengthOfResponseBytes+(f*header[e]);
f=f*10;
e--; }
// read response bytes
byteCount=0;
bytesRead=0;
ack=new byte[1];
while(true) {
byteCount=inputStream.read(temp);
if(byteCount<0) break;
bytesRead+=byteCount;
byteArrayOutputStream.write(temp,0,byteCount);
ack[0]=1;
outputStream.write(ack,0,ack.length);
outputStream.flush();
if(bytesRead==lengthOfResponseBytes) break; 
}

responseBytes=byteArrayOutputStream.toByteArray();

return responseBytes;

}catch(IOException ioException) 
{
System.out.println(ioException.getMessage()); 
}
return null; 
}
}