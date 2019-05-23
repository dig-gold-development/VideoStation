package com.site.vs.videostation.kit.contact.viewholder.header;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.model.FriendRequestValue;
import com.site.vs.videostation.R;

@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_header_friend)
public class FriendRequestViewHolder extends HeaderViewHolder<FriendRequestValue> {
    @BindView(R.id.unreadFriendRequestCountTextView)
    TextView unreadRequestCountTextView;
    private FriendRequestValue value;


    public FriendRequestViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(FriendRequestValue value) {
        this.value = value;
        if (value.getUnreadRequestCount() > 0) {
            unreadRequestCountTextView.setVisibility(View.VISIBLE);
            unreadRequestCountTextView.setText("" + value.getUnreadRequestCount());
        } else {
            unreadRequestCountTextView.setVisibility(View.GONE);
        }
    }
}
