package util;

public final class Constants {
	

    private Constants() { }

   
    public static final String plannedFile="planned.csv";
    public static final String actualFile="actual.csv";
    public static final String outputFile="output.csv";
	
    public static final String separatorTrenoStazione=" ";
    public static final String separatorDataTime="T";
    
    public static final String intestazioneOutputCsv="Numero treno,Destinazione,Ora pianificata,Ora effettiva,Ritardo";
    
    public static final Integer totaleTreniInRitardo=5;

}
