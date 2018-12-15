package com.kelvingabe.bakerapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kelvingabe.bakerapp.R;

import java.util.List;

import static com.kelvingabe.bakerapp.MainActivity.SELECTED_RECIPE;

public class WidgetService extends RemoteViewsService {
    Context context;

    public WidgetService() {

    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //context = getApplicationContext();
        return new ViewFactory(getApplicationContext(), intent);
    }

    class ViewFactory implements RemoteViewsService.RemoteViewsFactory {
        final Context c;
        List<String> ingredients;

        ViewFactory(Context context, Intent intent) {
            c = context;
            ingredients = intent.getStringArrayListExtra(SELECTED_RECIPE);

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            ingredients = null;
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            String ingredient = ingredients.get(position);
            RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widget_ingredient_item);
            views.setTextViewText(R.id.widget_ingredient_name, ingredient);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}