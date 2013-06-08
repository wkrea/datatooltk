package com.dickimawbooks.datatooltk.io;

public class DatatoolExportException extends Exception
{
   public DatatoolExportException(String message)
   {
      super(message);
   }

   public DatatoolExportException(String message, Exception exception)
   {
      super(message, exception);
   }

   public DatatoolExportException(Exception exception)
   {
      super(exception);
   }
}