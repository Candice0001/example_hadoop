package cn.itcast.hadoop.mapreduce.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
 * 这里就是mapReduce程序  ：mapper阶段实现的类
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 * 	KEYIN ：表示数据输入的时候数据类型，在默认的读取数据组件下，叫InputFormat，它是一行一行的读取待处理的数据
 * 		读取一行，返回一行给Mr程序，这种情况下，keyIn表示每一行的起始变量，数据类型是long
 * 
 * 	VALUEIN：表示数据输入的时候value的数据类型，在默认的读取数据组件下，valuein表示读取的这一行的内容，数据类型是string
 * 
 * 	KEYOUT:表示maper阶段数据输出时key的数据类型，在本例子中,输出的key是单词，数据类型:string
 * 
 * 	VALUEOUT：表示maper阶段数据输出时value的数据类型，在本例子中,输出的是单词的次数，数据类型:Integer
 * 
 * 	这里所说的数据类型是jdk自带的数据类型， 在序列化的时候，效率低下，因此hadoop自己封装了一套数据类型
 * 	long ---- LongWritable
 *  String ---- Text
 *  Integer --- Intwritable
 *  null --- Nullwritable
 * 
 * Title: WordCount.java
 * Description: 
 * @author chengge
 * @date 2018年8月20日
 *
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	/**
	 * 这里就是mapper阶段具体的业务逻辑实现方法， 该方法的调用取决于读取数据的组件有没有给mr程序传入参数
	 * 若有：每传入一个<k,v>对，该方法就会被调用一次
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		//1. 拿到传入进来的一行内容，把数据类型转化为String
		String line = value.toString();
		
		//2. 将这一行数据按照分隔符进行内容否切割
		String[] words = line.split(" ");
		
		//3.遍历数组，每出现一个单词就标记1
		//例如： hadoop hadoop spark
		for (String word : words) {
			//使用mr程序的上下文context,把mapper阶段处理的数据发送出去，作为reduce阶段的输入数据
			context.write(new Text(word), new IntWritable(1));
			//发送出去的kv对：<hadoop,1> <hadoop,1> <spark,1>
		}
		
		
	}
	
	

}
