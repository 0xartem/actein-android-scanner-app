package com.actein.activity;

import android.content.Intent;
import android.os.Bundle;

import com.actein.common.Intents;
import com.actein.controls.SortableVrStationsTableView;
import com.actein.mvp.model.VrStation;
import com.actein.mvp.presenter.VrStationsActivityPresenter;
import com.actein.mvp.presenter.VrStationsPresenter;
import com.actein.mvp.view.VrStationsView;
import com.actein.scanner.R;

public class VrStationsManagementActivity extends BaseActivity implements VrStationsView
{
    public VrStationsManagementActivity()
    {
        mPresenter = new VrStationsActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_stations_management);

        mPresenter.onCreate();

        final SortableVrStationsTableView vrStationsTableView =
                (SortableVrStationsTableView) findViewById(R.id.vr_stations_table_view);

        if (vrStationsTableView != null)
        {
            vrStationsTableView.setDataAdapter(mPresenter.createDataAdapter());
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPresenter.onDestroy(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK && requestCode == GAME_START_REQUEST_CODE)
        {
            mPresenter.onGameStartActivityResult(intent);
        }
    }

    @Override
    public void startGameStartActivity(VrStation vrStation)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName(this, StartGameActivity.class.getName());
        intent.putExtra(Intents.StartGame.BOOTH_ID, vrStation.getBoothId());
        startActivityForResult(intent, GAME_START_REQUEST_CODE);
    }

    private VrStationsPresenter mPresenter;
    private static final int GAME_START_REQUEST_CODE = 1;

}
