package com.application.student;

import com.application.student.model.StudentService;
import com.application.visitor.model.Visitor;

public interface OnSign {
    public  void  signIn(StudentService studentService, Visitor visitor);
    public  void signOut(StudentService studentService, Visitor visitor);
}
