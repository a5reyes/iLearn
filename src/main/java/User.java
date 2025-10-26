public class User {
    private int id;
    private String password;
    private Boolean isTeacher;
    private String name;
    private String[] classrooms;

    public User(int id, String password, Boolean isTeacher, String name, String[] classrooms) {
        this.id = id;
        this.password = password;
        this.isTeacher = isTeacher;
        this.name = name;
        this.classrooms = classrooms;
    }

    public String getName() {
        return this.name;
    }

    public void isStudent(){
        this.isTeacher = false;
    }

    public void isTeacher(){
        this.isTeacher = true;
    }
    
    public String viewClassrooms() {
        return String.join(", ", this.classrooms);
    }

    public void submitAssignment(int id) {
        //
    }

    public void viewGrades(int id) {
        //
    }

    public void sendMessage(int id) {
        //
    }

    public void viewClassroomRoster(int id) {
        //
    }

    public void viewCalendar(int id) {
        //
    }

    public static void main(String[] args) {
        //
    }
}
