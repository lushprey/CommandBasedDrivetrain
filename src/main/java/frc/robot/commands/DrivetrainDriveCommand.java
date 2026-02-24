package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.Joystick;

public class DrivetrainDriveCommand extends Command {
    private final Joystick stick;
    private final DrivetrainSubsystem drivetrainSubsystem;
    
    public DrivetrainDriveCommand(DrivetrainSubsystem drivetrainSubsystem, Joystick stick){
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.stick = stick;
        addRequirements(drivetrainSubsystem);
    }
    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
    // Obtener valores de los sticks
    double forward = -stick.getRawAxis(1);  // Stick izquierdo Y (adelante/atrás)
    double turn = stick.getRawAxis(4);      // Stick derecho X (giro)
    
    // Obtener valores de los triggers
    double rightTrigger = stick.getRawAxis(3);  // Trigger derecho trwtret
    double leftTrigger = stick.getRawAxis(2);   // Trigger izquierdo
    
    // Determinar el multiplicador de velocidad basado en los triggers
    double speedMultiplier = 0.6;  // Velocidad por defecto: 60%
    
    if (rightTrigger > 0.1) {
      speedMultiplier = 0.9;  // Trigger derecho: 100%
    } else if (leftTrigger > 0.1) {
      speedMultiplier = 0.3;  // Trigger izquierdo: 30%
    }
    
    // Aplicar el multiplicador de velocidad
    forward *= speedMultiplier;
    turn *= speedMultiplier;
    drivetrainSubsystem.arcadeDrive(turn, forward);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
