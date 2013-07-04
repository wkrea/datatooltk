package com.dickimawbooks.datatooltk.gui;

import javax.swing.undo.*;

import com.dickimawbooks.datatooltk.*;

public class ReplaceRowEdit extends AbstractUndoableEdit
{
   public ReplaceRowEdit(DatatoolDbPanel panel, 
     int row, DatatoolRow newRow)
   {
      super();
      this.panel = panel;
      this.row = row;

      this.newRow = newRow;
      this.oldRow = panel.db.getRow(row);

      oldTypes = new int[panel.getColumnCount()];

      for (int i = 0, n = panel.getColumnCount(); i < n; i++)
      {
         oldTypes[i] = panel.db.getHeader(i).getType();
      }

      panel.db.replaceRow(row, newRow);
      panel.dataUpdated(false);

      newTypes = new int[panel.getColumnCount()];

      typesChanged = false;

      for (int i = 0, n = panel.getColumnCount(); i < n; i++)
      {
         newTypes[i] = panel.db.getHeader(i).getType();

         if (!typesChanged && i < oldTypes.length)
         {
            typesChanged = (newTypes[i] != oldTypes[i]);
         }
      }

      // Have new columns been inserted?

      if (newTypes.length > oldTypes.length)
      {
         int n = newTypes.length-oldTypes.length;

         newHeaders = new DatatoolHeader[n];

         for (int i = 0; i < n; i++)
         {
            newHeaders[i] = panel.db.getHeader(oldTypes.length+i);
         }
      }
   }

   public boolean canUndo() {return true;}
   public boolean canRedo() {return true;}

   public void undo() throws CannotUndoException
   {
      panel.setModified(true);
      panel.db.replaceRow(row, oldRow);

      if (typesChanged)
      {
         for (int i = 0; i < oldTypes.length; i++)
         {
            panel.db.getHeader(i).setType(oldTypes[i]);
         }
      }

      if (newHeaders != null)
      {
         for (int i = newTypes.length-1; i >= oldTypes.length; i--)
         {
            panel.db.removeColumn(i);
         }
      }

      panel.dataUpdated(false);
   }

   public void redo() throws CannotRedoException
   {
      panel.setModified(true);
      panel.db.replaceRow(row, newRow);

      if (typesChanged)
      {
         for (int i = 0; i < newTypes.length; i++)
         {
            panel.db.getHeader(i).setType(newTypes[i]);
         }
      }

      if (newHeaders != null)
      {
         for (int i = oldTypes.length; i < newTypes.length; i++)
         {
            panel.db.insertColumn(i, newHeaders[i]);
         }
      }

      panel.dataUpdated(false);
   }

   public String getPresentationName()
   {
      return name;
   }

   private int row;
   private int[] oldTypes, newTypes;
   private boolean typesChanged;
   private DatatoolRow newRow, oldRow;
   private static final String name = DatatoolTk.getLabel("undo.replace_row");
   private DatatoolDbPanel panel;

   private DatatoolHeader[] newHeaders;
}