package com.site.vs.videostation.kit.conversation.message.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import com.site.vs.videostation.kit.annotation.EnableContextMenu;
import com.site.vs.videostation.kit.annotation.MessageContentType;
import com.site.vs.videostation.kit.annotation.ReceiveLayoutRes;
import com.site.vs.videostation.kit.annotation.SendLayoutRes;
import com.site.vs.videostation.kit.conversation.message.model.UiMessage;
import com.site.vs.videostation.R;
import cn.wildfirechat.message.UnknownMessageContent;

@MessageContentType(UnknownMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_unknown_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_unknown_receive)
@EnableContextMenu
public class UnkownMessageContentViewHolder extends NormalMessageContentViewHolder {
    @BindView(R.id.contentTextView)
    TextView contentTextView;

    public UnkownMessageContentViewHolder(FragmentActivity context, RecyclerView.Adapter adapter, View itemView) {
        super(context, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        contentTextView.setText("unknown message");
    }
}
