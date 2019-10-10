package bob.eve.walle.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.widget.CountdownView;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.helper.InputTextHelper;
import butterknife.BindView;
import butterknife.OnClick;

public final class PasswordForgetActivity extends MyActivity {

    @BindView(R.id.et_password_forget_phone)
    EditText mPhoneView;
    @BindView(R.id.et_password_forget_code)
    EditText mCodeView;
    @BindView(R.id.cv_password_forget_countdown)
    CountdownView mCountdownView;
    @BindView(R.id.btn_password_forget_commit)
    Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_forget;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_password_forget_title;
    }

    @Override
    protected void initView() {
        new InputTextHelper.Builder(this)
                .setMain(mCommitView)
                .addView(mPhoneView)
                .addView(mCodeView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.cv_password_forget_countdown, R.id.btn_password_forget_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_password_forget_countdown:
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 获取验证码
                    toast(getString(R.string.common_send_code_succeed));
                }
                break;
            case R.id.btn_password_forget_commit:
                if (mPhoneView.getText().toString().length() != 11) {
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    Intent i=new Intent(PasswordForgetActivity.this,PasswordResetActivity.class);
                    i.putExtra("phoneNo",mPhoneView.getText().toString());
                    startActivity(i);
                }
                break;
            default:
                break;
        }
    }
}
