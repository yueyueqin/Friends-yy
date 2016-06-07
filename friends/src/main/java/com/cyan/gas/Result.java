package com.cyan.gas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-06-08
 * Time: 04:40
 * 类说明：
 */
public class Result extends Activity
{
    private TextView tv_name, tv_money, tv_number, tv_type, tv_station, tv_data,btn_pay;


    PayOrder order;
    PopupWindow choseDialog;
    BmobPay bmobPay;
    Bitmap whole;
    public static final String APPID = "9c3147854c788ad6895cbdff4f386770";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_scanpay);

        BmobPay.init(this.getApplicationContext(), APPID);
        bmobPay = new BmobPay(this);
        order = new PayOrder();
        if (BmobUser.getCurrentUser(this) != null) {
            order.setUsername(BmobUser.getCurrentUser(this).getUsername());
        }
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_station = (TextView) findViewById(R.id.tv_station);
        btn_pay=(TextView) findViewById(R.id.btn_pay);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String data = intent.getStringExtra("data");
        String type = intent.getStringExtra("type");
        String number = intent.getStringExtra("number");
        String money = intent.getStringExtra("money");
        String station = intent.getStringExtra("station");
        tv_station.setText(station);
        tv_type.setText(type);
        tv_number.setText(number);
        tv_money.setText(money);
        tv_name.setText(name);
        tv_data.setText(data);
        btn_pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                choseType();
            }
        });
    }
    void choseType()
    {
        if (choseDialog == null) {
            View typeDialog = getLayoutInflater().inflate(
                    R.layout.dialog_paytype, null);
            typeDialog.findViewById(R.id.alipay).setOnClickListener(
                    new View.OnClickListener()
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
                    new View.OnClickListener()
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
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        choseDialog.showAtLocation((View) btn_pay.getParent(),
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
        order.save(Result.this);

        PayListener listener = new PayListener()
        {

            @Override
            public void unknow()
            {
                Toast.makeText(Result.this, "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void succeed()
            {
                Toast.makeText(Result.this,
                        "支付操作完成!请等待服务器校验通过即可满足您的要求!", Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void orderId(String arg0)
            {
                order.setOrderId(arg0);
                order.update(Result.this);
            }

            @Override
            public void fail(int arg0, String arg1)
            {
                Toast.makeText(Result.this, "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
                if (!byAli && arg0 == -3) {
                    Toast.makeText(Result.this, "您尚未安裝微信支付插件", Toast.LENGTH_LONG)
                            .show();
                    InstallPlugin.installBmobPayPlugin(Result.this,
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
                    Toast.makeText(Result.this, "支付失败，很抱歉你只能看这么一点了",
                            Toast.LENGTH_SHORT).show();
                } else {
                    order.setPaid(true);
                    order.update(Result.this);
                    Toast.makeText(Result.this,
                            "感谢你购买" + "!", Toast.LENGTH_SHORT)
                            .show();


                }
            }

            @Override
            public void fail(int arg0, String arg1)
            {
                Toast.makeText(Result.this, "查询失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
