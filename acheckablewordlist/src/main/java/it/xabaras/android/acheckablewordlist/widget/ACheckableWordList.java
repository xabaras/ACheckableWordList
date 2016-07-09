package it.xabaras.android.acheckablewordlist.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.acheckablewordlist.R;
import it.xabaras.android.logger.Logger;

/**
 * Created by Paolo Montalto on 07/07/16.
 * Copyright (c) 2016 Paolo Montalto. All rights reserved.
 */

/**
 * This is a simple Android library implementing a (comma) separated word list where each word can be clicked and checked/unchecked.
 */
public class ACheckableWordList extends ViewGroup {
    private int horizontalSpacing;
    private int verticalSpacing;
    private float textSize;
    private ColorStateList textColor;
    private OnItemClickListener mListener;
    private boolean isSelectionEnabled;
    private String separator;

    public ACheckableWordList(Context context) {
        this(context, null);
    }

    public ACheckableWordList(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.aCheckableWordListStyle);
    }

    public ACheckableWordList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ACheckableWordList, defStyleAttr, R.style.ACheckableWordList);
        try {
            horizontalSpacing = (int) a.getDimension(R.styleable.ACheckableWordList_acwl_horizontal_spacing,dp2px(8.0f));
            verticalSpacing = (int) a.getDimension(R.styleable.ACheckableWordList_acwl_vertical_spacing, dp2px(4.0f));
            textColor = a.getColorStateList(R.styleable.ACheckableWordList_acwl_text_color);
            isSelectionEnabled = a.getBoolean(R.styleable.ACheckableWordList_acwl_selection_enabled, false);
            textSize = a.getDimensionPixelSize(R.styleable.ACheckableWordList_acwl_text_size, context.getResources().getDimensionPixelSize(R.dimen.default_text_size));
            separator = a.getString(R.styleable.ACheckableWordList_acwl_separator);
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Register a callback to be invoked when an item in this ACheckableWordList has been clicked.
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Called to determine the size requirements for this view and all of its children.
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            final int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
            final int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            int width = 0;
            int height = 0;

            int row = 0; // The row counter.
            int rowWidth = 0; // Calc the current row width.
            int rowMaxHeight = 0; // Calc the max tag height, in current row.

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                if (child.getVisibility() != GONE) {
                    rowWidth += childWidth;
                    if (rowWidth > widthSize) { // Next line.
                        rowWidth = childWidth; // The next row width.
                        height += rowMaxHeight + verticalSpacing;
                        rowMaxHeight = childHeight; // The next row max height.
                        row++;
                    } else { // This line.
                        rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                    }
                    rowWidth += horizontalSpacing;
                }
            }
            // Account for the last row height.
            height += rowMaxHeight;

            // Account for the padding too.
            height += getPaddingTop() + getPaddingBottom();

            // If the tags grouped in one row, set the width to wrap the tags.
            if (row == 0) {
                width = rowWidth;
                width += getPaddingLeft() + getPaddingRight();
            } else {// If the tags grouped exceed one line, set the width to match the parent.
                width = widthSize;
            }

            setMeasuredDimension(widthMode == View.MeasureSpec.EXACTLY ? widthSize : width,
                    heightMode == View.MeasureSpec.EXACTLY ? heightSize : height);
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Called when this view should assign a size and position to all of its children.
     * @param changed This is a new size or position for this view
     * @param l Left position, relative to parent
     * @param t Top position, relative to parent
     * @param r Right position, relative to parent
     * @param b Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            final int parentLeft = getPaddingLeft();
            final int parentRight = r - l - getPaddingRight();
            final int parentTop = getPaddingTop();
            final int parentBottom = b - t - getPaddingBottom();

            int childLeft = parentLeft;
            int childTop = parentTop;

            int rowMaxHeight = 0;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                if (child.getVisibility() != GONE) {
                    if (childLeft + width > parentRight) { // Next line
                        childLeft = parentLeft;
                        childTop += rowMaxHeight + verticalSpacing;
                        rowMaxHeight = height;
                    } else {
                        rowMaxHeight = Math.max(rowMaxHeight, height);
                    }
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);

                    childLeft += width + horizontalSpacing;
                }
            }
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Convert dp size to pixels
     * @param dp size in dp
     * @return size in pixels
     */
    private float dp2px(float dp) {
        try {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    getResources().getDisplayMetrics());
        } catch(Exception e) {
            Logger.e(this, e);
            return -1;
        }
    }

    /*** Public methods ***/

    /**
     * Interface definition for a callback to be invoked when an item in this ACheckableWordList has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this ACheckableWordList has been clicked.
         * @param v The view within the ACheckableWordList that was clicked
         * @param position The position of the view in the ACheckableWordList.
         * @param isSelected Whether the clicked word is selected or not
         */
        void onItemClick(View v, int position, boolean isSelected);
    }

    /**
     * Initialize ACheckableWordList with the provided words
     * @param words a list of words
     */
    public void setWords(List<String> words) {
        try {
            for ( String s : words )
                addWord(s);
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Add a single word to ACheckableWordList
     * @param word word to be added
     */
    public void addWord(String word) {
        try {
            TextView txt = new TextView(getContext());
            txt.setText(word);
            txt.setTextColor(textColor);
            txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            txt.setTag(getChildCount());
            txt.setClickable(true);
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if ( isSelectionEnabled )
                            v.setSelected(!v.isSelected());

                        if ( mListener != null ) {
                            mListener.onItemClick(v, (int)v.getTag(), v.isSelected());
                        }
                    } catch(Exception e) {
                        Logger.e(this, e);
                    }
                }
            });
            addView(txt);
            if ( separator != null && separator.length() > 0 && getChildCount() > 1 ) {
                TextView txtPrev = (TextView) getChildAt(getChildCount()-2);
                txtPrev.setText(txtPrev.getText().toString() + separator);
            }
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Get the positions of the selected words
     * @return a list of positions
     */
    public List<Integer> getSelection() {
        List<Integer> result = new ArrayList<>();
        try {
            if ( !isSelectionEnabled )
                return result;

            for ( int i=0; i<getChildCount(); i++ ) {
                View v = getChildAt(i);
                if ( v.isSelected() )
                    result.add((Integer) v.getTag());
            }
        } catch(Exception e) {
            Logger.e(this, e);
        }
        return result;
    }

    /**
     * Set selection state of the word at specified position
     * @param position position of the word to be selected/unselected
     * @param selected true if selected, false otherwise
     */
    public void setWordSelected(int position, boolean selected) {
        try {
            View v = getChildAt(position);
            v.setSelected(selected);
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Set all words in ACheckableWordList as unselected
     */
    public void clearSelection() {
        try {
            for ( int i=0; i<getChildCount(); i++ )
                setWordSelected(i, false);
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Get the number of words in the list
     * @return the number of words in ACheckableWordList
     */
    public int getWordCount() {
        try {
            return getChildCount();
        } catch(Exception e) {
            Logger.e(this, e);
            return 0;
        }
    }

    /**
     * Remove word at selected position
     * @param position position of the word to be removed
     */
    public void removeWordAt(int position) {
        try {
            removeViewAt(position);
            int childCount = getChildCount();
            if ( separator != null && separator.length() > 0 && childCount > 1  && position == childCount ) {
                TextView txtPrev = (TextView) getChildAt(getChildCount()-1);
                txtPrev.setText(txtPrev.getText().toString().substring(0, txtPrev.getText().toString().length() -1));
            }
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }

    /**
     * Remove all the words in the ACheckableWordList
     */
    public void removeAllWords() {
        try {
            removeAllViews();
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }
}
