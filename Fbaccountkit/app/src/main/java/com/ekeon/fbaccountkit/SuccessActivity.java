package com.ekeon.fbaccountkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ekeon on 2016. 5. 11..
 */
public class SuccessActivity extends AppCompatActivity {

  @Bind(R.id.tv_info_accountkitid) TextView tvInfoAccountKitId;
  @Bind(R.id.tv_info_phonenumber) TextView tvInfoPhoneNumber;
  @Bind(R.id.tv_info_email) TextView tvInfoEmail;

  @OnClick(R.id.btn_logout)
  void onLogout() {
    AccountKit.logOut();
    Intent goHomeActivity = new Intent(this, MainActivity.class);
    this.startActivity(goHomeActivity);
    this.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.success_layout);
    ButterKnife.bind(this);

    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
      @Override
      public void onSuccess(Account account) {
        tvInfoAccountKitId.setText("" + account.getId());
        tvInfoEmail.setText("" + account.getEmail());
        tvInfoPhoneNumber.setText("" + account.getPhoneNumber());
      }

      @Override
      public void onError(AccountKitError error) {

      }
    });
  }
}
