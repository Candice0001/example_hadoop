package cn.itcast.hadoop.mapreduce.flowsum;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class FlowBean implements WritableComparable<FlowBean> {

	private long upFlow;// 上行流量

	private long downFlow;// 下行流量

	private long sumFlow;// 总流量

	
	public FlowBean() {
		super();
	}
	
	public FlowBean(long upFlow, long downFlow) {
		super();
		this.upFlow = upFlow;
		this.downFlow = downFlow;
	}

	public FlowBean(long upFlow, long downFlow, long sumFlow) {
		super();
		this.upFlow = upFlow;
		this.downFlow = downFlow;
		this.sumFlow = sumFlow;
	}

	// 这是序列化方法
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(upFlow);
		out.writeLong(downFlow);
		out.writeLong(sumFlow);
	}

	// 这是反序列化方法
	//反序列化是需注意 ： 注意序列化的顺序
	//先序列化的先出來 
	@Override
	public void readFields(DataInput in) throws IOException {
		this.upFlow = in.readLong();
		this.downFlow = in.readLong();
		this.sumFlow = in.readLong();
		
	}
	
	//设置一个set方法
	public void set(long upFlow, long downFlow) {
		this.upFlow = upFlow;
		this.downFlow = downFlow;
		this.sumFlow = upFlow + downFlow;
	}


	public long getUpFlow() {
		return upFlow;
	}

	public void setUpFlow(long upFlow) {
		this.upFlow = upFlow;
	}

	public long getDownFlow() {
		return downFlow;
	}

	public void setDownFlow(long downFlow) {
		this.downFlow = downFlow;
	}

	public long getSumFlow() {
		return sumFlow;
	}

	public void setSumFlow(long sumFlow) {
		this.sumFlow = sumFlow;
	}

	/**
	 * 此方法很重要，不然输出结果中输出的是一个对象；
	 */
	@Override
	public String toString() {
		return upFlow + "\t" + downFlow + "\t" + sumFlow;
	}

	/**
	 * 这里就是bean对象比较大小的方法
	 * 倒序
	 */
	@Override
	public int compareTo(FlowBean o) {
		//倒序
		return this.sumFlow > o.getSumFlow() ? -1:1;
		//正序
		//return this.sumFlow > o.getSumFlow() ? 1:-1;
	}
	
	

}
