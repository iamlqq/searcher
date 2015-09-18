package com.zhihui.quicksearch.http;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SearchEncrypt
{

	private static final String tag = SearchEncrypt.class.getSimpleName();
	private static String Algorithm = "DES";
	public static String key = "CB7A92E3D34919FG";
	// 定义 加密算法,可用 DES,DESede,Blowfish
	static boolean debug = false;

	/**
	 * 构造函数
	 */
	public SearchEncrypt()
	{

	}

	/**
	 * 生成密钥
	 * 
	 * @return byte[] 返回生成的密钥
	 * @throws exception
	 *             扔出异常.
	 */
	public static byte[] getSecretKey() throws Exception
	{
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
		// System.out.println("creat key:"
		// + bytesToHexString(deskey.getEncoded()));
		// if (debug)
		// System.out.println("creat key:"
		// + bytesToHexString(deskey.getEncoded()));
		return deskey.getEncoded();

	}

	/**
	 * 将指定的数据根据提供的密钥进行加�?
	 * 
	 * @param input
	 *            �?��加密的数�?
	 * @param key
	 *            密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encryptData(byte[] input, byte[] key) throws Exception
	{
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		// if (debug)
		// {
		// System.out.println("before encrypt:" + byte2hex(input));
		// System.out.println("afte encrypt:" + new String(input));
		//
		// }
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		// if (debug)
		// System.out.println("afte encrypt:" + byte2hex(cipherByte));
		return cipherByte;

	}

	/**
	 * 将给定的已加密的数据通过指定的密钥进行解�?
	 * 
	 * @param input
	 *            待解密的数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密后的数据
	 * @throws Exception
	 */
	public static byte[] decryptData(byte[] input, byte[] key) throws Exception
	{
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		// if (debug)
		// System.out.println("before decrypt info:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		// if (debug)
		// {
		// System.out.println("after decrypt hex:" + byte2hex(clearByte));
		// System.out.println("after decrypt string:"
		// + (new String(clearByte)));
		//
		// }
		return clearByte;

	}

	/**
	 * 字节码转换成16进制字符�?
	 * 
	 * @param b
	 *            输入要转换的字节�?
	 * @return String 返回转换后的16进制字符�?
	 */
	public static String byte2hex(byte[] b)
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";

		}
		return hs.toUpperCase();

	}

	/**
	 * 字符串转成字节数�?
	 * 
	 * @param hex
	 *            要转化的字符�?
	 * @return byte[] 返回转化后的字符�?
	 */
	public static byte[] hexStringToByte(String hex)
	{
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++)
		{
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c)
	{
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 字节数组转成字符�?
	 * 
	 * @param bArray
	 *            要转化的字符�?
	 * @return 返回转化后的字节数组.
	 */
	public static final String bytesToHexString(byte[] bArray)
	{
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++)
		{
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 从数据库中获取密�?
	 * 
	 * @param deptid
	 *            企业id.
	 * @return 要返回的字节数组.
	 * @throws Exception
	 *             可能抛出的异�?
	 */
	public static byte[] getSecretKey(long deptid) throws Exception
	{
		byte[] key = null;
		String value = null;
		// CommDao dao=new CommDao();
		// List list=dao.getRecordList("from Key k where k.deptid="+deptid);
		// if(list.size()>0){
		// value=((com.csc.sale.bean.Key)list.get(0)).getKey();
		value = "CB7A92E3D3491964";
		key = hexStringToByte(value);
		// }
		// if (debug)
		// System.out.println("key:" + value);
		return key;
	}

	/**
	 * 加密数据
	 * 
	 * @param data
	 * @return
	 */
	public static String encryptData2(String data)
	{
		String en = null;
		try
		{
			byte[] key = hexStringToByte(SearchEncrypt.key);
			en = bytesToHexString(encryptData(data.getBytes(), key));
		} catch (Exception e)
		{
			// e.printStackTrace();
		}
		return en;
	}

	public static String decryptData2(String data)
	{
		String de = null;
		try
		{
			byte[] key = hexStringToByte(SearchEncrypt.key);
			de = new String(decryptData(hexStringToByte(data), key));
		} catch (Exception e)
		{
			// e.printStackTrace();
		}
		return de;
	}

	public static String decryptData2(String data, String key)
	{
		String de = null;
		try
		{
			byte[] k = hexStringToByte(key);
			de = new String(decryptData(hexStringToByte(data), k));
		} catch (Exception e)
		{
			// e.printStackTrace();
		}
		return de;
	}

	// /**
	// * 判断JSON数据有没经过本类加密<br>
	// * 加过密的话返回true，否则返回false
	// * <p>
	// * 备注:只能判断JSON格式的数据是否加�?不能判断非JSON格式的数�?!!
	// *
	// * @param jsonData
	// * JSON原始数据，如:<br>
	// * {"seq":"TKDZ20130313","from":"KLF001","use_sum":5,"use_day":5,
	// * "comm_interval_day"
	// * :10,"stop_chars":"hello_88","pay_ok":"abc123@google.com"
	// * ,"charge_switch"
	// * :true,"host_url":"http://mydomain.com/","port_name"
	// * :"foreign_config.jsp"} 或�?是这份数据的加密数据
	// * @return 加过密的话返回true，否则返回false
	// */
	// public static final boolean isJsonEncryption(String jsonData)
	// {
	// boolean v = false;
	// // 不包含这些字符的话，表示加密�?
	// v = !(jsonData.contains("{") || jsonData.contains("}") || jsonData
	// .contains(":"));
	// return v;
	// }
}
