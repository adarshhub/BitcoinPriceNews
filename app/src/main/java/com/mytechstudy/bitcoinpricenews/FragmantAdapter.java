package com.mytechstudy.bitcoinpricenews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FragmantAdapter extends FragmentPagerAdapter {
    public FragmantAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new AllCoinsFragment();
            case 1:
                return new BitcoinFragment();
            case 2:
                return new EthereumFragment();
            case 3:
                return new DashFragment();
            case 4:
                return new RippleFragment();
            case 5:
                return new BitcoinCashFragment();
            case 6:
                return new LiteCoinFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "ALL COINS";
            case 1:
                return "BITCOIN";
            case 2:
                return "ETHEREUM";
            case 3:
                return "DASH";
            case 4:
                return "RIPPLE";
            case 5:
                return "BITCOIN CASH";
            case 6:
                return "LITECOIN";
            default:
                return null;
        }
    }
}
