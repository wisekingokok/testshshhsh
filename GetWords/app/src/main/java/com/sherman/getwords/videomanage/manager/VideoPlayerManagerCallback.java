package com.sherman.getwords.videomanage.manager;

import com.sherman.getwords.videomanage.PlayerMessageState;
import com.sherman.getwords.videomanage.meta.MetaData;
import com.sherman.getwords.videomanage.ui.VideoPlayerView;

public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
