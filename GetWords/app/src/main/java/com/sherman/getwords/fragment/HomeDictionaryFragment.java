package com.sherman.getwords.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.sherman.getwords.R;
import com.sherman.getwords.activity.VideoActivity;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.VideoBean;
import com.sherman.getwords.bean.VideoResponse;
import com.sherman.getwords.bean.WordDBUtil;
import com.sherman.getwords.utils.Constants;
import com.sherman.getwords.utils.SharedPreferencesHelper;
import com.sherman.getwords.view.MarginDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeDictionaryFragment extends Fragment implements View.OnClickListener{

    private RefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private GridLayoutManager mLayoutManager;

    private ProgressBar progress_center;

//    private FrameLayout mVideoFloatContainer;
//    private View mVideoPlayerBg;
//    private ImageView mVideoCoverMask;
//    private VideoPlayerView mVideoPlayerView;
//    private View mVideoLoadingView;
//    private ProgressBar mVideoProgressBar;
//
//    private View mCurrentPlayArea;
//    private VideoControllerView mCurrentVideoControllerView;
//    private int mCurrentActiveVideoItem = -1;

    private int currentPage = 1;

//    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private int mCurrentBuffer;
//
//    private boolean mCanTriggerStop = true;
//
    private List<VideoBean> videoBeans = new ArrayList<>();
//
//    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(null);


    private SharedPreferencesHelper sharedPreferencesHelper;

//    /**
//     * Stop video have two scenes
//     * 1.click to stop current video and start a new video
//     * 2.when video item is dismiss or ViewPager changed ? tab changed ? ...
//     */
//    private boolean mIsClickToStop;
//
//    private float mOriginalHeight;
//
//    private float mMoveDeltaY;

    public static HomeDictionaryFragment newInstance(String s){
        HomeDictionaryFragment homeFragment = new HomeDictionaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS,s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_content, container, false);
//        mVideoFloatContainer = view.findViewById(R.id.layout_float_video_container);
//        mVideoPlayerBg = view.findViewById(R.id.video_player_bg);
//        mVideoCoverMask = view.findViewById(R.id.video_player_mask);
//        mVideoPlayerView = view.findViewById(R.id.video_player_view);
//        mVideoLoadingView = view.findViewById(R.id.video_progress_loading);
//        mVideoProgressBar = view.findViewById(R.id.video_progress_bar);

        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.addItemDecoration(new MarginDecoration(getContext()));

        progress_center = view.findViewById(R.id.progress_center);

        recyclerAdapter = new RecyclerAdapter(this,videoBeans);
        mRecyclerView.setAdapter(recyclerAdapter);

        //获取视频
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity().getApplication());
        RealmResults<VideoBean> wordResults = Realm.getDefaultInstance().where(VideoBean.class).findAllAsync();
        wordResults.addChangeListener(new RealmChangeListener<RealmResults<VideoBean>>() {
            @Override
            public void onChange(RealmResults<VideoBean> element) {
                List<VideoBean> datas = Realm.getDefaultInstance().copyFromRealm(element);
                if (!datas.isEmpty()){
                    progress_center.setVisibility(View.GONE);
                    videoBeans.clear();
                    videoBeans.addAll(datas);
                    recyclerAdapter.notifyDataSetChanged();
                }else {

                    OkGo.<VideoResponse>post(UrlBean.getAllVideo)
                            .params("token",sharedPreferencesHelper.getString("token",""))
                            .params("uploadTime",0)
                            .isMultipart(true).tag(this).execute(new JsonCallback<VideoResponse>(VideoResponse.class) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<VideoResponse> response) {
                            if (response.body().getCode().equals("200")){
                                progress_center.setVisibility(View.GONE);
                                List<VideoBean> list = response.body().getData();
                                WordDBUtil.getInstance().insertVideo(list);
                                videoBeans.clear();
                                videoBeans.addAll(list);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });



        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
//                currentPage++;
//                RealmResults<VideoBean> wordResults = Realm.getDefaultInstance().where(VideoBean.class).findAllAsync();
//                wordResults.addChangeListener(new RealmChangeListener<RealmResults<VideoBean>>() {
//                    @Override
//                    public void onChange(RealmResults<VideoBean> element) {
//                        List<VideoBean> datas = Realm.getDefaultInstance().copyFromRealm(element);
//                        if (!datas.isEmpty()){
//                            videoBeans.addAll(datas.subList((currentPage-1)*20,currentPage*20));
//                            recyclerAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
            }
        });
//        mVideoPlayerView.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
//            @Override
//            public void onVideoSizeChangedMainThread(int width, int height) {
//
//            }
//
//            @Override
//            public void onVideoPreparedMainThread() {
//
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoPreparedMainThread");
//                mVideoFloatContainer.setVisibility(View.VISIBLE);
//                mVideoPlayerView.setVisibility(View.VISIBLE);
//                mVideoLoadingView.setVisibility(View.VISIBLE);
//                //for cover the pre Video frame
//                mVideoCoverMask.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onVideoCompletionMainThread() {
//
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoCompletionMainThread");
//
//                if (mCurrentPlayArea != null) {
//                    mCurrentPlayArea.setClickable(true);
//                }
//
//                mVideoFloatContainer.setVisibility(View.INVISIBLE);
//                mCurrentPlayArea.setVisibility(View.VISIBLE);
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//                ViewAnimator.putOn(mVideoFloatContainer).translationY(0);
//
//                //stop update progress
//                mVideoProgressBar.setVisibility(View.GONE);
//                mHandler.removeCallbacks(mProgressRunnable);
//            }
//
//            @Override
//            public void onErrorMainThread(int what, int extra) {
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onErrorMainThread");
//                if (mCurrentPlayArea != null) {
//                    mCurrentPlayArea.setClickable(true);
//                    mCurrentPlayArea.setVisibility(View.VISIBLE);
//                }
//                mVideoFloatContainer.setVisibility(View.INVISIBLE);
//
//                //stop update progress
//                mVideoProgressBar.setVisibility(View.GONE);
//                mHandler.removeCallbacks(mProgressRunnable);
//            }
//
//            @Override
//            public void onBufferingUpdateMainThread(int percent) {
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onBufferingUpdateMainThread");
//                mCurrentBuffer = percent;
//            }
//
//            @Override
//            public void onVideoStoppedMainThread() {
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoStoppedMainThread");
//                if (!mIsClickToStop) {
//                    mCurrentPlayArea.setClickable(true);
//                    mCurrentPlayArea.setVisibility(View.VISIBLE);
//                }
//
//                //stop update progress
//                mVideoProgressBar.setVisibility(View.GONE);
//                mHandler.removeCallbacks(mProgressRunnable);
//            }
//
//            @Override
//            public void onInfoMainThread(int what) {
//                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onInfoMainThread what:" + what);
//                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//
//                    //start update progress
//                    mVideoProgressBar.setVisibility(View.VISIBLE);
//                    mHandler.post(mProgressRunnable);
//
//                    mVideoPlayerView.setVisibility(View.VISIBLE);
//                    mVideoLoadingView.setVisibility(View.GONE);
//                    mVideoCoverMask.setVisibility(View.GONE);
//                    mVideoPlayerBg.setVisibility(View.VISIBLE);
//                    createVideoControllerView();
//
//                    mCurrentVideoControllerView.showWithTitle("VIDEO TEST - " + mCurrentActiveVideoItem);
//                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                    mVideoLoadingView.setVisibility(View.VISIBLE);
//                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                    mVideoLoadingView.setVisibility(View.GONE);
//                }
//            }
//        });
        return view;
    }

//    private Runnable mProgressRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if(mPlayerControlListener != null){
//
//                if(mCurrentVideoControllerView.isShowing()){
//                    mVideoProgressBar.setVisibility(View.GONE);
//                }else {
//                    mVideoProgressBar.setVisibility(View.VISIBLE);
//                }
//
//                int position = mPlayerControlListener.getCurrentPosition();
//                int duration = mPlayerControlListener.getDuration();
//                if(duration != 0) {
//                    long pos = 1000L * position / duration;
//                    int percent = mPlayerControlListener.getBufferPercentage() * 10;
//                    mVideoProgressBar.setProgress((int) pos);
//                    mVideoProgressBar.setSecondaryProgress(percent);
//                    mHandler.postDelayed(this,1000);
//                }
//            }
//        }
//    };
//
//    private void createVideoControllerView() {
//
//        if (mCurrentVideoControllerView != null) {
//            mCurrentVideoControllerView.hide();
//            mCurrentVideoControllerView = null;
//        }
//
//        mCurrentVideoControllerView = new VideoControllerView.Builder(getActivity(), mPlayerControlListener)
//                .withVideoView(mVideoPlayerView)//to enable toggle display controller view
//                .canControlBrightness(true)
//                .canControlVolume(true)
//                .canSeekVideo(false)
//                .exitIcon(R.drawable.video_top_back)
//                .pauseIcon(R.drawable.ic_media_pause)
//                .playIcon(R.drawable.ic_media_play)
//                .shrinkIcon(R.drawable.ic_media_fullscreen_shrink)
//                .stretchIcon(R.drawable.ic_media_fullscreen_stretch)
//                .build(mVideoFloatContainer);//layout container that hold video play view
//    }

//    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//
//        int totalDy;
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
//                mOriginalHeight = mVideoFloatContainer.getTranslationY();
//                totalDy = 0;
//            }
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            /**
//             * NOTE: RecyclerView will callback this method when {@link RecyclerViewFragment#onConfigurationChanged(Configuration)}
//             * happened,so handle this special scene.
//             */
//            if(mPlayerControlListener.isFullScreen()) return;
//
//            //Calculate the total scroll distance of RecyclerView
//            totalDy += dy;
//            mMoveDeltaY = -totalDy;
//            startMoveFloatContainer(false);
//
//            if (mCurrentActiveVideoItem < mLayoutManager.findFirstVisibleItemPosition()
//                    || mCurrentActiveVideoItem > mLayoutManager.findLastVisibleItemPosition()) {
//                if (mCanTriggerStop) {
//                    mCanTriggerStop = false;
//                    stopPlaybackImmediately();
//                }
//            }
//        }
//    };

//    public void stopPlaybackImmediately() {
//
//        mIsClickToStop = false;
//
//        if (mCurrentPlayArea != null) {
//            mCurrentPlayArea.setClickable(true);
//        }
//
//        if (mVideoPlayerManager != null) {
//            mVideoFloatContainer.setVisibility(View.INVISIBLE);
//            mVideoPlayerManager.stopAnyPlayback();
//        }
//    }
//
//    private void startMoveFloatContainer(boolean click) {
//
//        if (mVideoFloatContainer.getVisibility() != View.VISIBLE) return;
//        final float moveDelta;
//
//        if (click) {
//            ViewAnimator.putOn(mVideoFloatContainer).translationY(0);
//
//            int[] playAreaPos = new int[2];
//            int[] floatContainerPos = new int[2];
//            mCurrentPlayArea.getLocationOnScreen(playAreaPos);
//            mVideoFloatContainer.getLocationOnScreen(floatContainerPos);
//            mOriginalHeight = moveDelta = playAreaPos[1] - floatContainerPos[1];
//
//        } else {
//            moveDelta = mMoveDeltaY;
//        }
//
//        float translationY = moveDelta + (!click ? mOriginalHeight : 0);
//
//        ViewAnimator.putOn(mVideoFloatContainer).translationY(translationY);
//    }
//
//    private boolean checkMediaPlayerInvalid() {
//        return mVideoPlayerView != null && mVideoPlayerView.getMediaPlayer() != null;
//    }
//
//    private VideoControllerView.MediaPlayerControlListener mPlayerControlListener = new VideoControllerView.MediaPlayerControlListener() {
//        @Override
//        public void start() {
//            if (checkMediaPlayerInvalid())
//                mVideoPlayerView.getMediaPlayer().start();
//        }
//
//        @Override
//        public void pause() {
//            mVideoPlayerView.getMediaPlayer().pause();
//        }
//
//        @Override
//        public int getDuration() {
//            if (checkMediaPlayerInvalid()) {
//                return mVideoPlayerView.getMediaPlayer().getDuration();
//            }
//            return 0;
//        }
//
//        @Override
//        public int getCurrentPosition() {
//            if (checkMediaPlayerInvalid()) {
//                return mVideoPlayerView.getMediaPlayer().getCurrentPosition();
//            }
//            return 0;
//        }
//
//        @Override
//        public void seekTo(int position) {
//            if (checkMediaPlayerInvalid()) {
//                mVideoPlayerView.getMediaPlayer().seekToPosition(position);
//            }
//        }
//
//        @Override
//        public boolean isPlaying() {
//            if (checkMediaPlayerInvalid()) {
//                return mVideoPlayerView.getMediaPlayer().isPlaying();
//            }
//            return false;
//        }
//
//        @Override
//        public boolean isComplete() {
//            return false;
//        }
//
//        @Override
//        public int getBufferPercentage() {
//            return mCurrentBuffer;
//        }
//
//        @Override
//        public boolean isFullScreen() {
//            return true;
////            return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
////                    || getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
//        }
//
//        @Override
//        public void toggleFullScreen() {
//            if (isFullScreen()) {
////                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                getActivity().setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
//                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
//                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            } else {
//                getActivity().setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
//                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
//                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            }
//        }
//
//        @Override
//        public void exit() {
//            //TODO to handle exit status
//            if (isFullScreen()) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
//        }
//    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_play_area) {
            VideoBean model = (VideoBean) v.getTag();
            if (TextUtils.isEmpty(model.getVideoUrl())){
                return;
            }

            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra("videoBean", model);
            startActivity(intent);


//            mIsClickToStop = true;
//            v.setClickable(false);
//            if (mCurrentPlayArea != null) {
//                if (mCurrentPlayArea != v) {
//                    mCurrentPlayArea.setClickable(true);
//                    mCurrentPlayArea.setVisibility(View.VISIBLE);
//                    mCurrentPlayArea = v;
//                } else {//click same area
//                    if (mVideoFloatContainer.getVisibility() == View.VISIBLE) return;
//                }
//            } else {
//                mCurrentPlayArea = v;
//            }
//
//            //invisible self ,and make visible when video play completely
//            v.setVisibility(View.INVISIBLE);
//            if (mCurrentVideoControllerView != null)
//                mCurrentVideoControllerView.hide();
//
//            mVideoFloatContainer.setVisibility(View.VISIBLE);
//            mVideoCoverMask.setVisibility(View.GONE);
//            mVideoPlayerBg.setVisibility(View.GONE);
//
//            mCurrentActiveVideoItem = model.position;
//            mCanTriggerStop = true;
//
//            //move container view
//            startMoveFloatContainer(true);
//
//            mVideoLoadingView.setVisibility(View.VISIBLE);
//            mVideoPlayerView.setVisibility(View.INVISIBLE);
//
//            //play video
//            mVideoPlayerManager.playNewVideo(new CurrentItemMetaData(model.position, v), mVideoPlayerView, model.getVideoUrl());

        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        View.OnClickListener listener;
        List<VideoBean> wordBeans;

        public RecyclerAdapter(View.OnClickListener listener,List<VideoBean> wordBeans) {
            this.listener = listener;
            this.wordBeans = wordBeans;
        }

        @Override
        public RecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerAdapter.ItemViewHolder holder, int position) {
            final VideoBean model = wordBeans.get(position);
            holder.name.setText(model.getWord());
//            Picasso.with(getActivity()).load(model.get)
//                    .placeholder(R.drawable.shape_place_holder)
//                    .into(holder.cover);
            holder.cover.setImageDrawable(getResources().getDrawable(R.drawable.shape_place_holder));

            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> bitmapObservableEmittere) throws Exception {
                    Bitmap bitmap = createVideoThumbnail(model.getVideoUrl(), 96, 96);
                    bitmapObservableEmittere.onNext(bitmap);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Bitmap bitmap) {
                    holder.cover.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
            model.position = position;
            holder.playArea.setTag(model);
            holder.playArea.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return wordBeans.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public ImageView cover;
            public View playArea;

            public ItemViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_video_name);
                cover = (ImageView) itemView.findViewById(R.id.img_cover);
                playArea = itemView.findViewById(R.id.layout_play_area);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopPlaybackImmediately();
    }
}
