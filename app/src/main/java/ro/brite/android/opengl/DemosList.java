package ro.brite.android.opengl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class DemosList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SimpleAdapter adapter = new SimpleAdapter(this, parseDemosXml(), R.layout.item,
                new String[] { "title", "description", "image" },
                new int[] { R.id.item_title, R.id.item_description, R.id.item_image });
        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<?, ?> item = (Map<?, ?>)getListAdapter().getItem(position);
        int itemId = (Integer)item.get("id");
        switch (itemId) {
            case  1: startActivity(new Intent(this, ro.brite.android.nehe01.GlApp.class)); break;
            case  2: startActivity(new Intent(this, ro.brite.android.nehe02.GlApp.class)); break;
            case  3: startActivity(new Intent(this, ro.brite.android.nehe03.GlApp.class)); break;
            case  4: startActivity(new Intent(this, ro.brite.android.nehe04.GlApp.class)); break;
            case  5: startActivity(new Intent(this, ro.brite.android.nehe05.GlApp.class)); break;
            case  6: startActivity(new Intent(this, ro.brite.android.nehe06.GlApp.class)); break;
            case  7: startActivity(new Intent(this, ro.brite.android.nehe07.GlApp.class)); break;
            case  8: startActivity(new Intent(this, ro.brite.android.nehe08.GlApp.class)); break;
            case  9: startActivity(new Intent(this, ro.brite.android.nehe09.GlApp.class)); break;
            case 16: startActivity(new Intent(this, ro.brite.android.nehe16.GlApp.class)); break;
            case 18: startActivity(new Intent(this, ro.brite.android.nehe18.GlApp.class)); break;
            case 23: startActivity(new Intent(this, ro.brite.android.nehe23.GlApp.class)); break;
            default:
                super.onListItemClick(l, v, position, id);
                break;
        }
    }

    private List<Map<String, Object>> parseDemosXml() {

        final List<Map<String, Object>> items = new ArrayList<>();
        final Map<String, Object> currentItem = new HashMap<>();

        RootElement root = new RootElement("demos");
        Element item = root.getChild("demo");
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add(new HashMap<>(currentItem));
            }
        });
        item.getChild("title").setEndTextElementListener(new EndTextElementListener() {
            public void end(String value) {
                currentItem.put("title", value);
            }
        });
        item.getChild("description").setEndTextElementListener(new EndTextElementListener() {
            public void end(String value) {
                currentItem.put("description", value);
            }
        });
        item.getChild("id").setEndTextElementListener(new EndTextElementListener() {
            public void end(String value) {
                currentItem.put("id", Integer.parseInt(value));
            }
        });
        item.getChild("image").setEndTextElementListener(new EndTextElementListener() {
            public void end(String value) {
                int resId = getResources().getIdentifier(value, null, getPackageName());
                currentItem.put("image", resId);
            }
        });

        try {
            Xml.parse(getResources().openRawResource(R.raw.demos),
                    Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return items;
    }
}
