package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.RemoteListAdapter;

/**
 * The service is used to for a remote adapter to request RemoteViews. It provides a
 * RemoteViewsFactory which is used to populate the remote collection view.
 * Created by YANG on 1/7/2016.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetService extends RemoteViewsService {
    /**
     * Define a remote adapter of the remote collection view (listView).
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteListAdapter(getApplicationContext());
    }
}
