package cn.itcast.hadoop.mapreduce.flowsum.partitioner;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import cn.itcast.hadoop.mapreduce.flowsum.FlowBean;
import cn.itcast.hadoop.mapreduce.flowsum.FlowSumDriver;

/***
 * 	将流量汇总统计结果按照手机号归属地不同省份输出到不同的文件中
 * 
 * Title: FlowSumProvince.java
 * Description: 
 * @author chengge
 * @date 2018年8月26日
 *
 */
public class FlowSumProvince {
	
	public static class FlowSumProvinceMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

		Text k = new Text();
		FlowBean v = new FlowBean();
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context)
				throws IOException, InterruptedException {

			String line = value.toString();//一行一行读取的
			System.out.println("line:"+line);
			
			String[] fields = line.split(", ");//返回的是字段的数组,注意分隔符
			
			String phonenum = fields[1];
			
			long upFlow = Long.parseLong(fields[fields.length-3]);
			
			long downFlow = Long.parseLong(fields[fields.length-2]);
			
			k.set(phonenum);
			v.set(upFlow, downFlow);
			
			//context.write(new Text(phonenum),new FlowBean(upFlow,downFlow));
			context.write(k,v);
		}
	}
	
	public static class FlowSumProvinceReducer extends Reducer<Text, FlowBean, Text, FlowBean>{
		
		FlowBean v = new FlowBean();
		
		@Override
		protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context)
				throws IOException, InterruptedException {
			
			long upFlowCount = 0; //总上行流量
			long downFlowCount = 0; //总下行流量
			
			//迭代
			for (FlowBean flowBean : values) {
				upFlowCount += flowBean.getUpFlow();
				downFlowCount += flowBean.getDownFlow();
			}
			
			v.set(upFlowCount, downFlowCount);
			
			context.write(key, v);
			
		}
		
	}
	
	private static Logger logger = Logger.getLogger(FlowSumDriver.class);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		logger.info("starting....");
		
		// 通过job来封装本次mr的相关信息
		Configuration conf = new Configuration();
		// 设置本地运行模式
		conf.set("mapreduce.framework.name", "local");

		Job job = Job.getInstance();

		// 指定本次mr job jar运行的主类
		job.setJarByClass(FlowSumProvince.class);

		// 指定本次mr 所用的mapper reducer类分别是什么
		job.setMapperClass(FlowSumProvinceMapper.class);
		job.setReducerClass(FlowSumProvinceReducer.class);

		// 指定本次mr mapper阶段的输出 k v 类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		// 指定本次mr 最终输出的k v 类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);

		//设置ReduceTasks个数 5 ，和 privincePartitioner里面的一致
		//分区个数 == ReducerTask个数 正常执行
		//分区个数 < ReducerTask个数:正常执行 ，只不过会有空的结果文件产生
		//分区个数 > reduceTask个数：无法正常执行         错误：Ilegal partition
		job.setNumReduceTasks(5);
		//指定使用我们自定义的分区组件
		job.setPartitionerClass(ProvincePartitioner.class);

		// 指定mr 输入的数据路径，和最终输出结果存放在什么位置
		FileInputFormat.setInputPaths(job, "D:/studyPro/studyhadoop/flowsum/input");
		FileOutputFormat.setOutputPath(job, new Path("D:/studyPro/studyhadoop/flowsumprovince/output"));

		// 提交
		// job.submit();

		// 提交程序，并监控和打印程序执行情况
		boolean flag = job.waitForCompletion(true);
		// 退出程序
		System.exit(flag ? 0 : 1);
		logger.info("ending....");
	}
	

}
