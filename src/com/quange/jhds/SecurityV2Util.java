package com.quange.jhds;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/// <summary>
/// 提供加密、解密算法
/// </summary>
public class SecurityV2Util {

	// / <summary>
	// / 3DES加密算法
	// / </summary>
	// / <param name="plainText">明文字符串</param>
	public static String DESEncrypt(String key, String plainText) {
		try {
			while(key.length() < 24){
				key += "0";
			}
			key = key.substring(0, 24);
			
			SecretKey deskey = new SecretKeySpec(key.getBytes(), "DESede");
			// "算法/模式/补码方式", 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] inputByteArray = plainText.getBytes("UTF-8");
			byte[] encrypted = cipher.doFinal(inputByteArray);
			encrypted = Base64.encode(encrypted, Base64.NO_WRAP);
			return new String(encrypted, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	// / <summary>
	// / 3DES解密
	// / </summary>
	// / <param name="cipherText">密文字符串</param>
	public static String DESDecrypt(String key, String cipherText) {
		try {
			while(key.length() < 24){
				key += "0";
			}
			key = key.substring(0, 24);
			
			byte[] cipherText2 = cipherText.getBytes("UTF-8");
			cipherText2 = Base64.decode(cipherText2, Base64.NO_WRAP);
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "DESede");
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(cipherText2);
			return new String(original, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getSignatureByMD5(String key){
		try {
			char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			//对参数进行加密活动签名
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(key.getBytes("UTF-8"));
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
