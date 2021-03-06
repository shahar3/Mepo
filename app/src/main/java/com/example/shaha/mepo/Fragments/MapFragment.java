package com.example.shaha.mepo.Fragments;

/**
 * Created by shaha on 16/02/2018.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.example.shaha.mepo.EventItem;
import com.example.shaha.mepo.MepoEvent;
import com.example.shaha.mepo.R;
import com.example.shaha.mepo.Utils.MepoEventUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.List;

/**
 * Created by shaha on 18/01/2018.
 */

public class MapFragment extends LocalEventsFragment {
    //Beer Sheva langtitude and longtitude
    private static final Double LATITUDE = 31.252973;
    private static final Double LONGTITUDE = 34.7914620000000024;
    private GoogleMap mMap;
    private ClusterManager<EventItem> mClusterManager;

    private class EventRenderer extends DefaultClusterRenderer<EventItem> {

        public EventRenderer(Context context) {
            super(context, mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(EventItem item, MarkerOptions markerOptions) {
            // Draw a single event.
            MepoEvent event = item.getEvent();

            switch (event.getType()) {
                case SPORT:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_run_black_24dp));
                    break;
                case CARPOOL:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp));
                    break;
                case SALE:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping_basket_black_24dp));
                    break;
                case PARTY:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_whatshot_black_24dp));
                    break;
                case BAR_SALE:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_bar_black_24dp));
                    break;
                case MISC:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_black_24dp));
                    break;
            }
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    public void setEventMarkers(GoogleMap map, List<MepoEvent> events) {
        mMap = map;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE, LONGTITUDE), 10));
        mClusterManager = new ClusterManager<EventItem>(getContext(), mMap);
        //set the markers to be a custom design
        mClusterManager.setRenderer(new EventRenderer(getContext()));

        mMap.setOnCameraIdleListener(mClusterManager);
        for (MepoEvent event : events) {
            double latitude = event.getEventLocation().getMepoCoordinate().getLatitude();
            double longtitude = event.getEventLocation().getMepoCoordinate().getLongtitude();

            EventItem item = new EventItem(latitude, longtitude, event.getEventName(), event.getDescription(), event);
            mClusterManager.addItem(item);
        }
    }

    //set marker according to the event type
    private Marker setMarker(double latitude, double longtitude, String eventName) {
        return mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title(eventName));
    }

    @Override
    protected void startMapActions() {
        setEventMarkers(getMap(), MepoEventUtils.getEvents());
    }
}