package com.gautham.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gautham.chatapp.Fragments.ChatsFragment;
import com.gautham.chatapp.Fragments.ProfileFragment;
import com.gautham.chatapp.Fragments.usersFragment;
import com.gautham.chatapp.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myref= FirebaseDatabase.getInstance().getReference("Myusers").child(firebaseUser
                .getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TabLayout tabLayout=findViewById(R.id.Tablayout);
        ViewPager viewpager=findViewById(R.id.view_pager);
        ViewPagerAdapter vpA=new ViewPagerAdapter(getSupportFragmentManager());
        vpA.addfragments(new ChatsFragment(),"chats");
        vpA.addfragments(new usersFragment(),"Users");
        vpA.addfragments(new ProfileFragment(),"Profile");
        viewpager.setAdapter(vpA);
        tabLayout.setupWithViewPager(viewpager);




    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                return true;

        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment>fragments;
        private ArrayList<String> titles;


        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addfragments(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);

        }

        public CharSequence getPageTitle(int position){
            return titles.get(position);

        }


    }

    private void checkstatus(String status){
        myref=FirebaseDatabase.getInstance().getReference("Myusers").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        myref.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkstatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkstatus("offline");
    }
}