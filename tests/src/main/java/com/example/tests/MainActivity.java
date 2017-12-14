package com.example.tests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class MainActivity extends AppCompatActivity {

    SignaturePad signaturePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signaturePad=(SignaturePad)findViewById(R.id.signature_pad);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });
    }
}
