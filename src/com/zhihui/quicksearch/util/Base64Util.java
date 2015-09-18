package com.zhihui.quicksearch.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Author: <a href="tuziilm@163.com">Tuziilm</a>
 * Date: 15-6-5
 */
public class Base64Util {
	public Base64Util() {
	}

	public static String encode(String data) {
		return new String(encode(data.getBytes()));
	}

	public static char[] encode(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	public static byte[] decode(char[] data) {
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || codes[data[ix]] < 0) {
				--tempLen; // ignore non-valid chars and padding
			}
		}
		// calculate required length:
		// -- 3 bytes for every 4 valid base64 chars
		// -- plus 2 bytes if there are 3 extra base64 chars,
		// or plus 1 byte if there are 2 extra.

		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3) {
			len += 2;
		}
		if ((tempLen % 4) == 2) {
			len += 1;

		}
		byte[] out = new byte[len];

		int shift = 0; // # of excess bits stored in accum
		int accum = 0; // excess bits
		int index = 0;

		// we now go through the entire array (NOT using the 'tempLen' value)
		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];

			if (value >= 0) { // skip over non-code
				accum <<= 6; // bits shift up by 6 each time thru
				shift += 6; // loop, with new bits being put in
				accum |= value; // at the bottom.
				if (shift >= 8) { // whenever there are 8 or more shifted in,
					shift -= 8; // write them out (from the top, leaving any
					out[index++] = // excess at the bottom for next iteration.
							(byte) ((accum >> shift) & 0xff);
				}
			}
		}
		// if there is STILL something wrong we just have to throw up now!
		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index
					+ " instead of " + out.length + ")");
		}
		return out;
	}

	public static void encode(File file) throws IOException {
		if (!file.exists()) {
			System.exit(0);
		}else {
			byte[] decoded = readBytes(file);
			char[] encoded = encode(decoded);
			writeChars(file, encoded);
		}
		file = null;
	}

	public static void decode(File file) throws IOException {
		if (!file.exists()) {
			System.exit(0);
		} else {
			char[] encoded = readChars(file);
			byte[] decoded = decode(encoded);
			writeBytes(file, decoded);
		}
		file = null;
	}

	//
	// code characters for values 0..63
	//
	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();

	//
	// lookup table for converting base64 characters to value in range 0..63
	//
	private static byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++) {
			codes[i] = -1;
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			codes[i] = (byte) (i - 'A');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}

		for (int i = 'a'; i <= 'z'; i++) {
			codes[i] = (byte) (26 + i - 'a');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		for (int i = '0'; i <= '9'; i++) {
			codes[i] = (byte) (52 + i - '0');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		codes['+'] = 62;
		codes['/'] = 63;
	}

	private static byte[] readBytes(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = null;
		InputStream fis = null;
		InputStream is = null;
		try {
			fis = new FileInputStream(file);
			is = new BufferedInputStream(fis);
			int count = 0;
			byte[] buf = new byte[16384];
			while ((count = is.read(buf)) != -1) {
				if (count > 0) {
					baos.write(buf, 0, count);
				}
			}
			b = baos.toByteArray();

		} finally {
			try {
				if (fis != null)
					fis.close();
				if (is != null)
					is.close();
				if (baos != null)
					baos.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return b;
	}

	private static char[] readChars(File file) throws IOException {
		CharArrayWriter caw = new CharArrayWriter();
		Reader fr = null;
		Reader in = null;
		try {
			fr = new FileReader(file);
			in = new BufferedReader(fr);
			int count = 0;
			char[] buf = new char[16384];
			while ((count = in.read(buf)) != -1) {
				if (count > 0) {
					caw.write(buf, 0, count);
				}
			}

		} finally {
			try {
				if (caw != null)
					caw.close();
				if (in != null)
					in.close();
				if (fr != null)
					fr.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return caw.toCharArray();
	}

	private static void writeBytes(File file, byte[] data) throws IOException {
		OutputStream fos = null;
		OutputStream os = null;
		try {
			fos = new FileOutputStream(file);
			os = new BufferedOutputStream(fos);
			os.write(data);

		} finally {
			try {
				if (os != null)
					os.close();
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private static void writeChars(File file, char[] data) throws IOException {
		Writer fos = null;
		Writer os = null;
		try {
			fos = new FileWriter(file);
			os = new BufferedWriter(fos);
			os.write(data);

		} finally {
			try {
				if (os != null)
					os.close();
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static char[] key = "s7J3M1LHOTexvfqzhXmVr5lZwUuKb0DtdYo-FyB2N9QgRapI4E8niW6_GCSAjPkc=".toCharArray();
	private static char[] encodeKey = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=".toCharArray();
	private static BiMap<Character,Character> keyMap = relationship();
	private static BiMap<Character,Character> relationship(){
		BiMap<Character,Character> keyMap= HashBiMap.create();
		for(int i=0;i<key.length;i++) {
			keyMap.put(encodeKey[i], key[i]);
		}
		return keyMap;
	}

	public static String encodeByKey(String code){
		char[] codeChars = code.toCharArray();
		char[] result = new char[code.length()];
		for(int i=0;i<codeChars.length;i++){
			result[i]=keyMap.get(codeChars[i]);
		}
		return String.copyValueOf(result);
	}

	public static String  decodeByKey(String code){
		char[] codeChars = code.toCharArray();
		char[] result = new char[code.length()];
		for(int i=0;i<codeChars.length;i++) {
			result[i] = keyMap.inverse().get(codeChars[i]);
		}
		return String.copyValueOf(result);
	}

//	public static void main(String[] args) {
//		//登陆
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"1001\",\"opdata\":{\"pwd\":\"test\",\"user\":\"test\"},\"os\":\"win7\"}");
//		//套餐
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"1020\",\"opdata\":{\"checkcode\":\"\",\"uid\":\"1\"},\"os\":\"win7\",\"token\":\"C5E544D59691A3CBF9349A2FA55D0D4A\",\"uid\":\"1\"}");
//		//单应用安装
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"2011\",\"opdata\":{\"uid\":\"32\",\"androidid\":\"bbe123ad537dde21\",\"appid\":\"5\",\"bbv\":\"\",\"bv\":\"4721.000000\",\"cpunum\":\"9fc5e07a\",\"deviceos\":\"4.4.4\",\"disk\":\"5021.000000\",\"flag\":0,\"iccid\":\"\",\"imei\":\"866615020422273\",\"imsi\":\"460110418522962\",\"installflag\":\"1\",\"itime\":\"2015-06-24 17:00:16\",\"itv\":\"00-50-56-c0-00-08\",\"mac\":\"3c:91:57:ad:5a:49\",\"mno\":\"Coolpad SK1-01\",\"msisdn\":\"\",\"only\":\"866615020422273\",\"ptype\":\"YuLong\",\"reason\":\"\",\"sno\":\"usb#vid_1ebf&pid_702a&mi_01#6&235fd6af&0&0001\",\"userid\":\"1\",\"ver\":\"3.3.4.0\",\"winver\":\"win7\"},\"os\":\"win7\",\"token\": \"DC89DDD9E89B5BA87CA151139862C14B\",\"uid\":\"1\"}");
//		//设备安装
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"2020\",\"opdata\":{\"deviceid\":\"866615020422273\",\"devicename\":\"YuLong_Coolpad SK1-01\",\"devicetype\":\"0\",\"scount\":\"1\",\"starttime\":\"2015-06-24 17:02:14\",\"tcount\":\"1\",\"tickcount\":\"144\"},\"os\":\"win7\",\"token\":\"0BF7BB60A2F0ABA64050F6C132643ADD\",\"uid\":\"1\"}");
//		//应用激活
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"1011\",\"opdata\":{\"uid\":\"32\",\"appid\":\"5\",\"imei\":\"866615020422273\",\"imsi\":\"460110418522962\",\"installflag\":\"1\",\"itime\":\"2015-06-24 17:00:16\",\"mac\":\"3c:91:57:ad:5a:49\",\"mno\":\"Coolpad SK1-01\",\"ptype\":\"YuLong\",\"cp\":\"3\",\"sno\":\"usb#vid_1ebf&pid_702a&mi_01#6&235fd6af&0&0001\",\"userid\":\"1\",\"ver\":\"3.3.4.0\",\"winver\":\"win7\"},\"os\":\"win7\",\"token\": \"362BC643A26BAD57DA09CAF3BC8A25FF\",\"uid\":\"1\"}");
//		//检查用户账号是否冻结
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"1003\",\"os\":\"win7\",\"token\":\"2B0AE38890D02B82D23ABC44EE9751B0\",\"uid\":\"1\"}");
//		//问题反馈
////		String code = encode("{\"clientver\":\"3.3.4.0\",\"opcode\":\"1060\",\"opdata\":{\"contents\":\"test辅导书定积分\"},\"os\":\"win7\",\"token\":\"D6A763B0B3E963647535C6F047562F23\",\"uid\":\"1\"}");
//		//aPK激活
//		String code = encode("{\"uid\":\"37\",\"sn\":{\"idfv\":\"\",\"openudid\":\"\",\"mac\":\"90:4e:2b:ac:7c:9c\",\"udid\":\"\",\"idfa\":\"\"},\"devicetype\":\"0\",\"token\":\"\",\"deviceos\":\"4.2.2\",\"opdata\":{\"sno\":\"0123456789ABCDEF\",\"cp\":\"3\",\"mno\":\"HUAWEI Y511-T00\",\"itime\":\"2015-09-04 04:57:27\",\"imei\":\"861800002065416\",\"androidId\":\"f260df0cfa12ea7f\",\"appid\":\"18\",\"mac\":\"90:4e:2b:ac:7c:9c\",\"pno\":\"4.2.2\",\"ptype\":\"HUAWEI\"},\"opcode\":\"1001\"}");
//// 		String code = "eyJjbGllbnR2ZXIiOiIzLjMuNC4wIiwib3Bjb2RlIjoiMTAwMSIsIm9wZGF0YSI6eyJwd2QiOiJlMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZSIsInVzZXIiOiIzNjE5MzczNzhAcXEuY29tIn0sIm9zIjoid2luNyJ9Cg==";
////		System.out.println(decode(decodeByKey(code)));
////		System.out.println(code);
////		System.out.println(encodeByKey(code));
////		System.out.println(decodeByKey(encodeByKey(code)));
//		System.out.println("d=" + encodeByKey(code) + "&c=" + SecurityUtils.Get32CodeModel(encodeByKey(code)));
//	}

}