package at.htl.server.enums;

public enum StudentState{
    FINISHED,
    CONNECTION_LOST,
    NORMAL;

    public int compareToStudentState(StudentState b){
        if (this == b) {
            return 0;
        }
        else if(this == FINISHED){
            return -1;
        }
        else {
            return 1;
        }
    }
}
