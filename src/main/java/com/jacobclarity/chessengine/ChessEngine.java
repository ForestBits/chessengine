package com.jacobclarity.chessengine;

import com.jacobclarity.chessengine.uci.controller.UciPacketHandler;
import com.jacobclarity.chessengine.uci.UciProtocolHandler;
import org.apache.commons.cli.*;

import java.io.PrintWriter;

public class ChessEngine
{
    public static void main(String[] args)
    {
        Options options = new Options();

        options.addOption(Option.builder("h")
               .longOpt("help")
               .desc("show this help")
               .type(String.class)
               .build());

        options.addOption(Option.builder("l")
               .longOpt("log-file")
               .desc("path of log file")
               .hasArg()
               .type(String.class)
               .build());

        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = null;

        try
        {
            commandLine = parser.parse(options, args);
        }

        catch (ParseException ex)
        {
            System.out.println("Failed to parse arguments: " + ex.getMessage());

            System.exit(-1);
        }

        if (commandLine.hasOption("help"))
        {
            HelpFormatter helpFormatter = new HelpFormatter();

            PrintWriter out = new PrintWriter(System.out);

            out.println("chessengine [--log-file <path>]" + System.lineSeparator());
            out.println("Possible options:");

            helpFormatter.printOptions(out, 80, options, 2, 8);

            out.flush();

            System.exit(0);
        }

        UciPacketHandler packetHandler = new UciPacketHandler(commandLine);
        UciProtocolHandler protocolHandler = new UciProtocolHandler(commandLine);

        packetHandler.setOutputPacketHandler(protocolHandler);
        protocolHandler.setInputPacketHandler(packetHandler);

        Thread packetHandlerThread = new Thread(packetHandler);
        Thread uciProtocolThread = new Thread(protocolHandler);

        packetHandlerThread.start();
        uciProtocolThread.start();
    }
}
