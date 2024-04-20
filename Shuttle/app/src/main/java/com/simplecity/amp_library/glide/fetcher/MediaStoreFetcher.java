package com.simplecity.amp_library.glide.fetcher;

import android.content.Context;
import com.simplecity.amp_library.model.ArtworkProvider;
import java.io.InputStream;

public class MediaStoreFetcher extends BaseFetcher {

    String mediaStoreFetcherTag = "MediaStoreFetcher";

    private Context applicationContext;

    public MediaStoreFetcher(Context context, ArtworkProvider artworkProvider) {
        super(artworkProvider);
        applicationContext = context.getApplicationContext();
    }

    @Override
    protected String getTag() {
        return mediaStoreFetcherTag ;
    }

    @Override
    protected InputStream getStream() {
        return artworkProvider.getMediaStoreArtwork(applicationContext);
    }
}