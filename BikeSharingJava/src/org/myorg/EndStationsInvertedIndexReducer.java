package org.myorg;

import java.util.ArrayList;
import java.util.Comparator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class EndStationsInvertedIndexReducer extends Reducer<Text, Text, Text, Text> 
{
	protected void reduce(Text key, Iterable<Text> values, Context context)
		throws java.io.IOException, InterruptedException 
	{
		ArrayList<String> bufferStations = new ArrayList<String>();
		
		for (Text value : values) {
			
			String[] splitValue = value.toString().split("\n");
			
			if (splitValue[0].equals("{")) {
				
				int i = 1;
				
				while (!(splitValue[i].equals("}"))) {
					
					String[] stationInfo = splitValue[i].split(",");
					
					//0 sID, 1 sName, 2 sNum
					
					int index = -1;
					for (int j = 0; j < bufferStations.size(); j++) {
						if (bufferStations.get(j).startsWith(stationInfo[0] + "," + stationInfo[1])) {
							index = j;
							j = bufferStations.size();
						}
					}			
					
					if (index == -1) {
						bufferStations.add(splitValue[i].toString());
					} else {
						stationInfo = bufferStations.remove(index).split(",");
						int updateValue = Integer.parseInt(stationInfo[2]) + 1;
						String newValue = stationInfo[0] + "," + stationInfo[1] + "," + updateValue;
						bufferStations.add(index, newValue);
					}
					
					i++;	
				}
			}
			
		}
		
		bufferStations.sort(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				String[] splitStr1 = str1.split(",");
				String[] splitStr2 = str2.split(",");
				return Integer.parseInt(splitStr2[2]) - Integer.parseInt(splitStr1[2]);
			}
		});
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\n");
		for (String station : bufferStations) {
			buffer.append(station.toString());
			buffer.append("\n");
		}
		buffer.append("}");
		
		Text documentList = new Text();
		documentList.set(buffer.toString());
		context.write(key, documentList);
	}
}