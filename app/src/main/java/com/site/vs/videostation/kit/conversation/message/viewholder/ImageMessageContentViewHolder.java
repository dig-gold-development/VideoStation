package com.site.vs.videostation.kit.conversation.message.viewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.site.vs.videostation.kit.GlideApp;
import com.site.vs.videostation.kit.GlideRequest;
import com.site.vs.videostation.kit.annotation.EnableContextMenu;
import com.site.vs.videostation.kit.annotation.MessageContentType;
import com.site.vs.videostation.kit.annotation.ReceiveLayoutRes;
import com.site.vs.videostation.kit.annotation.SendLayoutRes;
import com.site.vs.videostation.kit.conversation.ConversationMessageAdapter;
import com.site.vs.videostation.kit.conversation.message.model.UiMessage;
import com.site.vs.videostation.kit.preview.MMPreviewActivity;
import com.site.vs.videostation.kit.third.utils.UIUtils;
import com.site.vs.videostation.kit.widget.BubbleImageView;
import com.site.vs.videostation.R;
import cn.wildfirechat.message.ImageMessageContent;
import cn.wildfirechat.message.Message;
import cn.wildfirechat.message.MessageContent;
import cn.wildfirechat.message.core.MessageDirection;
import cn.wildfirechat.message.core.MessageStatus;

@MessageContentType(ImageMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_image_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_image_receive)
@EnableContextMenu
public class ImageMessageContentViewHolder extends MediaMessageContentViewHolder {

    @BindView(R.id.imageView)
    BubbleImageView imageView;

    public ImageMessageContentViewHolder(FragmentActivity context, RecyclerView.Adapter adapter, View itemView) {
        super(context, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        ImageMessageContent imageMessage = (ImageMessageContent) message.message.content;
        Bitmap thumbnail = imageMessage.getThumbnail();
        int width = thumbnail != null ? thumbnail.getWidth() : 200;
        int height = thumbnail != null ? thumbnail.getHeight() : 200;
        imageView.getLayoutParams().width = UIUtils.dip2Px(width > 200 ? 200 : width);
        imageView.getLayoutParams().height = UIUtils.dip2Px(height > 200 ? 200 : height);

        if (!TextUtils.isEmpty(imageMessage.localPath)) {
            GlideApp.with(context)
                    .load(imageMessage.localPath)
                    .centerCrop()
                    .into(imageView);
        } else {
            GlideRequest<Drawable> request = GlideApp.with(context)
                    .load(imageMessage.remoteUrl);
            if (thumbnail != null) {
                request = request.placeholder(new BitmapDrawable(context.getResources(), imageMessage.getThumbnail()));
            } else {
                request = request.placeholder(R.mipmap.img_error);
            }
            request.centerCrop()
                    .into(imageView);
        }
    }

    @OnClick(R.id.imageView)
    void preview() {
        // FIXME: 2018/10/3
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
    protected void setSendStatus(Message item) {
        super.setSendStatus(item);
        MessageContent msgContent = item.content;
        if (msgContent instanceof ImageMessageContent) {
            boolean isSend = item.direction == MessageDirection.Send;
            if (isSend) {
                MessageStatus sentStatus = item.status;
                if (sentStatus == MessageStatus.Sending) {
                    imageView.setPercent(message.progress);
                    imageView.setProgressVisible(true);
                    imageView.showShadow(true);
                } else if (sentStatus == MessageStatus.Send_Failure) {
                    imageView.setProgressVisible(false);
                    imageView.showShadow(false);
                } else if (sentStatus == MessageStatus.Sent) {
                    imageView.setProgressVisible(false);
                    imageView.showShadow(false);
                }
            } else {
                imageView.setProgressVisible(false);
                imageView.showShadow(false);
            }
        }
    }
}
