package com.jacobclarity.chessengine.uci;

import com.jacobclarity.chessengine.engine.Engine;
import com.jacobclarity.chessengine.game.Board;
import com.jacobclarity.chessengine.uci.packet.PositionPacket;
import com.jacobclarity.chessengine.uci.packet.UciPacket;

//acts as bridge between UciProtocolHandler, which handles input and output via UCI protocol, and
//the actual engine, which does the calculations. UciPacketHandler translates UCI protocol commands
//to engine commands, and engine outputs to UCI protocol commands
//there is no specific protocol for this, as the only external interface is via UCI
public class UciPacketHandler implements Runnable, UciInputPacketHandler
{
    private UciOutputPacketHandler outputHandler;

    private final Engine engine = new Engine();

    public void setOutputPacketHandler(UciOutputPacketHandler outputHandler)
    {
        this.outputHandler = outputHandler;
    }

    @Override
    public void run()
    {
        System.out.println("Packet handler thread");
    }

    @Override
    public void handleInputPacket(UciPacket packet)
    {
        System.out.println("handler got packet: " + packet);

        if (packet instanceof PositionPacket)
        {
            Board b = ((PositionPacket) packet).getBoard();

            System.out.println(b.getBoardDebugString());
        }
    }
}
