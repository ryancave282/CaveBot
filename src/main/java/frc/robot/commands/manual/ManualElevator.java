package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.Elevator;

public class ManualElevator extends Command {
    private final Elevator elevator;
    private final Supplier<Double> elevatorMove;
    
    //driver::getLeftX
    public ManualElevator(Elevator elevator, Supplier<Double> elevatorMove) {
        this.elevator = elevator;
        this.elevatorMove = elevatorMove;
        
        addRequirements(elevator);
    }

//     @Override
//     public void initialize() { }

    @Override
    public void execute() {
        // if(!isClimbing){
        //     if(climb.getEncoderPosition() > 15){
        //         isClimbing = true;
        //     }
        // }
        // if(isClimbing){
        //     intake.setPositionCommand(Intake.Value.CLIMB);
        // }
        double move = -elevatorMove.get();
        move = DroidRageConstants.squareInput(move);
        move = DroidRageConstants.applyDeadBand(move);
        // climb.setPower(move*1);
        // climb.setTargetPosition(climb.getTargetPosition() + move * 0.4);//For Motor
        elevator.setTargetPosition(elevator.getTargetPosition() + move * 0.05);//For Encoder

        // elevator.setMovingManually(!(move == 0));
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
