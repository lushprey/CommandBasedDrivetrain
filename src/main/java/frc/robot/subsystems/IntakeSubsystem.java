package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;

public class IntakeSubsystem extends SubsystemBase {

    private Spark intakeLeftMotor = new Spark(5);
    private Spark intakeRightMotor = new Spark(6);
    private intakeDoubleSolenoid = new DoubleSolenoid(1,PneumaticsModuleType.REVPH,0,1)

    public IntakeSubsystem() {
        IntakeDoubleSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    @Override
    public void periodic() {
    }

    public void setPosition(boolean open) {
        if (open) {
            intakeLeftMotor.set(IntakeConstants.kOpenSpeed);
            intakeRightMotor.set(IntakeConstants.kOpenSpeed);
            intakeDoubleSolenoid.set(DoubleSolenoid.Value.kForward);
        } else {
            intakeLeftMotor.set(IntakeConstants.kCloseSpeed);
            intakeRightMotor.set(IntakeConstants.kCloseSpeed);
            intakeDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void stop() {
    intakeLeftMotor.set(0); // Detiene el motor por completo
    intakeRightMotor.set(0);
    }
}
