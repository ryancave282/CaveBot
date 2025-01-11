package frc.robot.subsystems.carriage;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Carriage {
    public enum CoralValue{
        START(0, 0),
        INTAKE_HPS(0, 0),
        INTAKE_GRND(0, 0),
        L1(0, 0),
        L2(0, 0),
        L3(0, 0),
        L4(0, 0);

        private final double armAngle;
        private final double pivotAngle;


        private CoralValue(double armAngle, double pivotAngle){
            this.armAngle = armAngle;
            this.pivotAngle = pivotAngle;
        }
        
        public double getArmAngle(){
            return armAngle;
        }

        public double getPivotAngle(){
            return pivotAngle;
        }
    }

    public enum CoralIntakeValue {
        INTAKE(0),
        OUTTAKE(0),
        STOP(0);

        private final double intakeSpeed;

        private CoralIntakeValue(double intakeSpeed){
            this.intakeSpeed = intakeSpeed;
        }

        public double getIntakeSpeed(){
            return intakeSpeed;
        }
    }

    private final Arm arm;
    private final Pivot pivot;
    private final Intake intake;
    private final DigitalInput coralLimitSwith;

    private CoralValue position = CoralValue.START;

    public Carriage(Arm arm, Pivot pivot, Intake intake){
        this.arm = arm;
        this.pivot = pivot;
        this.intake = intake;
        this.coralLimitSwith = new DigitalInput(0);
    }

    public CoralValue getPosition() {
        return position;
    }
    
    public Command setPositionCommand(CoralValue targetPos) {
        position = targetPos;
        return Commands.sequence(
            switch (targetPos) {
                case START -> 
                    new SequentialCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle())
                    );
                case INTAKE_HPS -> 
                    new SequentialCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle())
                    );
                case INTAKE_GRND ->
                    new SequentialCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle())
                    );
                case L1,L2,L3,L4 -> 
                    new SequentialCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle())
                );                   
                default -> 
                    new ParallelCommandGroup(
                        arm.setTargetPositionCommand(targetPos.getArmAngle()),
                        pivot.setTargetPositionCommand(targetPos.getPivotAngle())
                    );
            }
        );
    }

    public Command setIntakeCommand(CoralIntakeValue intakeValue){
        return Commands.sequence(
            intake.setTargetPositionCommand(intakeValue.getIntakeSpeed())
        );
    }

    public Arm getCoralArm(){
        return arm;
    }

    public Pivot getCoralPivot(){
        return pivot;
    }

    public Intake getCoralIntake(){
        return intake;
    }

    public boolean isCoralIn(){
        return coralLimitSwith.get();
    }
}
