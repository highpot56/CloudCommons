package ot.CloudCommon.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

/**
 * Hello world!
 *
 */
public class App
{
	static final String S3_ACCESS_KEY			= "";
	static final String S3_SECRET_KEY			= "";
	static final String S3_SERVICE_END_POINT	= "";
	static final String S3_REGION				= "";
	static final String S3_BUCKET_NAME			= "";

    public static void main( String[] args )
    {
    	try{
    		upload();
    		download();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
//        System.out.println( "Hello World!" );
    }

    // 認証処理
    private static AmazonS3 auth(){
    	System.out.println("auth start");

    	// AWSの認証情報
    	AWSCredentials credentials = new BasicAWSCredentials(S3_ACCESS_KEY, S3_SECRET_KEY);

    	// クライアント設定
    	ClientConfiguration clientConfig = new ClientConfiguration();
    	clientConfig.setProtocol(Protocol.HTTPS);	// HTTPS?
    	clientConfig.setProxyHost("mtc-px14");
    	clientConfig.setProxyPort(8081);
    	clientConfig.setConnectionTimeout(10000);

    	// エンドポイント設定
    	EndpointConfiguration endpointConfiguration = new EndpointConfiguration(S3_SERVICE_END_POINT,  S3_REGION);

    	// S3アクセスクライアントの生成
    	AmazonS3 client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
    			.withClientConfiguration(clientConfig)
    			.withEndpointConfiguration(endpointConfiguration).build();

    	System.out.println("auth end");
    	return client;
    }

    // ｱｯﾌﾟﾛｰﾄﾞ処理
    private static void upload() throws Exception{
    	System.out.println("upload start");

    	AmazonS3 client = auth();

    	//File file = new File("C:/Users/opengion/Downloads/testdesu.txt");
    	File file = new File("C:/Users/opengion/Downloads/test.xls");
    	FileInputStream fis = new FileInputStream(file);


    	ObjectMetadata om = new ObjectMetadata();
    	om.setContentLength(file.length());
    	//final PutObjectRequest putRequest = new PutObjectRequest(S3_BUCKET_NAME, file.getName(), file);
    	final PutObjectRequest putRequest = new PutObjectRequest(S3_BUCKET_NAME, file.getName(), fis, om);

    	// 権限の設定
    	putRequest.setCannedAcl(CannedAccessControlList.PublicRead);

    	// アップロード
    	client.putObject(putRequest);

    	fis.close();

    	System.out.println("upload end");
    }

    // ﾀﾞｳﾝﾛｰﾄﾞ処理
    private static void download() throws Exception{
    	System.out.println("download start");

    	AmazonS3 client = auth();

    	final GetObjectRequest getRequest = new GetObjectRequest(S3_BUCKET_NAME, "test.xls");

    	S3Object object = client.getObject(getRequest);

    	FileOutputStream fos = new FileOutputStream(new File("C:/Users/opengion/Downloads/aswout.xls"));
    	IOUtils.copy(object.getObjectContent(), fos);

    	fos.close();

    	System.out.println("download end");
    }
}
