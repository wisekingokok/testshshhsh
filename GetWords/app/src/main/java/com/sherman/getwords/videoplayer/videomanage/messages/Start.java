package com.sherman.getwords.videoplayer.videomanage.messages;

import android.media.MediaPlayer;

import com.sherman.getwords.videoplayer.videomanage.PlayerMessageState;
import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;


/**
 * This PlayerMessage calls {@link MediaPlayer#start()} ()} ()} on the instance that is used inside {@link VideoPlayerView}
 */
public class Start extends PlayerMessage {

    public Start(VideoPlayerView videoView, VideoPlayerManagerCallback callback) {
        super(videoView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.start();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.STARTING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.STARTED;
    }
}
