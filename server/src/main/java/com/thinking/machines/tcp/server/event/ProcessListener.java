package com.thinking.machines.tcp.server.event;
import com.thinking.machines.tcp.server.*;
public interface ProcessListener
{
public void onCompleted(Processor processor);
}