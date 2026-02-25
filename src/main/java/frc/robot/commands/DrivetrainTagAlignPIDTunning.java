package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.GenericEntry;

import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;

public class DrivetrainTagAlignPIDTunning extends Command {

    private final DrivetrainSubsystem drivetrainSubsystem;
    private final CameraSubsystem cameraSubsystem;

    // PID
    private final PIDController pidController;

    // Shuffleboard
    private final ShuffleboardTab tab;
    private final GenericEntry kpEntry;
    private final GenericEntry kiEntry;
    private final GenericEntry kdEntry;
    private final GenericEntry toleranceEntry;

    // Configuración
    private static final double TAG_DISTANCE_FROM_CENTER = 59.69; // cm
    private static final double MAX_TURN = 0.8;

    public DrivetrainTagAlignPIDTunning(
            DrivetrainSubsystem drivetrainSubsystem,
            CameraSubsystem cameraSubsystem) {

        this.drivetrainSubsystem = drivetrainSubsystem;
        this.cameraSubsystem = cameraSubsystem;

        // PID inicial (se puede cambiar en vivo)
        pidController = new PIDController(0.5, 0.0, 0.2);

        // Shuffleboard
        tab = Shuffleboard.getTab("Tag Align PID");

        kpEntry = tab.add("kP", 0.02).getEntry();
        kiEntry = tab.add("kI", 0.0).getEntry();
        kdEntry = tab.add("kD", 0.002).getEntry();
        toleranceEntry = tab.add("Tolerance (deg)", 1.0).getEntry();

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        pidController.reset();
        pidController.setTolerance(toleranceEntry.getDouble(1.0));
    }

    @Override
    public void execute() {

        // Leer PID desde Shuffleboard
        double kP = kpEntry.getDouble(0.02);
        double kI = kiEntry.getDouble(0.0);
        double kD = kdEntry.getDouble(0.002);
        double tolerance = toleranceEntry.getDouble(1.0);

        pidController.setPID(kP, kI, kD);
        pidController.setTolerance(tolerance);

        double tx = cameraSubsystem.getTX();        // grados
        double d  = cameraSubsystem.getDistance();  // cm

        // Seguridad
        if (d <= 0) {
            drivetrainSubsystem.arcadeDrive(0, 0);
            return;
        }

        // Compensación por offset lateral del AprilTag
        double angleOffsetDeg =
                Math.toDegrees(Math.atan(TAG_DISTANCE_FROM_CENTER / d));

        double realTX = tx + angleOffsetDeg;

        // PID (queremos llegar a 0 grados)
        double turn = pidController.calculate(realTX, 0);

        // Limitar salida
        turn = MathUtil.clamp(turn, -MAX_TURN, MAX_TURN);

        // Giro puro
        drivetrainSubsystem.arcadeDrive(0, turn);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.arcadeDrive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }
}