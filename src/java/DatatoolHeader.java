package com.dickimawbooks.datatooltk;

import java.io.*;
import java.util.regex.*;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.dickimawbooks.datatooltk.io.*;
import com.dickimawbooks.datatooltk.gui.*;

public class DatatoolHeader extends TableColumn
{
   public DatatoolHeader()
   {
      this(null);
   }

   public DatatoolHeader(String key)
   {
      this(key, key);
   }

   public DatatoolHeader(String key, String title)
   {
      this(key, title, DatatoolDb.TYPE_UNKNOWN);
   }

   public DatatoolHeader(String key, String title, int type)
   {
      super();
      setKey(key);
      setTitle(title);
      setType(type);
   }

   public int getType()
   {
      return type;
   }

   public String getKey()
   {
      return key;
   }

   public String getTitle()
   {
      return title;
   }

   public void setType(int type)
   {
      if (type < DatatoolDb.TYPE_UNKNOWN || type > DatatoolDb.TYPE_CURRENCY)
      {
         throw new IllegalArgumentException(
            DatatoolTk.getLabelWithValue("error.invalid_data_type", type));
      }

      this.type = type;
   }

   public void setKey(String key)
   {
      this.key = key;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String toString()
   {
      return title;
   }

   public Object clone()
   {
      return new DatatoolHeader(key, title, type);
   }

   public TableCellRenderer getCellRenderer()
   {
      if (type == DatatoolDb.TYPE_UNKNOWN || type == DatatoolDb.TYPE_STRING)
      {
         return STRING_CELL_RENDERER;
      }
      else
      {
         return NUMERICAL_CELL_RENDERER;
      }
   }

   public TableCellEditor getCellEditor()
   {
      return NUMERICAL_CELL_EDITOR;
   }

   public String getHeaderValue()
   {
      return title;
   }

   private String key;
   private String title;
   private int type = DatatoolDb.TYPE_UNKNOWN;

   private static final DbCellRenderer STRING_CELL_RENDERER
      = new DbCellRenderer();

   private static final DbNumericalCellRenderer NUMERICAL_CELL_RENDERER
      = new DbNumericalCellRenderer();

   private static final DbNumericalCellEditor NUMERICAL_CELL_EDITOR 
     = new DbNumericalCellEditor();
}
