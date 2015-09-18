package com.zhihui.quicksearch.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class SearchHttp
{

	private static final String tag = SearchHttp.class.getSimpleName();
	public static final int TIMEOUT = 1000 * 30;

	private static DefaultHttpClient getClient()
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);

		return httpClient;
	}

	public static String post(String url, List<NameValuePair> params)
	{
		InputStream ins = doAction(url, params, true);
		return streamToString(ins);
	}

	public static String streamToString(InputStream ins)
	{
		if (ins == null)
			return null;
		InputStreamReader br = new InputStreamReader(ins);
		StringBuffer result = new StringBuffer();
		char[] buf = new char[1024 * 4];
		int r = 0;
		try
		{
			while ((r = br.read(buf)) > 0)
				result.append(buf, 0, r);
			return result.toString();
		} catch (IOException e)
		{
		}
		return null;
	}

	public static InputStream doAction(String url, List<NameValuePair> data,
			boolean gzip)
	{
		HttpResponse response = null;
		HttpUriRequest request = null;
		try
		{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
			request = httpPost;
			if (gzip)
			{
				request.addHeader("Accept-Encoding", "gzip");
			}
			response = getClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				InputStream ins = response.getEntity().getContent();
				if (contentEncoding != null && gzip
						&& contentEncoding.getValue().equalsIgnoreCase("gzip"))
				{
					return new GZIPInputStream(ins);
				} else
				{
					return ins;
				}
			} else
			{
				// markVisitError();
				return null;
			}
		} catch (Exception e)
		{
			// markVisitError();			
			Log.e("liao","", e);
			
		};
		return null;
	}

	// private static void markVisitError()
	// {
	// String content = FileUtils.readFile(SERIAL);
	// content = Eryptogram.decryptData(content);
	// if (content == null && "".equals(content))
	// return;
	// String[] str = content.split(";");
	// if (str != null && str.length == 6)
	// {
	// try
	// {
	// long last = Long.valueOf(str[0]);
	// long cycle = Long.valueOf(str[1]);
	// String base = str[2];
	// String backup = str[3];
	// long fail_times = Long.valueOf(str[4]) + 1;
	// long check_times = Long.valueOf(str[5]);
	// content = last + ";" + cycle + ";" + base + ";" + backup
	// + ";" + fail_times + ";" + check_times;
	// FileUtils.saveFile(SERIAL, Eryptogram.encryptData(content));
	// } catch (Exception e)
	// {
	// }
	// }
	// }

	public static boolean doDownload(String remote, FileOutputStream outStream)
	{
		InputStream is = null;
		boolean flag = false;
		try
		{
			URL urlConn = new URL(remote);
			URLConnection conn = urlConn.openConnection();
			is = conn.getInputStream();
			int c = 0;
			byte[] buffer = new byte[1024];
			while ((c = is.read(buffer)) != -1)
			{
				outStream.write(buffer, 0, c);
			}
			flag = true;
		} catch (Exception e)
		{
			flag = false;
		} finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
				if (outStream != null)
				{
					outStream.close();
				}
			} catch (IOException e)
			{
			}
		}
		return flag;
	}

	public static String postToHttp(String url, List<NameValuePair> paras)
	{
		return postToHttp(url, paras, TIMEOUT);
	}

	public static String postToHttp(String url,
			List<NameValuePair> paras, int timeout)
	{

		HttpPost request = new HttpPost(url);
		HttpConnectionParams.setConnectionTimeout(request.getParams(), timeout);
		HttpConnectionParams.setSoTimeout(request.getParams(), timeout);
		String msg = null;
		try
		{
			request.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				msg = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e)
		{

			// MyUtils.logInfo(tag, e.getMessage());

			return null;
		}
		return msg;
	}

	public static String postnoParam(String strurl) {
        String result = null;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            url = new URL(strurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            DataOutputStream dop = new DataOutputStream(
                    connection.getOutputStream());
            dop.writeBytes("token=alexzhou");
            dop.flush();
            dop.close();
 
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
 
        }
        return result;
    }
}
