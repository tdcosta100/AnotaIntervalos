package com.tiago.AnotaIntervalos;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditarAnotacao extends Activity
{
	private Cursor   mAnotacao;
	private Long	 mNumDataHora;
	private IntervalosDbAdapter mDbHelper;
	private int 	 mResultado;
	
	//propriedades da anotação
	private Long 	 mIdAnotacao;
	private Long 	 mDataHora;
	private Long	 mIdLinha;
	private String	 mEstacao;
	private String	 mSentido;
	private Long	 mIdLinhaNovo;
	private String	 mEstacaoNovo;
	private String	 mSentidoNovo;
	
	
	//propriedades dos elementos da UI
	private TextView mUIDataHora;
	private Spinner  mUILinha;
	private Spinner  mUIEstacao;
	private Spinner  mUISentido;
	
	public class AnotacaoOnItemSelectedListener implements OnItemSelectedListener
	{
		private Spinner mSpinner;
		
		public AnotacaoOnItemSelectedListener(Spinner spinner)
		{
			mSpinner = spinner;
		}
		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
		{
			if(mSpinner == mUILinha)
			{
				if(mIdLinhaNovo == null || mIdLinhaNovo != id)
				{
					String[] origem;
					int[] destino;
					
					mIdLinhaNovo = mUILinha.getSelectedItemId();
					
					Cursor estacoes = mDbHelper.fetchAllEstacoes(mUILinha.getSelectedItemId());
					
					origem = new String[] { IntervalosDbAdapter.ESTACOES_DESCRICAO };
					destino = new int[] { android.R.id.text1 };
					
					SimpleCursorAdapter adapterEstacoes = new SimpleCursorAdapter(parent.getContext(), android.R.layout.simple_spinner_item, estacoes, origem, destino);
					mUIEstacao.setAdapter(adapterEstacoes);
					
					Cursor terminais = mDbHelper.fetchAllEstacoesTerminais(mUILinha.getSelectedItemId());
					
					origem = new String[] { IntervalosDbAdapter.ESTACOES_DESCRICAO };
					destino = new int[] { android.R.id.text1 };
					
					SimpleCursorAdapter adapterTerminais = new SimpleCursorAdapter(parent.getContext(), android.R.layout.simple_spinner_item, terminais, origem, destino);
					mUISentido.setAdapter(adapterTerminais);
					
					if(mEstacaoNovo == null || mSentidoNovo == null)
					{
						preencherCampos();
					}
				}
			}
			else if(mSpinner == mUIEstacao)
			{
				Cursor cEstacao = mDbHelper.fetchEstacao(mUIEstacao.getSelectedItemId());
				
				mEstacaoNovo = cEstacao.getString(cEstacao.getColumnIndexOrThrow(IntervalosDbAdapter.ESTACOES_SIGLA));
			}
			else if(mSpinner == mUISentido)
			{
				Cursor cSentido = mDbHelper.fetchEstacao(mUISentido.getSelectedItemId());
				
				mSentidoNovo = cSentido.getString(cSentido.getColumnIndexOrThrow(IntervalosDbAdapter.ESTACOES_SIGLA));
			}
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
			//não faça nada
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_anotacao);
		
		mDbHelper = new IntervalosDbAdapter(this);
		mDbHelper.open();
		
		mUIDataHora  = (TextView) findViewById(R.id.anotacao_dataHora);
		mUILinha 	 = (Spinner)  findViewById(R.id.anotacao_linha);
		mUIEstacao   = (Spinner)  findViewById(R.id.anotacao_estacao);
		mUISentido   = (Spinner)  findViewById(R.id.anotacao_sentido);
		mResultado   = RESULT_FIRST_USER;
		
		
		// Obter os dados de instância já salva
		if(savedInstanceState != null)
		{
			mIdAnotacao = (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.ANOTACOES_IDANOTACAO);
			mDataHora 	= (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.ANOTACOES_DATAHORA);
			mIdLinha 	= (Long)   savedInstanceState.getSerializable(IntervalosDbAdapter.ANOTACOES_LINHA);
			mEstacao 	= (String) savedInstanceState.getSerializable(IntervalosDbAdapter.ANOTACOES_ESTACAO);
			mSentido 	= (String) savedInstanceState.getSerializable(IntervalosDbAdapter.ANOTACOES_SENTIDO);
			
			mNumDataHora = mDataHora / 1000;
		}
		else
		{
			mIdAnotacao = null;
			mDataHora 	= null;
			mIdLinha	= null;
			mEstacao	= null;
			mSentido	= null;
		}
		
		if(mIdAnotacao == null)
		{
			Bundle dados = getIntent().getExtras();
			mIdAnotacao = (dados == null) ? null : dados.getLong(IntervalosDbAdapter.ANOTACOES_IDANOTACAO);
		}
		
		// Preencher o Spinner das linhas
		Cursor linhas = mDbHelper.fetchAllLinhas();
		
		String[] origem = new String[] { IntervalosDbAdapter.LINHAS_DESCRICAO };
		int[] destino = new int[] { android.R.id.text1 };
		
		SimpleCursorAdapter adapterLinhas = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, linhas, origem, destino);
		
		adapterLinhas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mUILinha.setAdapter(adapterLinhas);
		
		// Preencher os campos
		//preencherCampos();
		
		mUILinha.setOnItemSelectedListener(new AnotacaoOnItemSelectedListener(mUILinha));
		mUIEstacao.setOnItemSelectedListener(new AnotacaoOnItemSelectedListener(mUIEstacao));
		mUISentido.setOnItemSelectedListener(new AnotacaoOnItemSelectedListener(mUISentido));
		
		Button btnConfirmarAnotacao = (Button) findViewById(R.id.btnConfirmarAnotacao);
		Button btnCancelarAnotacao = (Button) findViewById(R.id.btnCancelarAnotacao);
		
		btnConfirmarAnotacao.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mResultado = RESULT_OK;
				setResult(RESULT_OK);
				finish();
			}
		});
		
		btnCancelarAnotacao.setOnClickListener(new View.OnClickListener()
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
			salvarAnotacao();
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
			salvarAnotacao();
		}
		else
		{
			salvarTemporario(outState);
		}
		
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_IDANOTACAO, mIdAnotacao);
	}
	
	private void preencherCampos()
	{
		if(mIdAnotacao != null)
		{
			if(mAnotacao == null)
			{
				mAnotacao = mDbHelper.fetchAnotacao(mIdAnotacao);
				startManagingCursor(mAnotacao);
			}
			
			if(mDataHora == null)
			{
				mNumDataHora = mAnotacao.getLong(mAnotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_DATAHORA));
				
				mDataHora = mNumDataHora * 1000;
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				
				mUIDataHora.setText(formato.format(new Date(mDataHora)));
			}
			
			long idLinha;
			
			if(mIdLinha == null)
			{
				idLinha = mAnotacao.getLong(mAnotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_LINHA));
				mIdLinha = idLinha;
			}
			else
			{
				idLinha = mIdLinha;
			}
			
			if(mUILinha.getSelectedItemId() != idLinha)
			{
				int posicao = 0;
				
				while(mUILinha.getItemIdAtPosition(posicao) != idLinha)
				{
					posicao++;
				}
				
				mUILinha.setSelection(posicao, true);
			}
			
			String estacao;
			
			if(mEstacao == null)
			{
				estacao = mAnotacao.getString(mAnotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_ESTACAO));
				mEstacao = estacao;
			}
			else
			{
				estacao = mEstacao;
			}
			
			
			Cursor cEstacao = mDbHelper.fetchEstacao(estacao, idLinha);
			long idEstacao = cEstacao.getLong(cEstacao.getColumnIndexOrThrow(IntervalosDbAdapter.ESTACOES_IDESTACAO));
			
			if(mUIEstacao.getSelectedItemId() != idEstacao)
			{
				int posicao = 0;
				
				while(mUIEstacao.getItemIdAtPosition(posicao) != idEstacao)
				{
					posicao++;
				}
				
				mUIEstacao.setSelection(posicao, true);
			}
			
			String sentido;
			
			if(mSentido == null)
			{
				sentido = mAnotacao.getString(mAnotacao.getColumnIndexOrThrow(IntervalosDbAdapter.ANOTACOES_SENTIDO));
				mSentido = sentido;
			}
			else
			{
				sentido = mSentido;
			}
			
			Cursor cSentido = mDbHelper.fetchEstacao(sentido, idLinha);
			long idSentido = cSentido.getLong(cSentido.getColumnIndexOrThrow(IntervalosDbAdapter.ESTACOES_IDESTACAO));
			
			if(mUISentido.getSelectedItemId() != idSentido)
			{
				int posicao = 0;
				
				while(mUISentido.getItemIdAtPosition(posicao) != idSentido)
				{
					posicao++;
				}
				
				mUISentido.setSelection(posicao, true);
			}
		}
		else
		{
			mDataHora = new Date().getTime();
			mNumDataHora = mDataHora / 1000;
			
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			mUIDataHora.setText(formato.format(new Date(mDataHora)));
		}
	}
	
	
	private void salvarAnotacao()
	{
		mIdLinha = mIdLinhaNovo;
		mEstacao = mEstacaoNovo;
		mSentido = mSentidoNovo;
		
		if(mIdAnotacao == null)
		{
			long idAnotacao = mDbHelper.insertAnotacao(mNumDataHora, mIdLinha.toString(), mEstacao, mSentido);
			
			if(idAnotacao > 0)
			{
				mIdAnotacao = idAnotacao;
			}
		}
		else
		{
			mDbHelper.updateAnotacao(mIdAnotacao, mNumDataHora, mIdLinha.toString(), mEstacao, mSentido);
		}
	}
	
	
	private void salvarTemporario(Bundle outState)
	{
		mIdLinha = mIdLinhaNovo;
		mEstacao = mEstacaoNovo;
		mSentido = mSentidoNovo;
		
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_DATAHORA, mDataHora);
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_LINHA, mIdLinha);
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_ESTACAO, mEstacao);
		outState.putSerializable(IntervalosDbAdapter.ANOTACOES_SENTIDO, mSentido);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mDbHelper.close();
	}
}
