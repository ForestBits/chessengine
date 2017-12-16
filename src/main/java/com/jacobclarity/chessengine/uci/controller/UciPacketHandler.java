package com.jacobclarity.chessengine.uci.controller;

import com.jacobclarity.chessengine.engine.Engine;
import com.jacobclarity.chessengine.uci.UciInputPacketHandler;
import com.jacobclarity.chessengine.uci.UciOutputPacketHandler;
import com.jacobclarity.chessengine.uci.option.UciOption;
import com.jacobclarity.chessengine.uci.packet.*;
import org.apache.commons.cli.CommandLine;

//acts as bridge between UciProtocolHandler, which handles input and output via UCI protocol, and
//the actual controller, which does the calculations. UciPacketHandler translates UCI protocol commands
//to controller commands, and controller outputs to UCI protocol commands
//there is no specific protocol for this, as the only external interface is via UCI
public class UciPacketHandler implements Runnable, UciInputPacketHandler
{
    private UciOutputPacketHandler outputHandler;

    private final Engine engine = new Engine();

    private UciState currentState = UciState.STARTUP;

    private final CommandLine commandLine;

    private boolean debug = false;

    private void handleStartupPackets(UciPacket packet)
    {
        if (packet instanceof UseUciPacket)
        {
            outputHandler.handleOutputPacket(new IDPacket("name", "ChessEngine"));
            outputHandler.handleOutputPacket(new IDPacket("author", "Jacob Clarity"));

            for (UciOption option : UciOption.getOptions())
                outputHandler.handleOutputPacket(new OptionPacket(option));

            outputHandler.handleOutputPacket(new UciOkPacket());

            currentState = UciState.IDLE;
        }
    }

    private void handleIdlePackets(UciPacket packet)
    {
        //debug packet
        //setoption
        //ucinewgame
        //ppsition
        //go

        if (packet instanceof IsReadyPacket)
            outputHandler.handleOutputPacket(new ReadyOkPacket());
    }

    private void handleSearchPackets(UciPacket packet)
    {

    }

    public UciPacketHandler(CommandLine commandLine)
    {
        this.commandLine = commandLine;
    }

    public void setOutputPacketHandler(UciOutputPacketHandler outputHandler)
    {
        this.outputHandler = outputHandler;
    }

    @Override
    public void run()
    {

    }

    @Override
    public void handleInputPacket(UciPacket packet)
    {
        //handle state independent packets here, and handle everything else by case (some packets only occur by case,
        //or may have different behavior depending on case)

        if (packet instanceof DebugPacket)
        {
            DebugPacket debugPacket = (DebugPacket) packet;

            debug = debugPacket.shouldDebug();
        }
        else if (packet instanceof IsReadyPacket)
            outputHandler.handleOutputPacket(new ReadyOkPacket());
        else if (packet instanceof SetOptionPacket)
        {
            SetOptionPacket setOptionPacket = (SetOptionPacket) packet;


        }
        else if (packet instanceof QuitPacket)
        {
            System.exit(0);
        }
        else
            switch (currentState)
            {
                case STARTUP:
                    handleStartupPackets(packet);

                    break;

                case IDLE:
                    handleIdlePackets(packet);

                    break;

                case SEARCHING:
                    handleSearchPackets(packet);

                    break;
            }
    }
}
