public class User {
    int id;
    String password;
    Boolean isTeacher;
    String name;
    int[] classrooms;

    public User(int id, String password, Boolean ifTeacher, String name, int[] classrooms) {
        this.id = id;
        this.password = password;
        this.isTeacher = ifTeacher;
        this.name = name;
        this.classrooms = classrooms;
    }
    
    public void viewClassrooms(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public void submitAssignment(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public void viewGrades(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public void sendMessage(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public void viewClassroomRoster(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public void viewCalendar(int id) {
        //get all the classrooms with id
        /*
        int[] ids = Classroom.user_ids;
        for (int i = 0; i < ids.length; i++) {
            if(ids[i] == id){
                System.out.println(ids[i] + " " + classrooms[i]);
            }
        }
         */
    }

    public static void main(String[] args) {
        //
    }
}
