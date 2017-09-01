package ot.CloudCommon.bluemix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.storage.ObjectStorageService;
import org.openstack4j.core.transport.Config;
import org.openstack4j.core.transport.ProxyHost;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.openstack.OSFactory;

/**
 * Bluemixｵﾌﾞｼﾞｪｸﾄｽﾄﾚｰｼﾞ接続ﾃｽﾄ
 *
 */
public class BluemixTest
{
	// BluemixObjectStorage認証情報
	// ﾕｰｻﾞ名
	private static final String USERNAME = "[userName]";
	// ﾊﾟｽﾜｰﾄﾞ
	private static final String PASSWORD = "[password]";
	// ﾄﾞﾒｲﾝID
	private static final String DOMAIN_ID = "[domainId]";
	// ﾌﾟﾛｼﾞｪｸﾄID
	private static final String PROJECT_ID = "[projectId]";

    public static void main( String[] args )
    {
    	try{
    		// ダウンロード
    		bluemixDonwload();
    		// アップロード
//    		bluemixUpload();
    	}catch(Exception e){
    		System.out.println("err:" + e.getMessage());
    	}
        System.out.println("END");
    }

    // 認証処理
	private static ObjectStorageService authenticateAndGetObjectStorageService() {
		String OBJECT_STORAGE_AUTH_URL = "https://identity.open.softlayer.com/v3";

		System.out.println("Authenticating...");

		// Proxyの設定(ローカルから実行の場合は必要)
		ProxyHost proxy = ProxyHost.of("http://mtc-px14", 8081);
		Config config = Config.newConfig().withProxy(proxy);

		// 認証処理
		OSClientV3 os = OSFactory.builderV3()
				.endpoint(OBJECT_STORAGE_AUTH_URL)
				.withConfig(config)
				.credentials(USERNAME,PASSWORD, Identifier.byId(DOMAIN_ID))
				.scopeToProject(Identifier.byId(PROJECT_ID))
				.authenticate();

		System.out.println("Authenticated successfully!");

		ObjectStorageService objectStorage = os.objectStorage();

		return objectStorage;
	}

	// ダウンロードテスト
	private static void bluemixDonwload() throws Exception{
		ObjectStorageService oss = authenticateAndGetObjectStorageService();

		// ｺﾝﾃﾅ名
		String container = "javatest";
		// ﾌｧｲﾙ名
		String name = "test01.txt";

		// ｺﾝﾃﾅ名,ﾌｧｲﾙ名を渡すことでﾀﾞｳﾝﾛｰﾄﾞ(ｽﾄﾘｰﾑに変換可)
		SwiftObject so = oss.objects().get(container,name);
		DLPayload payload = so.download();
		InputStream is = payload.getInputStream();

		// ﾀﾞｳﾝﾛｰﾄﾞ先
		FileOutputStream fos = new FileOutputStream(new File("H:/opengion_user/tmp/test01/bmtest.txt"));

		IOUtils.copy(is, fos);

		fos.close();
		is.close();
	}

	// アップロードテスト
	private static void bluemixUpload() throws Exception{
		ObjectStorageService oss = authenticateAndGetObjectStorageService();

		// ｱｯﾌﾟﾛｰﾄﾞ対象ﾌｧｲﾙ
		FileInputStream fis = new FileInputStream(new File("H:/opengion_user/tmp/test01/oc01.txt"));

		Payload<FileInputStream> payload = new FileInputPayload(fis);
		String containerName = "javatest";
		String fileName = "test02.txt";
		// ｺﾝﾃﾅ名,ﾌｧｲﾙ名,streamを渡すことでｱｯﾌﾟﾛｰﾄﾞ
		oss.objects().put(containerName,fileName,payload);
	}

	// FileInputStream用のPayloadｸﾗｽ
	public static class FileInputPayload implements Payload<FileInputStream> {
		private FileInputStream stream = null;

		public FileInputPayload(FileInputStream stream) {
			this.stream = stream;
		}

		@Override
		public void close() throws IOException {
			stream.close();
		}

		@Override
		public InputStream open() {
			return stream;
		}

		@Override
		public void closeQuietly() {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}

		@Override
		public FileInputStream getRaw() {
			return stream;
		}

	}
}
