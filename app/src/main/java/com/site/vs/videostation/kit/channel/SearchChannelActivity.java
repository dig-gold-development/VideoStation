package com.site.vs.videostation.kit.channel;

import java.util.List;

import com.site.vs.videostation.kit.search.SearchActivity;
import com.site.vs.videostation.kit.search.SearchableModule;
import com.site.vs.videostation.kit.search.module.ChannelSearchModule;

public class SearchChannelActivity extends SearchActivity {
    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        modules.add(new ChannelSearchModule());
    }
}
