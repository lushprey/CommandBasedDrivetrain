package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    private final SparkMax rightOut = new SparkMax(20, MotorType.kBrushless);
    private final SparkMax rightIn  = new SparkMax(21, MotorType.kBrushless);
    private final SparkMax leftOut  = new SparkMax(22, MotorType.kBrushless);
    private final SparkMax leftIn   = new SparkMax(23, MotorType.kBrushless);

    private final SparkClosedLoopController controllerRI = rightIn.getClosedLoopController();
    private final SparkClosedLoopController controllerRO = rightOut.getClosedLoopController();
    private final SparkClosedLoopController controllerLI = leftIn.getClosedLoopController();
    private final SparkClosedLoopController controllerLO = leftOut.getClosedLoopController();

    private final Servo rightServo = new Servo(0);
    private final Servo leftServo  = new Servo(1);

    /* ===================== */
    /* === PID (2026) ====== */
    /* ===================== */

    private double kP = 0.0005;
    private double kI = 0.0;
    private double kD = 0.0;

    // Config PID reutilizable
    private final SparkMaxConfig pidConfig = new SparkMaxConfig();

    public ShooterSubsystem() {

        SparkMaxConfig rightOutConfig = new SparkMaxConfig();
        rightOutConfig.idleMode(IdleMode.kBrake);
        rightOut.configure(rightOutConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig rightInConfig = new SparkMaxConfig();
        rightInConfig.idleMode(IdleMode.kBrake);
        rightIn.configure(rightInConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig leftOutConfig = new SparkMaxConfig();
        leftOutConfig.idleMode(IdleMode.kBrake);
        leftOut.configure(leftOutConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig leftInConfig = new SparkMaxConfig();
        leftInConfig.idleMode(IdleMode.kBrake);
        leftIn.configure(leftInConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        rightServo.set(90);
        leftServo.set(90);

        /* Dashboard */
        SmartDashboard.putNumber("Shooter kP", kP);
        SmartDashboard.putNumber("Shooter kI", kI);
        SmartDashboard.putNumber("Shooter kD", kD);

        applyPID();
    }

    @Override
    public void periodic() {
        double newP = SmartDashboard.getNumber("Shooter kP", kP);
        double newI = SmartDashboard.getNumber("Shooter kI", kI);
        double newD = SmartDashboard.getNumber("Shooter kD", kD);

        if (newP != kP || newI != kI || newD != kD) {
            setPID(newP, newI, newD);
        }
    }

    public void setPID(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        applyPID();
    }

    private void applyPID() {
        pidConfig.closedLoop.pid(kP, kI, kD);

        rightIn.configure(pidConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        rightOut.configure(pidConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        leftIn.configure(pidConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        leftOut.configure(pidConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }


    public void setAllRPM(int RPM) {
        controllerRI.setSetpoint(RPM, ControlType.kVelocity);
        controllerRO.setSetpoint(RPM, ControlType.kVelocity);
        controllerLI.setSetpoint(RPM, ControlType.kVelocity);
        controllerLO.setSetpoint(RPM, ControlType.kVelocity);
    }

    public void setAllPower(double power) {
        rightIn.set(power);
        rightOut.set(power);
        leftIn.set(power);
        leftOut.set(power);
    }

    public void setServos(int angle) {
        rightServo.set(angle);
        leftServo.set(angle);
    }
}