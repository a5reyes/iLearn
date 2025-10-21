class User {
    int id;
    String password;
    Boolean ifTeacher;
    String name;
    int[] classrooms;

    public User(int id, String password, Boolean ifTeacher, String name, int[] classrooms) {
        this.id = id;
        this.password = password;
        this.ifTeacher = ifTeacher;
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
    public static void main(String[] args) {
        //
    }
}
