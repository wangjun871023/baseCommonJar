package com.macrosoft.common.json;

/**
 * JSON工具类工厂
 * @author 呆呆
 *
 */
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