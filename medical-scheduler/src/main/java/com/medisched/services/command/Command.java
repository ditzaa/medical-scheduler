package com.medisched.services.command;

public interface Command {
    void execute();
    // Opțional, aici se poate adăuga: void undo();
}