package com.example.philip.chainsaw;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login extends FragmentActivity{


    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        get_hash();
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
         loginButton.setReadPermissions( Arrays.asList("public_profile","email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent obj = new Intent(Login.this, MainActivity.class);
                   String accessToken = AccessToken.getCurrentAccessToken().getSource().toString();
                   String userId=  AccessToken.getCurrentAccessToken().getUserId();
                obj.putExtra("accessToken",accessToken);
                obj.putExtra("userId",userId);

                startActivity(obj);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//                    accessToken=currentAccessToken;
//                Log.i("access token.....",accessToken.getToken());
//
//                Intent obj = new Intent(Login.this, MainActivity.class);
//                obj.putExtra("accessToken",accessToken);
//                startActivity(obj);
//
//            }
//        };
        // If the access token is available already assign it.
        //
       // LoginManager.getInstance().registerCallback(callbackManager,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void get_hash(){
    PackageInfo info;
try

    {
        info = getPackageManager().getPackageInfo("com.example.philip.chainsaw", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String something = new String(Base64.encode(md.digest(), 0));
            //String something = new String(Base64.encodeBytes(md.digest()));
            Log.e("hash key", something);
        }
    } catch(PackageManager.NameNotFoundException e1)
    {
        Log.e("name not found", e1.toString());
    } catch(
    NoSuchAlgorithmException e)

    {
        Log.e("no such an algorithm", e.toString());
    } catch(
    Exception e)

    {
        Log.e("exception", e.toString());
    }
}


}
