package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterAlign extends Command{
    private ShooterSubsystem shooterSubsystem;
    private CameraSubsystem cameraSubsystem;

    // mx + b
    private static final double RPM_M = 22;
    private static final double RPM_B = 22;

    // A sin(Bx + C) + D
    private static final double HOOD_A = 22;
    private static final double HOOD_B = 22;
    private static final double HOOD_C = 22;
    private static final double HOOD_D = 22;

    public ShooterAlign(ShooterSubsystem shooterSubsystem, CameraSubsystem cameraSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.cameraSubsystem = cameraSubsystem;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        if (cameraSubsystem.getCurrentID() == -1) {
            return;
        }

        //d en metros
        double d = cameraSubsystem.getDistance();

        int finalRPM = (int)(RPM_M * d + RPM_B);
        finalRPM = MathUtil.clamp(finalRPM, 0, 6000);

        double angleRad = HOOD_B * d + HOOD_C;
        int finalDegrees = (int)(HOOD_A * Math.sin(angleRad) + HOOD_D);
        finalDegrees = MathUtil.clamp(finalDegrees, 0, 180);

        shooterSubsystem.setAllRPM(finalRPM);
        shooterSubsystem.setServos(finalDegrees);
    }

    public boolean isFinished() {
        return false;
    }


}
