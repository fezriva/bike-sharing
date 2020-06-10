package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AverageTripDurationNDistanceReducer extends Reducer<Text, Text, Text, Text> {
	
	@Override
	public void reduce (Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException
	{
		double sumDuration = 0.0, avgDuration;
		double sumDistance = 0.0, avgDistance;
		double sumSubs = 0.0, subsRatio;
		int count = 0;
		
		for(Text value : values) {
			
			String[] line = value.toString().split(",");
			
			sumDuration += Double.parseDouble(line[0]);
			sumDistance += Double.parseDouble(line[1]);
			sumSubs += Double.parseDouble(line[2]);
			count++;
		}
		
		avgDuration = sumDuration / count;
		avgDistance = sumDistance / count;
		subsRatio = sumSubs / count * 100;
		
		context.write(key, new Text(avgDuration + "," + avgDistance + "," + Math.round(subsRatio)));
	}
}