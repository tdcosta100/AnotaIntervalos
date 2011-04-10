package com.tiago.AnotaIntervalos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;

public class TableLayoutView_Old extends AbsListView
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private TableLayout mTableLayout;
	private int mTableRowResource;
	private View mEmpty;
	private ListAdapter mAdapter;
	private AdapterView.OnItemClickListener mOnItemClickListener;
	private View.OnCreateContextMenuListener mOnCreateContextMenuListener;
	private ContextMenuInfo mContextMenuInfo;
	
	public class OnItemClickListener implements View.OnClickListener
	{
		private TableLayoutView_Old mListener;
		private int mPosition;
		
		public OnItemClickListener(TableLayoutView_Old listener, int position)
		{
			mListener = listener;
			mPosition = position;
		}
		
		@Override
		public void onClick(View v)
		{
			if(mOnItemClickListener != null)
			{
				mOnItemClickListener.onItemClick(mListener, v, mPosition, mAdapter.getItemId(mPosition));
			}
		}
	}
	
	
	public class OnCreateContextMenuItemListener implements View.OnCreateContextMenuListener
	{
		private TableLayoutView_Old mListener;
		private int mPosition;
		
		public OnCreateContextMenuItemListener(TableLayoutView_Old listener, int position)
		{
			mListener = listener;
			mPosition = position;
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
		{
			AdapterContextMenuInfo itemMenuInfo = new AdapterContextMenuInfo(v, mPosition, mAdapter.getItemId(mPosition));
			
			mListener.setContextMenuInfo(itemMenuInfo);
			if(mListener.mOnCreateContextMenuListener != null)
				mListener.mOnCreateContextMenuListener.onCreateContextMenu(menu, v, itemMenuInfo);
		}
	}
	
	
	public TableLayoutView_Old(Context context)
	{
		this(context, null, -1, null);
	}
	
	
	public TableLayoutView_Old(Context context, TableLayout tableLayout, int tableRowResource)
	{
		this(context, tableLayout, tableRowResource, null);
	}
	
	
	public TableLayoutView_Old(Context context, TableLayout tableLayout, int tableRowResource, View empty)
	{
		this(context, tableLayout, tableRowResource, empty, null);
	}
	
	public TableLayoutView_Old(Context context, TableLayout tableLayout, int tableRowResource, View empty, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		mTableLayout = tableLayout;
		mTableRowResource = tableRowResource;
		mEmpty = empty;
		inicializarTableLayoutView();
	}
	
	public TableLayoutView_Old(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		inicializarTableLayoutView();
	}
	
	private void inicializarTableLayoutView()
	{
		if(mTableLayout != null)
		{
			mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}
	
	private void montarLayout()
	{
		int numLinhas = mAdapter.getCount();
		mTableLayout.removeAllViews();
		
		if(mEmpty == null)
		{
			mEmpty = this.getEmptyView();
		}
		
		if(numLinhas > 0)
		{
			if(mEmpty != null)
			{
				mEmpty.setVisibility(View.GONE);
			}
			
			mTableLayout.setVisibility(View.VISIBLE);
			
			for (int position = 0; position < numLinhas; position++)
			{
				TableRow convertView = (TableRow) mLayoutInflater.inflate(mTableRowResource, mTableLayout, false);
				
				if(convertView.getBackground() == null)
				{
					convertView.setBackgroundResource(android.R.drawable.list_selector_background);
				}
				convertView.setOnClickListener(new OnItemClickListener(this, position));
				convertView.setOnCreateContextMenuListener(new OnCreateContextMenuItemListener(this, position));
				
				mTableLayout.addView(mAdapter.getView(position, convertView, mTableLayout));
				
				View divider = mLayoutInflater.inflate(R.layout.divider_tablelayout, mTableLayout);
				divider.setEnabled(false);
			}
		}
		else if(mEmpty != null)
		{
			mTableLayout.setVisibility(View.GONE);
			mEmpty.setVisibility(View.VISIBLE);
		}
	}
	
	public void setTableLayout(TableLayout tableLayout)
	{
		mTableLayout = tableLayout;
		
		inicializarTableLayoutView();
	}
	
	public TableLayout getTableLayout()
	{
		return mTableLayout;
	}
	
	@Override
	public ListAdapter getAdapter()
	{
		return mAdapter;
	}
	
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		mAdapter = adapter;
		
		if(mAdapter != null)
		{
			montarLayout();
		}
	}
	
	@Override
	public void setSelection(int position)
	{
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener)
	{
		super.setOnItemClickListener(listener);
		mOnItemClickListener = listener;
	}
	
	
	@Override
	protected ContextMenuInfo getContextMenuInfo()
	{
		return mContextMenuInfo;
	}
	
	
	private void setContextMenuInfo(ContextMenuInfo menuInfo)
	{
		mContextMenuInfo = menuInfo;
	}


	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener l)
	{
		mOnCreateContextMenuListener = l;
	}
}
