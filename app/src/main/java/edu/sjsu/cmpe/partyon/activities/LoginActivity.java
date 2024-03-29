package edu.sjsu.cmpe.partyon.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.User;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private com.facebook.login.widget.LoginButton mFacebookLoginBtn;
    private static final int FACEBOOK_LOGIN = 1, GOOGLE_LOGIN = 2;
    private static final String TAG = "LoginActivity";
    private CallbackManager mCallbackManager;
    private static final int RC_GOOGLE_SIGN_IN = 17;
    public static final String OP_LOG_OUT = "operation_log_out";
    public static final String OP_CODE = "operation_code";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //facebook api
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    //google api
    private GoogleApiClient mGoogleApiClient;
    private String attemptEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        initFacebookApi();
        initGoogleApi();
        if(getIntent().getStringExtra(OP_CODE) != null
                && getIntent().getStringExtra(OP_CODE).equals(OP_LOG_OUT)){
            Log.d(TAG,"logout operation");
            callFacebookLogout();
        }

    }
    private void initFacebookApi(){
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginBtn = (LoginButton)findViewById(R.id.facebook_oauth_btn);
        mFacebookLoginBtn.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        mFacebookLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        if(currentProfile != null){
                            Log.d(TAG,"current Facebook Profile: user name: "+currentProfile.getLastName());
                        }else {
                            Log.d(TAG,"logged out");
                        }
                    }
                };

                profileTracker.startTracking();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, final GraphResponse response) {
                                Log.d("LoginActivity", response.toString());
                                Log.d(TAG,object.toString());
                                //Profile profile = Profile.getCurrentProfile();

                                // Application code

                                try {
                                    attemptEmail = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    if(attemptEmail == null)
                                        throw new Exception("Email address is not found");
                                    Log.d(TAG,attemptEmail);
                                    Log.d(TAG,birthday);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //if there is a user who owns this facebook account
                                ParseQuery<User> query = ParseQuery.getQuery(App.OBJ_NAME_USER);
                                query.whereEqualTo("email",attemptEmail);
                                query.whereEqualTo("facebookID",Profile.getCurrentProfile().getId());
                                Log.d(TAG,"looking for attemptEmail:"+attemptEmail+" facebookID:"+Profile.getCurrentProfile().getId());
                                query.getFirstInBackground(new GetCallback<User>() {
                                    public void done(final User userFromDb, ParseException e) {
                                        if (userFromDb == null) {//no, store this facebook profile to users
                                            User u = new User();
                                            try {
                                                u.setUsername(Profile.getCurrentProfile().getFirstName()
                                                        +" "+Profile.getCurrentProfile().getLastName());
                                                u.setFirstName(Profile.getCurrentProfile().getFirstName());
                                                u.setLastName(Profile.getCurrentProfile().getLastName());
                                                u.setEmail(attemptEmail);
                                                u.setBirthday(new Date(object.getString("birthday")));
                                                u.setFacebookID(Profile.getCurrentProfile().getId());
                                                if(object.getString("gender").equals("male"))
                                                    u.setGender(App.GENDER_MALE);
                                                else if (object.getString("gender").equals("female"))
                                                    u.setGender(App.GENDER_FEMALE);
                                                u.setProfilePicSmall(Profile.getCurrentProfile().getProfilePictureUri(100,100).toString());
                                                Log.d(TAG,"start to save object:"+u.toString());
                                                u.setPassword(Profile.getCurrentProfile().getId());
                                                u.signUp();
                                                finalizeLoginProcess();
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                        } else {//exists, then store this one as current user
                                            userFromDb.logInInBackground(userFromDb.getUsername(), userFromDb.getFacebookID(), new LogInCallback() {
                                                @Override
                                                public void done(ParseUser user, ParseException e) {
                                                    Log.d(TAG,"user logged in:"+user.getUsername());
                                                    //e.printStackTrace();
                                                    finalizeLoginProcess();
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    public void callFacebookLogout() {
//log out from facebook
        LoginManager.getInstance().logOut();
        GraphRequest delPermRequest
                = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/{user-id}/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if(graphResponse!=null){
                    FacebookRequestError error =graphResponse.getError();
                    if(error!=null){
                        Log.e(TAG, error.toString());
                    }else {

                    }
                }
            }
        });
        Log.d(TAG,"Executing revoke permissions with graph path" + delPermRequest.getGraphPath());
        delPermRequest.executeAsync();
    }
    private void finalizeLoginProcess() {
        App.isUserLoggedin = true;
        Log.d(TAG,"login:"+ User.getCurrentUser().getUsername());
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }

    private void initGoogleApi(){
        //google login plug
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().
                requestProfile().
                requestId()
                .build();
        //access google api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG,"google login failed");
                    }
                } )//this /* OnConnectionFailedListener */
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton mGoogleSignInBtn = (SignInButton) findViewById(R.id.google_oauth_btn);
        mGoogleSignInBtn.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInBtn.setScopes(gso.getScopeArray());
        mGoogleSignInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            }
        });
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
//        else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null){
                        showProgress(true);
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }


            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            //User user = new User();
            //user.setEmail(mEmail);      // setting the received email and password
            //user.setPassword(mPassword);

//            User.logInInBackground(mEmail, mPassword, new LogInCallback() {
//                @Override
//                public void done(User user, ParseException e) {
//                    if(e==null){
//                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                }
//
//
//            });


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }



            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                final GoogleSignInAccount acct = result.getSignInAccount();
                Log.d(TAG,acct.getDisplayName());
                attemptEmail = acct.getEmail();
                ParseQuery<User> query = ParseQuery.getQuery("User");
                query.whereEqualTo("email",attemptEmail);
                query.whereEqualTo("googleID",acct.getId());
                query.getFirstInBackground(new GetCallback<User>() {
                    @Override
                    public void done(User object, ParseException e) {
                        if (object == null) {//no, store this facebook profile to users
                            User u = new User();
                            try {
                                u.setUsername(acct.getDisplayName());
                                u.setFirstName(acct.getGivenName());
                                u.setLastName(acct.getFamilyName());
                                u.setEmail(attemptEmail);
                                u.setGoogleID(acct.getId());
                                u.setGender(0);
                                if(acct.getPhotoUrl() != null)
                                    u.setProfilePicSmall(acct.getPhotoUrl().toString());
                                else
                                    u.setProfilePicSmall("");
                                Log.d(TAG,"start to save object:"+u.toString());
                                u.setPassword(acct.getId());
                                u.signUp();
                                finalizeLoginProcess();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        } else {//exists, then store this one as current user
                            object.logInInBackground(object.getUsername(), object.getGoogleID(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    Log.d(TAG,"user logged in:"+user.getUsername());
                                    //e.printStackTrace();
                                    finalizeLoginProcess();
                                }
                            });
                        }
                    }
                });
            } else {
                // Signed out, show unauthenticated UI.
//                updateUI(false);
            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    // uncomment this when the facebook profile starts working
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        profileTracker.stopTracking();
//    }

    // For the register activity(nav)
    public void registerMessage(View view){
        Button mregisterButton1 =(Button)findViewById(R.id.registerButton1);
        mregisterButton1.setOnClickListener(new View.OnClickListener() {    //responds to click on this activity.
            @Override
            public void onClick(View view) {
                //toast

                Toast.makeText(LoginActivity.this, "register page", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class );
                startActivity(intent);      //redirect to the register page/activity.
            }
        });
    }
}

