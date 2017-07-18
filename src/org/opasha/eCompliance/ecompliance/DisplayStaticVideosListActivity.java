/*******************************************************************************
 * Copyright (c) 2017 Operation Asha.
 * All rights reserved. This program and the accompanying materials
 * are copyright of Operation Asha.
 *
 * Contributors:
 *     Abhishek Sinha - Author
 *******************************************************************************/
package org.opasha.eCompliance.ecompliance;


import org.opasha.eCompliance.ecompliance.util.Enums;
import org.opasha.eCompliance.ecompliance.util.IntentKeys;
import org.opasha.eCompliance.ecompliance.util.Enums.TestIds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayStaticVideosListActivity extends Activity  {
	
	ListView listView ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_display_static_videos_list);	
		
            setContentView(R.layout.activity_display_static_videos_list);
            
            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list);
            
            // Defined Array values to show in ListView
            String[] values = new String[] { "Video-1", 
                                             "Video-2",
                                             "Video-3",
                                             "Video-4", 
                                             "Video-5", 
                                             "Video-6", 
                                             "Video-7", 
                                             "Video-8" ,
                                             "Video-9"
                                            };
    
            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data
    
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, values);
    
    
            // Assign adapter to ListView
            listView.setAdapter(adapter); 
            
            // ListView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {
 
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) {
                    
                   // ListView Clicked item index
                   int itemPosition     = position;
                   
                   // ListView Clicked item value
                   String  itemValue    = (String) listView.getItemAtPosition(position);
                      
                    // Show Alert 
                    Toast.makeText(getApplicationContext(),
                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                      .show();
                    
                    Intent myIntent = new Intent(DisplayStaticVideosListActivity.this, DisplayStaticVideoActivity.class);
                    myIntent.putExtra("filename", itemValue); //Optional parameters
                    DisplayStaticVideosListActivity.this.startActivity(myIntent);
                    
                    
            		overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                 
                  }
    
             }); 
        }
    
	}
	
	
	
	


