package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.utility.InfoTracker.CycleTracker;

public class ShooterCommands {
    public static SequentialCommandGroup setIntakeAndShooter (){
        return new SequentialCommandGroup(
            // shooter.runOnce(()->shooter.setTargetVelocity(speed)),
            // new WaitCommand(.8),//.6
            // intake.setPositionCommand(intakePos)
        );
    }
}
