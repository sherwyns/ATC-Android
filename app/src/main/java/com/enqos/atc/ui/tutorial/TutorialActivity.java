package com.enqos.atc.ui.tutorial;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.launcher.SplashActivity;
import com.enqos.atc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TutorialActivity extends BaseActivity implements TutorialView {
    @Inject
    TutorialPresenter presenter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

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
}
