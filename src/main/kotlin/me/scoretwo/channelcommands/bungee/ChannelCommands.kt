package me.scoretwo.channelcommands.bungee

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import java.io.ByteArrayInputStream
import java.io.DataInputStream


class ChannelCommands: Plugin(), Listener {

    override fun onEnable() {
        ProxyServer.getInstance().pluginManager.registerListener(this, this)
    }

    @EventHandler
    fun onExecute(e: PluginMessageEvent) {
        if (e.tag != "BungeeCord") {
            return
        }
        val byteArray = ByteArrayInputStream(e.data)
        val dis = DataInputStream(byteArray)
        val player = ProxyServer.getInstance().getPlayer(dis.readUTF())!!
        val subChannel = dis.readUTF()

        if (subChannel == "ChannelCommands") {
            return
        }

        val message = dis.readUTF()

        when(dis.readUTF()) {
            "console" -> {
                ProxyServer.getInstance().pluginManager.dispatchCommand(ProxyServer.getInstance().console, message)
            }
            "player" -> {
                ProxyServer.getInstance().pluginManager.dispatchCommand(player, message)
            }
        }

    }

}