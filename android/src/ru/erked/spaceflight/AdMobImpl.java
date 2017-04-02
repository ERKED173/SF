package ru.erked.spaceflight;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


/**
 * Created by Andrey (cb) Mikheev
 * GPGS
 * 26.09.2016
 */
public class AdMobImpl implements ru.erked.spaceflight.AdMob {

    private final int SHOW_AD = 1;
    private final int HIDE_AD = 0;
    private final String id;

	// its my tablet and phone
	// you need get you own ID-s from debug-console
    private final String TEST_DEVICE1 = "EBD9E49762B2EB260F21552A794D60C6";

    public AdView                      adView   = null;
    public RelativeLayout.LayoutParams adParams = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
            switch ( msg.what ) {
                case SHOW_AD:
                    adView.setVisibility( View.VISIBLE );
                    break;
                case HIDE_AD:
                    adView.setVisibility( View.GONE );
                    break;
            }
        }
    };

    public AdMobImpl( String id ) {
        this.id = id;
    }

    public void init( Context context ) {
        adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        adParams.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
        adParams.addRule( RelativeLayout.ALIGN_PARENT_LEFT );

        adView = new AdView( context );
        adView.setAdSize( AdSize.BANNER );
        adView.setAdUnitId( id );
        AdRequest.Builder requestBuilder = new AdRequest.Builder();
        requestBuilder.addTestDevice( TEST_DEVICE1 );
        adView.loadAd( requestBuilder.build() );
    }

    @Override
    public void show() {
        handler.sendEmptyMessage( SHOW_AD );
    }

    @Override
    public void hide() {
        handler.sendEmptyMessage( HIDE_AD );
    }
}
