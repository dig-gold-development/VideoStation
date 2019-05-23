package com.site.vs.videostation.kit.contact.viewholder.footer;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.ContactViewModel;
import com.site.vs.videostation.kit.contact.model.FooterValue;

public abstract class FooterViewHolder<T extends FooterValue> extends RecyclerView.ViewHolder {
    protected Fragment fragment;
    protected ContactAdapter adapter;
    protected ContactViewModel contactViewModel;

    public FooterViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(itemView);
        this.fragment = fragment;
        this.adapter = adapter;
        contactViewModel = ViewModelProviders.of(fragment).get(ContactViewModel.class);
    }


    public abstract void onBind(T t);

}
