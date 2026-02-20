package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;


public class IntakeSubsystem extends SubsystemBase {
    //Stablishment of Constant for code
    public static final class Constants {
        public static final double kOpenSpeed = 0.7; 
        public static final double kCloseSpeed = -0.7;
        public static final int kLeftMotorPort = 5;
        public static final int kRightMotorPort = 6;
        public static final int kPneumaticHubID = 5;
        public static final int kPneumaticHubID = 5;
        public static final int kSolenoidChannelForward = 0;
        public static final int kSolenoidChannelReverse = 1;
    }

    //Pneumatic Main Properties
    private final PneumaticHub m_pneumaticHub = new PneumaticHub(5);
    private Spark intakeLeftMotor = new Spark(5);
    private Spark intakeRightMotor = new Spark(6);
    private final Compressor m_compressor = new Compressor(5, PneumaticsModuleType.REVPH);
    private final DoubleSolenoid IntakeDoubleSolenoid = new DoubleSolenoid(
        Constants.kPneumaticHubID, 
        PneumaticsModuleType.REVPH, 
        Constants.kForwardChannel, 
        Constants.kReverseChannel
    );

    public IntakeSubsystem() {
        m_compressor.enableAnalog(90, 120);
        IntakeDoubleSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    @Override
    public void periodic() {
    }

    //Intake Functions (open or close)
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

    //Intake Stop
    public void stop() {
    intakeLeftMotor.set(0);
    intakeRightMotor.set(0);
    }
}
