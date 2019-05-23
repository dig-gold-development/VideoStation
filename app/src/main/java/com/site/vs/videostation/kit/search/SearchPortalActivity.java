package com.site.vs.videostation.kit.search;

import java.util.List;

import com.site.vs.videostation.kit.search.module.ChannelSearchModule;
import com.site.vs.videostation.kit.search.module.ContactSearchModule;
import com.site.vs.videostation.kit.search.module.ConversationSearchModule;
import com.site.vs.videostation.kit.search.module.GroupSearchViewModule;

public class SearchPortalActivity extends SearchActivity {
    @Override
    protected void initSearchModule(List<SearchableModule> modules) {

        SearchableModule module = new ContactSearchModule();
        modules.add(module);

        module = new GroupSearchViewModule();
        modules.add(module);

        module = new ConversationSearchModule();
        modules.add(module);
        modules.add(new ChannelSearchModule());
    }
}
