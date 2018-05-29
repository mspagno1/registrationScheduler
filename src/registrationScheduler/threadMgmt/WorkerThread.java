package registrationScheduler.threadMgmt;

import java.io.IOException;
import registrationScheduler.objectPool.Student;
import registrationScheduler.objectPool.Course;
import registrationScheduler.objectPool.ObjectPool;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.Logger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Iterator;


public class WorkerThread implements Runnable{
    /**
     * RentrantLock lock
     */
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    /**
     * Lock for file operations
     */
    private Lock rdLock = rwl.readLock();
    /**
     * Lock for editing shared resource ObjectPools
     */
    private Lock schLock = new ReentrantLock();
    /**
     * ID of the thread
     */
    private int thrId;
    /**
     * Thread object
     */
    private Thread t;
    /**
     * ObjectPool object of all student/courses info
     */
    private ObjectPool info;
    /**
     * File object
     */
    private FileProcessor file;
    /**
     * File type 0 prefList 1 addDrop
     */
    private int operation;
    
    /**
     * WorkerThread Constructor
     * @param  ObjectPools r info of all students/courses, FileProcessor fp file being used , int threadId, int op file type
     * @return WorkerThread object
     */
    
	public WorkerThread(ObjectPool pool, FileProcessor fp, int id, int op){
		thrId = id;
		info = pool;
		file = fp;
		operation = op;
		t = null;
		Logger.writeMessage("WorkerThread Constructor called", Logger.DebugLevel.CONSTRUCTOR);
		
	}
    
    /**
     * Preforms the prefList schuedule or add-drop algorithem.
     * <p>
     * Reads a line from our file. Checks to see if it is a prefList operation or an addDrop operation.
     * Performs approiate operation depending on operation type. Locks are placed around shared data points
     * to prevent issues.
     * <p>
     * @param  None
     * @return None
     */
	//https://www.tutorialspoint.com/java/java_multithreading.htm
	public void run(){
	Logger.writeMessage("Run is called", Logger.DebugLevel.RUN);
	//add if else for diff run funcs
		String line = null;
		try{
			//lock
			rdLock.lock();
			line = file.readOneLine();
			rdLock.unlock();
			//release lock
			while(line != null){
				/*
				gen stud if op = 0
				gen a student
				assign pref to student
				*/
				
				
				if(0 == operation){
					schLock.lock();
					//System.out.println(thrId);
					//System.out.println(line);
					schedulizer(line);
					schLock.unlock();
					//handle add-drop if op = 1
				}else if(1 == operation){
					schLock.lock();
					//System.out.println(thrId);
					//System.out.println(line);
					//(int opValue,Student stud, String courseName)
					
					addOrDrop(line);
					schLock.unlock();
				}
				
				//lock
				rdLock.lock();
				//store ObjectPools in info	
				line = file.readOneLine();
				//release lock
				rdLock.unlock();
			//	t.wait();
				
			}
		}catch( IOException e){
			e.getMessage();
			e.printStackTrace();
			System.exit(1);
		}finally{
			//System.out.println("thread: "  + thrId + " exiting");
		}
	}


    /**
     * Checks to see if a student took as class/
     * <p>:
     * Checks to see if a student has already taken a class, by iterating through the students current schedule
     * If they have, return true, otherwise return false.
     * <p>
     * @param  Student std: student you wish to check , int classNum : class you want to access
     * @return boolean taken : true if class was already taken, false if class was not taken
     */
   	 public boolean alreadyTaken(Student std, String classStr){
		 ArrayList<String> temp = std.getCourseList();
		 Iterator<String> itr = temp.iterator();
		 boolean taken = false;
		 while(itr.hasNext() && taken == false){
			String tempStr = itr.next();
			if((tempStr.equals(classStr)) == true){
			    taken = true;
			}
		 }
    		 return taken;
   	 }
   
    /**
     * Checks to see if a student asks for a class later on
     * <p>:
     * Checks to see if a student to see if a student is asking for a class later on in his
     * preference list. This is mainly to make sure our preference math is accurate, as we
     * dont want to fill a student with a class they are gonna ask for later and have the
     * wrong preference score for that request. If it's asked for later on return true,
     * otherwise return false.
     * <p>
     * @param  Student std: student you wish to check , int index : class you want to access, String classStr
     * string of class you are checking
     * @return boolean laterOn : true is asked for later, false if the class is not asked for later
     */
	public boolean laterOnList(Student std, int index, String classStr){
		boolean laterOn = false;
		ArrayList<String> temp = std.getPrefList();
		for(int i = index; i < 4; i++){
			String tempStr = temp.get(i);
			if((tempStr.equals(classStr)) == true){
				laterOn = true;
			}
		}
		return laterOn;
	}
	
    /**
     * Sets up a schudule for a student
     * <p>
     * Gets a student and tries to give them a schudule based on their prefList. If that class is not avaiable
     * we then try give them another class if possible. Afterwards we calculate the pref score on how the algo
     * did based on their prefList and then update ObjectPools.
     * <p>
     * @param  None
     * @return None
     */
	public void schedulizer(String line){
		
		//Set up preference list of a student
		Student newStud = new Student();

		String[] prefTokens = line.split(" ");
		
		newStud.setStudName(prefTokens[0]);
		
		for(int i = 1; i < 6; i++){
			newStud.addToPrefList(prefTokens[i]);	
		}
		
		
		//Make Schedule
		
		for(int i = 0; i < 5; i ++){
			String classString = newStud.getPrefClass(i);
			//Check to see if class has space? how to accesss courses
			Course curCourse = info.getCourseString(classString);
			//If there is room left then we can add a course in
			if(curCourse.getSeatsLeft() > 0){
				newStud.addCourse(curCourse.getCourseName());
				//Might be only changing local value might need to do info.getCourseString(classString).removeSeat();
				info.getCourseString(classString).removeSeat();
			}
			else{
		            //If are prefered class is full we see what other class we can get you into
		            boolean anyOpen = false;
		            int index = 0;
		            while(index < 8 && anyOpen == false){
		            	Course checkCourse = info.getCourseIndex(index);
		                //Checks to see if there is any empty spots
		                if(checkCourse.getSeatsLeft() > 0){
		                    //CHecks to see if you already took this class
		                    if(alreadyTaken(newStud, checkCourse.getCourseName()) == false){
		                        //Checks to see you arent asking for it later so the preference math isnt messed up
		                        if((laterOnList(newStud, i, checkCourse.getCourseName())) == false){
		                            newStud.addCourse(checkCourse.getCourseName());
		                            //Might be only changing local value might need to do info.getCourseString(checkCourse.getCourseName()).removeSeat();
		                            info.getCourseIndex(index).removeSeat();
		                            anyOpen = true;
		                        }
		                    }
		                   
		                }
		                index++;
		            }
		       }
		
		}
		//Calc Pref Score
		newStud.calcPrefScore();
		/*
		//Arraylist<String> studCourses = newStud.getCourseList();
		for(int a = 0; a < newStud.getCourseList().size(); a++){
			System.out.println("course: " + newStud.getCourseList
		().get(a));
		}
		System.out.println("");
		*/
		//add to ObjectPools
		info.addStud(newStud);
	}
	
    /**
     * Adds or drops a course for a student
     * <p>
     * Gets student name from ObjectPools student array check if they have or in the case of add not have
     * Checks ObjectPools courses sees if there is space or no space left depending on which case
     * Case 0 is add Case 1 is drop
     * Update both student and courses if we succeed nothing is done if we fail
     * <p>
     * @param  String line from the file
     * @return None
     */
	public void addOrDrop(String line){
		//Add

		String[] prefTokens = line.split(" ");
		Student stud = info.getStudString(prefTokens[0]);
		
		int opValue = 0;
		try{
			opValue = Integer.parseInt(prefTokens[1]);
		}catch(NumberFormatException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}finally{}
		
		String courseName = prefTokens[2];
		
		Course curCourse = info.getCourseString(courseName);
		if(0 == opValue){
			//Check to see if class has room
			if(null != stud){
				//This might be a redundent call i think removeSeat checks this already
				if(curCourse.getSeatsLeft() > 0){
					//Add course here
					//System.out.println("addLine: " + line);
					//System.out.println("stud.toString(): " + stud.toString());
					boolean canAdd = stud.addCourse(courseName);
					if(true == canAdd){
						info.getCourseString(courseName).removeSeat();
					}
				}
			}
			//Do we do something for else....
		}
		//Drop
		else if(1 == opValue){
			
			//This might be a redundent call i think addSeat checks this already
			if(curCourse.getSeatsLeft() < 60){
				//Check if person has the course to drop
				//causes null ptr except
				if(null != stud){
					//System.out.println("removeLine: " + line);
					//System.out.println("stud.toString(): " + stud.toString());
					boolean hasCourse = stud.removeCourse(courseName);
					if(true == hasCourse){
						info.getCourseString(courseName).addSeat();
						//System.out.println("removed " + courseName);
					}else{
						//System.out.println("course " + courseName + " not found");
					}
				}
			}
		}
	}
	
    /**
     * Starts a thread
     * <p>
     * If t is not null, create a thread and start it
     * @param  None
     * @return None
     */
	
	public void start(){
		if(null == t){
			t = new Thread(this);
			t.start();
		}
	}
	
    /**
     * WorkerThread toString()
     * @param  None
     * @return String thrId
     */
	
	public String toString(){
	
		return "" + thrId;
	}
	
    /**
     * thrId getter
     * @param  None
     * @return int thrId
     */
    public int getThrId(){
        return thrId;
    }
    
    /**
     * thrId Setter
     * @param  int thrId
     * @return None
     */
    public void setThrId(int num){
        thrId = num;
    }
    /**
     * ObjectPools Getter
     * @param  None
     * @return ObjectPools info object
     */
    public ObjectPool getObjectPools(){
        return info;
    }
    /**
     * ObjectPools Setter
     * @param  ObjectPools info object
     * @return None
     */
    public void setObjectPools(ObjectPool pool){
        info = pool;
    }
    /**
     * File Getter
     * @param  None
     * @return FileProccessor file
     */
    public FileProcessor getFileProcessor(){
        return file;
    }
    /**
     * File Setter
     * @param  FileProccessor file
     * @return None
     */
    public void setFileProcessor(FileProcessor fp){
        file = fp;
    }
    /**
     * operation type Getter
     * @param  None
     * @return int operation type 0 pref 1 add-drop
     */
    public int getOperation(){
        return operation;
    }
    /**
     * operation type Setter
     * @param  int operation type 0 pref 1 add-drop
     * @return None
     */
    public void setOperation(int op){
        operation = op;
    }
    /**
     * Thread Getter
     * @param  None
     * @return Thread t
     */
    public Thread getThread(){
        return t;
    }
    /**
     * Thread Setter
     * @param  Thread t
     * @return None
     */
    public void setThread(Thread thr){
        t = thr;
    }
	
}
