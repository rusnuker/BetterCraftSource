// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.rcon;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class RconClientCli
{
    private static final int EXIT_CODE_SUCCESS = 0;
    private static final int EXIT_CODE_INVALID_ARGUMENTS = 1;
    private static final int EXIT_CODE_AUTH_FAILURE = 2;
    private static final int DEFAULT_PORT = 25575;
    private static final String QUIT_COMMAND = "\\quit";
    
    public static void main(final String[] args) {
        final int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
    
    private static int run(final String[] args) {
        if (args.length < 3) {
            return printUsage();
        }
        final String[] hostAndPort = args[0].split(":");
        if (hostAndPort.length > 2) {
            return printUsage();
        }
        final String host = hostAndPort[0];
        final int port = (hostAndPort.length == 2) ? Integer.parseInt(hostAndPort[1]) : 25575;
        final String password = args[1];
        final List<String> commands = new ArrayList<String>(Arrays.asList(args).subList(2, args.length));
        final boolean terminalMode = commands.contains("-t");
        if (terminalMode && commands.size() != 1) {
            return printUsage();
        }
        try {
            Throwable t = null;
            try {
                final RconClient client = RconClient.open(host, port, password);
                try {
                    Runtime.getRuntime().addShutdownHook(new Thread(client::close));
                    if (terminalMode) {
                        System.out.println("Authenticated. Type \"\\quit\" to quit.");
                        System.out.print("> ");
                        final Scanner scanner = new Scanner(System.in);
                        while (scanner.hasNextLine()) {
                            final String line = scanner.nextLine();
                            if (line.trim().equals("\\quit")) {
                                break;
                            }
                            final String response = client.sendCommand(line);
                            System.out.println("< " + (response.isEmpty() ? "(empty response)" : response));
                            System.out.print("> ");
                        }
                    }
                    else {
                        for (final String command : commands) {
                            System.out.println("> " + command);
                            final String response = client.sendCommand(command);
                            System.out.println("< " + (response.isEmpty() ? "(empty response)" : response));
                        }
                    }
                }
                finally {
                    if (client != null) {
                        client.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2;
                    t = t2;
                }
                else {
                    final Throwable t2;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (final AuthFailureException e) {
            System.err.println("Authentication failure");
            return 2;
        }
        return 0;
    }
    
    private static int printUsage() {
        System.out.println("Usage: java -jar minecraft-rcon-client-<version>.jar <host[:port]> <password> <-t|commands>");
        System.out.println();
        System.out.println("Example 1: java -jar minecraft-rcon-client-1.0.0.jar localhost:12345 hunter2 'say Hello, world' 'teleport Notch 0 0 0'");
        System.out.println("Example 2: java -jar minecraft-rcon-client-1.0.0.jar localhost:12345 hunter2 -t");
        System.out.println();
        System.out.println("The port can be omitted, the default is 25575.");
        System.out.println("\"-t\" enables terminal mode, to enter commands in an interactive terminal.");
        return 1;
    }
}
