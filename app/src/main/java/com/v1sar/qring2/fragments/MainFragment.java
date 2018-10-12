package com.v1sar.qring2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v1sar.qring2.R;

import java.util.concurrent.ThreadLocalRandom;

import static android.content.Context.MODE_PRIVATE;
import static com.v1sar.qring2.Utils.Constants.ANONYM_QR_PREFS;
import static com.v1sar.qring2.Utils.Constants.ANONYM_QR_URL;
import static com.v1sar.qring2.Utils.Constants.QR_URL_BASE;

public class MainFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        TextView urlQr = view.findViewById(R.id.url_qr);

        SharedPreferences prefs = getActivity().getSharedPreferences(ANONYM_QR_PREFS, MODE_PRIVATE);
        String restoredText = prefs.getString(ANONYM_QR_URL, null);
        if (restoredText == null) {
            restoredText = Integer.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)).toString();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(ANONYM_QR_PREFS, MODE_PRIVATE).edit();
            editor.putString(ANONYM_QR_URL, restoredText);
            editor.apply();
        }
        urlQr.setText(QR_URL_BASE + restoredText);

        return view;
    }
}
