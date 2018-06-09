package com.sherman.getwords.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.WordBean;
import com.sherman.getwords.bean.WordDBUtil;
import com.sherman.getwords.bean.WordResponse;
import com.sherman.getwords.roundView.RoundSpinView;
import com.sherman.getwords.utils.Constants;
import com.sherman.getwords.utils.SharedPreferencesHelper;
import com.sherman.getwords.videomanage.controller.VideoControllerView;
import com.sherman.getwords.videomanage.controller.ViewAnimator;
import com.sherman.getwords.videomanage.manager.SingleVideoPlayerManager;
import com.sherman.getwords.videomanage.manager.VideoPlayerManager;
import com.sherman.getwords.videomanage.meta.CurrentItemMetaData;
import com.sherman.getwords.videomanage.meta.MetaData;
import com.sherman.getwords.videomanage.ui.MediaPlayerWrapper;
import com.sherman.getwords.videomanage.ui.VideoPlayerView;
import com.sherman.getwords.view.CardGroupView;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeCheckFragment extends Fragment implements RoundSpinView.onRoundSpinViewListener {

    private RoundSpinView roundSpinView;
    SharedPreferencesHelper sharedPreferencesHelper;

    private CardGroupView mCardGroupView;

    List<WordBean> datas;
    int index;

    private static FrameLayout mVideoFloatContainer;
    private static View mVideoPlayerBg;
    private static ImageView mVideoCoverMask;
    private static VideoPlayerView mVideoPlayerView;
    private static View mVideoLoadingView;
    private static ProgressBar mVideoProgressBar;

    private ProgressBar progress_center;

    private RealmAsyncTask addTask;

    private static VideoControllerView mCurrentVideoControllerView;
    private static int mCurrentActiveVideoItem = -1;

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static int mCurrentBuffer;

    private static boolean mCanTriggerStop = true;

    private static VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(null);
    private static boolean mIsClickToStop;

    private static float mOriginalHeight;

    private static float mMoveDeltaY;

    String[] strings1 = new String[]{"今天","随数","两种","博原","技术","使用","机会","了。","间数","户有"};
    String[] strings2 = new String[]{"瞥气","三月","三寒","丰登","淋漓","可耐","逼人","空。","气爽","累累"};
    String[] strings3 = new String[]{"飞舞","冰封","成冰","口呆","眼快","昂胸","摩踵","舌。","愁脸","指脚"};

    public static HomeCheckFragment newInstance(String s){
        HomeCheckFragment homeFragment = new HomeCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS,s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_content, container, false);

        roundSpinView = (RoundSpinView)view.findViewById(R.id.roundView);

        progress_center = view.findViewById(R.id.progress_center);

        mCardGroupView = view.findViewById(R.id.card);

        roundSpinView.setOnRoundSpinViewListener(this);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity().getApplication());
        //本地数据库是否有单词库
        RealmResults<WordBean> wordResults = Realm.getDefaultInstance().where(WordBean.class).lessThanOrEqualTo("remenberNum",3).findAllAsync();
        wordResults.addChangeListener(new RealmChangeListener<RealmResults<WordBean>>() {
            @Override
            public void onChange(RealmResults<WordBean> element) {
                datas = Realm.getDefaultInstance().copyFromRealm(element);
                if (!datas.isEmpty()){

                    Collections.shuffle(datas);
                    roundSpinView.setData(datas.get(index),strings1[new Random().nextInt(9)],strings2[new Random().nextInt(9)],strings3[new Random().nextInt(9)]);

                    progress_center.setVisibility(View.GONE);
                    roundSpinView.setVisibility(View.VISIBLE);
                    roundSpinView.invalidate();
                }else {
                    //获取单词库
                    OkGo.<WordResponse>post(UrlBean.getAllWord)
                            .params("token",sharedPreferencesHelper.getString("token",""))
                            .params("uploadTime",0)
                            .isMultipart(true).tag(this).execute(new JsonCallback<WordResponse>(WordResponse.class) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<WordResponse> response) {
                            if (response.body().getCode().equals("200")){
                                progress_center.setVisibility(View.GONE);
                                roundSpinView.setVisibility(View.VISIBLE);
                                datas = response.body().getData();
                                WordDBUtil.getInstance().insert(datas);

                                roundSpinView.setData(datas.get(index),strings1[new Random().nextInt(9)],strings2[new Random().nextInt(9)],strings3[new Random().nextInt(9)]);
                                roundSpinView.invalidate();
                            }

                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onSingleTapUp(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0:
//                Toast.makeText(getActivity(), "place:0", Toast.LENGTH_LONG).show();
                break;
            case 1:
//                Toast.makeText(getActivity(), "place:1", Toast.LENGTH_LONG).show();
                break;
            case 2:
//                Toast.makeText(getActivity(), "place:2", Toast.LENGTH_LONG).show();
                break;
            case 3:
//                Toast.makeText(getActivity(), "place:3", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMoveTapUp(int position) {

        if (position == 0){
            Toast.makeText(getActivity(), "正确", Toast.LENGTH_LONG).show();
            index ++;
            if (index < datas.size()){
                roundSpinView.setData(datas.get(index),strings1[new Random().nextInt(9)],strings2[new Random().nextInt(9)],strings3[new Random().nextInt(9)]);
                roundSpinView.invalidate();
            }
        }else {
            //弹出提示
            mCardGroupView.setVisibility(View.VISIBLE);
            mCardGroupView.addView(getCard(datas.get(index).getWord(),datas.get(index).getSoundMark(),datas.get(index).getMeaning(),datas.get(index).getVideoUrl()));
        }
    }

    private View getCard(String title,String soundMark,String info,String videoUrl) {
        View card = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_video_txt, null);
        TextView cardTitle = (TextView)card.findViewById(R.id.card_title);
        TextView card_soundMark = card.findViewById(R.id.card_soundMark);
        TextView cardInfo = (TextView)card.findViewById(R.id.card_info);
        mVideoFloatContainer = card.findViewById(R.id.layout_float_video_container);
        mVideoPlayerBg = card.findViewById(R.id.video_player_bg);
        mVideoCoverMask = card.findViewById(R.id.video_player_mask);
        mVideoPlayerView = card.findViewById(R.id.video_player_view);
        mVideoLoadingView = card.findViewById(R.id.video_progress_loading);
        mVideoProgressBar = card.findViewById(R.id.video_progress_bar);

        cardTitle.setText(title);
        card_soundMark.setText(soundMark);
        cardInfo.setText(info);

        mVideoPlayerView.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
            @Override
            public void onVideoSizeChangedMainThread(int width, int height) {

            }

            @Override
            public void onVideoPreparedMainThread() {

                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoPreparedMainThread");
                mVideoFloatContainer.setVisibility(View.VISIBLE);
                mVideoPlayerView.setVisibility(View.VISIBLE);
                mVideoLoadingView.setVisibility(View.VISIBLE);
                //for cover the pre Video frame
                mVideoCoverMask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {

                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoCompletionMainThread");

                mVideoFloatContainer.setVisibility(View.INVISIBLE);
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                ViewAnimator.putOn(mVideoFloatContainer).translationY(0);

                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onErrorMainThread(int what, int extra) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onErrorMainThread");
                mVideoFloatContainer.setVisibility(View.INVISIBLE);

                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onBufferingUpdateMainThread(int percent) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onBufferingUpdateMainThread");
                mCurrentBuffer = percent;
            }

            @Override
            public void onVideoStoppedMainThread() {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoStoppedMainThread");
                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onInfoMainThread(int what) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onInfoMainThread what:" + what);
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {

                    //start update progress
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                    mHandler.post(mProgressRunnable);

                    mVideoPlayerView.setVisibility(View.VISIBLE);
                    mVideoLoadingView.setVisibility(View.GONE);
                    mVideoCoverMask.setVisibility(View.GONE);
                    mVideoPlayerBg.setVisibility(View.VISIBLE);
                    createVideoControllerView();

                    mCurrentVideoControllerView.showWithTitle("VIDEO TEST - " + mCurrentActiveVideoItem);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    mVideoLoadingView.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    mVideoLoadingView.setVisibility(View.GONE);
                }
            }
        });

        mIsClickToStop = true;

        //invisible self ,and make visible when video play completely
        if (mCurrentVideoControllerView != null)
            mCurrentVideoControllerView.hide();

        mVideoFloatContainer.setVisibility(View.VISIBLE);
        mVideoCoverMask.setVisibility(View.GONE);
        mVideoPlayerBg.setVisibility(View.GONE);

        mCanTriggerStop = true;

        //move container view
        startMoveFloatContainer(true);

        mVideoLoadingView.setVisibility(View.VISIBLE);
        mVideoPlayerView.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(videoUrl)){
            playbackImmediately(videoUrl);
        }

        return card;
    }

    private void startMoveFloatContainer(boolean click) {

        if (mVideoFloatContainer.getVisibility() != View.VISIBLE) return;
        final float moveDelta;

        if (click) {
            ViewAnimator.putOn(mVideoFloatContainer).translationY(0);

            int[] playAreaPos = new int[2];
            int[] floatContainerPos = new int[2];
            mVideoFloatContainer.getLocationOnScreen(floatContainerPos);
            mOriginalHeight = moveDelta = playAreaPos[1] - floatContainerPos[1];

        } else {
            moveDelta = mMoveDeltaY;
        }

        float translationY = moveDelta + (!click ? mOriginalHeight : 0);

        ViewAnimator.putOn(mVideoFloatContainer).translationY(translationY);
    }

    public void playbackImmediately(String videoUrl) {
        mIsClickToStop = true;
        if (mVideoPlayerManager != null) {
            mVideoFloatContainer.setVisibility(View.VISIBLE);
            mVideoPlayerManager.playNewVideo(new CurrentItemMetaData(0, mVideoPlayerView), mVideoPlayerView, videoUrl);
        }
    }

    public void stopPlaybackImmediately() {

        mIsClickToStop = false;
        if (mVideoPlayerManager != null) {
            if (mVideoFloatContainer != null){
                mVideoFloatContainer.setVisibility(View.INVISIBLE);

            }
            mVideoPlayerManager.stopAnyPlayback();
        }
    }

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if(mPlayerControlListener != null){

                if(mCurrentVideoControllerView.isShowing()){
                    mVideoProgressBar.setVisibility(View.GONE);
                }else {
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                }

                int position = mPlayerControlListener.getCurrentPosition();
                int duration = mPlayerControlListener.getDuration();
                if(duration != 0) {
                    long pos = 1000L * position / duration;
                    int percent = mPlayerControlListener.getBufferPercentage() * 10;
                    mVideoProgressBar.setProgress((int) pos);
                    mVideoProgressBar.setSecondaryProgress(percent);
                    mHandler.postDelayed(this,1000);
                }
            }
        }
    };

    private void createVideoControllerView() {

        if (mCurrentVideoControllerView != null) {
            mCurrentVideoControllerView.hide();
            mCurrentVideoControllerView = null;
        }

        mCurrentVideoControllerView = new VideoControllerView.Builder(getActivity(), mPlayerControlListener)
                .withVideoView(mVideoPlayerView)//to enable toggle display controller view
                .canControlBrightness(true)
                .canControlVolume(true)
                .canSeekVideo(false)
                .exitIcon(R.drawable.video_top_back)
                .pauseIcon(R.drawable.ic_media_pause)
                .playIcon(R.drawable.ic_media_play)
                .shrinkIcon(R.drawable.ic_media_fullscreen_shrink)
                .stretchIcon(R.drawable.ic_media_fullscreen_stretch)
                .build(mVideoFloatContainer);//layout container that hold video play view
    }

    private boolean checkMediaPlayerInvalid() {
        return mVideoPlayerView != null && mVideoPlayerView.getMediaPlayer() != null;
    }

    private VideoControllerView.MediaPlayerControlListener mPlayerControlListener = new VideoControllerView.MediaPlayerControlListener() {
        @Override
        public void start() {
            if (checkMediaPlayerInvalid())
                mVideoPlayerView.getMediaPlayer().start();
        }

        @Override
        public void pause() {
            mVideoPlayerView.getMediaPlayer().pause();
        }

        @Override
        public int getDuration() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().getDuration();
            }
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().getCurrentPosition();
            }
            return 0;
        }

        @Override
        public void seekTo(int position) {
            if (checkMediaPlayerInvalid()) {
                mVideoPlayerView.getMediaPlayer().seekToPosition(position);
            }
        }

        @Override
        public boolean isPlaying() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().isPlaying();
            }
            return false;
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public int getBufferPercentage() {
            return mCurrentBuffer;
        }

        @Override
        public boolean isFullScreen() {
            return true;
//            return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                    || getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        }

        @Override
        public void toggleFullScreen() {
            if (isFullScreen()) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    getActivity().setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
//                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
//                            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
//                    getActivity().setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
//                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
//                            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }

        @Override
        public void exit() {
            //TODO to handle exit status
            if (isFullScreen()) {
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        stopPlaybackImmediately();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (addTask != null && !addTask.isCancelled()) {
            addTask.cancel();
        }
    }
}
