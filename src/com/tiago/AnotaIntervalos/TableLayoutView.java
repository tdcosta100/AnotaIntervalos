package com.tiago.AnotaIntervalos;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TableLayout;

public class TableLayoutView extends AdapterView<ListAdapter>
{
	Context							mContext;
	TableLayout						mTableLayout;
	ListAdapter						mAdapter;
	LayoutInflater					mInflater;
	DataSetObserver					mDataSetObserver;
	int								mWidthMeasureSpec;
	int								mSelectedPosition;
	AdapterView.OnItemClickListener	mOnItemClickListener;
	ContextMenuInfo					mContextMenuInfo	= null;
	
	private class OnClickListener implements View.OnClickListener
	{
		TableLayoutView	mView;
		int				mPosition;
		
		public OnClickListener(TableLayoutView view, int position)
		{
			mView = view;
			mPosition = position;
		}
		
		@Override
		public void onClick(View v)
		{
			AdapterView.OnItemClickListener listener = getOnItemClickListener();
			
			if (listener != null)
			{
				listener.onItemClick(mView, v, mPosition,
						mAdapter.getItemId(mPosition));
			}
		}
	}
	
	private class OnLongClickListener implements View.OnLongClickListener
	{
		TableLayoutView	mView;
		int				mPosition;
		
		public OnLongClickListener(TableLayoutView view, int position)
		{
			mView = view;
			mPosition = position;
		}
		
		@Override
		public boolean onLongClick(View v)
		{
			mContextMenuInfo = new AdapterView.AdapterContextMenuInfo(v,
					mPosition, mAdapter.getItemId(mPosition));
			mView.showContextMenuForChild(mView);
			return true;
		}
	}
	
	public TableLayoutView(Context context)
	{
		super(context);
		mContext = context;
		init(null);
	}
	
	public TableLayoutView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		init(attrs);
	}
	
	public TableLayoutView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		init(attrs);
	}
	
	private void init(AttributeSet attrs)
	{
		mTableLayout = new TableLayout(mContext);
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mTableLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
	}
	
	public void setColumnCollapsed(int columnIndex, boolean isCollapsed)
	{
		mTableLayout.setColumnCollapsed(columnIndex, isCollapsed);
	}
	
	public void setColumnShrinkable(int columnIndex, boolean isShrinkable)
	{
		mTableLayout.setColumnShrinkable(columnIndex, isShrinkable);
	}
	
	public void setColumnStretchable(int columnIndex, boolean isStretchable)
	{
		mTableLayout.setColumnStretchable(columnIndex, isStretchable);
	}
	
	public void setShrinkAllColumns(boolean shrinkAllColumns)
	{
		mTableLayout.setShrinkAllColumns(shrinkAllColumns);
	}
	
	public void setStretchAllColumns(boolean stretchAllColumns)
	{
		mTableLayout.setStretchAllColumns(stretchAllColumns);
	}
	
	private void addRows()
	{
		int count = mAdapter.getCount();
		
		for (int i = 0; i < count; i++)
		{
			View row = mAdapter.getView(i, null, mTableLayout);
			
			if (row.getLayoutParams() == null)
			{
				row.setLayoutParams(new TableLayout.LayoutParams(
						TableLayout.LayoutParams.FILL_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT));
			}
			
			if (row.getBackground() == null)
			{
				row.setBackgroundResource(android.R.drawable.list_selector_background);
			}
			
			row.setOnClickListener(new OnClickListener(this, i));
			row.setOnLongClickListener(new OnLongClickListener(this, i));
			
			mTableLayout.addView(row);
			
			View divider = new View(mContext);
			
			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(),
					MeasureSpec.EXACTLY);
			int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1,
					MeasureSpec.EXACTLY);
			
			divider.measure(widthMeasureSpec, heightMeasureSpec);
			divider.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
			divider.setEnabled(false);
			
			mTableLayout.addView(divider);
		}
	}
	
	@Override
	public ListAdapter getAdapter()
	{
		return mAdapter;
	}
	
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		if (mAdapter != null)
		{
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		
		mAdapter = adapter;
		
		if (mAdapter != null)
		{
			mDataSetObserver = new DataSetObserver()
			{
				@Override
				public void onChanged()
				{
					int count = mAdapter.getCount();
					
					if (count > 0)
					{
						getEmptyView().setVisibility(View.GONE);
						setVisibility(View.VISIBLE);
					}
					else
					{
						getEmptyView().setVisibility(View.VISIBLE);
						setVisibility(View.GONE);
					}
					
					mTableLayout.removeAllViews();
					addRows();
					requestLayout();
				}
			};
			
			mAdapter.registerDataSetObserver(mDataSetObserver);
			
			if (mAdapter != null)
			{
				int count = mAdapter.getCount();
				
				if (count > 0)
				{
					getEmptyView().setVisibility(View.GONE);
					this.setVisibility(View.VISIBLE);
				}
				else
				{
					getEmptyView().setVisibility(View.VISIBLE);
					this.setVisibility(View.GONE);
				}
				
				mTableLayout.removeAllViews();
				addRows();
			}
			
			requestLayout();
		}
	}
	
	@Override
	public View getSelectedView()
	{
		return mTableLayout.getChildAt(mSelectedPosition);
	}
	
	@Override
	public void setSelection(int position)
	{
		mSelectedPosition = position;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		
		removeAllViewsInLayout();
		
		mTableLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
				MeasureSpec.AT_MOST), MeasureSpec.UNSPECIFIED);
		
		mTableLayout.layout(0, 0, mTableLayout.getMeasuredWidth(),
				mTableLayout.getMeasuredHeight());
		
		addViewInLayout(mTableLayout, -1, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT), false);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		mWidthMeasureSpec = widthMeasureSpec;
		
		mTableLayout.measure(MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
				MeasureSpec.UNSPECIFIED);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = mTableLayout.getMeasuredHeight();
		
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected ContextMenuInfo getContextMenuInfo()
	{
		return mContextMenuInfo;
	}
}
