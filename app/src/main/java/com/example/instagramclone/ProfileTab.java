package com.example.instagramclone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {


    private  EditText edtProfileName,edtBio,edtProfession,edtHobbies,edtFavSport;
    private Button btnUpdateInfo;
    private LinearLayout progressBarWindow;

    public ProfileTab() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfileName=view.findViewById(R.id.edtProfileName);
        edtBio=view.findViewById(R.id.edtBio);
        edtProfession=view.findViewById(R.id.edtProfession);
        edtFavSport=view.findViewById(R.id.edtSport);
        edtHobbies=view.findViewById(R.id.edtHobbies);

        btnUpdateInfo=view.findViewById(R.id.btnProfileUpdate);

        progressBarWindow=view.findViewById(R.id.windowProgressBar);

        loadProfileFields();

        final ParseUser parseUser=ParseUser.getCurrentUser();

         btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 progressBarWindow.setVisibility(View.VISIBLE);

                 parseUser.put("profileName",edtProfileName.getText().toString());
                 parseUser.put("profileBio",edtBio.getText().toString());
                 parseUser.put("profileProfession",edtProfession.getText().toString());
                 parseUser.put("profileHobbies",edtHobbies.getText().toString());
                 parseUser.put("profileFavSport",edtFavSport.getText().toString());

                 parseUser.saveInBackground(new SaveCallback() {
                     @Override
                     public void done(ParseException e) {
                         if(e==null)
                         {
                             Toast.makeText(getContext(),"Info Updated Successfully !",Toast.LENGTH_SHORT).show();
                         }
                         else
                         {
                             Toast.makeText(getContext(),"Error : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

                 progressBarWindow.setVisibility(View.GONE);

             }
         });



        return view;
    }

    public void loadProfileFields()
    {
        ParseUser parseUser=ParseUser.getCurrentUser();
        Object objProfileName=parseUser.get("profileName"),
        objProfileHobbies=parseUser.get("profileHobbies"),
        objProfileProfession=parseUser.get("profileProfession"),
        objProfileFavSport=parseUser.get("profileFavSport"), objProfileBio=parseUser.get("profileBio");

        if(objProfileName!=null)
        {
            edtProfileName.setText(objProfileName.toString());
        }

        if(objProfileHobbies!=null)
        {
            edtHobbies.setText(objProfileHobbies.toString());
        }

        if(objProfileBio!=null)
        {
            edtBio.setText(objProfileBio.toString());
        }

        if(objProfileFavSport!=null)
        {
            edtFavSport.setText(objProfileFavSport.toString());
        }

        if(objProfileProfession!=null)
        {
            edtProfession.setText(objProfileProfession.toString());
        }

    }


}
