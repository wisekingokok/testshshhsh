package com.sherman.getwords.videoplayer.videomanage.messages;


import com.sherman.getwords.videoplayer.videomanage.PlayerMessageState;
import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;

/**
 * This is generic PlayerMessage for setDataSource
 */
public abstract class SetDataSourceMessage extends PlayerMessage{

    public SetDataSourceMessage(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_DATA_SOURCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.DATA_SOURCE_SET;
    }
}
