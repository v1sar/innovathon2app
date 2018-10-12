package com.v1sar.qring2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.v1sar.qring2.Utils.Constants;

import org.json.JSONObject;

import me.kevingleason.pnwebrtc.PnPeerConnectionClient;

import static com.v1sar.qring2.Utils.Constants.ANONYM_QR_PREFS;
import static com.v1sar.qring2.Utils.Constants.ANONYM_QR_URL;

public class IncomingCallActivity extends AppCompatActivity{

    private String username;
    private String callUser;

    private Pubnub mPubNub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        Bundle extras = getIntent().getExtras();
        this.callUser = extras.getString(Constants.CALL_USER, "");
        ImageView accept = findViewById(R.id.accept_call);
        accept.setOnClickListener(view -> {
            acceptCall();
        });

        ImageView reject = findViewById(R.id.reject_call);
        reject.setOnClickListener(view -> {
            rejectCall();
        });

        SharedPreferences prefs = getSharedPreferences(ANONYM_QR_PREFS, MODE_PRIVATE);
        username  = prefs.getString(ANONYM_QR_URL, null);
        this.mPubNub  = new Pubnub(Constants.PUB_KEY, Constants.SUB_KEY);
        this.mPubNub.setUUID(this.username);
    }

    public void acceptCall(){
        Intent intent = new Intent(IncomingCallActivity.this, VideoChatActivity.class);
        intent.putExtra(Constants.USER_NAME, this.username);
        intent.putExtra(Constants.CALL_USER, this.callUser);
        startActivity(intent);
    }

    /**
     * Publish a hangup command if rejecting call.
     *
     */
    public void rejectCall(){
        JSONObject hangupMsg = PnPeerConnectionClient.generateHangupPacket(this.username);
        this.mPubNub.publish(this.callUser,hangupMsg, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Intent intent = new Intent(IncomingCallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.mPubNub!=null){
            this.mPubNub.unsubscribeAll();
        }
    }

}
