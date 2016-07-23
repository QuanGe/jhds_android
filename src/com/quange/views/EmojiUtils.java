package com.quange.views;

public class EmojiUtils {
	
	public static String convertTag(String str){
		return str
		//.replaceAll("<","&lt;")
		.replaceAll("\\[爱你\\]","<img src=\"d_aini\"/>")
        .replaceAll("\\[奥特曼\\]","<img src=\"d_aoteman\"/>")
        .replaceAll("\\[拜拜\\]","<img src=\"d_baibai\"/>")
        .replaceAll("\\[悲伤\\]","<img src=\"d_beishang\"/>")
        .replaceAll("\\[鄙视\\]","<img src=\"d_bishi\"/>")
        .replaceAll("\\[闭嘴\\]","<img src=\"d_bizui\"/>")
        .replaceAll("\\[馋嘴\\]","<img src=\"d_chanzui\"/>")
        .replaceAll("\\[吃惊\\]","<img src=\"d_chijing\"/>")
        .replaceAll("\\[哈欠\\]","<img src=\"d_dahaqi\"/>")
        .replaceAll("\\[打脸\\]","<img src=\"d_dalian\"/>")
        .replaceAll("\\[顶\\]","<img src=\"d_ding\"/>")
        .replaceAll("\\[doge\\]","<img src=\"d_doge\"/>")
        .replaceAll("\\[肥皂\\]","<img src=\"d_feizao\"/>")
        .replaceAll("\\[感冒\\]","<img src=\"d_ganmao\"/>")
        .replaceAll("\\[鼓掌\\]","<img src=\"d_guzhang\"/>")
        .replaceAll("\\[哈哈\\]","<img src=\"d_haha\"/>")
        .replaceAll("\\[害羞\\]","<img src=\"d_haixiu\"/>")
        .replaceAll("\\[汗\\]","<img src=\"d_han\"/>")
        .replaceAll("\\[呵呵\\]","<img src=\"d_hehe\"/>")
        .replaceAll("\\[微笑\\]","<img src=\"d_hehe\"/>")
        .replaceAll("\\[黑线\\]","<img src=\"d_heixian\"/>")
        .replaceAll("\\[哼\\]","<img src=\"d_heng\"/>")
        .replaceAll("\\[花心\\]","<img src=\"d_huaxin\"/>")
        .replaceAll("\\[挤眼\\]","<img src=\"d_jiyan\"/>")
        .replaceAll("\\[可爱\\]","<img src=\"d_keai\"/>")
        .replaceAll("\\[可怜\\]","<img src=\"d_kelian\"/>")
        .replaceAll("\\[酷\\]","<img src=\"d_ku\"/>")
        .replaceAll("\\[困\\]","<img src=\"d_kun\"/>")
        .replaceAll("\\[懒得理你\\]","<img src=\"d_landelini\"/>")
        .replaceAll("\\[浪\\]","<img src=\"d_lang\"/>")
        .replaceAll("\\[泪\\]","<img src=\"d_lei\"/>")
        .replaceAll("\\[喵喵\\]","<img src=\"d_miao\"/>")
        .replaceAll("\\[男孩儿\\]","<img src=\"d_nanhaier\"/>")
        .replaceAll("\\[怒\\]","<img src=\"d_nu\"/>")
        .replaceAll("\\[愤怒\\]","<img src=\"d_nu\"/>")
        .replaceAll("\\[怒骂\\]","<img src=\"d_numa\"/>")
        .replaceAll("\\[女孩儿\\]","<img src=\"d_nvhaier\"/>")
        .replaceAll("\\[钱\\]","<img src=\"d_qian\"/>")
        .replaceAll("\\[亲亲\\]","<img src=\"d_qinqin\"/>")
        .replaceAll("\\[傻眼\\]","<img src=\"d_shayan\"/>")
        .replaceAll("\\[生病\\]","<img src=\"d_shengbing\"/>")
        .replaceAll("\\[神兽\\]","<img src=\"d_shenshou\"/>")
        .replaceAll("\\[草泥马\\]","<img src=\"d_shenshou\"/>")
        .replaceAll("\\[失望\\]","<img src=\"d_shiwang\"/>")
        .replaceAll("\\[衰\\]","<img src=\"d_shuai\"/>")
        .replaceAll("\\[睡觉\\]","<img src=\"d_shuijiao\"/>")
        .replaceAll("\\[睡\\]","<img src=\"d_shuijiao\"/>")
        .replaceAll("\\[思考\\]","<img src=\"d_sikao\"/>")
        .replaceAll("\\[太开心\\]","<img src=\"d_taikaixin\"/>")
        .replaceAll("\\[抱抱\\]","<img src=\"d_taikaixin\"/>")
        .replaceAll("\\[偷笑\\]","<img src=\"d_touxiao\"/>")
        .replaceAll("\\[吐\\]","<img src=\"d_tu\"/>")
        .replaceAll("\\[兔子\\]","<img src=\"d_tuzi\"/>")
        .replaceAll("\\[挖鼻\\]","<img src=\"d_wabishi\"/>")
        .replaceAll("\\[委屈\\]","<img src=\"d_weiqu\"/>")
        .replaceAll("\\[笑cry\\]","<img src=\"d_xiaoku\"/>")
        .replaceAll("\\[熊猫\\]","<img src=\"d_xiongmao\"/>")
        .replaceAll("\\[嘻嘻\\]","<img src=\"d_xixi\"/>")
        .replaceAll("\\[嘘\\]","<img src=\"d_xu\"/>")
        .replaceAll("\\[阴险\\]","<img src=\"d_yinxian\"/>")
        .replaceAll("\\[疑问\\]","<img src=\"d_yiwen\"/>")
        .replaceAll("\\[右哼哼\\]","<img src=\"d_youhengheng\"/>")
        .replaceAll("\\[晕\\]","<img src=\"d_yun\"/>")
        .replaceAll("\\[抓狂\\]","<img src=\"d_zhuakuang\"/>")
        .replaceAll("\\[猪头\\]","<img src=\"d_zhutou\"/>")
        .replaceAll("\\[最右\\]","<img src=\"d_zuiyou\"/>")
        .replaceAll("\\[左哼哼\\]","<img src=\"d_zuohengheng\"/>")

        //浪小花表情
        .replaceAll("\\[悲催\\]","<img src=\"lxh_beicui\"/>")
        .replaceAll("\\[被电\\]","<img src=\"lxh_beidian\"/>")
        .replaceAll("\\[崩溃\\]","<img src=\"lxh_bengkui\"/>")
        .replaceAll("\\[别烦我\\]","<img src=\"lxh_biefanwo\"/>")
        .replaceAll("\\[不好意思\\]","<img src=\"lxh_buhaoyisi\"/>")
        .replaceAll("\\[不想上班\\]","<img src=\"lxh_buxiangshangban\"/>")
        .replaceAll("\\[得意地笑\\]","<img src=\"lxh_deyidexiao\"/>")
        .replaceAll("\\[给劲\\]","<img src=\"lxh_feijin\"/>")
        .replaceAll("\\[好爱哦\\]","<img src=\"lxh_haoaio\"/>")
        .replaceAll("\\[好棒\\]","<img src=\"lxh_haobang\"/>")
        .replaceAll("\\[好囧\\]","<img src=\"lxh_haojiong\"/>")
        .replaceAll("\\[好喜欢\\]","<img src=\"lxh_haoxihuan\"/>")
        .replaceAll("\\[hold住\\]","<img src=\"lxh_holdzhu\"/>")
        .replaceAll("\\[杰克逊\\]","<img src=\"lxh_jiekexun\"/>")
        .replaceAll("\\[纠结\\]","<img src=\"lxh_jiujie\"/>")
        .replaceAll("\\[巨汗\\]","<img src=\"lxh_juhan\"/>")
        .replaceAll("\\[抠鼻屎\\]","<img src=\"lxh_koubishi\"/>")
        .replaceAll("\\[困死了\\]","<img src=\"lxh_kunsile\"/>")
        .replaceAll("\\[雷锋\\]","<img src=\"lxh_leifeng\"/>")
        .replaceAll("\\[泪流满面\\]","<img src=\"lxh_leiliumanmian\"/>")
        .replaceAll("\\[玫瑰\\]","<img src=\"lxh_meigui\"/>")
        .replaceAll("\\[噢耶\\]","<img src=\"lxh_oye\"/>")
        .replaceAll("\\[霹雳\\]","<img src=\"lxh_pili\"/>")
        .replaceAll("\\[瞧瞧\\]","<img src=\"lxh_qiaoqiao\"/>")
        .replaceAll("\\[丘比特\\]","<img src=\"lxh_qiubite\"/>")
        .replaceAll("\\[求关注\\]","<img src=\"lxh_qiuguanzhu\"/>")
        .replaceAll("\\[群体围观\\]","<img src=\"lxh_quntiweiguan\"/>")
        .replaceAll("\\[甩甩手\\]","<img src=\"lxh_shuaishuaishou\"/>")
        .replaceAll("\\[偷乐\\]","<img src=\"lxh_toule\"/>")
        .replaceAll("\\[推荐\\]","<img src=\"lxh_tuijian\"/>")
        .replaceAll("\\[互相膜拜\\]","<img src=\"lxh_xianghumobai\"/>")
        .replaceAll("\\[想一想\\]","<img src=\"lxh_xiangyixiang\"/>")
        .replaceAll("\\[笑哈哈\\]","<img src=\"lxh_xiaohaha\"/>")
        .replaceAll("\\[羞嗒嗒\\]","<img src=\"lxh_xiudada\"/>")
        .replaceAll("\\[许愿\\]","<img src=\"lxh_xuyuan\"/>")
        .replaceAll("\\[有鸭梨\\]","<img src=\"lxh_youyali\"/>")
        .replaceAll("\\[赞啊\\]","<img src=\"lxh_zana\"/>")
        .replaceAll("\\[震惊\\]","<img src=\"lxh_zhenjing\"/>")
        .replaceAll("\\[转发\\]","<img src=\"lxh_zhuanfa\"/>")

        //其他
        .replaceAll("\\[蛋糕\\]","<img src=\"o_dangao\"/>")
        .replaceAll("\\[飞机\\]","<img src=\"o_feiji\"/>")
        .replaceAll("\\[干杯\\]","<img src=\"o_ganbei\"/>")
        .replaceAll("\\[话筒\\]","<img src=\"o_huatong\"/>")
        .replaceAll("\\[蜡烛\\]","<img src=\"o_lazhu\"/>")
        .replaceAll("\\[礼物\\]","<img src=\"o_liwu\"/>")
        .replaceAll("\\[围观\\]","<img src=\"o_weiguan\"/>")
        .replaceAll("\\[咖啡\\]","<img src=\"o_kafei\"/>")
        .replaceAll("\\[足球\\]","<img src=\"o_zuqiu\"/>")


        .replaceAll("\\[ok\\]","<img src=\"h_ok\"/>")
        .replaceAll("\\[躁狂症\\]","<img src=\"lxh_zaokuangzheng\"/>")
        .replaceAll("\\[威武\\]","<img src=\"weiwu\"/>")
        .replaceAll("\\[赞\\]","<img src=\"h_zan\"/>")
        .replaceAll("\\[心\\]","<img src=\"l_xin\"/>")
        .replaceAll("\\[伤心\\]","<img src=\"l_shangxin\"/>")
        .replaceAll("\\[月亮\\]","<img src=\"w_yueliang\"/>")
        .replaceAll("\\[鲜花\\]","<img src=\"w_xianhua\"/>")
        .replaceAll("\\[太阳\\]","<img src=\"w_taiyang\"/>")
        .replaceAll("\\[威武\\]","<img src=\"weiwu\"/>")
        .replaceAll("\\[浮云\\]","<img src=\"w_fuyun\"/>")
        .replaceAll("\\[神马\\]","<img src=\"shenma\"/>")
        .replaceAll("\\[微风\\]","<img src=\"w_weifeng\"/>")
        .replaceAll("\\[下雨\\]","<img src=\"w_xiayu\"/>")
        .replaceAll("\\[色\\]","<img src=\"huaxin\"/>")
        .replaceAll("\\[沙尘暴\\]","<img src=\"w_shachenbao\"/>")
        .replaceAll("\\[落叶\\]","<img src=\"w_luoye\"/>")
        .replaceAll("\\[雪人\\]","<img src=\"w_xueren\"/>")
        .replaceAll("\\[good\\]","<img src=\"h_good\"/>")
        .replaceAll("\\[哆啦A梦吃惊\\]","<img src=\"dorahaose_mobile\"/>")
        .replaceAll("\\[哆啦A梦微笑\\]","<img src=\"jqmweixiao_mobile\"/>")
        .replaceAll("\\[哆啦A梦花心\\]","<img src=\"dorahaose_mobile\"/>")
        .replaceAll("\\[弱\\]","<img src=\"ruo\"/>")
        .replaceAll("\\[炸鸡啤酒\\]","<img src=\"d_zhajipijiu\"/>")
        .replaceAll("\\[囧\\]","<img src=\"jiong\"/>")
        .replaceAll("\\[NO\\]","<img src=\"buyao\"/>")
        .replaceAll("\\[来\\]","<img src=\"guolai\"/>")
        .replaceAll("\\[互粉\\]","<img src=\"f_hufen\"/>")
        .replaceAll("\\[握手\\]","<img src=\"h_woshou\"/>")
        .replaceAll("\\[haha\\]","<img src=\"h_haha\"/>")
        .replaceAll("\\[织\\]","<img src=\"zhi\"/>")
        .replaceAll("\\[萌\\]","<img src=\"meng\"/>")
        .replaceAll("\\[钟\\]","<img src=\"o_zhong\"/>")
        .replaceAll("\\[给力\\]","<img src=\"geili\"/>")
        .replaceAll("\\[喜\\]","<img src=\"xi\"/>")
        .replaceAll("\\[绿丝带\\]","<img src=\"o_lvsidai\"/>")
        .replaceAll("\\[围脖\\]","<img src=\"weibo\"/>")
        .replaceAll("\\[音乐\\]","<img src=\"o_yinyue\"/>")
        .replaceAll("\\[照相机\\]","<img src=\"o_zhaoxiangji\"/>")
        .replaceAll("\\[耶\\]","<img src=\"h_ye\"/>")
        .replaceAll("\\[拍照\\]","<img src=\"lxhpz_paizhao\"/>")
        .replaceAll("\\[白眼\\]","<img src=\"landeln_baiyan\"/>")


        .replaceAll("\\[作揖\\]","<img src=\"o_zuoyi\"/>")
        .replaceAll("\\[拳头\\]","<img src=\"quantou_org\"/>")
        .replaceAll("\\[X教授\\]","<img src=\"xman_jiaoshou\"/>")
        .replaceAll("\\[天启\\]","<img src=\"xman_tianqi\"/>")
        .replaceAll("\\[抢到啦\\]","<img src=\"hb_qiangdao_org\"/>")
		
		;
	}
}