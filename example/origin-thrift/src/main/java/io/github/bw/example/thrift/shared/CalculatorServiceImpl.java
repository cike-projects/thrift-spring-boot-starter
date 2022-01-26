package io.github.bw.example.thrift.shared;

import io.github.bw.example.shared.SharedStruct;
import io.github.bw.example.tutorial.Calculator;
import io.github.bw.example.tutorial.InvalidOperation;
import io.github.bw.example.tutorial.Work;
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
