package com.cyan.gas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
import com.cyan.community.R;
import com.cyan.scanpay.InstallPlugin;
import com.cyan.scanpay.PayOrder;

import cn.bmob.v3.BmobUser;

/**
 * @author YueYue
 */
public class Information_detail extends Activity
{
    private TextView orderd_data, orderd_gas, orderd_model, orderd_volume,
            orderd_total, orderd_name, user_pay, user_logout01, tv_order, tv_title_button;
    PayOrder order;
    PopupWindow choseDialog;
    BmobPay bmobPay;
    Bitmap whole;
    public static final String APPID = "9c3147854c788ad6895cbdff4f386770";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.information_details);

        BmobPay.init(this.getApplicationContext(), APPID);
        bmobPay = new BmobPay(this);
        order = new PayOrder();
        if (BmobUser.getCurrentUser(this) != null) {
            order.setUsername(BmobUser.getCurrentUser(this).getUsername());
        }

        orderd_data = (TextView) findViewById(R.id.orderd_data);
        orderd_gas = (TextView) findViewById(R.id.orderd_gas);
        orderd_model = (TextView) findViewById(R.id.orderd_model);
        orderd_volume = (TextView) findViewById(R.id.orderd_volume);
        orderd_total = (TextView) findViewById(R.id.orderd_total);
        orderd_name = (TextView) findViewById(R.id.orderd_name);
        user_pay = (TextView) findViewById(R.id.user_pay);
        user_logout01 = (TextView) findViewById(R.id.user_logout01);


        Bundle bundle = getIntent().getExtras();
        orderd_data.setText(bundle.getString("data"));
        orderd_gas.setText(bundle.getString("gasstation"));
        orderd_model.setText(bundle.getString("model"));
        orderd_volume.setText(bundle.getString("volume"));
        orderd_total.setText(bundle.getString("total"));
        Log.e("orderd_total金额是多少啊？？？", "" + orderd_total);
        order.setPrice(Double.parseDouble(orderd_total.getText().toString()));
        orderd_name.setText(bundle.getString("name"));
        user_pay.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                choseType();
            }
        });
        user_logout01.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Information_detail.this, Qr_codeActivity.class);

                String name = orderd_name.getText().toString().trim();
                String station_name = orderd_gas.getText().toString().trim();
                String volume = orderd_volume.getText().toString().trim();
                String data = orderd_data.getText().toString().trim();
                String model = orderd_model.getText().toString().trim();
                String total = orderd_total.getText().toString().trim();
                String contents = "BEGIN:VCARD\nVERSION:3.0\n" + "N:" + name
                        + "\nNOTE:" + data + "\nORG:" + station_name +
                        "\nTEL:" + model + "\nTITLE:" + volume + "\nADR:" + total
                        + "\nURL:";
                Log.e("contents的内容是？？", "" + contents);
                intent.putExtra("contents", contents);


                startActivity(intent);

            }

        });
    }


    void choseType()
    {
        if (choseDialog == null) {
            View typeDialog = getLayoutInflater().inflate(
                    R.layout.dialog_paytype, null);
            typeDialog.findViewById(R.id.alipay).setOnClickListener(
                    new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (choseDialog != null && choseDialog.isShowing())
                                choseDialog.dismiss();
                            pay(true);
                        }
                    });
            typeDialog.findViewById(R.id.wxpay).setOnClickListener(
                    new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (choseDialog != null && choseDialog.isShowing())
                                choseDialog.dismiss();
                            pay(true);
                        }
                    });
            choseDialog = new PopupWindow(typeDialog,
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            choseDialog.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.bg_settings_item_down_pressed));
            choseDialog.setOutsideTouchable(true);
        }
        Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        choseDialog.showAtLocation((View) user_pay.getParent(),
                Gravity.CENTER, 0, 0);

    }

    protected final View getRootView(Activity context)
    {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    void pay(final boolean byAli)
    {
        Toast.makeText(this, "正在申请支付。。请稍候。。", Toast.LENGTH_SHORT).show();

        order.setPaid(true);
        order.save(Information_detail.this);

        PayListener listener = new PayListener()
        {

            @Override
            public void unknow()
            {
                Toast.makeText(Information_detail.this, "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void succeed()
            {
                Toast.makeText(Information_detail.this,
                        "支付操作完成!请等待服务器校验通过即可满足您的要求!", Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void orderId(String arg0)
            {
                order.setOrderId(arg0);
                order.update(Information_detail.this);
            }

            @Override
            public void fail(int arg0, String arg1)
            {
                Toast.makeText(Information_detail.this, "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
                if (!byAli && arg0 == -3) {
                    Toast.makeText(Information_detail.this, "您尚未安裝微信支付插件", Toast.LENGTH_LONG)
                            .show();
                    InstallPlugin.installBmobPayPlugin(Information_detail.this,
                            InstallPlugin.ASSETS_PLUGIN);
                }
            }
        };

        if (byAli) {
            bmobPay.pay(Double.parseDouble(order.getPrice().toString()),
                    "您要支付", listener);
        } else {
            bmobPay.payByWX(Double.parseDouble(order.getPrice().toString()),
                    "您要支付", listener);
        }

    }

    void checkAgain()
    {
        bmobPay.query(order.getOrderId(), new OrderQueryListener()
        {

            @Override
            public void succeed(String arg0)
            {
                if (arg0.equals("NOTPAY")) {
                    Toast.makeText(Information_detail.this, "支付失败，很抱歉你只能看这么一点了",
                            Toast.LENGTH_SHORT).show();
                } else {
                    order.setPaid(true);
                    order.update(Information_detail.this);
                    Toast.makeText(Information_detail.this,
                            "感谢你购买" + "!", Toast.LENGTH_SHORT)
                            .show();


                }
            }

            @Override
            public void fail(int arg0, String arg1)
            {
                Toast.makeText(Information_detail.this, "查询失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
