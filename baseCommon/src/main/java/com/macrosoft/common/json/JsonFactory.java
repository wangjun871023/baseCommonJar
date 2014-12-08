package com.macrosoft.common.json;

public final class JsonFactory
{
  private static JsonFactory inst = new JsonFactory();

  public static JsonFactory getInstance()
  {
    return inst;
  }

  public JsonInterface getJsonParser()
  {
    return new JacksonJsonImpl();
  }
}