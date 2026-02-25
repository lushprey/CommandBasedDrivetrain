package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;

public class DrivetrainTagAlign extends Command {

    private final DrivetrainSubsystem drivetrainSubsystem;
    private final CameraSubsystem cameraSubsystem;

    private final double kp = 0.5;
    private final double ki = 0;
    private final double kd = 0.2;

    private final double target = 0;

    private final PIDController pidController;
    private final double tolerance = 1.0;

    private final double tagDistanceFromCenter = 59.69;

    private static final double MAX_TURN = 0.8;

    public DrivetrainTagAlign(
            DrivetrainSubsystem drivetrainSubsystem,
            CameraSubsystem cameraSubsystem) {

        this.drivetrainSubsystem = drivetrainSubsystem;
        this.cameraSubsystem = cameraSubsystem;
        pidController = new PIDController(kp, ki, kd);
        pidController.setTolerance(tolerance);

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        pidController.reset();
    }

    @Override
    public void execute() {

        double tx = cameraSubsystem.getTX();        // grados
        double d  = cameraSubsystem.getDistance();  // cm

        // Evita división entre cero
        if (d <= 0) {
            drivetrainSubsystem.arcadeDrive(0, 0);
            return;
        }

        // Compensación por offset lateral del tag
        double angleOffsetDeg =
                Math.toDegrees(Math.atan(tagDistanceFromCenter / d));

        double realTX = tx + angleOffsetDeg;

        // PID: queremos llegar a 0 grados
        double turn = pidController.calculate(realTX, target);

        // 🔹 CLAMP con MAX_TURN = 0.8
        turn = MathUtil.clamp(turn, -MAX_TURN, MAX_TURN);

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