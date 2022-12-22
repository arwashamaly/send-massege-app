package com.example.contacts_arwashamaly;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.example.contacts_arwashamaly.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ActivityResultLauncher<Intent> arl;
    String phone=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if (intent!=null){
                binding.tvChooseName.setText(intent.getStringExtra("name"));
                phone = intent.getStringExtra("phone");}
            }
        });

        binding.tvChooseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
                arl.launch(intent);
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        sendSMS();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                }
            }
        });
    }

    private void sendSMS() {
        String sms = binding.etMessage.getText().toString().trim();
        if (phone == null){
            Toast.makeText(this, "Please choose name", Toast.LENGTH_SHORT).show();
        }else if (sms.isEmpty()){
            binding.etMessage.setError("Please type the message");
        }else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, sms, null, null);
            Toast.makeText(this, "Message has been sent", Toast.LENGTH_SHORT).show();
            phone=null;
            binding.etMessage.setText("");
            binding.tvChooseName.setText("Choose  name");
        }
        
    }
}