package com.tiago.AnotaIntervalos;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class PartidasViewBinder implements ViewBinder
{
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex)
	{
		TextView tv = (TextView) view;
		
		if(view.getId() == R.id.listaPartidas_posicao)
		{
			tv.setText((cursor.getPosition() + 1) + ".");
			return true;
		}
		else if(tv.getId() == R.id.listaPartidas_hora)
		{
			Date hora = new Date(cursor.getLong(columnIndex) * 1000);
			SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
			
			tv.setText(formato.format(hora));
			
			return true;
		}
		else if(tv.getId() == R.id.listaPartidas_intervalo)
		{
			String intervalo = "-";
			
			if(cursor.getPosition() > 0)
			{
				long horaAtual = cursor.getLong(cursor.getColumnIndex(IntervalosDbAdapter.PARTIDAS_HORA));
				
				cursor.move(-1);
				long horaAnterior = cursor.getLong(cursor.getColumnIndex(IntervalosDbAdapter.PARTIDAS_HORA));
				
				long segundos = horaAtual - horaAnterior;
				
				long diferencaSegundos = segundos % 60;
				long diferencaMinutos = segundos / 60;
				
				DecimalFormat formato2 = new DecimalFormat("#00");
				
				intervalo = formato2.format(diferencaMinutos) + ":" + formato2.format(diferencaSegundos);
			}
			
			tv.setText(intervalo);
			
			return true;
		}
		else
		{
			tv.setText(cursor.getString(columnIndex));
			return true;
		}
	}
	
}
