package com.profiler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

public class NfcReceiver1 extends BroadcastReceiver {
	/* ... */

	private NdefMessage[] msgs;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("", "************* Received  ");
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
					Log.v("", "Data is  " + msgs[i]);
				}

			}
		} /* end handle NDEF_DISCOVERED */
	} /* end onReceive */
}
