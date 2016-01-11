package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by YANG on 1/7/2016.

 */

@TargetApi(11)
public class RemoteListAdapter implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = RemoteListAdapter.class.getSimpleName();

    private static final String[] COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    static final int INDEX_HOME = 0;
    static final int INDEX_AWAY = 1;
    static final int INDEX_HOME_GOALS = 2;
    static final int INDEX_AWAY_GOALS = 3;
    static final int INDEX_TIME = 4;

    private Cursor cursor = null;
    private Context context;

    public RemoteListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        String date = Utilies.getDate() ;

        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                COLUMNS,
                null,
                new String[]{date},
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public int getCount() {
        //Test
        int count = (cursor == null ? 0 : cursor.getCount());
        Log.v(LOG_TAG, "Return data count = " + count);
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_item);

        String homeTeam = cursor.getString(INDEX_HOME);
        String awayTeam = cursor.getString(INDEX_AWAY);
        int homeScores = cursor.getInt(INDEX_HOME_GOALS);
        int awayScores = cursor.getInt(INDEX_AWAY_GOALS);
        String time = cursor.getString(INDEX_TIME);

        remoteViews.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(homeTeam));
        remoteViews.setImageViewResource(R.id.widget_away_crest, Utilies.getTeamCrestByTeamName(awayTeam));
        remoteViews.setTextViewText(R.id.widget_score_textview, Utilies.getScores(homeScores, awayScores));
        remoteViews.setTextColor(R.id.widget_score_textview, context.getResources().getColor(R.color.primary_text_default_material_dark));
        remoteViews.setTextColor(R.id.widget_data_textview, context.getResources().getColor(R.color.primary_text_default_material_dark));
        remoteViews.setTextViewText(R.id.widget_data_textview, time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            setRemoteContentDescription(remoteViews, homeTeam, awayTeam);
        }
        remoteViews.setContentDescription(R.id.widget_home_crest, homeTeam);

        //Test
        Log.v(LOG_TAG, "position = " + position);
        Log.v(LOG_TAG, "homeTeam = " + homeTeam);
        Log.v(LOG_TAG, "awayTeam = " + awayTeam);
        Log.v(LOG_TAG, "homeScores = " + homeScores);
        Log.v(LOG_TAG, "awayScores = " + awayScores);
        Log.v(LOG_TAG, "time = " + time);

        Log.v(LOG_TAG, "remoteViews.getLayoutId()" + Integer.toString(remoteViews.getLayoutId()));
        return remoteViews;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String homeName, String awayName) {
        views.setContentDescription(R.id.widget_home_crest, homeName);
        views.setContentDescription(R.id.widget_away_crest, awayName);
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(),R.layout.widget_list_item);
    }
}
