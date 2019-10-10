package bob.eve.walle.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hjq.image.ImageLoader;
import com.hjq.widget.SettingBar;
import com.hjq.widget.SwitchButton;

import bob.eve.walle.R;
import bob.eve.walle.common.MyActivity;
import bob.eve.walle.helper.ActivityStackManager;
import bob.eve.walle.helper.CacheDataManager;
import butterknife.BindView;
import butterknife.OnClick;

public final class SettingActivity extends MyActivity
        implements SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.sb_setting_cache)
    SettingBar mCleanCacheView;

    @BindView(R.id.sb_setting_auto)
    SettingBar mAutoLoginView;
    @BindView(R.id.sb_setting_switch)
    SwitchButton mAutoSwitchView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_setting_title;
    }

    @Override
    protected void initView() {
        // 设置切换按钮的监听
        mAutoSwitchView.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        // 获取应用缓存大小
        mCleanCacheView.setRightText(CacheDataManager.getTotalCacheSize(this));
    }

    @OnClick({R.id.sb_setting_language, R.id.sb_setting_update, R.id.sb_setting_agreement, R.id.sb_setting_about,
            R.id.sb_setting_cache, R.id.sb_setting_auto, R.id.sb_setting_exit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_setting_language:
                break;
            case R.id.sb_setting_update:
                break;
            case R.id.sb_setting_agreement:
               // startActivity(WebActivity.class);
                break;
            case R.id.sb_setting_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.sb_setting_auto: // 自动登录
                mAutoSwitchView.setChecked(!mAutoSwitchView.isChecked());
                break;
            case R.id.sb_setting_cache: // 清空缓存
                ImageLoader.clear(this);
                CacheDataManager.clearAllCache(this);
                // 重新获取应用缓存大小
                mCleanCacheView.setRightText(CacheDataManager.getTotalCacheSize(this));
                break;
            case R.id.sb_setting_exit: // 退出登录
                //startActivity(LoginActivity.class);
                // 进行内存优化，销毁掉所有的界面
                //ActivityStackManager.getInstance().finishAllActivities(LoginActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        toast(isChecked);
    }
}
