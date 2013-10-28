package com.leading.xmpp_client.listener;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

import com.leading.xmpp_client.server.XmppManager;
import com.leading.xmpp_client.tools.LogUtil;

public class PersistentConnectionListener implements ConnectionListener {
	  private static final String LOGTAG = LogUtil.makeLogTag(PersistentConnectionListener.class);

	    private final XmppManager xmppManager;

	    public PersistentConnectionListener(XmppManager xmppManager) {
	        this.xmppManager = xmppManager;
	    }

	    @Override
	    public void connectionClosed() {
	        Log.d(LOGTAG, "connectionClosed()...");
	    }

	    @Override
	    public void connectionClosedOnError(Exception e) {
	        Log.d(LOGTAG, "connectionClosedOnError()...");
	        if (xmppManager.getConnection() != null
	                && xmppManager.getConnection().isConnected()) {
	            xmppManager.getConnection().disconnect();
	        }
	        xmppManager.startReconnectionThread();
	    }

	    @Override
	    public void reconnectingIn(int seconds) {
	        Log.d(LOGTAG, "reconnectingIn()...");
	    }

	    @Override
	    public void reconnectionFailed(Exception e) {
	        Log.d(LOGTAG, "reconnectionFailed()...");
	    }

	    @Override
	    public void reconnectionSuccessful() {
	        Log.d(LOGTAG, "reconnectionSuccessful()...");
	    }

}
