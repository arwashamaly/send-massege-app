package com.example.contacts_arwashamaly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts_arwashamaly.databinding.ContactsItemBinding;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {
ArrayList<Contacts> contactsArrayList;
OnAction action;

    public ContactsAdapter(ArrayList<Contacts> contactsArrayList, OnAction action) {
        this.contactsArrayList = contactsArrayList;
        this.action = action;
    }

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactsItemBinding binding =ContactsItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new ContactsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {
        int pos = position;
        Contacts contacts = contactsArrayList.get(pos);
        holder.tvName.setText(contacts.getName());
        holder.tvPhone.setText(contacts.getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.onChooseName(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsArrayList.size();
    }

    class ContactsHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        public ContactsHolder(@NonNull ContactsItemBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvName;
            tvPhone = binding.tvPhone;
        }
    }
}
