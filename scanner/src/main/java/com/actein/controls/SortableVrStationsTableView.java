package com.actein.controls;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.actein.mvp.model.VrStation;
import com.actein.mvp.model.VrStationComparators;
import com.actein.scanner.R;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class SortableVrStationsTableView extends SortableTableView<VrStation>
{
    public SortableVrStationsTableView(final Context context)
    {
        this(context, null);
    }

    public SortableVrStationsTableView(final Context context, final AttributeSet attributes)
    {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableVrStationsTableView(final Context context,
                                       final AttributeSet attributes,
                                       final int styleAttributes)
    {
        super(context, attributes, styleAttributes);

        setColumnCount(COLUMN_COUNT);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter =
                new SimpleTableHeaderAdapter(context,
                                             R.string.booth_id_table_header,
                                             R.string.equipment_table_header,
                                             R.string.start_stop_table_header,
                                             R.string.online_status_table_header,
                                             R.string.experience_table_header,
                                             R.string.time_table_header);
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(COLUMN_COUNT);
        tableColumnWeightModel.setColumnWeight(Columns.BOOTH_CL.getValue(), 2);
        tableColumnWeightModel.setColumnWeight(Columns.EQUIPMENT_CL.getValue(), 3);
        tableColumnWeightModel.setColumnWeight(Columns.START_STOP_CL.getValue(), 2);
        tableColumnWeightModel.setColumnWeight(Columns.ONLINE_STATUS_CL.getValue(), 1);
        tableColumnWeightModel.setColumnWeight(Columns.EXPERIENCE_CL.getValue(), 3);
        tableColumnWeightModel.setColumnWeight(Columns.TIME_LEFT_CL.getValue(), 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(Columns.BOOTH_CL.getValue(), VrStationComparators.getBoothIdComparator());
        setColumnComparator(Columns.EQUIPMENT_CL.getValue(), VrStationComparators.getEquipmentComparator());
        setColumnComparator(Columns.START_STOP_CL.getValue(), VrStationComparators.getStartStopComparator());
        setColumnComparator(Columns.ONLINE_STATUS_CL.getValue(), VrStationComparators.getOnlineStatusComparator());
        setColumnComparator(Columns.EXPERIENCE_CL.getValue(), VrStationComparators.getExperienceComparator());
        setColumnComparator(Columns.TIME_LEFT_CL.getValue(), VrStationComparators.getTimeLeftComparator());
    }

    public enum Columns
    {
        BOOTH_CL(0),
        EQUIPMENT_CL(1),
        START_STOP_CL(2),
        ONLINE_STATUS_CL(3),
        EXPERIENCE_CL(4),
        TIME_LEFT_CL(5);

        Columns(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        private int value;
    }

    private static final int COLUMN_COUNT = 6;
}
