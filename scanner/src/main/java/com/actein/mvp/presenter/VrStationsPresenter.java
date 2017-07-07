package com.actein.mvp.presenter;

import android.content.Intent;

import com.actein.adapter.VrStationsTableDataAdapter;

public interface VrStationsPresenter extends Presenter
{
    VrStationsTableDataAdapter createDataAdapter();
    void onGameStartActivityResult(Intent intent);
}
