package com.tea.teahome.User.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.R;
import com.tea.teahome.User.Utils.ZoneId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.tea.teahome.Utils.ViewUtil.addStatusBar;

/**
 * @author jiang yuhang
 * @version 1.0
 * @className UserInfZoneActivity
 * @program teaHome
 * @date 2021-04-17 18:28
 */
public class UserInfZoneActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    static List<String> items = ZoneId.getAllZoneIdsAndItsOffSet();

    @BindView(R.id.lv_zone)
    ListView lv_zone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inf_zone);
        ButterKnife.bind(this);
        addStatusBar(this, R.id.linearLayout, R.color.statusBar_color);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_list, items);

        lv_zone.setAdapter(adapter);
    }


    @OnClick(R.id.back)
    @Override
    public void onClick(View v) {
        finish();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @OnItemClick(R.id.lv_zone)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("selected_item", items.get(position).split(" ")[0]);
        setResult(0, intent);
        finish();
    }
}