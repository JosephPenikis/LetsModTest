package com.crazyjoe.letsmodtest.client.local;

import com.crazyjoe.letsmodtest.utility.LogHelper;

import javax.naming.Context;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPServer
{
    private DatagramPacket packet;
    private MulticastSocket socket;
    static int port = 8000;

    // Used to pause and resume the server
    private boolean isRun = false;
    // Used to start or stop server, as well as maintain the state through pause/resume scenarios
    private boolean isStarted = false;

    private String message = "";
    private int broadcastInterval;
    private Context context;

    public boolean isRunUDPServer() { return isRun; }

    public void pauseBroadcast() { isRun = false; }

    public void endBroadcast() { isStarted = false; }

    public void resumeBroadcast()
    {
        if (isStarted)
        {
            isRun = true;
        }
    }

    public void startBroadcast(int broadcastInterval)
    {
        isRun = true;
        isStarted = true;

        this.broadcastInterval = broadcastInterval;

        new Thread(runner).start();
    }

    Runnable runner = new Runnable() {
        @Override
        public void run()
        {
            while (isStarted)
            {
                try
                {
                    message = "The cake is a lie";
                    sendBroadcastMessageOverWifi(message);
                    Thread.sleep(broadcastInterval);
                }
                catch (InterruptedException e)
                {
                    LogHelper.warn("Server error...");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return;
                }
            }

            if (socket != null)
            {
                socket.disconnect();
                socket.close();
            }

        }
    };

    public UDPServer() {}
    // public UDPServer(Context context) { this.context = context; }

    private InetAddress getBroadcastAddress() throws IOException
    {
        return InetAddress.getLocalHost();
    }

    private InetAddress getLoopbackAddress() throws IOException
    {
        return InetAddress.getLoopbackAddress();
    }

    private InetAddress getGroupAddress() throws IOException
    {
        return InetAddress.getByName("228.5.6.7");
    }

    private void sendBroadcastMessageOverWifi (String message) throws IOException
    {
        InetAddress addr = getLoopbackAddress();

        if (addr != null)
        {
            if (socket == null)
            {
                socket = new MulticastSocket(port);
                socket.joinGroup(getGroupAddress());
                socket.setBroadcast(true);
            }
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(packet);
        }
    }
}
