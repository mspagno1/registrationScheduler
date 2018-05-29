package registrationScheduler.objectPool;

import registrationScheduler.util.Logger;
import java.util.ArrayList;

public class Student{
    /**
     * String name of the student
     */
    private String studName;
    /**
     * ArrayList of the students prefList of classes
     */
    private ArrayList<String> prefList;
    /**
     * ArrayList of what the student is taking
     */
    private ArrayList<String> schedule;
    /**
     * int score of the preference of classes the student got
     */
    private int prefScore;
    
    /**
     * Student Constructor
     * @param  None
     * @return Student Object
     */
    public Student(){
        //studName = name;
        schedule = new ArrayList<String>();
        prefList = new ArrayList<String>();
        prefScore = 0;
	Logger.writeMessage("Student Constructor called", Logger.DebugLevel.CONSTRUCTOR);
    }
    
    /**
     * prefScore getter
     * @param  None
     * @return int prefScore
     */
    public int getPrefScore(){
        return prefScore;
    }
    
    /**
     * prefScore setter
     * @param  int score
     * @return None
     */
    public void setPrefScore(int score){
        prefScore = score;
    }
    
    /**
     * Calculates the preference score
     * <p>
     * Matches your prefList based on your schedule. Matches are worth more depending on what location
     * they are in the array. If there is a no match score but a class is there we add one. Otherwise score
     * does not change.
     * <p>
     * @param  None
     * @return None
     */
    public void calcPrefScore(){
        for(int i = 0; i < schedule.size(); i++){
            boolean foundCourse = false;;
            for(int j = 0; j < prefList.size(); j++){
                if(schedule.get(i).equals(prefList.get(j))){
                    //index = j;
                    prefScore = prefScore + 6 - j;
                    foundCourse = true;
                }
            }
            if(false == foundCourse){
                prefScore++;
            }
        }
    }
    
    /**
     * getter of studName
     * @param  None
     * @return String studName
     */
    public String getStudName(){
        return studName;
    }
    
    /**
     * Setter of studName
     * @param  String name
     * @return None
     */
    public void setStudName(String name){
        studName = name;
    }
    
    /**
     * adds courseName to the end of prefList
     * @param  String courseName
     * @return None
     */
    public void addToPrefList(String name){
        prefList.add(name);
    }
    
    /**
     * ArrayList prefList getter
     * @param  None
     * @return ArrayList<String> prefList
     */
    public ArrayList<String> getPrefList(){
        return prefList;
    }
    
    /**
     * Get class String from prefList
     * @param  int index of element to get
     * @return String courseName
     */
    public String getPrefClass(int index){
        return prefList.get(index);
    }
    
    /**
     * ArrayList schudule getter
     * @param  ArrayList<String> schudule
     * @return None
     */
    public ArrayList<String> getCourseList(){
        return schedule;
    }
    
    /**
     * checks to see if we can add a course
     * <p>
     * Checks to see if we already are taking a course we are trying to add. If we do return false, otherwise
     * return true and add the course.
     * <p>
     * @param  String courseName
     * @return boolean true is successful add, false on fail
     */
    public boolean addCourse(String courseName){
        boolean retval = true;
        for(int i = 0; i < schedule.size(); i++){
            
            if(courseName.equals(schedule.get(i))){
                retval = false;
            }
        }
        
        if(true == retval){
            schedule.add(courseName);
        }
        return retval;
    }
    
    /**
     * Checks to see if we can remove a course
     * <p>
     * Checks to see if we have the course we are trying to remove. If we dont return false, otherwise
     * return true and remove the course.
     * <p>
     * @param  String courseName
     * @return boolean true is successful add, false on fail
     */
    public boolean removeCourse(String courseName){
        int removeIndex = 0;
        boolean retval = false;
        for(int i = 0; i < schedule.size(); i++){
            
            if(courseName.equals(schedule.get(i))){
                retval = true;
                removeIndex = i;
            }
        }	
        
        if(true == retval){
            schedule.remove(courseName);
        }
        return retval;
    }
    
    /**
     * toString() for student
     * @param  None
     * @return String of the studName
     */
    public String toString(){
        return studName;
    }
}
