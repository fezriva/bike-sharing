package org.myorg;

import java.io.IOException;
import java.util.Calendar;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AverageTripDurationNDistanceMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Text durationNDistance = new Text();
	private Text user = new Text();
	
	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{
		String[] line = value.toString().split(",");
		
		String ageTarget = new String();
		String genderTarget = new String();
		
		try {
			
			int userAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(line[13]);
			
			//https://support.google.com/google-ads/answer/2580383?hl=it
			//Google age targets
			
			if (userAge < 18) {
				ageTarget = "under 18";
			} else if (userAge >= 18 && userAge < 25) {
				ageTarget = "18-24";
			} else if (userAge >= 25 && userAge < 35) {
				ageTarget = "25-34";
			} else if (userAge >= 35 && userAge < 45) {
				ageTarget = "35-44";
			} else if (userAge >= 45 && userAge < 55) {
				ageTarget = "45-54";
			} else if (userAge >= 55 && userAge < 65) {
				ageTarget = "55-64";
			} else if (userAge >= 65) {
				ageTarget = "over 65";
			}
			
			genderTarget = line[14];
			
	    } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
	    	
	    	ageTarget = "Unknown";
	    	genderTarget = "Unknown";
	    	
	    }
		
		user.set(genderTarget + "," + ageTarget);
		
		double distance = EarthDist.distance(Double.parseDouble(line[5]),Double.parseDouble(line[9]),Double.parseDouble(line[6]),Double.parseDouble(line[10]),0.0,0.0);
		int duration = Integer.parseInt(line[0].trim());
		if (line[12].trim().equals("Subscriber")) {
			durationNDistance.set(duration + "," + distance + ",1");
		} else {
			durationNDistance.set(duration + "," + distance + ",0");
		}
		
		context.write(user, durationNDistance);
		
	}
}