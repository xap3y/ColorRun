package me.xap3y.colorrun.util

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Field
import java.lang.reflect.Method


class ActionBar {
    companion object {
        fun sendActionbar(player: Player, message: String) {
            var nmsVersion = Bukkit.getServer().javaClass.getPackage().name
            nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1)


            //1.10 and up
            if (!nmsVersion.startsWith("v1_9_R") && !nmsVersion.startsWith("v1_8_R")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                return
            }


            //1.8.x and 1.9.x
            try {
                val craftPlayerClass = Class.forName("org.bukkit.craftbukkit.$nmsVersion.entity.CraftPlayer")
                val craftPlayer = craftPlayerClass.cast(player)

                val ppoc = Class.forName("net.minecraft.server.$nmsVersion.PacketPlayOutChat")
                val packet = Class.forName("net.minecraft.server.$nmsVersion.Packet")
                val packetPlayOutChat: Any
                val chat = Class.forName(
                    "net.minecraft.server.$nmsVersion" + (if (nmsVersion.equals(
                            "v1_8_R1",
                            ignoreCase = true
                        )
                    ) ".ChatSerializer" else ".ChatComponentText")
                )
                val chatBaseComponent = Class.forName("net.minecraft.server.$nmsVersion.IChatBaseComponent")

                var method: Method? = null
                if (nmsVersion.equals("v1_8_R1", ignoreCase = true)) method =
                    chat.getDeclaredMethod("a", String::class.java)

                val `object` = if (nmsVersion.equals("v1_8_R1", ignoreCase = true)) chatBaseComponent.cast(
                    method?.invoke(
                        chat,
                        "{'text': '$message'}"
                    )
                ) else chat.getConstructor(
                    *arrayOf<Class<*>>(
                        String::class.java
                    )
                ).newInstance(message)
                packetPlayOutChat = ppoc.getConstructor(*arrayOf(chatBaseComponent, java.lang.Byte.TYPE))
                    .newInstance(`object`, 2.toByte())

                val handle: Method = craftPlayerClass.getDeclaredMethod("getHandle")
                val iCraftPlayer: Any = handle.invoke(craftPlayer)
                val playerConnectionField: Field = iCraftPlayer.javaClass.getDeclaredField("playerConnection")
                val playerConnection: Any = playerConnectionField.get(iCraftPlayer)
                val sendPacket: Method = playerConnection.javaClass.getDeclaredMethod("sendPacket", packet)
                sendPacket.invoke(playerConnection, packetPlayOutChat)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}