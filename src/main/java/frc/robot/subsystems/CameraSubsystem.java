package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;

public class CameraSubsystem extends SubsystemBase{

    private double hT = 1.45;      // altura del tag, metros
    private double hR = 0.75;      // altura de la cámara, metros
    private double mountAngle = 25.0; // grados

    private double ty;
    private double tx;
    private int currentID;
    private double d; //metros

    private String name = "limelight";

    public CameraSubsystem() {
        LimelightHelpers
         .SetFiducialIDFiltersOverride(name, new int[]{9,25});
    }

    @Override
    public void periodic(){
        if (!LimelightHelpers.getTV(name)) {
            return;
        }    
        currentID = (int) LimelightHelpers.getFiducialID(name);
        ty = LimelightHelpers.getTY(name);
        tx = LimelightHelpers.getTX(name);

        double angleDeg = mountAngle + ty;
        if (Math.abs(angleDeg) < 1.0) {
            return;
        }
        double angleRad = Math.toRadians(angleDeg);

        d = (hT - hR) / Math.tan(angleRad);
    }

    public double getDistance(){
        return d;
    }

    public double getTX(){
        return tx;
    }

    public int getCurrentID(){
        return currentID;
    }
}
