package com.zhihui.quicksearch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsynLoadimg {
	File cache;
	public void asyncloadImage(ImageView iv_header, String path, File cache) {
		this.cache = cache;
      AsyncImageTask task = new AsyncImageTask(iv_header);
      task.execute(path);
  }

  private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {

      private ImageView iv_header;

      public AsyncImageTask(ImageView iv_header) {
          this.iv_header = iv_header;
      }

      // 后台运行的子线程子线程
      @Override
      protected Uri doInBackground(String... params) {
          try {
              return getImageURI(params[0], cache);
          } catch (Exception e) {
              e.printStackTrace();
          }
          return null;
      }

      // 这个放在在ui线程中执行
      @Override
      protected void onPostExecute(Uri result) {
          super.onPostExecute(result); 
          // 完成图片的绑定
          if (iv_header != null && result != null) {
              iv_header.setImageURI(result);
          }
      }
  }
  
  /*
   * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
   * 这里的path是图片的地址
   */
  public Uri getImageURI(String path, File cache) throws Exception {
      String name = MD5.getMD5(path) + path.substring(path.lastIndexOf("."));
      File file = new File(cache, name);
      // 如果图片存在本地缓存目录，则不去服务器下载 
      if (file.exists()) {
          return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
      } else {
          // 从网络上获取图片
          URL url = new URL(path);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setConnectTimeout(5000);
          conn.setRequestMethod("GET");
          conn.setDoInput(true);
          if (conn.getResponseCode() == 200) {

              InputStream is = conn.getInputStream();
              FileOutputStream fos = new FileOutputStream(file);
              byte[] buffer = new byte[1024];
              int len = 0;
              while ((len = is.read(buffer)) != -1) {
                  fos.write(buffer, 0, len);
              }
              is.close();
              fos.close();
              // 返回一个URI对象
              return Uri.fromFile(file);
          }
      }
      return null;
  }
}
