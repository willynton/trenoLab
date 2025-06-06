package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public final class Utils {
	
    private Utils() { }
    
    
	public static boolean validatorExistFile(String pathFile) {
		
		String file=pathFile.trim();
		File f = new File(file);

		if(f.exists()) {
			return true;
		}else {
			return false;
		}
		
	}	
	

	public static HashMap<String, String> popolateHashMap(String pathFile) {
		
		HashMap<String, String> result = new HashMap<>();
		
		boolean isFirstLine = true;
				
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	
                if (isFirstLine) {
                	isFirstLine = false; // jump first line
                    continue;
                }            	
            	
                String[] values = line.split(","); 
                if (values.length >= 3) { // Make sure there are at least three values: 2 for key and 1 for value               	
                	
                	result.put(values[0].trim()+' '+values[1].trim(), values[2].trim()); // The first and second value is the key, the third is the value
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }		

		return result;
	}
	
	

	public static HashMap<String, Integer> generateDelayHashMap(HashMap<String, String> plannedHashMap, HashMap<String, String> actualHashMap) {
		
		HashMap<String, Integer> delayHashMap = new HashMap<>();
		
        LocalDateTime localDateTimePlanned;
        LocalDateTime localDateTimeActual;
        OffsetDateTime offsetDateTime;
        Duration duration;		
		
		String plannedTime="";
		String actualTime="";
		String delaySeconds = "";
		
		String[] key = new String[2];
        
        for (Map.Entry<String, String> entryPlanned : plannedHashMap.entrySet()) {
        	
        	plannedTime = entryPlanned.getValue();
        	actualTime = actualHashMap.get(entryPlanned.getKey());
        	
        	offsetDateTime = OffsetDateTime.parse(plannedTime);
        	localDateTimePlanned = offsetDateTime.toLocalDateTime();
        	
        	offsetDateTime = OffsetDateTime.parse(actualTime);
        	localDateTimeActual = offsetDateTime.toLocalDateTime();
        	
        	duration = Duration.between(localDateTimePlanned, localDateTimeActual);
        	delaySeconds = String.valueOf(duration.getSeconds());
        	
        	key = entryPlanned.getValue().split(Constants.separatorDataTime); // Split using a 'T'
        	plannedTime = key[1];
        	
        	key = actualHashMap.get(entryPlanned.getKey()).split(Constants.separatorDataTime);
        	actualTime = key[1];
        	
        	delayHashMap.put(entryPlanned.getKey(), Integer.parseInt(delaySeconds));
        	            
        }		

		return delayHashMap;
	}	
	
	
    public static boolean createCsv(String nameFile, HashMap<String, String> plannedHashMap, HashMap<String, String> actualHashMap,  LinkedHashMap<String, Integer> linkedHashMap) {
    	
        String treno = "";
        String stazione = "";
		String plannedTime="";
		String actualTime="";
    	
		String[] key = new String[2];
		int counterTrains=0;
		
        try (FileWriter writer = new FileWriter(nameFile)) {
            // Intestazione
            writer.write(Constants.intestazioneOutputCsv + "\n");
            
            Set<String> treniInRitardo = new HashSet<>();

            for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            	
            	key = entry.getKey().split(Constants.separatorTrenoStazione); // Split using a space
            	treno = key[0];
            	stazione = key[1];  
            	
            	          	
            	key = plannedHashMap.get(entry.getKey()).split(Constants.separatorDataTime); // Split using a 'T'
            	plannedTime = key[1];
            	
            	key = actualHashMap.get(entry.getKey()).split(Constants.separatorDataTime); // Split using a 'T'
            	actualTime = key[1];            	
            	
            	
        		if (!treniInRitardo.contains(treno)) {
        			 treniInRitardo.add(treno);
            	     writer.write(treno + "," + stazione + "," + plannedTime + "," + actualTime +"," + entry.getValue() + "\n");
                            
                     counterTrains++;            	            
            	}            		
            	                
                if(counterTrains>=Constants.totaleTreniInRitardo) {
                	break;
                }
                	                                
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }	    

}
