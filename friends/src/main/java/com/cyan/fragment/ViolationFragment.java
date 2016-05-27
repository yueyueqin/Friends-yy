package com.cyan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.WeizhangIntentService;
import com.cheshouye.api.client.json.CarInfo;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.InputConfigJson;
import com.cyan.community.R;
import com.cyan.ui.MainActivity;
import com.cyan.violation.ProvinceList;
import com.cyan.violation.ShortNameList;
import com.cyan.violation.WeizhangResult;


/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:44
 * 类说明：
 */
public class ViolationFragment extends PreferenceFragment implements View.OnClickListener
{
    private String defaultChepai = "粤"; // 粤=广东

    private TextView short_name;
    private TextView query_city;
    private View btn_cpsz;
    private Button btn_query;

    private EditText chepai_number;
    private EditText chejia_number;
    private EditText engine_number;

    // 行驶证图示
    private View popXSZ;

    public static ViolationFragment newInstance(){
        ViolationFragment violationFragment=new ViolationFragment();
        return violationFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("初始化服务代码","");
        Intent weizhangIntent = new Intent(getActivity(), WeizhangIntentService.class);
        weizhangIntent.putExtra("appId","您的appId");// 您的appId
        weizhangIntent.putExtra("appKey", "您的appKey");// 您的appKey
        getActivity().startService(weizhangIntent);
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_violation, null);
        initView(fragmentView);
        return fragmentView;
    }
    private void initView(View fragmentRootView)
    {
        // 标题
        TextView txtTitle = (TextView)fragmentRootView. findViewById(R.id.txtTitle);

        // ********************************************************

        // 选择省份缩写
        query_city = (TextView) fragmentRootView.findViewById(R.id.cx_city);
        chepai_number = (EditText)fragmentRootView. findViewById(R.id.chepai_number);
        chejia_number = (EditText)fragmentRootView. findViewById(R.id.chejia_number);
        engine_number = (EditText)fragmentRootView. findViewById(R.id.engine_number);
        short_name = (TextView) fragmentRootView.findViewById(R.id.chepai_sz);
short_name.setText(defaultChepai);
        // ----------------------------------------------

        btn_cpsz = (View)fragmentRootView. findViewById(R.id.btn_cpsz);
        btn_query = (Button)fragmentRootView. findViewById(R.id.btn_query);


        btn_cpsz.setOnClickListener(this);
        query_city.setOnClickListener(this);
        btn_query.setOnClickListener(this);


        // 显示隐藏行驶证图示

        popXSZ = (View)fragmentRootView. findViewById(R.id.popXSZ);
        popXSZ.setOnTouchListener(new popOnTouchListener());
        hideShowXSZ();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btn_cpsz://车牌号码查询
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShortNameList.class);
                intent.putExtra("select_short_name", short_name.getText());
                startActivityForResult(intent, 0);
            case R.id.query_city://查询地选择
                Intent Intent_q = new Intent();
                Intent_q.setClass(getActivity(), ProvinceList.class);
                startActivityForResult(Intent_q, 1);
            case R.id.btn_query:
                // 获取违章信息
                CarInfo car = new CarInfo();
                String quertCityStr = null;
                String quertCityIdStr = null;

                final String shortNameStr = short_name.getText().toString()
                        .trim();
                final String chepaiNumberStr = chepai_number.getText()
                        .toString().trim();
                if (query_city.getText() != null
                        && !query_city.getText().equals("")) {
                    quertCityStr = query_city.getText().toString().trim();

                }

                if (query_city.getTag() != null
                        && !query_city.getTag().equals("")) {
                    quertCityIdStr = query_city.getTag().toString().trim();
                    car.setCity_id(Integer.parseInt(quertCityIdStr));
                }
                final String chejiaNumberStr = chejia_number.getText()
                        .toString().trim();
                final String engineNumberStr = engine_number.getText()
                        .toString().trim();

                Intent intent3 = new Intent();

                car.setChejia_no(chejiaNumberStr);
                car.setChepai_no(shortNameStr + chepaiNumberStr);

                car.setEngine_no(engineNumberStr);

                Bundle bundle = new Bundle();
                bundle.putSerializable("carInfo", car);
                intent3.putExtras(bundle);

                boolean result = checkQueryItem(car);

                if (result) {
                    intent3.setClass(getActivity(), WeizhangResult.class);
                    startActivity(intent3);

                }

        }
    }

    // 根据默认查询地城市id, 初始化查询项目
    // setQueryItem(defaultCityId, defaultCityName);
  //  short_name.setText(defaultChepai);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 0:
                Bundle bundle = data.getExtras();
                String ShortName = bundle.getString("short_name");
                short_name.setText(ShortName);
                break;
            case 1:
                Bundle bundle1 = data.getExtras();
                // String cityName = bundle1.getString("city_name");
                String cityId = bundle1.getString("city_id");
                // query_city.setText(cityName);
                // query_city.setTag(cityId);
                // InputConfigJson inputConfig =
                // WeizhangClient.getInputConfig(Integer.parseInt(cityId));
                // System.out.println(inputConfig.toJson());
                setQueryItem(Integer.parseInt(cityId));

                break;
        }
    }

    // 根据城市的配置设置查询项目
    private void setQueryItem(int cityId) {

        InputConfigJson cityConfig = WeizhangClient.getInputConfig(cityId);

        // 没有初始化完成的时候;
        if (cityConfig != null) {
            CityInfoJson city = WeizhangClient.getCity(cityId);

            query_city.setText(city.getCity_name());
            query_city.setTag(cityId);

            int len_chejia = cityConfig.getClassno();
            int len_engine = cityConfig.getEngineno();

            View row_chejia = (View)fragmentView. findViewById(R.id.row_chejia);
            View row_engine = (View)fragmentView. findViewById(R.id.row_engine);

            // 车架号
            if (len_chejia == 0) {
                row_chejia.setVisibility(View.GONE);
            } else {
                row_chejia.setVisibility(View.VISIBLE);
                setMaxlength(chejia_number, len_chejia);
                if (len_chejia == -1) {
                    chejia_number.setHint("请输入完整车架号");
                } else if (len_chejia > 0) {
                    chejia_number.setHint("请输入车架号后" + len_chejia + "位");
                }
            }

            // 发动机号
            if (len_engine == 0) {
                row_engine.setVisibility(View.GONE);
            } else {
                row_engine.setVisibility(View.VISIBLE);
                setMaxlength(engine_number, len_engine);
                if (len_engine == -1) {
                    engine_number.setHint("请输入完整车发动机号");
                } else if (len_engine > 0) {
                    engine_number.setHint("请输入发动机后" + len_engine + "位");
                }
            }
        }
    }

    // 提交表单检测
    private boolean checkQueryItem(CarInfo car) {
        if (car.getCity_id() == 0) {
            Toast.makeText(getActivity(), "请选择查询地", 0).show();
            return false;
        }

        if (car.getChepai_no().length() != 7) {
            Toast.makeText(getActivity(), "您输入的车牌号有误", 0).show();
            return false;
        }

        if (car.getCity_id() > 0) {
            InputConfigJson inputConfig = WeizhangClient.getInputConfig(car
                    .getCity_id());
            int engineno = inputConfig.getEngineno();
            int registno = inputConfig.getRegistno();
            int classno = inputConfig.getClassno();

            // 车架号
            if (classno > 0) {
                if (car.getChejia_no().equals("")) {
                    Toast.makeText(getActivity(), "输入车架号不为空", 0).show();
                    return false;
                }

                if (car.getChejia_no().length() != classno) {
                    Toast.makeText(getActivity(), "输入车架号后" + classno + "位",
                            0).show();
                    return false;
                }
            } else if (classno < 0) {
                if (car.getChejia_no().length() == 0) {
                    Toast.makeText(getActivity(), "输入全部车架号", 0).show();
                    return false;
                }
            }

            //发动机
            if (engineno > 0) {
                if (car.getEngine_no().equals("")) {
                    Toast.makeText(getActivity(), "输入发动机号不为空", 0).show();
                    return false;
                }

                if (car.getEngine_no().length() != engineno) {
                    Toast.makeText(getActivity(),
                            "输入发动机号后" + engineno + "位", 0).show();
                    return false;
                }
            } else if (engineno < 0) {
                if (car.getEngine_no().length() == 0) {
                    Toast.makeText(getActivity(), "输入全部发动机号", 0).show();
                    return false;
                }
            }

            // 注册证书编号
            if (registno > 0) {
                if (car.getRegister_no().equals("")) {
                    Toast.makeText(getActivity(), "输入证书编号不为空", 0).show();
                    return false;
                }

                if (car.getRegister_no().length() != registno) {
                    Toast.makeText(getActivity(),
                            "输入证书编号后" + registno + "位", 0).show();
                    return false;
                }
            } else if (registno < 0) {
                if (car.getRegister_no().length() == 0) {
                    Toast.makeText(getActivity(), "输入全部证书编号", 0).show();
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    // 设置/取消最大长度限制
    private void setMaxlength(EditText et, int maxLength) {
        if (maxLength > 0) {
            et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                    maxLength) });
        } else { // 不限制
            et.setFilters(new InputFilter[] {});
        }
    }

    // 显示隐藏行驶证图示
    private void hideShowXSZ() {

        View btn_help1 = (View)fragmentView.findViewById(R.id.ico_chejia);
        if (btn_help1==null){
            Log.e("btn_help1kongde","+++++++");
        }
        View btn_help2 = (View)fragmentView.findViewById(R.id.ico_engine);
        if (btn_help2==null){
            Log.e("btn_help2kongde","+++++++");
        }
       // Button btn_closeXSZ = (Button)findViewById(R.id.btn_closeXSZ);
        Button btn=(Button)fragmentView.findViewById(R.id.btn_closeXSZ);
        if (btn==null){
            Log.e("btn","+++++++");
        }


        btn_help1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn_help2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.GONE);
            }
        });
    }

// 避免穿透导致表单元素取得焦点
private class popOnTouchListener implements OnTouchListener {
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        popXSZ.setVisibility(View.GONE);
        return true;
    }
}

}  
