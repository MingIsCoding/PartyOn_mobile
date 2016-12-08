package edu.sjsu.cmpe.partyon.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.utilities.BadgeTool;

public class BadgeInfoActivity extends CloseableActivity {
    private CircularImageView mLevel1Icon,mLevel2Icon,mLevel3Icon,mLevel4Icon,mLevel5Icon;
    private TextView mPoints;
    @Override
    int getToolBarID() {
        return R.id.badge_info_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_badge_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_badge_info);
        mLevel1Icon = (CircularImageView)findViewById(R.id.level_1_icon);
        mLevel1Icon.setBorderColor(Color.WHITE);

        mLevel2Icon = (CircularImageView)findViewById(R.id.level_2_icon);
        mLevel2Icon.setBorderColor(Color.GRAY);

        mLevel3Icon = (CircularImageView)findViewById(R.id.level_3_icon);
        mLevel3Icon.setBorderColor(Color.GREEN);

        mLevel4Icon = (CircularImageView)findViewById(R.id.level_4_icon);
        mLevel4Icon.setBorderColor(Color.BLUE);

        mLevel5Icon = (CircularImageView)findViewById(R.id.level_5_icon);
        mLevel5Icon.setBorderColor(Color.RED);
        switch (BadgeTool.getLevelColor(AppData.getUser().getPoints())){
            case Color.WHITE:
                Picasso.with(this).load(AppData.getUser().getProfilePicSmall()).into(mLevel1Icon);
                break;
            case Color.GRAY:
                Picasso.with(this).load(AppData.getUser().getProfilePicSmall()).into(mLevel2Icon);
                break;
            case Color.GREEN:
                Picasso.with(this).load(AppData.getUser().getProfilePicSmall()).into(mLevel3Icon);
                break;
            case Color.BLUE:
                Picasso.with(this).load(AppData.getUser().getProfilePicSmall()).into(mLevel4Icon);
                break;
            case Color.RED:
                Picasso.with(this).load(AppData.getUser().getProfilePicSmall()).into(mLevel5Icon);
                break;
        }
        mPoints = (TextView)findViewById(R.id.points_view);
        mPoints.setText(AppData.getUser().getPoints()+"");

    }

}
