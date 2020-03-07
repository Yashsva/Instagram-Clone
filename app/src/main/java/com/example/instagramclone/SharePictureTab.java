package com.example.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView imgShare;
    private EditText edtImgDescription;
    private Button btnShareImg;

    private Bitmap recievedImageBitMap;
    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare=view.findViewById(R.id.imgShare);
        edtImgDescription=view.findViewById(R.id.edtImgDescription);
        btnShareImg=view.findViewById(R.id.btnShareImg);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImg.setOnClickListener(SharePictureTab.this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgShare:

                if(Build.VERSION.SDK_INT>=23 && 
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions((new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}),1000);
                }
                else
                {
                    getChoosenImage();
                }

                break;

            case R.id.btnShareImg:
                if(recievedImageBitMap!=null)
                {

                    if(edtImgDescription.getText().toString().equals(""))
                    {
                        Toast.makeText(getContext(),"Error : You Must Specify a description",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        recievedImageBitMap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                        byte[] bytes=byteArrayOutputStream.toByteArray();
                        ParseFile parseFile=new ParseFile("img.png",bytes);
                        ParseObject parseObject=new ParseObject("Photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("image_des",edtImgDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog=new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if(e==null)
                                {
                                    Toast.makeText(getContext(),"Done !!!",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Unknown Error :"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                }

                                dialog.dismiss();
                            }
                        });



                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Error : You must select an image .",Toast.LENGTH_SHORT ).show();

                }


                break;
        }

    }

    private void getChoosenImage() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1000)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getChoosenImage();
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2000)
        {
            if(resultCode== Activity.RESULT_OK  &&  data!=null)
            {
                try {
                    Uri selectedImage=data.getData();
                    String[] filePathColumn={ MediaStore.Images.Media.DATA};

                    Cursor cursor =getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null,null);

                    cursor.moveToFirst();
                    int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath=cursor.getString(columnIndex);

                    cursor.close();
                    recievedImageBitMap= BitmapFactory.decodeFile(picturePath);

                    imgShare.setImageBitmap(recievedImageBitMap);

                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        }
    }
}
