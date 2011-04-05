package com.tiago.AnotaIntervalos;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

public class ListarPartidas extends Activity
{
	private static final int PARTIDA_ADICIONAR = 3;
	private static final int PARTIDA_EDITAR = 4;
	
	private TableLayoutView mTableLayoutView;
	private IntervalosDbAdapter mDbHelper;
	private long mIdAnotacao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.lista_partidas);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titulo_lista_partidas);
		
		mDbHelper = new IntervalosDbAdapter(this);
		mDbHelper.open();
		
		mIdAnotacao = getIntent().getExtras().getLong(IntervalosDbAdapter.ANOTACOES_IDANOTACAO);
		
		Cursor anotacao = mDbHelper.fetchAnotacao(mIdAnotacao);
		startManagingCursor(anotacao);
		
		Date dataHora = new Date(anotacao.getLong(anotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_DATAHORA)) * 1000);
		String idLinha = anotacao.getString(anotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_LINHA));
		String estacao = anotacao.getString(anotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_ESTACAO));
		String sentido = anotacao.getString(anotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_SENTIDO));
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		TextView txtDataHora = (TextView) findViewById(R.id.titulo_listaPartidas_dataHora);
		txtDataHora.setText("Horário: " + formato.format(dataHora));
		
		TextView txtLinha = (TextView) findViewById(R.id.titulo_listaPartidas_linha);
		txtLinha.setText("Linha " + idLinha);
		
		TextView txtEstacao = (TextView) findViewById(R.id.titulo_listaPartidas_estacao);
		txtEstacao.setText("Estação " + estacao);
		
		TextView txtSentido = (TextView) findViewById(R.id.titulo_listaPartidas_sentido);
		txtSentido.setText("Sentido " + sentido);
		
		Button btnAdicionarPartida = (Button) findViewById(R.id.btnAdicionarPartida);
		btnAdicionarPartida.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				editarPartida(PARTIDA_ADICIONAR, -1);
			}
		});
		
		TableLayout tableLayout = (TableLayout) findViewById(android.R.id.list);
		
		mTableLayoutView = new TableLayoutView(this, tableLayout, R.layout.linha_partida);
		mTableLayoutView.setEmptyView(findViewById(android.R.id.empty));
		
		mTableLayoutView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				editarPartida(PARTIDA_EDITAR, id);
			}
			
		});
		
		listarPartidas();
	}
	
	
	
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if((requestCode == PARTIDA_ADICIONAR || requestCode == PARTIDA_EDITAR) && resultCode == RESULT_OK)
		{
			listarPartidas();
		}
	}
	
	
	private void editarPartida(int requestCode, long id)
	{
		Intent i = new Intent(this, EditarPartida.class);

		if(requestCode == PARTIDA_EDITAR)
		{
			i.putExtra(IntervalosDbAdapter.PARTIDAS_IDPARTIDA, id);
		}
		
		i.putExtra(IntervalosDbAdapter.PARTIDAS_IDANOTACAO, mIdAnotacao);
		
		startActivityForResult(i, requestCode);
	}
	
	
	
	private void listarPartidas()
	{
		Cursor listaPartidas = mDbHelper.fetchAllPartidas(mIdAnotacao);
		
		String[] origem = new String[] {
				IntervalosDbAdapter.PARTIDAS_IDPARTIDA, // posição
				IntervalosDbAdapter.PARTIDAS_IDTREM,
				IntervalosDbAdapter.PARTIDAS_HORA,
				IntervalosDbAdapter.PARTIDAS_IDPARTIDA //intervalo
		};
			
		int[] destino = new int[] {
			R.id.listaPartidas_posicao,
			R.id.listaPartidas_idTrem,
			R.id.listaPartidas_hora,
			R.id.listaPartidas_intervalo
		};
		
		SimpleCursorAdapter adapterPartidas = new SimpleCursorAdapter(this, R.layout.linha_partida, listaPartidas, origem, destino);
		adapterPartidas.setViewBinder(new PartidasViewBinder());
		
		mTableLayoutView.setAdapter(adapterPartidas);
	}
}
