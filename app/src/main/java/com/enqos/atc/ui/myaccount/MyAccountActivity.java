package com.enqos.atc.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.utils.Constants;
import com.enqos.atc.utils.SharedPreferenceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountActivity extends BaseActivity implements MyAccountView {

    @BindView(R.id.image_left)
    ImageView imgLeft;
    @BindView(R.id.image_right)
    ImageView imgRight;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_acc_email)
    TextView tvEmail;
    @BindView(R.id.et_current_password)
    EditText etCurrentPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_new_password)
    EditText etConfirmPassword;
    @BindView(R.id.change_pass_arrow)
    ImageView changePassArrow;
    @BindView(R.id.change_password_layout)
    LinearLayout changePassLayout;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    @Inject
    MyAccountPresenter myAccountPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountPresenter.attachView(this);
        ButterKnife.bind(this);
        modifyHeaderViews();
    }

    private void modifyHeaderViews() {
        imgLeft.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        imgRight.setVisibility(View.GONE);
        title.setText(R.string.my_account);

        String email = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.EMAIL);
        tvEmail.setText(email);
    }

    @OnClick({R.id.tv_sign_out, R.id.image_left, R.id.change_pass_header, R.id.tv_change_password})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_sign_out:
                sharedPreferenceManager.clearPreference();
                Intent intent = new Intent(MyAccountActivity.this, HomeActivity.class);
                intent.putExtra(Constants.IS_FAV, false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.image_left:
                onBackPressed();
                break;
            case R.id.change_pass_header:
                if (changePassLayout.getVisibility() == View.GONE) {
                    changePassLayout.setVisibility(View.VISIBLE);
                    changePassArrow.setRotation(0);
                } else {
                    changePassLayout.setVisibility(View.GONE);
                    changePassArrow.setRotation(180);
                }
                break;
            case R.id.tv_change_password:
                String oldPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String re_enter_password = etConfirmPassword.getText().toString();
                String accessToken = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.TOKEN);

                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword))
                    Toast.makeText(this, "Enter valid inputs", Toast.LENGTH_LONG).show();
                else if (!re_enter_password.equalsIgnoreCase(newPassword))
                    Toast.makeText(this, "New Password and Re-enter password mismatch", Toast.LENGTH_LONG).show();
                else
                    myAccountPresenter.changePassword(this, accessToken, oldPassword, newPassword);
                break;
        }
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myAccountPresenter.detachView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Password changed Successfully!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorMessage(String message) {
        if (message.equalsIgnoreCase("Null is not a valid element")) {
            etConfirmPassword.setText("");
            etCurrentPassword.setText("");
            etNewPassword.setText("");
            Toast.makeText(this, "Password changed Successfully!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
