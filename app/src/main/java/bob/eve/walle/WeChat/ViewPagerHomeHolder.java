package bob.eve.walle.WeChat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHomeHolder {
    public class ViewPagerHomeAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments =  new ArrayList<>();

        public ViewPagerHomeAdapter(FragmentManager fm) {
            super(fm);
            fragments.clear();
            fragments.add(new HomeUtilsFragment());
            fragments.add(new HomeViewFragment());
            fragments.add(new HomeOtherFragment());
            fragments.add(new HomeResourcesFragment());
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
}}
