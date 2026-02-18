package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkClosedLoopController;

public class ShooterSubsystem extends SubsystemBase{

    private final SparkMax rightOut = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax rightIn = new SparkMax(2, MotorType.kBrushless);
    private final SparkMax leftOut = new SparkMax(3, MotorType.kBrushless);
    private final SparkMax leftIn = new SparkMax(4, MotorType.kBrushless);

    private final SparkClosedLoopController controllerRI = rightIn.getClosedLoopController();
    private final SparkClosedLoopController controllerRO = rightOut.getClosedLoopController();
    private final SparkClosedLoopController controllerLI = leftIn.getClosedLoopController();
    private final SparkClosedLoopController controllerLO = leftOut.getClosedLoopController();
    
    public ShooterSubsystem() {
        // Configurar rightFront en modo brake
        SparkMaxConfig rightOutConfig = new SparkMaxConfig();
        rightOutConfig.idleMode(IdleMode.kBrake);
        rightOut.configure(rightOutConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar rightBack en modo brake y seguir a rightFront
        SparkMaxConfig rightInConfig = new SparkMaxConfig();
        rightInConfig.idleMode(IdleMode.kBrake);
        rightIn.configure(rightInConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);  
        
        // Configurar leftFront en modo brake
        SparkMaxConfig leftOutConfig = new SparkMaxConfig();
        leftOutConfig.idleMode(IdleMode.kBrake);
        leftOut.configure(leftOutConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar leftBack en modo brake y seguir a leftFront
        SparkMaxConfig leftInConfig = new SparkMaxConfig();
        leftInConfig.idleMode(IdleMode.kBrake);
        leftIn.configure(leftInConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    };
    

    @Override
    public void periodic(){

    }

    public void setAllRPM(int RPM){
        controllerRI.setSetpoint(RPM, ControlType.kVelocity);
        controllerRO.setSetpoint(RPM, ControlType.kVelocity);
        controllerLI.setSetpoint(RPM, ControlType.kVelocity);
        controllerLO.setSetpoint(RPM, ControlType.kVelocity);
    }

    public void setAllPower(double power){
        rightIn.set(power);
        rightOut.set(power);
        leftIn.set(power);
        leftOut.set(power);
    }
}
