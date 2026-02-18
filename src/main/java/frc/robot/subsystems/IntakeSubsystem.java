package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {

    private Spark intakeMotor = new Spark(5);

    public IntakeSubsystem() {
    }

    @Override
    public void periodic() {
    }

    public void setPosition(boolean open) {
        if (open) {
            intakeMotor.set(IntakeConstants.kOpenSpeed);
        } else {
            intakeMotor.set(IntakeConstants.kCloseSpeed);
        }
    }
}
