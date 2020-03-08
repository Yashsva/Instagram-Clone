package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UsersPosts extends AppCompatActivity {


    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);




        linearLayout=findViewById(R.id.linearLayout);


        Intent recievedIntentObject=getIntent();
        final String recievedUserName=recievedIntentObject.getStringExtra("username");

        setTitle(recievedUserName+"'s Posts");

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();




        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",recievedUserName);
        parseQuery.orderByDescending("createdAt");



        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null)
                {

                    for(ParseObject post: objects)
                    {
                        final TextView imageDescription=new TextView(UsersPosts.this);
                        imageDescription.setText(post.get("image_des")+"");


                        ParseFile postPicture= (ParseFile) post.get("picture");

                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if(data!=null && e==null)
                                {
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);


                                    ImageView postImageView=new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams imageView_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(10,5,10,5);

                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);



                                    LinearLayout.LayoutParams description_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    description_params.setMargins(10,5,10,5);

                                    imageDescription.setLayoutParams(description_params);
                                    imageDescription.setGravity(Gravity.CENTER);
                                    imageDescription.setBackgroundColor(Color.parseColor("#00574B"));
                                    imageDescription.setTextColor(Color.WHITE);
                                    imageDescription.setTextSize(20f);


                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imageDescription);



                                }


                            }
                        });

                    }

                }
                else
                {
                    Toast.makeText(UsersPosts.this,recievedUserName+" doesn't have any posts !",Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });
        progressDialog.dismiss();


    }
}
