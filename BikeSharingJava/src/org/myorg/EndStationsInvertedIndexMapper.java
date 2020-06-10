package org.myorg;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EndStationsInvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> 
{
	private Text startStationID = new Text();
	private Text endStationID = new Text();

    @Override
	protected void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException 
	{
    	String[] line = value.toString().split(",");
		startStationID.set(line[3] + "," + line[4]);
		endStationID.set(line[7] + "," + line[8]);
		
		context.write(endStationID, new Text ("{\n" + startStationID.toString() + ",1\n}"));
		
	}
}