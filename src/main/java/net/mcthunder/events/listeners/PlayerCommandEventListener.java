package net.mcthunder.events.listeners;

import net.mcthunder.handlers.CommandHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEventListener implements net.mcthunder.interfaces.PlayerCommandEventListener {
    private CommandHandler commandHandler;

    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onCommand(Server server, Session session, ClientChatPacket packet) {
        commandHandler = new CommandHandler();
        commandHandler.handleChat(server, session, packet, sessionsList);
    }
}
