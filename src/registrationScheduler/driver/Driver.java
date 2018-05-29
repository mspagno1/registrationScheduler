
package registrationScheduler.driver;

import registrationScheduler.threadMgmt.CreateWorkers;


import java.io.IOException;
import java.util.ArrayList;
import registrationScheduler.util.Logger;
import registrationScheduler.objectPool.Student;
import registrationScheduler.objectPool.Course;
import registrationScheduler.objectPool.ObjectPool;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.store.Results;


public class Driver{
	public static void main(String[] args) {
		
		
	
		//System.out.println("args.length: " + args.length);
		if(args.length != 5){
			System.err.println("need exactly 5 command line args");
			System.exit(1);
		}
		
		//arg0 is reg-preference.txt
		//System.out.println("\n The args[0] is: " + args[0]);
		//arg1 is add-drop.txt
		//System.out.println("\n The args[1] is: " + args[1]);
		//arg2 is output.txt
		//System.out.println("\n The args[2] is: " + args[2]);
		//arg3 is num of threads
		//System.out.println("\n The args[3] is: " + args[3]);
		//arg4 is the debug level as an int
		//System.out.println("\n The args[4] is: " + args[4]);
		
		int arg3 = 0;
		try{
			arg3 = Integer.parseInt(args[3]);
		}catch(NumberFormatException e){
			System.err.println("arg3 is not an int");
			e.printStackTrace();
			System.exit(1);
		}finally{
			if(arg3 < 1 || arg3 > 3){
				System.err.println("arg3 is not between 1 and 3");
				System.exit(1);
			}
		}
		
		int arg4 = 0;
		Logger logger = new Logger();
		try{
			arg4 = Integer.parseInt(args[4]);
			
			logger.setDebugValue(arg4);
		}catch(NumberFormatException e){
			System.err.println("arg4 is not an int");
			e.printStackTrace();
			System.exit(1);
		}finally{
			if(arg4 < 0 || arg4 > 4){
				System.err.println("arg4 is not between 0 and 5");
				System.exit(1);
			}
		}
		//ALL OBJECTS ARE GONNA HAV TO BE PASSED THIS LOGGER OBJECT TO DO THE CHECKS
		//THEN DO IF CHECKS IN THE PLACES WE NEED TO PRINT THESE THINGS OUT IN 
		//CALL WRITE MESSAGE ON THOSE CASES
		
		
		//put all this in ObjectPool
		//init courses here
		
		
		
		//opens regPref file
		ObjectPool pool = new ObjectPool();
		FileProcessor regPrefInput = new FileProcessor(args[0] , true);
		
		//pass file, ObjectPool, and other info to CreateWorkers
		CreateWorkers regTimeWork= new CreateWorkers(pool, regPrefInput, arg3, 0);
		regTimeWork.StartWorkers();
		
		//calc pref score here
		ArrayList<Student> studArr = pool.getStudArrayList();
		for(int k = 0; k < studArr.size(); k++){
			studArr.get(k).calcPrefScore();
			//System.out.println("stud name: " + (studArr.get(k)).getStudName());
			//System.out.println("pref score: " + studArr.get(k).getPrefScore());
		} 

		//Add or drop thread here
		FileProcessor addDrop = new FileProcessor(args[1] , true);
		
		CreateWorkers addDropWork= new CreateWorkers(pool, addDrop, arg3, 1);
		addDropWork.StartWorkers();
		
		
		//Output to file
		Results result = new Results(pool);
		result.generateOutput();
		
		result. writeScheduleToScreen();
		
		
		//result.writeScheduleToScreen(); <- This would be logger value 1
		result.writeSchedulesToFile(args[2]);
		
	} // end main(...)

} // end public class Driver

