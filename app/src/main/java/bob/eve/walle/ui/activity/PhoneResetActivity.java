package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.widget.CountdownView;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.helper.InputTextHelper;
import butterknife.BindView;
import butterknife.OnClick;

public class PhoneResetActivity extends MyActivity {

    @BindView(R.id.et_phone_reset_phone)
    EditText mPhoneView;
    @BindView(R.id.et_phone_reset_code)
    EditText mCodeView;

    @BindView(R.id.cv_phone_reset_countdown)
    CountdownView mCountdownView;

    @BindView(R.id.btn_phone_reset_commit)
    Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_reset;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_phone_reset_title;
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

    @OnClick({R.id.cv_phone_reset_countdown, R.id.btn_phone_reset_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_phone_reset_countdown:
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 获取验证码
                    toast(getString(R.string.common_send_code_succeed));
                }
                break;
            case R.id.btn_phone_reset_commit:
                if (mPhoneView.getText().toString().length() != 11) {
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 更换手机号
                    toast(getString(R.string.phone_reset_commit_succeed));
                    finish();
                }
                break;
        }
    }
}
