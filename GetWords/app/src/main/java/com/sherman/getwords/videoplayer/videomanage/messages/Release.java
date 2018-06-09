package com.sherman.getwords.videoplayer.videomanage.messages;

import android.media.MediaPlayer;

import com.sherman.getwords.videoplayer.videomanage.PlayerMessageState;
import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;


/**
 * This PlayerMessage calls {@link MediaPlayer#release()} on the instance that is used inside {@link VideoPlayerView}
 */
public class Release extends PlayerMessage {

    public Release(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.release();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.RELEASING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.RELEASED;
    }
}
