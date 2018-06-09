package com.sherman.getwords.videoplayer.videomanage;


import com.sherman.getwords.videoplayer.tracker.IViewTracker;
import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.messages.PlayerMessage;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;

public class SetNewViewForPlayback extends PlayerMessage {

    private final IViewTracker mViewTracker;
    private final VideoPlayerView mCurrentPlayer;
    private final VideoPlayerManagerCallback mCallback;

    public SetNewViewForPlayback(IViewTracker viewTracker, VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
        mViewTracker = viewTracker;
        mCurrentPlayer = videoPlayerView;
        mCallback = callback;
    }

    @Override
    public String toString() {
        return SetNewViewForPlayback.class.getSimpleName() + ", mCurrentPlayer " + mCurrentPlayer;
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        mCallback.setCurrentItem(mViewTracker, mCurrentPlayer);
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_NEW_PLAYER;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.IDLE;
    }
}
