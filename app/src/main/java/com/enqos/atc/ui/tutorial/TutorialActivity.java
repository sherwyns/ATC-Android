package com.enqos.atc.ui.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TutorialActivity extends BaseActivity implements TutorialView, ViewPager.OnPageChangeListener {
    @Inject
    TutorialPresenter presenter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.skip)
    TextView tvSkip;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attachView(this);
        List<Integer> resIds = new ArrayList<>();
        resIds.add(R.drawable.tutorial_1);
        resIds.add(R.drawable.tutorial_2);
        resIds.add(R.drawable.tutorial_3);
        resIds.add(R.drawable.tutorial_4);
        resIds.add(R.drawable.tutorial_5);

        TutorialAdapter tutorialAdapter = new TutorialAdapter(this, resIds);
        viewPager.setAdapter(tutorialAdapter);
        tabLayout.setupWithViewPager(viewPager, true);

        viewPager.addOnPageChangeListener(this);
    }

    @OnClick(R.id.skip)
    public void onClick() {
        Intent intent = new Intent(TutorialActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.IS_FAV, false);
        startActivity(intent);
        finish();
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tutorial;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

        if(i == 4){
            tvSkip.setText(R.string.done);
        }else{
            tvSkip.setText(getString(R.string.skip));
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
