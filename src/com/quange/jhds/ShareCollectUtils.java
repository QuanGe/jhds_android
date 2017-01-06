package com.quange.jhds;


import com.quange.jhds.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ShareCollectUtils {

	static String[] shareMessage = {"叮咚，新鲜的简画出炉了，看我画的还凑合吧？",
            "快来瞧，快来看，我刚创作了一番，看看我的水平是啥等级的",
            "哎呀，妈呀，累死我了，终于画完了",
            "小手一抖，简画在手",
            "啥？我画的不好看？你来试试",
            "you can you up，no can no BB",
            "快得了吧，这是我画的最好的了，😄",
            "简画，就是简单，想画就画，我骄傲😄",
            "一句话：不服来画给我看。"};
	/** 微博微博分享接口实例 */
    public static IWeiboShareAPI  mWeiboShareAPI = null;
	//0 text 1localimg 2bitmap
	public static void shareContent(final Activity activity,
			final String title, final String shareUrl,final Bitmap bitmap,final int type) {

		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(
				activity, "3129504298");
		
		mWeiboShareAPI.registerApp();
		mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), (Response) activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		final View vPopWindow = inflater.inflate(R.layout.layout_share, null);
		final PopupWindow popupWindow = new PopupWindow(vPopWindow,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.update();

		Button btn_weibo = (Button) vPopWindow.findViewById(R.id.btn_weibo);
		Button btn_weixin_1 = (Button) vPopWindow
				.findViewById(R.id.btn_weixin_1);
		Button btn_weixin_2 = (Button) vPopWindow
				.findViewById(R.id.btn_weixin_2);
		Button btn_qq_1 = (Button) vPopWindow.findViewById(R.id.btn_qq_1);
		Button btn_qq_2 = (Button) vPopWindow.findViewById(R.id.btn_qq_2);
		
		if(type != 0)
			vPopWindow.findViewById(R.id.zoneBtn).setVisibility(View.GONE);
		else
			vPopWindow.findViewById(R.id.zoneBtn).setVisibility(View.VISIBLE);
		Button btn_cancel = (Button) vPopWindow.findViewById(R.id.btn_cancel);
		final String description = QQQZoneShare.getShareContent(title);

		btn_weibo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				MobclickAgent.onEvent(v.getContext(), "canvas_share_sina");
				
				
				
				if(type ==1 ||type ==2)
				{
					/*图片对象*/  
				    ImageObject imageobj = new ImageObject();  
				  
				    if (type ==2) {  
				        imageobj.setImageObject(bitmap);  
				    } 
				    else
				    {
				    	imageobj.setImageObject(AppCommon.getInstance().getLoacalBitmap(shareUrl, AppCommon.getInstance().screenWidth, AppCommon.getInstance().screenHeight));
				    }
				  
				    /*微博数据的message对象*/  
				    WeiboMultiMessage multmess = new WeiboMultiMessage();  
				    TextObject textobj = new TextObject();  
				    String sm = shareMessage[(int) (Math.random() * 9)];
				    textobj.text = "@简画大师 #简画大师#"+sm;  
				  
				    multmess.textObject = textobj;  
				    multmess.imageObject = imageobj;  
				    /*微博发送的Request请求*/  
				    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();  
				    request.multiMessage = multmess;  
				    //以当前时间戳为唯一识别符  
				    request.transaction = String.valueOf(System.currentTimeMillis());  
				   
				    
					mWeiboShareAPI.sendRequest(activity,request);
				
				}
				else if(type == 0)
				{
					WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
					TextObject textObject = new TextObject();
					textObject.text = title;
					weiboMessage.textObject = textObject;
					WebpageObject wo = new WebpageObject();
					wo.actionUrl = shareUrl;
					wo.title = title;
					wo.description = QQQZoneShare.getProtectBabyShareContent(title);
					wo.identify = Utility.generateGUID();
					Bitmap bmp = BitmapFactory.decodeResource(
							activity.getResources(), R.drawable.ic_launcher);
					wo.setThumbImage(bmp);
					weiboMessage.mediaObject = wo;

					SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
					request.transaction = String.valueOf(System.currentTimeMillis());
					request.multiMessage = weiboMessage;
					mWeiboShareAPI.sendRequest(activity,request);
				}
				popupWindow.dismiss();
			}
		});

		btn_weixin_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(v.getContext(), "canvas_share_wx");
				if(type ==1 ||type ==2)
				{
					
					Bitmap tmpBitmap = null;
					if(type == 1)
						tmpBitmap = AppCommon.getInstance().getLoacalBitmap(shareUrl, AppCommon.getInstance().screenWidth, AppCommon.getInstance().screenHeight);
					else
						tmpBitmap = bitmap;
					WXImageObject imgObj = new WXImageObject(tmpBitmap);
					
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;
					Bitmap thumbBitmap =  Bitmap.createScaledBitmap(tmpBitmap, 150, 150*(AppCommon.getInstance().screenHeight/AppCommon.getInstance().screenWidth), true);  
					if(type == 2)
						bitmap.recycle();  
			        msg.thumbData = AppCommon.getInstance().Bitmap2Bytes(thumbBitmap);  //设置缩略图 
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneSession;
					AppCommon.getInstance().api.sendReq(req);
					
				}
				else if(type == 0)
				{
				
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = shareUrl;
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = title;
					msg.description = QQQZoneShare.getProtectBabyShareContent(title);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneSession;
					AppCommon.getInstance().api.sendReq(req);
					
				}
				
				popupWindow.dismiss();
			}
		});

		btn_weixin_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(v.getContext(), "canvas_share_pyq");
				if(type ==1 ||type ==2)
				{
					Bitmap tmpBitmap = null;
					if(type == 1)
						tmpBitmap = AppCommon.getInstance().getLoacalBitmap(shareUrl, AppCommon.getInstance().screenWidth, AppCommon.getInstance().screenHeight);
					else
						tmpBitmap = bitmap;
					WXImageObject imgObj = new WXImageObject(tmpBitmap);
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;
					Bitmap thumbBitmap =  Bitmap.createScaledBitmap(tmpBitmap, 150, 150*(AppCommon.getInstance().screenHeight/AppCommon.getInstance().screenWidth), true);  
					if(type == 2)
						bitmap.recycle();  
			        msg.thumbData = AppCommon.getInstance().Bitmap2Bytes(thumbBitmap);  //设置缩略图 
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					AppCommon.getInstance().api.sendReq(req);
				}
				else if(type == 0)
				{
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = shareUrl;
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = title;
					msg.description = QQQZoneShare.getProtectBabyShareContent(title);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
				}
				popupWindow.dismiss();
			}
		});

		btn_qq_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(v.getContext(), "canvas_share_qq");
				
				if(type ==1 ||type ==2)
				{
					try {
						if(type == 2)
							AppCommon.getInstance().saveBitmapToFile(bitmap, shareUrl, false);
						QQQZoneShare.addQQQZonePlatform(activity,
								QQQZoneShare.SHARE_QQ);
						QQQZoneShare.setShareImg (shareUrl);
						QQQZoneShare.performShare(activity,
								QQQZoneShare.mController, SHARE_MEDIA.QQ,
								popupWindow);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				else if(type == 0)
				{
					try {
						QQQZoneShare.addQQQZonePlatform(activity,
								QQQZoneShare.SHARE_QQ);
						QQQZoneShare.setShareContent(QQQZoneShare.SHARE_QQ, title,
								QQQZoneShare.getProtectBabyShareContent(title), shareUrl);
						QQQZoneShare.performShare(activity,
								QQQZoneShare.mController, SHARE_MEDIA.QQ,
								popupWindow);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				popupWindow.dismiss();
			}
		});

		btn_qq_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					QQQZoneShare.addQQQZonePlatform(activity,
							QQQZoneShare.SHARE_QZONE);
					QQQZoneShare.setShareContent(QQQZoneShare.SHARE_QZONE,
							title, QQQZoneShare.getProtectBabyShareContent(title), shareUrl);
					QQQZoneShare.performShare(activity,
							QQQZoneShare.mController, SHARE_MEDIA.QZONE,
							popupWindow);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				popupWindow.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.showAtLocation(activity.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);

	}

}
