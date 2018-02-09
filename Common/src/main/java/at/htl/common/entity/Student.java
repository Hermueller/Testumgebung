package at.htl.common.entity;

import at.htl.common.enums.StudentState;

public class Student {

    private Pupil pupil;

    private StudentState studentState;

    public Student(Pupil pupil) {
        this.pupil = pupil;
        this.studentState = StudentState.NORMAL;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public StudentState getStudentState() {
        return studentState;
    }

    public void setStudentState(StudentState studentState) {
        this.studentState = studentState;
    }

    @Override
    public String toString() {
        return pupil.getLastName();
    }
}
