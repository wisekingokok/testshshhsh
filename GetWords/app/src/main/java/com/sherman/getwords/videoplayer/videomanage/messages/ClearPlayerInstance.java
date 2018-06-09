package com.sherman.getwords.videoplayer.videomanage.messages;


import com.sherman.getwords.videoplayer.videomanage.PlayerMessageState;
import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;

/**
 */
public class ClearPlayerInstance extends PlayerMessage {

    public ClearPlayerInstance(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.clearPlayerInstance();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.CLEARING_PLAYER_INSTANCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PLAYER_INSTANCE_CLEARED;
    }
}
