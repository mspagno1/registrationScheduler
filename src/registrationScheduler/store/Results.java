
package registrationScheduler.store;

import registrationScheduler.objectPool.Student;
import registrationScheduler.objectPool.ObjectPool;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.StdoutDisplayInterface;
import registrationScheduler.util.FileDisplayInterface;
import registrationScheduler.util.Logger;
import java.util.ArrayList;
import java.io.IOException;

public class Results implements StdoutDisplayInterface, FileDisplayInterface {
    /**
     * ArrayList of all the output Strings
     */
	private ArrayList<String> outputData;
    /**
     * ObjectPool of all the data
     */
	private ObjectPool info;

    /**
     * Result Constructor
     * @param  ObjectPool of all the data, FileProcessor of the output
     * @return Result Object
     */
    
	public Results(ObjectPool pool){
		outputData = new ArrayList<String>();
		info = pool;
		Logger.writeMessage("Result Constructor called", Logger.DebugLevel.CONSTRUCTOR);
	}

     /**
     * Generates the outputData 
     * <p>
     * Goes through the objectPool and gets the information in a the right format and stores that information
     * into our arrayList. Also calculates the average preferscore score to add to the results.
     * <p>
     * @param  None
     * @return None
     */
	public void generateOutput(){
		ArrayList<Student>studArrList = info.getStudArrayList();
		double avgPrefScore = 0.0;
		double numScores = 0.0;
		for(int i = 0; i < studArrList.size(); i++){
			Student newStud = studArrList.get(i);
			String outputString = "";
			outputString = outputString + newStud.getStudName() + " ";
			ArrayList<String> schedule = newStud.getCourseList();
			for(int k = 0; k < schedule.size(); k++){
				outputString = outputString + schedule.get(k) + " "; 
			}
			numScores++;
			avgPrefScore = avgPrefScore + newStud.getPrefScore();
			outputString = outputString + newStud.getPrefScore();
			outputData.add(outputString);
			Logger.writeMessage("String was added to Result Data Structure", Logger.DebugLevel.ENTRY_ADDED);

		}
		avgPrefScore = avgPrefScore/numScores;
		String avgPrefScoreLine = "Average preference_score is: " + avgPrefScore;
		outputData.add(avgPrefScoreLine);
		Logger.writeMessage("String was added to Result Data Structure", Logger.DebugLevel.ENTRY_ADDED);
	}



    /**
     * Getter for outputData
     * @param  None
     * @return ArrayList<String> outputData
     */
	public ArrayList<String> getOutputData(){
		return outputData;
	}

    /**
     * Setter for outputData
     * @param  ArrayList<String> outputData
     * @return None
     */
	public void setOutputData(ArrayList<String> newData){
		outputData = newData;
	}

    /**
     * Getter for ObjectPool
     * @param  None
     * @return ObjectPool info
     */
	public ObjectPool getObjectPool(){
		return info;
	}	

    /**
     * Setter for ObjectPool
     * @param  ObjectPool info
     * @return None
     */
	public void setObjectPool(ObjectPool pool){
		info = pool;
	}
     /**
     * Prints out outPutData to system.out
     * @param  None
     * @return None
     */	
	public void writeScheduleToScreen() {
		for(int i = 0; i < outputData.size(); i++){
			Logger.writeMessage((outputData.get(i)+ "\n"), Logger.DebugLevel.DATA_STRUCT);
			//System.out.println(outputData.get(i) + "\n");
		}
	}
     /**
     * Writes out outPutData to a given file
     * @param  String fileName of the file your writing to
     * @return None
     */	
	
	public void writeSchedulesToFile(String outPutFile){
		FileProcessor file = new FileProcessor(outPutFile , false);
		for(int i = 0; i < outputData.size(); i++){
			try{
				file.writeOneLine(outputData.get(i));
			}catch(IOException e){
				System.err.println("Couldn't write to output file");
				e.printStackTrace();
				try{
					file.closeWriteFile();
				}catch(IOException e2){
					System.err.println("couldn't close file");
					e2.printStackTrace();
					System.exit(1);
				}finally{

				}
				System.exit(1);
			}finally{
				
			}
		}
		try{
			file.closeWriteFile();
		}catch(IOException e){
			System.err.println("couldn't close file");
			e.printStackTrace();
			System.exit(1);
		}finally{

		}
		

	}
} 


