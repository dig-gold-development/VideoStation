package com.site.vs.videostation.kit.conversation.mention;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.model.HeaderValue;
import com.site.vs.videostation.kit.contact.viewholder.header.HeaderViewHolder;
import com.site.vs.videostation.R;

@LayoutRes(resId = R.layout.conversation_header_mention_all)
public class MentionAllHeaderViewHolder extends HeaderViewHolder<HeaderValue> {
    public MentionAllHeaderViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(HeaderValue value) {

    }
}
