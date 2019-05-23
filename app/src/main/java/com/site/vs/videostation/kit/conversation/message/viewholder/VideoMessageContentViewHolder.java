package com.site.vs.videostation.kit.conversation.message.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.site.vs.videostation.kit.annotation.EnableContextMenu;
import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.annotation.MessageContentType;
import com.site.vs.videostation.kit.conversation.ConversationMessageAdapter;
import com.site.vs.videostation.kit.conversation.message.model.UiMessage;
import com.site.vs.videostation.kit.preview.MMPreviewActivity;
import com.site.vs.videostation.kit.widget.BubbleImageView;
import com.site.vs.videostation.R;
import cn.wildfirechat.message.VideoMessageContent;

@MessageContentType(VideoMessageContent.class)
@LayoutRes(resId = R.layout.conversation_item_video_send)
@EnableContextMenu
public class VideoMessageContentViewHolder extends MediaMessageContentViewHolder {
    @BindView(R.id.imageView)
    BubbleImageView imageView;
    @BindView(R.id.playImageView)
    ImageView playImageView;

    public VideoMessageContentViewHolder(FragmentActivity context, RecyclerView.Adapter adapter, View itemView) {
        super(context, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        VideoMessageContent fileMessage = (VideoMessageContent) message.message.content;
        if (fileMessage.getThumbnail() != null && fileMessage.getThumbnail().getWidth() > 0) {
            imageView.setImageBitmap(fileMessage.getThumbnail());
            playImageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.mipmap.img_video_default);
            playImageView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.videoContentLayout)
    void play() {
        List<UiMessage> messages = ((ConversationMessageAdapter) adapter).getMessages();
        List<UiMessage> mmMessages = new ArrayList<>();
        for (UiMessage msg : messages) {
            if (msg.message.content.getType() == cn.wildfirechat.message.core.MessageContentType.ContentType_Image
                    || msg.message.content.getType() == cn.wildfirechat.message.core.MessageContentType.ContentType_Video) {
                mmMessages.add(msg);
            }
        }
        if (mmMessages.isEmpty()) {
            return;
        }

        int current = 0;
        for (int i = 0; i < mmMessages.size(); i++) {
            if (message.message.messageId == mmMessages.get(i).message.messageId) {
                current = i;
                break;
            }
        }
        MMPreviewActivity.startActivity(context, mmMessages, current);
    }

    @Override
    public boolean contextMenuItemFilter(UiMessage uiMessage, String tag) {
        if (MessageContextMenuItemTags.TAG_FORWARD.equals(tag)) {
            return true;
        } else {
            return super.contextMenuItemFilter(uiMessage, tag);
        }
    }
}
