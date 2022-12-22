package com.example.contacts_arwashamaly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.contacts_arwashamaly.databinding.ActivityContactsBinding;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements OnAction {
    ActivityContactsBinding binding;
    ArrayList<Contacts> contactsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            getAllContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            getAllContacts();
        }
    }

    void getAllContacts() {
        long id;
        String name;
        String phone;
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_ID"));
                Uri uri1 = ContactsContract.Data.CONTENT_URI;
                Cursor cursor1 = getContentResolver().query(uri1, null,
                        ContactsContract.Data.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)}, null);
                if (cursor1.moveToFirst()) {
                    name = cursor1.getString(cursor1.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME));
                    do {
                        if (cursor1.getString(cursor1.getColumnIndexOrThrow("mimeType"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                            if (cursor1.getInt(cursor1.getColumnIndexOrThrow("data2")) ==
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                                phone = cursor1.getString(cursor1.getColumnIndexOrThrow("data1"));
                                Contacts contacts = new Contacts(name, phone);
                                contactsArrayList.add(contacts);
                            }
                        }
                    } while (cursor1.moveToNext());
                }
                cursor1.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        ContactsAdapter adapter = new ContactsAdapter(contactsArrayList,ContactsActivity.this);
        binding.rc.setAdapter(adapter);
        binding.rc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    @Override
    public void onChooseName(int position) {
        Intent intent = new Intent();
        Contacts contacts =contactsArrayList.get(position);
        intent.putExtra("name",contacts.getName());
        intent.putExtra("phone",contacts.getPhone());
        setResult(RESULT_OK,intent);
        finish();
    }
}