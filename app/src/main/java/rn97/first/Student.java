package rn97.first;
public class Student {
    String studentName;
    String studentId;
    String studentMailId;
    String studentScore;
    String studentBranch;

    public Student(){

    }
    public Student(String studentId, String studentName,String studentMailId, String studentScore,String studentBranch){
        this.studentName = studentName;
        this.studentMailId = studentMailId;
        this.studentId = studentId;
        this.studentScore = studentScore;
        this.studentBranch = studentBranch;
    }
    public String getStudentMailId(){
        return studentMailId;
    }
    public String getStudentName(){
        return studentName;
    }
    public String getStudentId(){
        return studentId;
    }
    public String getStudentBranch(){return studentBranch;}
    public String getStudentScore(){
        return studentScore;
    }
}
