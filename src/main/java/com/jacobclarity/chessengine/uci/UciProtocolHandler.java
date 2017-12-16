package com.jacobclarity.chessengine.uci;

import com.jacobclarity.chessengine.game.NotationException;
import com.jacobclarity.chessengine.uci.packet.*;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//deals with text I/O for UCI protocol and parsing packets
public class UciProtocolHandler implements Runnable, UciOutputPacketHandler
{
    private UciInputPacketHandler packetHandler;

    private final CommandLine commandLine;

    private boolean debug = false;

    private final PrintStream debugFile;

    public UciProtocolHandler(CommandLine commandLine)
    {
        this.commandLine = commandLine;

        PrintStream file = null;

        if (commandLine.hasOption("log-file"))
        {
            //if the file can be created/exists, enable debug logging
            //else ignore

            String path = commandLine.getOptionValue("log-file");

            try
            {
                file = new PrintStream(path);
            }

            catch (NullPointerException | FileNotFoundException ex)
            {
                System.out.println("Failed to open log file " + path);

                debug = false;
            }

            debug = true;
        }

        debugFile = file;
    }

    public void setInputPacketHandler(UciInputPacketHandler packetHandler)
    {
        this.packetHandler = packetHandler;
    }

    @Override
    public void run()
    {
        Scanner in = new Scanner(System.in);

        PrintStream out = System.out;

        UciTokenizer tokenizer = new UciTokenizer();

        //all UCI commands start with a unique token, so we can determine how to parse it
        //based off of the first token

        Map<UciToken, UciParseFunction> parseFunctions = new HashMap<>();

        parseFunctions.put(UciToken.DEBUG, DebugPacket::parsePacket);
        parseFunctions.put(UciToken.IS_READY, IsReadyPacket::parsePacket);
        parseFunctions.put(UciToken.UCI_NEW_GAME, NewGamePacket::parsePacket);
        parseFunctions.put(UciToken.POSITION, PositionPacket::parsePacket);
        parseFunctions.put(UciToken.QUIT, QuitPacket::parsePacket);
        parseFunctions.put(UciToken.REGISTER, RegisterPacket::parsePacket);
        parseFunctions.put(UciToken.GO, SearchPacket::parsePacket);
        parseFunctions.put(UciToken.SET_OPTION, SetOptionPacket::parsePacket);
        parseFunctions.put(UciToken.STOP, StopSearchPacket::parsePacket);
        parseFunctions.put(UciToken.UCI, UseUciPacket::parsePacket);


        while (true)
        {
            String line = in.nextLine();

            if (debug)
                debugFile.println(line);

            if (line.trim().length() == 0) //blank line
                continue;

            //valid line of some type

            UciTokenData[] tokens = tokenizer.getTokensFromLine(line);

            UciToken firstToken = tokens[0].getToken();

            UciParseFunction func = parseFunctions.get(firstToken);

            if (func == null)
            {
                String string = "Unknown command";

                System.out.println(string);

                if (debug)
                    debugFile.println(string);
            }
            else
            {
                try
                {
                    UciPacket packet = func.parsePacket(tokens);

                    packetHandler.handleInputPacket(packet);
                }

                catch (UciParseException | NotationException ex)
                {
                    String string = "Bad command: " + ex.getMessage();

                    System.out.println(string);

                    if (debug)
                        debugFile.println(string);
                }
            }
        }
    }

    @Override
    public void handleOutputPacket(UciPacket packet)
    {
        String string = packet.toCommandString();

        System.out.println(string);

        if (debug)
            debugFile.println(string);
    }
}
