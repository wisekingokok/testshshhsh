package com.sherman.getwords.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lin.cardlib.CardLayoutManager;
import com.lin.cardlib.CardSetting;
import com.lin.cardlib.CardTouchHelperCallback;
import com.lin.cardlib.OnSwipeCardListener;
import com.lin.cardlib.utils.ReItemTouchHelper;
import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.adapter.CardAdapter;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.WordBean;
import com.sherman.getwords.bean.WordDBUtil;
import com.sherman.getwords.bean.WordResponse;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeRememberFragment extends Fragment{

    RecyclerView mRecyclerView;
    CardGroupView mCardGroupView;
    ReItemTouchHelper mReItemTouchHelper;
    SharedPreferencesHelper sharedPreferencesHelper;

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

    private List<WordBean> wordBeanList;

    public static HomeRememberFragment newInstance(String s){
        HomeRememberFragment homeFragment = new HomeRememberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS,s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remenber_content, container, false);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity().getApplication());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        progress_center = view.findViewById(R.id.progress_center);

        mCardGroupView = (CardGroupView) view.findViewById(R.id.card);
        mCardGroupView.setLoadSize(1);

        wordBeanList = new ArrayList<>();
        initEvent();

        //本地数据库是否有单词库
        RealmResults<WordBean> wordResults = Realm.getDefaultInstance().where(WordBean.class).lessThanOrEqualTo("remenberNum",3).isNotNull("videoUrl").findAllAsync();
        wordResults.addChangeListener(new RealmChangeListener<RealmResults<WordBean>>() {
            @Override
            public void onChange(RealmResults<WordBean> element) {
                List<WordBean> datas = Realm.getDefaultInstance().copyFromRealm(element);
                if (!datas.isEmpty()){

//                    Collections.shuffle(datas);
                    initData(datas);
                    progress_center.setVisibility(View.GONE);
                }else {
                    //获取单词库
                    OkGo.<WordResponse>post(UrlBean.getAllWord)
                            .params("token",sharedPreferencesHelper.getString("token",""))
                            .params("uploadTime",0)
                            .isMultipart(true).tag(this).execute(new JsonCallback<WordResponse>(WordResponse.class) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<WordResponse> response) {
                            List<WordBean> list = response.body().getData();
                            WordDBUtil.getInstance().insert(list);

                            Collections.shuffle(list);
                            initData(list);

                            progress_center.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }

    private void initEvent() {
        mCardGroupView.setLeftOrRightListener(new CardGroupView.LeftOrRight() {
            @Override
            public void leftOrRight(boolean left) {
                if (left) {
                    stopPlaybackImmediately();
                } else {
                    stopPlaybackImmediately();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlaybackImmediately();
    }

    private void addCard(String title, String soundMark, String info, String videoUrl) {
        mCardGroupView.addView(getCard(title,soundMark,info,videoUrl));
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
        playbackImmediately(videoUrl);

        return card;
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
            if(mVideoFloatContainer != null){
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

    private void initData(List<WordBean> listBean){
        wordBeanList.addAll(listBean);
        CardSetting setting=new CardSetting();
        setting.setSwipeListener(new OnSwipeCardListener<WordBean>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float dx, float dy, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
                        Log.e("aaa", "swiping direction=down");
                        break;
                    case ReItemTouchHelper.UP:
                        Log.e("aaa", "swiping direction=up");
                        break;
                    case ReItemTouchHelper.LEFT:
                        Log.e("aaa", "swiping direction=left");
                        break;
                    case ReItemTouchHelper.RIGHT:
                        Log.e("aaa", "swiping direction=right");
                        break;
                }
            }

            @Override
            public void onSwipedOut(RecyclerView.ViewHolder viewHolder, WordBean cardBean, int direction) {
                switch (direction) {
                    case ReItemTouchHelper.DOWN:
//                        Toast.makeText(getActivity(), "swipe down out", Toast.LENGTH_SHORT).show();
                        break;
                    case ReItemTouchHelper.UP:
//                        Toast.makeText(getActivity(), "swipe up out ", Toast.LENGTH_SHORT).show();
                        break;
                    case ReItemTouchHelper.LEFT:
//                        Toast.makeText(getActivity(), "swipe left out", Toast.LENGTH_SHORT).show();
                        //记录次数
                        remenberNum(cardBean.getWord());

                        break;
                    case ReItemTouchHelper.RIGHT:
//                        Toast.makeText(getActivity(), "swipe right out", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), ActivityDialog.class);
//                        intent.putExtra("url",o.getVideoUrl());
//                        startActivity(intent);
                        if (!TextUtils.isEmpty(cardBean.getVideoUrl())){
                            addCard(cardBean.getWord(),cardBean.getSoundMark(),cardBean.getMeaning(),cardBean.getVideoUrl());
                        }
                        break;
                }
            }

            @Override
            public void onSwipedClear() {
//                Toast.makeText(getActivity(), "cards are consumed", Toast.LENGTH_SHORT).show();
            }
        });
        CardTouchHelperCallback helperCallback = new CardTouchHelperCallback(mRecyclerView, wordBeanList,setting);
        mReItemTouchHelper = new ReItemTouchHelper(helperCallback);
        CardLayoutManager layoutManager = new CardLayoutManager(mReItemTouchHelper, setting);
        mRecyclerView.setLayoutManager(layoutManager);
        CardAdapter cardAdapter = new CardAdapter(wordBeanList,getActivity().getApplication());
        mRecyclerView.setAdapter(cardAdapter);
    }


    private void remenberNum(final String word){
        addTask = Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                WordBean wordBean = realm.where(WordBean.class).equalTo("word", word).findFirst();
//                realm.beginTransaction();
                if (wordBean != null) {
                    wordBean.setRemenberNum(wordBean.getRemenberNum()+1);
                    realm.copyToRealmOrUpdate(wordBean);
                }
//                realm.commitTransaction();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (addTask != null && !addTask.isCancelled()) {
            addTask.cancel();
        }
    }
}
