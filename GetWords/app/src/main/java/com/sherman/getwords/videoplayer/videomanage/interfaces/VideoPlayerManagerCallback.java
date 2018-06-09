package com.sherman.getwords.videoplayer.videomanage.interfaces;


import com.sherman.getwords.videoplayer.tracker.IViewTracker;
import com.sherman.getwords.videoplayer.videomanage.PlayerMessageState;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;

public interface VideoPlayerManagerCallback {

    void setCurrentItem(IViewTracker viewTracker, VideoPlayerView newPlayerView);

    void updateVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
