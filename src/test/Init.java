package test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import util.Constants;
import util.Utils;



public class Init {
	
	private static final Logger logger = Logger.getLogger(Init.class.getName());

	public static void main(String[] args) {
		
		String namePlannedFile=Constants.plannedFile;
		String nameActualFile=Constants.actualFile;
		String nameOutputFile=Constants.outputFile;
		
		if(!Utils.validatorExistFile(namePlannedFile)) {  
			logger.severe("The file 'planned.csv' with the expected times is missing.");
			System.exit(0);
		}
		if(!Utils.validatorExistFile(nameActualFile)) {
			logger.severe("The file 'actual.csv' with the current times is missing.");
			System.exit(0);
		}

		
		HashMap<String, String> plannedHashMap = new HashMap<>();
		HashMap<String, String> actualHashMap = new HashMap<>();
		HashMap<String, Integer> delayHashMap = new HashMap<>();
		
		plannedHashMap = Utils.popolateHashMap(namePlannedFile);
		actualHashMap = Utils.popolateHashMap(nameActualFile);
		
		delayHashMap=Utils.generateDelayHashMap(plannedHashMap, actualHashMap);        
        
        LinkedHashMap<String, Integer> delayLinkedHashMap = delayHashMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
        
        
        if(Utils.createCsv(nameOutputFile, plannedHashMap, actualHashMap, delayLinkedHashMap)) {
        	logger.info("file 'output.csv' generated successfully");	
        }else {
        	logger.info("file 'output.csv' not generated");	
        }
        		
	}
	
}
