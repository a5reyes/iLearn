public class Classroom {
    int classroomId;
	String teacher;
	String discussions;
	
	public Classroom(int classroomId, String teacher, String discussions) {
		this.classroomId = classroomId;
		this.teacher = teacher;
		this.discussions = discussions;
	}
	
	

	public int getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(int classroomId) {
		this.classroomId = classroomId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getDiscussions() {
		return discussions;
	}
	public void setDiscussions(String discussions) {
		this.discussions = discussions;
	}
	
	
}
