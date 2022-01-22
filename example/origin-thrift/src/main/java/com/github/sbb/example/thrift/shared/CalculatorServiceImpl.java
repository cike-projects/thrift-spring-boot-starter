package com.github.sbb.example.thrift.shared;

import com.github.sbb.example.shared.SharedStruct;
import com.github.sbb.example.tutorial.Calculator;
import com.github.sbb.example.tutorial.InvalidOperation;
import com.github.sbb.example.tutorial.Work;
import org.apache.thrift.TException;

public class CalculatorServiceImpl implements Calculator.Iface {

  @Override
  public SharedStruct getStruct(int key) throws TException {
    return new SharedStruct(key, "CalculatorService" + key);
  }

  @Override
  public void ping() throws TException {

  }

  @Override
  public int add(int num1, int num2) throws TException {
    return num1 + num2;
  }

  @Override
  public int calculate(int logid, Work w) throws InvalidOperation, TException {
    return 0;
  }

  @Override
  public void zip() throws TException {

  }
}
