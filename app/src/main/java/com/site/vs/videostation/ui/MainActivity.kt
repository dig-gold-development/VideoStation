package com.site.vs.videostation.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cn.wildfirechat.model.Conversation
import com.orhanobut.logger.Logger
import com.site.vs.videostation.R
import com.site.vs.videostation.base.BaseActivity
import com.site.vs.videostation.kit.contact.ContactFragment
import com.site.vs.videostation.kit.contact.ContactViewModel
import com.site.vs.videostation.kit.conversationlist.ConversationListFragment
import com.site.vs.videostation.kit.conversationlist.ConversationListViewModel
import com.site.vs.videostation.kit.conversationlist.ConversationListViewModelFactory
import com.site.vs.videostation.ui.homepage.MainChannelFragment
import com.site.vs.videostation.ui.homepage.MainHomeFragment
import com.site.vs.videostation.ui.homepage.MainRankingFragment
import com.site.vs.videostation.ui.login.MeFragment
import com.zhusx.core.utils._Activitys._addFragment
import kotlinx.android.synthetic.main.activity_main.*
import q.rorbin.badgeview.QBadgeView
import java.util.*

class MainActivity : BaseActivity() {

    var fragments = arrayOfNulls<Fragment>(6)
    private var currentFragment: Fragment? = null

    var exitDialog: AlertDialog? = null

    private var unreadMessageUnreadBadgeView: QBadgeView? = null
    private var unreadFriendRequestBadgeView: QBadgeView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var i = 0
            when (checkedId) {
                R.id.radio_home -> {
                    i = 0
                    if (fragments[i] == null) {
                        fragments[i] = MainHomeFragment()
                    }
                }

                R.id.radio_channel -> {
                    i = 1
                    if (fragments[i] == null) {
                        fragments[i] = MainChannelFragment()
                    }
                }

                R.id.radio_rank -> {
                    i = 2
                    if (fragments[i] == null) {
                        fragments[i] = MainRankingFragment()
                    }
                }

                R.id.conversation -> {
                    i = 3
                    if (fragments[i] == null) {
                        fragments[i] = ConversationListFragment()
                    }

                }

                R.id.contact -> {
                    i = 4
                    if (fragments[i] == null) {
                        fragments[i] = ContactFragment()
                    }

                }


                R.id.mine -> {
                    i = 5
                    if (fragments[i] == null) {
                        fragments[i] = MeFragment()
                    }

                }


            }

            if (fragments[i] == null) {
                fragments[i] = MainHomeFragment()
            }
            showFragment(fragments[i]!!)
        }
        radioGroup.check(R.id.radio_home)
        initUnread()

    }

    fun initUnread() {
        val conversationListViewModel = ViewModelProviders
                .of(this, ConversationListViewModelFactory(Arrays.asList<Conversation.ConversationType>(Conversation.ConversationType.Single, Conversation.ConversationType.Group, Conversation.ConversationType.Channel), Arrays.asList<Int>(0)))
                .get(ConversationListViewModel::class.java)
        conversationListViewModel.unreadCountLiveData().observe(this,androidx.lifecycle.Observer {
            unreadCount ->

            if (unreadCount != null && unreadCount!!.unread > 0) {

                if (unreadMessageUnreadBadgeView == null) {

                    val view = radioLine.getChildAt(3)
                    unreadMessageUnreadBadgeView = QBadgeView(this@MainActivity)
                    unreadMessageUnreadBadgeView?.let {
                        it.bindTarget(view)
                    }
                }

                unreadMessageUnreadBadgeView?.let {
                    it.setBadgeNumber(unreadCount!!.unread)
                }



            } else if (unreadMessageUnreadBadgeView != null) {
                unreadMessageUnreadBadgeView.let {
                    it?.hide(true)
                }

            }

        } )

        val contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        contactViewModel.friendRequestUpdatedLiveData().observe(this,androidx.lifecycle.Observer {
            count ->
            if (count == null || count === 0) {
                if (unreadFriendRequestBadgeView != null) {
                    unreadFriendRequestBadgeView?.let {
                        it.hide(true)
                    }

                }
            } else {
                if (unreadFriendRequestBadgeView == null) {

                    val view = radioLine.getChildAt(4)
                    unreadFriendRequestBadgeView = QBadgeView(this@MainActivity)
                    unreadFriendRequestBadgeView?.let {
                        it.bindTarget(view)
                    }
                }

                unreadFriendRequestBadgeView?.let {
                    it.setBadgeNumber(count!!.toInt())
                }


            }
        })
    }



    fun showFragment(fragment: Fragment) {
        Logger.e("showFragment $fragment")
        if (currentFragment != null) {
            if (currentFragment === fragment) {
                return
            }
        }

        _addFragment(this, R.id.content, currentFragment, fragment)
        currentFragment = fragment


    }

    override fun onBackPressed() {
        if (exitDialog == null) {
            exitDialog = AlertDialog.Builder(this).setTitle(title).setIcon(R.mipmap.ic_launcher)
                    .setMessage("确认退出" + title + "吗？").setNegativeButton("再看看", null)
                    .setPositiveButton("退出") { _, _ -> finish() }.create()

        }
        exitDialog!!.show()
    }

    fun hideUnreadFriendRequestBadgeView() {

    }
}

