package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.Transaction;
import edu.sjsu.cmpe.partyon.entities.User;

public class WalletActivity extends CloseableActivity {
    private TextView mBalanceView, mHistoryView;
    private static String TAG = "WalletActivity";
    @Override
    int getToolBarID() {
        return R.id.wallet_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_wallet;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wallet);
        mBalanceView = (TextView)findViewById(R.id.balance_view);
        mHistoryView = (TextView)findViewById(R.id.history_view);
        loadData();
    }

    public void showQrCode(View view) {
        Intent in = new Intent(WalletActivity.this, MyPaymentQRCodeActivity.class);
        startActivity(in);
    }
    private void loadData(){

        App.getUser().fetchInBackground(new GetCallback<User>(){

            @Override
            public void done(User object, ParseException e) {
                double newKB = Math.round(Float.parseFloat(object.get("balance").toString())
                        *100.0)/100.0;
                mBalanceView.setText("$"+newKB);
            }
        });

        ParseQuery query = ParseQuery.getQuery(App.OBJ_NAME_TRANSACTION);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                if (e == null) {
                    Log.v(TAG,"the parties size from server:" + objects.size());
                    mHistoryView.setText("");
                    for(Transaction t : objects){
                        JSONArray records = null;
                        try {
                            records = new JSONArray(t.getItems());
                            for(int i = 0 ; i < records.length(); i++){
                                mHistoryView.append(
                                        ((JSONObject)records.get(i)).get("name")+
                                                "\t\t\t"+
                                Math.round(Float.parseFloat(((JSONObject)records.get(i)).get("price").toString())
                                        *100.0)/100.0
                                                +"\n");
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                } else {
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }
}
