package jp.co.goalist.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3Handler {
	public static void main(String[] args){
        
		String region = "ap-northeast-1"; //リージョン
        String bucketName = "goalist-dev-sandbox"; //バケット名
        String keyName = "oz-training/media_mst.csv"; //オブジェクトキー
        String dest = "/training/media_mst2.csv"; //ダウンロード先のファイルパス
        
        downloadObject(region, keyName, bucketName, dest); 
       
        System.out.println("Done!");
    }
	
	private static void downloadObject(String region, String keyName, String bucketName, String dest) {
		
		 System.out.format("Downloading %s from S3 bucket %s...\n", keyName, bucketName);
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	                .withRegion(region) //リージョンをセット
	                .withCredentials(new ProfileCredentialsProvider())
	                .build();

	        try {
	            S3Object o = s3Client.getObject(bucketName, keyName);
	            S3ObjectInputStream s3is = o.getObjectContent(); 
	            FileOutputStream fos = new FileOutputStream(new File(dest));
	            byte[] read_buf = new byte[1024];
	            int read_len = 0;
	            while ((read_len = s3is.read(read_buf)) > 0) {
	                fos.write(read_buf, 0, read_len); 
	            }
	            s3is.close();
	            fos.close();
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getErrorMessage());
	            System.exit(1);
	        } catch (FileNotFoundException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        }
	}
}
