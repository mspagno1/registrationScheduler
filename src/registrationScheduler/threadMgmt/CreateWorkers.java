package registrationScheduler.threadMgmt;

import java.io.IOException;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.Logger;
import registrationScheduler.objectPool.ObjectPool;

//Takes in the num of threads and creates them

public class CreateWorkers{

    /**
     * ObjectPools info contains information on all the students and courses
     */
    private ObjectPool info;
    /**
     * File we are accessing
     */
    private FileProcessor file;
    /**
     * number of threads that can be running
     */
    private int numThreads;
    /**
     * Array containing the WorkerThread thread objects
     */
    private WorkerThread[] workerArray;
    /**
     * Int number that determines which type of file 0 is reg-pref file, 1 is add-drop file
     */
    private int operation;
	
    /**
     * CreateWorkers Constructor
     * @param  ObjectPools r info of all students/courses, FileProcessor fp file being used , int numOfThreads number of threads we can use, int op file type
     * @return CreateWorker object
     */
	public CreateWorkers(ObjectPool pool, FileProcessor fp, int numOfThreads, int op){
		info = pool;
		file = fp;
		numThreads = numOfThreads;
		operation = op;
		workerArray = new WorkerThread[numThreads];
		Logger.writeMessage("CreateWorkers Constructor called", Logger.DebugLevel.CONSTRUCTOR);
	}

    /**
     * Starts to run a worker thread and waits for those threads to finish
     * <p>
     * Creates x amount of WorkerThreads based on on the numThreads we are given. Then tries to run these
     * threads and waits for them all to finish.
     * <p>
     * @param  Void
     * @return Void
     */
	public void StartWorkers(){
		for(int i = 0; i < numThreads; i++){
			workerArray[i] = new WorkerThread(info, file, i, operation);
			//System.out.println(workerArray[i].getThrId());
			//workerArray[i].run();
		}
		
		for(int i = 0; i < numThreads; i++){
			workerArray[i].start();
		}
		for(int j = 0; j < numThreads; j++){
			try{
				workerArray[j].getThread().join();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
				e.printStackTrace();
				try{
					file.closeReadFile();
				}catch(IOException ex){
					System.err.println(ex.getMessage());
					ex.printStackTrace();
					System.exit(1);
				}finally{
				
				}
				System.exit(1);
			}finally{
			
			}
		}
		
		try{
			file.closeReadFile();
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}finally{
			
		}
	}
	
    /**
     * ObjectPool Getter
     * @param  None
     * @return ObjectPools info object
     */
    public ObjectPool getObjectPools(){
        return info;
    }
    
    /**
     * ObjectPool Setter
     * @param  ObjectPools info object
     * @return None
     */
    public void setObjectPool(ObjectPool pool){
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
     * numThreads Getter
     * @param  None
     * @return int threads number
     */
    public int getNumThreads(){
        return numThreads;
    }
    
    /**
     * numThreads Setter
     * @param  int threads number
     * @return None
     */
    public void setNumThreads(int numOfThreads){
        numThreads = numOfThreads;
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
	
	//end of getters and setters
	
    /**
     * ToString() for CreateWorkers
     * @param  None
     * @return String numOfThreads + files toString()
     */
	public String toString(){
		return "" + numThreads + " " + file.toString();
		
	}
	
}
