package com.leading.xmpp_client.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import com.leading.xmpp_client.server.NotificationIQ;
import com.leading.xmpp_client.server.XmppManager;
import com.leading.xmpp_client.tools.Constants;
import com.leading.xmpp_client.tools.LogUtil;

import android.content.Intent;
import android.util.Log;

public class NotificationPacketListener implements PacketListener {
	private static final String LOGTAG = LogUtil.makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                String notificationUri = notification.getUri();
                String notificationViewApp=notification.getViewApp();
                String notificationFSIID=notification.getFSIID();

                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY,notificationApiKey);
                intent.putExtra(Constants.NOTIFICATION_TITLE,notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE,notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                intent.putExtra(Constants.NOTIFICATION_VIEWAPP, notificationViewApp);
                intent.putExtra(Constants.NOTIFICATION_FSIID, notificationFSIID);
                
                xmppManager.sendNotificationFeekback(notificationId);
                xmppManager.getContext().sendBroadcast(intent);
                
                
            }
        }

    }
}
