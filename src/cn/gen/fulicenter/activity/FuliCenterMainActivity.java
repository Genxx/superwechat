package cn.gen.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.fragment.BoutiqueFragment;
import cn.gen.fulicenter.fragment.CategoryFragment;
import cn.gen.fulicenter.fragment.NewGoodFragment;
import cn.gen.fulicenter.fragment.PersonalCenterFragment;

public class FuliCenterMainActivity extends BaseActivity {

    TextView mTvCartHint;
    RadioButton mRadioNewGood;
    RadioButton mRadioButique;
    RadioButton mRadiCategory;
    RadioButton mRadioCart;
    RadioButton mRadionPersonalCenter;
    NewGoodFragment mNewGoodFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonalCenterFragment mPersonalCenterFragment;
    RadioButton[] mRadios = new RadioButton[5];
    Fragment[] mFragments = new Fragment[5];
    private int index;
    // 当前fragment的index
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initView();
        initFragment();
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mNewGoodFragment)
                .add(R.id.fragment_container, mBoutiqueFragment).hide(mBoutiqueFragment)
                .add(R.id.fragment_container, mCategoryFragment).hide(mCategoryFragment)
                .add(R.id.fragment_container, mPersonalCenterFragment).hide(mPersonalCenterFragment)
                .show(mNewGoodFragment)
                .commit();

    }

    private void initFragment() {
        mNewGoodFragment = new NewGoodFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonalCenterFragment = new PersonalCenterFragment();
        mFragments[0] = mNewGoodFragment;
        mFragments[1] = mBoutiqueFragment;
        mFragments[2] = mCategoryFragment;
        mFragments[4] = mPersonalCenterFragment;
    }

    private void initView() {
        mTvCartHint = (TextView) findViewById(R.id.tvCartHint);
        mRadioNewGood = (RadioButton) findViewById(R.id.layout_new_good);
        mRadioButique = (RadioButton) findViewById(R.id.layout_boutique);
        mRadiCategory = (RadioButton) findViewById(R.id.layout_category);
        mRadioCart = (RadioButton) findViewById(R.id.layout_cart);
        mRadionPersonalCenter = (RadioButton) findViewById(R.id.layout_personal_center);

        mRadios[0] = mRadioNewGood;
        mRadios[1] = mRadioButique;
        mRadios[2] = mRadiCategory;
        mRadios[3] = mRadioCart;
        mRadios[4] = mRadionPersonalCenter;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                index = 3;
                break;
            case R.id.layout_personal_center:
                if(FuliCenterApplication.getInstance().getUser()!=null){
                    index = 4;
                }else {
                    gotoLogin();
                }
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(cn.gen.fulicenter.R.id.fragment_container, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
            setRadioChecked(index);
            currentTabIndex = index;
        }
    }

    private void gotoLogin() {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void setRadioChecked(int index) {
        for (int i = 0; i < mRadios.length; i++) {
            if (i == index) {
                mRadios[i].setChecked(true);
            } else {
                mRadios[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("FuliCenterMainActivity=","currentTabIndex"+currentTabIndex+",index="+index);
        if(FuliCenterApplication.getInstance().getUser()!=null){
            index = 4;
            if (currentTabIndex != index) {
                FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                trx.hide(mFragments[currentTabIndex]);
                if (!mFragments[index].isAdded()) {
                    trx.add(cn.gen.fulicenter.R.id.fragment_container, mFragments[index]);
                }
                trx.show(mFragments[index]).commit();
                setRadioChecked(index);
                currentTabIndex = index;
            }
        }else{
            setRadioChecked(index);
        }
    }
}
