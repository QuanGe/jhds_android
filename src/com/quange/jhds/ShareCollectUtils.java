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

	public static void shareContent(final Activity activity,
			final String title, final String shareUrl,final Bitmap bitmap) {

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
		
		if(bitmap != null)
			vPopWindow.findViewById(R.id.zoneBtn).setVisibility(View.GONE);
		else
			vPopWindow.findViewById(R.id.zoneBtn).setVisibility(View.VISIBLE);
		Button btn_cancel = (Button) vPopWindow.findViewById(R.id.btn_cancel);
		final String description = QQQZoneShare.getShareContent(title);

		btn_weibo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				MobclickAgent.onEvent(v.getContext(), "canvas_share_sina");
				IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(
						activity, "3129504298");
				if (mWeiboShareAPI.checkEnvironment(true)) {
					// 注册第三方应用 到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
					// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
					mWeiboShareAPI.registerApp();
				}
				
				if(bitmap != null)
				{
					/*图片对象*/  
				    ImageObject imageobj = new ImageObject();  
				  
				    if (bitmap != null) {  
				        imageobj.setImageObject(bitmap);  
				    }  
				  
				    /*微博数据的message对象*/  
				    WeiboMultiMessage multmess = new WeiboMultiMessage();  
				    TextObject textobj = new TextObject();  
				    textobj.text = "我用#简画大师#创作了一副简画，快来围观吧";  
				  
				    multmess.textObject = textobj;  
				    multmess.imageObject = imageobj;  
				    /*微博发送的Request请求*/  
				    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();  
				    request.multiMessage = multmess;  
				    //以当前时间戳为唯一识别符  
				    request.transaction = String.valueOf(System.currentTimeMillis());  
				   
				    
					mWeiboShareAPI.sendRequest(request);
				
				}
				else
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
				}
				popupWindow.dismiss();
			}
		});

		btn_weixin_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(v.getContext(), "canvas_share_wx");
				if(bitmap != null)
				{
					
					WXImageObject imgObj = new WXImageObject(bitmap);  
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;
					Bitmap thumbBitmap =  Bitmap.createScaledBitmap(bitmap, 150, 150*(AppCommon.getInstance().screenHeight/AppCommon.getInstance().screenWidth), true);  
			        bitmap.recycle();  
			        msg.thumbData = AppCommon.getInstance().Bitmap2Bytes(thumbBitmap);  //设置缩略图 
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneSession;
					AppCommon.getInstance().api.sendReq(req);
					
				}
				else
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
				if(bitmap != null)
				{
					WXImageObject imgObj = new WXImageObject(bitmap);  
					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;
					Bitmap thumbBitmap =  Bitmap.createScaledBitmap(bitmap, 150, 150*(AppCommon.getInstance().screenHeight/AppCommon.getInstance().screenWidth), true);  
			        bitmap.recycle();  
			        msg.thumbData = AppCommon.getInstance().Bitmap2Bytes(thumbBitmap);  //设置缩略图 
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String.valueOf(System.currentTimeMillis());
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					AppCommon.getInstance().api.sendReq(req);
				}
				else
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
				
				if(bitmap != null)
				{
					try {
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
				else
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
