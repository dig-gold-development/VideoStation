package com.site.vs.videostation.kit.conversation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.lqr.optionitemview.OptionItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.site.vs.videostation.Config;
import com.site.vs.videostation.kit.ConfigEventViewModel;
import com.site.vs.videostation.kit.WfcScheme;
import com.site.vs.videostation.kit.WfcUIKit;
import com.site.vs.videostation.kit.common.OperateResult;
import com.site.vs.videostation.kit.contact.ContactViewModel;
import com.site.vs.videostation.kit.conversationlist.ConversationListViewModel;
import com.site.vs.videostation.kit.conversationlist.ConversationListViewModelFactory;
import com.site.vs.videostation.kit.group.AddGroupMemberActivity;
import com.site.vs.videostation.kit.group.GroupViewModel;
import com.site.vs.videostation.kit.group.RemoveGroupMemberActivity;
import com.site.vs.videostation.kit.group.SetGroupNameActivity;
import com.site.vs.videostation.kit.qrcode.QRCodeActivity;
import com.site.vs.videostation.kit.user.UserInfoActivity;
import com.site.vs.videostation.kit.user.UserViewModel;
import com.site.vs.videostation.R;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.UserInfo;

public class GroupConversationInfoFragment extends Fragment implements ConversationMemberAdapter.OnMemberClickListener, CompoundButton.OnCheckedChangeListener {

    // group
    @BindView(R.id.groupLinearLayout_0)
    LinearLayout groupLinearLayout_0;
    @BindView(R.id.groupNameOptionItemView)
    OptionItemView groupNameOptionItemView;
    @BindView(R.id.groupQRCodeOptionItemView)
    OptionItemView groupQRCodeOptionItemView;
    @BindView(R.id.groupNoticeLinearLayout)
    LinearLayout noticeLinearLayout;
    @BindView(R.id.groupNoticeTextView)
    TextView noticeTextView;
    @BindView(R.id.groupManageOptionItemView)
    OptionItemView groupManageOptionItemView;
    @BindView(R.id.groupManageDividerLine)
    View groupManageDividerLine;

    @BindView(R.id.groupLinearLayout_1)
    LinearLayout groupLinearLayout_1;
    @BindView(R.id.myGroupNickNameOptionItemView)
    OptionItemView myGroupNickNameOptionItemView;
    @BindView(R.id.showGroupMemberAliasSwitchButton)
    SwitchButton showGroupMemberNickNameSwitchButton;

    @BindView(R.id.quitButton)
    Button quitGroupButton;

    @BindView(R.id.markGroupLinearLayout)
    LinearLayout markGroupLinearLayout;
    @BindView(R.id.markGroupSwitchButton)
    SwitchButton markGroupSwitchButton;

    // common
    @BindView(R.id.memberRecyclerView)
    RecyclerView memberReclerView;
    @BindView(R.id.stickTopSwitchButton)
    SwitchButton stickTopSwitchButton;
    @BindView(R.id.silentSwitchButton)
    SwitchButton silentSwitchButton;

    private ConversationInfo conversationInfo;
    private ConversationMemberAdapter conversationMemberAdapter;
    private ConversationViewModel conversationViewModel;
    private UserViewModel userViewModel;

    private GroupViewModel groupViewModel;
    private GroupInfo groupInfo;
    // me in group
    private GroupMember groupMember;


    private static final int REQUEST_ADD_MEMBER = 100;
    private static final int REQUEST_REMOVE_MEMBER = 200;
    private static final int REQUEST_CODE_SET_GROUP_NAME = 300;

    public static GroupConversationInfoFragment newInstance(ConversationInfo conversationInfo) {
        GroupConversationInfoFragment fragment = new GroupConversationInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("conversationInfo", conversationInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        conversationInfo = args.getParcelable("conversationInfo");
        assert conversationInfo != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_info_group_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        conversationViewModel = ViewModelProviders.of(this, new ConversationViewModelFactory(conversationInfo.conversation)).get(ConversationViewModel.class);
        userViewModel = WfcUIKit.getAppScopeViewModel(UserViewModel.class);
        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        String userId = userViewModel.getUserId();
        groupLinearLayout_0.setVisibility(View.VISIBLE);
        groupLinearLayout_1.setVisibility(View.VISIBLE);
        markGroupLinearLayout.setVisibility(View.VISIBLE);
        markGroupSwitchButton.setOnCheckedChangeListener(this);
        quitGroupButton.setVisibility(View.VISIBLE);

        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        List<GroupMember> groupMembers = groupViewModel.getGroupMembers(conversationInfo.conversation.target, true);
        List<String> memberIds = new ArrayList<>();
        for (GroupMember member : groupMembers) {
            if (member.memberId.equals(userId)) {
                groupMember = member;
            }
            memberIds.add(member.memberId);
        }
        groupInfo = groupViewModel.getGroupInfo(conversationInfo.conversation.target, false);

        if (groupMember == null || groupInfo == null) {
            Toast.makeText(getActivity(), "你不在群组或发生错误, 请稍后再试", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }

        SharedPreferences sp = getActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE);
        String showAliasKey = String.format(Config.SP_KEY_SHOW_GROUP_MEMBER_ALIAS, groupInfo.target);
        showGroupMemberNickNameSwitchButton.setChecked(sp.getBoolean(showAliasKey, false));
        showGroupMemberNickNameSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sp.edit()
                    .putBoolean(showAliasKey, isChecked)
                    .apply();
            ConfigEventViewModel configEventViewModel = WfcUIKit.getAppScopeViewModel(ConfigEventViewModel.class);
            configEventViewModel.postGroupAliasEvent(groupInfo.target, isChecked);
        });

        boolean enableRemoveMember = false;
        if (groupMember.type != GroupMember.GroupMemberType.Normal || userId.equals(groupInfo.owner)) {
            enableRemoveMember = true;
        }
        conversationMemberAdapter = new ConversationMemberAdapter(true, enableRemoveMember);
        List<UserInfo> members = contactViewModel.getContacts(memberIds);

        for (GroupMember member : groupMembers) {
            for (UserInfo userInfo : members) {
                if (!TextUtils.isEmpty(member.alias) && member.memberId.equals(userInfo.uid)) {
                    userInfo.displayName = member.alias;
                    break;
                }
            }
        }
        myGroupNickNameOptionItemView.setRightText(groupMember.alias);
        groupNameOptionItemView.setRightText(groupInfo.name);

        conversationMemberAdapter.setMembers(members);
        conversationMemberAdapter.setOnMemberClickListener(this);

        memberReclerView.setAdapter(conversationMemberAdapter);
        memberReclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        stickTopSwitchButton.setChecked(conversationInfo.isTop);
        silentSwitchButton.setChecked(conversationInfo.isSilent);
        stickTopSwitchButton.setOnCheckedChangeListener(this);
        silentSwitchButton.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.groupNameOptionItemView)
    void updateGroupName() {
        Intent intent = new Intent(getActivity(), SetGroupNameActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivityForResult(intent, REQUEST_CODE_SET_GROUP_NAME);
    }

    @OnClick(R.id.groupNoticeLinearLayout)
    void updateGroupNotice() {
        // TODO
    }

    @OnClick(R.id.groupManageOptionItemView)
    void manageGroup() {
        // TODO
    }

    @OnClick(R.id.myGroupNickNameOptionItemView)
    void updateMyGroupAlias() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .input("请输入你的群昵称", groupMember.alias, false, (dialog1, input) -> {
                    groupViewModel.modifyMyGroupAlias(groupInfo.target, input.toString().trim())
                            .observe(GroupConversationInfoFragment.this, new Observer<OperateResult>() {
                                @Override
                                public void onChanged(@Nullable OperateResult operateResult) {
                                    ConfigEventViewModel configEventViewModel = WfcUIKit.getAppScopeViewModel(ConfigEventViewModel.class);
                                    configEventViewModel.postGroupAliasEvent(groupInfo.target, true);
                                    if (operateResult.isSuccess()) {
                                        myGroupNickNameOptionItemView.setRightText(input.toString().trim());
                                    } else {
                                        Toast.makeText(getActivity(), "修改群昵称失败:" + operateResult.getErrorCode(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                })
                .negativeText("取消")
                .positiveText("确定")
                .onPositive((dialog12, which) -> {
                    dialog12.dismiss();
                })
                .build();
        dialog.show();
    }

    @OnClick(R.id.quitButton)
    void quitGroup() {
        groupViewModel.quitGroup(conversationInfo.conversation.target, Collections.singletonList(0)).observe(this, aBoolean -> {
            if (aBoolean != null && aBoolean) {
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "退出群组失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.clearMessagesOptionItemView)
    void clearMessage() {
        conversationViewModel.clearConversationMessage(conversationInfo.conversation);
    }

    @OnClick(R.id.groupQRCodeOptionItemView)
    void showGroupQRCode() {
        String qrCodeValue = WfcScheme.QR_CODE_PREFIX_GROUP + groupInfo.target;
        Intent intent = QRCodeActivity.buildQRCodeIntent(getActivity(), "群二维码", groupInfo.portrait, qrCodeValue);
        startActivity(intent);
    }

    @Override
    public void onUserMemberClick(UserInfo userInfo) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    @Override
    public void onAddMemberClick() {
        Intent intent = new Intent(getActivity(), AddGroupMemberActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivityForResult(intent, REQUEST_ADD_MEMBER);
    }

    @Override
    public void onRemoveMemberClick() {
        if (groupInfo != null) {
            Intent intent = new Intent(getActivity(), RemoveGroupMemberActivity.class);
            intent.putExtra("groupInfo", groupInfo);
            startActivityForResult(intent, REQUEST_REMOVE_MEMBER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_MEMBER:
                if (resultCode == AddGroupMemberActivity.RESULT_ADD_SUCCESS) {
                    List<String> memberIds = data.getStringArrayListExtra("memberIds");
                    addGroupMember(memberIds);
                }
                break;
            case REQUEST_REMOVE_MEMBER:
                if (resultCode == RemoveGroupMemberActivity.RESULT_REMOVE_SUCCESS) {
                    List<String> memberIds = data.getStringArrayListExtra("memberIds");
                    removeGroupMember(memberIds);
                }
                break;
            case REQUEST_CODE_SET_GROUP_NAME:
                if (resultCode == SetGroupNameActivity.RESULT_SET_GROUP_NAME_SUCCESS) {
                    groupNameOptionItemView.setRightText(data.getStringExtra("groupName"));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void addGroupMember(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return;
        }
        List<UserInfo> userInfos = userViewModel.getUserInfos(memberIds);
        if (userInfos == null) {
            return;
        }
        conversationMemberAdapter.addMembers(userInfos);
    }

    private void removeGroupMember(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return;
        }
        conversationMemberAdapter.removeMembers(memberIds);
    }

    private void stickTop(boolean top) {
        ConversationListViewModel conversationListViewModel = ViewModelProviders
                .of(this, new ConversationListViewModelFactory(Arrays.asList(Conversation.ConversationType.Single, Conversation.ConversationType.Group, Conversation.ConversationType.Channel), Arrays.asList(0)))
                .get(ConversationListViewModel.class);
        conversationListViewModel.setConversationTop(conversationInfo, top);
    }

    private void markGroup(boolean mark) {
        groupViewModel.setFavGroup(groupInfo.target, mark);
    }

    private void silent(boolean silent) {
        conversationViewModel.setConversationSilent(conversationInfo.conversation, silent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.markGroupSwitchButton:
                markGroup(isChecked);
                break;
            case R.id.stickTopSwitchButton:
                stickTop(isChecked);
                break;
            case R.id.silentSwitchButton:
                silent(isChecked);
                break;
            default:
                break;
        }

    }
}
