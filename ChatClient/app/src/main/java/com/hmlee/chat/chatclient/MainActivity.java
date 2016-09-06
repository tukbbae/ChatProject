package com.hmlee.chat.chatclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hmlee.chat.chatclient.data.Contact;
import com.hmlee.chat.chatclient.http.HttpClient;
import com.hmlee.chat.chatclient.http.model.GetFriendList;
import com.hmlee.chat.chatclient.http.model.GetFriendListResponse;
import com.hmlee.chat.chatclient.utils.CommonUtils;
import com.hmlee.chat.chatclient.utils.ConfigSettingPreferences;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // HTTP Client
    private HttpClient mHttpClient;

    private GetFriendListTask mFriendTask;

    // TODO :: 주소록 DB 생성 후 삭제 필요
    public static ArrayList<Contact> sContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Set default username is anonymous.
//        mUsername = ANONYMOUS;
//
//        // Initialize Firebase Auth
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        if (mFirebaseUser == null) {
//            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, SignInActivity.class));
//            finish();
//            return;
//        } else {
//            mUsername = mFirebaseUser.getDisplayName();
//            if (mFirebaseUser.getPhotoUrl() != null) {
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//            }
//        }
        checkLogin();
        initHttpModule();
        getFriendList();
        setFragment();
    }

    private void checkLogin() {
        String userEmail = ConfigSettingPreferences.getInstance(MainActivity.this).getPrefsUserEmail();

        if (TextUtils.isEmpty(userEmail)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    private void initHttpModule() {
        mHttpClient = new HttpClient(this, CommonUtils.SERVER_URL, null);
    }

    private void getFriendList() {
        sContactList = new ArrayList<Contact>();
        mFriendTask = new GetFriendListTask("tukbbae@gmail.com");
        mFriendTask.execute((Void) null);
    }

    private void setFragment() {
        //Fragment
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_pager);

        Fragment[] arrFragments = new Fragment[3];
        arrFragments[0] = new ContactsFragment();
        arrFragments[1] = new ConversationsFragment();
        arrFragments[2] = new ConfigFragment();

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), arrFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] arrFragments;

        public MyPagerAdapter(FragmentManager fm, Fragment[] arrFragments) {
            super(fm);
            this.arrFragments = arrFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return arrFragments[position];
        }

        @Override
        public int getCount() {
            return arrFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0 :
                    return "Contacts";
                case 1 :
                    return "Messages";
                case 2 :
                    return "Settings";

                default :
                    return "";
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GetFriendListTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUserEmail;

        public GetFriendListTask(String mUserEmail) {
            this.mUserEmail = mUserEmail;
        }

        protected Boolean doInBackground(Void... params) {
            boolean result = false;

            GetFriendList request = new GetFriendList(mUserEmail);

            try {
                GetFriendListResponse response = mHttpClient.sendRequest("/api/getFriendList", GetFriendList.class, GetFriendListResponse.class, request);


                if (response != null) {
                    for (int i = 0; i < response.getFriendList().size(); i++) {
                        String name = response.getFriendList().get(i).getName();
                        String email = response.getFriendList().get(i).getEmail();
                        String token = response.getFriendList().get(i).getToken();

                        Contact contact = new Contact(name, email, token);

                        sContactList.add(contact);
                    }
                }

                result = true;

            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

            } else {

            }

        }
    }
}
