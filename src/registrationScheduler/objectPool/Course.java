package registrationScheduler.objectPool;
import registrationScheduler.util.Logger;

public class Course{
    /**
     * Int Max number of students possible for a course constant 60
     */
    private final int MAX_STUDENT = 60;
    /**
     * Int containing the number of seats left range 60 - 0
     */
    private int seatsLeft;
    /**
     * String of the course name
     */
    private String courseName;    
    
    /**
     * Course Constructor
     * @param  String name of the course , Logger for debug
     * @return Course Object
     */
    public Course(String name){
        seatsLeft = MAX_STUDENT;
        courseName = name;
	Logger.writeMessage("Course Constructor called", Logger.DebugLevel.CONSTRUCTOR);
    }
    
    /**
     * Getter for seatsLeft
     * @param  None
     * @return Int seatsLeft
     */
    public int getSeatsLeft(){
        return seatsLeft;
    }
    
    /**
     * Setter for seatsLeft
     * @param  int seatsNum
     * @return None
     */
    public void setSeatsLeft(int seat){
        seatsLeft = seat;
    }
    
    /**
     * Checks to see if a we can add a seat
     * <p>
     * Checks to see if a class can gain a seat. If it is already empty we print an error. Otherwise
     * we add one to the seatNum.
     * <p>
     * @param  None
     * @return boolean true if success false on failure
     */
    public boolean addSeat(){
        boolean retval = false;
        if(seatsLeft < MAX_STUDENT){
            seatsLeft++;
            retval = true;
        }else{
            System.out.println("course drop error");
        }
        return retval;
    }
    
    /**
     * Checks to see if a we can remove a seat
     * <p>
     * Checks to see if a class can gain a seat. If it is already full we print an Message. Otherwise
     * we remove one to the seatNum.
     * <p>
     * @param  None
     * @return boolean true if success false on failure
     */
    public boolean removeSeat(){
        boolean retval = false;
        if(seatsLeft > 0){
            seatsLeft--;
            retval = true;
        }else{
            System.out.println("course full");
        }
        return retval;
    }
    /**
     * Getter for courseName
     * @param  None
     * @return String courseName
     */
    public String getCourseName(){
        return courseName;
    }
    /**
     * Setter for courseName
     * @param  String courseName
     * @return None
     */
    public void setCourseName(String name){
        courseName = name;
    }
    /**
     * ToString print of a course
     * @param  None
     * @return String (courseName + seatsLeft)
     */
    public String toString(){
        return courseName + " " + seatsLeft + "\n";
    }
    
}
