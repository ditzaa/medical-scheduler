package com.medisched.model.protocols;

public class GeneralProtocol implements MedicalProtocol {

    @Override
    public String getInstructions() {
        return "Prezentați-va cu 10 minute înainte. Aveți la îndemână buletinul și cardul de sănătate.";
    }

    @Override
    public String getRequiredEquipment() {
        return "Stetoscop, Termometru, Tensiometru standard.";
    }
}