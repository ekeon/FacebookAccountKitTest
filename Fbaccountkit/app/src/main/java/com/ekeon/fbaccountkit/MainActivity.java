package com.ekeon.fbaccountkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "TAG";

  @OnClick(R.id.btn_email)
  void onEmail() {
    Log.d(TAG, "btnemailclick");
    onLoginPhoneOrEmail(LoginType.EMAIL);
  }

  @OnClick(R.id.btn_phone)
  void onPhone() {
    Log.d(TAG,"btnphoneclick");
    onLoginPhoneOrEmail(LoginType.PHONE);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AccountKit.initialize(getApplicationContext());
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    AccessToken accessToken = AccountKit.getCurrentAccessToken();

    if (accessToken != null) {
      //Handle Returning User
      goToMyLoggedInActivity();
      Log.d(TAG, "onCreate: !null " + accessToken);
    } else {
      //Handle new or logged out user
      Log.d(TAG, "onCreate: null " + accessToken);
    }
  }

  public static int APP_REQUEST_CODE = 99;

  public void onLoginPhoneOrEmail(LoginType loginType) {
    final Intent intent = new Intent(this, AccountKitActivity.class);
    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
            new AccountKitConfiguration.AccountKitConfigurationBuilder(
                    loginType,
                    AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
    // ... perform additional configuration ...
    intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build());
    startActivityForResult(intent, APP_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(
          final int requestCode,
          final int resultCode,
          final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
      AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
      String toastMessage;
      if (loginResult.getError() != null) {
        toastMessage = loginResult.getError().getErrorType().getMessage();
        showErrorActivity(loginResult.getError());
      } else if (loginResult.wasCancelled()) {
        toastMessage = "Login Cancelled";
      } else {
        if (loginResult.getAccessToken() != null) {
          toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
        } else {
          toastMessage = String.format("Success:%s...",
                  loginResult.getAuthorizationCode().substring(0,10));
        }

        // If you have an authorization code, retrieve it from
        // loginResult.getAuthorizationCode()
        // and pass it to your server and exchange it for an access token.

        // Success! Start your next activity...
        goToMyLoggedInActivity();
      }

      // Surface the result to your user in an appropriate way.
      Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
  }

  private void showErrorActivity(AccountKitError error) {
    Log.d("TAG", "showErrorActivity: " + error.toString());
  }

  private void goToMyLoggedInActivity(){
    final Intent intent = new Intent(this, SuccessActivity.class);
    this.startActivity(intent);
  }
}
