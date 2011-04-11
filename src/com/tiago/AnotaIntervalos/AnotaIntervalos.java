package com.tiago.AnotaIntervalos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;

public class AnotaIntervalos extends Activity
{
	private static final int	ANOTACAO_ADICIONAR	= 0;
	private static final int	ANOTACAO_EDITAR		= 1;
	private static final int	PARTIDAS_LISTAR		= 2;
	
	private static final int	EDIT_ID				= Menu.FIRST;
	private static final int	DELETE_ID			= Menu.FIRST + 1;
	
	private TableLayoutView		mTableLayoutView;
	private IntervalosDbAdapter	mDbHelper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mDbHelper = new IntervalosDbAdapter(this);
		mDbHelper.open();
		
		mTableLayoutView = (TableLayoutView) findViewById(android.R.id.list);
		mTableLayoutView.setEmptyView(findViewById(android.R.id.empty));
		mTableLayoutView.setColumnStretchable(1, true);
		
		mTableLayoutView
				.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id)
					{
						listarPartidas(PARTIDAS_LISTAR, id);
					}
				});
		
		Button btnAdicionarAnotacao = (Button) findViewById(R.id.btnAdicionarAnotacao);
		btnAdicionarAnotacao.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				editarAnotacao(ANOTACAO_ADICIONAR, -1);
			}
		});
		
		registerForContextMenu(mTableLayoutView);
		
		listarAnotacoes();
	}
	
	private void listarAnotacoes()
	{
		Cursor listaAnotacoes = mDbHelper.fetchAllAnotacoes();
		startManagingCursor(listaAnotacoes);
		
		String[] origem = new String[] {
				IntervalosDbAdapter.ANOTACOES_IDANOTACAO,
				IntervalosDbAdapter.ANOTACOES_DATAHORA,
				IntervalosDbAdapter.ANOTACOES_LINHA,
				IntervalosDbAdapter.ANOTACOES_ESTACAO,
				IntervalosDbAdapter.ANOTACOES_SENTIDO };
		
		int[] destino = new int[] { R.id.listaAnotacoes_posicao,
				R.id.listaAnotacoes_dataHora, R.id.listaAnotacoes_linha,
				R.id.listaAnotacoes_estacao, R.id.listaAnotacoes_sentido };
		
		SimpleCursorAdapter adapterAnotacoes = (SimpleCursorAdapter) mTableLayoutView
				.getAdapter();
		
		if (adapterAnotacoes == null)
		{
			adapterAnotacoes = new SimpleCursorAdapter(this,
					R.layout.linha_anotacao, listaAnotacoes, origem, destino);
			adapterAnotacoes.setViewBinder(new AnotacoesViewBinder());
			
			mTableLayoutView.setAdapter(adapterAnotacoes);
		}
		else
		{
			adapterAnotacoes.changeCursor(listaAnotacoes);
		}
	}
	
	protected void editarAnotacao(int requestCode, long id)
	{
		Intent i = new Intent(this, EditarAnotacao.class);
		
		if (requestCode == ANOTACAO_EDITAR)
		{
			i.putExtra(IntervalosDbAdapter.ANOTACOES_IDANOTACAO, id);
		}
		
		startActivityForResult(i, requestCode);
	}
	
	protected void listarPartidas(int requestCode, long id)
	{
		Intent i = new Intent(this, ListarPartidas.class);
		i.putExtra(IntervalosDbAdapter.PARTIDAS_IDANOTACAO, id);
		
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if ((requestCode == ANOTACAO_ADICIONAR || requestCode == ANOTACAO_EDITAR)
				&& resultCode == RESULT_OK)
		{
			listarAnotacoes();
		}
	}
	
	/*
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) { super.onListItemClick(l, v, position, id);
	 * 
	 * listarPartidas(PARTIDAS_LISTAR, id); }
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle(R.string.anotacoes_contextMenu);
		menu.add(0, EDIT_ID, 0, R.string.anotacoes_editar);
		menu.add(0, DELETE_ID, 1, R.string.anotacoes_remover);
	}
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case EDIT_ID:
			{
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
						.getMenuInfo();
				editarAnotacao(ANOTACAO_EDITAR, info.id);
				return true;
			}
				
			case DELETE_ID:
			{
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
						.getMenuInfo();
				mDbHelper.deleteAnotacao(info.id);
				listarAnotacoes();
				return true;
			}
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mDbHelper.close();
	}
}
