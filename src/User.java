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
}
