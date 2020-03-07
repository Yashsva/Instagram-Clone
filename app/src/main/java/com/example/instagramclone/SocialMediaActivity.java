package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {


    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private TabAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);


        setTitle("Social Media App");

        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager=findViewById(R.id.viewPager);

        tabAdapter=new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tableLayout=findViewById(R.id.tabLayout);
        tableLayout.setupWithViewPager(viewPager,false);







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.postImageItem)
        {
            if(Build.VERSION.SDK_INT>=23 &&
                    checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions((new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}),3000);
            }
            else
            {
                getChoosenImage();
            }
        }
        else if(item.getItemId()==R.id.logoutUserItem)
        {
            ParseUser.getCurrentUser().logOut();
            Intent intent=new Intent(SocialMediaActivity.this,SignUp.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==3000)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getChoosenImage();
            }

        }

    }

    private void getChoosenImage() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4000)
        {
            if(resultCode== Activity.RESULT_OK &&    data!=null)
            {
                try {
                    Uri capturedImage=data.getData();

                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImage);

                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                    byte[] bytes=byteArrayOutputStream.toByteArray();


                    ParseFile parseFile=new ParseFile("img.png",bytes);
                    ParseObject parseObject=new ParseObject("Photo");
                    parseObject.put("picture",parseFile);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                    final ProgressDialog dialog=new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null)
                            {
                                Toast.makeText(SocialMediaActivity.this,"Done !!!",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(SocialMediaActivity.this,"Unknown Error :"+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                            dialog.dismiss();
                        }
                    });





                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        }
    }

}
