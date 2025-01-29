// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
  CANcoder encoder;
  CANSparkMax motor;
  private double m_targetSpeed = 0.0;
  private double m_armTargetPos = 0.0;
  private CANcoder m_CANCoder;
  private PIDController pidController;

  /** Creates a new Elbow. */
  public Arm() {
    motor = new CANSparkMax(22, MotorType.kBrushed);
    m_CANCoder = new CANcoder(14);
    pidController = new PIDController(75, 0, 0);
    pidController.enableContinuousInput(-0.5, 0.5);
  }

  @Override
  public void periodic() {
    // setTargetSpeed(m_targetSpeed);
    motor.set(m_targetSpeed);
    // This method will be called once per scheduler run

  }

  public void setTargetSpeed(double targetSpeed) {
    m_targetSpeed = targetSpeed;
  }

  public double getTargetSpeed() {
    return m_targetSpeed;
  }

  // public void down(){
  //   m_armTargetPos = -0.495;
  // }

  // public void up(){
  //   m_armTargetPos = -0.312;
  // }

  public void setArmAngle(double setArmAngle) {
    double pidSpeed =
        pidController.calculate(m_CANCoder.getAbsolutePosition().refresh().getValue(), setArmAngle);
    m_targetSpeed = pidSpeed;
  }
}
