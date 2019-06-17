package com.example.myapplication.Utils;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/** Klasa sprawiająca, że ImageView przyjmuje kształt kwadratu
 *
 */
public class SqaureImageView extends AppCompatImageView {


    /** \brief Konstruktor klasy SqaureImageView
     * @param context
     */
    public SqaureImageView(Context context) {
        super(context);
    }

    /** \brief Konstruktor klasy SqaureImageView
     * @param context
     * @param attrs
     */
    public SqaureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** \brief Konstruktor klasy SqaureImageView
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SqaureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** \brief Konstruktor klasy SqaureImageView
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
