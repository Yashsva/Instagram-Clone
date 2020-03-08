package com.example.instagramclone;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener {


    private ListView listView;
    private ArrayList<String>   arrayList;
    private ArrayAdapter arrayAdapter;

    private TextView txtLoadingUsersData;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users_tab, container, false);


        listView=view.findViewById(R.id.listView);

        arrayList=new ArrayList();

        arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);

        txtLoadingUsersData=view.findViewById(R.id.txtLoadingUsers);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);


        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser user: objects)
                        {
                            arrayList.add(user.getUsername());
                        }

                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsersData.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(getContext(),UsersPosts.class);
        intent.putExtra("username",arrayList.get(position));

        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null && e==null)
                {
//                    Toast.makeText(getContext(),user.get("profileProfession")+"",Toast.LENGTH_SHORT).show();

                    String userBio="N.A",userProfession="N.A",userHobbies="N.A",userFavSport="N.A",userProfName="N.A";

                    if(user.get("profileBio")!=null)
                    {
                        userBio=user.get("profileBio").toString();
                    }

                    if(user.get("profileProfession")!=null)
                    {
                        userProfession=user.get("profileProfession").toString();
                    }
                    if(user.get("profileHobbies")!=null)
                    {
                        userHobbies=user.get("profileHobbies").toString();
                    }
                    if(user.get("profileFavSport")!=null)
                    {
                        userFavSport=user.get("profileFavSport").toString();
                    }

                    final PrettyDialog prettyDialog=new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername()+"'s Info")
                    .setMessage("Bio : "+ userBio
                            +"\nProfession : " + userProfession
                            +"\nHobbies : " +userHobbies
                            +"\nFavorite Sport : " + userFavSport);

                    prettyDialog.setIcon(R.drawable.person);
                    prettyDialog.addButton("Ok",
                            R.color.pdlg_color_white,
                            R.color.pdlg_color_green,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            });
                    prettyDialog.show();


                }
            }
        });


        return true;
    }
}
