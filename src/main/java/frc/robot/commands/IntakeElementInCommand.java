package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.algae.AlgaeSubsystem;
import frc.robot.subsystems.coral.CoralSubsystem;

public class IntakeElementInCommand extends ParallelCommandGroup {
    public IntakeElementInCommand(CommandXboxController driver, AlgaeSubsystem algaeSubSystem, CoralSubsystem coralSubsystem) {
        addRequirements(algaeSubSystem.getAlgaeIntake(), coralSubsystem.getCoralIntake());
        addCommands(
            new ConditionalCommand(
                new SequentialCommandGroup(//true
                    algaeSubSystem.setIntakePositionCommand(AlgaeSubsystem.IntakeValue.STOP),
                    new OperatorXboxControllerRumble(driver, RumbleType.kRightRumble,3)
                    // new InstantCommand(
                    //     ()->driver.getHID().setRumble(RumbleType.kBothRumble, 1)
                    // ).withTimeout(3)
                ),
                new SequentialCommandGroup(//false
                    algaeSubSystem.setIntakePositionCommand(AlgaeSubsystem.IntakeValue.INTAKE)
                ),
                algaeSubSystem::isAlgaeIn),
            new ConditionalCommand(
                new SequentialCommandGroup(//true
                    coralSubsystem.setIntakeCommand(CoralSubsystem.CoralIntakeValue.STOP),
                    new OperatorXboxControllerRumble(driver, RumbleType.kLeftRumble,3)
                    // new InstantCommand(
                    //     ()->driver.getHID().setRumble(RumbleType.kBothRumble, 1)
                    // ).withTimeout(3)
                ),
                new SequentialCommandGroup(//false
                    coralSubsystem.setIntakeCommand(CoralSubsystem.CoralIntakeValue.INTAKE)
                ),
                coralSubsystem::isCoralIn)
            
        );
    }
}
