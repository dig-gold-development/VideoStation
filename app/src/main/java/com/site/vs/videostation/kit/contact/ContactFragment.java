package com.site.vs.videostation.kit.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.List;


import com.site.vs.videostation.kit.IMServiceStatusViewModel;
import com.site.vs.videostation.kit.WfcUIKit;
import com.site.vs.videostation.kit.channel.ChannelListActivity;
import com.site.vs.videostation.kit.contact.model.ContactCountFooterValue;
import com.site.vs.videostation.kit.contact.model.FriendRequestValue;
import com.site.vs.videostation.kit.contact.model.GroupValue;
import com.site.vs.videostation.kit.contact.model.HeaderValue;
import com.site.vs.videostation.kit.contact.model.UIUserInfo;
import com.site.vs.videostation.kit.contact.newfriend.FriendRequestListActivity;
import com.site.vs.videostation.kit.contact.viewholder.footer.ContactCountViewHolder;
import com.site.vs.videostation.kit.contact.viewholder.header.ChannelViewHolder;
import com.site.vs.videostation.kit.contact.viewholder.header.FriendRequestViewHolder;
import com.site.vs.videostation.kit.contact.viewholder.header.GroupViewHolder;
import com.site.vs.videostation.kit.group.GroupListActivity;
import com.site.vs.videostation.kit.user.UserInfoActivity;
import com.site.vs.videostation.kit.user.UserViewModel;
import com.site.vs.videostation.kit.widget.QuickIndexBar;
import com.site.vs.videostation.ui.MainActivity;

import cn.wildfirechat.model.UserInfo;

public class ContactFragment extends BaseContactFragment implements QuickIndexBar.OnLetterUpdateListener {
    private UserViewModel userViewModel;
    private IMServiceStatusViewModel imServiceStatusViewModel;

    private Observer<Integer> friendRequestUpdateLiveDataObserver = count -> {
        FriendRequestValue requestValue = new FriendRequestValue(count == null ? 0 : count);
        contactAdapter.updateHeader(0, requestValue);
    };

    private Observer<Object> contactListUpdateLiveDataObserver = o -> {
        loadContacts();
    };

    private Observer<Boolean> imStatusLiveDataObserver = status -> {
        if (status && (contactAdapter != null && (contactAdapter.contacts == null || contactAdapter.contacts.size() == 0))) {
            loadContacts();
        }
    };

    private void loadContacts() {
        contactViewModel.getContactsAsync(false)
                .observe(this, userInfos -> {
                    if (userInfos == null || userInfos.isEmpty()) {
                        return;
                    }
                    contactAdapter.setContacts(userInfoToUIUserInfo(userInfos));
                    contactAdapter.notifyDataSetChanged();

                    for (UserInfo info : userInfos) {
                        if (info.name == null || info.displayName == null) {
                            userViewModel.getUserInfo(info.uid, true);
                        }
                    }
                });
    }

    private Observer<List<UserInfo>> userInfoLiveDataObserver = userInfos -> {
        contactAdapter.updateContacts(userInfoToUIUserInfo(userInfos));
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        contactViewModel.contactListUpdatedLiveData().observeForever(contactListUpdateLiveDataObserver);
        contactViewModel.friendRequestUpdatedLiveData().observeForever(friendRequestUpdateLiveDataObserver);

        userViewModel = WfcUIKit.getAppScopeViewModel(UserViewModel.class);
        userViewModel.userInfoLiveData().observeForever(userInfoLiveDataObserver);
        imServiceStatusViewModel = WfcUIKit.getAppScopeViewModel(IMServiceStatusViewModel.class);
        imServiceStatusViewModel.imServiceStatusLiveData().observeForever(imStatusLiveDataObserver);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactViewModel.contactListUpdatedLiveData().removeObserver(contactListUpdateLiveDataObserver);
        contactViewModel.friendRequestUpdatedLiveData().removeObserver(friendRequestUpdateLiveDataObserver);
        userViewModel.userInfoLiveData().removeObserver(userInfoLiveDataObserver);
        imServiceStatusViewModel.imServiceStatusLiveData().removeObserver(imStatusLiveDataObserver);
    }

    @Override
    public void initHeaderViewHolders() {
        addHeaderViewHolder(FriendRequestViewHolder.class, new FriendRequestValue(contactViewModel.getUnreadFriendRequestCount()));
        addHeaderViewHolder(GroupViewHolder.class, new GroupValue());
        addHeaderViewHolder(ChannelViewHolder.class, new HeaderValue());
    }

    @Override
    public void initFooterViewHolders() {
        addFooterViewHolder(ContactCountViewHolder.class, new ContactCountFooterValue());
    }

    @Override
    public void onContactClick(UIUserInfo userInfo) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo.getUserInfo());
        startActivity(intent);
    }

    @Override
    public void onHeaderClick(int index) {
        switch (index) {
            case 0:
                ((MainActivity) getActivity()).hideUnreadFriendRequestBadgeView();
                showFriendRequest();
                break;
            case 1:
                showGroupList();
                break;
            case 2:
                showChannelList();
                break;
            default:
                break;
        }
    }

    private void showFriendRequest() {
        FriendRequestValue value = new FriendRequestValue(0);
        contactAdapter.updateHeader(0, value);

        contactViewModel.clearUnreadFriendRequestStatus();
        Intent intent = new Intent(getActivity(), FriendRequestListActivity.class);
        startActivity(intent);
    }

    private void showGroupList() {
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        startActivity(intent);
    }

    private void showChannelList() {
        Intent intent = new Intent(getActivity(), ChannelListActivity.class);
        startActivity(intent);
    }
}
