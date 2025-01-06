package frc.robot.subsystems.algae;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AlgaeSubsystem {
    public enum ArmValue {
        START(0),
        GROUND(0),
        HIGH(0),
        LOW(0),
        PROCESSOR(0),
        SHOOT(0);

        private final double algaeArmPos;
        private ArmValue(double algaeArmPos) {
            this.algaeArmPos = algaeArmPos;
    
        }

        public double getAlgaeArmPos() {
            return algaeArmPos;
        }
    }

    public enum IntakeValue{
        START(0,0),
        INTAKE(0,0),
        OUTTAKE(0,0),
        STOP(0,0);
    
        private final double algaeIntakeSpeed;
        private final double algaeShooterSpeed;
        private IntakeValue(double algaeIntakeSpeed, double algaeShooterSpeed) {
        this.algaeIntakeSpeed = algaeIntakeSpeed;
        this.algaeShooterSpeed = algaeShooterSpeed;
    }

    public double getAlgaeIntakeSpeed() {
        return algaeIntakeSpeed;
        }
    public double getAlgaeShooterSpeed() {
        return algaeShooterSpeed;
        }
    }

    private final AlgaeArm algaeArm;
    private final AlgaeIntake algaeIntake;
    private final AlgaeShooter algaeShooter;
    private final DigitalInput algaeLimitSwitch;

    public AlgaeSubsystem (AlgaeArm algaeArm, AlgaeIntake algaeIntake, AlgaeShooter algaeShooter) {
        this.algaeArm = algaeArm;
        this.algaeIntake = algaeIntake;
        this.algaeShooter = algaeShooter;
        this.algaeLimitSwitch = new DigitalInput(0);
    }

    public Command setPositionCommand(ArmValue targetPosition) {
        return switch (targetPosition) {
            case START, PROCESSOR, SHOOT -> 
                new SequentialCommandGroup(
                    armAlgae.setTargetPositionCommand(targetPosition.getArmAlgaePos())
                );
            case GROUND, HIGH, LOW ->
            new SequentialCommandGroup(
                algaeArm.setTargetPositionCommand(targetPosition.getAlgaeArmPos())
                );

            default -> new SequentialCommandGroup(
                algaeArm.setTargetPositionCommand(targetPosition.getAlgaeArmPos())
                );
        };
    }
    
    public Command setIntakePositionCommand(IntakeValue targetPosition) {
        return switch (targetPosition){
            case INTAKE-> new SequentialCommandGroup(
                algaeIntake.setTargetPositionCommand(targetPosition.getAlgaeIntakeSpeed()),
                algaeShooter.setTargetPositionCommand(targetPosition.getAlgaeShooterSpeed())        
            );
            case OUTTAKE-> new SequentialCommandGroup(    
                algaeIntake.setTargetPositionCommand(targetPosition.getAlgaeIntakeSpeed()),
                algaeShooter.setTargetPositionCommand(targetPosition.getAlgaeShooterSpeed())        
            );
            default -> new SequentialCommandGroup(
                algaeIntake.setTargetPositionCommand(targetPosition.getAlgaeIntakeSpeed()),
                algaeShooter.setTargetPositionCommand(targetPosition.getAlgaeShooterSpeed())
            );
        };
    }   

    public AlgaeArm getAlgaeArm() {
        return algaeArm;
    }

    public AlgaeIntake getAlgaeIntake() {
        return algaeIntake;
    }

    public AlgaeShooter getAlgaeShooter() {
        return algaeShooter;
    }
    
    public boolean isAlgaeIn() {
        return algaeLimitSwitch.get();
    }

}    
    