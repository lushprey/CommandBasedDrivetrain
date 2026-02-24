package frc.robot.subsystems;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class InsideSubsystem extends SubsystemBase{

    private final SparkMax bands = new SparkMax(55, MotorType.kBrushless);
    private final SparkMax indexer = new SparkMax(55, MotorType.kBrushless);
    
    
    public InsideSubsystem() {
        // Configurar rightFront en modo brake
        SparkMaxConfig bandsConfig = new SparkMaxConfig();
        bandsConfig.idleMode(IdleMode.kBrake);
        bands.configure(bandsConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configurar rightBack en modo brake y seguir a rightFront
        SparkMaxConfig indexerConfig = new SparkMaxConfig();
        indexerConfig.idleMode(IdleMode.kBrake);
        indexer.configure(indexerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);  
    };
    

    @Override
    public void periodic(){

    }
    
    public void powerBands(double power){
        bands.set(power);
    }

    public void powerIndexer(double power){
        indexer.set(power);
    }
}
