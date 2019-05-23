package com.site.vs.videostation.kit.search;

import java.util.List;

import com.site.vs.videostation.kit.search.module.ConversationMessageSearchModule;
import cn.wildfirechat.model.Conversation;

public class SearchMessageActivity extends SearchActivity {
    private Conversation conversation;

    @Override
    protected void beforeViews() {
        conversation = getIntent().getParcelableExtra("conversation");
    }

    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        modules.add(new ConversationMessageSearchModule(conversation));
    }
}
