package ot.CloudCommon.Azure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Include the following imports to use blob APIs.
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * Hello world!
 *
 */
public class App
{
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" +
			"AccountName=[AccountName];" +
			"AccountKey=[AcountKey]";

	private static final String containerName = "[ContainerName]";

    public static void main( String[] args )
    {
    	try{
//    		auth();
//    		upload();
    		download();
    	}catch(Exception e){
    		e.printStackTrace();
    	}

        //System.out.println( "Hello World!" );
    }

    // 認証処理
    private static CloudBlobContainer auth() throws Exception{
    	System.out.println("auth start");

//        String storageConnectonString = "";

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        // container.createIfNotExists();

        System.out.println("auth end");
        return container;
    }

    // ｱｯﾌﾟﾛｰﾄﾞ処理
    private static void upload() throws Exception{
    	CloudBlobContainer container = auth();

    	// ｱｯﾌﾟﾛｰﾄﾞ処理
    	String uploadName = "test.txt";
    	CloudBlockBlob blockBlob = container.getBlockBlobReference(uploadName);

    	File file = new File("C:/Users/opengion/Downloads/testfile.txt");
    	FileInputStream fis = new FileInputStream(file);

    	blockBlob.upload(fis, file.length());

    	fis.close();
    }

    // ﾀﾞｳﾝﾛｰﾄﾞ処理
    private static void download() throws Exception{
    	CloudBlobContainer container = auth();

    	// ﾀﾞｳﾝﾛｰﾄﾞ処理
    	String uploadName = "test.txt";
    	CloudBlockBlob blockBlob = container.getBlockBlobReference(uploadName);

    	String downloadPath = "C:/Users/opengion/Downloads/download.txt";
    	FileOutputStream fos = new FileOutputStream(new File(downloadPath));
    	blockBlob.download(fos);

    	fos.close();
    }
}
