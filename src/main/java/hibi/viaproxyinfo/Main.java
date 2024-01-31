package hibi.viaproxyinfo;

import java.net.InetSocketAddress;

import com.viaversion.viaversion.libs.gson.JsonObject;

import io.netty.buffer.Unpooled;
import net.lenni0451.lambdaevents.EventHandler;
import net.raphimc.netminecraft.constants.ConnectionState;
import net.raphimc.netminecraft.packet.PacketTypes;
import net.raphimc.viaproxy.ViaProxy;
import net.raphimc.viaproxy.cli.options.Options;
import net.raphimc.viaproxy.plugins.ViaProxyPlugin;
import net.raphimc.viaproxy.plugins.events.FillPlayerDataEvent;
import net.raphimc.viaproxy.plugins.events.ProxyStartEvent;

public class Main extends ViaProxyPlugin {

    @Override
    public void onEnable() {
        ViaProxy.EVENT_MANAGER.register(this);
    }

    @EventHandler
    void onProxyStart(ProxyStartEvent ev) {
        System.out.println(Options.PROTOCOL_VERSION.getVersion());
    }

    @EventHandler
    void onFillPlayerData(FillPlayerDataEvent ev) {
        if (!(Options.CONNECT_ADDRESS instanceof InetSocketAddress addr))
            return;
        var con = ev.getProxyConnection();
        new Thread(() -> {
            while (con.getC2pConnectionState() != ConnectionState.PLAY) {
                Thread.yield();
            }
            var json = new JsonObject();
            json.addProperty("host", addr.getHostString());
            json.addProperty("port", addr.getPort());
            json.addProperty("remoteVersion", Options.PROTOCOL_VERSION.getVersion());
            System.out.println("Sending proxy info packet");
            var buf = Unpooled.buffer();
            PacketTypes.writeString(buf, json.toString());
            con.sendCustomPayload("viapxyinfo:query", buf);
        }, "ViaProxyInfo").start();
    }
}
