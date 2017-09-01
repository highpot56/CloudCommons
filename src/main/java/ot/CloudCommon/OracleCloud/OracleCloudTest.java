package ot.CloudCommon.OracleCloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import oracle.cloud.storage.CloudStorage;
import oracle.cloud.storage.CloudStorageConfig;
import oracle.cloud.storage.CloudStorageFactory;
import oracle.cloud.storage.model.StorageInputStream;

/**
 * Hello world!
 *
 */
public class OracleCloudTest
{
    public static void main( String[] args )
    {
    	try{
//    		test01();	// ダウンロードテスト
    		test02();	// アップロードテスト
    	}catch(Exception e){
    		System.out.println("err:" + e.getMessage());
    	}
//        System.out.println( "Hello World!" );
    }

    // proxy環境の場合は、
    // VM引数にproxy情報を設定する
    // -Dhttp.proxyHost=mtc-px14 -Dhttp.proxyPort=8081
    // -Dhttps.proxyHost=mtc-px14 -Dhttps.proxyPort=8081

    // 認証処理
    private static CloudStorage authCloud() throws Exception{
    	CloudStorageConfig myConfig = new CloudStorageConfig();
    	myConfig.setServiceName("Storage-a514074")
    	.setUsername("highpot89@gmail.com")
    	.setPassword("Tuna1234".toCharArray())
    	.setServiceUrl("https://a514074.storage.oraclecloud.com");

    	CloudStorage myConnection = CloudStorageFactory.getStorage(myConfig);
    	return myConnection;
    }

    // アップロードテスト
    public static void test01() throws Exception{
    	CloudStorage myConnection = authCloud();

    	String filePath = "C:/Users/opengion/Desktop/test2.txt";
    	FileInputStream fis = new FileInputStream(filePath);

    	String container = "Archive";
    	String name = "test2.txt";

    	myConnection.storeObject(container, name, "text/plain", fis);

//    	myConnection.object
    }

    // ダウンロードテスト
    public static void test02() throws Exception{
    	CloudStorage myConnection = authCloud();

    	String container = "Archive";
//    	String container = "Standard";
    	String name = "test2.txt";

    	StorageInputStream sis = myConnection.retrieveObject(container, name);

    	FileOutputStream fos = new FileOutputStream(new File("H:/opengion_user/tmp/test01/oc01.txt"));

    	IOUtils.copy(sis, fos);

    	fos.close();
    	sis.close();
    }
}
