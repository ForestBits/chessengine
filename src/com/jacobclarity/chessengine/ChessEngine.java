package com.jacobclarity.chessengine;

import com.jacobclarity.chessengine.uci.controller.UciPacketHandler;
import com.jacobclarity.chessengine.uci.UciProtocolHandler;

public class ChessEngine
{
    public static void main(String[] args)
    {
        UciPacketHandler packetHandler = new UciPacketHandler();
        UciProtocolHandler protocolHandler = new UciProtocolHandler();

        packetHandler.setOutputPacketHandler(protocolHandler);
        protocolHandler.setInputPacketHandler(packetHandler);

        Thread packetHandlerThread = new Thread(packetHandler);
        Thread uciProtocolThread = new Thread(protocolHandler);

        packetHandlerThread.start();
        uciProtocolThread.start();
    }
}
