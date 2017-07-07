package com.actein.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actein.event.TurnGameOffEvent;
import com.actein.mvp.model.VrStation;
import com.actein.controls.SortableVrStationsTableView;
import com.actein.mvp.view.VrStationsView;
import com.actein.scanner.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class VrStationsTableDataAdapter extends TableDataAdapter<VrStation>
{
    public VrStationsTableDataAdapter(VrStationsView vrStationsView,
                                      Context context,
                                      List<VrStation> data)
    {
        super(context, data);
        mVrStationsView = vrStationsView;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView)
    {
        VrStation vrStation = getRowData(rowIndex);
        View renderedView = null;

        if (columnIndex == SortableVrStationsTableView.Columns.BOOTH_CL.getValue())
        {
            renderedView = renderString(Integer.toString(vrStation.getBoothId()));
        }
        else if (columnIndex == SortableVrStationsTableView.Columns.EQUIPMENT_CL.getValue())
        {
            renderedView = renderString(vrStation.getEquipment());
        }
        else if (columnIndex == SortableVrStationsTableView.Columns.START_STOP_CL.getValue())
        {
            if (vrStation.isGameRunning() || vrStation.isGameStopped())
                renderedView = renderImageButton(vrStation.getRunningIcon(), vrStation);
            else
                renderedView = renderProgressBar(vrStation.getRunningIcon());
        }
        else if (columnIndex == SortableVrStationsTableView.Columns.ONLINE_STATUS_CL.getValue())
        {
            renderedView = renderImage(vrStation.getOnlineIcon());
        }
        else if (columnIndex == SortableVrStationsTableView.Columns.EXPERIENCE_CL.getValue())
        {
            renderedView = renderString(vrStation.getExperience());
        }
        else if (columnIndex == SortableVrStationsTableView.Columns.TIME_LEFT_CL.getValue())
        {
            renderedView = renderString(vrStation.getTimeStr());
        }

        return renderedView;
    }

    private View renderImage(final int resId)
    {
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resId);
        return imageView;
    }

    private View renderProgressBar(final int resId)
    {
        return mVrStationsView.getLayoutInflater().inflate(resId, null);
    }

    private View renderImageButton(final int resId, final VrStation vrStation)
    {
        final ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(resId);

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (vrStation.isGameRunning())
                {
                    EventBus.getDefault().post(new TurnGameOffEvent(vrStation));
                }
                else if (vrStation.isGameStopped())
                {
                    mVrStationsView.startGameStartActivity(vrStation);
                }
            }
        });

        imageButton.setEnabled(vrStation.isOnline());

        return imageButton;
    }

    private View renderString(final String value)
    {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                             getResources().getDimension(R.dimen.vr_stations_text_size));
        return textView;
    }

    private VrStationsView mVrStationsView;
}
