package com.tea.iot.sharingdevice;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.tea.iot.CommonModule.GosBaseActivity;
import com.tea.iot.R;

/**
 * Created by Sunny on 2015年6月25日
 *
 * @author Sunny
 */
public class MsgNoticeActivity extends GosBaseActivity {
    private ListView lvNotice;
    private TextView tvNoNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

//		setActionBar(true, true, R.string.msg_notice);
        initView();
//		initData();
    }

    private void initView() {
        lvNotice = (ListView) findViewById(R.id.lvNotice);
        tvNoNotice = (TextView) findViewById(R.id.tvNoNotice);
    }

//	private void initData(){
//		NoticeDBService dbService= new NoticeDBService(this);
//		ArrayList<NoticeBean> lsNotice=dbService.getNoticeList();
//		
//		if(lsNotice!=null&&lsNotice.size()>0){
//			lvNotice.setVisibility(View.VISIBLE);
//			tvNoNotice.setVisibility(View.GONE);
//			
//			NoticeAdapter na=new NoticeAdapter(this, lsNotice);
//			lvNotice.setAdapter(na);
//		}else{
//			lvNotice.setVisibility(View.GONE);
//			tvNoNotice.setVisibility(View.VISIBLE);
//		}
//	}

    @Override
    public void onResume() {
        super.onResume();
//		initData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        super.onOptionsItemSelected(menu);
        if (menu.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}