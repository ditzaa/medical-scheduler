package com.medisched.model.protocols;

public class CardiologyProtocol implements MedicalProtocol{
    @Override
    public String getInstructions() {
        return "Fără efort fizic cu 2h înainte. Nu consumați cafea.";
    }
    @Override
    public String getRequiredEquipment() {
        return "Echipament EKG, Tensiometru";
    }
}
