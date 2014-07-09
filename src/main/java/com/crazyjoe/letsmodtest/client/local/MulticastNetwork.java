package com.crazyjoe.letsmodtest.client.local;

import com.crazyjoe.letsmodtest.utility.LogHelper;

import javax.naming.Context;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastNetwork
{
    private DatagramPacket packet;
    private MulticastSocket transmitter;
    private MulticastSocket receiver;
    static int transmitterPort = 8000;
    static int receiverPort = 8001;

    // Used to pause/resume the server
    private boolean isNetworkRunning = false;
    // Used to start or stop the server, as well as maintain the state through pause/resume scenarios
    private boolean isNetworkStarted = false;

    private String message = null;
    private int broadcastInterval;
    private Context context;

    public boolean isNetworkRunning() { return isNetworkRunning; }

    public void pauseNetwork() { isNetworkRunning = false; }

    public void stopNetwork() { isNetworkStarted = false; }

    public void resumeNetwork() { if (isNetworkStarted) isNetworkRunning = true; }

    public void startNetwork(int broadcastInterval)
    {
        isNetworkRunning = true;
        isNetworkStarted = true;

        this.broadcastInterval = broadcastInterval;

        new Thread(networkThread, "Multicast Network thread").start();

        new Thread(receiveMessage, "Network Receiver Tread").start();
    }

    Runnable networkThread = new Runnable() {
        private int counter = 0;

        @Override
        public void run()
        {
            while (isNetworkStarted)
            {
                if (isNetworkRunning)
                {
                    try
                    {
                        message = "Manual update";
                        sendBroadcastMessageOverWifi(Integer.toString(counter++));
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
                else
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            if (transmitter != null)
            {
                try
                {
                    transmitter.leaveGroup(getGroupAddress());
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                transmitter.disconnect();
                transmitter.close();
            }
        }
    };

    Runnable receiveMessage = new Runnable() {
        @Override
        public void run()
        {
            Object received = null;

            while (isNetworkStarted)
            {
                received = receiveBroadcastMessageOverWifi();

                if (received instanceof String)
                {
                    LogHelper.info("Received message: " + received);
                }
                else
                {
                    LogHelper.warn("Null received");
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            if (receiver != null)
            {
                try
                {
                    receiver.leaveGroup(getGroupAddress());
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                receiver.disconnect();
                receiver.close();
            }
        }
    };

    public MulticastNetwork() {}

    private static InetAddress getBroadcastAddress() throws UnknownHostException { return InetAddress.getLocalHost(); }
    private static InetAddress getLoopbackAddress() { return InetAddress.getLoopbackAddress(); }
    private static InetAddress getGroupAddress() throws UnknownHostException { return InetAddress.getByName("228.5.6.7"); }

    private void sendBroadcastMessageOverWifi(String message) throws IOException
    {
        InetAddress addr = getBroadcastAddress();

        if (addr != null)
        {
            if (transmitter == null)
            {
                transmitter = new MulticastSocket(transmitterPort);
                transmitter.joinGroup(getGroupAddress());
                transmitter.setBroadcast(true);
            }
            packet = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, transmitterPort);
            transmitter.send(packet);
        }
    }

    private Object receiveBroadcastMessageOverWifi()
    {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try
        {
            if (receiver == null)
            {
                receiver = new MulticastSocket(receiverPort);
                receiver.joinGroup(getGroupAddress());
                receiver.setBroadcast(false);
            }
            LogHelper.info("Waiting...");
            receiver.receive(packet);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new String(packet.getData(), 0 , packet.getLength());
    }
}
