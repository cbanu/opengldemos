package ro.brite.android.nehe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import android.widget.SimpleAdapter;


public class LessonsList extends ListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SimpleAdapter adapter = new SimpleAdapter(this, parseLessonsXml(), R.layout.item,
        		new String[] { "title", "description", "image" },
        		new int[] { R.id.item_title, R.id.item_description, R.id.item_image });
        setListAdapter(adapter);
    }

	private List<Map<String, Object>> parseLessonsXml() {
    	
    	final List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    	final Map<String, Object> currentItem = new HashMap<String, Object>();
    	
    	RootElement root = new RootElement("lessons");
    	Element item = root.getChild("lesson");
    	item.setEndElementListener(new EndElementListener() {
    		public void end() {
    			items.add(new HashMap<String, Object>(currentItem));
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
            Xml.parse(getResources().openRawResource(R.raw.lessons),
            		Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    	return items;
    }
    
}