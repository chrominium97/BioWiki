package kr.kdev.dg1s.biowiki.networking;

import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CachedDownloader {

    File root;
    int identifier;

    public CachedDownloader(int id) {
        root = android.os.Environment.getDataDirectory();
        identifier = id;
    }

    public void deleteFile(String fileName, String fileType) {

        File dir = new File(root.getAbsolutePath() + "/", fileType);

        File file = new File(dir, "." + fileName);
        file.delete();
        file = new File(dir, fileName);
        file.delete();

    }

    public int forceRedownload(String DownloadUrl, String fileName, String fileType) {
        deleteFile(fileName, fileType);
        prepareFile(DownloadUrl, fileName, fileType);
        return identifier;
    }

    public int prepareFile(String DownloadUrl, String fileName, String fileType) {
        File dir = new File(root.getAbsolutePath() + "/", fileType);
        File file = new File(dir, "." + fileName);
        if (file.exists()) {
            Log.d("DownloadManager", "already downloaded");
            return identifier;
        } else {
            downloadFromUrl(DownloadUrl, fileName, fileType);
            return identifier;
        }
    }

    public void downloadFromUrl(String DownloadUrl, String fileName, String fileType) {

        try {

            File dir = new File (root.getAbsolutePath() + "/" + fileType);
            if(dir.exists()==false) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();
            Log.d("DownloadManager", "download beginning");
            Log.d("DownloadManager", "download url:" + url);
            Log.d("DownloadManager", "downloaded file name:" + fileName);

           /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }


           /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();

            File UAG = new File(dir, "." + fileName);
            UAG.createNewFile();

            Log.d("DownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
        }

    }
}
