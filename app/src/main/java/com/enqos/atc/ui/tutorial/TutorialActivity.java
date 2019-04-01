package com.enqos.atc.ui.tutorial;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TutorialActivity extends BaseActivity implements TutorialView {
    @Inject
    TutorialPresenter presenter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

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
