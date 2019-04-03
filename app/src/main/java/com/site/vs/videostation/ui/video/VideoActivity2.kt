package com.site.vs.videostation.ui.video

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.github.tcking.viewquery.ViewQuery
import com.site.vs.videostation.R
import com.site.vs.videostation.base.MVPBaseActivity
import com.site.vs.videostation.entity.DetailEntity
import com.site.vs.videostation.entity.MoveAddressEntity
import com.site.vs.videostation.ui.detail.presentation.PlayContract
import com.site.vs.videostation.ui.detail.presentation.PlayPresenter
import tcking.github.com.giraffeplayer2.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer


class VideoActivity2 : MVPBaseActivity<PlayPresenter>(), PlayContract.View {

    private var realUrl: Int = 0
    private var originIndex = 0
    private var playIndex = 0
    private var entity: DetailEntity? = null
    private var url: String? = null
    private var currentPos: Int = 0
    lateinit var videoView: VideoView
    lateinit var viewQuery: ViewQuery

    override fun createPresenter() {
        mPresenter = PlayPresenter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video2)
        GiraffePlayer.debug = true//show java logs
        GiraffePlayer.nativeDebug = false//not show native logs


        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }


        PlayerManager.getInstance().defaultVideoInfo.addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                                                                             "multiple_requests", 1L))


        initData(intent)
        initPlayer()


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            initData(it)
        }
    }


    private fun initData(intent: Intent) {
        originIndex = intent.getIntExtra(ORIGIN_INDEX, 0)
        playIndex = intent.getIntExtra(PLAY_INDEX, 0)
        entity = intent.getBundleExtra(DATA).getSerializable(DATA) as DetailEntity
        url = intent.getStringExtra(URL)
        title = intent.getStringExtra(TITLE)
        currentPos = intent.getIntExtra(TIME, 0)
    }


    private fun initPlayer() {
        viewQuery = ViewQuery(this)

        videoView = viewQuery.id(R.id.video_view).view()

        url = url?.replace("\r\n", "")?.replace("\t", "")?.replace(" ", "")

        videoView.setVideoPath(url)

        videoView.getPlayer().aspectRatio(VideoInfo.AR_ASPECT_FILL_PARENT)
        PlayerManager.getInstance()
                .mediaControllerGenerator = PlayerManager.MediaControllerGenerator { context, videoInfo ->
            DefaultMediaController(context)
        }

        videoView.getPlayer().toggleFullScreen()
        if (!videoView.player.isPlaying)
            videoView.player.start()

    }

    override fun playMoveSuccess(entity: MoveAddressEntity?, title: String?) {
    }

    override fun playWebMoveSuccess(msg: String?) {
    }

    override fun playMoveFailed(msg: String?) {
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                Toast.makeText(this, "please grant read permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        PlayerManager.getInstance().onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if (PlayerManager.getInstance().onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    companion object {

        val TITLE = "title"
        val URL = "url"
        val ORIGIN_INDEX = "origin_index"
        val PLAY_INDEX = "play_index"
        val DATA = "data"
        val TIME = "time"

        fun playVideo(activity: Activity, title: String?, url: String, entity: DetailEntity, originIndex: Int, playIndex: Int, time: Int) {
            val intent = Intent(activity, VideoActivity2::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(URL, url)
            intent.putExtra(ORIGIN_INDEX, originIndex)
            intent.putExtra(PLAY_INDEX, playIndex)
            val bundle = Bundle()
            bundle.putSerializable(DATA, entity)
            intent.putExtra(DATA, bundle)
            intent.putExtra(TIME, time)

            activity.startActivity(intent)
        }
    }
}