
package com.cyan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cyan.community.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kys-34 on 2016/5/12.
 */
public class CarInfoActivity extends Activity implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {



    //region 成员变量 ——数组
    private String[] pinpai = {
            "奥迪", "宝马", "保时捷", "奔驰", "大众", "迈巴赫", "MINI",
            "斯柯达", "smart", "威兹曼", "别克", "福特", "悍马", "Jeep吉普", "凯迪拉克", "克莱斯勒",
            "林肯", "欧宝", "雪佛兰", "标致", "雷诺", "雪铁龙", "布加迪", "法拉利", "菲亚特", "兰博基尼",
            "玛莎拉蒂", "本田", "丰田", "雷克萨斯", "铃木", "马自达", "讴歌", "日产", "三菱", "斯巴鲁",
            "英菲尼迪", "起亚", "双龙", "现代", "阿斯顿马丁", "宾利", "捷豹", "劳斯莱斯", "路虎", "莲花",
            "MG", "比亚迪", "长安", "长安商用", "长城", "长丰", "帝豪", "东风风行", "东南", "风神", "广汽",
            "哈飞", "海马", "红旗", "华普", "汇众", "江淮", "江铃", "吉奥", "金杯", "开瑞", "猎豹", "力帆",
            "理念", "陆风", "纳智捷", "启辰", "奇瑞", "全球鹰", "荣威", "瑞麒", "双杯", "思铭", "威麟",
            "五菱", "川汽野马", "英伦汽车", "一汽", "一汽吉林", "一汽通用", "中华", "众泰", "中兴", "中誉",
            "萨博", "沃尔沃"
    };
    private String[] xinghaoaodi = {
            "A4L", "A6L", "Q5", "A!", "A3", "A5双门", "A5掀背", "A5敞篷", "A7", "A8L", "Q5", "Q7",
            "R8", "S5掀背", "S5敞篷", "S5双门", "S8", "TT"
    };
    private String[] xinghaobaoma = {
            "华晨宝马3系", "华晨宝马5系", "华晨宝马X1", "进口宝马1系敞篷", "进口宝马1系", "进口宝马1系双门",
            "进口宝马1系Mcoupe", "进口宝马3系（进口）", "进口宝马3系敞篷", "进口宝马3系双门", "进口宝马5系",
            "进口宝马5系GT", "进口宝马旅行车", "进口宝马6系敞篷", "进口宝马6系双门", "进口宝马7系",
            "进口宝马6系Gran Coupe", "进口宝马M3", "进口宝马M5", "进口宝马M6", "进口宝马X1(进口)",
            "进口宝马X3", "进口宝马X5", "进口宝马X5M", "进口宝马X6", "进口宝马X6M", "进口宝马Z4",
    };
    private String[] xinghaobaoshijie = {
            " 911", " Boxster", " Cayman", " Cayenne", " Panamera"
    };
    private String[] xinghaobenchi = {
            " 北京奔驰C级", "北京奔驰E级长轴距", "北京奔驰GLK级",
            " 福建奔驰凌特", " 福建奔驰威霆", " 福建奔驰唯雅诺",
            " 进口奔驰A级", " 进口奔驰SLK级", " 进口奔驰B级", "进口奔驰C63 AMG", " 进口奔驰C级旅行",
            " 进口奔驰CLK级敞篷", " 进口奔驰CLS级", " 进口奔驰CLS级 AMG", " 进口奔驰E级敞篷", " 进口奔驰E级双门",
            "进口奔驰G55 AMG", " 进口奔驰G级", " 进口奔驰GL级", " 进口奔驰GLK级（进口）", " 进口奔驰ML级",
            " 进口奔驰ML63 AMG", " 进口奔驰R级", " 进口奔驰S级 AMG", " 进口奔驰S级", " 进口奔驰S级混合动力",
            " 进口奔驰SL级", " 进口奔驰SL级AMG", " 进口奔驰SLK55 AMG", " 进口奔驰SLS AMG"
    };

    private String[] xinghaodazhong = {
            " 上海大众Cross POLO", " 上海大众朗逸", " 上海大众帕萨特领驭", " 上海大众Polo", " 上海大众Polo Sporty", " 上海大众Polo劲取", " 上海大众帕萨特", " 上海大众桑塔纳", " 上海大众桑塔纳志俊", " 上海大众途安", " 上海大众途观",
            "一汽大众宝来", " 一汽大众CC", " 一汽大众高尔夫", " 一汽大众高尔夫GTI", " 一汽大众捷达", " 一汽大众迈腾", " 一汽大众速腾",
            " 进口大众CC(进口)", " 进口大众CrossGolf", " 进口大众Eos", " 进口大众高尔夫R", " 进口大众高尔夫敞篷", " 进口大众Golf Variant", " 进口大众辉腾", " 进口大众甲壳虫", " 进口大众甲壳虫敞篷", " 进口大众旅行车", " 进口大众迈腾旅行车", " 进口大众R36", " 进口大众尚酷", " 进口大众尚酷R", " 进口大众T5", " 进口大众Tiguan", " 进口大众途锐", " 进口大众夏朗"
    };
    private String[] xinghaomaibahe = {
            " 57", " 62"
    };
    private String[] xinghaoMINI = {
            "MINI", " CLUBMAN", " COUNTRYMAN", " COUPE", " ROADSTER", " CABRIO"
    };
    private String[] xinghaosikeda = {
            " 上海大众斯柯达", " 晶锐", " 晶锐跨界版", " 明锐", " 明锐RS", " 昊锐"
    };
    private String[] xinghaosmart = {
            " fortwo"
    };
    private String[] xinghaoweiziman = {
            " MF4", " MF5"
    };
    private String[] xinghaobieke = {
            " 上海通用别克GL8", " 上海通用别克君威", " 上海通用别克君威GS", " 上海通用别克君越", " 上海通用别克君越混合动力", " 上海通用别克凯越", " 上海通用别克林荫大道", " 上海通用别克英朗GT", " 上海通用别克英朗XT", " 进口别克昂科雷"
    };
    private String[] xinghaofute = {
            " 长安福特福克斯两厢", " 长安福特福克斯三厢", " 长安福特嘉年华两厢", " 长安福特嘉年华三厢", " 长安福特麦柯斯S-MAX", " 长安福特蒙迪欧致胜", " 长安福特翼虎",
            " 进口福特F-150", " 进口福特锐界", " 进口福特野马", " 进口福特悍马", " 进口福特H3"
    };
    private String[] xinghaohanma = {
            "H3"
    };
    private String[] xinghaoJeep = {
            " 大切诺基", " 牧马人两门版", " 牧马人四门版", " 指挥官", " 指南者", " 自由客"
    };
    private String[] xinghaokaidilake = {
            " 上海通用凯迪拉克SLS赛威 3",
            " 进口凯迪拉克CTS 3", " 进口凯迪拉克CTS Coupe", " 进口凯迪拉克CTS-V", " 进口凯迪拉克凯雷德混动", " 进口凯迪拉克SRX 4", " 进口凯迪拉克XLR",
    };
    private String[] xinghaokelaisile = {
            " 大捷龙", " PT漫步者"
    };
    private String[] xinghaolinken = {
            " 领航员", " MKS", " MKT", " MKX", " MKZ"
    };
    private String[] xinghaooubao = {
            " 安德拉", " 赛飞利", " 雅特", " 雅特A+", " 雅特敞篷"
    };
    private String[] xinghaoxuefulan = {
            " 上海通用雪佛兰爱唯欧两厢", " 上海通用雪佛兰爱唯欧三厢", " 上海通用雪佛兰景程", " 上海通用雪佛兰科鲁兹", " 上海通用雪佛兰科帕奇", " 上海通用雪佛兰乐骋", " 上海通用雪佛兰乐风", " 上海通用雪佛兰迈锐宝", " 上海通用雪佛兰赛欧两厢", " 上海通用雪佛兰赛欧三厢",
            " 进口雪佛兰Camaro科迈罗", " 进口雪佛兰科帕奇(进口)", " 进口雪佛兰斯帕可", " 进口雪佛兰Volt沃蓝达"
    };
    private String[] xinghaobiaozhi = {
            " 东风标致207两厢", " 东风标致207三厢", " 东风标致307两厢", " 东风标致307三厢", " 东风标致308", " 东风标致408", " 东风标致508", " 东风标致Cross 307",
            " 进口标致207CC", " 进口标致3008", " 进口标致308CC", " 进口标致308SW", " 进口标致407", " 进口标致407Coupe", " 进口标致407SW", " 进口标致607", " 进口标致4008", " 进口标致RCZ"
    };
    private String[] xinghaoleinuo = {
            " 风景", " 风朗", " 科雷傲", " 拉古那", " 拉古那-古贝", " 梅甘娜", " 梅甘娜CC", " 塔利斯曼", " 纬度"
    };
    private String[] xinghaoxuetielong = {
            " 东风雪铁龙爱丽舍两厢", " 东风雪铁龙爱丽舍三厢", " 东风雪铁龙C2", " 东风雪铁龙C5", " 东风雪铁龙凯旋", " 东风雪铁龙萨拉毕加索", " 东风雪铁龙世嘉两厢", " 东风雪铁龙世嘉三厢",
            " 进口雪铁龙C4", "进口雪铁龙C4 Aircross", "进口雪铁龙C6", " 进口雪铁龙大C4毕加索"
    };
    private String[] xinghaobujiadi = {
            "威航"
    };
    private String[] xinghaofalali = {
            " 458", " 599", " 612-OTO", " California", " FF"
    };
    private String[] xinghaofeiyate = {
            " 500", " 500敞篷版", " 博悦", " 菲跃", " 领雅", " 朋多"
    };
    private String[] xinghaolanbojini = {
            " Aventador", " 盖拉多", " 蝙蝠"
    };
    private String[] xinghaomashaladi = {
            " GranCabrio", " GT", " 总裁"
    };
    private String[] xinghaobentian = {
            " 广汽本田奥德赛", " 广汽本田飞度", " 广汽本田锋范", " 广汽本田歌诗图", " 广汽本田雅阁",
            " 东风本田CR-V", " 东风本田思铂睿", " 东风本田思域", " 东风本田思域混合动力"
    };
    private String[] xinghaofengtian = {
            " 一汽丰田花冠EX", " 一汽丰田皇冠", " 一汽丰田卡罗拉", " 一汽丰田兰德酷路泽", " 一汽丰田普锐斯", " 一汽丰田RAV4", " 一汽丰田锐志", " 一汽丰田威驰",
            " 广汽丰田汉兰达", " 广汽丰田凯美瑞", " 广汽丰田凯美瑞尊瑞混动", " 广汽丰田雅力士", " 广汽丰田逸致",
            " 进口丰田埃尔法", " 进口丰田FJ酷路泽", " 进口丰田海狮", " 进口丰田普拉多", " 进口丰田普瑞维亚", " 进口丰田ZELAS杰路驰"
    };
    private String[] xinghaoleikesasi = {
            " CT200h", " ES", " GS", " GS混动", " GX", " IS", " IS敞篷", " LF-A", " LS", " LS混动", " LX", " RX", " RX混动", " SC"
    };
    private String[] xinghaolingmu = {
            " 长安铃木奥拓", " 长安铃木羚羊", " 长安铃木天语尚悦", " 长安铃木天语SX4", " 长安铃木雨燕",
            " 昌河铃木北斗星", " 昌河铃木北斗星e+", " 昌河铃木浪迪", " 昌河铃木利亚纳两厢", " 昌河铃木利亚纳三厢", " 昌河铃木派喜",
            " 进口铃木超级维特拉", " 进口铃木吉姆尼", " 进口铃木凯泽西"
    };
    private String[] xinghaomazida = {
            " 一汽马自达Mazda8", " 一汽马自达Mazda6", "一汽马自达睿翼", " 一汽马自达睿翼轿跑车",
            "长安马自达2", " 长安马自达2劲翔", " 长安马自达3经典", " 长安马自达3星骋", " 长安马自达3星骋两厢",
            " 进口马自达CX-7", " 进口马自达Mazda5", " 进口马自达MX-5"
    };
    private String[] xinghaoouge = {
            " MDX", " RL", " TL", " ZDX"
    };
    private String[] xinghaorichan = {
            " 东风日产楼兰", " 东风日产玛驰", " 东风日产奇骏", " 东风日产天籁", " 东风日产逍客", " 东风日产轩逸", " 东风日产阳光", " 东风日产颐达", " 东风日产骐达", " 东风日产骊威",
            " 郑州日产奥丁", "郑州日产D22", " 郑州日产凯普斯达", " 郑州日产NV200", " 郑州日产帕拉丁", " 郑州日产帕拉骐", " 郑州日产锐骐多功能商用车", " 郑州日产锐骐皮卡", " 郑州日产帅客", " 郑州日产途乐", " 郑州日产御轩", " 郑州日产ZN6493",
            " 进口日产370Z", " 进口日产GT-R", " 进口日产贵士"
    };
    private String[] xinghaosanling = {
            " 东南三菱戈蓝", " 东南三菱君阁", " 东南三菱蓝瑟", " 东南三菱蓝瑟EX", " 东南三菱翼神",
            " 长丰三菱帕杰罗",
            " 进口三菱格蓝迪", " 进口三菱帕杰罗劲畅", " 进口三菱劲炫（进口）", " 进口三菱蓝瑟 翼豪陆神", " 进口三菱欧蓝德劲界", " 进口三菱帕杰罗长轴", " 进口三菱帕杰罗短轴", " 进口三菱伊柯丽斯"
    };
    private String[] xinghaosibalu = {
            "傲虎", "驰鹏", "力狮", "力狮wagon", "森林人", "XV", "翼豹两厢 2", "翼豹三厢"
    };
    private String[] xinghaoyingfeinidi = {
            " EX", " FX", " G", " G级敞篷", " G级双门", "M", "QX"
    };
    private String[] xinghaoqiya = {
            " 东风锐达起亚福瑞迪", " 东风锐达起亚嘉华", " 东风锐达起亚K5", " 东风锐达起亚K2三厢", " 东风锐达起亚K2两厢", " 东风锐达起亚RIO", " 东风锐达起亚赛拉图", " 东风锐达起亚赛拉图欧风", " 东风锐达起亚狮跑", " 东风锐达起亚秀尔", " 东风锐达起亚智跑",
            "进口起亚霸锐", "进口起亚新佳乐", "进口起亚凯尊", "进口起亚欧菲莱斯", "进口起亚速迈", "进口起亚索兰托 ", "进口起亚VQ威客"
    };
    private String[] xinghaoshuanglong = {
            "爱腾", "雷斯特", "路帝", "柯兰多", "享御", "主席"
    };
    private String[] xinghaoxiandai = {
            "北京现代i30", "北京现代ix35", "北京现代名驭", "北京现代瑞纳两厢", "北京现代索纳塔八", "北京现代途胜", "北京现代瑞纳三厢", "北京现代雅绅特", "北京现代伊兰特", "北京现代悦动",
            "进口现代飞思Veloster", "进口现代劳恩斯", "进口现代劳恩斯-酷派", "进口现代维拉克斯", "进口现代新胜达", "进口现代雅科仕", "进口现代雅尊",
    };
    private String[] xinghaoasidunmading = {
            "DB9", "DB9 敞篷", "DBS", "Rapide", "V8 Vantage", "V8 VR", "Virage"

    };
    private String[] xinghaobinli = {
            "慕尚", "欧陆", "雅致"
    };
    private String[] xinghaojiebao = {
            "XF", "XJ", "XK"
    };
    private String[] xinghaolaosilaisi = {
            "古思特", "幻影"
    };
    private String[] xinghaoluhu = {
            "发现4", "揽胜", "揽胜极光", "揽胜运动版 ", "神行者2", "卫士"
    };
    private String[] xinghaolianhua = {
            "爱丽丝", "Evora", "Exige"
    };
    private String[] xinghaoMG = {
            "3", "6", "7", "5", "6 Saloon", "TF"
    };
    private String[] xinghaobiyadi = {
            "e6先行者", "S6", "F0", "F3", "F3DM", "F3R", "F6", "G3", "G3R", "G6", "L3", "M6", "S8"
    };
    private String[] xinghaochangan = {
            "奔奔i", "奔奔love", "奔奔mini", "逸动", "长安CM8", "CX20", "CX30三厢", "杰勋", "杰勋HEV", "悦翔两厢", "悦翔三厢", "志翔"
    };
    private String[] xinghaochanganshangyong = {
            "长安之星", "金牛星", "长安欧诺"
    };
    private String[] xinghaochangcheng = {
            "腾翼C20R", "C50", "风骏", "哈弗H3", "哈弗H5", "哈弗H6", "哈弗M1", "哈弗M2", "哈弗 派", "金迪尔",
            "精灵", "精灵Cross", "酷熊", "凌傲", "赛酷", "赛铃", "腾翼C30", "腾翼V80", "炫丽Cross", "炫丽"
    };
    private String[] xinghaochangfeng = {
            "DUV", "飞扬", "骐菱"
    };
    private String[] xinghaodihao = {
            "EC7", "EC7-RV", "EC8"
    };
    private String[] xinghaodongfengfengxing = {
            "景逸", "景逸LV", "景逸SUV", "菱智"
    };
    private String[] xinghaodongnan = {
            "得利卡", "菱悦", "希旺"
    };
    private String[] xinghaofengshen = {
            "A60", "H30", "H30 Cross", "S30"
    };
    private String[] xinghaoguangqi = {
            "传祺", "传祺GS5"
    };
    private String[] xinghaohafei = {
            "骏意", "路宝", "路尊大霸王", "路尊小霸王", "民意", "赛豹V系", "赛豹Ⅲ系", "赛马", "中意"
    };
    private String[] xinghaohaima = {
            "爱尚", "福美来", "福美来VS", "福仕达", "海福星", "欢动", "普力马", "骑士", "丘比特", "王子"
    };
    private String[] xinghaohongqi = {
            "盛世"
    };
    private String[] xinghaohuapu = {
            "海锋", "海尚", "海迅两厢", "海迅三厢", "海域两厢", "海域三厢"
    };
    private String[] xinghaohuizhong = {
            "伊思坦纳"
    };
    private String[] xinghaojianghuai = {
            "宾悦", "和悦", "和悦RS", "瑞风", "瑞风II", "瑞鹰", "同悦", "同悦CROSS", "同悦RS", "悦悦"
    };
    private String[] xinghaojiangling = {
            "驭胜", "全顺"
    };
    private String[] xinghaojiao = {
            "奥轩G3", "奥轩G5", "奥轩GX5", "帅豹", "帅舰", "星旺"
    };
    private String[] xinghaojinbei = {
            "大海狮", "阁瑞斯", "海狮", "S50"
    };
    private String[] xinghaokairui = {
            "优劲", "优派", "优胜", "优胜二代", "优雅", "优翼", "优优"
    };
    private String[] xinghaoliebao = {
            "CS6", "飞腾时尚", "飞腾经典", "黑金刚", "奇兵"
    };
    private String[] xinghaolifan = {
            "320", "520", "520i", "620", "X60"
    };
    private String[] xinghaolinian = {
            "S1"
    };
    private String[] xinghaolufeng = {
            "风尚", "X6", "X8", "X9"
    };
    private String[] xinghaonazhijie = {
            "大7"
    };
    private String[] xinghaoqichen = {
            "D50"
    };
    private String[] xinghaoqirui = {
            "A3两厢", "A3三厢", "东方之子", "E5", "风云2两厢", "风云2三厢", "A1", "旗云1", "旗云2", "旗云3", "旗云5",
            "QQ3", "QQme", "瑞虎", "瑞虎DR欧版"
    };
    private String[] xinghaoquanqiuying = {
            "GC7", "GX2", "GX7", "新自由舰", "熊猫", "新远景"
    };
    private String[] xinghaorongwei = {
            "350", "550", "750", "750混合动力", "950", " W5"
    };
    private String[] xinghaoruiqi = {
            "G3", "G5", "G6", "M1", "M5", "X1"
    };
    private String[] xinghaoshuanghuan = {
            "SCEO", "小贵族"
    };
    private String[] xinghaosiming = {
            "思铭"
    };
    private String[] xinghaoweilin = {
            "H3", "H5", "V5", "X5"
    };
    private String[] xinghaowuling = {
            "宏光", "荣光", "之光", "兴旺"
    };
    private String[] xinghaochuanqiyema = {
            "F99", "F12"
    };
    private String[] xinghaoyinglunqiche = {
            "金刚", "金刚2代", "金鹰Cross", "SC5-RV", "SC7", "TX4"
    };
    private String[] xinghaoyiqi = {
            "威乐", "威志三厢", "威志V2", "威志V2 CROSS", "威志V5", "威姿", "夏利A+两厢", "夏利A+三厢", "夏利N3+两厢", "夏利N3+三厢", "夏利N5"

    };
    private String[] xinghaoyiqijilin = {
            "佳宝V系", "森雅M80", "森雅S80"
    };
    private String[] xinghaoyiqitongyong = {
            "坤程"
    };
    private String[] xinghaozhonghua = {
            "H530", "骏捷", "骏捷Cross", "骏捷FRV", "骏捷FSV", "骏捷Wagon", "酷宝", "V5", "尊驰",
    };
    private String[] xinghaozhongtai = {
            "2008", "5008", "江南TT", "Z200", "Z200HB", "朗悦", "V10", "Z300"
    };
    private String[] xinghaozhongxing = {
            "昌铃皮卡", "旗舰皮卡", "威虎F1", "无限"
    };
    private String[] xinghaozhongyu = {
            "迷你巴士", "威霆Vito3", "威霆Vito5"
    };
    private String[] xinghaosabo = {
            "9-3"
    };
    private String[] xinghaowoerwo = {
            "长安沃尔沃S40", "长安沃尔沃S80L", "沃尔沃C30", "沃尔沃C70", "沃尔沃S60", "沃尔沃V60", "沃尔沃XC60", "沃尔沃XC90"
    };
    private String[] cheshenaodi = {
            "中型车", "中大型车", "SUV", "小型车", "紧凑型车", "中型车", "中型车", "中型车", "跑车", "豪华车", "SUV", "SUV", "跑车", "跑车", "跑车", "跑车", "豪华车", "跑车"
    };
    private String[] cheshenbaoma = {
            "中型车", "中大型车", "SUV", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "中型车", "跑车", "跑车", "中大型车", "中大型车",
            "中大型车", "跑车", "跑车", "豪华车", "跑车", "跑车", "跑车", "跑车", "SUV", "SUV", "SUV", "SUV", "SUV", "SUV", "跑车"
    };
    private String[] cheshenbaoshijie = {
            "跑车", "跑车", "跑车", "SUV", "豪华车"
    };
    private String[] cheshenbenchi = {
            "中型车", "中大型车", "SUV", "MPV", "MPV", "MPV", "小型车", "跑车", "紧凑型车", "跑车", "中型车", "跑车", "跑车",
            "跑车", "中大型车", "中大型车", "SUV", "SUV", "SUV", "SUV", "SUV", "SUV", "MPV", "豪华车", "豪华车", "豪华车", "跑车", "跑车", "跑车", "跑车"
    };
    private String[] cheshendazhong = {
            "小型车", "紧凑型车", "中型车", "小型车", "小型车", "小型车", "中型车", "中型车", "中型车", "MPV", "SUV", "紧凑型车", "中型车", "紧凑型车",
            "紧凑型车", "紧凑型车", "中型车", "紧凑型车", "中型车", "紧凑型车", "紧凑型车", "跑车", "紧凑型车", "紧凑型车", "豪华车", "紧凑型车", "紧凑型车", "中型车", "中型车", "中型车",
            "跑车", "紧凑型车", "MPV", "SUV", "SUV", "MPV"
    };
    private String[] cheshenmaibahe = {
            "豪华车", "豪华车"
    };
    private String[] cheshenMINI = {
            "小型车", "小型车", "SUV", "小型车", "小型车", "小型车"
    };
    private String[] cheshensikeda = {
            "小型车", "小型车", "紧凑型车", "紧凑型车", "中型车"
    };
    private String[] cheshensmart = {
            "微型车"
    };
    private String[] cheshenweiziman = {
            "跑车", "跑车"
    };
    private String[] cheshenbieke = {
            "MPV", "中型车", "中型车", "中型车", "其它", "紧凑型车", "中大型车", "紧凑型车", "紧凑型车", "SUV"
    };
    private String[] cheshenfute = {
            "紧凑型车", "紧凑型车", "小型车", "小型车", "MPV", "中型车", "SUV", "其它", "SUV", "跑车"
    };
    private String[] cheshenhanma = {
            "SUV"
    };
    private String[] cheshenJeep = {
            "SUV", "SUV", "SUV", "SUV", "SUV", "SUV"
    };
    private String[] cheshenkaidilake = {
            "中大型车", "中型车", "中型车", "跑车", "其它", "SUV", "跑车"
    };
    private String[] cheshenkelaisile = {
            "MPV", "紧凑型车"
    };
    private String[] cheshenlinken = {
            "SUV", "中大型车", "SUV", "SUV", "中大型车"
    };
    private String[] cheshenoubao = {
            "SUV", "MPV", "紧凑型车", "紧凑型车", "跑车"
    };
    private String[] cheshenxuefulan = {
            "小型车", "小型车", "中型车", "紧凑型车", "SUV", "小型车", "小型车", "中型车", "小型车", "小型车", "跑车", "SUV", "微型车", "其它"
    };
    private String[] cheshenbiaozhi = {
            "小型车", "小型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "中型车", "紧凑型车",
            "跑车", "SUV", "跑车", "紧凑型车", "中型车", "跑车", "中型车", "中大型车", "SUV", "跑车"
    };
    private String[] cheshenleinuo = {
            "MPV", "紧凑型车", "SUV", "中型车", "中型车", "紧凑型车", "跑车", "中大型车", "中型车"
    };
    private String[] cheshenxuetielong = {
            "紧凑型车", "紧凑型车", "小型车", "中型车", "中型车", "MPV", "紧凑型车", "紧凑型车", "紧凑型车", "SUV", "中大型车", "MPV"
    };
    private String[] cheshenbujiadi = {
            "跑车"
    };
    private String[] cheshenfalali = {
            "跑车", "跑车", "跑车", "跑车", "跑车"
    };
    private String[] cheshenfeiyate = {
            "小型车", "小型车", "紧凑型车", "SUV", "紧凑型车", "小型车"
    };
    private String[] cheshenlanbojini = {
            "跑车", "跑车", "跑车"
    };
    private String[] cheshenmashaladi = {
            "跑车", "跑车", "豪华车"
    };
    private String[] cheshenbentian = {
            "MPV", "小型车", "紧凑型车", "SUV", "中型车", "SUV", "中型车", "紧凑型车", "其它"
    };
    private String[] cheshenfengtian = {
            "紧凑型车", "中大型车", "紧凑型车", "SUV", "其它", "SUV", "中型车", "小型车", "SUV", "中型车", "其它",
            "小型车", "MPV",
            "MPV", "SUV", "MPV", "SUV", "MPV", "紧凑型车"
    };
    private String[] cheshenleikesasi = {
            "紧凑型车", "中型车", "中大型车", "其它", "SUV", "中型车", "跑车", "跑车", "豪华车", "其它", "SUV", "SUV", "其它", "跑车"
    };
    private String[] cheshenlingmu = {
            "微型车", "小型车", "紧凑型车", "紧凑型车", "小型车",
            "微型车", "微型车", "其它", "紧凑型车", "小型车", "小型车",
            "SUV", "SUV", "中型车"
    };
    private String[] cheshenmazida = {
            "MPV", "中型车", "中型车", "中型车",
            "小型车", "小型车", "紧凑型车", "紧凑型车", "紧凑型车",
            "SUV", "MPV", "跑车"
    };
    private String[] cheshenouge = {
            "SUV", "中大型车", "中型车", "中大型车"
    };
    private String[] cheshenrichan = {
            "SUV", "小型车", "SUV", "中型车", "SUV", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车",
            "SUV", "其它", "其它", "MPV", "SUV", "其它", "其它", "其它", "MPV", "SUV", "MPV", "其它",
            "跑车", "跑车", "MPV"
    };
    private String[] cheshensanling = {
            "中型车", "SUV", "紧凑型车", "紧凑型车", "紧凑型车", "SUV",
            "MPV", "SUV", "SUV", "跑车", "SUV", "SUV", "SUV", "跑车"
    };
    private String[] cheshensibalu = {
            "中型车", "SUV", "中型车", "中型车", "SUV", "SUV", "紧凑型车", "紧凑型车"
    };
    private String[] cheshenyingfeinidi = {
            "中型车", "SUV", "中型车", "跑车", "跑车", "中大型车", "SUV"
    };
    private String[] cheshenqiya = {
            "紧凑型车", "MPV", "中型车", "小型车", "小型车", "小型车", "紧凑型车", "紧凑型车", "SUV", "紧凑型车", "SUV",
            "SUV", "MPV", "中大型车", "中大型车", "跑车", "SUV", "MPV"
    };
    private String[] cheshenshuanglong = {
            "SUV", "SUV", "MPV", "SUV", "SUV", "中大型车"
    };
    private String[] cheshenxiandai = {
            "紧凑型车", "SUV", "中型车", "小型车", "中型车", "SUV", "小型车", "小型车", "紧凑型车", "紧凑型车",
            "跑车", "中大型车", "跑车", "SUV", "SUV", "豪华车", "中型车"
    };
    private String[] cheshenasidunmading = {
            "跑车", "跑车", "跑车", "跑车", "跑车", "跑车", "跑车"
    };
    private String[] cheshenbinli = {
            "豪华车", "跑车", "豪华车"
    };
    private String[] cheshenjiebao = {
            "中大型车", "豪华车", "跑车"
    };
    private String[] cheshenlaosilaisi = {
            "豪华车", "豪华车"
    };
    private String[] cheshenluhu = {
            "SUV", "SUV", "SUV", "SUV", "SUV", "SUV"
    };
    private String[] cheshenlianhua = {
            "跑车", "跑车", "跑车",
    };
    private String[] cheshenMG = {
            "小型车", "紧凑型车", "中型车", "紧凑型车", "紧凑型车"
    };
    private String[] cheshenbiyadi = {
            "其它", "SUV", "微型车", "紧凑型车", "其它", "紧凑型车", "中型车", "紧凑型车", "紧凑型车", "中型车", "紧凑型车", "MPV", "跑车"
    };
    private String[] cheshenchangan = {
            "微型车", "微型车", "微型车", "紧凑型车", "其它", "小型车", "紧凑型车", "MPV", "其它", "小型车", "小型车", "紧凑型车"
    };
    private String[] cheshenchanganshangyong = {
            "其它", "其它", "MPV"
    };
    private String[] cheshenchangcheng = {
            "紧凑型车", "紧凑型车", "其它", "SUV", "SUV", "SUV", "SUV", "SUV", "SUV", "其它", "微型车", "微型车", "小型车", "小型车", "其它", "其它", "紧凑型车", "MPV", "小型车", "小型车"
    };
    private String[] cheshenchangfeng = {
            "SUV", "其它", "MPV"
    };
    private String[] cheshendihao = {
            "紧凑型车", "紧凑型车", "中型车"
    };
    private String[] cheshendongfengfengxing = {
            "紧凑型车", "紧凑型车", "SUV", "MPV"
    };
    private String[] cheshendongnan = {
            "其它", "紧凑型车", "其它"
    };
    private String[] cheshenfengshen = {
            "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车"
    };
    private String[] cheshenguangqi = {
            "中型车", "SUV"
    };
    private String[] cheshenhafei = {
            "其它", "微型车", "其它", "其它", "其它", "紧凑型车", "紧凑型车", "小型车", "其它"
    };
    private String[] cheshenhaima = {
            "微型车", "紧凑型车", "紧凑型车", "微型车", "紧凑型车", "紧凑型车", "MPV", "SUV", "小型车", "微型车"
    };
    private String[] cheshenhongqi = {
            "中大型车"
    };
    private String[] cheshenhuapu = {
            "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车"
    };
    private String[] cheshenhuizhong = {
            "其它"
    };
    private String[] cheshenjianghuai = {
            "中型车", "紧凑型车", "MPV", "MPV", "MPV", "SUV", "小型车", "小型车", "小型车", "微型车"
    };
    private String[] cheshenjiangling = {
            "SUV", "其它"
    };
    private String[] cheshenjiao = {
            "SUV", "SUV", "中型车", "SUV", "SUV", "其它"
    };
    private String[] cheshenjinbei = {
            "其它", "MPV", "其它", "SUV"
    };
    private String[] cheshenkairui = {
            "其它", "其它", "其它", "微型车", "其它", "其它", "其它"
    };
    private String[] cheshenliebao = {
            "SUV", "SUV", "SUV", "SUV", "SUV"
    };
    private String[] cheshenlifan = {
            "微型车", "紧凑型车", "紧凑型车", "紧凑型车", "SUV"
    };
    private String[] cheshenlinian = {
            "小型车"
    };
    private String[] cheshenlufeng = {
            "MPV", "SUV", "SUV", "SUV"
    };
    private String[] cheshennazhijie = {
            "SUV"
    };
    private String[] cheshenqichen = {
            "紧凑型车"
    };
    private String[] cheshenqirui = {
            "紧凑型车", "紧凑型车", "中型车", "紧凑型车", "小型车", "小型车", "小型车", "微型车", "小型车", "紧凑型车", "中型车", "微型车", "微型车", "SUV", "SUV"
    };
    private String[] cheshenquanqiuying = {
            "紧凑型车", "微型车", "SUV", "小型车", "微型车", "紧凑型车"
    };
    private String[] cheshenrongwei = {
            "紧凑型车", "紧凑型车", "中型车", "其它", "中型车", "SUV"
    };
    private String[] cheshenruiqi = {
            "紧凑型车", "中型车", "中大型车", "微型车", "微型车", "微型车"
    };
    private String[] cheshenshuanghuan = {
            "SUV", "微型车"
    };
    private String[] cheshensiming = {
            "紧凑型车"
    };
    private String[] cheshenweilin = {
            "MPV", "MPV", "MPV", "SUV"
    };
    private String[] cheshenwuling = {
            "MPV", "其它", "其它", "其它"
    };
    private String[] cheshenchuanqiyema = {
            "SUV", "SUV"
    };
    private String[] cheshenyinglunqiche = {
            "小型车", "小型车", "小型车", "小型车", "紧凑型车", "中型车"
    };
    private String[] cheshenyiqi = {
            "小型车", "小型车", "小型车", "小型车", "小型车", "小型车", "小型车", "小型车", "小型车", "小型车", "小型车",
    };
    private String[] cheshenyiqijilin = {
            "其它", "MPV", "SUV"
    };
    private String[] cheshenyiqitongyong = {
            "其它"
    };
    private String[] cheshenzhonghua = {
            "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "紧凑型车", "跑车", "SUV", "中型车"
    };
    private String[] cheshenzhongtai = {
            "SUV", "SUV", "微型车", "小型车", "小型车", "紧凑型车", "微型车", "紧凑型车"
    };
    private String[] cheshenzhongxing = {
            "其它", "其它", "其它", "SUV"
    };
    private String[] cheshenzhongyu = {
            "其它", "MPV", "MPV"
    };
    private String[] cheshensabo = {
            "中型车"
    };
    private String[] cheshenwoerwo = {
            "紧凑型车", "中型车", "中型车", "中型车", "SUV"
    };
    private String[][] xinghaolist = new String[][]{
            xinghaoaodi, xinghaobaoma, xinghaobaoshijie, xinghaobenchi,
            xinghaodazhong, xinghaomaibahe, xinghaoMINI, xinghaosikeda, xinghaosmart, xinghaoweiziman, xinghaobieke,
            xinghaofute, xinghaohanma, xinghaoJeep, xinghaokaidilake, xinghaokelaisile, xinghaolinken, xinghaooubao,
            xinghaoxuefulan, xinghaobiaozhi, xinghaoleinuo, xinghaoxuetielong, xinghaobujiadi, xinghaofalali, xinghaofeiyate,
            xinghaolanbojini, xinghaomashaladi, xinghaobentian, xinghaofengtian, xinghaoleikesasi, xinghaolingmu, xinghaomazida,
            xinghaoouge, xinghaorichan, xinghaosanling, xinghaosibalu, xinghaoyingfeinidi, xinghaoqiya, xinghaoshuanglong,
            xinghaoxiandai, xinghaoasidunmading, xinghaobinli, xinghaojiebao, xinghaolaosilaisi, xinghaoluhu, xinghaolianhua,
            xinghaoMG, xinghaobiyadi, xinghaochangan, xinghaochanganshangyong, xinghaochangcheng, xinghaochangfeng, xinghaodihao,
            xinghaodongfengfengxing, xinghaodongnan, xinghaofengshen, xinghaoguangqi, xinghaohafei, xinghaohaima, xinghaohongqi,
            xinghaohuapu, xinghaohuizhong, xinghaojianghuai, xinghaojiangling, xinghaojiao, xinghaojinbei, xinghaokairui, xinghaoliebao,
            xinghaolifan, xinghaolinian, xinghaolufeng, xinghaonazhijie, xinghaoqichen, xinghaoqirui, xinghaoquanqiuying, xinghaorongwei,
            xinghaoruiqi, xinghaoshuanghuan, xinghaosiming, xinghaoweilin, xinghaowuling, xinghaochuanqiyema, xinghaoyinglunqiche,
            xinghaoyiqi, xinghaoyiqijilin, xinghaoyiqitongyong, xinghaozhonghua, xinghaozhongtai, xinghaozhongxing, xinghaozhongyu,
            xinghaosabo, xinghaowoerwo
    };
    private String[][] cheshenlist = new String[][]{
            cheshenaodi, cheshenbaoma, cheshenbaoshijie, cheshenbenchi,
            cheshendazhong, cheshenmaibahe, cheshenMINI, cheshensikeda, cheshensmart, cheshenweiziman, cheshenbieke,
            cheshenfute, cheshenhanma, cheshenJeep, cheshenkaidilake, cheshenkelaisile, cheshenlinken, cheshenoubao,
            cheshenxuefulan, cheshenbiaozhi, cheshenleinuo, cheshenxuetielong, cheshenbujiadi, cheshenfalali, cheshenfeiyate,
            cheshenlanbojini, cheshenmashaladi, cheshenbentian, cheshenfengtian, cheshenleikesasi, cheshenlingmu, cheshenmazida,
            cheshenouge, cheshenrichan, cheshensanling, cheshensibalu, cheshenyingfeinidi, cheshenqiya, cheshenshuanglong,
            cheshenxiandai, cheshenasidunmading, cheshenbinli, cheshenjiebao, cheshenlaosilaisi, cheshenluhu, cheshenlianhua,
            cheshenMG, cheshenbiyadi, cheshenchangan, cheshenchanganshangyong, cheshenchangcheng, cheshenchangfeng, cheshendihao,
            cheshendongfengfengxing, cheshendongnan, cheshenfengshen, cheshenguangqi, cheshenhafei, cheshenhaima, cheshenhongqi,
            cheshenhuapu, cheshenhuizhong, cheshenjianghuai, cheshenjiangling, cheshenjiao, cheshenjinbei, cheshenkairui, cheshenliebao,
            cheshenlifan, cheshenlinian, cheshenlufeng, cheshennazhijie, cheshenqichen, cheshenqirui, cheshenquanqiuying, cheshenrongwei,
            cheshenruiqi, cheshenshuanghuan, cheshensiming, cheshenweilin, cheshenwuling, cheshenchuanqiyema, cheshenyinglunqiche,
            cheshenyiqi, cheshenyiqijilin, cheshenyiqitongyong, cheshenzhonghua, cheshenzhongtai, cheshenzhongxing, cheshenzhongyu,
            cheshensabo, cheshenwoerwo
    };
    private int sign[] = new int[]{
            R.drawable.aodi, R.drawable.baoma, R.drawable.baoshijie, R.drawable.benchi,
            R.drawable.dazhong, R.drawable.maibahe, R.drawable.mini, R.drawable.sikeda, R.drawable.smart, R.drawable.weiziman, R.drawable.bieke,
            R.drawable.fute, R.drawable.hanma, R.drawable.jeep, R.drawable.kaidilake, R.drawable.kelaisile, R.drawable.linken, R.drawable.oubao,
            R.drawable.xuefulan, R.drawable.biaozhi, R.drawable.leinuo, R.drawable.xuetielong, R.drawable.bujiadi, R.drawable.falali, R.drawable.feiyate,
            R.drawable.lanbojini, R.drawable.mashaladi, R.drawable.bentian, R.drawable.fengtian, R.drawable.leikesasi, R.drawable.lingmu, R.drawable.mazida,
            R.drawable.ouge, R.drawable.richan, R.drawable.sanling, R.drawable.sibalu, R.drawable.yingfeinidi, R.drawable.qiya, R.drawable.shuanglong,
            R.drawable.xiandai, R.drawable.asidunmading, R.drawable.binli, R.drawable.jiebao, R.drawable.laosilaisi, R.drawable.luhu, R.drawable.lianhua,
            R.drawable.mg, R.drawable.biyadi, R.drawable.changan, R.drawable.changanshangyong, R.drawable.changcheng, R.drawable.changfeng, R.drawable.dihao,
            R.drawable.dongfengfengxing, R.drawable.dongnan, R.drawable.fengshen, R.drawable.guangqi, R.drawable.hafei, R.drawable.haima, R.drawable.hongqi,
            R.drawable.huapu, R.drawable.huizhong, R.drawable.jianghuai, R.drawable.jiangling, R.drawable.jiao, R.drawable.jinbei, R.drawable.kairui, R.drawable.liebao,
            R.drawable.lifan, R.drawable.linian, R.drawable.lufeng, R.drawable.nazhijie, R.drawable.qichen, R.drawable.qirui, R.drawable.quanqiuying, R.drawable.rongwei,
            R.drawable.ruilin, R.drawable.shuanghuan, R.drawable.siming, R.drawable.weilin, R.drawable.wuling, R.drawable.chuanqiyema, R.drawable.yinglunqiche,
            R.drawable.yiqi, R.drawable.yiqijilin, R.drawable.yiqitongyong, R.drawable.zhonghua, R.drawable.zhongtai, R.drawable.zhongxing, R.drawable.zhongyu,
            R.drawable.sabo, R.drawable.woerwo
    };
    //endregion
    private List<View> caruser_pageview;
    private List<View> viewlist1;
    private ViewPager caruser_viewpager;
    private Spinner[] caruser_brand, caruser_type, caruser_bodylevel;
    private ImageView[] caruser_sign;
    private EditText[] caruser_plate, caruser_engine, caruser_mileage, caruser_gasoline;
    private TextView[] caruser_submit;
    private CheckBox[] caruser_engineper, caruser_transmission, light_choice_switch;

    private String[][] thecarinfo = new String[3][10];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.carinfo_layout);

        viewlist1 = new ArrayList<View>();
        viewlist1.add(0, LayoutInflater.from(CarInfoActivity.this).inflate(R.layout.carinfoinclude0, null));
        viewlist1.add(1, LayoutInflater.from(CarInfoActivity.this).inflate(R.layout.carinfoinclude1, null));
        viewlist1.add(2, LayoutInflater.from(CarInfoActivity.this).inflate(R.layout.carinfoinclude2, null));

        Spinner spinner = (Spinner) findViewById(R.id.carinfo_number);
        String[] carnumber = {"1", "2", "3"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, carnumber);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    private int brandnum = 0;//记录当前车辆型号

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //加载viewpager布局
        caruser_pageview = new ArrayList<View>();
        for (int i = 0; i <= position; i++) {
            View pagerview = viewlist1.get(position);
            caruser_pageview.add(i, pagerview);
        }

        caruser_viewpager = (ViewPager) findViewById(R.id.caruser_viewpager);
        Mypageradapter mypageradapter = new Mypageradapter();
        caruser_viewpager.setAdapter(mypageradapter);
        caruser_viewpager.setOnPageChangeListener(this);
        caruser_viewpager.setOffscreenPageLimit(3);//设置当前缓存页数

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    //region onPageChangeListener
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        caruser_brand = new Spinner[caruser_pageview.size()];
        caruser_sign = new ImageView[caruser_pageview.size()];
        caruser_type = new Spinner[caruser_pageview.size()];
        caruser_bodylevel = new Spinner[caruser_pageview.size()];
        caruser_plate = new EditText[caruser_pageview.size()];
        caruser_engine = new EditText[caruser_pageview.size()];
        caruser_mileage = new EditText[caruser_pageview.size()];
        caruser_gasoline = new EditText[caruser_pageview.size()];
        caruser_submit = new TextView[caruser_pageview.size()];
        caruser_engineper = new CheckBox[caruser_pageview.size()];
        caruser_transmission = new CheckBox[caruser_pageview.size()];
        light_choice_switch = new CheckBox[caruser_pageview.size()];


        switch (i) {
            case 0:
                caruser_sign[0] = (ImageView) findViewById(R.id.caruser_sign);
                caruser_type[0] = (Spinner) findViewById(R.id.caruser_type);
                caruser_bodylevel[0] = (Spinner) findViewById(R.id.caruser_bodylevel);
                caruser_brand[0] = (Spinner) findViewById(R.id.caruser_brand);
                caruser_plate[0] = (EditText) findViewById(R.id.caruser_plate);
                caruser_engine[0] = (EditText) findViewById(R.id.caruser_engine);
                caruser_mileage[0] = (EditText) findViewById(R.id.caruser_mileage);
                caruser_gasoline[0] = (EditText) findViewById(R.id.caruser_gasoline);
                caruser_submit[0] = (TextView) findViewById(R.id.caruser_submit);
                caruser_engineper[0] = (CheckBox) findViewById(R.id.caruser_engineper);
                caruser_transmission[0] = (CheckBox) findViewById(R.id.caruser_transmission);
                light_choice_switch[0] = (CheckBox) findViewById(R.id.light_choice_switch);
                caruser_engineper[0].setOnCheckedChangeListener(new Mycheckchanginpage());
                caruser_transmission[0].setOnCheckedChangeListener(new Mycheckchanginpage());
                light_choice_switch[0].setOnCheckedChangeListener(new Mycheckchanginpage());
                ArrayAdapter arrayAdapterbrand = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, pinpai);
                caruser_brand[0].setAdapter(arrayAdapterbrand);
                caruser_brand[0].setOnItemSelectedListener(new MySelect());
                caruser_type[0].setOnItemSelectedListener(new MySelect());
                caruser_bodylevel[0].setOnItemSelectedListener(new MySelect());
                caruser_submit[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thecarinfo[0][3] = caruser_plate[0].getText().toString();
                        thecarinfo[0][4] = caruser_engine[0].getText().toString();
                        thecarinfo[0][5] = caruser_mileage[0].getText().toString();
                        thecarinfo[0][6] = caruser_gasoline[0].getText().toString();
                        if(thecarinfo[2][3].equals("")||thecarinfo[2][4].equals("")||
                                thecarinfo[2][5].equals("")||thecarinfo[2][6].equals("")){
                            Toast.makeText(CarInfoActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(CarInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 1:
                caruser_sign[1] = (ImageView) findViewById(R.id.caruser_sign1);
                caruser_type[1] = (Spinner) findViewById(R.id.caruser_type1);
                caruser_bodylevel[1] = (Spinner) findViewById(R.id.caruser_bodylevel1);
                caruser_brand[1] = (Spinner) findViewById(R.id.caruser_brand1);
                caruser_plate[1] = (EditText) findViewById(R.id.caruser_plate1);
                caruser_engine[1] = (EditText) findViewById(R.id.caruser_engine1);
                caruser_mileage[1] = (EditText) findViewById(R.id.caruser_mileage1);
                caruser_gasoline[1] = (EditText) findViewById(R.id.caruser_gasoline1);
                caruser_submit[1] = (TextView) findViewById(R.id.caruser_submit1);
                caruser_engineper[1] = (CheckBox) findViewById(R.id.caruser_engineper1);
                caruser_transmission[1] = (CheckBox) findViewById(R.id.caruser_transmission1);
                light_choice_switch[1] = (CheckBox) findViewById(R.id.light_choice_switch1);
                caruser_engineper[1].setOnCheckedChangeListener(new Mycheckchanginpage());
                caruser_transmission[1].setOnCheckedChangeListener(new Mycheckchanginpage());
                light_choice_switch[1].setOnCheckedChangeListener(new Mycheckchanginpage());
                ArrayAdapter arrayAdapterbrand11 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, pinpai);
                caruser_brand[1].setAdapter(arrayAdapterbrand11);
                caruser_brand[1].setOnItemSelectedListener(new MySelect());
                caruser_type[1].setOnItemSelectedListener(new MySelect());
                caruser_bodylevel[1].setOnItemSelectedListener(new MySelect());
                caruser_submit[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thecarinfo[1][3] = caruser_plate[1].getText().toString();
                        thecarinfo[1][4] = caruser_engine[1].getText().toString();
                        thecarinfo[1][5] = caruser_mileage[1].getText().toString();
                        thecarinfo[1][6] = caruser_gasoline[1].getText().toString();
                        if(thecarinfo[2][3].equals("")||thecarinfo[2][4].equals("")||
                                thecarinfo[2][5].equals("")||thecarinfo[2][6].equals("")){
                            Toast.makeText(CarInfoActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(CarInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                caruser_sign[2] = (ImageView) findViewById(R.id.caruser_sign2);
                caruser_type[2] = (Spinner) findViewById(R.id.caruser_type2);
                caruser_bodylevel[2] = (Spinner) findViewById(R.id.caruser_bodylevel2);
                caruser_brand[2] = (Spinner) findViewById(R.id.caruser_brand2);
                caruser_plate[2] = (EditText) findViewById(R.id.caruser_plate2);
                caruser_engine[2] = (EditText) findViewById(R.id.caruser_engine2);
                caruser_mileage[2] = (EditText) findViewById(R.id.caruser_mileage2);
                caruser_gasoline[2] = (EditText) findViewById(R.id.caruser_gasoline2);
                caruser_submit[2] = (TextView) findViewById(R.id.caruser_submit2);
                caruser_engineper[2] = (CheckBox) findViewById(R.id.caruser_engineper2);
                caruser_transmission[2] = (CheckBox) findViewById(R.id.caruser_transmission2);
                light_choice_switch[2] = (CheckBox) findViewById(R.id.light_choice_switch2);
                caruser_engineper[2].setOnCheckedChangeListener(new Mycheckchanginpage());
                caruser_transmission[2].setOnCheckedChangeListener(new Mycheckchanginpage());
                light_choice_switch[2].setOnCheckedChangeListener(new Mycheckchanginpage());
                ArrayAdapter arrayAdapterbrand22 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, pinpai);
                caruser_brand[2].setAdapter(arrayAdapterbrand22);
                caruser_brand[2].setOnItemSelectedListener(new MySelect());
                caruser_type[2].setOnItemSelectedListener(new MySelect());
                caruser_bodylevel[2].setOnItemSelectedListener(new MySelect());
                caruser_submit[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thecarinfo[2][3] = caruser_plate[2].getText().toString();
                        thecarinfo[2][4] = caruser_engine[2].getText().toString();
                        thecarinfo[2][5] = caruser_mileage[2].getText().toString();
                        thecarinfo[2][6] = caruser_gasoline[2].getText().toString();
                        if(thecarinfo[2][3].equals("")||thecarinfo[2][4].equals("")||
                                thecarinfo[2][5].equals("")||thecarinfo[2][6].equals("")){
                            Toast.makeText(CarInfoActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(CarInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //endregion

    private class Mypageradapter extends PagerAdapter implements AdapterView.OnItemSelectedListener {
        ImageView caruser_sign0;
        Spinner caruser_type0;
        Spinner caruser_bodylevel0;
        Spinner caruser_brand0;
        EditText caruser_plate0;
        EditText caruser_engine0;
        EditText caruser_mileage0;
        EditText caruser_gasoline0;
        TextView caruser_submit0;
        CheckBox caruser_engineper0;
        CheckBox caruser_transmission0;
        CheckBox light_choice_switch0;

        @Override
        public int getCount() {
            return caruser_pageview.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + (position + 1) + "辆车";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //必须使用layoutinflater创建页面 否则不会消除页面
            int[] layoutinflaternumber = {R.layout.carinfoinclude0, R.layout.carinfoinclude1, R.layout.carinfoinclude2};
            View pageview = LayoutInflater.from(CarInfoActivity.this).inflate(layoutinflaternumber[position], null);
            ((ViewPager) container).addView(pageview, position);
            caruser_sign0 = (ImageView) findViewById(R.id.caruser_sign);
            caruser_type0 = (Spinner) findViewById(R.id.caruser_type);
            caruser_bodylevel0 = (Spinner) findViewById(R.id.caruser_bodylevel);
            caruser_brand0 = (Spinner) findViewById(R.id.caruser_brand);
            caruser_plate0 = (EditText) findViewById(R.id.caruser_plate);
            caruser_engine0 = (EditText) findViewById(R.id.caruser_engine);
            caruser_mileage0 = (EditText) findViewById(R.id.caruser_mileage);
            caruser_gasoline0 = (EditText) findViewById(R.id.caruser_gasoline);
            caruser_submit0 = (TextView) findViewById(R.id.caruser_submit);
            caruser_engineper0 = (CheckBox) findViewById(R.id.caruser_engineper);
            caruser_transmission0 = (CheckBox) findViewById(R.id.caruser_transmission);
            light_choice_switch0 = (CheckBox) findViewById(R.id.light_choice_switch);




            ArrayAdapter arrayAdapterbrand = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, pinpai);
            caruser_brand0.setAdapter(arrayAdapterbrand);
            caruser_brand0.setOnItemSelectedListener(this);
            caruser_type0.setOnItemSelectedListener(this);
            caruser_bodylevel0.setOnItemSelectedListener(this);
            caruser_engineper0.setOnCheckedChangeListener(new Mycheckchang());
            caruser_transmission0.setOnCheckedChangeListener(new Mycheckchang());
            light_choice_switch0.setOnCheckedChangeListener(new Mycheckchang());
            caruser_submit0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thecarinfo[0][3] = caruser_plate0.getText().toString();
                    thecarinfo[0][4] = caruser_engine0.getText().toString();
                    thecarinfo[0][5] = caruser_mileage0.getText().toString();
                    thecarinfo[0][6] = caruser_gasoline0.getText().toString();
                    if(thecarinfo[0][3].equals("")||thecarinfo[0][4].equals("")||
                            thecarinfo[0][5].equals("")||thecarinfo[0][6].equals("")){
                        Toast.makeText(CarInfoActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(CarInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }




                }
            });

            return pageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (caruser_pageview.size() > 2) {
                ((ViewPager) container).removeView(caruser_pageview.get(position));
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.caruser_brand:
                    ArrayAdapter arrayAdaptertype = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, xinghaolist[position]);
                    caruser_type0.setAdapter(arrayAdaptertype);
                    caruser_sign0.setImageResource(sign[position]);
                    TextView brandtext = (TextView) view;
                    thecarinfo[0][0] = brandtext.getText().toString();
                    Log.e("thenamr", "shenfasfa" + thecarinfo[0][0]);
                    break;
                case R.id.caruser_type:
                    brandnum = position;
                    ArrayAdapter typearrayAdapterbodylevel = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, cheshenlist[brandnum]);
                    caruser_bodylevel0.setAdapter(typearrayAdapterbodylevel);
                    TextView typetext = (TextView) view;
                    thecarinfo[0][0] = typetext.getText().toString();
                    break;
                case R.id.caruser_bodylevel:
                    TextView bodyleveltext = (TextView) view;
                    thecarinfo[0][0] = bodyleveltext.getText().toString();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        private class Mycheckchang implements CompoundButton.OnCheckedChangeListener {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.caruser_engineper:
                        if (isChecked) {
                            thecarinfo[0][7] = "正常";
                        } else {
                            thecarinfo[0][7] = "异常";
                        }
                        break;
                    case R.id.caruser_transmission:
                        if (isChecked) {
                            thecarinfo[0][8] = "正常";
                        } else {
                            thecarinfo[0][8] = "异常";
                        }
                        break;
                    case R.id.light_choice_switch:
                        if (isChecked) {
                            thecarinfo[0][9] = "正常";
                        } else {
                            thecarinfo[0][9] = "异常";
                        }
                        break;
                }
            }
        }
    }

    private class MySelect implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.caruser_brand:

                    ArrayAdapter arrayAdaptertype = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, xinghaolist[position]);
                    caruser_type[0].setAdapter(arrayAdaptertype);
                    caruser_sign[0].setImageResource(sign[position]);
                    TextView brandtext = (TextView) view;
                    thecarinfo[0][0] = brandtext.getText().toString();

                    break;
                case R.id.caruser_type:
                    brandnum = position;
                    ArrayAdapter typearrayAdapterbodylevel = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, cheshenlist[brandnum]);
                    caruser_bodylevel[0].setAdapter(typearrayAdapterbodylevel);
                    TextView typetext = (TextView) view;
                    thecarinfo[0][0] = typetext.getText().toString();
                    break;
                case R.id.caruser_bodylevel:
                    TextView bodyleveltext = (TextView) view;
                    thecarinfo[0][0] = bodyleveltext.getText().toString();
                    break;


                case R.id.caruser_brand1:
                    ArrayAdapter arrayAdaptertype1 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, xinghaolist[position]);
                    caruser_type[1].setAdapter(arrayAdaptertype1);
                    caruser_sign[1].setImageResource(sign[position]);
                    TextView brandtext1 = (TextView) view;
                    thecarinfo[1][0] = brandtext1.getText().toString();
                    break;
                case R.id.caruser_type1:
                    brandnum = position;
                    ArrayAdapter typearrayAdapterbodylevel1 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, cheshenlist[brandnum]);
                    caruser_bodylevel[1].setAdapter(typearrayAdapterbodylevel1);
                    TextView typetext1 = (TextView) view;
                    thecarinfo[1][0] = typetext1.getText().toString();
                    break;
                case R.id.caruser_bodylevel1:
                    TextView bodyleveltext1 = (TextView) view;
                    thecarinfo[1][0] = bodyleveltext1.getText().toString();
                    break;


                case R.id.caruser_brand2:
                    ArrayAdapter arrayAdaptertype2 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, xinghaolist[position]);
                    caruser_type[2].setAdapter(arrayAdaptertype2);
                    caruser_sign[2].setImageResource(sign[position]);
                    TextView brandtext2 = (TextView) view;
                    thecarinfo[2][0] = brandtext2.getText().toString();
                    break;
                case R.id.caruser_type2:
                    brandnum = position;
                    ArrayAdapter typearrayAdapterbodylevel2 = new ArrayAdapter(CarInfoActivity.this, android.R.layout.simple_spinner_item, cheshenlist[brandnum]);
                    caruser_bodylevel[2].setAdapter(typearrayAdapterbodylevel2);
                    TextView typetext2 = (TextView) view;
                    thecarinfo[2][0] = typetext2.getText().toString();
                    break;
                case R.id.caruser_bodylevel2:
                    TextView bodyleveltext2 = (TextView) view;
                    thecarinfo[2][0] = bodyleveltext2.getText().toString();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class Mycheckchanginpage implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.caruser_engineper:
                    if (isChecked) {
                        thecarinfo[0][7] = "正常";
                    } else {
                        thecarinfo[0][7] = "异常";
                    }
                    break;
                case R.id.caruser_transmission:
                    if (isChecked) {
                        thecarinfo[0][8] = "正常";
                    } else {
                        thecarinfo[0][8] = "异常";
                    }
                    break;
                case R.id.light_choice_switch:
                    if (isChecked) {
                        thecarinfo[0][9] = "正常";
                    } else {
                        thecarinfo[0][9] = "异常";
                    }
                    break;
                case R.id.caruser_engineper1:
                    if (isChecked) {
                        thecarinfo[1][7] = "正常";
                    } else {
                        thecarinfo[1][7] = "异常";
                    }
                    break;
                case R.id.caruser_transmission1:
                    if (isChecked) {
                        thecarinfo[1][8] = "正常";
                    } else {
                        thecarinfo[1][8] = "异常";
                    }
                    break;
                case R.id.light_choice_switch1:
                    if (isChecked) {
                        thecarinfo[1][9] = "正常";
                    } else {
                        thecarinfo[1][9] = "异常";
                    }
                    break;
                case R.id.caruser_engineper2:
                    if (isChecked) {
                        thecarinfo[2][7] = "正常";
                    } else {
                        thecarinfo[2][7] = "异常";
                    }
                    break;
                case R.id.caruser_transmission2:
                    if (isChecked) {
                        thecarinfo[2][8] = "正常";
                    } else {
                        thecarinfo[2][8] = "异常";
                    }
                    break;
                case R.id.light_choice_switch2:
                    if (isChecked) {
                        thecarinfo[2][9] = "正常";
                    } else {
                        thecarinfo[2][9] = "异常";
                    }
                    break;
            }
        }
    }

    @Override
    public void finish() {

        super.finish();
    }
}
