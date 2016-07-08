package com.quange.jhds;

import java.io.File;


import android.app.Activity;
import android.content.Context;
import android.widget.PopupWindow;

import com.quange.viewModel.JHDSAPIManager;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

public class QQQZoneShare {
	
	static String[] shareMessage = {"叮咚，新鲜的简画出炉了，看我画的还凑合吧？#简画大师#",
        "快来瞧，看来看，我刚创作了一番，看看我的水平是啥等级的#简画大师#",
        "哎呀，妈呀，累死我了，终于画完了#简画大师#",
        "小手一抖，简画在手#简画大师#",
        "啥？我画的不好看？你来试试#简画大师#",
        "you can you up，no can no BB？#简画大师#",
        "快得了吧，这是我画的最好的了，😄？#简画大师#",
        "简画，就是简单，想画就画，我骄傲😄？#简画大师#",
        "一句话：不服来画给我看。#简画大师#"};
	
	public final static UMSocialService mController = UMServiceFactory
            .getUMSocialService(QQQZoneShare.DESCRIPTOR);
	
    public static String appId_qq = "1105326131";
	
	public static String appKey = "CoUMtB1NLxoDIuxM";
	
	public static final String DESCRIPTOR = "com.umeng.share";
	
	public final static int SHARE_QQ = 4;
	
	public final static int SHARE_QZONE = 5;
	
	public static void addQQQZonePlatform(Activity mActivity, int whichshare) {
        switch(whichshare){
        case SHARE_QQ:
        	 // 添加QQ支持
             UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId_qq, appKey);
             qqSsoHandler.addToSocialSDK();
        	 break;
        case SHARE_QZONE:
        	 // 添加QZone平台
             QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId_qq, appKey);
             qZoneSsoHandler.addToSocialSDK();
        	 break;
        }
        
    }
	
	/**
     * 根据不同的平台设置不同的分享内容</br>
     */
    public static void setShareContent(int whichshare, 
    		                           String sharetitle, 
    		                           String sharecontent, 
    		                           String shareurl) {
    	UMImage localImage = new UMImage(JHDSAPIManager.getInstance(null).getContextVar(), com.quange.jhds.R.drawable.jhds_launcher);
    	switch(whichshare){
    	case QQQZoneShare.SHARE_QQ:
    		 QQShareContent qqShareContent = new QQShareContent();
             qqShareContent.setShareContent(sharecontent);
             qqShareContent.setTitle(sharetitle);
             qqShareContent.setTargetUrl(shareurl);
             qqShareContent.setShareMedia(localImage);
             mController.setShareMedia(qqShareContent);
    		 break;
    	case QQQZoneShare.SHARE_QZONE:
    		 // 设置QQ空间分享内容
             QZoneShareContent qzone = new QZoneShareContent();
             qzone.setShareContent(sharecontent);
             qzone.setTargetUrl(shareurl);
             qzone.setTitle(sharetitle);
             qzone.setShareMedia(localImage);
             mController.setShareMedia(qzone);
    		 break;
    	}

    }
	
	public static void performShare(Context mContext,
			                        UMSocialService mController, 
			                        SHARE_MEDIA platform, 
			                        final PopupWindow popupWindow) {
        mController.postShare(mContext, platform, new SnsPostListener() {

            @Override
            public void onStart() {
            	//Utils.showTextToast("开始分享");
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            	if (eCode == 200) {
            		//Utils.showTextToast("分享成功");
                } 
//            	else {
//                     String eMsg = "";
//                     if (eCode == -101){
//                         eMsg = "没有授权";
//                     }
//                     Utils.showTextToast("分享失败[" + eCode + "] " + eMsg);
//                }
            	popupWindow.dismiss();
            }
        });
    }
    
	/**
	 * 
	 
	 * @param content
	 * @return
	 */
	public static String getShareContent(String content){
		String str = "";
		
			 str = "我用#简画大师#这个APP发现了一个好的简画教程《" 
	                 + content 
	                 +"》";
	
		return str;
		
	}
	
	/**
	 * 
	 
	 * @param content
	 * @return
	 */
	public static String getProtectBabyShareContent(String content){
		String str = "";
		
			 str = "我愿意转发，守护宝贝，助力早日回家,《" 
	                 + content 
	                 +"》，来自#简画大师#";
	
		return str;
		
	}
	
	 public static void setShareImg( 
	            String localImgUrl) {
	    	File file = new File(localImgUrl); 
			UMImage localImage = new UMImage(JHDSAPIManager.getInstance(null).getContextVar(), file);
			
			QQShareContent qqShareContent = new QQShareContent();
			String sm = shareMessage[(int) (Math.random() * 9)];
			qqShareContent.setTitle(sm);
			
			qqShareContent.setShareImage(localImage);
			mController.setShareMedia(qqShareContent);
			
			
			
		}
}
