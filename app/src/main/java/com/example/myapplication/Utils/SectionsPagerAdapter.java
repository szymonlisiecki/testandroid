package com.example.myapplication.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/** Klasa przeznaczona do uporządkowania fragmentów. Ustawia fragmenty w odpowiedniej kolejności.
 *
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPagerAdapter";
    private final List<Fragment> mFragmentList = new ArrayList<>();
    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }


    /** \brief Metoda do pobierania pozycji fragmentu
     * @param position
     * @return zwraca pozycję fragmentu
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /** \brief Metoda do pobieranie długości listy
     * @return zwraca długość listy
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /** \brief Metoda do dodawania fragmentu
     * @param fragment
     * @return zwraca długość listy
     */
    public void addFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }
}
