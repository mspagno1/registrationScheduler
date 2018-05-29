
package registrationScheduler.objectPool;
import registrationScheduler.objectPool.Student;
import registrationScheduler.objectPool.Course;
import registrationScheduler.util.Logger;
import java.util.ArrayList;

public class ObjectPool {
    /**
     * ArrayList of all the courses
     */
	private Course[] courseArray;
    /**
     * ArrayList of all the students
     */
	private ArrayList<Student> studArrayList;
    /**
     * Int Max number of students in total
     */
	private final int MAX_STUDENTS = 80;
    /**
     * Int Max number of courses
     */
	private	final int MAX_COURSES = 8;
    
	
    /**
     * ObjectPool Constructor
     * @param  Logger debug
     * @return ObjectPool Object
     */
    
	public ObjectPool(){
		courseArray = new Course[MAX_COURSES];
		studArrayList = new ArrayList<Student>();
		courseArray[0] = new Course("A");
		courseArray[1] = new Course("B");
		courseArray[2] = new Course("C");
		courseArray[3] = new Course("D");
		courseArray[4] = new Course("E");
		courseArray[5] = new Course("F");
		courseArray[6] = new Course("G");
		courseArray[7] = new Course("H");
		Logger.writeMessage("ObjectPool Constructor called", Logger.DebugLevel.CONSTRUCTOR);
	}

    /**
     * Getter for studArrayList
     * @param  None
     * @return ArrayList<Student> studArrayList
     */
	public ArrayList<Student> getStudArrayList(){
		return studArrayList;
	}

    /**
     * Getter for student in studArrayList based on index
     * @param  int index of student
     * @return Student stud
     */
	public Student getStud(int index){
		return studArrayList.get(index);
	}
	
    /**
     * Getter for student in studArrayList based on name
     * @param String name of student
     * @return Student temp or null if they dont exist
     */
	public Student getStudString(String name){
		Student temp = null;
		for(int i =0; i < studArrayList.size(); i++){
			if(name.equals(studArrayList.get(i).getStudName())){
				temp = studArrayList.get(i);
				break;
			}
		}
		return temp;
	}

    /**
     * Setter for student in studArrayList
     * @param int index location to be added, Student object being added
     * @return None
     */
	public void setStud(int index, Student stud){
		studArrayList.set(index, stud);
	}
    /**
     * Adds a student to the end studArrayList
     * @param  Student object being added
     * @return None
     */
	public void addStud(Student stud){
		studArrayList.add(stud);
	}
	
    /**
     * Getter for course in courseArray based on name
     * @param String name of course
     * @return Course temp or null if they dont exist
     */
	public Course getCourseString(String name){
		Course temp = null;
		for(int i =0; i < 8; i++){
			if(name.equals(courseArray[i].getCourseName())){
				temp = courseArray[i];
				break;
			}
		}
		return temp;
	}
    /**
     * Getter for course in courseArray based on index
     * @param int index
     * @return Course course at index
     */
	public Course getCourseIndex(int index){
		return courseArray[index];
	}
	
} 


