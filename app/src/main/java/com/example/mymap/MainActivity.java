package com.example.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.geometry.Geometry;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.search.*;
import android.widget.Toast;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private SearchManager searchManager;
    private Session searchSession;
    private MapView mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("e19c89ed-e7a8-4623-875d-bd88306b8ae8");
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);

        setContentView(R.layout.activity_main);

        final AutoCompleteTextView textView = findViewById(R.id.search);
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

        mapview = findViewById(R.id.mapview);

        Button button = findViewById(R.id.bttn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MoveLoacation(textView.getText().toString());
                textView.clearFocus();
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                MoveLoacation(textView.getText().toString());
                textView.clearFocus();
            }
        });

    }

    String search;


    private void MoveLoacation(String Name)
    {
        searchSession = searchManager.submit(
                Name,
                VisibleRegionUtils.toPolygon(mapview.getMap().getVisibleRegion()),
                new SearchOptions(), new Session.SearchListener()
                {
                    @Override
                    public void onSearchResponse(@NonNull Response response)
                    {
                        GeoObject geo = null;
                        List<String> set = new ArrayList<>();
                        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
                            geo = searchResult.getObj();
                            break;
                        }
                        Point destinaton = geo.getGeometry().get(0).getPoint();
                        mapview.getMap().move(
                                new CameraPosition(destinaton, 14.0f, 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 4.5f),
                                null);

                    }

                    @Override
                    public void onSearchError(@NonNull Error error)
                    {

                    }
                }
        );
    }

    @Override
    protected void onStop()
    {
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }
}
