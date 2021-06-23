package ru.tishin.xmlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View view) {
        XmlPullParser parser = getResources().getXml(R.xml.simple_rss);
        int event_type = XmlPullParser.START_DOCUMENT;
        String title = "", link = "", description = "";
        ArrayList<Item> items = new ArrayList<>();
        try {
            while (event_type != XmlPullParser.END_DOCUMENT){
                if (event_type == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("title")){
                        title = parser.nextText();
                    } else {
                        if(name.equals("link")){
                            link = parser.nextText();
                        } else {
                            if(name.equals("description")){
                                description = parser.nextText();
                                Item item = new Item(title, link, description);
                                items.add(item);
                            }
                        }
                    }
                }
                event_type = parser.next();
            }
        }
        catch(XmlPullParserException e){
            Toast.makeText(this, "Возникла ошибка типа XmlPullParserException", Toast.LENGTH_SHORT);
        }
        catch(IOException e){

        }
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.list_item, items) {
            public View getView(int position, View view, ViewGroup viewGroup)
            {
                View v = super.getView(position, view, viewGroup);
                Item i = (Item) this.getItem(position);
                ((TextView)v).setText(Html.fromHtml("<b>" + i.title + "</b><br/><u>" + i.link + "</u><br/>" + i.description));
                return v;
            }
        };

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}