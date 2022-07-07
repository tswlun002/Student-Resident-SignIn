package com.host;

import com.visitor.Visitor;

public interface OnSign {
    public  void  signIn(Host host, Visitor visitor);
    public  void signOut(Host host, Visitor visitor);
}
