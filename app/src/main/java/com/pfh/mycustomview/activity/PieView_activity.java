package com.pfh.mycustomview.activity;

import android.os.Bundle;

import com.pfh.mycustomview.R;
import com.pfh.mycustomview.model.PieData;
import com.pfh.mycustomview.view.PieView;

import java.util.ArrayList;
import java.util.List;


public class PieView_activity extends BaseActivity {

    private PieView pieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieview);
        setToolbarTitle("图表");
        pieView = (PieView) findViewById(R.id.pieview);
        int[] colors = {0xFFCCFF00,0xFF6495ED,0xFFE32636,0xFF7CFC00};//颜色
        List<PieData> pieDatas = new ArrayList<>();//数据源
        for (int i=0;i<4;i++){
            pieDatas.add(new PieData("name"+i, (float) (100*Math.random()),colors[i]));
        }
        pieView.setData(pieDatas);


    }


}
