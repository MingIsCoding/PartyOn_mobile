package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Party;

public class NewPartyActivity extends AppCompatActivity {

    EditText nameField;
    EditText descriptionField;
    Menu createMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("NewPartyActivity -> started to creating");
        setContentView(R.layout.activity_new_party);
        nameField = (EditText)findViewById(R.id.nameField);
        descriptionField = (EditText)findViewById(R.id.descriptionField);
        createMenu = (Menu)findViewById(R.id.action_createMenu);

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar2");
        testObject.saveInBackground();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_party, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_createMenu){
            System.out.println("start to saving new party");
            Party newParty = new Party();
            newParty.setName(nameField.getText().toString());
            newParty.setDescription(descriptionField.getText().toString());
            newParty.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    System.out.println("new party has been saved.");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
