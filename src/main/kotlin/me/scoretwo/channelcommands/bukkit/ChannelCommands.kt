package me.scoretwo.channelcommands.bukkit

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class ChannelCommands: JavaPlugin() {

    override fun onEnable() {
        getCommandMap().register("ChannelCommands", object : Command("ChannelCommands","", "/ChannelCommands", listOf("cc","ccmd")) {

            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (!sender.hasPermission("ChannelCommands.use")) {
                    sender.sendMessage("§3该命令需要权限才能使用.")
                    return true
                }
                if (args.isEmpty()) {
                    sender.sendMessage("§eChannelCommands")
                    help(sender)
                    return true
                }



                when(args[0]) {
                    "bc" -> {
                        if (args.size < 3) {
                            sender.sendMessage("§c用法错误")
                            help(sender)
                            return true
                        }

                        val stringBuilder = StringBuilder()

                        for (i in 3 until args.size) {
                            stringBuilder.append("${args[i]} ")
                        }
                        val player = Bukkit.getPlayer(args[1])!!
                        val type = args[2]
                        val message = stringBuilder.toString()

                        if (type == "console" || type == "player")
                            sendToBungeeMessage(player, listOf("ChannelCommands",message,type))
                        else {
                            sender.sendMessage("§c执行类型不明确")
                            help(sender)
                        }

                    }
                }

                return true
            }

        })

    }

    fun help(sender: CommandSender) {
        sender.sendMessage("§b/ccmd bc <player> <player or console?> <command...> - send command to bungee.")
    }

    fun sendToBungeeMessage(player: Player, messages: List<String>) {
        val byteArray = ByteArrayOutputStream()
        val out = DataOutputStream(byteArray)
        out.writeUTF(player.name)
        for (arg in messages) {
            try {
                out.writeUTF(arg)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        player.sendPluginMessage(this, "BungeeCord", byteArray.toByteArray())
    }


    private fun getCommandMap() : CommandMap {
        return Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap
    }

}