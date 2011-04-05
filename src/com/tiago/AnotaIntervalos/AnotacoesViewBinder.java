package com.tiago.AnotaIntervalos;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class AnotacoesViewBinder implements ViewBinder
{
	
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex)
	{
		TextView tv = (TextView) view;
		
		if(view.getId() == R.id.listaAnotacoes_posicao)
		{
			tv.setText((cursor.getPosition() + 1) + ".");
			return true;
		}
		else if (view.getId() == R.id.listaAnotacoes_dataHora)
		{
			Date dataHora = new Date(cursor.getLong(columnIndex) * 1000);
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			tv.setText(formato.format(dataHora));
			return true;
		}
		else if (view.getId() == R.id.listaAnotacoes_linha)
		{
			tv.setText("Linha " + cursor.getInt(columnIndex));
			return true;
		}
		else if ((view.getId() == R.id.listaAnotacoes_estacao) || (view.getId() == R.id.listaAnotacoes_sentido))
		{
			tv.setText(cursor.getString(columnIndex));
			return true;
		}
		else
		{
			return false;
		}
	}
}
