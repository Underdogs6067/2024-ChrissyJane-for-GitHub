// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  CANSparkMax rightMotor;
  CANSparkMax leftMotor;
  private double m_targetSpeed = 0.0;

  /** Creates a new Arm. */
  public Shooter() {
    rightMotor = new CANSparkMax(21, MotorType.kBrushed);
    leftMotor = new CANSparkMax(20, MotorType.kBrushed);
  }

  @Override
  public void periodic() {
    rightMotor.set(m_targetSpeed);
    leftMotor.set(-m_targetSpeed);

    // This method will be called once per scheduler run
  }

  public void setTargetSpeed(double targetSpeed) {
    m_targetSpeed = targetSpeed;
  }

  public double getTargetSpeed() {
    return m_targetSpeed;
  }
}
