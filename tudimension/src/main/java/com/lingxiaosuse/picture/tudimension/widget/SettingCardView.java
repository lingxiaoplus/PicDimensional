package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;

public class SettingCardView extends CardView{

    private TextView titleView;
    private TextView messageView;
    private SwitchCompat switchCompat;
    private ImageView iconImage;

    private String title,message;
    private boolean visable,switchs,iconVisable;
    private float titleSize,messageSize;
    private int iconSrt,titleColor,messageColor;
    private Bitmap mBitmap;
    public SettingCardView(Context context) {
        this(context,null);
    }

    public SettingCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        View.inflate(context, R.layout.setting_card_layout,this);
        titleView = findViewById(R.id.tv_card_title);
        messageView = findViewById(R.id.tv_card_message);
        switchCompat = findViewById(R.id.switch_card);
        iconImage = findViewById(R.id.iv_image);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingCardView);
        title = a.getString(R.styleable.SettingCardView_title);
        message = a.getString(R.styleable.SettingCardView_message);
        switchs = a.getBoolean(R.styleable.SettingCardView_switchcompat,false);
        visable = a.getBoolean(R.styleable.SettingCardView_visable,true);
        iconVisable = a.getBoolean(R.styleable.SettingCardView_iconVisvble,false);
        titleSize = a.getDimension(R.styleable.SettingCardView_titleSize,16);
        messageSize = a.getDimension(R.styleable.SettingCardView_messageSize,14);
        iconSrt = a.getResourceId(R.styleable.SettingCardView_iconSrc,R.mipmap.ic_launcher);
        titleColor = a.getColor(R.styleable.SettingCardView_titleColor,
                getResources().getColor(R.color.grey));
        messageColor = a.getColor(R.styleable.SettingCardView_messageColor,
                getResources().getColor(R.color.messagegrey));
        titleView.setText(title);
        titleView.setTextSize(titleSize);
        titleView.setTextColor(titleColor);

        messageView.setText(message);
        messageView.setTextSize(messageSize);
        messageView.setTextColor(messageColor);
        if (null != message){
            messageView.setVisibility(VISIBLE);
        }else {
            messageView.setVisibility(GONE);
        }

        switchCompat.setChecked(switchs);
        iconImage.setVisibility(iconVisable?VISIBLE:GONE);
        mBitmap = BitmapFactory.decodeResource(getResources(), iconSrt);
        iconImage.setImageBitmap(mBitmap);

        if (visable){
            switchCompat.setVisibility(VISIBLE);
        }else {
            switchCompat.setVisibility(GONE);
        }
        a.recycle();
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setMessage(String message){
        messageView.setText(message);
    }

    public void setChecked(boolean checked){
        switchCompat.setChecked(checked);
    }


    public String getTitle(){
        return titleView.getText().toString();
    }

    public String getMessage(){
        return messageView.getText().toString();
    }

    public boolean getChecked(){
        return switchCompat.isChecked();
    }

    public void setVisable(boolean vis){
        if (vis){
            switchCompat.setVisibility(VISIBLE);
        }else {
            switchCompat.setVisibility(GONE);
        }
    }
}
