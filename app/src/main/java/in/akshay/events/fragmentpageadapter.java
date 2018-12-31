package in.akshay.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class fragmentpageadapter extends FragmentPagerAdapter {
    int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3", "Tab4"};

    public fragmentpageadapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.PAGE_COUNT = NumOfTabs;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return event_list.newInstance(0);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return event_grounds.newInstance(1);
            case 2: // Fragment # 1 - This will show SecondFragment
                return event_hist.newInstance(2);
            case 3:
                return event_players.newInstance(3);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
