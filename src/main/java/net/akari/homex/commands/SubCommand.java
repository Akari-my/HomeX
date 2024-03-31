package net.akari.homex.commands;

import org.bukkit.command.CommandSender;
import java.io.IOException;

public interface SubCommand {

    void execute(CommandSender sender, String[] args) throws IOException;
}
