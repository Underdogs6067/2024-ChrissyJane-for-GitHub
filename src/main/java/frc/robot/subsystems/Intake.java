// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  CANSparkMax motor;
  private double m_targetSpeed = 0.0;

  /** Creates a new Elbow. */
  public Intake() {
    motor = new CANSparkMax(23, MotorType.kBrushed);
  }

  @Override
  public void periodic() {
    motor.set(m_targetSpeed);
    // This method will be called once per scheduler run
  }

  public void setTargetSpeed(double targetSpeed) {
    m_targetSpeed = targetSpeed;
  }

  public double getTargetSpeed() {
    return m_targetSpeed;
  }
}
