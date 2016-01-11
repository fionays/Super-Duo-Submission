package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.WidgetService;

/**
 * Created by YANG on 1/7/2016.
 */
public class WidgetProvider extends AppWidgetProvider {

    /**
     * Gets called every interval as specified on widget_provider_info.xml.
     * Also gets called on evry device reboot.
     */
    @Override
    public void onUpdate(Context c, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i < appWidgetIds.length; i++) {
            //Get RemoteViews
            RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widget_layout);

            // Create an intent to launch MainActivity.class when click widget
            Intent intent = new Intent(c, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(c, views);
                setEmptyView(views);
            } else {
                setRemoteAdapterV11(c, views);
            }

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }

        super.onUpdate(c, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setEmptyView(@NonNull final RemoteViews views) {
        views.setEmptyView(R.id.widget_list, R.id.widget_empty_view);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list, new Intent(context, WidgetService.class));
    }
}
