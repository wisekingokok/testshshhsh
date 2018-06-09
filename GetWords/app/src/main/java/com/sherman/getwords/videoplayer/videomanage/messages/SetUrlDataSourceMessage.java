package com.sherman.getwords.videoplayer.videomanage.messages;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.sherman.getwords.videoplayer.videomanage.interfaces.VideoPlayerManagerCallback;
import com.sherman.getwords.videoplayer.videomanage.meta.MetaData;
import com.sherman.getwords.videoplayer.videomanage.player.VideoPlayerView;


/**
 * This PlayerMessage calls {@link MediaPlayer#setDataSource(Context, Uri)} on the instance that is used inside {@link VideoPlayerView}
 */
public class SetUrlDataSourceMessage extends SetDataSourceMessage{

    private final MetaData mMetaData;

    public SetUrlDataSourceMessage(VideoPlayerView videoPlayerView, MetaData metaData, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
        mMetaData = metaData;
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        //Get video url
        currentPlayer.setDataSource(mMetaData.getVideoUrl());
    }
}
