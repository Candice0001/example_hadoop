package cn.itcast.hadoop.hdfs;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

public class TestHdfsClient {
	public static void main(String[] args) throws Exception {
		//createDir();
		
		//downloadFile();
		
		//uploadFile();
		
		testUpload_steam();
	}
	
	// 创建一个目录
	public static void createDir() throws IOException {

		Configuration conf = new Configuration();

		// 这里指定使用的是hdfs文件系统
		conf.set("fs.defaultFS", "hdfs://192.168.41.140:9000");

		// 方式一：设置Java客户端访问hdfs的身份
		System.setProperty("HADOOP_USER_NAME", "hadoop");

		// 通过FileSystem的静态方法获取文件系统客户端对象
		FileSystem fs = FileSystem.get(conf);
		// 方式二： 通过如下方式指定文件系统的类型 并且同时设置用户身份
		// FileSystem fs = FileSystem.get(new
		// URI("hdfs://192.168.41.140:9000"),conf,"hadoop");

		// 创建一个目录
		//fs.create(new Path("/helloDfs"));
		
		//1.创建目录
		//fs.mkdirs(new Path("/a/b/c"));
		
		//2.删除文件夹，如果是非空文件夹，第二个参数必须为true(直接删除)
		//fs.delete(new Path("/aaa"),true);
		
		//3.文件夹的重命名 rename
		//fs.rename(new Path("/a1"), new Path("/a2"));
		
		//列出文件
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
		while(listFiles.hasNext()) {
			LocatedFileStatus fileStatus = listFiles.next();
			
			if (fileStatus.isFile()) {
				System.out.println("file---");
			}else {
				System.out.println("dir----");
			}
			
			System.out.println(fileStatus.getPath().getName());
			System.out.println(fileStatus.getBlockSize());
			System.out.println(fileStatus.getPermission());
			System.out.println(fileStatus.getLen());
			BlockLocation[] blockLocation = fileStatus.getBlockLocations();
			for (BlockLocation bl : blockLocation) {
				System.out.println("block-length:"+bl.getLength()+"-"+"block-offset:"+bl.getOffset());
				
			}
		}
		
		fs.close();

	}
	
	public static void downloadFile() throws IOException {

		Configuration conf = new Configuration();

		// 这里指定使用的是hdfs文件系统
		conf.set("fs.defaultFS", "hdfs://192.168.41.140:9000");

		// 方式一：设置Java客户端访问hdfs的身份
		System.setProperty("HADOOP_USER_NAME", "hadoop");

		// 通过FileSystem的静态方法获取文件系统客户端对象
		FileSystem fs = FileSystem.get(conf);
		// 方式二： 通过如下方式指定文件系统的类型 并且同时设置用户身份
		// FileSystem fs = FileSystem.get(new
		// URI("hdfs://192.168.41.140:9000"),conf,"hadoop");

		// 下载文件到本地 注意：需要配置本地hadoop环境，不然报错
		fs.copyToLocalFile(new Path("/helloDfs"), new Path("D://"));

		fs.close();
		
	}

	public static void uploadFile() throws IOException {
		Configuration conf = new Configuration();

		// 这里指定使用的是hdfs文件系统
		conf.set("fs.defaultFS", "hdfs://192.168.41.140:9000");

		// 方式一：设置Java客户端访问hdfs的身份
		System.setProperty("HADOOP_USER_NAME", "hadoop");

		// 通过FileSystem的静态方法获取文件系统客户端对象
		FileSystem fs = FileSystem.get(conf);
		// 方式二： 通过如下方式指定文件系统的类型 并且同时设置用户身份
		// FileSystem fs = FileSystem.get(new
		// URI("hdfs://192.168.41.140:9000"),conf,"hadoop");

		//上传文件
		Path src = new Path("d:/testupload.txt");
		Path dst = new Path("/");
		fs.copyFromLocalFile(src, dst);

		fs.close();
		
	}
	
	/**
	 * 流式操作
	 * 使用stream的形式操作hdfs，这是一个更底层的操作
	 * @throws URISyntaxException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void testUpload_steam() throws IOException, InterruptedException, URISyntaxException {
		Configuration conf = new Configuration();

		// 这里指定使用的是hdfs文件系统
		conf.set("fs.defaultFS", "hdfs://192.168.41.140:9000");
		
		FileSystem fs = FileSystem.get(new URI("hdfs://192.168.41.140:9000"),conf,"hadoop");
		
		FSDataOutputStream outputStream = fs.create(new Path("/a.txt"),true);
		
		FileInputStream inputStream = new FileInputStream("D:/a.txt");
		IOUtils.copy(inputStream, outputStream);
		fs.close();
		
	}
	
	/**
	 * 案例：shell 定时采集数据至HDFS
	 * 
	 * 	周期性实时上传文件
	 */
	
}
