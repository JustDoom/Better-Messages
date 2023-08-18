package com.imjustdoom.bettermessages.listener;

public class ServerChangeListener {

//    @Subscribe(order = PostOrder.FIRST)
//    public void onPlayerChat(ServerEvent event) {
//        // do stuff
//    }
//
//    @EventHandler
//    public void onServerChange(ServerSwitchEvent event) {
//        ProxiedPlayer player = event.getPlayer();
//
//        ByteArrayOutputStream b = new ByteArrayOutputStream();
//        DataOutputStream out = new DataOutputStream(b);
//        try {
//            out.writeUTF("ServerSwitchEvent");
//            out.writeUTF(player.getName());
//            out.writeUTF(event.getFrom() == null ? "" : event.getFrom().getName());
//            out.writeUTF(player.getServer().getInfo().getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        player.getServer().sendData("BetterMessages", b.toByteArray());
//    }
}