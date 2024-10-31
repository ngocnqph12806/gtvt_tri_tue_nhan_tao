package com.projectj.finaly.ngocnq;

class SearchState {
    String state;
    String nextStates;
    String listL1;
    String listL;
    
    public SearchState(String state, String nextStates, String listL1, String listL) {
        this.state = state;
        this.nextStates = nextStates;
        this.listL1 = listL1;
        this.listL = listL;
    }
}