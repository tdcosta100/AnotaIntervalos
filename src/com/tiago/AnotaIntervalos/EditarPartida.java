package com.tiago.AnotaIntervalos;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditarPartida extends Activity
{
	private Cursor	 mPartida;
	private Long	 mNumDataHora;
	private int		 mResultado;
	
	//propriedades da partida
	private Long 	 mIdPartida;
	private Long	 mIdAnotacao;
	private Long	 mDataHora;
	private String	 mIdTrem;
	
	//propriedades dos elementos da UI
	private TextView mUIDataHora;
	private EditText mUIIdTrem;
	
	private IntervalosDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_partida);
		
		mDbHelper = new IntervalosDbAdapter(this);
		mDbHelper.open();
		
		mUIDataHora = (TextView) findViewById(R.id.partida_dataHora);
		mUIIdTrem   = (EditText) findViewById(R.id.partida_idTrem);
		
		mUIIdTrem.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				mIdTrem = s.toString();
			}
		});
		
		mResultado = RESULT_FIRST_USER;
		
		if(savedInstanceState != null)
		{
			mIdPartida	= (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.PARTIDAS_IDPARTIDA);
			mIdAnotacao = (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.PARTIDAS_IDANOTACAO);
			mDataHora	= (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.PARTIDAS_HORA);
			mIdTrem		= (String) savedInstanceState.getSerializable(IntervalosDbAdapter.PARTIDAS_IDTREM);
			
			mNumDataHora = mDataHora / 1000;
		}
		else
		{
			mIdPartida 	= null;
			mIdAnotacao = getIntent().getExtras().getLong(IntervalosDbAdapter.PARTIDAS_IDANOTACAO);
			mDataHora	= null;
			mIdTrem		= null;
		}
		
		if(mIdPartida == null)
		{
			Bundle dados = getIntent().getExtras();
			
			if(dados.containsKey(IntervalosDbAdapter.PARTIDAS_IDPARTIDA))
			{
				mIdPartida = dados.getLong(IntervalosDbAdapter.PARTIDAS_IDPARTIDA);
			}
		}
		
		if(mIdAnotacao == null)
		{
			mIdAnotacao = getIntent().getExtras().getLong(IntervalosDbAdapter.PARTIDAS_IDANOTACAO);
		}
		
		preencherCampos();
		
		Button btnConfirmarPartida = (Button) findViewById(R.id.btnConfirmarPartida);
		Button btnCancelarPartida = (Button) findViewById(R.id.btnCancelarPartida);
		
		btnConfirmarPartida.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mResultado = RESULT_OK;
				setResult(RESULT_OK);
				finish();
			}
		});
		
		btnCancelarPartida.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mResultado = RESULT_CANCELED;
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(mResultado == RESULT_OK)
		{
			salvarPartida();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mResultado = RESULT_FIRST_USER;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		if(mResultado == RESULT_OK)
		{
			salvarPartida();
		}
		else
		{
			salvarTemporario(outState);
		}
		
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_IDANOTACAO, mIdPartida);
	}
	
	
	private void preencherCampos()
	{
		if(mIdPartida != null)
		{
			if(mPartida == null)
			{
				mPartida = mDbHelper.fetchPartida(mIdPartida);
				startManagingCursor(mPartida);
			}
			
			if(mDataHora == null)
			{
				mNumDataHora = mPartida.getLong(mPartida.getColumnIndexOrThrow(IntervalosDbAdapter.PARTIDAS_HORA));
				
				mDataHora = mNumDataHora * 1000;
				
				SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
				
				mUIDataHora.setText(formato.format(new Date(mDataHora)));
			}
			
			if(mIdTrem == null)
			{
				mIdTrem = mPartida.getString(mPartida.getColumnIndexOrThrow(IntervalosDbAdapter.PARTIDAS_IDTREM));
			}
			
			mUIIdTrem.setText(mIdTrem);
		}
		else
		{
			mDataHora = new Date().getTime();
			mNumDataHora = mDataHora / 1000;
			
			SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
			
			mUIDataHora.setText(formato.format(new Date(mDataHora)));
		}
	}
	
	
	private void salvarPartida()
	{
		if(mIdPartida == null)
		{
			long idPartida = mDbHelper.insertPartida(mIdAnotacao, mNumDataHora, mIdTrem);
			
			if(idPartida > 0)
			{
				mIdPartida = idPartida;
			}
		}
		else
		{
			mDbHelper.updatePartida(mIdPartida, mIdAnotacao, mNumDataHora, mIdTrem);
		}
	}
	
	
	private void salvarTemporario(Bundle outState)
	{
		outState.putSerializable(IntervalosDbAdapter.PARTIDAS_IDANOTACAO, mIdAnotacao);
		outState.putSerializable(IntervalosDbAdapter.PARTIDAS_HORA, mDataHora);
		outState.putSerializable(IntervalosDbAdapter.PARTIDAS_IDTREM, mIdTrem);
	}
}
