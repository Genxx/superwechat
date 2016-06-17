package cn.gen.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.gen.fulicenter.R;
import cn.gen.fulicenter.fragment.NewGoodFragment;

public class FuliCenterMainActivity extends BaseActivity {

    TextView mTvCartHint;
    RadioButton mRadioNewGood;
    RadioButton mRadioButique;
    RadioButton mRadiCategory;
    RadioButton mRadioCart;
    RadioButton mRadionPersonalCenter;
    RadioButton[] mRadios = new RadioButton[5];
    NewGoodFragment mNewGoodFragment;
    Fragment[] mFragments = new Fragment[1];
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
//                .add(cn.gen.fulicenter.R.id.fragment_container, contactListFragment)
//                .hide(contactListFragment)
                .show(mNewGoodFragment)
                .commit();

    }

    private void initFragment() {
        mNewGoodFragment = new NewGoodFragment();
    }

    private void initView() {
        mTvCartHint = (TextView) findViewById(R.id.tvCartHint);
        mRadioNewGood = (RadioButton) findViewById(R.id.layout_new_good);
        mRadioButique = (RadioButton) findViewById(R.id.layout_boutique);
        mRadiCategory = (RadioButton) findViewById(R.id.layout_category);
        mRadioCart = (RadioButton) findViewById(R.id.layout_cart);
        mRadionPersonalCenter = (RadioButton) findViewById(R.id.layout_personal_center);

        mRadios[0]=mRadioNewGood;
        mRadios[1]=mRadioButique;
        mRadios[2]=mRadiCategory;
        mRadios[3]=mRadioCart;
        mRadios[4]=mRadionPersonalCenter;
    }
    public void onCheckedChange(View view){
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
                index = 4;
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

    private void setRadioChecked(int index){
        for(int i=0;i<mRadios.length;i++){
            if (i==index) {
                mRadios[i].setChecked(true);
            }else {
                mRadios[i].setChecked(false);
            }
        }

    }
}
