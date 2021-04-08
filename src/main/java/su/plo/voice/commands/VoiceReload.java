package su.plo.voice.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import su.plo.voice.PlasmoVoice;
import su.plo.voice.PlasmoVoiceConfig;
import su.plo.voice.common.packets.tcp.ConfigPacket;
import su.plo.voice.common.packets.tcp.PacketTCP;
import su.plo.voice.socket.SocketServerUDP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

public class VoiceReload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        PlasmoVoice.getInstance().reloadConfig();
        PlasmoVoice.getInstance().updateConfig();

        PlasmoVoiceConfig config = PlasmoVoice.getInstance().config;

        Bukkit.getScheduler().runTaskAsynchronously(PlasmoVoice.getInstance(), () -> {
            try {
                Enumeration<Player> it = SocketServerUDP.clients.keys();
                while (it.hasMoreElements()) {
                    Player player = it.nextElement();

                    byte[] pkt = PacketTCP.write(new ConfigPacket(config.sampleRate,
                            new ArrayList<>(config.distances),
                            config.defaultDistance,
                            config.maxPriorityDistance,
                            config.disableVoiceActivation || !player.hasPermission("voice.activation"),
                            config.fadeDivisor,
                            config.priorityFadeDivisor));

                    player.sendPluginMessage(PlasmoVoice.getInstance(), "plasmo:voice", pkt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sender.sendMessage(PlasmoVoice.getInstance().getMessagePrefix("reloaded"));
        return true;
    }
}
